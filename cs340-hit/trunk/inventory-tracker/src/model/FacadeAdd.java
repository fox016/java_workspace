/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
class FacadeAdd {
	
	private static PersistenceFacade pf = PersistenceFactory.getFacade();

	static StorageUnit addStorageUnit(String name) throws IllegalArgumentException {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Invalid Storage Unit Name");
		}
		StorageUnit newUnit = new StorageUnitImpl(name);
		House.instance().addStorageUnit(newUnit);
		Facade.setChangedFlag();
		Notification n =
				new Notification(OperationType.CREATE, ObjectType.STORAGE_UNIT, newUnit);
		Facade.notifyAllObservers(n);
		
		UnitDataObject unitObj = PersistenceFactory.createUnitDataObject(newUnit);
		pf.begin();
		pf.createUnit(unitObj);
		pf.end();
		
		return newUnit;
	}

	static ProductGroup addProductGroup(String name, Size supply, ProductContainer parent)
			throws IllegalArgumentException {
		ProductGroup newPG = parent.addContainer(name);
		newPG.setSupply(supply);
		Facade.setChangedFlag();
		Notification n =
				new Notification(OperationType.CREATE, ObjectType.PRODUCT_CONTAINER, newPG);
		Facade.notifyAllObservers(n);
		
		GroupDataObject groupObj = PersistenceFactory.createGroupDataObject(newPG);
		pf.begin();
		pf.createGroup(groupObj);
		pf.end();
		
		return newPG;
	}

	/* Is this ever being called? Does it need to update the database?
	 * - Nate
	 	It is internally.  Changed to private
		- Chris
	 */
	static Item addItem(Item item) throws IllegalArgumentException {
		if (ItemManager.instance().canInsertItem(item))
		{
			ItemManager.instance().insert(item, new ItemBarcode());
			return item;
		}
		else 
		{
			throw new IllegalArgumentException("Invalid item");
		}
	}

	/* Resets product entry date to match oldest item entry date
	 */
	static void addItem(Item item, StorageUnit storageUnit)
	{
		ProductManager.instance().updateProductDate(item.getProduct(), item.getEntryDate());
		addItem(item);
		
		// Moved to AddItemBatchController
		/*
		pf.begin();
		ItemDataObject itemObj = PersistenceFactory.createItemDataObject(item);
		pf.createItem(itemObj);
		ProductDataObject productObj =
			PersistenceFactory.createProductDataObject(item.getProduct());
		pf.updateProduct(productObj);
		pf.end();
		*/
    }
	
	/*
	 * Is this ever being called? Does it need to update the database?
	 * - Nate
	 */
	static Item createItem(Product p, Calendar entry, StorageUnit unit)
	{
		return new Item(p, unit, entry);
	}

	/*
	 */
	public static void addBatchItems(List<Item> items)
	{
		pf.begin();
		for(Item item : items)
		{
			addItem(item, item.getStorageUnit());
			ItemDataObject itemObj = PersistenceFactory.createItemDataObject(item);
			pf.createItem(itemObj);
		}

		if(items.size() > 0)
		{
			ProductDataObject productObj =
					PersistenceFactory.createProductDataObject(items.get(0).getProduct());
			pf.updateProduct(productObj);
		}

		pf.end();
	}
	
	/**
	 * * @deprecated. Use AddItem(item, storageUnit) instead, called in a loop.
	 * */
	static List<Item> addBatchItems(Product product, Calendar entry,
			StorageUnit unit, int count) throws IllegalArgumentException
	{
		//Check to see if the products entry Date needs updated
		ProductManager.instance().updateProductDate(product, entry);
		//Add count items
		List<Item> addedItems = new ArrayList<Item>();
		for (int i = 0; i < count; i++) {
			Item item = new Item(product, unit, entry);
			addItem(item);
			addedItems.add(item);
		}
		Facade.setChangedFlag();
		Facade.notifyAllObservers(new Notification(OperationType.CREATE,
				ObjectType.ITEM, addedItems));
		return addedItems;
	}

        
	/**
	 * Creates a product, adding it to the product manager.
	 * @param p the product to add.
	 */
	static void createProduct(Product p)
	{
		if (ProductManager.instance().containsProduct(p.getBarcode()))
		{
			throw new IllegalArgumentException("Product already exists!");
		}	
		ProductManager.instance().addProduct(p);
		Facade.setChangedFlag();
		Notification n = new Notification(OperationType.CREATE,ObjectType.PRODUCT, p);
		Facade.notifyAllObservers(n);
	
		ProductDataObject groupObj = PersistenceFactory.createProductDataObject(p);
		pf.begin();
		pf.createProduct(groupObj);
		pf.end();
	}
        
        
    /**
     * Adds a product to a product container. This method used to also check to see
     * if the product existed: if it didn't, it would create it by adding it to the
     * product manager. IT NO LONGER DOES! Now, that functionality is handled by the
     * createProduct method in this class.
     * @param product the product to add.
     * @param container the container to add it to.
     * @throws IllegalArgumentException if the product doesn't exist.
     * @deprecated. Use createProduct, and/or addProductToSrotageUnitByBarcode
     */
	static void addProductToContainer(Product product, ProductContainer container)
			throws IllegalArgumentException
        {
	    if (product == null || container == null) 
            { // Called when user clicks "cancel" from add product view
	    		Facade.setChangedFlag();
	    		Notification n = new Notification(OperationType.CREATE,
	    					ObjectType.PRODUCT, null);
	    		Facade.notifyAllObservers(n);
	    		return;
            }
            //Error detection: Product creation is handled elsewhere now.
            if (!ProductManager.instance().containsProduct(product.getBarcode())) 
            {
                throw new IllegalArgumentException("Product must exist! "
                        + "(See javadoc for addProductToContainer)");
            }
            
            StorageUnit unit = container.getRoot();
            ProductContainer oldPC = FacadeGet.getProductContainer(unit, product);
            if (oldPC == null) 
            {
		ProductManager.instance().addContainer(product, container);
		container.addProduct(product.getBarcode());
            } 
            else 
            {
		ItemManager.instance().removeProduct(oldPC, product);
		ProductManager.instance().removeProductFromContainer(product, oldPC);
		oldPC.removeProduct(product.getBarcode());
		container.addProduct(product.getBarcode());
		ProductManager.instance().addContainer(product, container);
		ItemManager.instance().addProduct(container, product);	
            }
            Facade.setChangedFlag();
            Notification n =
            		new Notification(OperationType.CREATE,ObjectType.PRODUCT, product);
            Facade.notifyAllObservers(n);

    		pf.begin();
    		//ProductDataObject productObj =
    			//PersistenceFactory.createProductDataObject(product);
    		//pf.updateProduct(productObj);
				if(oldPC != null)
				{
					if(oldPC instanceof StorageUnit)
					{
						UnitDataObject unitObj =
								PersistenceFactory.createUnitDataObject((StorageUnit) oldPC);
						pf.updateUnit(unitObj);
					}
					else
					{
						GroupDataObject groupObj =
								PersistenceFactory.createGroupDataObject((ProductGroup) oldPC);
						pf.updateGroup(groupObj);
					}
				}
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
	
        /**
	 * @param unitName
	 * @return
	 */
	static boolean canAddStorageUnit(String unitName)
	{
		// Case for null/empty handled by House's isValidName
		assert(true);
		
		return House.instance().isValidName(unitName);
	}
	
	/**
	 * @param name
	 * @param parent
	 * @return
	 */
	static boolean canAddProductGroup(String name, ProductContainer parent)
	{
		assert(true);
		
		if(parent == null)
			return false;
		
		return parent.isValidName(name);
	}
	
	/**
	 * @param item
	 * @return
	 */
	static boolean canAddItem(Item item)
	{
		assert(true);
		
		if(item == null)
			return false;
		
		return ItemManager.instance().canInsertItem(item);
	}
	
	/**
	 * @param product
	 * @param container
	 * @return
	 */
	static boolean canAddProductToContainer(Product product, ProductContainer container)
	{
		
		if(product == null || container == null)
			return false;
		
		// Test for valid product
		if(!ProductManager.instance().canAddProduct(product))
			return false;
		
		return true;
	}

	/**
	 * Adds a product to a storage unit, taking in the productBarCode of the
	 * product in question. This assumes that the storage unit doesn't have the
	 * product inside of it anywhere.
	 * @pre container.hasProduct(product)==false
	 * @param product the productBarCode to add
	 * @param container the container to add it to
	 */
	static void addProductToStorageUnitByBarcode(ProductBarcode product, 
	                                           StorageUnit container)
	{
		if (container.hasProduct(product))
	    {
	        throw new IllegalArgumentException("Pre condition violated: container "+
	        			"already contains product!");
	    }
	
	    ProductManager.instance().addContainer(ProductManager.instance().
	                                           getProductByBarcode(product), container);
	    container.addProduct(product);
			
		Facade.setChangedFlag();
		Notification n = new Notification(OperationType.CREATE,
		            ObjectType.PRODUCT, ProductManager.instance().getProductByBarcode(product));
		Facade.notifyAllObservers(n);
		
		UnitDataObject unitObj = PersistenceFactory.createUnitDataObject(container);
		pf.begin();
		pf.updateUnit(unitObj);
		pf.end();
	}
    
            /**
         * Removes a product from a storage unit, taking in the productBarCode of the
         * product in question. This assumes that the storage unit has the
         * product inside of it anywhere.
         * @pre container.hasProduct(product)==true
         * @param product the productBarCode to add
         * @param container the container to add it to
         */
    static void removeProductFromStorageUnitByBarcode(ProductBarcode product, 
                                               StorageUnit container)
    {
	if (!container.getProducts(false).contains(product))
        {
            throw new IllegalArgumentException("Pre condition violated: container "+
                      "does not contain product in top level!");
        }
	
        ProductManager.instance().removeProductFromContainer(ProductManager.instance().
                                               getProductByBarcode(product), container);
        container.removeProduct(product);
		
		Facade.setChangedFlag();
		Notification n = new Notification(OperationType.DELETE,
	                ObjectType.PRODUCT, ProductManager.instance().getProductByBarcode(product));
		Facade.notifyAllObservers(n);
		
		UnitDataObject unitObj = PersistenceFactory.createUnitDataObject(container);
		pf.begin();
		pf.updateUnit(unitObj);
		pf.end();
    }

    static void uneradicateItem(Item i)
    {
        ItemManager.instance().unAnnihilateItem(i);
        
        ItemDataObject itemObj = PersistenceFactory.createItemDataObject(i);
        pf.begin();
        pf.updateItem(itemObj);
        pf.end();
    }

    static void commitItemBatch() 
    {
        Facade.setChangedFlag();
	Notification n = new Notification(OperationType.CREATE,
                ObjectType.ITEM, null); //Doesn't hurt if this is null... I think.
	Facade.notifyAllObservers(n);
    }
	
}

