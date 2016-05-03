import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.stream.JsonReader;

public class IncomeParser {
	private JsonReader jsRead;
	private String company;
	private HashMap<String,  ArrayList<Double>> finData;
	private int year;
	private int document;


	public IncomeParser(File json, int document) throws FileNotFoundException{
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
	
	
	
	public String getCompany(){
		return company;
	}
	
	public HashMap<String, ArrayList<Double>> getFinData(){
		return finData;
}
}