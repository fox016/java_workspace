
package model.report;
import model.*;
import java.io.IOException;

	/** 
		This class extracts information needed for the N-Month Supply Report
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

class NMonthSupplyVisitor implements Visitor
{
	private static final int PRODUCT_NUM_COLUMNS = 4;
	private static final int PRODUCT_GROUP_NUM_COLUMNS = 4;
	private int numMonths;
	private ReportBuilder builder;

	/** Constructs a NMonthSupplyVisitor
		@param builder The builder that constructs the Report
		@param numMonths The number of months to use in
			constructing the N-Month Supply Report
	*/
	NMonthSupplyVisitor(ReportBuilder builder, int numMonths) throws IOException
	{
		this.builder = builder;
		this.numMonths = numMonths;
		builder.writeTitle(numMonths + "-Month Supply Report");
		builder.writeBlankLine();
	}

	void prepProducts() throws IOException
	{
		builder.writeTextHeader("Product");
		builder.writeBlankLine();
		builder.startTable(PRODUCT_NUM_COLUMNS);
		builder.writeTableHeader(getProductHeaders());
	}
	
	void prepProductGroups() throws IOException
	{
		builder.endTable();
		builder.writeBlankLine();
		builder.writeTextHeader("Product Groups");
		builder.writeBlankLine();
		builder.startTable(PRODUCT_GROUP_NUM_COLUMNS);
		builder.writeTableHeader(getGroupHeaders());
	}

	private String[] getProductHeaders()
	{
		return new String[] {"Description", "Barcode",
									(numMonths + "-Month Supply"), "Current Supply"};
	}

	private String[] getGroupHeaders()
	{
		return new String[] {"Product Group", "StorageUnit",
									(numMonths + "-Month Supply"), "Current Supply"};
	}
	/** Visits a StorageUnit
		@param unit The StorageUnit to visit
	*/
	public void visit(StorageUnit unit)
	{
		//do nothing because they don't have a supply 
	}

	/** Visits a ProductGroup
		@param group The ProductGroup to visit
	*/
	public void visit(ProductGroup group)
	{
		Size threeMonthSupply = group.getSupply();
		if(threeMonthSupply.getAmount() < 0)
			return;
		double nAmount = threeMonthSupply.getAmount() / 3.0 * this.numMonths;
		if(threeMonthSupply.getType() == SupplyType.COUNT)
			nAmount = Math.ceil(nAmount);
		Size nMonthSupply = new Size(nAmount, threeMonthSupply.getType());

		Size currentSupply = getCurrentGroupSupply(group);
		if(currentSupply.getAmount() < nAmount)
		{
			String pg = group.getName();
			String su = group.getRoot().getName();
			String nms = nMonthSupply.toString();
			String curSupply = currentSupply.toString();
			write(pg, su, nms, curSupply);
		}
	}

	/** Visits a Product
		@param product The Product to visit
	*/
	public void visit(Product product)
	{
		int threeMonthSupply = product.getThreeMonthSupply();
		if(threeMonthSupply < 1)
			return;
		double nMonthSupply = (threeMonthSupply / 3.0 * this.numMonths);
		int currentSupply = getCurrentSupply(product);

		if(currentSupply < nMonthSupply)
		{
			String desc = product.getDescription();
			String bar = product.getBarcode().toString();
			String nSupply = String.valueOf(nMonthSupply) + " count";
			String curSupply = String.valueOf(currentSupply) + " count";
			write(desc, bar, nSupply, curSupply);
		}
	}

	private int getCurrentSupply(Product product)
	{
		return ItemManager.instance().getSystemItems(product).size();
	}

	private Size getCurrentGroupSupply(ProductGroup group)
	{
		if(group.getSupply().getType() == SupplyType.COUNT)
			return new Size(ItemManager.instance().getSystemItems(group, true).size(),
								 SupplyType.COUNT);
		NMonthSupplyHelperVisitor helper =
				new NMonthSupplyHelperVisitor(group.getSupply().getType());
		group.accept(helper);

		return helper.getResult();
	}

	private void write(String one, String two, String three, String four)
	{
		try
		{
			builder.writeTableRow(new String[] {one, two, three, four});
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void finish()
	{
		try
		{
			builder.endTable();
			builder.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}

