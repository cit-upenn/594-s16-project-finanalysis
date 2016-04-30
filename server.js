var express = require('express');
var api = require('./apicalls.js');
var app = express();

  var output;
  
app.use(function(req, res, next) {
    // Set permissive CORS header - this allows this server to be used only as
    // an API server in conjunction with something like webpack-dev-server.
    res.setHeader('Access-Control-Allow-Origin', '*');

    // Disable caching so we'll always get the latest comments.
    res.setHeader('Cache-Control', 'no-cache');
    next();
});

// setTimeout(()=>{
//   console.log('in timer!');
//   api.getJavaOutput();
//     // api.test();


// }, 4000);

app.get('/', function (req, res) {
  var output= api.getJavaOutput();
  console.log('sending this: ' + JSON.stringify(output));
  res.send(JSON.stringify(output));

});

app.listen(3000, function () {
  console.log('Example app listening on port 3000!');
});
