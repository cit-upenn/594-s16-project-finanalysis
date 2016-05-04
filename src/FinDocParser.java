import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.stream.JsonReader;

/**
 * This class parses the financial document JSONs. In this implementation the parser 
 * can handle Balance Sheets, Cash Flow Statement, Income Statement, and Key Financial Ratios
 * @author weiyin
 *
 */
public class FinDocParser {
	private JsonReader jsRead;
	private String company;
	private HashMap<String,  ArrayList<Double>> finData;
	private int year;
	private int document;
	
	public FinDocParser(File json, int document) throws FileNotFoundException{
		 this.jsRead = new JsonReader(new FileReader(json));
		 company = null;
		 finData = new HashMap<String, ArrayList<Double>>();
		 
		 this.document = document;
		 
//		 numbers dictate what type of document it is
		 if(document == 0){
			 try {
				readBalance(jsRead);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				buildFinData("MissingBalanceSheet", 0.0,year);

			}
		 }else if(document == 1){
			 try {
				readCF(jsRead);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				buildFinData("MissingCashFlowStatement", 0.0,year);
			} 
		 }else if(document == 2){
			 try {
				readIncome(jsRead);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				buildFinData("MissingIncomeStatement", 0.0,year);
			}
		 }else if(document == 3){
			 try {
				readRatio(jsRead);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				buildFinData("MissingRatios", 0.0,year);
			}
		 }
		 


		
	}
	
	/**
	 * gets the recency of the parsed document; 0 is most recent, 1 is one year ago... etc
	 * @return recency of parsed doucment
	 */
	public int getDocument(){
		return document;
	}
	
	/**
	 * gets the name of the company of the files
	 * @return the company name
	 */
	public String getCompany(){
		return company;
	}
	
	/**
	 * gets the extracted financial data in HashMap form
	 * @return HashMap of financial data
	 */
	public HashMap<String, ArrayList<Double>> getFinData(){
		return finData;
	}
	
	/**
	 * This helper method puts the Key-Value pair into the Map according to date
	 * @param key
	 * @param value 
	 * @param year recency of the 10-K
	 */
	private void buildFinData(String key, double value, int year){
		if(year == 0){
			ArrayList<Double> arr = new ArrayList<Double>();
			arr.add(value);
			finData.put(key, arr);
		} else{
			ArrayList<Double> arr = finData.get(key);
			arr.add(year,value);
			finData.put(key, arr);
		}
	}
	
	/**
	 * This method provides the function for reading the outermost layer of 
	 * Gson-converted balance sheet JSON
	 * 
	 * @param balance 
	 * @throws IOException
	 */
	private void readBalance(JsonReader balance)throws IOException{
//		 parse method from gson javadoc
		 try{
			balance.beginObject();
		     while (balance.hasNext()) {
		    	 String name =balance.nextName();;
		       
	//	       for error display later 
		       if (name.equals("Company")) {
		         company = balance.nextString();
		         buildFinData("Company", 1.0, year);
		         System.out.println("Company: " + company);
		       } else if(name.equals("Status")){
		    	   year = balance.nextInt();
		       }else if(name.equals("Data")){
		    	   parseBalData(balance);
		       } else if(name.equals("statusCode")){
		    	   buildFinData("APILimitReached", 1.0, year);
		       }else{
		    	   balance.skipValue();
		       }
		     }
		     balance.endObject();
	     }catch(java.lang.IllegalStateException e){
	    		 System.out.println("RATE LIMIT EXCEEDED");
	    		 buildFinData("APILimitReached", 1.0, year);
	    	 }	   

		
	}
	
	/**
	 * This method provides the function for reading the inner layer of 
	 * Gson-converted balance sheet JSON
	 * @param baldat
	 * @throws IOException
	 */
	private void parseBalData(JsonReader baldat)throws IOException{
		baldat.beginObject();
	     while (baldat.hasNext()) {
	    	 String name = baldat.nextName();
	    	if (name.equals("CashAndCashEquivalentsAtCarryingValue")) {
	         buildFinData("CashEqv", baldat.nextDouble(), year);
	         System.out.println("Cash: " + finData.get("CashEqv").get(year));
	       } else if (name.equals("Assets")) {
	    	 buildFinData("Assets", baldat.nextDouble(), year);
	         System.out.println("Assets: "+ finData.get("Assets").get(year));
	       } else if (name.equals("PropertyPlantAndEquipmentNet")) {
	    	 buildFinData("PPE", baldat.nextDouble(), year);
	         System.out.println("PPE: " + finData.get("PPE").get(year));
	       } else if (name.equals("StockholdersEquity")) {
		     buildFinData("Equity", baldat.nextDouble(), year);
		     System.out.println("Equity: " + finData.get("Equity").get(year));
		         
	       }else if (name.equals("Liabilities")){
	    	   buildFinData("Liabilities", baldat.nextDouble(), year);

	    	 System.out.println("Liability: " + finData.get("Liabilities").get(year));
	       }else {
			   baldat.skipValue();
	       }
	     }
	     baldat.endObject();
		
	}
	
