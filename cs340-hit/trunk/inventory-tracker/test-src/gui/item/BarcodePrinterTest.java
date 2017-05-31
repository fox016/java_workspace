
package gui.item;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.io.IOException;

import model.*;

public class BarcodePrinterTest extends FacadeTest
{
	@Test
	public void emptyItemTest() throws IOException
	{
		BarcodePrinter bp = new BarcodePrinter("pdfs/emptyTest.pdf");
		bp.writeBarcodesToPdf(new ArrayList<Item>());
	}

	@Test
	public void overloadTest() throws IOException
	{
		super.init();

		BarcodePrinter bp = new BarcodePrinter("pdfs/overloadTest.pdf");
		for(int k = 0; k < 10; k++)
		{
			for(Item item : items)
			{
				Facade.addItem(new Item(item.getProduct(), item.getStorageUnit(), item.getEntryDate() ) );
				//assertNotNull(item.getItemBarcode());
			}
		}
		bp.writeBarcodesToPdf(ItemManager.instance().allItems());

	}

	@Test
	public void manyItemTest() throws IOException
	{
		//lets us use the Items
		super.init();
		
		BarcodePrinter bp = new BarcodePrinter("pdfs/multipleItemTest.pdf");
		for(Item item : items)
		{
			Facade.addItem(item);
			assertNotNull(item.getItemBarcode());
		}
		bp.writeBarcodesToPdf(ItemManager.instance().allItems());
	}

	@Test
	public void fewItemTest() throws IOException
	{
		//lets us use the Items
		super.init();
		
		BarcodePrinter bp = new BarcodePrinter("pdfs/fewItemTest.pdf");
		for(int k = 0; k < 3; k++)
		{
			Item item = items.get(k);
			Facade.addItem(item);
			assertNotNull(item.getItemBarcode());
		}
		bp.writeBarcodesToPdf(ItemManager.instance().allItems());
	}

	@Test
	public void singleItemTest() throws IOException
	{
		//lets us use the Items
		super.init();
		
		BarcodePrinter bp = new BarcodePrinter("pdfs/singleItemTest.pdf");
		Item item = items.get(0); 
		Facade.addItem(item);
		assertNotNull(item.getItemBarcode());
		assertTrue(!ItemManager.instance().allItems().isEmpty());
		bp.writeBarcodesToPdf(ItemManager.instance().allItems());
	}
}

