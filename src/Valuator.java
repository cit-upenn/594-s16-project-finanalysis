import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
//		System.out.println(cfs.get("OCF").get(year));
//		System.out.println(balance.get("PPE").get(year));
		double fcf = cfs.get("OCF").get(year)-balance.get("PPE").get(year);
		System.out.println("FCF: " + fcf);
		return fcf;
	}
	
	public double getFCFperShare(){
		double fcfps = (cfs.get("OCF").get(year)-balance.get("PPE").get(year))/income.get("Shares").get(year);
		System.out.println("FCF per share: " + fcfps);
		return fcfps;
	}
	
	public double getEA(){
//		System.out.println(balance.get("Equity").get(year));
//		System.out.println(balance.get("Assets").get(year));
		double EA = balance.get("Equity").get(year)/balance.get("Assets").get(year);
		System.out.println("E/A: " + EA);
		return EA;
	}
	
	public double getLA(){
		double LA = balance.get("Liabilities").get(year)/balance.get("Assets").get(year);
		System.out.println("L/A: " + LA);
		return LA;
	}
	
	public double getEPS(){
		return income.get("EPS").get(year);
	}
	
	public Map<String, Double> getJSON(){
		Map<String, Double> json = new HashMap<String, Double>();
		json.put("Ready", 1.0);
		json.put("FCF", getFCF());
		json.put("FCFperShare", getFCFperShare());
		json.put("EA", getEA());
		json.put("LA", getLA());
		json.put("EPS", getEPS());

		return json;
	}

}