	/**
	 * This method provides the function for reading the outermost layer of 
	 * Gson-converted cash flow statement JSON
	 * 
	 * @param cf 
	 * @throws IOException
	 */
	private void readCF(JsonReader cf)throws IOException{
		 try{
			 cf.beginObject();
			     while (cf.hasNext()) {
			    	 String name =cf.nextName();;
			       
		//	       for error display later 
			       if (name.equals("Company")) {
			         company = cf.nextString();
			         buildFinData("Company", 1.0, year);
			         System.out.println("Company: " + company);
			       } else if(name.equals("Status")){
			    	   year = cf.nextInt();
			       }else if(name.equals("Data")){
			    	   parseCFData(cf);
			       } else if(name.equals("statusCode")){
			    	   buildFinData("APILimitReached", 1.0, year);
			       }else{
			    	   cf.skipValue();
			       }
			     }
			     cf.endObject();
		     }catch(java.lang.IllegalStateException e){
		    		 System.out.println("RATE LIMIT EXCEEDED");
		    		 buildFinData("APILimitReached", 1.0, year);
		    	 }	   

		
	}
	
	/**
	 * This method provides the function for reading the inner layer of 
	 * Gson-converted cash flow statement JSON
	 * @param cfdat
	 * @throws IOException
	 */
	private void parseCFData(JsonReader cfdat)throws IOException{
		cfdat.beginObject();
	     while (cfdat.hasNext()) {
	    	 String name = cfdat.nextName();
	    	if (name.equals("NetIncomeLoss")) {
	    	 buildFinData("NetIncome", cfdat.nextDouble(), year);
	         System.out.println("Net Income: " + finData.get("NetIncome").get(year));
	       } else if (name.equals("CashAndCashEquivalentsPeriodIncreaseDecrease")) {
		     buildFinData("CashEqvPeriod", cfdat.nextDouble(), year);

	         System.out.println("CashEqvPeriod: "+ finData.get("CashEqvPeriod").get(year));
	       } else if (name.equals("CashAndCashEquivalentsPeriodIncreaseDecreaseExcludingExchangeRateEffect")) {
			     buildFinData("CashEqvPeriod", cfdat.nextDouble(), year);

	         System.out.println("CashEqvPeriod: " + finData.get("CashEqvPeriod").get(year));
	       } else if (name.equals("NetCashProvidedByUsedInOperatingActivitiesContinuingOperations")) {
			     buildFinData("OCF", cfdat.nextDouble(), year);

		     System.out.println("OCF: " + finData.get("OCF").get(year));     
	       }else if(name.equals("NetCashProvidedByUsedInOperatingActivities")){
			     buildFinData("OCF", cfdat.nextDouble(), year);
			     System.out.println("OCF: " + finData.get("OCF").get(year)); 
	       }else {
	    	   cfdat.skipValue();
	       }
	     }
	     cfdat.endObject();
		
	}
	
	/**
	 * This method provides the function for reading the outermost layer of 
	 * Gson-converted income statement JSON
	 * 
	 * @param inc 
	 * @throws IOException
	 */
	private void readIncome(JsonReader inc)throws IOException{
		 try{
			 inc.beginObject();
			     while (inc.hasNext()) {
			    	 String name =inc.nextName();;
			       
		//	       for error display later 
			       if (name.equals("Company")) {
			         company = inc.nextString();
			         buildFinData("Company", 1.0, year);
			         System.out.println("Company: " + company);
			       } else if(name.equals("Status")){
			    	   year = inc.nextInt();
			       }else if(name.equals("Data")){
			    	   parseIncData(inc);
			       } else if(name.equals("statusCode")){
			    	   buildFinData("APILimitReached", 1.0, year);
			       }else{
			    	   inc.skipValue();
			       }
			     }
			     inc.endObject();
		     }catch(java.lang.IllegalStateException e){
//		    	 if(balance.nextName()== null){
		    		 System.out.println("RATE LIMIT EXCEEDED");
		    		 buildFinData("APILimitReached", 1.0, year);
		    	 }	   
		
	}
	
