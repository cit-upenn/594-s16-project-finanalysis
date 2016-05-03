import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.stream.JsonReader;

public class CFSParser {
	private JsonReader jsRead;
	private String company;
	private HashMap<String,  ArrayList<Double>> finData;
	private int year;
	private int document;
	
	public CFSParser(File json, int document) throws FileNotFoundException{
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
	
	
	
	
	
	public String getCompany(){
		return company;
	}
	
	public HashMap<String, ArrayList<Double>> getFinData(){
		return finData;
	}
}
