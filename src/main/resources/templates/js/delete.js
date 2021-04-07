var uuids = [];

$(function () {
    showSpin(false);
    showDeletePanel(false);

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#load_uuid_submit" ).click(function() { loadOneUuid(); showDeletePanel(true); });
    $( "#load_file_submit" ).click(function() { loadUuidsFromFile(); showDeletePanel(true); });
    $( "#delete_button" ).click(function() { sendUuids(); showDeletePanel(false); });
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
    setWaiting(true);

    const formData = new FormData();
    const params = new Blob([JSON.stringify(uuids)], {type : "application/json"})
    formData.append('uuids', params)

    $.ajax({
        type: "DELETE",
        url: "/delete",
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

    clearTable();
    setWaiting(false);
}

function showSpin(show) {
    var spin = document.getElementById("wait-spin");
    showElement(spin, show);
}

function showDeletePanel(show) {
    var deletePanel = document.getElementById("delete_panel");
    var table = document.getElementById('uuid_table_container');
    showElement(deletePanel, show);
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
    var reindex_button = document.getElementById('delete_button');
    if (set) {
        table.style.opacity = "0.5";
        reindex_button.disabled = true;
    } else {
        table.style.opacity = "1.0";
        reindex_button.disabled = false;
    }
}
