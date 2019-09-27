var stompClient = null;
var uuids = [];

$(function () {
    showSpin(false);
    showReindexPanel(false);

    var socket = new SockJS('/reindex-websocket');
    stompClient = Stomp.over(socket);

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#load_uuid_submit" ).click(function() { loadOneUuid(); showReindexPanel(true); });
    $( "#load_file_submit" ).click(function() { loadUuidsFromFile(); showReindexPanel(true); });
    $( "#reindex_button" ).click(function() { sendUuids(); showReindexPanel(false); });
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

function sendUuids() {
    for(var i = 0; i < uuids.length; i++) {
        stompClient.send("/reindex-websocket", {}, uuids[i]);
    }
    clearTable()
}

function showSpin(show) {
    var spin = document.getElementById("wait-spin");
    showElement(spin, show);
}

function showReindexPanel(show) {
    var button = document.getElementById("reindex_button_form");
    var table = document.getElementById('uuid_table_container');
    var info = document.getElementById('info_div');
    showElement(button, show);
    showElement(table, show);
    showElement(info, show);
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