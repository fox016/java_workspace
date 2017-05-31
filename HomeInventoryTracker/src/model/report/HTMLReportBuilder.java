
package model.report;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

	/** Defines the Interface for creating Reports
		of an abstract filetype
		@author Christopher Tensmeyer
		@version March 6th, 2013
	*/

public class HTMLReportBuilder implements ReportBuilder
{
	
	private boolean isOpen;
	private boolean isInTable;
	private FileWriter writer;
	private File report;
	
	public HTMLReportBuilder()
	{
		isOpen = false;
		isInTable = false;
	}

	/** Writes the Title of the Report
		@param title The Title
		@throws IOException If the HTMLReportBuilder has not been opened for writing or
							this method is called after startTable(int) and before endTable()
	*/
	public void writeTitle(String title) throws IOException
	{
		if(!isOpen)
			throw new IOException("HTMLReportBuilder needs to be opened before writing");
		if(isInTable)
			throw new IOException("HTMLReportBuilder cannot write a title in the table");
		writer.write("<h2 style='text-align:center'>" + escape(title) + "</h2>");
	}

	/** Starts a Table
		@param columns The number of columns in the table
		@param widths The width of the specified columns.  If null, assumed to be uniform.
		@throws IOException If the ReportBuilder has not been opened for writing or
						if this method is invoked twice without calling endTable()
	*/
	public void startTable(int columns, int[] widths) throws IOException
	{
		if(!isOpen)
			throw new IOException("HTMLReportBuilder needs to be opened before writing");
		if(isInTable)
			throw new IOException("HTMLReportBuilder cannot nest tables");
		isInTable = true;
		writer.write("<table border=1>");
		//TODO: implement table widths
	}

	/** Starts a Table
		@param columns The number of columns in the table
		@throws IOException If the ReportBuilder has not been opened for writing or
						if this method is invoked twice without calling endTable()
	*/
	public void startTable(int columns) throws IOException
	{
		startTable(columns, null);
	}

	/** Closes the Table 
		@throws IOException If the ReportBuilder has not been opened for writing or
						if this method is invoked without calling startTable(int)
	*/
	public void endTable() throws IOException
	{
		if(!isOpen)
			throw new IOException("HTMLReportBuilder needs to be opened before writing");
		if(!isInTable)
			throw new IOException("HTMLReportBuilder cannot end a table that has not started");
		isInTable = false;
		writer.write("</table>");
	}

	/** Writes the header for each column in the table in bold
		@param headers The headers
		@throws IOException If the ReportBuilder has not been opened for writing
	*/
	public void writeTableHeader(String[] headers) throws IOException
	{
		if(!isOpen)
			throw new IOException("HTMLReportBuilder needs to be opened before writing");
		if(!isInTable)
			throw new IOException("HTMLReportBuilder cannot write table header");
		//TODO: Check the implementation of table headers
		String row = "<tr>";
		for(String cell : headers)
		{
			row += "<td><b>";
			row += escape(cell);
			row += "</b></td>";
		}
		row += "</tr>";	
		writer.write(row);
	}

	/** Writes a row in the table
		@param content The content of each cell in the table.
		@throws IOException If the ReportBuilder has not been opened for writing
	*/
	public void writeTableRow(String[] content) throws IOException
	{
		
		if(!isOpen)
			throw new IOException("HTMLReportBuilder needs to be opened before writing");
		if(!isInTable)
			throw new IOException("HTMLReportBuilder cannot write table row");
		String row = "<tr>";
		for(String cell : content)
		{
			row += "<td>";
			row += escape(cell);
			row += "</td>";
		}
		row += "</tr>";
		writer.write(row);
	}

	/** Writes a blank line
		@throws IOException If the ReportBuilder has not been opened for writing or
							if a table has been started and not ended.
	*/
	public void writeBlankLine() throws IOException
	{
		if(!isOpen)
			throw new IOException("HTMLReportBuilder needs to be opened before writing");
		if(isInTable)
			throw new IOException("HTMLReportBuilder cannot write blank line inside table");
		writer.write("</br>");
	}

	/** Writes a header (bold) of plain text 
		@param text The text to be written
		@throws IOException If the ReportBuilder has not been opened for writing or
							this method is called after startTable(int) and before endTable()
	*/
	public void writeTextHeader(String text) throws IOException
	{
		if(!isOpen)
			throw new IOException("HTMLReportBuilder needs to be opened before writing");
		if(isInTable)
			throw new IOException("HTMLReportBuilder cannot write text header inside table");
		writer.write("<h4>" + escape(text) + "</h4>");
	}

	/** Writes a left justified line of text
		@param text The text to be written
		@throws IOException If the ReportBuilder has not been opened for writing or
							this method is called after startTable(int) and before endTable()
	*/
	public void writeTextLine(String text) throws IOException
	{
		if(!isOpen)
			throw new IOException("HTMLReportBuilder needs to be opened before writing");
		if(isInTable)
			throw new IOException("HTMLReportBuilder cannot write text line inside table");
		writer.write("<p>" + escape(text) + "</p>");
	}

	/** Opens the file for writing
		@param filename  The filename where we want to save the Report
		@throws IOException If the ReportBuilder has already an opened file or
					if the file could not be opened
	*/
	public void open(String filename) throws IOException
	{
		if(isOpen)
			throw new IOException("HTMLReportBuilder cannot open another file");
		report = new File(filename);
		writer = new FileWriter(report);
		isOpen = true;
		
		writer.write("<html>\n<body>\n");
	}

	/** Closes the File */
	public void close() throws IOException
	{
		writer.write("</body>\n</html>");
		writer.flush();
		writer.close();
		isOpen = false;
		writer = null;
	}

	/** Returns the generated Report
		@throws IOException If the ReportBuilder has been opened and not closed
		@return A File object for the generated HTML report or null if the ReportBuilder
					has not been opened.
	*/
	public File getReport() throws IOException
	{
		if(isOpen)
			throw new IOException("HTMLReportBuilder cannot retrieve " +
					"a report untill it has been closed");
		return report;
	}
	String escape(String text) {
		if (text == null) return null;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (c == '&') {
				sb.append("&amp;");
			} else if (c == '\'') {
				sb.append("&#39;");
			} else if (c == '"') {
				sb.append("&quot;");
			} else if (c == '<') {
				sb.append("&lt;");
			} else if (c == '>') {
				sb.append("&gt;");
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}


}


