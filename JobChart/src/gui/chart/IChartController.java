package gui.chart;

import model.Job;
import model.Location;
import gui.common.*;

/**
 * Controller interface for inventory view.
 */
public interface IChartController extends IController {
	
	boolean canAddLocation();
	
	boolean canEditLocation();
	
	boolean canRemoveLocation();
	
	boolean canAddJob();
	
	boolean canEditJob();
	
	boolean canRemoveJob();
	
	void getRandomJob();
	
	void addLocation();
	
	void editLocation();
	
	void removeLocation();
	
	void addJob();
	
	void editJob();
	
	void removeJob();
	
	void locationSelectionChanged();
	
	void jobSelectionChanged();
	
	void moveJobToLocation(Job job, Location location);
}

