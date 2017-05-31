
package model;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FacadeDeleteTest extends FacadeTest 
{

    @Before
    public void initDeleteTest()
	 {
	 	super.init();
    }

    @Test
    public void testDeleteStorageUnit()
    {
        Facade.deleteStorageUnit(House.instance().getUnitByName("Bathroom"));

        Iterator<StorageUnit> units = Facade.getStorageUnits().iterator();
        while(units.hasNext())
            assertFalse(units.next().getName().equals("Bathroom"));

        assertNull(House.instance().getUnitByName("SqueedalySpooch"));

        Facade.deleteStorageUnit(new StorageUnitImpl("The Best"));

        Facade.addStorageUnit("Bathroom"); assertTrue(House.instance().containsUnit(House.instance().getUnitByName("Bathroom")));
	}

	@Test
	public void testDeleteItem()
	{
		Item item = items.get(0);
		StorageUnit unit = item.getStorageUnit();
		Product product = item.getProduct();
		Facade.addItem(item);
			
		Set<Item> itemsBeforeRemove = Facade.getItemsByProduct(unit, product);
		assertTrue(itemsBeforeRemove.contains(item));
		
		Facade.removeItemFromStorage(item);
		
		Set<Item> itemsAfterRemove = Facade.getItemsByProduct(unit, product);
		assertFalse(itemsAfterRemove.contains(item));	
	}

}

