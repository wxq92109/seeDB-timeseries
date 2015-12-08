var express = require('express');
var router = express.Router();
/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Home' });
});

router.post('/getRecommendations', function(req, res) {
    console.log("router post called");

    console.log(req.body.user_hashtag);    
    console.log(req.body.start_time);
    console.log(req.body.end_time);

    user_hashtag = req.body.user_hashtag;
    start_time = req.body.start_time;
    end_time = req.body.end_time;
    res.send(JSON.stringify(data));   

});


module.exports = router;
