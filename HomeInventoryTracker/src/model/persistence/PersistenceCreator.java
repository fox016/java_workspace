
package model.persistence;

import model.*;

/** The interface for the data persistence abstract factory
	@author Chris Tensmeyer
	@version March 25 2013
*/

public interface PersistenceCreator
{
	/** Factory method to get a reference to the single PersistenceFacade
		@return The singleton PersistenceFacade
	*/
	PersistenceFacade getFacade();

	/** Factory method to create an ItemDataObejct
		@param item The model object represented by the returned DataObject
		@return Returns a new ItemDataObject representing item
	*/
	ItemDataObject createItemDataObject(Item item);

	/** Factory method to create a ProductDataObejct
		@param product The model object represented by the returned DataObject
		@return Returns a new ProductDataObject representing product
	*/
	ProductDataObject createProductDataObject(Product product);

	/** Factory method to create a GroupDataObejct
		@param group The model object represented by the returned DataObject
		@return Returns a new GroupDataObject representing product
	*/
	GroupDataObject createGroupDataObject(ProductGroup group);

	/** Factory method to create a UnitDataObject
		@param unit The model object represented by the returned DataObject
		@return Returns a new UnitDataObject representing unit
	*/ UnitDataObject createUnitDataObject(StorageUnit unit);

}





