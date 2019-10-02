var stompClient = null;
var uuids = [];

$(function () {
    showSpin(false);
    showControlPanel(false);

    var socket = new SockJS('/rights-websocket');
    stompClient = Stomp.over(socket);

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#load_uuid_submit" ).click(function() { loadOneUuid(); showControlPanel(true); });
    $( "#load_file_submit" ).click(function() { loadUuidsFromFile(); showControlPanel(true); });
    $( "#public_button" ).click(function() { sendUuids('public'); showControlPanel(false); });
    $( "#private_button" ).click(function() { sendUuids('private'); showControlPanel(false); });
    $( "#clean_button" ).click(function() { clearTable(); showControlPanel(false); });
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

function showSpin(show) {
    var spin = document.getElementById("wait-spin");
    showElement(spin, show);
}

function showControlPanel(show) {
    var reindexPanel = document.getElementById("change_panel");
    var table = document.getElementById('uuid_table_container');
    showElement(reindexPanel, show);
    showElement(table, show);
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