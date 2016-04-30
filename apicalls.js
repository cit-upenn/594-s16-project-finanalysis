   
   // import {ticker} from 'interface.jsx';
   	// var face = require('./interface.jsx');
	var https = require('https');
	var fs = require('fs');
	var async = require('async');
	var balanceDone = false;
	var CFDone = false;
	var incDone = false;
	var ratioDone = false;
	ticker='AMZN';

	var setTicker = (symbol)=>{
		ticker = symbol;
		console.log(ticker);
	}

	var getTicker = ()=>{
		return ticker;
	}


	// fs.readFile('output.json', 'utf8', function (err, data) {
	//   if (err) throw err;
	//   obj = JSON.parse(data);
	//   console.log(obj)
	// });



	// make ticker a variable based on request from something else
	// var ticker = 'FB';

    var params = {
        	// Request parameters
	        "formType": "10-K",
	        "filingOrder": "0",
	    };

    var header = {"Ocp-Apim-Subscription-Key": "d2d9d9cb72264482ad1e3941394b3b74"

		};
     
   
    var getBalance = {
			hostname: "services.last10k.com",
			path: '/v1/company/' + ticker + '/balancesheet?',
			method: 'GET',
			headers: header,
			qs: params,
		};

   var getCF = {
			hostname: "services.last10k.com",
			path: '/v1/company/' + ticker + '/cashflows?',
			method: 'GET',
			headers: header,
			qs: params,
		};

   var getInc = {
			hostname: "services.last10k.com",
			path: '/v1/company/' + ticker + '/income?',
			method: 'GET',
			headers: header,
			qs: params,
		};

   var getRatio = {
			hostname: "services.last10k.com",
			path: '/v1/company/' + ticker + '/ratios?',
			method: 'GET',
			headers: header,
			// qs: params,
		};

	var writeBalance = (response)=> {
		  var str = '';
		  // appending data to string
		  response.on('data', function (recieved) {
		     str += recieved;
		  });

		  //the whole response has been recieved, so we just print it out here
		  response.on('end', function () {

			fs.writeFile('balance.json', str, (err) => {
			  if (err) throw err;
			  console.log('write balance complete!');
			  balanceDone = !balanceDone;
			  // console.log(balanceDone);
			});
		  });
		}


	var writeCF = (response)=> {
		  var str = '';
		  // appending data to string
		  response.on('data', function (recieved) {
		     str += recieved;
		  });

		  //the whole response has been recieved, so we just print it out here
		  response.on('end', function () {

			fs.writeFile('CashFlow.json', str, (err) => {
			  if (err) throw err;
			  console.log('write CF complete!');
			  CFDone = !CFDone;
			});
		  });
		}

	var writeInc = (response)=> {
		  var str = '';
		  // appending data to string
		  response.on('data', function (recieved) {
		     str += recieved;
		  });

		  //the whole response has been recieved, so we just print it out here
		  response.on('end', function () {

			fs.writeFile('Income.json', str, (err) => {
			  if (err) throw err;
			  console.log('write Income complete!');
			  incDone = !incDone;
			});
		  });
		}

	var writeRatio = (response)=> {
		  var str = '';
		  // appending data to string
		  response.on('data', function (recieved) {
		     str += recieved;
		  });

		  //the whole response has been recieved, so we just print it out here
		  response.on('end', function () {

			fs.writeFile('Ratio.json', str, (err) => {
			  if (err) throw err;
			  console.log('write Ratio complete!');
			  ratioDone = !ratioDone;
			});
		  });
		}




		
		var b = () =>{https.request(getBalance, writeBalance).end();}
		var c = () =>{https.request(getCF, writeCF).end(); }
		var i = () =>{https.request(getInc, writeInc).end(); }
		var r = () =>{https.request(getRatio, writeRatio).end(); }
		// console.log(balance);
		// doWork = (get, write, cb) => {https.request(get, write).end(); cb();}


		// async.series([
		// 	  function (callback) {
		// 	    doWork(getBalance, writeBalance, callback);
		// 	  }, function (callback) {
		// 	    doWork(getCF, writeCF, callback);
		// 	  }, function (callback) {
		// 	    doWork(getInc,writeInc, callback);
		// 	  }, function (callback) {
		// 	    doWork(getRatio, writeRatio,callback);
		// 	  }
		// 	], function () {
		// 	  console.log('all Done!');
		// 	});
   		b();
   		c();
   		i();
   		r();

		setInterval(()=>{
   			console.log(balanceDone);
   			console.log(CFDone);
   			console.log(incDone);
   			console.log(ratioDone);
   			if(	balanceDone === true &&  CFDone === true &&  incDone === true &&  ratioDone === true){
   				console.log('all true!');
   				check = '{"check": "OK"}'
   				fs.writeFile('check.json', check, (err) => {

			  		if (err) throw err;
					  console.log('OKAY JSON DONE!');
					  // ratioDone = !ratioDone;
				});
				fs.writeFile('NodetoJava.txt', 'OKAY', (err) => {

			  		if (err) throw err;
					  console.log('OKAY TEXT DONE!');
					  // ratioDone = !ratioDone;
				});
				  
   			}
   			
   		}, 1500)

   		setInterval(()=>{
	   		fs.readFile('JavatoNode.json', 'utf8', function (err, data) {
				  if (err) throw err;
				  obj = JSON.parse(data);
				  if(obj.Ready === 1.0){
				  	  console.log('Java status: ' + obj.Ready);
					  console.log('FCF: ' + obj.FCF);
					  console.log('FCFperShare: ' + obj.FCFperShare);
					  console.log('E/A: ' + obj.EA);
					  console.log('L/A: ' + obj.EA);
					  console.log('EPS: ' + obj.EA);
				  }

			});
		}, 2000);

  //  		writeLogLine = (str, cb) => {console.log(str); cb();}

  //  		async.series([
  // 			 function (callback) {
		//     writeLogLine('This is the first line', callback);
		//   }, function (callback) {
		//     writeLogLine('This is the second line', callback);
		//   }, function (callback) {
		//     writeLogLine('This is the third line', callback);
		//   }
		// ], function () {
		//   console.log('Done writing the logs');
		// });



		// writeLogLine('This is the first line', function () {
		//   writeLogLine('This is the second line', function () {
		//       writeLogLine('This is the third line', function () {
		//           console.log('Done writing the logs.');
		//         });
		//     });
		// });



		// b(()=> {
		//   c(()=> {
		//      i(()=> {
		//       	r(()=> {
 	// 				console.log(ratioDone);
		//       		});
		//         });
		//     });
		// });










   


