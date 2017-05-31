
package model.persistence;

import model.ProductGroup;

/** The class representing a Serialized Product Group
	@author Chris Tensmeyer
	@version March 25 2013
*/

class SerializationGroupDataObject implements GroupDataObject
{
	private ProductGroup group;

	/** Creates a SerializationGroupDataObject representing a ProductGroup 
		@param group The Product Group to be represented
	*/
	public SerializationGroupDataObject(ProductGroup group)
	{
		this.group = group;
	}

	public ProductGroup getGroup()
	{
		return group;
	}

}

