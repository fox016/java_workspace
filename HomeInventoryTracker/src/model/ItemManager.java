
package model;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.io.Serializable;
import java.util.NoSuchElementException;

import model.persistence.ItemDataObject;
import model.persistence.PersistenceFactory;

/**
 * This Singleton Class represents a Lookup Table
 * 	for Items based on their barcodes and if they are currently
 * 	in the Tracking System.
 * Impelemented with HashMaps and HashSets
 * @author Christopher Tensmeyer
 * @version Phase 1.0 Jan 26, 2013
*/
public class ItemManager implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2560037083665187901L;
	
	private static ItemManager _manager;
	private Map<ItemBarcode,Item> liveItems;
	private Map<ItemBarcode,Item> deadItems;
	private Map<ProductContainer,Set<Item>> itemsByProductContainer; //this is only live items
	private Map<Product,Set<Item>> itemsByProduct; //this is live and dead items
	private Map<ItemBarcode,Item> allItems;

	public String toString()
	{
		return "Live Items:\n" + liveItems + "\n" + "Dead Items:\n" + deadItems + "\n" +
			"All Items:\n" + allItems + "\n" + "By Product:\n" + itemsByProduct + "\n" +
			"By ProductContainer:\n" + itemsByProductContainer + "\n";
	}

	private ItemManager()
	{
		liveItems = new HashMap<ItemBarcode,Item>();
		deadItems = new HashMap<ItemBarcode,Item>();
		itemsByProductContainer = new HashMap<ProductContainer,Set<Item>>();
		itemsByProduct = new TreeMap<Product,Set<Item>>();
		allItems = new HashMap<ItemBarcode,Item>();
	}

	/** Returns the single instance of ItemManager
	 * 
	 * @return	The single instance of ItemManager
	 */
	public static ItemManager instance()
	{
		if(_manager == null)
			_manager = new ItemManager();
		return _manager;
	}
	
	public static void setInstance(ItemManager im)
	{
		_manager = im;
	}
	
	/**
	 * Resets instance
	 */
	public static void reset()
	{
		_manager = null;
	}

	/** Test for membership of an Item
		@param barcode The Barcode of the Item we are looking for
		@return Returns true if the ItemManager contains an Item with ItemBarcode barcode
	*/
	public boolean contains(ItemBarcode barcode)
	{
		assert(true);
		return allItems.containsKey(barcode);
	}

	/** Test for membership of an Item
		@param item The Item we are looking for
		@return Returns true if the ItemManager contains the Item
	*/
	public boolean contains(Item item)
	{
		if(item == null || item.getItemBarcode() == null)
			return false;
		return this.contains(item.getItemBarcode());
	}

	/** The number of Items contained
		@return the number of Items both in Storage and Removed
	*/
	public int totalItems()
	{
		assert(true);
		return allItems.size();
	}

	/** The number of Items in the Tracking System
		@return Returns the number of Items currenly in Storage
	*/
	public int numSystemItems()
	{
		assert(true);
		return liveItems.size();
	}

	/** The number of Removed Items
		@return Returns the number of Items that have been removed from Storage
	*/
	public int numRemovedItems()
	{
		assert(true);
		return deadItems.size();
	}

	/** Ask if the Item is currenly in the System
		@param barcode The ItemBarcode of the Item in question
		@return Returns true if the Item is in Storage
	*/
	public boolean isInSystem(ItemBarcode barcode)
	{
		assert(true);
		return liveItems.containsKey(barcode);
	}

	/** Ask if the Item has been removed from the System
		@param barcode The ItemBarcode of the Item in question
		@return Returns true if the Item has been removed
	*/
	public boolean isRemoved(ItemBarcode barcode)
	{
		assert(true);
		return deadItems.containsKey(barcode);
	}

	public void removeItem(Item item)
	{
		removeItem(item.getItemBarcode());
	}

	/** Removes an Item from the System
		@param barcode The ItemBarcode of the Item in question
		@throws ItemAlreadyRemovedException If the Item has already been removed
		@throws NoSuchElementException If the Item is not in the system
	*/
	public void removeItem(ItemBarcode barcode) throws ItemAlreadyRemovedException
	{
		if(!this.contains(barcode))
			throw new NoSuchElementException();
		if(deadItems.containsKey(barcode))
			throw new ItemAlreadyRemovedException(barcode);
		Item item = liveItems.get(barcode);
		item.remove();
		liveItems.remove(barcode);
		deadItems.put(barcode,item);
	}

	public void resurrectItem(ItemBarcode barcode)
	{
		if(!deadItems.containsKey(barcode))
			throw new NoSuchElementException();
		Item item = deadItems.get(barcode);
		item.resurrect();
		deadItems.remove(barcode);
		liveItems.put(barcode,item);
	}

	/** Annihilates an Item.  Like it Never existed.
		@param barcode The ItemBarcode of the Item you are annihilating
	*/
	public void annihilateItem(ItemBarcode barcode)
	{
		Item item = allItems.get(barcode);
		if(item == null)
			return;
		liveItems.remove(barcode);
		deadItems.remove(barcode);
		allItems.remove(barcode);
		ibp(item.getProduct()).remove(item);
		ProductContainer container = item.getStorageUnit().getProductContainer((item.getProduct()));
		if(container == null)
			container = item.getStorageUnit();
		ibpc(container).remove(item);
	}

	/** Undoes the annihilation of an Item.  Like it never didn't exist.
		@param barcode The ItemBarcode of the Item you are annihilating
	*/
	public void unAnnihilateItem(Item item)
	{
		allItems.put(item.getItemBarcode(), item);
		if(item.isInSystem())
			liveItems.put(item.getItemBarcode(), item);
		else
			deadItems.put(item.getItemBarcode(), item);
		addItemToProductSet(item);
		addItemToContainerSet(item);
	}


	/** Finds the Item
		@param barcode The ItemBarcode of the Item
		@return Returns - The Item if it is found, otherwise returns null
	*/
	public Item getItem(ItemBarcode barcode)
	{
		assert(true);
		return allItems.get(barcode);
	}

	/** Finds the Item if it is in the System
		@param barcode The ItemBarcode of the Item
		@return Returns - The Item if it is found in the System, otherwise returns null
	*/
	public Item getSystemItem(ItemBarcode barcode)
	{
		assert(true);
		if(liveItems.containsKey(barcode))
			return liveItems.get(barcode);
		else
			return null;
	}

	/** Finds the Item if it has been removed from the System
		@param barcode The ItemBarcode of the Item
		@return Returns - The Item if it has been removed from the system, otherwise null
	*/
	public Item getRemovedItem(ItemBarcode barcode)
	{
		assert(true);
		if(deadItems.containsKey(barcode))
			return deadItems.get(barcode);
		else
			return null;
	}

	/** Returns all Items of a particular product
		@param product The Product specifies what Items are returned
		@return Returns a Set of Items.  Can be empty.
	*/
	public Set<Item> getItems(Product product)
	{
		assert(true);
		Set<Item> items = copy(ibp(product));
		return items;
	}

	public Set<Item> getItems(ProductContainer container)
	{
		return getItems(container, false);
	}

	/** Returns all Items inside of a ProductContainer.
		@param container The ProductContainer containing the Items
		@param recurse A boolean flag.  If true, we return all Items contained
			in the children of container as well
		@return Returns a Set of Live Items.  Can be empty.
		@throws NullPointerException If container is null
	*/
	public Set<Item> getItems(ProductContainer container, boolean recurse)
	{
		if(container == null)
			throw new NullPointerException();
		Set<Item> items = copy(ibpc(container));

		if(recurse)
		{
			Set<String> childNames = container.getContainers();
			for(String name : childNames)
			{
				ProductContainer child = container.getProductContainer(name);
				Set<Item> childItems = this.getItems(child, true);
				items.addAll(childItems);
			}
		}
		return items;
	}

	public Set<Item> getSystemItems(ProductContainer container)
	{
		return getSystemItems(container, false);
	}

	/** Returns all Live Items inside of a ProductContainer
		@param container The ProductContainer containing the Items
		@param recurse A boolean flag.  If true, we return all Items contained
			in the children of container as well
		@return Returns a Set of Live Items.  Can be empty.
		@throws NullPointerException If container is null
	*/
	public Set<Item> getSystemItems(ProductContainer container, boolean recurse)
	{
		if(container == null)
			throw new NullPointerException();
		return filterLive(getItems(container, recurse));
	}

	/** Returns all Items inside of a ProductContainer of a specific product
		@param container The ProductContainer containing the Items
		@param product The Product specifying Item type
		@param recurse A boolean flag.  If true, we return all Items of product contained
			in the children of container as well
		@return Returns a Set of Live Items.  Can be empty.
		@throws NullPointerException If container is null or product is null
		TODO: should this have the options to recurse or not?
	*/
	public Set<Item> getItems(ProductContainer container, Product product, boolean recurse)
	{
		if(container == null || product == null)
			throw new NullPointerException();

		Set<Item> systemItemsOfProduct = getItems(product);
		Set<Item> itemsInProductContainer = getItems(container, recurse);
		Set<Item> items = emptySet();

		//computes the intersection of the two sets
		for(Item item : systemItemsOfProduct)
			if(itemsInProductContainer.contains(item))
				items.add(item);
		return items;
	}
	
	/** Returns all Items inside of a ProductContainer of a specific product
		@param container The ProductContainer containing the Items
		@param product The Product specifying Item type
		@param recurse A boolean flag.  If true, we return all Items of product contained
			in the children of container as well
		@return Returns a Set of Live Items.  Can be empty.
		@throws NullPointerException If container is null or product is null
		TODO: should this have the options to recurse or not?
	*/
	public Set<Item> getSystemItems(ProductContainer container, Product product, boolean recurse)
	{
		if(container == null || product == null)
			throw new NullPointerException();
		Set<Item> result = filterLive(getItems(container, product, recurse));
		return result;
	}

	/** Returns the Set of Items currently in the system of that product
		@param product The Product specifies what Items are returned
		@return Returns a Set of Items.  Can be empty.
	*/
	public Set<Item> getSystemItems(Product product)
	{
		assert(true);
		return filterLive(getItems(product));
	}

	/** Returns the Set of Items removed from the System of that Product type
		@param product The Product specifies what Items are returned
		@return Returns a Set of Items. Can be empty
	*/
	public Set<Item> getRemovedItems(Product product)
	{
		assert(true);
		return filterDead(getItems(product));
	}

	/** Returns a set of all Items
		@return Returns a set of all Items
	*/
	public Collection<Item> allItems()
	{
		assert(true);
		return allItems.values();
	}

	/** Returns a set of all Items in the System
		@return Returns a set of all Items in the System
	*/
	public Collection<Item> allSystemItems()
	{
		assert(true);
		return liveItems.values();
	}

	/** Returns a set of all Removed Items
		@return Returns a set of all Removed Items
	*/
	public Collection<Item> allRemovedItems()
	{
		assert(true);
		return deadItems.values();
	}

	/** Precondition: !hasSystemItem(product)
		Postcondition: All Items in the Removed Table are purged (permanently deleted)
		@param product The product we wish to purge
		@throws IllegalItemOperationException If the precondition is violated
		@throws NullPointerException If product is null
	*/
	public void purgeProduct(Product product) throws IllegalItemOperationException
	{
		if(product == null)
			throw new NullPointerException();
		if(hasSystemItem(product))
			throw new IllegalItemOperationException();
		itemsByProduct.remove(product);
			//Removing elements while iterating is difficult
		Set<ItemBarcode> toRemove = new HashSet<ItemBarcode>();
		for(ItemBarcode barcode : deadItems.keySet())
		{
			Item item = deadItems.get(barcode);
			if(item.isProduct(product))
				toRemove.add(barcode);
		}

		//purges all Items of that product
		for(ItemBarcode barcode : toRemove)
		{
			ItemDataObject ido = PersistenceFactory.createItemDataObject(allItems.get(barcode));
			PersistenceFactory.getFacade().begin();
			PersistenceFactory.getFacade().deleteItem(ido);
			PersistenceFactory.getFacade().end();
			deadItems.remove(barcode);
			allItems.remove(barcode);
		}
	}

	/** Check the System for an Item of that Product 
		@param product The product we are searching for
		@return Returns true if there is an Item in the system that is an instance
			of product
	*/
	public boolean hasSystemItem(Product product)
	{
		assert(true);
		return !getSystemItems(product).isEmpty();
	}

	/** Inserts the item into the table
	*/
	public boolean insert(Item item, ItemBarcode barcode)
	{
		if(barcode == null || item == null)
			throw new NullPointerException();
		if(!item.isValidItem() || !item.isInSystem())
			throw new IllegalArgumentException("Invalid Item");
		if(this.contains(item))
			return false;
		if(item.getItemBarcode() != null)
			throw new ItemException("Item already has Barcode but it not indexed");

		//this is the only place where barcodes are assigned to Items
		item.setItemBarcode(barcode);
		allItems.put(item.getItemBarcode(),item);
		liveItems.put(item.getItemBarcode(),item);
		addItemToProductSet(item);
		addItemToContainerSet(item);

		return true;
	}

	/** This evaluates to true if we can construct an Item from these parameters
		@param item The Item we want to add
		@return Returns true if this item can be inserted into the table
	*/
	public boolean canInsertItem(Item item)
	{
		return item.isValidItem() && item.isInSystem() && item.getItemBarcode() == null;
	}
	
	public boolean canTransferItem(Item item, StorageUnit newUnit)
	{
		assert(true);
		
		if(item == null || newUnit == null)
			return false;
		
		if(contains(item) && item.isInSystem() && !item.getStorageUnit().equals(newUnit))
			return true;
		
		return false;
	}

	private void moveItemsOfProduct(ProductContainer from, ProductContainer to, Product product)
	{
		Set<Item> fromItems = ibpc(from);
		Set<Item> toItems = ibpc(to);

		Set<Item> movingItems = filterProduct(fromItems, product);
		for(Item item : movingItems)
		{
			fromItems.remove(item);
			toItems.add(item);
		}
	}

	/** Updates the Item indexing to reflect removing a Product from a ProductContainer
			All Items of that product should be moved to the Storage Unit level
		@param container This should be a Product Group. 
		@param product The product beeing removed.
		@throws NullPointerException If container or product is null
	*/
	public void removeProduct(ProductContainer container, Product product)
	{
		if(container == null)
			throw new NullPointerException("ProductContainer is null");
		if(product == null)
			throw new NullPointerException("Product is null");
		//should be equivilant
		moveItemsOfProduct(container, container.getRoot(), product);
	}

	/** Adds a Product to a ProductContainer
		@param container This should be a Product Group. 
		@param product The product beeing removed.
		@throws NullPointerException If container or product is null
	*/
	public void addProduct(ProductContainer container, Product product)
	{
		System.out.println("ItemManager addProduct()");
		System.out.println(container);
		System.out.println(product);
		
		if(container == null)
			throw new NullPointerException("ProductContainer is null");
		if(product == null)
			throw new NullPointerException("Product is null");
		//should be equivilant
		moveItemsOfProduct(container.getRoot(), container,  product);
	}

	/** transfers the item to the appropiate StorageUnit
		@param item The Item to be transfered
		@param storageUnit Where the Item is moving.  It will be added to the appropiate
			sub ProductGroup where Product of the Item resides
		@throws NullPointerException If item or target is null
	*/
	public void transfer(Item item, StorageUnit storageUnit)
	{
		System.out.println("ItemManager transfer");
		if(item == null)
			throw new NullPointerException("Item is null");
		if(storageUnit == null)
			throw new NullPointerException("Storage Unit is null");

		//remove the Item from it's previous container
		ProductContainer from = getLocation(item);
		ibpc(from).remove(item);

		//add the Item to it's new location
		ProductContainer to = storageUnit.getProductContainer(item.getProduct());
		if(to == null)
			to = storageUnit;
		ibpc(to).add(item);
		item.setStorageUnit(storageUnit);
	}

	public ProductContainer getLocation(Item item)
	{
		assert(item != null);
		Product product = item.getProduct();
		ProductContainer temp = item.getStorageUnit().getProductContainer(product);
		return temp;
	}
	

	/** You can only purge a Product from the system if no items of that Product exist
		@param product The product we wish to purge from the system
		@throws NullpointerException If product is null
		@return Returns true if no Items are live in the system of this product
	*/
	public boolean canPurge(Product product)
	{
		assert(true);
		return !hasSystemItem(product);
	}

	private Set<Item> emptySet()
	{
		assert(true);
		return new HashSet<Item>();
	}

	private Set<Item> filterLive(Set<Item> items)
	{
		assert(items != null);
		Set<Item> result = emptySet();
		for(Item item : items)
			if(item.isInSystem())
				result.add(item);
		return result;
	}

	private Set<Item> filterDead(Set<Item> items)
	{
		assert(items != null);
		Set<Item> result = emptySet();
		for(Item item : items)
			if(!item.isInSystem())
				result.add(item);
		return result;
	}

	private Set<Item> filterProduct(Set<Item> items, Product p)
	{
		assert(items != null);
		Set<Item> result = emptySet();
		for(Item item: items)
			if(item.isProduct(p))
				result.add(item);
		return result;
	}

	private void addItemToProductSet(Item item)
	{
		Set<Item> productItems = ibp(item.getProduct());
		productItems.add(item);
	}

	private void addItemToContainerSet(Item item)
	{
		if(item.getStorageUnit() == null)
		{
			Set<Item> containerItems = ibpc(null);
			containerItems.add(item);
			return;
		}
			
		ProductContainer container = item.getStorageUnit().getProductContainer((item.getProduct()));
		if(container == null)
			container = item.getStorageUnit();
		Set<Item> containerItems = ibpc(container);
		containerItems.add(item);
	}

	//For accessing itemsByProductContainer.  If there is no mapping,
	//add an empty set mapping, so we don't pass back null
	private Set<Item> ibpc(ProductContainer container)
	{
		Set<Item> temp = itemsByProductContainer.get(container);
		if(temp == null)
		{
			temp = emptySet();
			itemsByProductContainer.put(container, temp);
		}
		return temp;
	}
			
	//For accessing itemsByProductContainer.  If there is no mapping,
	// add an empty set mapping, so we don't pass back null
	private Set<Item> ibp(Product product)
	{
		Set<Item> temp = itemsByProduct.get(product);
		if(temp == null)
		{
			temp = emptySet();
			itemsByProduct.put(product, temp);
		}
		return temp;
	}

	private Set<Item> copy(Set<Item> other)
	{
		assert(other != null);
		Set<Item> copy = emptySet();
		copy.addAll(other);
		return copy;
	}
}


