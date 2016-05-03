
/**
 * The abstract superclass for the financial document parser factories. 
 * It has the factory method.
 * @author weiyin
 *
 */
public abstract class FinDocFactory {

	public abstract FinDocParser makeFinDoc(String json);
}
