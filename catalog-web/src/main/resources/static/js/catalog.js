$(document).ready(function () {
    $('.error:not(:empty):not(.global)').each(function () {
        $(this).parent().parent().addClass('has-error');
    });
    imdbShow();
});

const imdbShow = function () {
    const checked = $('#imdb:checked').length > 0;
    if (checked) {
        $('#imdbPanel').show();
    } else {
        $('#imdbPanel').hide();
    }
};
