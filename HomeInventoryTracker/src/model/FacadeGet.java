package model;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * FacadeGet contains implementations of functions that the GUI can
 * call to access the core model
 * 
 * @author Nate Fox
 *
 */
class FacadeGet
{
	
	/**
	 * @param parent
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Set<String> getChildContainers(ProductContainer parent)
			throws IllegalArgumentException
	{
		if(parent == null)
			throw new IllegalArgumentException("Cannot get children of null" +
					" product container");
		
		return parent.getContainers();
	}
	
	/**
	 * @param container
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Set<ProductBarcode> getProductsByContainer(ProductContainer container)
			throws IllegalArgumentException
	{
		if(container == null)
			throw new IllegalArgumentException("Cannot get products of null" +
					" product container");
		
		return container.getProducts();
	}
	
	/**
	 * @param container
	 * @return
	 * @throws NullPointerException
	 */
	public static Set<Item> getItemsByContainer(ProductContainer container)
		throws NullPointerException
	{
		return ItemManager.instance().getSystemItems(container, false);
	}
	
	/**
	 * @param unit
	 * @param product
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Set<Item> getItemsByProduct(StorageUnit unit, Product product)
			throws IllegalArgumentException
	{
		if(unit == null || product == null)
			throw new IllegalArgumentException("Cannot get items of null " +
					"storage unit or null product");
		
		return ItemManager.instance().getSystemItems(unit, product, true);
	}
	
	/**
	 * @param unit
	 * @param product
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static ProductContainer getProductContainer(StorageUnit unit, Product product)
		throws IllegalArgumentException
	{
		if(unit == null || product == null)
			throw new IllegalArgumentException("Cannot get container of null " +
					"storage unit or null product");
		
		return unit.getProductContainer(product);
	}


	public static Set<StorageUnit> getStorageUnits()
	{
		return House.instance().getAllUnits();
	}
	
	/**
	 * @param barcode
	 * @return
	 */
	public static Product getProductByBarcode(ProductBarcode barcode)
	{
		if(barcode == null)
			throw new IllegalArgumentException("Cannot get product by null barcode");
		
		return ProductManager.instance().getProductByBarcode(barcode);
	}
	
}



