var express = require('express');
var connect = require('connect');
var api = require('./apicalls.js');
var app = express();
var bodyParser = require('body-parser');
app.use( bodyParser.json() );       // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
}));



app.use(function(req, res, next) {
    // Set permissive CORS header - this allows this server to be used only as
    // an API server in conjunction with something like webpack-dev-server.
    res.setHeader('Access-Control-Allow-Origin', '*');

    // Disable caching so we'll always get the latest comments.
    res.setHeader('Cache-Control', 'no-cache');
    next();
});


// What to do when GET request is received
app.get('/', function (req, res) {
  var output= api.getJavaOutput();
  console.log('SERVER: sending this: ' + JSON.stringify(output));
  res.send(JSON.stringify(output));

});

// What to do when POST request is received
app.post('/', function (req, res) {
  var query=req.body.name;
  console.log('SERVER: got this: ' + query);
  api.setTicker(query);
  res.send('POST: GOT TICKER');

});

app.listen(3000, function () {
  console.log('Mini-server listening on port 3000!');
});
