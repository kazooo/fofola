var uuids = [];

$(function () {
    showSpin(false);
    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#load_uuid_submit" ).click(function() { checkOneUuid(); });
    $( "#load_file_submit" ).click(function() { checkUuidsFromTable(); });
});

function insertUuidIntoTable(data) {
    var table = document.getElementById('uuid_table');
    insertRowUuid(table, data);
}

function checkOneUuid() {
    uuids = []
    const uuid = document.getElementById('enter_uuid').value;
    if (uuid !== '') {
        uuids.push(uuid)
        sendUuids();
    }
}

function checkUuidsFromTable() {
    uuids = []
    clearTable();
    var file_el = $("#enter_file")[0];
    var file = file_el.files[0];
    var reader = new FileReader();
    reader.onload = function(progressEvent){
        var lines = this.result.split('\n');
        for(var line = 0; line < lines.length; line++){
            if (lines[line] !== '') {
                uuids.push(lines[line])
            }
        }
        if (uuids.length > 0) {
            const submit = document.getElementById('load_file_submit');
            showElement(submit, false);
            sendUuids()
        }
    };
    reader.readAsText(file);
}

function insertRowUuid(table, data) {
    var row = table.getElementsByTagName('tbody')[0].insertRow(-1);

    var cell = row.insertCell(0);
    cell.innerHTML = data.uuid;

    cell = row.insertCell(1);
    cell.innerHTML = data.model;

    cell = row.insertCell(2);
    cell.innerHTML = data.isIndexed + ' / ' + data.isStored;
    if (!data.isIndexed || !data.isStored) {
        cell.className = 'table-danger'
    }

    cell = row.insertCell(3);
    cell.innerHTML = data.accessibilityInSolr + ' / ' + data.accessibilityInFedora;
    if (data.accessibilityInSolr !== data.accessibilityInFedora) {
        cell.className = 'table-danger'
    }

    cell = row.insertCell(4);
    var href = data.imgUrl !== 'no image' ? '<a target="_blank" href="' + data.imgUrl + '">image url</a>' : 'no image';
    cell.innerHTML = href;
    if (data.model === 'page' && data.imgUrl === 'no image') {
        cell.className = 'table-danger'
    }

    cell = row.insertCell(5);
    cell.innerHTML = data.rootTitle;
    cell.title = data.rootTitle;  // to display long text on hover

    cell = row.insertCell(6);
    cell.innerHTML = data.solrModifiedDate + '<br/>' + data.fedoraModifiedDate;
    cell.title = data.solrModifiedDate + ' / ' + data.fedoraModifiedDate;
    if (data.hasOwnProperty('solrModifiedDate') === false ||
        data.hasOwnProperty('fedoraModifiedDate') === false) {
        cell.className = 'table-danger'
    }

    cell = row.insertCell(7);
    setOperationButtons(cell)
}

function setOperationButtons(cell) {
    var btn = document.createElement('button');
    btn.className = 'reindex_btn';
    btn.title = 'Reindexovat';
    btn.setAttribute( "onClick", "javascript: operate(this, 'reindex');" );
    cell.appendChild(btn);

    btn = document.createElement('button');
    btn.className = 'public_btn';
    btn.title = 'Zveřejnit';
    btn.setAttribute( "onClick", "javascript: operate(this, 'public');" );
    cell.appendChild(btn);

    btn = document.createElement('button');
    btn.className = 'private_btn';
    btn.title = 'Zneveřejnit';
    btn.setAttribute( "onClick", "javascript: operate(this, 'private');" );
    cell.appendChild(btn)
}

function operate(element, action) {
    var table = document.getElementById('uuid_table');
    var uuid = table.rows[element.parentNode.parentNode.rowIndex].cells[0].textContent;
    switch (action) {
        case 'reindex':
            stompClient.send("/reindex-websocket", {}, uuid);
            break;
        case 'public':
            stompClient.send("/rights-websocket", {'action': 'public'}, uuid);
            break;
        case 'private':
            stompClient.send("/rights-websocket", {'action': 'private'}, uuid);
            break;
    }
}

function sendUuids() {
    setWaiting(true);

    const formData = new FormData();
    const params = new Blob([JSON.stringify(uuids)], {type : "application/json"})
    formData.append('uuids', params)

    $.ajax({
        type: "POST",
        url: "/check-uuid",
        data: formData,
        contentType: false,
        processData: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
            const states = JSON.parse(data)
            states.forEach(state =>
                insertUuidIntoTable(state)
            )
        },
        error: function (e) {
            console.log("ERROR : ", e);
        }
    });
    uuids = []
    setWaiting(false);
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

function clearTable() {
    var table = document.getElementById('uuid_table');
    var rowCount = table.rows.length;
    for(var i = rowCount-2; i >= 0; i--) {   // -2 to dont remove table header
        table.deleteRow(i);
    }
}

function setWaiting(set) {
    showSpin(set);
    var table = document.getElementById('uuid_table');
    var load_button = document.getElementById('load_uuid_submit');
    if (set) {
        table.style.opacity = "0.5";
        load_button.disabled = true;
    } else {
        table.style.opacity = "1.0";
        load_button.disabled = false;
    }
}
