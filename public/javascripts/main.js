function submitForm(formId, callbackFunc) {
    form = $("#"+formId);
    dataString = form.serialize();
    $.ajax({
        type: form.attr('method'),
        url: form.attr('action'),
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
