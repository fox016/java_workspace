
package model.report;
import model.*;

import java.util.Set;

	/** 
		This class helps the NMonthSupplyVisitor by finding the current supply
		of a certain SupplyType (Count, Weight, or Volume) of a Product Group subtree
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

class NMonthSupplyHelperVisitor implements Visitor
{
	private double amount;
	private SupplyType type;

	/** Constructs a NMonthSupplyHelpderVisitor
	*/
	public NMonthSupplyHelperVisitor(SupplyType type)
	{
		this.type = type;
		this.amount = 0.0;
	}

	/** Visits a StorageUnit
		@param unit The StorageUnit to visit
	*/
	public void visit(StorageUnit unit)
	{
		throw new UnsupportedOperationException("Not Implemented");
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
			processProduct(group, product);
		}
	}

	private void processProduct(ProductGroup group, Product product)
	{
		Size supply = product.getSize();
		//skip if the SupplyTypes are not the same category
		if(!SupplyType.isSame(this.type, supply.getType()))
			return;
		Size converted = new Size(supply, this.type);
		int count = ItemManager.instance().getSystemItems(group, product, false).size();

		this.amount += count * converted.getAmount();
	}

	/** Visits a Product
		@param product The Product to visit
	*/
	public void visit(Product product)
	{
		throw new UnsupportedOperationException("Not Implemented");
	}

	Size getResult()
	{
		return new Size(this.amount, this.type);
	}

	public void finish()
	{ }

}

