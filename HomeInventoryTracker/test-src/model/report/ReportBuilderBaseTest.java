
package model.report;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
	This is the base class for the ReportBuilders
	Subclass this and override getReportBuilder() and extension()
	@author Christopher Tensmeyer
	@version March 11, 2013
*/

public class ReportBuilderBaseTest
{
	protected ReportBuilder builder;
	private static String filepath = "reports/test/";


	protected ReportBuilder getReportBuilder()
	{
		return new EmptyReportBuilder();
	}

	protected String extension() 
	{
		return ".empty";
	}

	public void init(String filename) throws IOException
	{
		builder = getReportBuilder();
		builder.open(filepath + filename + extension());
	}
	
	@Test
	public void emptyTest() throws IOException
	{
		init("empty");
		builder.writeBlankLine();
	}

	@Test
	public void titleTest() throws IOException
	{
		init("title");
		builder.writeTitle("This is a Title");
	}

	@Test
	public void textTest() throws IOException
	{
		init("text");
		builder.writeTitle("This is a Title");
		builder.writeBlankLine();
		builder.writeTextHeader("This is a Text Header");
		builder.writeBlankLine();
		builder.writeTextLine("This is a Text Line");
	}

	@Test
	public void emptyTable() throws IOException
	{
		init("emptyTable");
		builder.startTable(1);
		builder.endTable();
	}

	@Test
	public void simpleTable() throws IOException
	{
		init("simpleTable");
		builder.startTable(3);
		builder.writeTableHeader(new String[] {"Header 1", "Header 2", "Header 3"});
		builder.writeTableRow(new String[] {"Cell 1", "Cell 2", "Cell 3"});
		builder.writeTableRow(new String[] {"Cell 4", "Cell 5", "Cell 6"});
		builder.endTable();
	}

	@After
	public void end() throws IOException
	{
		builder.close();
	}

}


