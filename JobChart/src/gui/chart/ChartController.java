package gui.chart;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import model.DataSerializer;
import model.Job;
import model.JobManager;
import model.Location;
import model.LocationManager;
import model.Notification;
import gui.common.Controller;

public class ChartController extends Controller implements IChartController, Observer
{
	private Location root;

	protected ChartController(IChartView view)
	{
		super(view);
		
		construct();
		
		LocationManager.instance().addObserver(this);
		JobManager.instance().addObserver(this);
	}
	
	@Override
	protected IChartView getView()
	{
		return (IChartView)super.getView();
	}
	
	@Override
	protected void loadValues()
	{
		root = new Location("root");
		
		getView().setLocations(root);
		
		try {
			DataSerializer.load("./");
		} catch (ClassNotFoundException | IOException e) {
			getView().displayErrorMessage("No job chart found. " +
					"Creating a new job chart.");
			return;
		}
		
		loadLocationsFromModel(null);
	}

	@Override
	public boolean canAddLocation() {
		return true;
	}

	@Override
	public boolean canEditLocation() {
		return true;
	}

	// Can remove location if it has no jobs
	@Override
	public boolean canRemoveLocation()
	{
		Location loc = getView().getSelectedLocation();
		return JobManager.instance().getJobsByLocation(loc).isEmpty();
	}

	@Override
	public boolean canAddJob() {
		return true;
	}

	@Override
	public boolean canEditJob() {
		return true;
	}

	@Override
	public boolean canRemoveJob() {
		return true;
	}

	@Override
	public void addLocation() {
		getView().displayAddLocationView();
	}

	@Override
	public void editLocation() {
		getView().displayEditLocationView();
	}

	@Override
	public void removeLocation() {
		Location loc = getView().getSelectedLocation();
		LocationManager.instance().removeLocation(loc.getName());
	}

	@Override
	public void addJob() {
		getView().displayAddJobView();
	}

	@Override
	public void editJob() {
		getView().displayEditJobView();
	}

	@Override
	public void removeJob() {
		Job job = getView().getSelectedJob();
		JobManager.instance().removeJob(job);
	}

	@Override
	public void locationSelectionChanged() {
		setJobTable();
	}

	@Override
	public void jobSelectionChanged() {
		return;
	}

	@Override
	public void moveJobToLocation(Job job, Location location) {
		JobManager.instance().transferJob(job,  location);
	}
	
	public void getRandomJob()
	{
		getView().displayRandomJobView();
	}
	
	/**
	 * Query model and load Job table for selected Location
	 */
	private void setJobTable()
	{
		Location loc = getView().getSelectedLocation();
		
		Set<Job> jobs;
		if(loc.equals(root) || loc == null)
		{
			jobs = JobManager.instance().getAllJobs();
		}
		else
		{
			jobs = JobManager.instance().getJobsByLocation(loc);
		}
		
		Job[] table = new Job[jobs.size()];
		int i = 0;
		for(Job job : jobs)
		{
			table[i++] = job;
		}
		
		getView().setJobs(table);
	}
	
    /**
     * Query model and load Location tree into view
     */
    private void loadLocationsFromModel(String selectWhenDone)
    {
		root.removeAllChildren();
		
    	Collection<Location> locs = LocationManager.instance().getAllLocations();
    	
    	Iterator<Location> locIter = locs.iterator();
		while(locIter.hasNext())
		{
			root.addChild(locIter.next());
		}
		
		getView().setLocations(root);
		if(selectWhenDone != null)
			getView().selectLocation(LocationManager.instance().getLocation(selectWhenDone));
    }

	@Override
	public void update(Observable o, Object arg) {
		
		Notification note = (Notification) arg;
		
		switch(note.getType())
		{
		case ADD_LOCATION:
		case DELETE_LOCATION:
		case EDIT_LOCATION:
			loadLocationsFromModel((String)note.getObject());
			break;
		case ADD_JOB:
		case DELETE_JOB:
		case EDIT_JOB:
			setJobTable();
			break;
		case RANDOM_JOB:
			getView().selectJob((Job)note.getObject());
			break;
		}
	}
}