	/**
	 * This method provides the function for reading the inner layer of 
	 * Gson-converted income statement JSON
	 * @param incdat
	 * @throws IOException
	 */
	private void parseIncData(JsonReader incdat)throws IOException{
		incdat.beginObject();
	     while (incdat.hasNext()) {
	    	 String name = incdat.nextName();
	    	if(name.equals("NetIncomeLoss")) {
		      buildFinData("NetIncome", incdat.nextDouble(), year);

	       }else if (name.equals("SalesRevenueNet")) {
			  buildFinData("SalesRevenueNet", incdat.nextDouble(), year);

	         System.out.println("SalesRevenueNet: " + finData.get("SalesRevenueNet").get(year));
	       } else if (name.equals("CostOfRevenue")) {
			     buildFinData("CostOfRevenue", incdat.nextDouble(), year);
	         System.out.println("CostOfRevenue: "+ finData.get("CostOfRevenue").get(year));
	       } else if (name.equals("GrossProfit")) {
			  buildFinData("GrossProfit", incdat.nextDouble(), year);

	         System.out.println("GrossProfit: " + finData.get("GrossProfit").get(year));
	       } else if (name.equals("ResearchAndDevelopmentExpense")) {
				  buildFinData("RD", incdat.nextDouble(), year);

		     System.out.println("RD: " + finData.get("RD").get(year));     
	       } else if (name.equals("OperatingIncomeLoss")) {
				  buildFinData("OperatingIncome", incdat.nextDouble(), year);

	         System.out.println("OperatingIncomeLoss: " + finData.get("OperatingIncome").get(year));
	       } else if (name.equals("EarningsPerShareBasic")) {
				  buildFinData("EPS", incdat.nextDouble(), year);

		     System.out.println("EPS: " + finData.get("EPS").get(year));     
	       }else if (name.equals("WeightedAverageNumberOfSharesOutstandingBasic")) {
				  buildFinData("Shares", incdat.nextDouble(), year);
  
			     System.out.println("Shares: " + finData.get("Shares").get(year));     
	       }else {
	    	   incdat.skipValue();
	       }
	     }
	     incdat.endObject();
		
	}
	
	/**
	 * This method provides the function for reading the outermost layer of 
	 * Gson-converted key financial ratios JSON
	 * 
	 * @param ratio 
	 * @throws IOException
	 */
	private void readRatio(JsonReader ratio)throws IOException{
//		 parse method from gson javadoc
		 try{
			 ratio.beginObject();
		     while (ratio.hasNext()) {
		       String name =ratio.nextName();;
		       
	//	       for error display later 
		       if (name.equals("Company")) {
		         company = ratio.nextString();
		         buildFinData("Company", 1.0, year);
		         System.out.println("[Ratio]Company: " + company);
		       } else if(name.equals("CompanyTicker")){
		         company = ratio.nextString();
		         buildFinData("Company", 1.0, year);
		         System.out.println("[Ratio]Company: " + company);
		       }else if(name.equals("EarningsPerShare")){
//		    	   IDs are used to identify which historical parser to use
		    	   String id = "EarningsPerShare";
		    	   parseHistoricals(ratio, id);
		       } else if(name.equals("TotalStockholdersEquity")){
		    	   String id = "TotalStockholdersEquity";
		    	   parseHistoricals(ratio, id);
		       } else if(name.equals("Shares")){
		    	   String id = "Shares";
		    	   parseHistoricals(ratio, id);
		       } else if(name.equals("Book Value Per Share USD")){
		    	   String id = "Book Value Per Share USD";
		    	   parseHistoricals(ratio, id);
		       } else if(name.equals("FreeCashFlow")){
		    	   String id = "FreeCashFlow";
		    	   parseHistoricals(ratio, id);
		       }else if(name.equals("statusCode")){
		    	   buildFinData("APILimitReached", 1.0, year);
		       }else{
		    	   ratio.skipValue();
		       }
		     }
		     ratio.endObject();
	     }catch(java.lang.IllegalStateException e){
	    		 System.out.println("RATE LIMIT EXCEEDED");
	    		 buildFinData("APILimitReached", 1.0, year);
	    	 }	   

		
	}
	
