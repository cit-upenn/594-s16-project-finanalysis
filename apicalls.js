   
	var https = require('https');
	var fs = require('fs');
	var async = require('async');
	var balanceDone = false;
	var CFDone = false;
	var incDone = false;
	var ratioDone = false;
	// var ticker = '';
	JavatoNode = new Object;

	// var getTicker = ()=>{
	// 	return ticker;
	// }

	// var test = () =>{
	// 	console.log('this is just a long test string');
	// }
	var getJavaOutput = () => {
			console.log('getting JavatoNode!');
		    console.log('Java status: ' + JavatoNode.Ready);
            console.log('FCF: ' + JavatoNode.FCF);
            console.log('FCFperShare: ' + JavatoNode.FCFperShare);
            console.log('E/A: ' + JavatoNode.EA);
            console.log('L/A: ' + JavatoNode.LA);
            console.log('EPS: ' + JavatoNode.EPS);
		return JavatoNode;
	}

	var setJavaOutput = (input) => {
		JavatoNode = input;
		console.log('JtN set!');
	}

	module.exports = {
		  setTicker: (symbol)=>{
		  		var ticker = symbol;
		  		var getBalance = makeGetters(ticker,'/balancesheet?' );
		  		var getCF = makeGetters(ticker,'/cashflows?' );
		  		var getInc = makeGetters(ticker,'/income?' );
		  		var getRatio = makeGetters(ticker,'/ratios?');
				console.log('ticker is ' + ticker);
				makeCalls(getBalance,getCF,getInc,getRatio);
		  },
		  getJavaOutput: () => {
		  	console.log('getting JavatoNode!');
		    console.log('Java status: ' + JavatoNode.Ready);
            console.log('FCF: ' + JavatoNode.FCF);
            console.log('FCFperShare: ' + JavatoNode.FCFperShare);
            console.log('E/A: ' + JavatoNode.EA);
            console.log('L/A: ' + JavatoNode.LA);
            console.log('EPS: ' + JavatoNode.EPS);
			return JavatoNode;
			}
		};


	// make ticker a variable based on request from something else
	// var ticker = 'FB';

    var params = {
        	// Request parameters
	        "formType": "10-K",
	        "filingOrder": "0",
	    };

    var header = {"Ocp-Apim-Subscription-Key": "d2d9d9cb72264482ad1e3941394b3b74"

		};
     
   	var makeGetters = (ticker, doc) =>{
   		var koolObj = {
   			hostname: "services.last10k.com",
			path: '/v1/company/' + ticker + doc,
			method: 'GET',
			headers: header,
			qs: params,
   		};
   		return koolObj;
   	}
  //   var getBalance = {
		// 	hostname: "services.last10k.com",
		// 	path: '/v1/company/' + ticker + '/balancesheet?',
		// 	method: 'GET',
		// 	headers: header,
		// 	qs: params,
		// };

  //   var getCF = {
		// 	hostname: "services.last10k.com",
		// 	path: '/v1/company/' + ticker + '/cashflows?',
		// 	method: 'GET',
		// 	headers: header,
		// 	qs: params,
		// };

  //   var getInc = {
		// 	hostname: "services.last10k.com",
		// 	path: '/v1/company/' + ticker + '/income?',
		// 	method: 'GET',
		// 	headers: header,
		// 	qs: params,
		// };

  //  var getRatio = {
		// 	hostname: "services.last10k.com",
		// 	path: '/v1/company/' + ticker + '/ratios?',
		// 	method: 'GET',
		// 	headers: header,
		// };

	var writeBalance =(response)=> {
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


		var b = (getBalance, writeBalance) =>{https.request(getBalance, writeBalance).end();}
		var c = (getCF, writeCF) =>{https.request(getCF, writeCF).end(); }
		var i = (getInc, writeInc) =>{https.request(getInc, writeInc).end(); }
		var r = (getRatio, writeRatio) =>{https.request(getRatio, writeRatio).end(); }


				// b();
		  //  		c();
		  //  		i();
		  //  		r();

   		var makeCalls = (getBalance,getCF,getInc,getRatio ) =>{
   			// console.log('Making the calls! Ticker is ' + ticker);
			   	b(getBalance, writeBalance);
		   		c(getCF, writeCF);
		   		i(getInc, writeInc);
		   		r(getRatio, writeRatio);
   		}

  //  		var resetReady = () =>{
		// 	console.log('ready reset!!!!');
		// 	fs.writeFile('JavatoNode.json', '{"Ready":0.0}', (err) => {

		// 	  		if (err) throw err;
		// 			  console.log('WAIT TEXT!');
		// 	  });
		// }

		// Intialize the NodetoJava.txt at WAIT
   			fs.writeFile('NodetoJava.txt', 'WAIT', (err) => {

				  		if (err) throw err;
						  console.log('WAIT TEXT!');
				  });

   		// check if all API calls have returned
		setInterval(()=>{
   			// console.log(balanceDone);
   			// console.log(CFDone);
   			// console.log(incDone);
   			// console.log(ratioDone);
   			if(	balanceDone === true &&  CFDone === true &&  incDone === true &&  ratioDone === true){
   				// console.log('all true!');
   				// check = '{"check": "OK"}'

   	// 			fs.writeFile('check.json', check, (err) => {

			 //  		if (err) throw err;
				// 	  console.log('OKAY JSON DONE!');
				// 	  // ratioDone = !ratioDone;
				// });
				fs.writeFile('NodetoJava.txt', 'OKAY', (err) => {

			  		if (err) throw err;
					console.log('OKAY TEXT DONE!');
					balanceDone = !balanceDone;
					CFDone = !CFDone;
					incDone = !incDone;
					ratioDone =!ratioDone;
				});
				  
   			}
   			
   		}, 2000)


		// See if JavatoNode.json is ready
   		setInterval(()=>{
	   		fs.readFile('JavatoNode.json', 'utf8', function (err, data) {
				  if (err){
				  	console.log(JSON.stringify(data));
				  	throw err;
				  } 
				  obj = JSON.parse(data);
				  console.log('Ready: ' + obj.Ready);
				  setJavaOutput(obj);

				  // if(obj.Ready === 1.0){
				  // 	  console.log('Java status: ' + obj.Ready);
					 //  console.log('FCF: ' + obj.FCF);
					 //  console.log('FCFperShare: ' + obj.FCFperShare);
					 //  console.log('E/A: ' + obj.EA);
					 //  console.log('L/A: ' + obj.EA);
					 //  console.log('EPS: ' + obj.EA);
					 //  console.log('JtN got!');
					 //  setJavaOutput(obj);
					 //  fs.writeFile('NodetoJava.txt', 'WAIT', (err) => {

					 //  		if (err) throw err;
						// 	  console.log('WAIT TEXT!');

						// 			fs.writeFile('JavatoNode.json', '{"Ready":0.0}', (err) => {

						// 			  		if (err) throw err;
						// 					  console.log('WAIT TEXT!');
						// 			  });
					 //  });



				  // }

			});
			// resetReady();
		}, 2000);



 










   


