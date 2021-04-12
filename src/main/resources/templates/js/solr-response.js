$(function () {
    setWaiting(true);
    requestData();
    setWaiting(false);
});

function requestData() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/solr-response/all",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
            updateFileTable(data);
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
}

function updateFileTable(fileNames) {
    var table = document.getElementById('file_table');
    for (var i = 0; i < fileNames.length; i++) {
        insertFileName(table, fileNames[i])
    }
    setWaiting(false);
}

function insertFileName(table, fileName) {
    var row = table.getElementsByTagName('tbody')[0].insertRow(-1);
    row.insertCell(0);
    row.cells[0].innerHTML = fileName;
    const cell = row.insertCell(1); // cell with operation button
    setOperationButton(cell);
}

function setOperationButton(cell) {
    var checkbox = document.createElement('div');
    checkbox.className = 'form-check';
    checkbox.style = 'display: inline';
    var checkinput = document.createElement('input');
    checkinput.className = 'form-check-input file-check';
    checkinput.type = 'checkbox';
    checkinput.name = 'radio';
    checkbox.appendChild(checkinput);
    cell.appendChild(checkbox);

    var btn = document.createElement('button');
    btn.className = 'remove_btn';
    btn.title = 'Smazat';
    btn.setAttribute( "onClick", "javascript: operate(this, 'remove');" );
    cell.appendChild(btn);

    btn = document.createElement('button');
    btn.className = 'download_btn';
    btn.title = 'Stahnout';
    btn.setAttribute( "onClick", "javascript: operate(this, 'download');" );
    cell.appendChild(btn);
}

function operate(element, action) {
    var table = document.getElementById('file_table');
    var i = element.parentNode.parentNode.rowIndex;
    var fileName = table.rows[i].cells[0].textContent;
    switch (action) {
        case 'remove':
            removeCommand(fileName)
            table.deleteRow(i);
            break;
        case 'download':
            downloadCommand(fileName)
            break;
    }
}

function removeCommand(fileName) {
    $.ajax({
        type: 'DELETE',
        contentType: "application/json",
        url: "/solr-response/remove/" + fileName,
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

function downloadCommand(fileName) {
    var form = document.createElement("form");
    form.method = "GET";
    form.action = "/solr-response/download/" + fileName;
    document.body.appendChild(form);
    form.submit();
    document.body.removeChild(form);
}

function startProcess() {
    setWaiting(true);

    var process_params = {
        'year_from': $("#year_from").val(),
        'year_to': $("#year_to").val(),
        'model': $("#model").val(),
        'accessibility': $("#accessibility").val(),
    }
    const formData = new FormData();
    const params = new Blob([JSON.stringify(process_params)], {type : "application/json"})
    formData.append('params', params)

    $.ajax({
        type: "POST",
        url: "/internal-processes/new/solr-response",
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

    setWaiting(false);
}

function clearTable() {
    var table = document.getElementById('file_table');
    var rowCount = table.rows.length;
    for(var i = rowCount-1; i > 0; i--) {   // -2 to dont remove table header
        table.deleteRow(i);
    }
}

function setWaiting(set) {
    showSpin(set);
    var table = document.getElementById('file_table');
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

function updateSelect(element) {
    var checked = element.checked;
    var checkBoxes = document.getElementsByClassName('file-check');
    for(var i = 0; i < checkBoxes.length; i++) {
        checkBoxes[i].checked = checked;
    }
}

function operateChecked(operation) {
    var checkBoxes = document.getElementsByClassName('file-check');
    // count from the end of list because operate('remove') erases rows
    for(var i = checkBoxes.length-1; i >= 0; i--) {
        if (checkBoxes[i].checked) {
            operate(checkBoxes[i].parentElement, operation)
        }
    }
    document.getElementById('operation_checkbox').checked = false;
    requestData()
}
