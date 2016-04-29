import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class NodeCommunicator extends TimerTask implements Runnable{
	private ReentrantLock myLock;
	private boolean decision;
	private String text;
	private HashMap<String, Double> content;
	private PrintWriter out;
	private PrintWriter out2;
	private Gson gson;
	private String json;
	ArrayList<FinDocParser> docs;

	
	

	public NodeCommunicator(ArrayList<FinDocParser> docs, String text) throws FileNotFoundException{
//		this.jsRead = new JsonReader(new FileReader(json));
		this.gson = new Gson();
		this.text = text;
		this.myLock = new ReentrantLock();
		this.out = new PrintWriter(new FileOutputStream("JavatoNode.json", false));
		this.out2 = new PrintWriter(new FileOutputStream("JavatoNode.json", false));
//		this.out2 = new PrintWriter("")
		this.content = new HashMap<String, Double>();
		content.put("Ready", 0.0);
		this.json = gson.toJson(content);;
		this.docs = docs;

	}
	@Override
	public void run() {
		try {
				System.out.println("in try!");
				File file = new File(text);
				BufferedReader in = new BufferedReader(new FileReader(file));
				while (in.lines() != null) {
					String line = in.readLine();
					if(line == null){
						in.close();
						break;
					}
					if(line.equals("OKAY")){
						System.out.println("OKAY");
						decision = true;
						System.out.println("decision-- " + decision);
					}
				}
				
				out2.println(json);
				out2.close();
				
//				if(json != null){
//					out.println(json);
//					
//				}
				if(getDecision() == true){
					HashMap<String, ArrayList<Double>> balanceData = new HashMap<String, ArrayList<Double>>();
					HashMap<String, ArrayList<Double>> CFData = new HashMap<String, ArrayList<Double>>();
					HashMap<String, ArrayList<Double>>incData = new HashMap<String, ArrayList<Double>>();
					for(int i = 0; i < docs.size(); i++){
						if(docs.get(i).getDocument() == 0){
							balanceData = docs.get(i).getFinData();

						}else if (docs.get(i).getDocument() == 1){
							CFData = docs.get(i).getFinData();

						}else if (docs.get(i).getDocument() == 2){
							incData = docs.get(i).getFinData();

						}
					}
					Valuator vt0 = new Valuator(balanceData, CFData, incData, 0);
					
					setOutput(vt0.getJSON());
					out.println(json);
					out.close();
				}
				

				

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void checkStatus(){
//		content = "{\"status\": true}";
	}
	
	public boolean getDecision(){
		System.out.println("Decision is " + decision);
		return decision;
	}
	
	public void setOutput(Map<String, Double> valuation){
		json = gson.toJson(valuation);
		
	}
	


}
