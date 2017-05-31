package gui.reports.supply;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import model.report.ReportGenerator;
import model.report.ReportType;
import gui.common.*;

/**
 * Controller class for the N-month supply report view.
 */
	public class SupplyReportController extends Controller implements
		ISupplyReportController {
/*---	STUDENT-INCLUDE-BEGIN

	/**
	 * Constructor.
	 * 
	 * @param view Reference to the N-month supply report view
	 */	
	public SupplyReportController(IView view) {
		super(view);
		
		construct();
	}

	//
	// Controller overrides
	//
	
	/**
	 * Returns a reference to the view for this controller.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns a reference to the view for this controller.}
	 */
	@Override
	protected ISupplyReportView getView() {
		return (ISupplyReportView)super.getView();
	}

	/**
	 * Sets the enable/disable state of all components in the controller's view.
	 * A component should be enabled only if the user is currently
	 * allowed to interact with that component.
	 * 
	 * {@pre None}
	 * 
	 * {@post The enable/disable state of all components in the controller's view
	 * have been set appropriately.}
	 */
	@Override
	protected void enableComponents() {
		getView().enableFormat(true);
		getView().enableMonths(true);
		getView().enableOK(isValidMonth());
	}
	
	/**
	 * @return True iff month in view is valid (1-100)
	 */
	private boolean isValidMonth()
	{
		String monthStr = getView().getMonths();
		int month = 0;
		try {
			month = Integer.parseInt(monthStr);
		}catch(NumberFormatException e)
		{
			return false;
		}
		
		if(month >= 1 && month <= 100)
			return true;
		
		return false;
	}

	/**
	 * Loads data into the controller's view.
	 * 
	 *  {@pre None}
	 *  
	 *  {@post The controller has loaded data into its view}
	 */
	@Override
	protected void loadValues() {
		getView().setMonths("1");
		getView().setFormat(FileFormat.PDF);
	}

	//
	// IExpiredReportController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * N-month supply report view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the N-month supply report view.
	 */
	@Override
	public void display() {
		
		// Validate form data
		if(!isValidMonth())
		{
			getView().displayErrorMessage("Month must be 1-100");
			return;
		}
		
		File supplyReport;
		int months = Integer.parseInt(getView().getMonths());
		
		try {
			
			// Generate HTML report
			if(getView().getFormat().equals(FileFormat.HTML))	
				supplyReport = ReportGenerator.generateNMonthSupplyReport("supply.html",
						ReportType.HTML, months);
			
			// Generate PDF report
			else
				supplyReport = ReportGenerator.generateNMonthSupplyReport("supply.pdf",
						ReportType.PDF, months);
	
			Desktop.getDesktop().open(supplyReport);
		
		}catch (IOException e)
		{
			getView().displayErrorMessage("IOException");
			e.printStackTrace();
		}
	}

}

