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
        beforeSend: function( xhr ){
            xhr.setRequestHeader('Authorization', make_base_auth( $("#key").val(), "" ));
        },
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

function make_base_auth( user, password ) {
    var tok = user + ':' + password;
    var hash = btoa(tok);
    return "Basic " + hash;
}
