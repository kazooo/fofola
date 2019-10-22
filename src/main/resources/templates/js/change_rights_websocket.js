var stompClient = null;
var uuids = [];
var interval = 3 * 1000;
var refreshIntervalId = null;

$(function () {
    showSpin(false);
    showControlPanel(false);
    showTableContainer(false);

    var socket = new SockJS('/rights-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/processes/rights', function (data) {
            fillProgressTable(JSON.parse(data.body));
        });
        stompClient.subscribe('/processes/one-p-info', function (data) {
            fillProgressTable(JSON.parse(data.body));
        });
    });

    var check = function(){
        console.log('check...');
        if(stompClient.ws.readyState === WebSocket.OPEN){
            refreshIntervalId = setInterval(requestForProcessInfo, interval);
        } else {
            setTimeout(check, 1000); // check again in a second
        }
    };

    check();

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#load_uuid_submit" ).click(function() {
        loadOneUuid();
        showControlPanel(true);
        showTableContainer(true);
        clearProgressTable();
        toggleProgressTable(false);
    });
    $( "#load_file_submit" ).click(function() {
        loadUuidsFromFile();
        showControlPanel(true);
        showTableContainer(true);
        clearProgressTable();
        toggleProgressTable(false);
    });
    $( "#public_button" ).click(function() {
        sendUuids('public');
        showControlPanel(false);
        showTableContainer(true);
        toggleProgressTable(true);
    });
    $( "#private_button" ).click(function() {
        sendUuids('private');
        showControlPanel(false);
        showTableContainer(true);
        toggleProgressTable(true);
    });
    $( "#clean_button" ).click(function() {
        clearTable();
        clearProgressTable();
        showControlPanel(false);
        showTableContainer(false);
        toggleProgressTable(false);
    });
});

function loadOneUuid() {
    clearTable();
    var uuid = document.getElementById('enter_uuid').value;
    var table = document.getElementById('uuid_table');
    insertRowUuid(table, uuid);
    updateTotal();
}

function loadUuidsFromFile() {
    clearTable();
    var file_el = $("#enter_file")[0];
    var file = file_el.files[0];
    var reader = new FileReader();
    var table = document.getElementById('uuid_table');
    reader.onload = function(progressEvent){
        var lines = this.result.split('\n');
        for(var line = 0; line < lines.length; line++){
            insertRowUuid(table, lines[line])
        }
        updateTotal();
        var submit = document.getElementById('load_file_submit');
        showElement(submit, false);
    };
    reader.readAsText(file);
}

function insertRowUuid(table, uuid) {
    var row = table.insertRow(-1);
    var cell = row.insertCell(0);
    cell.innerHTML = uuid;
    uuids.push(uuid)
}

function sendUuids(action) {
    setWaiting(true);
    for(var i = 0; i < uuids.length; i++) {
        stompClient.send("/rights-websocket", {'action': action}, uuids[i]);
    }
    clearTable();
    setWaiting(false);
}

function requestForProcessInfo() {
    var progressTable = document.getElementById('progress-table');
    var rows = progressTable.rows;
    for(var i = 1; i < rows.length; i++) {
        var c = rows[i].cells[0];
        stompClient.send("/process-info", {}, rows[i].cells[0].textContent);
    }
}

function showSpin(show) {
    var spin = document.getElementById("wait-spin");
    showElement(spin, show);
}

function showControlPanel(show) {
    var panel = document.getElementById("change_panel");
    showElement(panel, show);
}

function showTableContainer(show) {
    var tableContainer = document.getElementById('uuid_table_container');
    showElement(tableContainer, show)
}

function showElement(element, show) {
    if (show) {
        element.style.display = "block";
    } else {
        element.style.display = "none";
    }
}

function updateTotal() {
    var total = document.getElementById('total');
    total.textContent = uuids.length;
}

