
package model.persistence;

import model.Product;

/** The class representing a Serialized Product
	@author Chris Tensmeyer
	@version March 25 2013
*/

class SerializationProductDataObject implements ProductDataObject
{
	private Product product;

	/** Creates a SerializationProductDataObject representing a Product
		@param product The Product to be represented
	*/
	public SerializationProductDataObject(Product product)
	{
		this.product = product;
	}

	public Product getProduct()
	{
		return product;
	}

}

