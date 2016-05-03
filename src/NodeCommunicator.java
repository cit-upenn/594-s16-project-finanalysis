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
//	private ReentrantLock myLock;
	private boolean decision;
	private String text;
	private HashMap<String, Double> content;
	private PrintWriter out;
	private PrintWriter out2;
	private Gson gson;
	private String json;
	private static NodeCommunicator comm;
	ArrayList<FinDocParser> docs;


	
	
	/**
	 * The constructor for NodeCommunicator. It takes in an ArrayList of documents and 
	 * a txt doc that tells it to start/stop the JSON-making.
	 * @param docs the parsed financial docs
	 * @param text a text file that tells the communicator to start/stop parsing
	 * @throws FileNotFoundException
	 */
	private NodeCommunicator(String text) throws FileNotFoundException{
		this.gson = new Gson();
		this.text = text;
//		this.myLock = new ReentrantLock();

		this.content = new HashMap<String, Double>();
		content.put("Ready", 0.0);
		this.json = gson.toJson(content);;
		this.docs = new ArrayList<FinDocParser>();

	}
	
	public synchronized static NodeCommunicator getInstance(String text) throws FileNotFoundException{
		if(comm == null){
			comm = new NodeCommunicator( text);
		}
		return comm;
	}
	@Override
	public synchronized void run() {
		try {
//				System.out.println("in try!");
				this.out = new PrintWriter(new FileOutputStream("JavatoNode.json", false));
				this.out2 = new PrintWriter(new FileOutputStream("JavatoNode.json", false));
				FinDocParser jbal = new FinDocParser(new File("balance.json"), 0);
				FinDocParser jcf = new FinDocParser(new File("CashFlow.json"), 1);
				FinDocParser jinc = new FinDocParser(new File("Income.json"), 2);
				File file = new File(text);
				BufferedReader in = new BufferedReader(new FileReader(file));
				while (in.lines() != null) {
					String line = in.readLine();
					if(line == null){
						in.close();
						break;
					}
					if(line.equals("OKAY")){
						
						System.out.println("in NodeComm, Company: " + jbal.getCompany());
						docs.add(jbal);
						docs.add(jcf);
						docs.add(jinc);
						System.out.println("OKAY: documents will be parsed!");
						decision = true;
						System.out.println("decision-- " + decision);
					}else{
						decision = false;
					}
				}
				
				out2.println(json);
				out2.close();
				
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
					System.out.println("JSON WRITTEN!!!");
					out.close();
				}
				

				

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error getting Input/Output!");
			e.printStackTrace();
		}
	}
	
//	public void checkStatus(){
////		content = "{\"status\": true}";
//	}
	
	/**
	 * This method gets the decision boolean, which determines whether parsing happens
	 * this interval or not
	 * @return the decision boolean; true if parsing, false if not
	 */
	private boolean getDecision(){
		System.out.println("Decision is " + decision);
		return decision;
	}
	
	private void setOutput(Map<String, Double> valuation){
		json = gson.toJson(valuation);
		
	}
	


}
