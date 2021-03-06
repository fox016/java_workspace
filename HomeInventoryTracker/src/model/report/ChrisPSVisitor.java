
package model.report;

import model.*;
import java.util.*;

	/** 
		This interface defines the Abstraction for the Visitor Pattern
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

public class ChrisPSVisitor implements Visitor
{
	private ReportBuilder builder;
	private Calendar start;
	private Calendar end;
	private Calendar endExact;
	private static final int NUM_COLS = 10;

	public ChrisPSVisitor(ReportBuilder b, int numMonths)
	{
		builder = b;
		//now
		end = Calendar.getInstance();
		endExact = end;
		end = copyMidnight(end);
		Calendar tmp = Calendar.getInstance();
		tmp.add(Calendar.MONTH, -1 * numMonths);
		start = copyMidnight(tmp);
		start(numMonths);
	}

	public ChrisPSVisitor(ReportBuilder b, Calendar s, Calendar e)
	{
		builder = b;
		start = copyMidnight(s);
		end = copyMidnight(e);
		endExact = (Calendar) e.clone();
		start(diff((Calendar)start.clone(), end, Calendar.MONTH));
	}

	//linear in the difference
	int diff(Calendar one, Calendar two, int field)
	{
		if(two.before(one))
		{
			Calendar tmp = two;
			two = one;
			one = two;
		}
		//one is before two;
		int diff = 0;

		do
		{
			one.add(field, 1);
			diff++;
		} while(one.before(two));
		
		return diff;
	}

	private void start(int numMonths)
	{
		try
		{
			builder.writeTitle("Product Report (" + numMonths + " Months)");
			builder.startTable(NUM_COLS);
			builder.writeTableHeader(getHeaders());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private String[] getHeaders()
	{
		return new String[] { "Description", "Barcode", "Size", "3-Month Supply",
			"Supply: Cur/Avg", "Supply: Min/Max", "Supply: Used/Added", "Shelf Life",
			"Used Age: Avg/Max", "Cur Age: Avg/Max"};
	}

	private Calendar copyMidnight(Calendar c)
	{
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		GregorianCalendar cal = new GregorianCalendar(year, month, day, 0, 0, 0);
		return cal;
	}

	/** Visits a StorageUnit
		@param unit The StorageUnit to visit
	*/
	public void visit(StorageUnit unit)
	{
		throw new UnsupportedOperationException("Not Implemeneted");
	}

	/** Visits a ProductGroup
		@param group The ProductGroup to visit
	*/
	public void visit(ProductGroup group)
	{
		throw new UnsupportedOperationException("Not Implemeneted");
	}

	/** Visits a Product
		@param product The Product to visit
	*/
	public void visit(Product product)
	{
		String desc = product.getDescription();
		String barcode = product.getBarcode().toString();
		String size = product.getSize().toString();
		int _3ms = product.getThreeMonthSupply();
		String threeMonthSupply = (_3ms == 0) ? "" : String.valueOf(_3ms);

		String supplyCurAvg = supplyCurAvg(product);
		String supplyMinMax = supplyMinMax(product);
		String supplyUsedAdded = supplyUse(product);

		int sl = product.getShelfLife();
		String shelfLife = (sl == 0) ? "" : (sl + " months");

		String usedAge = usedAge(product);
		String curAge = curAge(product);

		String[] row = new String[] { desc, barcode, size, threeMonthSupply,
				supplyCurAvg, supplyMinMax, supplyUsedAdded, shelfLife,  usedAge, curAge};
		try
		{
			builder.writeTableRow(row);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private Calendar startDay(Product p)
	{
		Calendar day = (Calendar) start.clone();
		Calendar prodCreation = p.getCreationDate();
		if(prodCreation.after(day))
		{
			day = copyMidnight(prodCreation);
		}

		return day;
	}

	private String formatDouble(double d)
	{
		d *= 10;
		d += 0.5;
		d = (double) (int) d;
		d /= 10.0;
		return String.valueOf(d);
	}

	private String supplyCurAvg(Product p)
	{
		Set<Item> items = ItemManager.instance().getItems(p);
		Calendar curDay = startDay(p);

		int totalSupply = 0;
		int numDays = 0;
		while(!end.before(curDay))
		{
			int curSupply = calculateSupply(curDay, items);
			totalSupply += curSupply;

			curDay.add(Calendar.DAY_OF_MONTH, 1);
			numDays++;
		}
		double average = totalSupply / (double) numDays;
		int current = calculateSupply(end, items);

		return current + " / " + formatDouble(average);
	}

	private String supplyMinMax(Product p)
	{
		Set<Item> items = ItemManager.instance().getItems(p);
		Calendar curDay = startDay(p);

		int min = Integer.MAX_VALUE;
		int max = -1;
		while(!end.before(curDay))
		{
			int curSupply = calculateSupply(curDay, items);
			if(curSupply > max)
				max = curSupply;
			if(curSupply < min)
				min = curSupply;
			curDay.add(Calendar.DAY_OF_MONTH, 1);
		}

		return min + " / " + max;
	}

	private String supplyUse(Product p)
	{
		Set<Item> items = ItemManager.instance().getItems(p);
		Calendar curDay = startDay(p);

		int added = 0;
		int used = 0;
		while(!end.before(curDay))
		{
			added += calculateAdded(curDay, items);
			used += calculateUsed(curDay, items);

			curDay.add(Calendar.DAY_OF_MONTH, 1);
		}

		return used + " / " + added;
	}

	private int calculateUsed(Calendar cal, Set<Item> items)
	{
		int used = 0;

		for(Item item: items)
		{
			Calendar exitDate = item.getExitTime();
			if(exitDate != null && sameDay(exitDate, cal))
				used++;
		}
		return used;
	}

	private int calculateAdded(Calendar cal, Set<Item> items)
	{
		int added = 0;

		for(Item item: items)
		{
			Calendar entryDate = item.getEntryDate();
			if(sameDay(entryDate, cal))
				added++;
		}
		return added;
	}

	//Supply is determined by the number of Items in storage at the end
		//of the day
	private int calculateSupply(Calendar cal, Set<Item> items)
	{
		int supply = 0;

		for(Item item : items)
		{
			if(isLiveAt(cal, item))
				supply++;
		}

		return supply;
	}

	private boolean isLiveAt(Calendar cal, Item item)
	{
		Calendar entryDate = item.getEntryDate();
		Calendar exitDate = item.getExitTime();

		//if we examine before the entry day
		if(cal.before(entryDate) && !sameDay(entryDate, cal))
			return false;

		if(exitDate == null)
			return true;

		//we know that the item has been removed from the system

		if(sameDay(exitDate, cal))
			return false;

		if(exitDate.after(cal))
			return true;

		//exitDate is before cal
		return false;
	}

	private boolean sameDay(Calendar cal1, Calendar cal2)
	{
		int year = Calendar.YEAR;
		int month = Calendar.MONTH;
		int day = Calendar.DAY_OF_MONTH;
		return cal1.get(year) == cal2.get(year) &&
				cal1.get(month) == cal2.get(month) &&
				cal1.get(day) == cal2.get(day);
	}

	private boolean wasUsed(Item item)
	{
		Calendar entryDate = item.getEntryDate();
		Calendar exitDate = item.getExitTime();

		if(exitDate == null)
			return false;

		return ( (sameDay(exitDate, start) || exitDate.after(start)) &&
					(sameDay(exitDate, end)   || exitDate.before(end)) );

	}

	//this breaks
	private String usedAge(Product p)
	{
		Set<Item> allItems = ItemManager.instance().getItems(p);
		Set<Item> usedItems = new HashSet<Item>();
		for(Item item: allItems)
			if(wasUsed(item))
				usedItems.add(item);

		int totalAge = 0;
		int maxAge = 0;
		for(Item item: usedItems)
		{
			Calendar entryDate = item.getEntryDate();
			Calendar exitDate = item.getExitTime();
			Calendar copy = (Calendar) entryDate.clone();
			int age = 0;
			while(!sameDay(copy, exitDate))//(copy.before(exitDate))
			{
				copy.add(Calendar.DAY_OF_MONTH, 1);
				age++;
			}
			totalAge += age;
			if(age > maxAge)
				maxAge = age;
		}
		double average = totalAge / (double) usedItems.size();

		return formatDouble(average) + " days / " + maxAge + " days";
	}

	private String curAge(Product p)
	{
		Set<Item> allItems = ItemManager.instance().getItems(p);
		Set<Item> curItems = new HashSet<Item>();
		for(Item item: allItems)
			if(isLiveAt(end, item))
				curItems.add(item);

		int totalAge = 0;
		int maxAge = 0;
		for(Item item: curItems)
		{
			Calendar entryDate = item.getEntryDate();
			Calendar copy = (Calendar) entryDate.clone();
			int age = 0;
			while(!sameDay(copy, end))
			{
				copy.add(Calendar.DAY_OF_MONTH, 1);
				age++;
			}
			totalAge += age;
			if(age > maxAge)
				maxAge = age;
		}
		double average = totalAge / (double) curItems.size();

		return formatDouble(average) + " days / " +  maxAge + " days";
	}

	/** Finish the Algorithm */
	public void finish()
	{
		try
		{
			builder.endTable();
			builder.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}

