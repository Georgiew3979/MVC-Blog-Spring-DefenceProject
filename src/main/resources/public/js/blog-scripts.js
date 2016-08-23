$(function() {
    $('#messages li').click(function() {
        $(this).fadeOut();
    });
    setTimeout(function() {
        $('#messages li.info').fadeOut();
    }, 6000);
    setTimeout(function() {
        $('#messages li.alert').fadeOut();
    }, 3000);
});