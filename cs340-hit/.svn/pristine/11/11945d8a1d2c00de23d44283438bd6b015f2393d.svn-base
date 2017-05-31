
package model.report;

import java.io.IOException;
import java.io.File;

	/** Defines the Interface for creating Reports
		of an abstract filetype
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

class EmptyReportBuilder implements ReportBuilder
{

	/** Writes the Title of the Report
		@param title The Title
	*/
	public void writeTitle(String title) 
	{}

	/** Starts a Table. Defaults to uniform width
		@param columns The number of columns in the table
	*/
	public void startTable(int columns) 
	{}

	/** Starts a Table
		@param columns The number of columns in the table
		@param widths Specifies the widths of the columns
	*/
	public void startTable(int columns, int[] width) 
	{}

	/** Closes the Table */
	public void endTable()
	{}

	/** Writes the header for each column in the table in bold
		@param headers The headers
	*/
	public void writeTableHeader(String[] headers) 
	{}

	/** Writes a row in the table
		@param content The content of each cell in the table.
	*/
	public void writeTableRow(String[] content) 
	{}

	/** Writes a blank line */
	public void writeBlankLine()
	{}

	/** Writes a header (bold) of plain text 
		@param text The text to be written
	*/
	public void writeTextHeader(String text)
	{}

	/** Writes a left justified line of text
		@param text The text to be written
	*/
	public void writeTextLine(String text)
	{}

	/** Opens the file for writing */
	public void open(String filename)
	{}

	/** Closes the File */
	public void close()
	{}

	/** This method is optional and because there is no File to return, throws an Exception */
	public File getReport() throws IOException
	{
		return null;
	}

}


