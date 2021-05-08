$(document).ready(function()
{
    $('#templateMovieCardContainer').remove();  // Remove from page if present.

    var templateMovieCard = $(
        '<div id="templateMovieCardContainer">' +
        '<div id="templateMovieCardContent"></div>' +
        '</div>'
    );

    $('body').append(templateMovieCard);

    // Hide card by default
    $('#templateMovieCardContainer').hide();


    $('.movieCardAnchor').mouseenter(function() {

        $('#templateMovieCardContainer').hide();

        var movieId = $(this).attr('id');

        $.get('movieCard', {id: movieId}, function(responseText) {

            console.log(responseText);
            $('#templateMovieCardContent').html(responseText);
            $('#templateMovieCardContainer').fadeIn();
        });

        var pos = $(this).offset();
        var height = $(this).height();

        $('#templateMovieCardContainer').css({
            top: pos.top + height + 10 + 'px',
            left: (pos.left + 30) + 'px'
        });

    });

    $('.movieCardAnchor').mouseleave(function() {
            $('#templateMovieCardContainer').hide();
            $('#templateMovieCardContent').html("");
    });
});
