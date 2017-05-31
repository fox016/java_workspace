
package model.report;
import model.Item;
import model.ItemManager;
import model.StorageUnit;
import model.ProductGroup;
import model.Product;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

	/** 
		This class extracts information needed for the Removed Items Visitor
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

class RemovedItemsVisitor implements Visitor
{
	ReportBuilder builder;
	private final int COLUMNS = 5;
	Date startDate;
	/** Constructs a RemovedItemsVisitor
		@param builder The builder that constructs the Report
		@param startDate The startDate of the Removed Items Report
	 * @throws IOException 
	**/
	public RemovedItemsVisitor(ReportBuilder builder, Calendar startDate)
			throws IOException
	{
		this.builder = builder;
		this.startDate = startDate.getTime();
		builder.writeTitle("Items Removed Since " + formatDate(startDate.getTime()));
		builder.startTable(COLUMNS);	
		builder.writeTableHeader(getColumnTitles());
	}
	
	private String[] getColumnTitles(){
		String[] columnTitles = {"Description", "Size", "Product Barcode",
				"Removed", "Current Supply"};
		return columnTitles;
	}
	
	/**
	 * 
	 * @param date to be formatted
	 * @return a string with the desired format output
	 */
	private String formatDate(Date date)
	{
		//TODO fix format
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		return format.format(date);
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
		throw new UnsupportedOperationException("Not Implemented");
	}

	/** Visits a Product
		@param product The Product to visit
	*/
	public void visit(Product product)
	{
		Set<Item> removedItems = ItemManager.instance().getRemovedItems(product);
		Set<Item> liveItems = ItemManager.instance().getSystemItems(product);
		
		int itemsRemovedAfterDate = getItemsRemovedAmount(removedItems);
		if(itemsRemovedAfterDate == 0)
			return;
		
			String[] row = new String[COLUMNS];
			row[0] = product.getDescription();
			row[1] = product.getSize().toString();
			row[2] = product.getBarcode().toString();
			row[3] = String.valueOf(itemsRemovedAfterDate);
			row[4] = String.valueOf(liveItems.size());
			
			
			try {
				builder.writeTableRow(row);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	private int getItemsRemovedAmount(Set<Item> removedItems){
		int amount = 0;
		for(Item item : removedItems){
			Date itemExitDate = item.getExitTime().getTime();
			if(itemExitDate.compareTo(startDate) > 0)
				amount++;
		}	
		return amount;
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

