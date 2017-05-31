package gui.job;

import java.util.Calendar;
import java.util.Date;

import model.Job;
import model.JobManager;
import model.Location;
import model.TimeRequired;
import gui.common.Controller;
import gui.common.IView;

public class RandomJobController extends Controller
	implements IRandomJobController
{
	private Location location;

	protected RandomJobController(IView view, Location location) {
		super(view);
		this.location = location;
		construct();
	}
	
	@Override
	protected IRandomJobView getView() {
		return (IRandomJobView)super.getView();
	}
	
	@Override
	public void enableComponents()
	{
		getView().enableLastDate(getView().getDateCheckbox());
		getView().enableTime(getView().getTimeCheckbox());
	}
	
	@Override
	public void loadValues()
	{
		getView().setDateCheckbox(false);
		getView().setTimeCheckbox(false);
		getView().setTime(TimeRequired.SHORT);
		getView().setLastDate(new Date());
		getView().enableLastDate(false);
		getView().enableTime(false);
		getView().enableOK(true);
	}

	@Override
	public void valuesChanged() {
		enableComponents();
	}

	@Override
	public void showRandomJob() {
		
		Location searchLoc = location;
		if(searchLoc == null || searchLoc.getName().equals("root"))
			searchLoc = null;
		
		TimeRequired time = null;
		if(getView().getTimeCheckbox())
		{
			time = getView().getTime();
		}
		
		Calendar lastCompleted = null;
		if(getView().getDateCheckbox())
		{
			lastCompleted = Calendar.getInstance();
			lastCompleted.setTime(getView().getLastDate());
		}
		
		Job randomJob = JobManager.instance().getRandomJob(searchLoc, time, lastCompleted);
		if(randomJob != null)
			getView().displayInformationMessage(randomJob.toString());
		else
			getView().displayInformationMessage("No job matches the given criteria");
	}

	@Override
	public void cancel() {
		return;
	}

	@Override
	public void timeFilterChanged() {
		enableComponents();
	}

	@Override
	public void dateFilterChanged() {
		enableComponents();
	}

}