	/**
	 * This method provides the function for reading the inner layer of 
	 * Gson-converted key financial ratios JSON
	 * @param baldat
	 * @throws IOException
	 */
	private void parseHistoricals(JsonReader ratdat, String id)throws IOException{
		ratdat.beginObject();
	     while (ratdat.hasNext()) {
	    	 String name = ratdat.nextName();
	    	if(name.equals("Historical") && id == "EarningsPerShare") {
		      parseEPSHistorical(ratdat);        
	       }else if(name.equals("Historical") && id == "TotalStockholdersEquity") {
	    	  parseEquityHistorical(ratdat);
	       }else if(name.equals("Historical") && id == "Shares"){
	    	  parseSharesHistorical(ratdat);
	       }else if(name.equals("Historical") && id == "Book Value Per Share USD"){
		    	  parseBooksHistorical(ratdat);
	       }else if(name.equals("Historical") && id == "FreeCashFlow"){
		    	  parseFCFHistorical(ratdat);
	       }else{
	    	  ratdat.skipValue(); 
	       }
	     }
	     	ratdat.endObject();	
	}
	
	/**
	 * reads the EPS historical in key financial ratios
	 * @param ratdat
	 * @throws IOException
	 */
	private void parseEPSHistorical(JsonReader ratdat)throws IOException{
		double sum =0;
		double count =0;
		ratdat.beginObject();
	     while (ratdat.hasNext()) {
	    	 String name = ratdat.nextName();
//	    	 System.out.println(name);
	    	if(name.startsWith("2015")) {
			   double eps = ratdat.nextDouble();
	    	   sum += eps;
			   count++;
			   buildFinData("EPS", eps, year);
			   System.out.println("[Ratio] EPS: " + eps);
		    }else if (name.startsWith("2014")) {
			      sum += ratdat.nextDouble();
			      count++;
	       } else if (name.startsWith("2013")) {
			      sum += ratdat.nextDouble();
			      count++;
	       } else if (name.startsWith("2012")) {
			      sum += ratdat.nextDouble();
			      count++;

	       } else if (name.startsWith("2011")) {
			      sum += ratdat.nextDouble();
			      count++;

	       } else {
	    	   ratdat.skipValue();
	       }
	     }
	     ratdat.endObject();
//	     put the average EPS in the hashmap
	     System.out.println("Sum: " + sum + " Count: "+count);
	     double avg =sum/count;
	     buildFinData("AvgEPS", avg, year);

		
	}
	

	/**
	 * reads the Equity historical in key financial ratios
	 * @param ratdat
	 * @throws IOException
	 */
	private void parseEquityHistorical(JsonReader ratdat)throws IOException{
		ratdat.beginObject();
	     while (ratdat.hasNext()) {
	    	 String name = ratdat.nextName();
	    	if(name.startsWith("2015")) {	
			   buildFinData("Equity%", (ratdat.nextDouble()/100), year);
			   
	       } else {
	    	   ratdat.skipValue();
	       }
	     }
	     ratdat.endObject();
		
	}
	
	/**
	 * reads the Shares historical in key financial ratios
	 * @param ratdat
	 * @throws IOException
	 */
	private void parseSharesHistorical(JsonReader ratdat)throws IOException{
		ratdat.beginObject();
	     while (ratdat.hasNext()) {
	    	 String name = ratdat.nextName();
	    	if(name.startsWith("2015")) {	
			   buildFinData("Shares", ratdat.nextDouble()*1000000, year);
	       } else {
	    	   ratdat.skipValue();
	       }
	     }
	     ratdat.endObject();
		
	}
	
	/**
	 * reads the book value historical in key financial ratios
	 * @param ratdat
	 * @throws IOException
	 */
	private void parseBooksHistorical(JsonReader ratdat)throws IOException{
		ratdat.beginObject();
	     while (ratdat.hasNext()) {
	    	 String name = ratdat.nextName();
	    	if(name.startsWith("2015")) {	
			   buildFinData("BVperShare", ratdat.nextDouble(), year);
	       } else {
	    	   ratdat.skipValue();
	       }
	     }
	     ratdat.endObject();
		
	}
	
	/**
	 * reads the Free Cash Flow historical in key financial ratios
	 * @param ratdat
	 * @throws IOException
	 */
	private void parseFCFHistorical(JsonReader ratdat)throws IOException{
		ratdat.beginObject();
	     while (ratdat.hasNext()) {
	    	 String name = ratdat.nextName();
	    	if(name.startsWith("2015")) {
//	    		convert the value back to millions
			   buildFinData("FCF", ratdat.nextDouble()*1000000, year);
	       } else {
	    	   ratdat.skipValue();
	       }
	     }
	     ratdat.endObject();
		
	}
	
	

	

}
