import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.stream.JsonReader;

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
		 }
		 


		
	}
	
	public int getDocument(){
		return document;
	}
	
	private void buildFinData(String key, double value, int year){
		if(year == 0){
			ArrayList<Double> arr = new ArrayList<Double>();
			arr.add(value);
			finData.put(key, arr);
			System.out.println(finData.get(key));
		} else{
			ArrayList<Double> arr = finData.get(key);
			arr.add(year,value);
			finData.put(key, arr);
		}
	}
	
	public void readBalance(JsonReader balance)throws IOException{
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
	
	public void parseBalData(JsonReader baldat)throws IOException{
		baldat.beginObject();
	     while (baldat.hasNext()) {
	    	 String name = baldat.nextName();
	    	if (name.equals("CashAndCashEquivalentsAtCarryingValue")) {
	         buildFinData("CashEqv", baldat.nextDouble(), year);
//	         finData.put("CashEqv", baldat.nextDouble());
	         System.out.println("Cash: " + finData.get("CashEqv").get(year));
	       } else if (name.equals("Assets")) {
	    	 buildFinData("Assets", baldat.nextDouble(), year);
//	         finData.put("Assets", baldat.nextDouble());
	         System.out.println("Assets: "+ finData.get("Assets").get(year));
	       } else if (name.equals("PropertyPlantAndEquipmentNet")) {
//	         retainedEarning = baldat.nextDouble();
	    	 buildFinData("PPE", baldat.nextDouble(), year);
//	         finData.put("PPE", baldat.nextDouble());
	         System.out.println("PPE: " + finData.get("PPE").get(year));
	       } else if (name.equals("StockholdersEquity")) {
//		     equity = baldat.nextDouble();
		     buildFinData("Equity", baldat.nextDouble(), year);

//		     finData.put("Equity", baldat.nextDouble());

		     System.out.println("Equity: " + finData.get("Equity").get(year));
		         
	       }else if (name.equals("Liabilities")){
//	    	   liabilities = baldat.nextDouble();
	    	   buildFinData("Liabilities", baldat.nextDouble(), year);
//		       finData.put("Liabilities", baldat.nextDouble());

	    	 System.out.println("Liability: " + finData.get("Liabilities").get(year));
	       }else {
			   baldat.skipValue();
	       }
	     }
	     baldat.endObject();
		
	}
	
	public void readCF(JsonReader cf)throws IOException{
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
//		    	 if(balance.nextName()== null){
		    		 System.out.println("RATE LIMIT EXCEEDED");
		    		 buildFinData("APILimitReached", 1.0, year);
		    	 }	   

		
	}
	
	public void parseCFData(JsonReader cfdat)throws IOException{
		cfdat.beginObject();
	     while (cfdat.hasNext()) {
	    	 String name = cfdat.nextName();
	    	if (name.equals("NetIncomeLoss")) {
	    	 buildFinData("NetIncome", cfdat.nextDouble(), year);
//	         finData.put("NetIncome", cfdat.nextDouble());
	         System.out.println("Net Income: " + finData.get("NetIncome").get(year));
	       } else if (name.equals("CashAndCashEquivalentsPeriodIncreaseDecrease")) {
		     buildFinData("CashEqvPeriod", cfdat.nextDouble(), year);

//	    	   finData.put("CashEqvPeriod", cfdat.nextDouble());
	         System.out.println("CashEqvPeriod: "+ finData.get("CashEqvPeriod").get(year));
	       } else if (name.equals("CashAndCashEquivalentsPeriodIncreaseDecreaseExcludingExchangeRateEffect")) {
			     buildFinData("CashEqvPeriod", cfdat.nextDouble(), year);

//	    	   finData.put("CashEqvPeriod", cfdat.nextDouble());
	         System.out.println("CashEqvPeriod: " + finData.get("CashEqvPeriod").get(year));
	       } else if (name.equals("NetCashProvidedByUsedInOperatingActivitiesContinuingOperations")) {
			     buildFinData("OCF", cfdat.nextDouble(), year);

//	    	   finData.put("OCF", cfdat.nextDouble());
		     System.out.println("OCF: " + finData.get("OCF").get(year));     
	       }else if(name.equals("NetCashProvidedByUsedInOperatingActivities")){
			     buildFinData("OCF", cfdat.nextDouble(), year);
			     System.out.println("OCF: " + finData.get("OCF").get(year)); 
	       }else if (name.equals("Liabilities")){
//		     finData.put("Liabilities", cfdat.nextDouble());
//	    	 System.out.println("Liability: " + finData.get("Liabilities"));
	       }else {
	    	   cfdat.skipValue();
	       }
	     }
	     cfdat.endObject();
		
	}
	
	
	public void readIncome(JsonReader inc)throws IOException{
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
	
	public void parseIncData(JsonReader incdat)throws IOException{
		incdat.beginObject();
	     while (incdat.hasNext()) {
	    	 String name = incdat.nextName();
	    	if(name.equals("NetIncomeLoss")) {
		      buildFinData("NetIncome", incdat.nextDouble(), year);

	       }else if (name.equals("SalesRevenueNet")) {
			  buildFinData("SalesRevenueNet", incdat.nextDouble(), year);

//	         finData.put("SalesRevenueNet", incdat.nextDouble());
	         System.out.println("SalesRevenueNet: " + finData.get("SalesRevenueNet").get(year));
	       } else if (name.equals("CostOfRevenue")) {
			     buildFinData("CostOfRevenue", incdat.nextDouble(), year);
//	         finData.put("CostOfRevenue", incdat.nextDouble());
	         System.out.println("CostOfRevenue: "+ finData.get("CostOfRevenue").get(year));
	       } else if (name.equals("GrossProfit")) {
			  buildFinData("GrossProfit", incdat.nextDouble(), year);

//	         finData.put("GrossProfit", incdat.nextDouble());
	         System.out.println("GrossProfit: " + finData.get("GrossProfit").get(year));
	       } else if (name.equals("ResearchAndDevelopmentExpense")) {
				  buildFinData("RD", incdat.nextDouble(), year);

//		     finData.put("RD", incdat.nextDouble());
		     System.out.println("RD: " + finData.get("RD").get(year));     
	       } else if (name.equals("OperatingIncomeLoss")) {
				  buildFinData("OperatingIncome", incdat.nextDouble(), year);

	         System.out.println("OperatingIncomeLoss: " + finData.get("OperatingIncome").get(year));
	       } else if (name.equals("EarningsPerShareBasic")) {
				  buildFinData("EPS", incdat.nextDouble(), year);

//		     finData.put("EPS", incdat.nextDouble());
		     System.out.println("EPS: " + finData.get("EPS").get(year));     
	       }else if (name.equals("WeightedAverageNumberOfSharesOutstandingBasic")) {
				  buildFinData("Shares", incdat.nextDouble(), year);
  
//	    	   finData.put("Shares", incdat.nextDouble());
			     System.out.println("Shares: " + finData.get("Shares").get(year));     
	       }else {
	    	   incdat.skipValue();
	       }
	     }
	     incdat.endObject();
		
	}
	
	
	
	public String getCompany(){
		return company;
	}
	
	public HashMap<String, ArrayList<Double>> getFinData(){
		return finData;
	}
	

}
