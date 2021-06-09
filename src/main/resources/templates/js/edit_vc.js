var uuid = null;

$(function () {
    showEditPanel(false);

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#load_uuid_submit" ).click(function() { loadOneUuid(); });
    $( "#edit_button" ).click(function() { sendUuids(); });
});

function loadOneUuid() {
    uuid = document.getElementById('enter_uuid').value;
    if (uuid) {
        showEditPanel(true);
    }
}

function sendUuids() {
    const data = {
        uuid: uuid,
        nameCz: document.getElementById('title_cz').value,
        nameEn: document.getElementById('title_en').value,
        descriptionCz: document.getElementById('desc_cz').value,
        descriptionEn: document.getElementById('desc_en').value
    }
    $.ajax({
        type: "PUT",
        url: "/vc",
        data: JSON.stringify(data),
        contentType: 'application/json',
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
    showEditPanel(false);
}

function showEditPanel(show) {
    var panel = document.getElementById("edit_panel");
    showElement(panel, show);
}

function showElement(element, show) {
    if (show) {
        element.style.display = "block";
    } else {
        element.style.display = "none";
    }
}
