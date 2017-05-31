package gui.item;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import model.Item;

public class BarcodePrinter
{
	private static String defaultFilename = "default.pdf";
	private static final int WIDTH = 712;
	private static final int HEIGHT = 992;
	private static final int BARCODES_PER_ROW = 5;
	private static final int DESCRIPTION_LENGTH = 24;
	private static final float PADDING = 10.0f;
	private static Font defaultFont = new Font(Font.FontFamily.TIMES_ROMAN, 7);
	private String filename;
	private BarcodeEAN barcodeMaker;

	//for testing purposes
	
	public BarcodePrinter()
	{
		this(defaultFilename);
	}

	public BarcodePrinter(String fn)
	{
		filename = fn;
		barcodeMaker = new BarcodeEAN();
		barcodeMaker.setCodeType(Barcode.UPCA);
	}

	/** Writes a pdf containing barcodes.
		@param items The list of Items for which we wish to print the barcodes for	
		@param filename The location where the pdf is to be written
		@throws IOException If the file cannot be written
		@return The File object associated with the newly created pdf
	*/
	public File writeBarcodesToPdf(Collection<Item> items) throws IOException
	{
		try
		{
			Document document = new Document(new Rectangle(WIDTH, HEIGHT));
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(this.filename));
			document.open();
			PdfPTable table = new PdfPTable(BARCODES_PER_ROW);

			Iterator<Item> iterator = items.iterator();
			do
			{
				addRow(table, iterator, writer);
			}
			while(iterator.hasNext());

			document.add(table);
			document.close();
		}
		catch(DocumentException e)
		{
			throw new IOException("Could not produce pdf");
		}

		return new File(this.filename);
	}

	private void addRow(PdfPTable table, Iterator<Item> iterator, PdfWriter writer)
			throws IOException
	{
		int numAdded = 0;
		PdfContentByte cb = writer.getDirectContent();

		List<PdfPCell> barcodes = new ArrayList<PdfPCell>();
		List<PdfPCell> descriptions = new ArrayList<PdfPCell>();

		while(numAdded < BARCODES_PER_ROW)
		{
			if(iterator.hasNext())
			{
				Item item = iterator.next();
			/*
				barcodeMaker.setCode(item.getItemBarcode().toString());

				PdfPCell barcodeCell =
					new PdfPCell(barcodeMaker.createImageWithBarcode(cb, null, null));
				barcodeCell.setBorder(Rectangle.NO_BORDER);
				
				barcodes.add(barcodeCell);
			*/
				barcodes.add(barcodeCell(item.getItemBarcode(), cb));

				descriptions.add(descriptionCell(item));
			}
			else
			{
				//pad the empty cells in the last row
				barcodes.add(emptyCell());
				descriptions.add(emptyCell());
			}
			numAdded++;
		}

		for(int k = 0; k < descriptions.size(); k++)
			table.addCell(descriptions.get(k));

		for(int k = 0; k < barcodes.size(); k++)
			table.addCell(barcodes.get(k));
	}

	private PdfPCell descriptionCell(Item item)
	{
		String description = formatDescription(item);	
		PdfPCell descriptionCell = new PdfPCell(new Phrase(description, defaultFont));
		format(descriptionCell);
		descriptionCell.setPaddingTop(PADDING);
		return descriptionCell;
	}

	private PdfPCell barcodeCell(model.Barcode barcode, PdfContentByte cb)
	{
		barcodeMaker.setCode(barcode.toString());
		PdfPCell barcodeCell = new PdfPCell(barcodeMaker.createImageWithBarcode(cb, null, null));
		format(barcodeCell);
		barcodeCell.setPaddingBottom(PADDING);
		return barcodeCell;
	}

	private PdfPCell emptyCell()
	{
		PdfPCell emptyCell = new PdfPCell();
		format(emptyCell);
		emptyCell.setPaddingTop(0.0f);
		return emptyCell;
	}

	private void format(PdfPCell cell)
	{
		cell.setBorder(Rectangle.NO_BORDER);
		//cell.setPadding(PADDING);
	}

	private String formatDescription(Item item)
	{
		String description = item.getDescription();
		if(description.length() > DESCRIPTION_LENGTH)
			description = description.substring(0, DESCRIPTION_LENGTH);
		String rtrn = "";
		rtrn += description;
		rtrn += "\n";
		rtrn += formatDate(item.getEntryDate().getTime());
		if(item.hasExpirationDate())
		{
			rtrn += " exp ";
			rtrn += formatDate(item.getExpirationDate().getTime());
		}
		return rtrn;
	}

	private String formatDate(Date date)
	{
		//TODO fix format
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		return format.format(date);
	}


}

