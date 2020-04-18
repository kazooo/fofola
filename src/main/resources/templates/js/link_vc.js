var uuids = [];

$(function () {
    showLinkPanel(false);

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#load_uuid_submit" ).click(function() { if (loadOneUuid()) { showLinkPanel(true); } });
    $( "#load_file_submit" ).click(function() { if (loadUuidsFromFile()) { showLinkPanel(true); } });
    $( "#link_button" ).click(function() { sendUuids(); showLinkPanel(false); });
});

function loadOneUuid() {
    var uuid = document.getElementById('enter_uuid').value;
    if (uuid !== '') {
        uuids.push(uuid)
        updateTotal();
        return true;
    }
    return false;
}

function loadUuidsFromFile() {
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
            updateTotal();
            var submit = document.getElementById('load_file_submit');
            showElement(submit, false);
            return true;
        } else {
            return false;
        }
    };
    reader.readAsText(file);
}

function sendUuids() {
    setWaiting(true);

    var process_params = {
        'vc_uuid': $("#vc_uuid").val(),
        'root_uuids': uuids,
        'solr_host': $("#solr_host").val(),
        'fedora_host': $("#fedora_host").val(),
        'fedora_user': $("#fedora_user").val(),
        'fedora_pswd': $("#fedora_pswd").val(),
    }

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/processes/new/vc_link",
        data: JSON.stringify(process_params),
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
    uuids = []
    setWaiting(false);
}

function showLinkPanel(show) {
    var reindexPanel = document.getElementById("link_panel");
    showElement(reindexPanel, show);
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

function setWaiting(set) {
    var reindex_button = document.getElementById('link_button');
    reindex_button.disabled = set;
}
