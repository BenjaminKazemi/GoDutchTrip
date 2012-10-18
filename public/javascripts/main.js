$(document).ready(function() {
});

function refresh() {
    window.location = window.location;
}

function submitForm(formId, callbackFunc) {
    form = $("#"+formId);
    url = form.attr('action');
    dataString = form.serialize();

    if( form.attr('method').toUpperCase() == "GET" ) {
        url += "?" + dataString;
        dataString = "";
    }
    $.ajax({
        type: form.attr('method'),
        url: url,
        data: dataString,
        error: function(data, textStatus, jqXHR) {
            if(callbackFunc != undefined ) {
                callbackFunc(data, textStatus, jqXHR);
            }
        },
        success: function(data, textStatus, jqXHR) {
            if(callbackFunc != undefined ) {
                callbackFunc(data, textStatus, jqXHR);
            }
        }
    });
    return false;
}
