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
			}
		 }else if(document == 1){
			 try {
				readCF(jsRead);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }else if(document == 2){
			 try {
				readIncome(jsRead);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }else if(document == 3){
			 try {
				readRatio(jsRead);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		
		balance.beginObject();
	     while (balance.hasNext()) {
	       String name = balance.nextName();
	       if (name.equals("Company")) {
	         company = balance.nextString();
	         System.out.println("Company: " + company);
	       } else if(name.equals("Status")){
	    	   year = balance.nextInt();
	       }else if(name.equals("Data")){
	    	   parseBalData(balance);
	       } else{
	    	   balance.skipValue();
	       }
	     }
	     balance.endObject();
	    		   

		
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
		cf.beginObject();
	     while (cf.hasNext()) {
	       String name = cf.nextName();
	       if (name.equals("Company")) {
	         company = cf.nextString();
	         System.out.println("Company: " + company);
	       } else if(name.equals("Status")){
	    	   year = cf.nextInt();
	       }else if(name.equals("Data")){
	    	   parseCFData(cf);
	       } else{
	    	   cf.skipValue();
	       }
	     }
	     cf.endObject();
		
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
		inc.beginObject();
	     while (inc.hasNext()) {
	       String name = inc.nextName();
	       if (name.equals("Company")) {
	         company = inc.nextString();
	         System.out.println("Company: " + company);
	       }  else if(name.equals("Status")){
	    	   year = inc.nextInt();
	       }else if(name.equals("Data")){
	    	   parseIncData(inc);
	       } else{
	    	   inc.skipValue();
	       }
	     }
	     inc.endObject();
		
	}
	
	public void parseIncData(JsonReader incdat)throws IOException{
		incdat.beginObject();
	     while (incdat.hasNext()) {
	    	 String name = incdat.nextName();
	    	if (name.equals("SalesRevenueNet")) {
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

//	         finData.put("OperatingIncome", incdat.nextDouble());
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
	public void readRatio(JsonReader rat)throws IOException{
		rat.beginObject();
	     while (rat.hasNext()) {
	       String name = rat.nextName();
	       if (name.equals("Company")) {
	         company = rat.nextString();
	         System.out.println("Company: " + company);
	       }  else if(name.equals("Status")){
	    	   year = rat.nextInt();
	       }else if(name.equals("TotalStockholdersEquity")){
	    	   parseEquityHistorical(rat);
	       } else if(name.equals("DebtEquity")){
	    	   parseDEHistorical(rat);
	       }else if(name.equals("EarningsPerShare")){
	    	   parseEPSHistorical(rat);
	       }else if(name.equals("FreeCashFlow")){
	    	   parseFCFHistorical(rat);
	       }else{
	    	   rat.skipValue();
	       }
	     }
	     rat.endObject();
		
	}
	
	public void parseEquityHistorical(JsonReader ratdat)throws IOException{
		ratdat.beginObject();
	     while (ratdat.hasNext()) {
	    	 String name = ratdat.nextName();
	    	if (name.equals("NetIncomeLoss")) {
				  buildFinData("NetIncome", ratdat.nextDouble(), year);

//	         finData.put("NetIncome", ratdat.nextDouble());
	         System.out.println("Net Income: " + finData.get("NetIncome").get(year));
	       } else if (name.equals("CashAndCashEquivalentsPeriodIncreaseDecrease")) {
				  buildFinData("CashEqvPeriod", ratdat.nextDouble(), year);

//	         finData.put("CashEqvPeriod", ratdat.nextDouble());
	         System.out.println("CashEqvPeriod: "+ finData.get("CashEqvPeriod").get(year));
	       } else if (name.equals("OperatingIncomeLoss")) {
				  buildFinData("OperatingIncome", ratdat.nextDouble(), year);

//	         finData.put("OperatingIncome", ratdat.nextDouble());
	         System.out.println("OperatingIncomeLoss: " + finData.get("OperatingIncome").get(year));
	       } else if (name.equals("EarningsPerShareBasic")) {
				  buildFinData("EPS", ratdat.nextDouble(), year);

//		     finData.put("EPS", ratdat.nextDouble());
		     System.out.println("EPS: " + finData.get("EPS").get(year));     
	       }else if (name.equals("Liabilities")){
//		     finData.put("Liabilities", cfdat.nextDouble());
//	    	 System.out.println("Liability: " + finData.get("Liabilities"));
	       }else {
	    	   ratdat.skipValue();
	       }
	     }
	     ratdat.endObject();
		
	}
	
	public void parseDEHistorical(JsonReader ratdat){
		
	}
	
	public void parseEPSHistorical(JsonReader ratdat){
		
	}
	public void parseFCFHistorical(JsonReader ratdat){
		
	}
	
	public String getCompany(){
		return company;
	}
	
	public HashMap<String, ArrayList<Double>> getFinData(){
		return finData;
	}
	

}
