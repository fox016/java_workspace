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

public class FacadeAddTest extends FacadeTest{

    @Before
    public void initAddTest()
    {
	 	super.init();
    }

    @Test
    public void testAddGroup()
    {
        ProductGroup[] testGroups = new ProductGroup[10];

        testGroups[0] = Facade.addProductGroup("K1", defaultSize, kitchen);
        testGroups[1] = Facade.addProductGroup("K2", defaultSize, kitchen);
        testGroups[2] = Facade.addProductGroup("K3", defaultSize, kitchen);

        testGroups[3] = Facade.addProductGroup("B1", defaultSize, bathroom);

        assertTrue(Facade.getChildContainers(kitchen).size() == 3);
        assertTrue(Facade.getChildContainers(bathroom).size() == 1);
        assertTrue(Facade.getChildContainers(basement).size() == 0);
    }

    @Test
    public void testAddGroupsToGroups()
    {
        ProductGroup[] testGroups = new ProductGroup[10];

        testGroups[0] = Facade.addProductGroup("K1", defaultSize, House.instance().getUnitByName("Kitchen"));
        testGroups[1] = Facade.addProductGroup("K2", defaultSize, House.instance().getUnitByName("Kitchen"));
        testGroups[2] = Facade.addProductGroup("K3", defaultSize, House.instance().getUnitByName("Kitchen"));

        testGroups[3] = Facade.addProductGroup("B1", defaultSize, House.instance().getUnitByName("Bathroom"));

        testGroups[4] = Facade.addProductGroup("C1_K1", defaultSize, testGroups[0]);
        testGroups[5] = Facade.addProductGroup("C2_K1", defaultSize, testGroups[0]);

        testGroups[6] = Facade.addProductGroup("C1_K3", defaultSize, testGroups[2]);

        testGroups[7] = Facade.addProductGroup("C1_B1", defaultSize, testGroups[3]);
        testGroups[8] = Facade.addProductGroup("C2_B1", defaultSize, testGroups[3]);
        testGroups[9] = Facade.addProductGroup("C3_B1", defaultSize, testGroups[3]);

        assertTrue(Facade.getChildContainers(testGroups[0]).size() == 2);
        assertTrue(Facade.getChildContainers(testGroups[1]).size() == 0);
        assertTrue(Facade.getChildContainers(testGroups[2]).size() == 1);
        assertTrue(Facade.getChildContainers(testGroups[3]).size() == 3);
    }

    @Test
    public void testAddProductsStorageUnit()
    {
        StorageUnit kitchen = House.instance().getUnitByName("Kitchen");
        Product p = products.get(0);

        assertFalse(kitchen.hasProduct(p.getBarcode()));
        Facade.addProductToContainer(p, kitchen);
        assertEquals(ProductManager.instance().getProductByBarcode(p.getBarcode()), p);
        assertTrue(kitchen.hasProduct(p.getBarcode()));
        
        assertNotNull(products);
        assertEquals(products.get(0).getBarcode(), new ProductBarcode("1024"));
        assertNotNull(ProductManager.instance().getProductByBarcode(products.get(0).getBarcode()));
        assertNotNull(ProductManager.instance().getProductByBarcode(new ProductBarcode("1024")));
        assertEquals(products.get(0), ProductManager.instance().getProductByBarcode(new ProductBarcode("1024")));
    }

    @Test
    public void testAddProductsToProductGroups()
    {
        //ProductGroup[] testGroups = new ProductGroup[10];

        ProductContainer container = Facade.addProductGroup("K1", defaultSize, House.instance().getUnitByName("Kitchen"));

		  Product p = products.get(0);
		  //Facade.addProduct(p);

		  assertFalse(container.hasProduct(p.getBarcode()));
		  Facade.addProductToContainer(p, container);
		  assertTrue(container.hasProduct(p.getBarcode()));
		  //Next line fails Product lacks equals method
		  assertEquals(ProductManager.instance().getProductByBarcode(p.getBarcode()), p);

    }

