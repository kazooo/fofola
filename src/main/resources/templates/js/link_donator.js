var uuids = [];

$(function () {
    showLinkPanel(false);

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#load_uuid_submit" ).click(function() { showLinkPanel(loadOneUuid()) });
    $( "#load_file_submit" ).click(function() { loadUuidsFromFile();  showLinkPanel(true); });
    $( "#link_button" ).click(function() { sendUuids(); showLinkPanel(false); });
});

function loadOneUuid() {
    uuids = []
    updateTotal();
    const uuid = $("#enter_uuid").val()
    const donator = $("#donator").val()
    if (uuid !== '' && donator !== '') {
        uuids.push(uuid)
        updateTotal();
        return true;
    }
    return false;
}

function loadUuidsFromFile() {
    uuids = []
    updateTotal();
    const file_el = $("#enter_file")[0];
    const file = file_el.files[0];
    const reader = new FileReader();
    reader.onload = function(progressEvent){
        const lines = this.result.split('\n');
        for(let i = 0; i < lines.length; i++){
            if (lines[i] !== '') {
                uuids.push(lines[i])
            }
        }
        if (uuids.length > 0) {
            updateTotal();
            const submit = document.getElementById('load_file_submit');
            showElement(submit, false);
        }
    };
    reader.readAsText(file);
}

function sendUuids() {
    setWaiting(true);

    var process_params = {
        'donator': $("#donator").val(),
        'mode': 'link',
        'root_uuids': uuids,
    }

    const formData = new FormData();
    const params = new Blob([JSON.stringify(process_params)], {type : "application/json"})
    formData.append('params', params)

    $.ajax({
        type: "POST",
        url: "/internal-processes/new/donator_link",
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

function showLinkPanel(show) {
    const reindexPanel = document.getElementById("link_panel");
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
