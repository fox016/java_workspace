
package model.report;

import java.io.File;

	/** Defines the class for creating Reports to test Visitors
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

class TestReportBuilder implements ReportBuilder
{
	private TestReportStructure report;

	public TestReportBuilder()
	{
		report = new TestReportStructure();
	}

	/** Writes the Title of the Report
		@param title The Title
	*/
	public void writeTitle(String title)
	{
		report.setTitle(title);
	}

	/** Starts a Table
		@param columns The number of columns in the table
	*/
	public void startTable(int columns)
	{
		return;
	}

	/** Starts a Table
		@param columns The number of columns in the table
		@param widths The width of the specified columns.  If null, assumed to be uniform.
	*/
	public void startTable(int columns, int[] widths)
	{
		return;
	}

	/** Closes the Table */
	public void endTable()
	{
		return;
	}

	/** Writes the header for each column in the table in bold
		@param headers The headers
	*/
	public void writeTableHeader(String[] headers)
	{
		report.addRow(headers);
	}

	/** Writes a row in the table
		@param content The content of each cell in the table.
	*/
	public void writeTableRow(String[] content)
	{
		report.addRow(content);
	}

	/** Writes a blank line */
	public void writeBlankLine()
	{
		report.addRow(new String[] {"\n"});
	}

	/** Writes a header (bold) of plain text 
		@param text The text to be written
	*/
	public void writeTextHeader(String text)
	{
		report.addRow(new String[] {text});
	}

	/** Writes a left justified line of text
		@param text The text to be written
	*/
	public void writeTextLine(String text)
	{
		report.addRow(new String[] {text});
	}

	/** Opens the file for writing */
	public void open(String filename)
	{
		return;
	}

	/** Closes the File */
	public void close()
	{
		return;
	}

	/** Returns the generated Report */
	public TestReportStructure getTestStructure()
	{
		return report;
	}

	/** This method is optional and because there is no File to return, throws an Exception */
	public File getReport()
	{
		throw new UnsupportedOperationException("There is no File to retrieve from TestBuilder");
	}

}