	 @Test
	 public void testAddItemToStorageUnit()
	 {
        StorageUnit kitchen = House.instance().getUnitByName("Kitchen");
        Product p = products.get(0);
        Facade.addProductToContainer(p, kitchen);
			
			Item item = new Item(p, kitchen, itemDates.get(0));
			assertTrue(item.isProduct(p));
			assertEquals(item.getStorageUnit(), kitchen);

			Facade.addItem(item);
			assertTrue(ItemManager.instance().getItems(p).contains(item));
			assertTrue(ItemManager.instance().getItems(kitchen).contains(item));
	 }

	 @Test
	 public void testAddItemToProductGroup()
	 {
        StorageUnit kitchen = House.instance().getUnitByName("Kitchen");
        ProductContainer container = Facade.addProductGroup("K1", defaultSize, kitchen);
        assertTrue(kitchen.getContainers().contains("K1"));

        Product p = products.get(0);
        Facade.addProductToContainer(p, container);
        assertEquals(Facade.getProductContainer(kitchen, p), container);

		Item item = new Item(p, kitchen, itemDates.get(0));
		assertTrue(item.isProduct(p));
		assertEquals(item.getStorageUnit(), kitchen);
		assertEquals(item.getStorageUnit().getProductContainer((item.getProduct())), container);
			
		//addItem depends on this to be correct
		assertEquals(kitchen.getProductContainer(p), container);

		Facade.addItem(item);
		//items by product
		assertTrue(ItemManager.instance().getItems(p).contains(item));
		//all items in container
		assertTrue(ItemManager.instance().getItems(container).contains(item));
		//all items in kitchen, but not in subtree
		assertFalse(ItemManager.instance().getItems(kitchen, false).contains(item));
		//all items in kitchen and it's children
		assertTrue(ItemManager.instance().getItems(kitchen, true).contains(item));
	 }

	 @Test
	 public void testAddProductMovesExistingItem()
	 {
	 	//Make child ProductGroup
      StorageUnit kitchen = House.instance().getUnitByName("Kitchen");
      ProductContainer container = Facade.addProductGroup("K1", defaultSize, kitchen);
      assertTrue(kitchen.getContainers().contains("K1"));

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

		//Move Product from Kitchen to it's ProductGroup.  The Item should move too
		Facade.addProductToContainer(p1, container);

		//all items
		assertTrue(ItemManager.instance().getItems(p1).contains(item1));
		assertFalse(ItemManager.instance().getItems(p1).contains(item2));
		assertTrue(ItemManager.instance().getItems(p2).contains(item2));
		assertFalse(ItemManager.instance().getItems(p2).contains(item1));

		//all items in container
		assertTrue(ItemManager.instance().getItems(container).contains(item1));
		assertFalse(ItemManager.instance().getItems(container).contains(item2));

		//all items in kitchen, but not in subtree
		assertFalse(ItemManager.instance().getItems(kitchen, false).contains(item1));
		assertTrue(ItemManager.instance().getItems(kitchen, false).contains(item2));

		//all items in kitchen and it's children
		assertTrue(ItemManager.instance().getItems(kitchen, true).contains(item1));
		assertTrue(ItemManager.instance().getItems(kitchen, true).contains(item2));

	 }

	 @Test
	 public void testAddBatch()
	 {
      StorageUnit kitchen = House.instance().getUnitByName("Kitchen");
      Product p1 = products.get(0);
		Calendar c1 = itemDates.get(0);
		int count = 10;


		assertTrue(ItemManager.instance().getItems(kitchen, false).isEmpty());

		Facade.addBatchItems(p1, c1, kitchen, count);

		Set<Item> batch = ItemManager.instance().getItems(kitchen, false);
		assertTrue(batch.size() == 10);
		for(Item item : batch)
		{
			assertEquals(kitchen, item.getStorageUnit());
			assertTrue(item.isProduct(p1));
			assertEquals(item.getEntryDate(), c1);
		}
	 }
}
