package gui.reports.expired;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import gui.common.*;
import model.report.*;


/**
 * Controller class for the expired items report view.
 */
public class ExpiredReportController extends Controller implements
										IExpiredReportController {
	
	/**
	 * Constructor.
	 * 
	 * @param view Reference to the expired items report view
	 */
	public ExpiredReportController(IView view) {
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
	protected IExpiredReportView getView() {
		return (IExpiredReportView)super.getView();
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
	// IExpiredReportController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * expired items report view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the expired items report view.
	 */
	@Override
	public void display() {
		File expiredReport;
		try {
	if(getView().getFormat().equals(FileFormat.HTML))	
		expiredReport = ReportGenerator.generateExpiredReport("expired_items.html",
				ReportType.HTML);	
	else
		expiredReport = ReportGenerator.generateExpiredReport("expired_items.pdf",
				ReportType.PDF);
	
		Desktop.getDesktop().open(expiredReport);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getView().displayErrorMessage("IOException");
			e.printStackTrace();
		}
	}
}

