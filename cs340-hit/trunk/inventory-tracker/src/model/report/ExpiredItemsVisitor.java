
package model.report;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import model.Item;
import model.ItemManager;
import model.StorageUnit;
import model.ProductGroup;
import model.Product;

	/** 
		This class extracts information needed for the Expired Item Report
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

class ExpiredItemsVisitor implements Visitor
{
	ReportBuilder builder;
	/**
	 * Number of columns in the report
	 */
	private final int COLUMNS = 6;

	/** Constructs an ExpiredItemVisitor
		@param builder The builder that constructs the Report
	 * @throws IOException 
	*/
	public ExpiredItemsVisitor(ReportBuilder builder) throws IOException
	{
		this.builder = builder;
		builder.writeTitle("Expired Items");
		builder.startTable(COLUMNS);	
		builder.writeTableHeader(getColumnTitles());
		
	}
	
	/**
	 * @return an array of strings representing the column headers of the expired items report.
	 */
	
	private String[] getColumnTitles(){
		String[] columnTitles = {"Description","Storage Unit", "Product Group", "Entry Date"
				, "Expire Date", "Item Barcode"};
		return columnTitles;
	}
	
	private String formatDate(Date date)
	{
		//TODO fix format
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		return format.format(date);
	}

	/** Visits a StorageUnit
		@param unit The StorageUnit to visit
	*/
	public void visit(StorageUnit unit)
	{
		List<Item> items = new ArrayList<>(ItemManager.instance().getSystemItems(unit, false));
		sortItems(items);
		for(Item item : items){
			//TODO: refactor to use item.isExpiredBy(Calendar)
			Calendar itemExpCal = item.getExpirationDate();
			Date curDate = Calendar.getInstance().getTime();
			if(itemExpCal == null){
				continue;
			}
			Date itemExpDate = itemExpCal.getTime();			
			if(itemExpDate.compareTo(curDate) > 0)
				continue;
			
			String itemExpDateStr = formatDate(itemExpDate);
			String entryDateStr = formatDate(item.getEntryDate().getTime());
			
			String[] row = new String[COLUMNS];
			row[0] = item.getDescription();
			row[1] = unit.getName();
			row[2] = "";
			row[3] = entryDateStr;		
			row[4] = itemExpDateStr;
			row[5] = item.getItemBarcode().toString();
			try {
				builder.writeTableRow(row);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/** Visits a ProductGroup
		@param group The ProductGroup to visit
	*/
	public void visit(ProductGroup group)
	{
		List<Item> items = new ArrayList<>(ItemManager.instance().getSystemItems(group, false));
		sortItems(items);
		for(Item item : items){
			//TODO: refactor to use item.isExpiredBy(Calendar)
			if(item.getExpirationDate() == null)
				continue;
			Date itemExpDate = item.getExpirationDate().getTime();
			Date curDate = Calendar.getInstance().getTime();
			if(itemExpDate == null){
				continue;
			}
			if(itemExpDate.compareTo(curDate) > 0)
				continue;
			
			String itemExpDateStr = formatDate(itemExpDate);
			String entryDateStr = formatDate(item.getEntryDate().getTime());
			
			String[] row = new String[COLUMNS];
			row[0] = item.getDescription();
			row[1] = group.getRoot().getName();
			row[2] = group.getName();
			row[3] = entryDateStr;		
			row[4] = itemExpDateStr;
			row[5] = item.getItemBarcode().toString();
			try {
				builder.writeTableRow(row);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static void sortItems(List<Item> items)
	{
		Collections.sort(items,
			new Comparator<Item>()
				{ public int compare(Item i1, Item i2)
					{
						int descCmp = i1.getDescription().compareTo(i2.getDescription());
						if(descCmp == 0)
							return i1.getEntryDate().compareTo(i2.getEntryDate());
						else
							return descCmp;
					}
				});
	}
	
	public void finish(){
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
	/** Visits a Product
		@param product The Product to visit
	*/
	public void visit(Product product)
	{
		throw new UnsupportedOperationException("Not Implemented");
	}

}

