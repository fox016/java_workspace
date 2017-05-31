package model;
import static org.junit.Assert.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;


import org.junit.Before;
import org.junit.Test;

public class ProductTest {

	Product p1 = new Product(new GregorianCalendar(2000,3,4), new ProductBarcode("345"),
			"String Cheese", new Size(2, SupplyType.COUNT),3, 4);
	
	Product p2;
	
	@Test
	public void nullTest()
	{	
		assertNotNull(p1);
		assertNotNull(p1.getCreationDate());
		assertNotNull(p1.getBarcode());
		assertNotNull(p1.getDescription());
		assertNotNull(p1.getShelfLife());
		assertNotNull(p1.getSize());
		assertNotNull(p1.getThreeMonthSupply());
	}
	
	@Test
	public void getterSetterTest(){
		
		p1.setBarcode(new ProductBarcode("543"));
		assertTrue(p1.getBarcode().compareTo(new ProductBarcode("543")) == 0);
		
		p1.setCreationDate(new GregorianCalendar(2001,1,1));
		assertTrue(p1.getCreationDate().compareTo(new GregorianCalendar(2001,1,1)) == 0);
		
		p1.setDescription("New Description");
		assertTrue(p1.getDescription().equals("New Description"));
		
		p1.setShelfLife(6);
		assertTrue(p1.getShelfLife()==6);
		
		p1.setSize(new Size(10,SupplyType.COUNT));
		assertTrue(p1.getSize().equals(new Size(10,SupplyType.COUNT)));
		
		p1.setThreeMonthSupply(50);
		assertTrue(p1.getThreeMonthSupply() == 50);
		
	}
	
	@Test
	public void isValidProductTest() {
		p1 = new Product(new GregorianCalendar(2002, 2, 12),
				new ProductBarcode("333"), "Jelly Beans", new Size(13,
						SupplyType.OUNCES), 4, 4);

		assertTrue(p1.isValidProduct());
		
		//Empty String description
		p1 = new Product(new GregorianCalendar(2002, 2, 12),
				new ProductBarcode("333"), "", new Size(13,
						SupplyType.OUNCES), 4, 4);
		
		assertFalse(p1.isValidProduct());
		

		// Note that nearly all of the parameters are checked within the product
		// itself
	}
	
	@Test
	public void updateProductTest() {

		p1 = new Product(new GregorianCalendar(2001, 0, 1), new ProductBarcode(
				"333"), "Crest", new Size(12, SupplyType.COUNT), 0, 0);

		p2 = new Product(new GregorianCalendar(2002, 2, 12),
				new ProductBarcode("333"), "Jelly Beans", new Size(13,
						SupplyType.OUNCES), 4, 4);

		p1.updateProduct(p2);
		
		assertTrue(p1.getCreationDate().equals(
				new GregorianCalendar(2002, 2, 12)));
		assertTrue(p1.getShelfLife() == 4);
		assertTrue(p1.getThreeMonthSupply() == 4);
		assertTrue(p1.getDescription() == "Jelly Beans");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void emptyBarcodeExceptionTest()
	{
		
		p1.setBarcode(new ProductBarcode(""));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void emptyDescriptionTest()
	{
		p1.setDescription("");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void negShelfLifeTest()
	{
		p1.setShelfLife(-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void negThreMonthTest()
	{
		p1.setThreeMonthSupply(-1);
	}
	

}
