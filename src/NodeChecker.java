import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class NodeChecker extends TimerTask implements Runnable{
	private ReentrantLock myLock;
	private JsonReader jsRead;
	private String condition;
	private boolean decision;
	private Gson gson;

	public NodeChecker(File json) throws FileNotFoundException{
		this.jsRead = new JsonReader(new FileReader(json));
		this.condition = null;
		this.myLock = new ReentrantLock();
		this.gson= new Gson();
	}
	@Override
	public void run() {
		try {
			
//				System.out.println("hi");
				readCondition(jsRead);
//				Map<String, String> obj = gson.fromJson(jsRead, Map.class);

//				Thread.sleep(500);
				
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readCondition(JsonReader check) throws IOException{
//		System.out.println("here!");
//		
//		check.beginObject();
//	     while (check.hasNext()) {
//	    	 System.out.println("in loop!");
//	       String name = check.nextName();
//	       if (name.equals("check")) {
//	    	    condition = check.nextString();
//	         System.out.println("Check: " + condition);
//	         if(condition.equals("OK")){
//	        	 decision = true;
//	         }else{
//	        	 decision = false;
//	         }
//	       }  else{
//	    	   check.skipValue();
//	       }
//	     }
//	     check.endObject();
	     

		
	}
	
	public boolean getDecision(){
		System.out.println("Decision is " + decision);
		return decision;
	}
	


}
