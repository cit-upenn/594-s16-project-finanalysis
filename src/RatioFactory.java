import java.io.File;
import java.io.FileNotFoundException;

/**
 * The factory class for Key Ratio Parsers
 * @author weiyin
 *
 */
public class RatioFactory extends FinDocFactory{

	/**
	 * Method for making Key Ratio Parsers
	 */
	@Override
	public FinDocParser makeFinDoc(String json) {
		// TODO Auto-generated method stub
		FinDocParser jdat = null;
		try {
			jdat = new FinDocParser(new File(json), 3);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No Key Ratios Found!");
		}
		return jdat;
	}

}
