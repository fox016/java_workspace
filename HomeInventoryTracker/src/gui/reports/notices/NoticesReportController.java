package gui.reports.notices;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import model.report.ReportGenerator;
import model.report.ReportType;
import gui.common.*;

/**
 * Controller class for the notices report view.
 */
public class NoticesReportController extends Controller implements
		INoticesReportController {

	/**
	 * Constructor.
	 * 
	 * @param view Reference to the notices report view
	 */	
	public NoticesReportController(IView view) {
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
	protected INoticesReportView getView() {
		return (INoticesReportView)super.getView();
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
		getView().enableOK(true);
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
		getView().setFormat(FileFormat.PDF);
	}

	//
	// InoticesReportController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * notices report view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the notices report view.
	 */
	@Override
	public void display() {
		
		File noticesReport;
		
		try {
			
			// Generate HTML report
			if(getView().getFormat().equals(FileFormat.HTML))	
				noticesReport = ReportGenerator.generateNoticesReport("notices.html",
						ReportType.HTML);
			
			// Generate PDF report
			else
				noticesReport = ReportGenerator.generateNoticesReport("notices.pdf",
						ReportType.PDF);
	
			Desktop.getDesktop().open(noticesReport);
		
		}catch (IOException e)
		{
			getView().displayErrorMessage("IOException");
			e.printStackTrace();
		}
	}
}
