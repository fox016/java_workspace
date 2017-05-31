
package model.persistence;

import java.util.HashSet;
import java.util.Set;

import model.ProductBarcode;
import model.ProductGroup;

/** The class representing a Database Product Group
	@author Chris Tensmeyer
	@version March 25 2013
*/

class DatabaseGroupDataObject implements GroupDataObject
{
	private ProductGroup group;

	/** Creates a DatabaseGroupDataObject representing a ProductGroup 
		@param group The Product Group to be represented
	*/
	public DatabaseGroupDataObject(ProductGroup group)
	{
		this.group = group;
	}

	public ProductGroup getGroup()
	{
		return group;
	}

	/** @return Returns the string form of the Group's name*/
	public String name()
	{
		return group.getName();
	}

	/** @return Returns the Set of strings representing all Products in the Group */
	public Set<String> pBarcodes()
	{
		Set<String> barcodeStr = new HashSet<String>();
		Set<ProductBarcode> barcodes = group.getProducts();
		
		for(ProductBarcode barcode : barcodes)
		{
			barcodeStr.add(barcode.toString());
		}
		
		return barcodeStr;
	}

	/** @return Returns the Set of children Ids of the Group */
	public Set<Integer> children()
	{
		return group.getChildIDs();
	}

	/** @return Returns the Id of the Group's Parent */
	public int parent()
	{
		return group.getParent().getContainerID();
	}

	/** @return Returns the Id of the Group's StorageUnit */
	public int root()
	{
		return group.getRoot().getContainerID();
	}

	/** @return Returns the amount of the Product Group's Size */
	public double amount()
	{
		return group.getSupply().getAmount();
	}
	
	/** @return Returns the unit of the Product Group's Size */
	public String amountUnit()
	{
		return group.getSupply().getType().toString();
	}
	
	/**
	 * @return Unique ProductContainerID of Product Group
	 */
	public int groupId()
	{
		return group.getContainerID();
	}
}

