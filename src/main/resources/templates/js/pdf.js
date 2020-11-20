$(function () {
    setWaiting(true);
    requestData();
    setWaiting(false);
});

function requestData() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/pdf/get",
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

function updateFileTable(genLog) {
    var table = document.getElementById('file_table');
    for (var i = 0; i < genLog.length; i++) {
        insertFileName(table, genLog[i])
    }
    setWaiting(false);
}

function insertFileName(table, genLog) {
    var row = table.getElementsByTagName('tbody')[0].insertRow(-1);
    row.insertCell(0);
    row.insertCell(1);
    row.insertCell(2);
    row.cells[0].innerHTML = genLog.id;
    row.cells[0].style.display = "none"
    row.cells[1].innerHTML = genLog.uuid;
    row.cells[2].innerHTML = genLog.state;
    const cell = row.insertCell(3); // cell with operation button
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
    switch (action) {
        case 'remove':
            const id = table.rows[i].cells[0].textContent;
            removeCommand(id)
            table.deleteRow(i);
            break;
        case 'download':
            const uuid = table.rows[i].cells[1].textContent;
            downloadCommand(uuid)
            break;
    }
}

function removeCommand(id) {
    $.ajax({
        type: 'DELETE',
        contentType: "application/json",
        url: "/pdf/remove/" + id,
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

function downloadCommand(uuid) {
    const win = window.open("/pdf/get/" + uuid, '_blank');
    win.focus();
}

function startGenerating() {
    setWaiting(true);
    const uuid = $("#uuid").val()

    $.ajax({
        type: "POST",
        url: "/pdf/generate/" + uuid,
        contentType: false,
        processData: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data)
            location.reload()
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });

    setWaiting(false);
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
