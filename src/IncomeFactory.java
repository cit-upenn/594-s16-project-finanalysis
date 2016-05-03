import java.io.File;
import java.io.FileNotFoundException;

/**
 * The factory class for generating Income Statement parsers
 * @author weiyin
 *
 */
public class IncomeFactory extends FinDocFactory{

	/**
	 * Method for making Income Statement Parsers
	 */
	@Override
	public FinDocParser makeFinDoc(String json) {
		// TODO Auto-generated method stub
		FinDocParser jinc = null;
		try {
			jinc = new FinDocParser(new File(json), 2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No Income Statement Found!");
		}
		return jinc;
	}

}
