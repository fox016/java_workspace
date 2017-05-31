/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * 
 * @author Talonos
 */
public class ProductManagerTest {
	private static ProductManager pm = ProductManager.instance();
	private static Product p1;
	private static Product p2;

	@Test
	public void addProductTest() {
		Product before = new Product(Calendar.getInstance(), new ProductBarcode(
				"12345"), "Bagels", new Size(1, SupplyType.COUNT), 2, 2);
		pm.addProduct(before);
		Product after = pm.getProductByBarcode(
				new ProductBarcode("12345"));
		assertNotNull(after);
		assertEquals(before, after);
	}

	@Test
	public void productDateTest() {
		StorageUnit su = new StorageUnitImpl("Kitchen");
		Calendar prodDate = new GregorianCalendar(2001,0,1);
		
		p1 = new Product(prodDate, new ProductBarcode(
				"5454"), "Crest", new Size(12, SupplyType.COUNT), 0, 0);
		pm.addProduct(p1);		
		Calendar itemDate = new GregorianCalendar(2005, 0, 1);
		Item item1 = new Item(p1, su, itemDate); //no shelf life
		
		assertTrue(p1.getCreationDate().compareTo(new GregorianCalendar(2001,0,1)) == 0);
		pm.updateProductDate(p1, item1.getEntryDate());
		assertTrue(p1.getCreationDate().compareTo(new GregorianCalendar(2001,0,1)) == 0);
		item1.setEntryDate(new GregorianCalendar(2000,0,1));
		pm.updateProductDate(p1, item1.getEntryDate());
		assertTrue(p1.getCreationDate().compareTo(item1.getEntryDate()) == 0);
		
		item1.setEntryDate(new GregorianCalendar(2000,0,1));		
	}
	
	@Test
	public void removeProductTest() {
		pm.addProduct(new Product(Calendar.getInstance(), new ProductBarcode(
				"12345"), "Bagels", new Size(1, SupplyType.COUNT), 2, 2));
		Product p1 = ProductManager.instance().getProductByBarcode(
				new ProductBarcode("12345"));
		assertTrue(pm.getProductByBarcode(p1.getBarcode()) != null);
		pm.removeProduct(p1);
		assertTrue(pm.getProductByBarcode(p1.getBarcode()) == null);
	}

	@Test
	public void addContainerTest() {
		ProductGroup pg = new ProductGroupImpl("Meat");
		Product p1 = new Product(new GregorianCalendar(2000, 3, 4),
				new ProductBarcode("345"), "Chicken", new Size(2,
						SupplyType.COUNT), 3, 4);
		pm.addProduct(p1);
		pm.addContainer(p1, pg);
		List<ProductContainer> container = pm.getContainers(p1);
		// System.out.println("NAME IS " + container.get(0).getName());
		assertTrue(container.get(0).getName().equals("Meat"));
	}

	@Test
	public void removeProductFromContainerTest() {
		StorageUnit su = new StorageUnitImpl("Kitchen");
		ProductGroup pg = su.addContainer("meat");
		
		Product p1 = new Product(new GregorianCalendar(2000, 3, 4),
				new ProductBarcode("345"), "Chicken", new Size(2,
						SupplyType.COUNT), 3, 4);
		
		pm.addProduct(p1);
		pm.addContainer(p1, pg);
		
		pg.addProduct(p1.getBarcode());
		List<ProductContainer> container = pm.getContainers(p1);
		assertFalse(container.isEmpty());
		assertTrue(pm.hasContainer(p1, pg));
		pm.removeProductFromContainer(p1, pg);
		assertTrue(container.isEmpty());
	}

	@Test
	public void canAddProductTest() {
		pm = ProductManager.instance();

		p1 = new Product(new GregorianCalendar(2001, 0, 1), new ProductBarcode(
				"333"), "Crest", new Size(1, SupplyType.COUNT), 0, 0);

		assertTrue(pm.canAddProduct(p1));

		// Note that nearly all of the parameters are checked within the product
		// itself
	}
}
