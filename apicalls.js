   
	var https = require('https');
	var fs = require('fs');
	var async = require('async');
	var balanceDone = false;
	var CFDone = false;
	var incDone = false;
	var ratioDone = false;
	JavatoNode = new Object;


	// gets message from Java code
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

	// sets the newest JavatoNode.json
	var setJavaOutput = (input) => {
		JavatoNode = input;
		console.log('JtN set!');
	}

	// functions that can be called by external sources
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

	// params for request
    var params = {
        	// Request parameters
	        "formType": "10-K",
	        "filingOrder": "0",
	    };

	// header with API key 
    var header = {"Ocp-Apim-Subscription-Key": "d2d9d9cb72264482ad1e3941394b3b74"

		};
    
    // a function for constructing the API calls
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
 
   	// outputs balance.json
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
			});
		  });
		}

	// outputs CashFlow.json
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

	// outputs Income.json
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

	// outputs Ratio.json
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

		// function for making multiple API calls at the same time
   		var makeCalls = (getBalance,getCF,getInc,getRatio ) =>{
			   	b(getBalance, writeBalance);
		   		c(getCF, writeCF);
		   		i(getInc, writeInc);
		   		r(getRatio, writeRatio);
   		}


		// Intialize the NodetoJava.txt at WAIT
		fs.writeFile('NodetoJava.txt', 'WAIT', (err) => {

		  		if (err) throw err;
				  console.log('WAIT TEXT!');
		  });

   		// check if all API calls have returned
		setInterval(()=>{

   			if(	balanceDone === true &&  CFDone === true &&  incDone === true &&  ratioDone === true){

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
				  	throw err;
				  } 
				  // console.log('This is JavatoNode: ' +JSON.stringify(data));
				  // sometimes the JSON parse goes bad, so wrapped in try catch blocks
				   try {
						  obj = JSON.parse(data);
					  } catch (e) {

					  	console.log('BAD JSON!!');
						} 
				  
				  console.log('Ready: ' + obj.Ready);
				  setJavaOutput(obj);


			});
			
		}, 2000);



 










   


