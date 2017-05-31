
package model;

/**
 * Class ItemNotFoundException.  When you attempt to perform an operation
 * 	on an Item that doesn't exist, this exception should be thrown
 * @author Christopher Tensmeyer
 * @version Phase 1.0 Jan 26, 2013
*/
public class ItemNotFoundException extends ItemException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4748540339150213800L;
	
	ItemBarcode barcode;
	
	/** Default Constructor */
	public ItemNotFoundException()
	{}
	
	/** Indicates the Cause of the Exception
		@param bc - The barcode of the Not Found Item
	*/
	public ItemNotFoundException(ItemBarcode bc)
	{
		barcode = bc;

	}

	/** Returns a Descripton of the Exception
		@return Returns a Descripton of the Exception, including the ItemBarcode
			used to locate the Item
	*/
	public String toString()
	{
		String cause = (barcode != null) ? barcode.toString() : "unknown";
		return "Item with barcode: " + cause + " was not found";
	}
}
