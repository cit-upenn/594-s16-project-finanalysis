
/**
 * The abstract superclass for the financial document parser factories. 
 * It has the factory method.
 * @author weiyin
 *
 */
public abstract class FinDocFactory {

	/**
	 * The abstract factory method for making parsers
	 * @param json name of json
	 * @return the right parser
	 */
	public abstract FinDocParser makeFinDoc(String json);
}
