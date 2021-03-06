
package model.persistence;

import model.Product;

/** The class representing a Database Product
	@author Chris Tensmeyer
	@version March 25 2013
*/

class DatabaseProductDataObject implements ProductDataObject
{
	private Product product;

	/** Creates a DatabaseProductDataObject representing a Product
		@param product The Product to be represented
	*/
	public DatabaseProductDataObject(Product product)
	{
		this.product = product;
	}

	public Product getProduct()
	{
		return product;
	}

	/** @return Returns the unix timestamp of the Product's Creation Date */
	public long creationDate()
	{
		return product.getCreationDate().getTimeInMillis();
	}

	/** @return Returns the string form of the Product's Barcode */
	public String barcode()
	{
		return product.getBarcode().toString();
	}

	/** @return Returns the string form of the Product's Description */
	public String description()
	{
		return product.getDescription();
	}

	/** @return Returns the amount of the Product's Size */
	public double amount()
	{
		if(product.getSize() != null)
			return product.getSize().getAmount();
		else
			return 0;
	}
	
	/** @return Returns the unit of the Product's Size */
	public String amountUnit()
	{
		if(product.getSize() != null)
			return product.getSize().getType().toString();
		else
			return "";
	}

	/** @return Returns the shelf life in months of the Product */
	public int shelfLife()
	{
		return product.getShelfLife();
	}

	/** @return Returns the 3 Month Supply of the Product */
	public int threeMonthSupply()
	{
		return product.getThreeMonthSupply();
	}
}

