var uuids = [];

$(function () {
    showPublishPanel(false);

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#load_uuid_submit" ).click(function() { showPublishPanel(loadOneUuid()) });
    $( "#load_file_submit" ).click(function() { loadUuidsFromFile();  showPublishPanel(true); });
    $( "#publish_button" ).click(function() { sendUuids(); showPublishPanel(false); });
});

function loadOneUuid() {
    uuids = []
    updateTotal();
    const uuid = $("#enter_uuid").val()
    if (uuid !== '') {
        uuids.push(uuid)
        updateTotal();
        return true;
    }
    return false;
}

function loadUuidsFromFile() {
    uuids = [];
    updateTotal();
    const file_el = $("#enter_file")[0];
    const file = file_el.files[0];
    const reader = new FileReader();
    reader.onload = function(progressEvent) {
        for (const line of this.result.split(/[\r\n]+/)) {
            if (line !== '') {
                uuids.push(line);
            }
        }
        if (uuids.length > 0) {
            updateTotal();
            const submit = document.getElementById('load_file_submit');
            showElement(submit, false);
        }
    };
    reader.readAsText(file, "utf-8");
}

function sendUuids() {
    setWaiting(true);

    var process_params = {
        'root_uuids': uuids,
    }

    const formData = new FormData();
    const params = new Blob([JSON.stringify(process_params)], {type : "application/json"})
    formData.append('params', params)

    $.ajax({
        type: "POST",
        url: "/internal-processes/new/perio_parts_pub",
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
    uuids = []
    setWaiting(false);
}

function showPublishPanel(show) {
    const reindexPanel = document.getElementById("publish_panel");
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
    var reindex_button = document.getElementById('publish_button');
    reindex_button.disabled = set;
}
