package model;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ModelTest {
	
	BarcodeGenerator gen = BarcodeGenerator.instance();

	@Test
	public void barcodeGeneratorTest()
	{
		// Test to make sure each Barcode is unique
		Set<String> codes = new HashSet<String>();
		for(int i = 0; i < 10000; i++)
			assertTrue(codes.add(gen.generateBarcode()));
	}
	
	@Test
	public void itemBarcodeTest() 
	{
		ItemBarcode b1 = new ItemBarcode("123456789012");
		ItemBarcode b2 = new ItemBarcode("123456789012");
		ItemBarcode b3 = new ItemBarcode("123456789013");
		
		assertTrue(b1.equals(b2));
		assertTrue(b2.equals(b1));
		assertFalse(b1.equals(b3));
		assertFalse(b3.equals(b1));
		
		assertEquals(new ProductBarcode("1024"), new ProductBarcode("1024"));
		assertEquals((new ProductBarcode("1024")).compareTo(new ProductBarcode("1024")), 0);
        assertEquals((new ProductBarcode("1024")).hashCode(), (new ProductBarcode("1024")).hashCode());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void illegalItemBarcodeTestA()
	{
		ItemBarcode b1 = new ItemBarcode("");
		b1.getClass();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void illegalItemBarcodeTestB()
	{
		ItemBarcode b1 = new ItemBarcode("123456789");
		b1.getClass();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void illegalItemBarcodeTestC()
	{
		ItemBarcode b1 = new ItemBarcode("12345678901A");
		b1.getClass();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void illegalItemBarcodeTestD()
	{
		ItemBarcode b1 = new ItemBarcode(null);
		b1.getClass();
	}
}
