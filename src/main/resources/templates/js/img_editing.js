$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $( "#editing_submit" ).click(function() {
        if ($("#enter_uuid").val() !== '') {
            send()
        } else {
            console.log('Please fill page uuid!')
        }
    });
});

function send() {
    const process_params = {
        'page_uuid': $("#enter_uuid").val(),
        'img_datastream': $("#img_datastream").val()
    }

    const formData = new FormData();
    formData.append('image', $('#enter_file')[0].files[0]);
    const params = new Blob([JSON.stringify(process_params)], {type : "application/json"})
    formData.append('params', params)

    $.ajax({
        type: "POST",
        url: "/internal-processes/new/img_editing",
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
        },
        complete: function () {
            document.getElementById('enter_uuid').value = ''
            const submitButton = document.getElementById('editing_submit');
            const loadButton = document.getElementById('load_button');
            const loadButtonLabel = document.getElementById('load_button_label')
            const dsSelector = document.getElementById('img_datastream')
            showElement(loadButton)
            showElement(loadButtonLabel)
            hideElement(submitButton)
            hideElement(dsSelector)
        }
    });
}

function hideElement(element) {
    element.style.display = "none";
    element.style.visibility = 'hidden';
}

function showElement(element) {
    element.style.display = "block";
    element.style.visibility = 'visible';
}