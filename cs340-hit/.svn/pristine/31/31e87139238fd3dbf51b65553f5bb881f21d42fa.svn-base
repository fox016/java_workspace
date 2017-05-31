
package model.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;

import model.DataSerializer;


/** 
	@author Nate Fox
	@version April 2 2013
*/

class SerializationFacade implements PersistenceFacade
{
	private static final String FILEPATH = "./";
	
	private static SerializationFacade _serializationFacade;
	
	public static SerializationFacade instance()
	{
		if(_serializationFacade == null)
			_serializationFacade = new SerializationFacade();
		return _serializationFacade;
	}
	
	private SerializationFacade()
	{
	}

	/** Stores a newly created Item in the data persistence model.
		@param ido The ItemDataObject representing the Item
	*/
	public void createItem(ItemDataObject ido)
	{
		return;
	}

	/** Updates an existing Item in the data persistence model.
		@param ido The ItemDataObject representing the Item
	*/
	public void updateItem(ItemDataObject ido)
	{
		return;
	}

	/** Removes an Item from the data persistence model.
		@param ido The ItemDataObject representing the Item
	*/
	public void deleteItem(ItemDataObject ido)
	{
		return;
	}

	/** Stores a newly created Product in the data persistence model.
		@param pdo The ProductDataObject representing the Item
	*/
	public void createProduct(ProductDataObject pdo)
	{
		return;
	}
	
	/** Updates an existing Product in the data persistence model.
		@param pdo The ProductDataObject representing the Item
	*/
	public void updateProduct(ProductDataObject pdo)
	{
		return;
	}

	/** Removes a Product from the data persistence model.
		@param pdo The ProductDataObject representing the Item
	*/
	public void deleteProduct(ProductDataObject pdo)
	{
		return;
	}

	/** Stores a newly created ProductGroup in the data persistence model.
		@param pgdo The GroupDataObject representing the Item
	*/
	public void createGroup(GroupDataObject pgdo)
	{
		return;
	}

	/** Updates an existing ProductGroup in the data persistence model.
		@param pgdo The GroupDataObject representing the Item
	*/
	public void updateGroup(GroupDataObject pgdo)
	{
		return;
	}

	/** Removes a ProductGroup from the data persistence model.
		@param pgdo The GroupDataObject representing the Item
	*/
	public void deleteGroup(GroupDataObject pgdo)
	{
		return;
	}

	/** Stores a newly created StorageUnit in the data persistence model.
		@param udo The UnitDataObject representing the Item
	*/
	public void createUnit(UnitDataObject udo)
	{
		return;
	}

	/** Updates an existing StorageUnit in the data persistence model.
		@param udo The UnitDataObject representing the Item
	*/
	public void updateUnit(UnitDataObject udo)
	{
		return;
	}

	/** Removes a StorageUnit from the data persistence model.
		@param udo The UnitDataObject representing the Item
	*/
	public void deleteUnit(UnitDataObject udo)
	{
		return;
	}

	/** Loads the persisted model into memory at application start 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException */
	public void loadAll() throws FileNotFoundException, ClassNotFoundException, IOException
	{
		DataSerializer.instance().load(FILEPATH);
	}

	/** Saves the memory model into persisted memory at application close 
	 * @throws IOException 
	 * @throws FileNotFoundException */
	public void saveAll() throws FileNotFoundException, IOException
	{
		DataSerializer.instance().save(FILEPATH);
	}

	/** Begins a transaction */
	public void begin()
	{
		return;
	}

	/** Commits a transaction */
	public void end()
	{
		return;
	}
}

