
package model.report;

import java.io.File;
import java.io.IOException;

	/** Defines the Interface for creating Reports
		of an abstract filetype
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

public interface ReportBuilder
{

	/** Writes the Title of the Report
		@param title The Title
	*/
	void writeTitle(String title) throws IOException;

	/** Starts a Table. Defaults to uniform width
		@param columns The number of columns in the table
	*/
	void startTable(int columns) throws IOException;

	/** Starts a Table
		@param columns The number of columns in the table
		@param widths Specifies the widths of the columns
	*/
	void startTable(int columns, int[] width) throws IOException;

	/** Closes the Table */
	void endTable() throws IOException;

	/** Writes the header for each column in the table in bold
		@param headers The headers
	*/
	void writeTableHeader(String[] headers) throws IOException;

	/** Writes a row in the table
		@param content The content of each cell in the table.
	*/
	void writeTableRow(String[] content) throws IOException;

	/** Writes a blank line */
	void writeBlankLine() throws IOException;

	/** Writes a header (bold) of plain text 
		@param text The text to be written
	*/
	void writeTextHeader(String text) throws IOException;

	/** Writes a left justified line of text
		@param text The text to be written
	*/
	void writeTextLine(String text) throws IOException;

	/** Opens the file for writing */
	void open(String filename) throws IOException;

	/** Closes the File */
	void close() throws IOException;

	/** Gets the Result File */
	File getReport() throws IOException;

}


