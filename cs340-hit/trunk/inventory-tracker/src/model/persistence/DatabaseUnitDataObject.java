
package model.persistence;

import java.util.HashSet;
import java.util.Set;

import model.ProductBarcode;
import model.StorageUnit;

/** The class representing a Database Storage Unit
	@author Chris Tensmeyer
	@version March 25 2013
*/

class DatabaseUnitDataObject implements UnitDataObject
{
	private StorageUnit unit;

	/** Creates a DatabaseUnitDataObject representing a StorageUnit 
		@param unit The Storage Unit to be represented
	*/
	public DatabaseUnitDataObject(StorageUnit unit)
	{
		this.unit = unit;
	}

	public StorageUnit getUnit()
	{
		return unit;
	}

	/** @return Returns the string form of the Storage Unit's name*/
	public String name()
	{
		return unit.getName();
	}

	/** @return Returns the Set of strings representing all Products in the Storage Unit */
	public Set<String> pBarcodes()
	{
		Set<String> barcodeStr = new HashSet<String>();
		Set<ProductBarcode> barcodes = unit.getProducts();
		
		for(ProductBarcode barcode : barcodes)
		{
			barcodeStr.add(barcode.toString());
		}
		
		return barcodeStr;
	}

	/** @return Returns the Set of children Ids of the Storage Unit */
	public Set<Integer> children()
	{
		return unit.getChildIDs();
	}
	
	/**
	 * 
	 * @return Unique ProductContainer id for this Storage Unit
	 */
	public int unitId()
	{
		return unit.getContainerID();
	}

}

