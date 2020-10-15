var lastPage = 0;
var interval = 5 * 1000;
var refreshIntervalId = null;

$(function () {
    setWaiting(true);
    requestData();
    refreshIntervalId = setInterval(frequentlyRequestIfCheckboxIsChecked, interval);
    updatePageNum();
    setWaiting(false);
});

function frequentlyRequestIfCheckboxIsChecked() {
    var autoReloadCheckbox = document.getElementById('auto_reload');
    if (autoReloadCheckbox.checked) {
        requestData()
    }
}

function requestData() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/internal-processes/all",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
            insertProcessesIntoTable(data);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

function insertProcessesIntoTable(processes) {
    var table = document.getElementById('uuid_table');
    for (var i = 0; i < processes.length; i++) {
        insertProcessInfo(table, processes[i])
    }
    setWaiting(false);
}

function insertProcessInfo(table, process) {
    // table already has some processes -> check if table contains given process
    if (table.rows.length > 1) {  // 1 because of header row
        for (var i = 1; i < table.rows.length; i++) {
            var uuid = table.rows[i].cells[0].textContent;
            if (uuid === process.id) {  // found process -> update only state info
                var cells = table.rows[i].cells;
                cells[2].innerHTML = process.state
                cells[4].innerHTML = process.finish;
                if ('terminationReason' in process) {
                    cells[5].innerHTML = process.terminationReason;
                }
                return;
            }
        }

    }
    // new process which table doesnt contain -> insert new row
    var row = table.getElementsByTagName('tbody')[0].insertRow(-1);
    insertCells(row, process);
    fillCells(row.cells, process);
}

function insertCells(row, process) {
    row.insertCell(0); // process id
    row.insertCell(1);  // process type
    var cell = row.insertCell(2);  // process state
    cell.style.color = colorByState(process.state);
    row.insertCell(3); // start date
    row.insertCell(4);  // finish date
    row.insertCell(5);  // finish reason
    cell = row.insertCell(6); // cell with operation buttons
    setOperationButtons(cell);
}

function fillCells(cells, process) {
    cells[0].innerHTML = process.id;
    cells[1].innerHTML = process.type;
    cells[2].innerHTML = process.state;
    cells[3].innerHTML = process.start;
    cells[4].innerHTML = process.finish;
    if ('terminationReason' in process) {
        cells[5].innerHTML = process.terminationReason;
    }
}

function setOperationButtons(cell) {
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
    btn.setAttribute( "onClick", "javascript: operate(this, 'terminate');" );
    cell.appendChild(btn);

    btn = document.createElement('button');
    btn.className = 'remove_btn';
    btn.title = 'Smazat';
    btn.setAttribute( "onClick", "javascript: operate(this, 'remove');" );
    cell.appendChild(btn);

    btn = document.createElement('button');
    btn.className = 'download_btn';
    btn.title = 'Otevřit logy';
    btn.setAttribute( "onClick", "javascript: operate(this, 'open_logs');" );
    cell.appendChild(btn);
}

function operate(element, action) {
    const table = document.getElementById('uuid_table');
    const i = element.parentNode.parentNode.rowIndex;
    const pid = table.rows[i].cells[0].textContent;
    switch (action) {
        case 'terminate':
            sendCommand("PUT", pid, 'terminate')
            break;
        case 'remove':
            sendCommand('DELETE', pid, 'remove')
            table.deleteRow(i);
            break;
        case 'open_logs':
            const url = '/internal-processes/logs/' + pid + '.log'
            const win = window.open(url, '_blank')
            win.focus()
            break;
    }
}

function sendCommand(type, pid, command) {
    $.ajax({
        type: type,
        contentType: "application/json",
        url: "/internal-processes/" + command + '/' + pid,
        dataType: 'json',
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
    // count from the end of list because operate('remove') erases rows
    for(var i = checkBoxes.length-1; i >= 0; i--) {
        if (checkBoxes[i].checked) {
            operate(checkBoxes[i].parentElement, operation)
        }
    }
    document.getElementById('operation_checkbox').checked = false;
    requestData()
}

function colorByState(state) {
    switch (state) {
        case 'ACTIVE': return 'green';
        case 'TERMINATED': return 'red';
        case 'FINISHED': return 'black';
    }
}