function clearTable() {
    var table = document.getElementById('uuid_table');
    var rowCount = table.rows.length;
    for(var i = rowCount-1; i >= 0; i--) {
        table.deleteRow(i);
    }
    uuids = []
}

function clearProgressTable() {
    var table = document.getElementById('progress-table');
    var rowCount = table.rows.length;
    for(var i = rowCount-1; i >= 1; i--) {
        table.deleteRow(i);
    }
}

function setWaiting(set) {
    showSpin(set);
    var table = document.getElementById('uuid_table');
    var public_button = document.getElementById('public_button');
    var private_button = document.getElementById('private_button');
    if (set) {
        table.style.opacity = "0.5";
        public_button.disabled = true;
        private_button.disabled = true;
    } else {
        table.style.opacity = "1.0";
        public_button.disabled = false;
        private_button.disabled = false;
    }
}

function toggleProgressTable(show) {
    var uuidTableContainer = document.getElementById("change-rights-table");
    var progressTableContainer = document.getElementById("change-rights-progress");
    var progressTable = document.getElementById('progress-table');
    var uuidTable = document.getElementById('uuid_table');

    function display() {
        progressTableContainer.style.display = "block";
        uuidTableContainer.style.display = "none";
    }

    function hide() {
        progressTableContainer.style.display = "none";
        uuidTableContainer.style.display = "block";
    }

    if (show !== null) {
        if (show) { display() }
        else { hide() }
        return
    }

    if ((progressTableContainer.style.display === "none" ||
        progressTableContainer.style.display === "") && progressTable.rows.length > 1) {
        display()
    } else if (uuidTable.rows.length > 1){
        hide()
    }
}

function fillProgressTable(data) {
    var progressTable = document.getElementById('progress-table');
    insertProcessInfo(progressTable, data, null)
}

function insertProcessInfo(table, data, className) {

    var hasChildren = data.hasOwnProperty('children') && data.children !== null && data.children.length < 1;

    if (table.rows.length > 1) {  // 1 because of header row
        for (var i = 1; i < table.rows.length; i++) {
            var uuid = table.rows[i].cells[0].textContent;
            if (uuid === data.uuid) {  // update only state and dates
                var cells = table.rows[i].cells;
                cells[2].innerHTML = data.name;
                cells[2].title = data.name;
                var state = data.batchState === 'NO_BATCH'? data.state : data.batchState;
                cells[3].innerHTML = state;
                cells[3].style.color = colorByState(state);
                cells[5].innerHTML = data.started;
                cells[6].innerHTML = data.finished;
                var logBtn = document.getElementById('logs-btn');
                logBtn.setAttribute( "onClick", "window.open('" + data.logUrl + "')");
                if (hasChildren) {
                    var children = data.children;
                    for (var j = 0; j < children.length; j++) {
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
        insertChildrens(row, table, data)
    }
}

function insertChildrens(row, table, data) {
    row.className = 'parent';
    var children = data.children;
    for (var j = 0; j < children.length; j++) {
        insertProcessInfo(table, children[j], 'child ' + data.uuid);
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
    btn.id = 'logs-btn';
    btn.title = 'Logy';
    btn.setAttribute( "onClick", "window.open('" + data.logUrl + "')");
    cell.appendChild(btn);
}

function operate(element, action) {
    var table = document.getElementById('progress-table');
    var i = element.parentNode.parentNode.rowIndex;
    var pid = table.rows[i].cells[0].textContent;
    switch (action) {
        case 'kill':
            stompClient.send("/process-manipulation-websocket", {'action': 'kill'}, pid);
            break;
        case 'remove':
            stompClient.send("/process-manipulation-websocket", {'action': 'remove'}, pid);
            table.deleteRow(i);
            break;
    }
}

function colorByState(state) {
    switch (state) {
        case 'RUNNING': return 'green';
        case 'FAILED': return 'red';
        case 'FINISHED': return 'black';
        case 'BATCH_FINISHED': return 'black';
        case 'BATCH_FAILED': return 'red';
    }
}

