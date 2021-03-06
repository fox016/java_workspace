
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

public class FacadeTransferTest extends FacadeTest {

    @Before
    public void initTransferTest()
    {
	 	super.init();
    }

	 @Test
	 public void testSimpleTransfer()
	 {
		//get Products, assign to Kitchen
      Product p1 = products.get(0);
		Product p2 = products.get(1);
      Facade.addProductToContainer(p1, kitchen);
      Facade.addProductToContainer(p2, kitchen);

		//create Items
		Item item1 = new Item(p1, kitchen, itemDates.get(0));
		assertTrue(item1.isProduct(p1));
		assertEquals(item1.getStorageUnit(), kitchen);
		Item item2 = new Item(p2, kitchen, itemDates.get(1));

		//Add Items to Kitchen
		assertTrue(Facade.canAddItem(item1));
		Facade.addItem(item1);
		Facade.addItem(item2);
		assertTrue(ItemManager.instance().getItems(kitchen, false).contains(item1));
		assertTrue(ItemManager.instance().getItems(kitchen, false).contains(item2));
		assertFalse(ItemManager.instance().getItems(bathroom, false).contains(item1));
		assertFalse(ItemManager.instance().getItems(bathroom, false).contains(item2));

		Facade.addProductToStorageUnitByBarcode(p1.getBarcode(), bathroom);
		Facade.transferItem(item1, bathroom);

		assertFalse(ItemManager.instance().getItems(kitchen, false).contains(item1));
		assertTrue(ItemManager.instance().getItems(kitchen, false).contains(item2));
		assertTrue(ItemManager.instance().getItems(bathroom, false).contains(item1));
		assertFalse(ItemManager.instance().getItems(bathroom, false).contains(item2));

	 }

	 @Test
	 public void testComplexTransfer()
	 {
      ProductContainer shelf = Facade.addProductGroup("B1", defaultSize,  bathroom);

		//get Products, assign to Kitchen
      Product p1 = products.get(0);
		Product p2 = products.get(1);
      Facade.addProductToContainer(p1, kitchen);
      Facade.addProductToContainer(p2, kitchen);
		Facade.addProductToContainer(p1, shelf);

		//create Items
		Item item1 = new Item(p1, kitchen, itemDates.get(0));
		assertTrue(item1.isProduct(p1));
		assertEquals(item1.getStorageUnit(), kitchen);
		Item item2 = new Item(p2, kitchen, itemDates.get(1));
		Item item3 = new Item(p1, kitchen, itemDates.get(2));

		//Add Items to Kitchen
		assertTrue(Facade.canAddItem(item1));
		Facade.addItem(item1);
		Facade.addItem(item2);
		Facade.addItem(item3);

		assertTrue(ItemManager.instance().getItems(kitchen, false).contains(item1));
		assertTrue(ItemManager.instance().getItems(kitchen, false).contains(item2));
		assertTrue(ItemManager.instance().getItems(kitchen, false).contains(item3));

		//the Bathroom nor Shelf have any items
		assertFalse(ItemManager.instance().getItems(bathroom, true).contains(item1));
		assertFalse(ItemManager.instance().getItems(bathroom, true).contains(item2));
		assertFalse(ItemManager.instance().getItems(bathroom, true).contains(item3));

		//should go to shelf
		Facade.transferItem(item1, bathroom); 

		//kitchen still has item2 and item3, but not item1
		assertFalse(ItemManager.instance().getItems(kitchen, false).contains(item1));
		assertTrue(ItemManager.instance().getItems(kitchen, false).contains(item2));
		assertTrue(ItemManager.instance().getItems(kitchen, false).contains(item3));

		//shelf has item1
		assertTrue(ItemManager.instance().getItems(bathroom, true).contains(item1));
		assertTrue(ItemManager.instance().getItems(shelf, false).contains(item1));

		//Bathroom does not immediately contain item1, nor 2 or 3
		assertFalse(ItemManager.instance().getItems(bathroom, false).contains(item1));
		assertFalse(ItemManager.instance().getItems(bathroom, false).contains(item2));
		assertFalse(ItemManager.instance().getItems(bathroom, false).contains(item3));
	 }

}
