var lastPage = 0;
var interval = 5 * 1000;
var refreshIntervalId = null;

$(function () {
    setWaiting(true);
    refreshIntervalId = setInterval(requestForDataByCheckBox, interval);
    requestForData()
    updatePageNum();
    setWaiting(false);
});

function requestForDataByCheckBox() {
    var autoReloadCheckbox = document.getElementById('auto_reload');
    if (autoReloadCheckbox.checked) {
        requestForData();
    }
}

function requestNewData() {
    setWaiting(true);
    requestForData();
}

function requestForData() {
    $.ajax({
        type: "GET",
        url: "/k-processes/page/" + lastPage,
        contentType: false,
        processData: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
            insertData(JSON.parse(data));
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

function insertData(json) {
    var table = document.getElementById('uuid_table');
    for (var i = 0; i < json.length; i++) {
        insertProcessInfo(table, json[i], null)
    }
    setWaiting(false);
}

function insertProcessInfo(table, data, className) {
    var hasChildren = data.hasOwnProperty('children');
    if (table.rows.length > 1) {  // 1 because of header row
        for (let i = 1; i < table.rows.length; i++) {
            const uuid = table.rows[i].cells[0].textContent;
            if (uuid === data.uuid) {
                const cells = table.rows[i].cells;
                cells[2].innerHTML = data.name
                cells[2].title = data.name;
                cells[3].innerHTML = data.batchState === 'NO_BATCH'? data.state : data.batchState;
                cells[3].style.color = colorByState(data.state);
                cells[5].innerHTML = data.started;
                cells[6].innerHTML = data.finished;
                if (hasChildren) {
                    let children = data.children;
                    for (let j = 0; j < children.length; j++) {
                        insertProcessInfo(table, children[j], 'child ' + data.uuid);
                    }
                }
                return;
            }
        }
    }
    var row = table.getElementsByTagName('tbody')[0].insertRow(-1);
    row.className = className;

    insertCells(row, data);
    fillCells(row.cells, data);

    if (hasChildren) {
        row.className = 'parent';
        var children = data.children;
        for (var j = 0; j < children.length; j++) {
            insertProcessInfo(table, children[j], 'child ' + data.uuid);
        }
    }
}

function insertCells(row, data) {
    var cell = row.insertCell(0);
    cell.style.display = 'none';

    row.insertCell(1);
    row.insertCell(2);

    cell = row.insertCell(3);
    cell.style.color = colorByState(data.state);

    row.insertCell(4);
    row.insertCell(5);
    row.insertCell(6);

    cell = row.insertCell(7);
    setOperationButtons(cell, data);
}

function fillCells(cells, data) {
    cells[0].innerHTML = data.uuid;
    cells[1].innerHTML = data.def;
    cells[2].innerHTML = data.name;
    cells[2].title = data.name;
    cells[3].innerHTML = data.batchState === 'NO_BATCH'? data.state : data.batchState;
    cells[4].innerHTML = data.planned;
    cells[5].innerHTML = data.started;
    cells[6].innerHTML = data.finished;
}

function setOperationButtons(cell, data) {

    var checkbox = document.createElement('div');
    checkbox.className = 'form-check';
    checkbox.style = 'display: inline';
    var checkinput = document.createElement('input');
    checkinput.className = 'form-check-input process-check';
    checkinput.type = 'checkbox';
    checkinput.name = 'radio';
    checkbox.appendChild(checkinput);
    cell.appendChild(checkbox);

    var btn = document.createElement('button');
    btn.className = 'kill_btn';
    btn.title = 'Zastavit';
    btn.setAttribute( "onClick", "javascript: operate(this, 'kill');" );
    cell.appendChild(btn);

    btn = document.createElement('button');
    btn.className = 'remove_btn';
    btn.title = 'Smazat';
    btn.setAttribute( "onClick", "javascript: operate(this, 'remove');" );
    cell.appendChild(btn);

    btn = document.createElement('button');
    btn.className = 'logs_btn';
    btn.title = 'Logy';
    btn.setAttribute( "onClick", "window.open('" + data.logUrl + "')");
    cell.appendChild(btn);
}

function operate(element, action) {
    var table = document.getElementById('uuid_table');
    var i = element.parentNode.parentNode.rowIndex;
    var pid = table.rows[i].cells[0].textContent;
    sendCommand(pid, action)
    if (action === 'remove') {
        table.deleteRow(i);
    }
}

function sendCommand(pid, action) {
    const paramsStr = {
        'pid': pid,
        'action': action
    }
    const formData = new FormData();
    const params = new Blob([JSON.stringify(paramsStr)], {type : "application/json"})
    formData.append('params', params)

    $.ajax({
        type: "POST",
        url: "/k-processes/command",
        data: formData,
        contentType: false,
        processData: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

function clearTable() {
    var table = document.getElementById('uuid_table');
    var rowCount = table.rows.length;
    for(var i = rowCount-1; i > 0; i--) {   // -2 to dont remove table header
        table.deleteRow(i);
    }
}

function setWaiting(set) {
    showSpin(set);
    var table = document.getElementById('uuid_table');
    if (set) {
        table.style.opacity = "0.5";
    } else {
        table.style.opacity = "1.0";
    }
}

function showSpin(show) {
    var spin = document.getElementById("wait-spin");
    showElement(spin, show);
}

function showElement(element, show) {
    if (show) {
        element.style.display = "block";
    } else {
        element.style.display = "none";
    }
}

function previousProcessPage() {
    if (lastPage > 0) {
        lastPage--;
        clearTable();
        requestNewData();
        updatePageNum();
    }
}

function nextProcessPage() {
    lastPage++;
    clearTable();
    requestNewData();
    updatePageNum();
}

function updatePageNum() {
    document.getElementById('page_num').value = (lastPage+1).toString(10); // +1 because lastPage is counting from 0
}

function goToPage(page) {
    lastPage = parseInt(page, 10)-1;  // -1 because lastPage is counting from 0
    clearTable();
    requestNewData();
}

function updateSelect(element) {
    var checked = element.checked;
    var checkBoxes = document.getElementsByClassName('process-check');
    for(var i = 0; i < checkBoxes.length; i++) {
        checkBoxes[i].checked = checked;
    }
}

function operateChecked(operation) {
    var checkBoxes = document.getElementsByClassName('process-check');
    for(var i = checkBoxes.length-1; i >= 0; i--) {  // count from the end of list because operate('remove') erases rows
        if (checkBoxes[i].checked) {
            operate(checkBoxes[i].parentElement, operation)
        }
    }
    document.getElementById('operation_checkbox').checked = false;
    requestNewData();
}

function colorByState(state) {
    switch (state) {
        case 'RUNNING': return 'green';
        case 'FAILED': return 'red';
        case 'FINISHED': return 'black';
        case 'BATCH_FINISHED': return 'black';
    }
}

function applyStateFilter() {
    var filter = document.getElementById('state_filter');
    var state = filter.options[filter.selectedIndex].text;
    applyFilter(state, 3)
}

function applyFilter(state, cellNum) {
    var table = document.getElementById('uuid_table');
    var rowCount = table.rows.length;
    for(var i = rowCount-1; i > 0; i--) {   // -2 to dont remove table header
        if (state !== table.rows[i].cells[cellNum].textContent) {
            table.rows[i].style.visibility = 'hidden'
        } else {
            table.rows[i].style.visibility = 'visible'
        }
    }
}