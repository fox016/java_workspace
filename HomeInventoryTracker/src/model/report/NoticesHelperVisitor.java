
package model.report;
import model.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;

	/** 
		This class helps the NoticesVisitor by traversing a subtree
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

class NoticesHelperVisitor implements Visitor
{
	private SupplyType type;
	private Map<Product, ProductGroup> map;

	NoticesHelperVisitor(SupplyType type)
	{
		this.type = type;
		map = new TreeMap<Product, ProductGroup>();
	}

	/** Visits a StorageUnit
		@param unit The StorageUnit to visit
	*/
	public void visit(StorageUnit unit)
	{
		throw new UnsupportedOperationException("NoticesHelperVisitor does not visit StorageUnits");
	}

	/** Visits a ProductGroup
		@param group The ProductGroup to visit
	*/
	public void visit(ProductGroup group)
	{
		Set<ProductBarcode> barcodes = group.getProducts();
		for(ProductBarcode barcode: barcodes)
		{
			Product product = ProductManager.instance().getProductByBarcode(barcode);
			if(!SupplyType.isSame(this.type, product.getSize().getType()))
				map.put(product, group);
		}
	}

	Map<Product, ProductGroup> getResult()
	{
		return map;
	}


	/** Visits a Product
		@param product The Product to visit
	*/
	public void visit(Product product)
	{
		throw new UnsupportedOperationException("NoticesHelperVisitor does not visit Products");
	}

	public void finish()
	{

	}
}

