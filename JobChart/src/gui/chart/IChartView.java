package gui.chart;

import model.Job;
import model.Location;
import gui.common.*;

/**
 * View interface for inventory view.
 */
public interface IChartView extends IView {
	
	void setLocations(Location root);
	
	void selectLocation(Location location);
	
	Location getSelectedLocation();
	
	void setJobs(Job[] jobs);
	
	Job getSelectedJob();
	
	void selectJob(Job job);
	
	void displayAddLocationView();
	
	void displayAddJobView();
	
	void displayEditLocationView();
	
	void displayEditJobView();

	void displayRandomJobView();
}

