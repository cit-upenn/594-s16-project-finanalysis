import java.io.FileNotFoundException;
import java.util.Timer;


/**
 * This class provides a main method for running the NodeCommunicator
 * @author weiyin
 *
 */
public class FinDocAnalyzer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timer executor = new Timer();
		
		try {
//			The NodeCommunicate is run every 3 seconds
			executor.scheduleAtFixedRate(NodeCommunicator.getInstance("NodetoJava.txt"), 0, 3000);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

				


	}

}
