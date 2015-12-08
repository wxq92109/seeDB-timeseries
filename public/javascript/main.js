$(function(){
    var hashtags = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        prefetch: {
            url: '/assets/citynames.json',
            filter: function(list) {
                return $.map(list, function(cityname) {
                    return { name: cityname }; });
            }
        }
    });
    hashtags.initialize();

    $('input#user_hashtag').tagsinput({
        typeaheadjs: {
            name: 'citynames',
            displayKey: 'name',
            valueKey: 'name',
            source: hashtags.ttAdapter()
        }
    });

    $('#start_time').timepicker();
    $('#end_time').timepicker();

    $("#getRecommendations").on('click', function (e) {
        console.log("getRecommendations clicked");
        
        var user_hashtag = $("input#user_hashtag").val();
        var start_time = $("input#start_time").val();
        var end_time = $("input#end_time").val();

        var params = {
            user_hashtag: user_hashtag,
            start_time: start_time,
            end_time: end_time
        };
        console.log(params);
        $.post('/getRecommendations', params, function(ret) {
            console.log("ret: ", ret);

        });
    });

});




