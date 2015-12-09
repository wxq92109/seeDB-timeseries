var express = require('express');
var router = express.Router();
var request = require('request');


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Home' });
});

router.post('/getRecommendations', function(req, res) {
    console.log("router post called");

    console.log(req.body.user_hashtag);    
    console.log(req.body.start_time);
    console.log(req.body.end_time);
    console.log(req.body);

    request({
        url: 'http://localhost:8084/getResult', //URL to hit
        qs: req.body, //Query string data
        method: 'GET', //Specify the method
        // headers: { //We can define headers too
        //     'Content-Type': 'MyContentType',
        //     'Custom-Header': 'Custom Value'
        // }
    }, function(error, response, body){
        if(error) {
            console.log(error);
        } else {
            console.log('outcome of request');
            res.send(JSON.stringify(body));   

        }
    });

    // user_hashtag = req.body.user_hashtag;
    // start_time = req.body.start_time;
    // end_time = req.body.end_time;
    // res.send(JSON.stringify(req.body));   



});


module.exports = router;
