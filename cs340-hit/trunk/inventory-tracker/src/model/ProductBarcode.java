package model;

/** A Product Barcode is a representation of a product's manufactured barcode.
 *  Each Product has exactly one Product Barcode that uniquely identifies it
 * 
 * @author Nathan Fox
 *
 */
public class ProductBarcode extends Barcode
{
	private static final long serialVersionUID = 1343463344592050610L;

	/** Constructor
	 * 
	 * @throws IllegalArgumentException value cannot be null
	 * @param value	String representation of barcode value
	 */
	public ProductBarcode(String value) throws IllegalArgumentException
	{
		if(value == null)
		{
			throw new IllegalArgumentException("ProductBarcode cannot " +
					"be initialized with null value");
		}
		
		barcodeString = value;
	}
}
