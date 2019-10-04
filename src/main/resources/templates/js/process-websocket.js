var stompClient = null;
var lastPage = 0;
var interval = 5 * 1000;
var refreshIntervalId = null;

$(function () {
    var socket = new SockJS('/process-websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/processes/info', function (data) {
            insertData(JSON.parse(data.body));
        });
    });

    var check = function(){
        console.log('check...');
        if(stompClient.ws.readyState === WebSocket.OPEN){
            requestForData();
            refreshIntervalId = setInterval(requestForDataByCheckBox, interval);
        } else {
            setTimeout(check, 1000); // check again in a second
        }
    };

    updatePageNum();
    check();
});

function requestForDataByCheckBox() {
    var autoReloadCheckbox = document.getElementById('auto_reload');
    if (autoReloadCheckbox.checked) {
        requestForData();
    }
}

function requestForData() {
    setWaiting(true);
    stompClient.send("/process-websocket", {}, lastPage);
}

function insertData(json) {
    clearTable();
    var table = document.getElementById('uuid_table');
    for (var i = 0; i < json.length; i++) {
        insertProcessInfo(table, json[i])
    }
    setWaiting(false);
}

function insertProcessInfo(table, data) {
    var row = table.getElementsByTagName('tbody')[0].insertRow(-1);

    var cell = row.insertCell(0);
    cell.innerHTML = data.pid;

    var cell = row.insertCell(1);
    cell.innerHTML = data.def;

    cell = row.insertCell(2);
    cell.innerHTML = data.name;
    cell.title = data.name;

    cell = row.insertCell(3);
    cell.innerHTML = data.state;

    cell = row.insertCell(4);
    cell.innerHTML = data.planned;

    cell = row.insertCell(5);
    cell.innerHTML = data.started;

    cell = row.insertCell(6);
    cell.innerHTML = data.finished;
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
        requestForData();
        updatePageNum();
    }
}

function nextProcessPage() {
    lastPage++;
    requestForData();
    updatePageNum();
}

function updatePageNum() {
    document.getElementById('page_num').textContent = lastPage + 1; // +1 because lastPage is counting from 0
}