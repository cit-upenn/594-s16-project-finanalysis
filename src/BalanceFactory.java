import java.io.File;
import java.io.FileNotFoundException;

/**
 * The factory class for Balance Sheet Parsers
 * @author weiyin
 *
 */
public class BalanceFactory extends FinDocFactory{

	/**
	 * Method for making Balance Sheet Parsers
	 */
	@Override
	public FinDocParser makeFinDoc(String json) {
		// TODO Auto-generated method stub
		FinDocParser jbal = null;
		try {
			jbal = new FinDocParser(new File(json), 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No Balance Sheet Found!");
		}
		return jbal;
	}

}
