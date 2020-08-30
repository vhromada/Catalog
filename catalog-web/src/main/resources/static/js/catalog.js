$(document).ready(function () {
    $('.error:not(:empty):not(.global)').each(function () {
        $(this).parent().parent().addClass('has-error');
    });
    imdbShow();
});

var imdbShow = function () {
    var checked = $('#imdb:checked').length > 0;
    if (checked) {
        $('#imdbPanel').show();
    } else {
        $('#imdbPanel').hide();
    }
};
