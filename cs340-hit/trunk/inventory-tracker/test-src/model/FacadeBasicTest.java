package model;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Set;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class FacadeBasicTest extends FacadeTest {
	
	@Before
	public void initBasicTest()
	{
		super.init();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddNullStorageUnit()
	{
		Facade.addStorageUnit(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddEmptyStorageUnit()
	{
		Facade.addStorageUnit("");
	}
	
	@Test
	public void testUpdateUnitName()
	{
		Facade.setStorageUnitName(basement, "Dungeon");
		assertTrue(basement.getName().equals("Dungeon"));
		assertTrue(House.instance().containsUnit(basement));
		
		Set<StorageUnit> units = Facade.getStorageUnits();
		Iterator<StorageUnit> iter = units.iterator();
		while(iter.hasNext())
			assertFalse(iter.next().getName().equals("Basement"));
		
		StorageUnit temp = Facade.addStorageUnit("Basement");
		assertTrue(temp.getName().equals("Basement"));
		Facade.deleteStorageUnit(temp);
		assertTrue(House.instance().containsUnit(basement));
		
		units = Facade.getStorageUnits();
		iter = units.iterator();
		while(iter.hasNext())
			assertFalse(iter.next().getName().equals("Basement"));
		
		Facade.setStorageUnitName(basement, "Basement");
		assertTrue(basement.getName().equals("Basement"));
	}

	@Test
	public void testProductGroups()
	{
		ProductGroup pg1 = Facade.addProductGroup("PG1", new Size(1.0, SupplyType.COUNT), kitchen);
		Set<String> names = Facade.getChildContainers(kitchen);
		assertTrue(names.contains(pg1.getName()));
		assertEquals(pg1, kitchen.getProductContainer(pg1.getName()));

		Product p1 = products.get(0);
		Facade.addProductToContainer(p1, pg1);
	}

	@Test
	public void addItems()
	{
		for(int k = 0; k < items.size(); k++)
		{
			Item item = items.get(k);
			assertNotNull(item);
			assertTrue(Facade.canAddItem(item));
			Facade.addItem(item);
		}
		Item faulty = new Item((Product)null, null, null);
		assertTrue(Facade.canAddItem(faulty));
	}
	
}
