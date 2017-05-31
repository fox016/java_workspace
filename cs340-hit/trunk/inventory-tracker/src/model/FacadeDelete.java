/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.NoSuchElementException;
import java.util.Set;

import model.persistence.GroupDataObject;
import model.persistence.ItemDataObject;
import model.persistence.PersistenceFacade;
import model.persistence.PersistenceFactory;
import model.persistence.ProductDataObject;
import model.persistence.UnitDataObject;

/**
 *
 * @author chris
 */
class FacadeDelete {
	
	private static PersistenceFacade pf = PersistenceFactory.getFacade();
	
	/**
	 * @param item
	 * @throws ItemAlreadyRemovedException
	 * @throws NoSuchElementException
	 */
	static void removeItemFromStorage(Item item)
		throws ItemAlreadyRemovedException, NoSuchElementException
	{
		ItemManager.instance().removeItem(item.getItemBarcode());
		Facade.setChangedFlag(); // notifyObservers only works if this is called first
		Notification n = new Notification(OperationType.DELETE,
				ObjectType.ITEM, item);
		Facade.notifyAllObservers(n);
		
		ItemDataObject itemObj = PersistenceFactory.createItemDataObject(item);
		pf.begin();
		pf.updateItem(itemObj);
		pf.end();
	}
	
	// ----------------------------------------------------------------- Delete
	static void deleteProductFromContainer(Product product, ProductContainer container)
	{
		if(canDeleteProductFromContainer(product, container))
		{
			container.removeProduct(product.getBarcode());
			ProductManager.instance().removeProductFromContainer(product, container);
			//ItemManager makes no distinction between unindexing
			//a Product and returning an emptySet
			
			ProductDataObject productObj = PersistenceFactory.createProductDataObject(product);
			pf.begin();
			pf.updateProduct(productObj);
			if(container instanceof StorageUnit)
			{
				UnitDataObject unitObj =
						PersistenceFactory.createUnitDataObject((StorageUnit) container);
				pf.updateUnit(unitObj);
			}
			else
			{
				GroupDataObject groupObj =
						PersistenceFactory.createGroupDataObject((ProductGroup) container);
				pf.updateGroup(groupObj);
			}
			pf.end();
		}
		else
		{
			//do nothing
		}

	}
	
	/**
	 * @param unit
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws NullPointerException
	 */
	static void deleteStorageUnit(StorageUnit unit)
		throws IllegalArgumentException, IllegalStateException,
		NullPointerException
	{
		if(unit == null)
			throw new IllegalArgumentException("Cannot delete null unit");
		
		// Unit cannot hold items
		Set<Item> items = ItemManager.instance().getSystemItems(unit, true);
		if(!items.isEmpty())
			throw new IllegalStateException("Cannot delete storage unit until you " +
					"remove all items");
		
		House.instance().removeStorageUnit(unit);
		
		UnitDataObject unitObj = PersistenceFactory.createUnitDataObject(unit);
		pf.begin();
		pf.deleteUnit(unitObj);
		pf.end();
	}
	
	/**
	 * @param group
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws NullPointerException
	 * @throws NoSuchElementException
	 */
	static void deleteProductGroup(ProductGroup group)
			throws IllegalArgumentException, IllegalStateException,
			NullPointerException, NoSuchElementException
	{
		if(group == null)
			throw new IllegalArgumentException("Cannot delete null product group");
		
		// Grup cannot hold items
		Set<Item> items = ItemManager.instance().getSystemItems(group, true);
		if(!items.isEmpty())
			throw new IllegalStateException("Cannot delete product group until you " +
					"remove all items");
		
		group.getParent().deleteContainer(group.getName());
		
		GroupDataObject groupObj = PersistenceFactory.createGroupDataObject(group);
		pf.begin();
		pf.deleteGroup(groupObj);
		pf.end();
	}
	
	/**
         * Despite it's name, it actually purges a product.
	 * @param product
	 * @throws NullPointerException
	 * @throws IllegalItemOperationException
	 */
	static void deleteProduct(Product product)
		throws NullPointerException, IllegalItemOperationException
	{
		if(ItemManager.instance().canPurge(product))
		{
			ItemManager.instance().purgeProduct(product);
			ProductManager.instance().removeProduct(product);
			//need to traverse over the tree to remove the Product everywhere
			for(StorageUnit unit : House.instance().getAllUnits())
			{
				if(unit.getProductContainer(product) != null)
				unit.getProductContainer(product).removeProduct(product.getBarcode());
			}
		}
                else
                {
                    throw new IllegalArgumentException("Tried to purge an unpurgable product!");
                }
		
		ProductDataObject productObj = PersistenceFactory.createProductDataObject(product);
		pf.begin();
		pf.deleteProduct(productObj);
		pf.end();
	}
	
	// ------------------------------------------------------------- Validation
	
	
	/**
	 * @param item
	 * @return
	 */
	static boolean canRemoveItemFromStorage(Item item)
	{
		if(item == null)
			return false;
	
		return ItemManager.instance().isInSystem(item.getItemBarcode());
	}
	
	/**
	 * @param unit
	 * @return
	 */
	static boolean canDeleteStorageUnit(StorageUnit unit)
	{
		if(unit == null)
			return false;
		
		Set<Item> items = ItemManager.instance().getSystemItems(unit, true);
		return items.isEmpty();
	}
	
	/**
	 * @param group
	 * @return
	 */
	static boolean canDeleteProductGroup(ProductGroup group)
	{
		if(group == null)
			return false;
		
		Set<Item> items = ItemManager.instance().getSystemItems(group, true);
		return items.isEmpty();
	}
	
	/**
	 * @param product
	 * @return
	 */
	static boolean canDeleteProduct(Product product)
	{
		if(product == null)
			return false;
		
		return ItemManager.instance().canPurge(product);
	}

	static boolean canDeleteProductFromContainer(Product product, ProductContainer container)
	{
		Set<Item> itemsOfProduct = ItemManager.instance().getSystemItems(container, product, false);
		return itemsOfProduct.isEmpty();
	}

    static void resurrectItemToStorage(Item item) 
    {
        ItemManager.instance().resurrectItem(item.getItemBarcode());
        Facade.setChangedFlag(); // notifyObservers only works if this is called first
        Notification n = new Notification(OperationType.DELETE,
        		ObjectType.ITEM, item); //I figure that anything that needed
        //to change because an item was removed will be notified again, and
        //the item will be readded. I'll test this to make sure, though.
        Facade.notifyAllObservers(n);
	
        ItemDataObject itemObj = PersistenceFactory.createItemDataObject(item);
        pf.begin();
        pf.updateItem(itemObj);
        pf.end();
    }

    static void eradicateItem(Item i) 
    {
        ItemManager.instance().annihilateItem(i.getItemBarcode());
	
        ItemDataObject itemObj = PersistenceFactory.createItemDataObject(i);
        pf.begin();
        pf.deleteItem(itemObj);
        pf.end();
    }

}
