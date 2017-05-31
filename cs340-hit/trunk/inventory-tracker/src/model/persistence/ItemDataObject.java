
package model.persistence;

import model.Item;

/** The interface for the DataObjects 
	@author Chris Tensmeyer
	@version March 25 2013
*/

public interface ItemDataObject extends DataObject
{
	Item getItem();
}

