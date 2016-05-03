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

import com.google.gson.Gson;

/**
 * The NodeCommunicator class runs in intervals to communicate with the Node.js codes.
 * It waits for a start message and periodically ultilizes the FinDocParser and Valuator
 * to interpret the JSONs and send a JSON back to Node. It utilizes the Singleton pattern.
 * @author weiyin
 *
 */
public class NodeCommunicator extends TimerTask implements Runnable{
	private boolean decision;
	private String text;
	private HashMap<String, Double> content;
	private PrintWriter out;
	private PrintWriter out2;
	private Gson gson;
	private String json;
	private static NodeCommunicator comm;
	ArrayList<FinDocParser> docs;
	FinDocFactory balFac;
	FinDocFactory cfFac;
	FinDocFactory incFac;

	
	
	/**
	 * The constructor for NodeCommunicator. It takes in a txt doc that tells it 
	 * to start the JSON-making.
	 * @param text a text file that tells the communicator to start/stop parsing
	 * @throws FileNotFoundException
	 */
	private NodeCommunicator(String text) throws FileNotFoundException{
		this.gson = new Gson();
		this.text = text;
		this.balFac = new BalanceFactory();
		this.cfFac = new CashFlowFactory();
		this.incFac = new IncomeFactory();
		this.content = new HashMap<String, Double>();
		content.put("Ready", 0.0);
		this.json = gson.toJson(content);;
		this.docs = new ArrayList<FinDocParser>();

	}
	/**
	 * the method for getting the single NodeCommunicator instance. As a scheduler there
	 * is no reason to have more than one instance.
	 * @param text
	 * @return
	 * @throws FileNotFoundException
	 */
	public synchronized static NodeCommunicator getInstance(String text) throws FileNotFoundException{
		if(comm == null){
			comm = new NodeCommunicator( text);
		}
		return comm;
	}
	/**
	 * The thread starts by creating new instances of the I/O and the Factories to
	 * ensure the json data is refreshed. It then determines whether to parse the JSONs
	 * or not, and outputs the message back to Node in JSON form once analysis is complete
	 * 
	 */
	@Override
	public synchronized void run() {
		try {
//				2 PrintWriters to deal with 2 possibilities of output. Makes things simpler
				this.out = new PrintWriter(new FileOutputStream("JavatoNode.json", false));
				this.out2 = new PrintWriter(new FileOutputStream("JavatoNode.json", false));
//				parsers are generated from the factories
				FinDocParser jbal = balFac.makeFinDoc("balance.json");
				FinDocParser jcf = cfFac.makeFinDoc("CashFlow.json");
				FinDocParser jinc = incFac.makeFinDoc("Income.json");

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
	
	
	/**
	 * This method gets the decision boolean, which determines whether parsing happens
	 * this interval or not
	 * @return the decision boolean; true if parsing, false if not
	 */
	private boolean getDecision(){
		System.out.println("Decision is " + decision);
		return decision;
	}
	
	/**
	 * This method sets the output that will be written as a JSON
	 * @param valuation The Map of various valuation metrics
	 */
	private void setOutput(Map<String, Double> valuation){
		json = gson.toJson(valuation);
		
	}
	


}
