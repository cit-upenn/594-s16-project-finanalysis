import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class gets the financial data and calculates the important ratios and the 
 * target price
 * @author weiyinko
 *
 */
public class Valuator {
	private HashMap<String, ArrayList<Double>> balance;
	private HashMap<String, ArrayList<Double>> cfs;
	private HashMap<String, ArrayList<Double>> income;
	private int year;
	public Valuator(HashMap<String, ArrayList<Double>> balance, HashMap<String, ArrayList<Double>> cfs, HashMap<String, ArrayList<Double>> income, int year){
		this.balance = new HashMap<String, ArrayList<Double>>();
		this.cfs = new HashMap<String, ArrayList<Double>>();
		this.income = new HashMap<String, ArrayList<Double>>();
		this.balance = balance;
		this.cfs = cfs;
		this.income = income;
		this.year = year;
		
	}
	
	public double getFCF(){
		double fcf = 0;
		if(cfs.containsKey("OCF") && balance.containsKey("PPE")){
			 fcf = cfs.get("OCF").get(year)-balance.get("PPE").get(year);

		}
		System.out.println("FCF: " + fcf);
		return fcf;
	}
	
	public double getFCFperShare(){
		double fcfps = 0;
		if(income.containsKey("Shares")&& cfs.containsKey("OCF")){
			fcfps = (cfs.get("OCF").get(year)-balance.get("PPE").get(year))/income.get("Shares").get(year);
			System.out.println("FCF per share: " + fcfps);
		}else if (income.containsKey("EPS") && cfs.containsKey("OCF")&& income.containsKey("NetNetIncome")){
//			In case the company does not report their share numbers...
			fcfps = (cfs.get("OCF").get(year)-balance.get("PPE").get(year))/(income.get("NetIncome").get(year)/income.get("EPS").get(year));
		}
		return fcfps;
	}
	
	public double getEA(){

		double EA = balance.get("Equity").get(year)/balance.get("Assets").get(year);
		System.out.println("E/A: " + EA);
		return EA;
	}
	
	public double getLA(){
		double LA = 0;
//		Just in case company does not report liabilities... (ugh)
		if(balance.containsKey("Liabilities")){
			LA = balance.get("Liabilities").get(year)/balance.get("Assets").get(year);	
		} else{
			LA = (balance.get("Assets").get(year) -	balance.get("Equity").get(year))/balance.get("Assets").get(year);	
		}
		System.out.println("L/A: " + LA);
		return LA;
	}
	
	public double getEPS(){
		if(income.containsKey("EPS")){
			return income.get("EPS").get(year);
		}else{
			return 0.0;
		}
		
	}
	
	public double getCurrentPrice(){
		if(income.containsKey("Shares")){
			return balance.get("Equity").get(year)/income.get("Shares").get(year);
		} else if (income.containsKey("EPS")){
			return balance.get("Equity").get(year)/(income.get("NetIncome").get(year)/income.get("EPS").get(year));
		}
		return 0.0;
	}
	
	public double getTargetPrice(){
		return getFCFperShare()*getCurrentPrice();
	}
	
	public Map<String, Double> getJSON(){
//		I wanted some ordering in the JSON presented so TreeMap is used
		Map<String, Double> json = new TreeMap<String, Double>();
//		some error handling for display
		if(!balance.containsKey("Company") ){
			json.put("MissingBalanceSheet!", 0.0);
			return json;
		} else if(!cfs.containsKey("Company")){
			json.put("MissingCashFlowStatement!", 0.0);
			return json;
		}else if(!income.containsKey("Company")){
			json.put("MissingIncomeStatement!", 0.0);
			return json;
		}else if(balance.containsKey("APILimitReached")||income.containsKey("APILimitReached")||cfs.containsKey("APILimitReached")){
			json.put("APILimitReached!!!", 0.0);
			return json;
		}
		
//		json.put("Ready", 1.0);
		json.put("6:FCF", getFCF());
		json.put("3:FCFperShare", getFCFperShare());
		json.put("5:EA", getEA());
		json.put("4:LA", getLA());
		json.put("2:EPS", getEPS());
		json.put("1:Target Price", getTargetPrice());

		return json;
	}

}
