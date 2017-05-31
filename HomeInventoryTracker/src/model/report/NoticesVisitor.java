
package model.report;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.io.IOException;


	/** 
		This class extracts information needed for the Notices Report
		@author Christopher Tensmeyer
		@verison March 6th, 2013
	*/

class NoticesVisitor implements Visitor
{
	private ReportBuilder builder;

	/** Constructs a NoticesVisitor
		@param builderParam The builder that constructs the Report
	*/
	public NoticesVisitor(ReportBuilder builderParam)
	{
		builder = builderParam;
		try
		{
			builder.writeTitle("Notices Report");
			builder.writeBlankLine();
			builder.writeBlankLine();
			builder.writeTextHeader("3-Month Supply Warnings");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/** Visits a StorageUnit
		@param unit The StorageUnit to visit
	*/
	public void visit(StorageUnit unit)
	{
		//does nothing to a Storage Unit because they don't have a 3 Month Supply
	}

	/** Visits a ProductGroup
		@param group The ProductGroup to visit
	*/
	public void visit(ProductGroup group)
	{
		SupplyType groupType = group.getSupply().getType();
		if(SupplyType.isCount(groupType))
			return;

		Map<Product, ProductGroup> productMap = getMismatchedProducts(group);

		if(productMap.isEmpty())
			return;
		List<Product> products = new ArrayList<Product>(productMap.keySet());
		ReportUtils.sortProducts(products);
		writeProductGroup(group);

		for(Product product: products)
		{
			writeProduct(product, productMap.get(product));
		}
	}

	private Map<Product, ProductGroup> getMismatchedProducts(ProductGroup group)
	{
		NoticesHelperVisitor visitor = new NoticesHelperVisitor(group.getSupply().getType());
		group.accept(visitor);

		return visitor.getResult();
	}

	private void writeProductGroup(ProductGroup group)
	{
		String str = "Product Group ";
		str += group.getRoot().getName() + "::" + group.getName();
		str += " has a 3-month supply (" + group.getSupply().toString() + ")";
		str += " that is inconsistent with the following products:";
		try
		{
			builder.writeBlankLine();
			builder.writeBlankLine();
			builder.writeTextLine(str);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void writeProduct(Product product, ProductGroup group)
	{
		String str = "- " + group.getName() + "::" + product.getDescription();
		str += "(size: " + product.getSize().toString() + ")";
		try
		{
			builder.writeTextLine(str);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}


	/** Visits a Product
		@param product The Product to visit
	*/
	public void visit(Product product)
	{
		throw new UnsupportedOperationException("NoticesVisitor does not visit Products");
	}

	public void finish()
	{

	}

}

