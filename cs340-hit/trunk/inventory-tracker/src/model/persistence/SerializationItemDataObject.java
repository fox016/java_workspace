
package model.persistence;

import model.Item;

/** The class representing a Serialized Item
	@author Chris Tensmeyer
	@version March 25 2013
*/

class SerializationItemDataObject implements ItemDataObject
{
	private Item item;

	/** Creates a SerializationItemDataObject representing an Item
		@param item The Item to be represented
	*/
	public SerializationItemDataObject(Item item)
	{
		this.item = item;
	}

	public Item getItem()
	{
		return item;
	}

}
