import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.stream.JsonReader;

public class BalanceParser {
	
	private JsonReader jsRead;
	private String company;
	private HashMap<String,  ArrayList<Double>> finData;
	private int year;
	private int document;
	
	public BalanceParser(File json, int document) throws FileNotFoundException{
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
	
	

	
	public String getCompany(){
		return company;
	}
	
	public HashMap<String, ArrayList<Double>> getFinData(){
		return finData;
	}
	

}
