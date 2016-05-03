import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ValuationTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timer executor = new Timer();
		FinDocParser jbal = null;
		FinDocParser jcf = null;
		FinDocParser jinc = null;
		ArrayList<FinDocParser> docs = new ArrayList<FinDocParser>();
//		NodeCommunicator nc = null;
		
		try {
//			nc = new NodeChecker(new File("check.json"));
			
//			jbal = new FinDocParser(new File("balance.json"), 0);
//			jcf = new FinDocParser(new File("CashFlow.json"), 1);
//			jinc = new FinDocParser(new File("Income.json"), 2);
//			docs.add(jbal);
//			docs.add(jcf);
//			docs.add(jinc);
//			NodeCommunicator.getInstance(docs,"NodetoJava.txt");
			executor.scheduleAtFixedRate(NodeCommunicator.getInstance("NodetoJava.txt"), 0, 3000);


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

				


	}

}
