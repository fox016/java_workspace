
package model.report;

import static org.junit.Assert.*;

import model.*;
import java.util.*;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TalonosReportTest
{
	//Test #3
	/*
		This tests over a period of one month
	*/
	//May 15, 2005 at 12:35:15 pm
	static Calendar endDate = new GregorianCalendar(2005, Calendar.MAY, 15, 12, 35, 15);
	//April 15, 2005
	static Calendar startDate = new GregorianCalendar(2005, Calendar.APRIL, 15);

	@Before
	public void initTest()
	{
		House.reset();
		ItemManager.reset();
		ProductManager.reset();
	}

	static Product createProduct(Calendar creationDate)
	{
		//May 15, 2004
		Size size = new Size(2.5, SupplyType.POUNDS);
		String desc = "Product Description";
		int threeMonthSupply = 5;
		int shelfLife = 3;
		ProductBarcode barcode = new ProductBarcode("Barcode123");

		Product p = new Product(creationDate, barcode, desc, size, shelfLife, threeMonthSupply);
		return p;
	}

	static TestReportStructure foo(Product p, boolean original)
	{
		try
		{
			TestReportBuilder builder = new TestReportBuilder();

			Visitor visitor = (original) ?
				new ProductStatisticsVisitor(builder, startDate, endDate) :
				new ChrisPSVisitor(builder, startDate, endDate);
			p.accept(visitor);
			visitor.finish();
			
			return builder.getTestStructure();
		}
		catch(Exception e)
		{
			return null;
		}
	}

	static void test(Product p, String title) throws IOException
	{
		System.out.println("\n");
		System.out.println(title);

		TestReportStructure nate = foo(p, true);
		TestReportStructure chris = foo(p, false);
		boolean same = true;
		
		if(chris == null) // I don't know why this is null sometimes - Nate
		{
			same = false;
			assertTrue(same);
			return;
		}

		System.out.println("Nate\tChris");
		for(int k = 0; k < 10; k++)
		{
			String nateCell = nate.get(1,k);
			String chrisCell = chris.get(1,k);

			System.out.println(nateCell + "\t" + chrisCell);
			if(!nateCell.equals(chrisCell))
				same = false;
		}

		assertTrue(same);
	}

	@Test
	public void emptyTest()
	{
		assertTrue(true);
	}
        
        @Test
	public void test3a() throws IOException
	{
            //Test a product that was initially added to the system 
            //during the reporting period (to make
            //sure that the days before the product was created are 
            //not counted in the product’s statistics)
            //a. The product was added in the middle of the reporting period
            
		Calendar exitDate = new GregorianCalendar(2005, Calendar.NOVEMBER, 1);
                Calendar productCreationDate = new GregorianCalendar(2004, Calendar.MAY, 1);
		Product p = createProduct(productCreationDate);
		StorageUnit unit = new StorageUnitImpl("Kitchen");
		ProductManager.instance().addProduct(p);
		ProductManager.instance().addContainer(p, unit);

		Calendar[] entryDates = new Calendar[] 
                {
			new GregorianCalendar(2005, Calendar.MAY, 1),
                        new GregorianCalendar(2005, Calendar.MAY, 14),
			new GregorianCalendar(2005, Calendar.MAY, 15),
			new GregorianCalendar(2005, Calendar.MAY, 16),
		};
                Item[] items = new Item[entryDates.length];

		for(int k = 0; k < items.length; k++)
		{
			items[k] = new Item(p, unit, entryDates[k]);
			ItemManager.instance().insert(items[k], new ItemBarcode());
                        ItemManager.instance().removeItem(items[k]);
                        items[k].setExitDate(exitDate);
		}

		test(p, "Test 3a");
	}

	@Test
	public void test3b() throws IOException
	{
            //Test a product that was initially added to the system 
            //during the reporting period (to make
            //sure that the days before the product was created are 
            //not counted in the product’s statistics)
            //b. The product was added on the first day of the reporting period
            
		Calendar exitDate = new GregorianCalendar(2005, Calendar.NOVEMBER, 1);
                Calendar productCreationDate = new GregorianCalendar(2004, Calendar.APRIL, 15);
		Product p = createProduct(productCreationDate);
		StorageUnit unit = new StorageUnitImpl("Kitchen");
		ProductManager.instance().addProduct(p);
		ProductManager.instance().addContainer(p, unit);

		Calendar[] entryDates = new Calendar[] {
                        new GregorianCalendar(2005, Calendar.APRIL, 15),
                        new GregorianCalendar(2005, Calendar.APRIL, 31),
			new GregorianCalendar(2005, Calendar.MAY, 1),
                        new GregorianCalendar(2005, Calendar.MAY, 14),
			new GregorianCalendar(2005, Calendar.MAY, 15),
			new GregorianCalendar(2005, Calendar.MAY, 16),
		};
                Item[] items = new Item[entryDates.length];

		for(int k = 0; k < items.length; k++)
		{
			items[k] = new Item(p, unit, entryDates[k]);
			ItemManager.instance().insert(items[k], new ItemBarcode());
                        ItemManager.instance().removeItem(items[k]);
                        items[k].setExitDate(exitDate);
		}

		test(p, "Test 3b");
	}

	@Test
	public void test3c() throws IOException
	{
            //Test a product that was initially added to the system 
            //during the reporting period (to make
            //sure that the days before the product was created are 
            //not counted in the product’s statistics)
            //c. The product was added on the last day of the reporting period
            
		Calendar exitDate = new GregorianCalendar(2005, Calendar.NOVEMBER, 1);
                Calendar productCreationDate = new GregorianCalendar(2004, Calendar.MAY, 15);
		Product p = createProduct(productCreationDate);
		StorageUnit unit = new StorageUnitImpl("Kitchen");
		ProductManager.instance().addProduct(p);
		ProductManager.instance().addContainer(p, unit);

		Calendar[] entryDates = new Calendar[] 
                {
			new GregorianCalendar(2005, Calendar.MAY, 15),
			new GregorianCalendar(2005, Calendar.MAY, 16),
		};
                Item[] items = new Item[entryDates.length];

		for(int k = 0; k < items.length; k++)
		{
			items[k] = new Item(p, unit, entryDates[k]);
			ItemManager.instance().insert(items[k], new ItemBarcode());
                        ItemManager.instance().removeItem(items[k]);
                        items[k].setExitDate(exitDate);
		}

		test(p, "Test 3c");
	}
        
        @Test
	public void test4a() throws IOException
	{
            //Test 4: Test a product that was initially added to the 
            //system before the reporting period (to make
            //sure that the days before the reporting period are 
            //not counted in the product’s statistics)
            //a. The product was added many days before the reporting period
            
		Calendar exitDate = new GregorianCalendar(2005, Calendar.NOVEMBER, 1);
                //This date should be valid. I guess I'm also testing that...
                Calendar productCreationDate = new GregorianCalendar(2000, Calendar.JANUARY, 1);
		Product p = createProduct(productCreationDate);
		StorageUnit unit = new StorageUnitImpl("Kitchen");
		ProductManager.instance().addProduct(p);
		ProductManager.instance().addContainer(p, unit);

		Calendar[] entryDates = new Calendar[] 
                {
                        new GregorianCalendar(2005, Calendar.APRIL, 14),
                        new GregorianCalendar(2005, Calendar.APRIL, 15),
                        new GregorianCalendar(2005, Calendar.APRIL, 16),
                        new GregorianCalendar(2005, Calendar.MAY, 14),
			new GregorianCalendar(2005, Calendar.MAY, 15),
			new GregorianCalendar(2005, Calendar.MAY, 16),
		};
                Item[] items = new Item[entryDates.length];

		for(int k = 0; k < items.length; k++)
		{
			items[k] = new Item(p, unit, entryDates[k]);
			ItemManager.instance().insert(items[k], new ItemBarcode());
                        ItemManager.instance().removeItem(items[k]);
                        items[k].setExitDate(exitDate);
		}

		test(p, "Test 4a");
	}
        
        @Test
	public void test4b() throws IOException
	{
            //Test 4: Test a product that was initially added to the 
            //system before the reporting period (to make
            //sure that the days before the reporting period are 
            //not counted in the product’s statistics)
            //b. The product was added the day before the reporting period
            
		Calendar exitDate = new GregorianCalendar(2005, Calendar.NOVEMBER, 1);
                Calendar productCreationDate = new GregorianCalendar(2005, Calendar.APRIL, 14);
		Product p = createProduct(productCreationDate);
		StorageUnit unit = new StorageUnitImpl("Kitchen");
		ProductManager.instance().addProduct(p);
		ProductManager.instance().addContainer(p, unit);

		Calendar[] entryDates = new Calendar[] 
                {
                        new GregorianCalendar(2005, Calendar.APRIL, 15),
                        new GregorianCalendar(2005, Calendar.APRIL, 16),
                        new GregorianCalendar(2005, Calendar.MAY, 14),
			new GregorianCalendar(2005, Calendar.MAY, 15),
			new GregorianCalendar(2005, Calendar.MAY, 16),
		};
                Item[] items = new Item[entryDates.length];

		for(int k = 0; k < items.length; k++)
		{
			items[k] = new Item(p, unit, entryDates[k]);
			ItemManager.instance().insert(items[k], new ItemBarcode());
                        ItemManager.instance().removeItem(items[k]);
                        items[k].setExitDate(exitDate);
		}

		test(p, "Test 4b");
	}
        
        public void test5() throws IOException
	{
            //Test 5: Test a report period that covers a time when the 
            //system has no products (and therefore no items)
            
		//A little confused here? "test" takes a product, so how do I do that?
	}

}


