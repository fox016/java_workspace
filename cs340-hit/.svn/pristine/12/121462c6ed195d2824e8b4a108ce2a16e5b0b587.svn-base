
package model.report;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
	This is the TestReportBuilderTest class
	Depends on ReportBuilderBaseTest for implementation independent tests
	@author Christopher Tensmeyer
	@version March 11, 2013
*/

public class TestReportBuilderTest extends ReportBuilderBaseTest
{
	
	protected ReportBuilder getReportBuilder()
	{
		return new TestReportBuilder();
	}

	protected String extension() 
	{
		return ".test";
	}
	@Test
	public void emptyTest() throws IOException
	{
		super.emptyTest();
		TestReportStructure report = ((TestReportBuilder) builder).getTestStructure();
		assertEquals(report.getTitle(), "");
		assertTrue(report.numRows() == 1);
	}

	@Test
	public void titleTest() throws IOException
	{
		super.titleTest();
		TestReportStructure report = ((TestReportBuilder) builder).getTestStructure();
		assertEquals(report.getTitle(), "This is a Title");
		assertTrue(report.numRows() == 0);
	}

	@Test
	public void textTest() throws IOException
	{
		super.textTest();
		TestReportStructure report = ((TestReportBuilder) builder).getTestStructure();
		assertTrue(report.numRows() == 4);
		assertEquals(report.getTitle(), "This is a Title");

		assertTrue(report.numCols(1) == 1);
		assertEquals(report.get(1,0), "This is a Text Header");

		assertTrue(report.numCols(3) == 1);
		assertEquals(report.get(3,0), "This is a Text Line");
	}

	@Test
	public void emptyTable() throws IOException
	{
		super.emptyTable();
		TestReportStructure report = ((TestReportBuilder) builder).getTestStructure();
		assertEquals(report.getTitle(), "");
		assertTrue(report.numRows() == 0);
	}

	@Test
	public void simpleTable() throws IOException
	{
		super.simpleTable();
		TestReportStructure report = ((TestReportBuilder) builder).getTestStructure();
		assertTrue(report.numRows() == 3);
		assertTrue(report.numCols(0) == 3);
		assertTrue(report.numCols(1) == 3);
		assertTrue(report.numCols(2) == 3);

		assertEquals(report.get(0,0), "Header 1");
		assertEquals(report.get(0,2), "Header 3");
		assertEquals(report.get(1,1), "Cell 2");
		assertEquals(report.get(2,0), "Cell 4");
		assertEquals(report.get(2,2), "Cell 6");
	}
}

