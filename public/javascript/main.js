$(function(){

    var big_width = 700;
    var big_height = 300;

    var small_width = 250;
    var small_height = 200;



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

    $("#getRecommendations").unbind('click').click(function (e) {

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


            var target_chart_data = [];
            // all of this is to get the biz viz to show up
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
                    target_chart_data.push(data_point);
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
                    var matching_datapoint = _.find(target_chart_data, function(a) {
                        return a[0] == timestamp
                    });
                    var date = new Date(timestamp);
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

            data.addColumn('datetime', 'Time');
            data.addColumn('number', "#" + target);
            data.addColumn('number', "#" + comparisons[0]);
            data.addRows(sorted_final_chart_data);
            var options = {
                title: "#" + target + " vs. " + "#" + comparisons[0],

                hAxis: {
                title: 'February 24, 2015'
                },  
                vAxis: {
                    title: 'Number of hashtags'
                },
                colors: ['#097138', '#FFA500']
            };


            var chart = new google.visualization.LineChart(document.getElementById('big_viz'));
            chart.draw(data, options);
            //////Start of individual recommendations

            // get all data for comparison hashtags
            var rec_comparison_data = _.filter(raw_data, function(val, key){ 
                return key != target;
            });

            // for each one, need to make a chart, with that and the target
            _.each(rec_comparison_data, function(val, idx) {
                console.log(val);
                var chart_data = []
                var data = new google.visualization.DataTable();
                data.addColumn('datetime', 'Time');
                data.addColumn('number', "#" + target);
                data.addColumn('number', "#" + hashtags[idx + 1] );
                // so, we have the target data

                var options = {
                    title: "#" + target + " vs. " + "#" + hashtags[idx + 1],
                    hAxis: {
                        title: 'February 24, 2015'
                    },  
                    vAxis: {
                        title: 'Number of hashtags'
                    },
                    colors: ['#097138', '#FFA500']

                };

                _.each(val, function(count, timestamp) {
                    // find the matching one in the chart data and push it to the end
                    var matching_datapoint = _.find(target_chart_data, function(a) {
                        return a[0] == timestamp
                    });
                    // target data appended with count
                    new_data = [matching_datapoint[0], matching_datapoint[1]];
                    new_data.push(count);
                    chart_data.push(new_data);
                });


                // now transofrm final chart data to be Date(date)
               chart_data = _.map(chart_data, function(data){ 
                    return [new Date(data[0]), data[1], data[2]];

                });

                // sort by dates, or else chart will look weird
                chart_data = _.sortBy(chart_data, function(a) {
                    return a[0]
                });
                // add the data 
                data.addRows(chart_data);
                console.log(chart_data);

                var el = $(".template_rec").clone();
                el.removeClass("template_rec");
                el.addClass("real_rec");
                
                var b = ($("#recs").data("num") + 1);

                $("#recs").data("num", b);
                var rec_id = "rec_" + b;
                console.log(el, b, rec_id);
                
                el.find(".rec_viz").last().attr("id", rec_id);
                el.data('image_options', options);
                el.data('image_raw_data', data);
                el.addClass('slide');

                $(".slider1").append(el);

                var chart = new google.visualization.LineChart(document.getElementById(rec_id));
                chart.draw(data, options);

                el.find(".zoom").on('click', function (e) {
                    var el = $(this).parent();
                    var data = el.data('image_raw_data');
                    var options = el.data('image_options');

                    
                    var chart = new google.visualization.LineChart(document.getElementById('big_viz'));

                    chart.draw(data, options);
                    $('#big_viz').data('image_raw_data', data);
                    $('#big_viz').data('image_options', options);
                    $(".bookmark").removeClass('bookmarked');
                    $(".normalize").removeClass('normalized');
                });
            });


        });
    });

});




