package model;

import java.io.Serializable;

/** This abstract class is a parent of ItemBarcodes and ProductBarcodes.
 *  It contains implementation details for shared functions between
 *  the two child classes.
 * 
 * @author Nate Fox
 *
 */
public abstract class Barcode implements Serializable, Comparable<Barcode>
{
	private static final long serialVersionUID = -1423968393343568640L;
	
	protected String barcodeString;
	
	/** Gives string representation of barcode value
	 * 
	 * @throws IllegalStateException Barcodes should have 
	 * string representations upon being constructed
	 * 
	 * @return string representation of barcode value
	 */
	@Override
	public String toString() throws IllegalStateException
	{
		if(barcodeString == null)
		{
			throw new IllegalStateException("Barcodes should have string" +
					"representations upon being constructed");
		}
		
		return barcodeString;
	}
	
	/** Gives length of string representation of barcode value,
	 *  useful to know if long representation of value drops leading
	 *  zeroes
	 * 
	 * @throws IllegalStateException Barcodes should have 
	 * string representations upon being constructed
	 * 
	 * @return length of string representation of barcode value
	 */
	public int getLength() throws IllegalStateException
	{
		if(barcodeString == null)
		{
			throw new IllegalStateException("Barcodes should have string" +
					"representations upon being constructed");
		}
		
		return barcodeString.length();
	}
	
	/** Tests equality based on long representation of barcode
	 * 
	 * @return true iff other object has same barcode value
	 */
	@Override
	public boolean equals(Object other) throws IllegalArgumentException
	{
		if(other == null)
		{
			return false; //You NEED to be able to compare something to null!
                        //if the other barcode is null, then obviously they are not equal
                        //to each other. This is standard practice in java, because in
                        //java, all objects implement NullType by default, so can be
                        //compared polymorphicly to null.
		}
		
		if(other instanceof Barcode)
			return this.toString().equals(((Barcode)other).toString());
		
		throw new IllegalArgumentException("Can only compare to other Barcodes");
	}
	
	/** Compares two barcodes
	 * 
	 * @throws IllegalArgumentException other cannot be null
	 * @return 0 iff other object has same barcode value, non-zero int otherwise
	 */
	@Override
	public int compareTo(Barcode other) throws IllegalArgumentException
	{
		if(other == null)
		{
			throw new IllegalArgumentException("Cannot compare Barcode to null");
		}
		
		if(other instanceof Barcode)
			return this.toString().compareTo(other.toString());
		
		throw new IllegalArgumentException("Can only compare to other Barcodes");
	}

	@Override
	public int hashCode()
	{
		return barcodeString.hashCode();
	}
}
