import java.io.File;
import java.io.FileNotFoundException;
/**
 * The factory class for Cash Flow Statement Parsers
 * @author weiyin
 *
 */
public class CashFlowFactory extends FinDocFactory{

	/**
	 * Method for making Cash Flow Statement Parsers
	 */
	@Override
	public FinDocParser makeFinDoc(String json) {
		// TODO Auto-generated method stub
		FinDocParser jcf = null;
		try {
			jcf = new FinDocParser(new File(json), 1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No Cash Flow Statement found!");
			
		}
		return jcf;
	}

}
