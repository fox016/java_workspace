package gui.job;

import java.util.Calendar;
import java.util.Date;

import model.Job;
import model.JobManager;
import model.TimeRequired;

import gui.common.*;

/**
 * Controller class for the Edit item view.
 */
public class EditJobController extends Controller implements
		IEditJobController {
	
	Job target;
	
	/**
	 * Constructor.
	 * 
	 * @param view Reference to the Edit product view
	 * @param barcode Barcode for the product being Edited
	 * @param target 
	 */
	public EditJobController(IView view, Job job)
	{
		super(view);
		
		target = job;
		
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
	protected IEditJobView getView() {
		return (IEditJobView)super.getView();
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
		getView().enableDescription(true);
		getView().enableLastDate(true);
		getView().enableTime(true);
		getView().enableOK(isValidJob());
	}
	
	/**
	 * @return True iff values in view can make a valid job
	 */
	private boolean isValidJob()
	{
		String desc = getView().getDescription();
		Date lastDate = getView().getLastDate();
		TimeRequired time = getView().getTime();
		Calendar last = Calendar.getInstance();
		if(lastDate != null)
			last.setTime(lastDate);
		else
			last = null;
		
		Job job = new Job(desc, time, target.getLocation());
		job.setLastCompleted(last);
		
		return job.isValidJob();
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
		getView().setDescription(target.getDescription());
		getView().setLastDate(target.getLastCompleted().getTime());
		getView().setTime(target.getTime());
	}

	//
	// IEditProductController overrides
	//
	
	/**
	 * This method is called when any of the fields in the
	 * Edit product view is changed by the user.
	 */
	@Override
	public void valuesChanged()
	{	
		enableComponents();
	}
	
	@Override
	public void cancel()
	{
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the Edit product view.
	 */
	@Override
	public void editJob()
	{	
		String desc = getView().getDescription();
		Date lastDate = getView().getLastDate();
		TimeRequired time = getView().getTime();
		Calendar last = Calendar.getInstance();
		if(lastDate != null)
			last.setTime(lastDate);
		else
			last = null;
		
		Job newJob = new Job(desc, time, target.getLocation());
		newJob.setLastCompleted(last);
		
		JobManager.instance().editJob(target, newJob);
	}
}

