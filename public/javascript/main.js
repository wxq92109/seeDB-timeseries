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
    google.load('visualization', '1.0', {'packages':['corechart'], callback: function() {}});

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
            var raw_data = $.parseJSON(ret);
            // not sure why, but we need to parse it twice before it becomes a JS object
            var raw_data = $.parseJSON(raw_data);

            var chart_data = [];
            var data = new google.visualization.DataTable();

            var hashtags = [];
            _.each(raw_data, function(val, key) {
                hashtags.push(key);
            });

            var target = hashtags[0];
            var comparisons = [hashtags[1]];
            // get all relevant target data
            // into [[timestamp, count], [timestamp, count]]
            var target_data = _.filter(raw_data, function(val, key){ 
                return key == target; 
            });
            _.each(target_data, function(data, idx) {
                _.each(data, function(count, timestamp) {
                    var data_point = [];
                    data_point.push(timestamp, count);
                    chart_data.push(data_point);
                });

            });

            // get all data for comparison hashtags
            var comparison_data = _.filter(raw_data, function(val, key){ 
                return $.inArray(key, comparisons) != -1;
            });

            var final_chart_data = [];
            _.each(comparison_data, function(data, idx) {
                _.each(data, function(count, timestamp) {
                    // find the matching one in the chart data and push it to the end
                    // or just make a new chart data 
                    var matching_datapoint = _.find(chart_data, function(a) {
                        return a[0] == timestamp
                    });
                    var date = new Date(timestamp);
                    console.log("date", date);
                    matching_datapoint.push(count);
                    final_chart_data.push(matching_datapoint);
                });
            });

            // now transofrm final chart data to be Date(date)
           final_chart_data = _.map(final_chart_data, function(data){ 
                return [new Date(data[0]), data[1], data[2]];

            });

            // sort by dates, or else chart will look weird
            sorted_final_chart_data = _.sortBy(final_chart_data, function(a) {
                return a[0]
            });


            console.log("sorted_final_chart_data");
            console.log(sorted_final_chart_data);


            data.addColumn('datetime', 'Time');
            data.addColumn('number', "#" + target);
            data.addColumn('number', "#" + comparisons[0]);


            data.addRows(sorted_final_chart_data);
            var options = {
                hAxis: {
                title: 'Time'
            },
            vAxis: {
                title: 'Number of hashtags'
            },
            colors: ['#a52714', '#097138']

          };
            var chart = new google.visualization.LineChart(document.getElementById('big_viz'));
            chart.draw(data, options);



        });
    });

});




