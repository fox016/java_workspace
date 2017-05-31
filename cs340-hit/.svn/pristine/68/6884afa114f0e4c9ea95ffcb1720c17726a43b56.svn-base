
package model;

/**
 * Class ItemAlreadyRemovedException.  When you attempt to remove an Item
 * 	that has previously been removed, this exception should be thrown
 * @author Christopher Tensmeyer
 * @version Phase 1.0 Jan 26, 2013
*/
public class ItemAlreadyRemovedException extends IllegalItemOperationException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7210973291967032614L;
	
	ItemBarcode barcode;
	
	/** Default Constructor */
	public ItemAlreadyRemovedException()
	{}
	
	/** Indicates the Cause of the Exception
		@param bc - The barcode of the Not Found Item
	*/
	public ItemAlreadyRemovedException(ItemBarcode bc)
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
		return "Item with barcode: " + cause + " has already been removed!";
	}
}
