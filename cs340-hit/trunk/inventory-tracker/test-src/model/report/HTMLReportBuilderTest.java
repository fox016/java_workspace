
package model.report;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
	This is the HTMLReportBuilderTest class
	Depends on ReportBuilderBaseTest for implementation independent tests
	@author Christopher Tensmeyer
	@version March 11, 2013
*/

public class HTMLReportBuilderTest extends ReportBuilderBaseTest
{
	
	protected ReportBuilder getReportBuilder()
	{
		return new HTMLReportBuilder();
	}

	protected String extension() 
	{
		return ".html";
	}

}


