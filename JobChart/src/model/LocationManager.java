package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;

public class LocationManager extends Observable implements Serializable
{
	private static final long serialVersionUID = -1014974085048035724L;
	
	private Map<String, Location> allLocations;
	
	private static LocationManager _locationManager;
	
	private JobManager _jobManager;
	
	public static LocationManager instance()
	{
		if(_locationManager == null)
			_locationManager = new LocationManager();
		return _locationManager;
	}
	
	public static void setInstance(LocationManager lm)
	{
		_locationManager = lm;
	}
	
	public void restoreSingletons()
	{
		JobManager.setInstance(_jobManager);
	}
	
	private LocationManager()
	{
		_jobManager = JobManager.instance();
		allLocations = new TreeMap<>();
	}
	
	/**
	 * Adds location to system
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 */
	public void addLocation(String name) throws IllegalArgumentException
	{
		if(name == null || name.isEmpty())
		{
			throw new IllegalArgumentException("Location name cannot be empty");
		}
		if(allLocations.containsKey(name))
		{
			throw new IllegalArgumentException("A location with this name already exists");
		}
		
		allLocations.put(name, new Location(name));
		this.setChanged();
		this.notifyObservers(new Notification(name, NotificationType.ADD_LOCATION));
	}
	
	/**
	 * @param name
	 * @return True iff name is valid and available
	 */
	public boolean canAddLocation(String name)
	{
		if(name == null || name.isEmpty() || name.equals("root"))
			return false;
		
		if(allLocations.keySet().contains(name))
			return false;
		
		return true;
	}
	
	/**
	 * @param loc
	 * @param name
	 * @return True iff name is valid and availabe, or loc already
	 * has given name
	 */
	public boolean canEditLocation(Location loc, String name)
	{
		if(loc.getName().equals(name))
			return true;
		return canAddLocation(name);
	}
	
	/**
	 * Removes location from system
	 * 
	 * @param name
	 * @throws IllegalArgumentException
	 */
	public void removeLocation(String name) throws IllegalArgumentException
	{
		if(!allLocations.containsKey(name))
		{
			throw new IllegalArgumentException("This location is not in the system");
		}
		
		allLocations.remove(name);
		this.setChanged();
		this.notifyObservers(new Notification(name, NotificationType.DELETE_LOCATION));
	}
	
	/**
	 * Edits the name of a location
	 * 
	 * @param oldName
	 * @param newName
	 * @throws IllegalArgumentException
	 */
	public void editLocation(String oldName, String newName) throws IllegalArgumentException
	{
		if(!allLocations.containsKey(oldName))
		{
			throw new IllegalArgumentException("This location is not in the system");
		}
		
		if(oldName.equals(newName))
			return;
		
		if(allLocations.containsKey(newName))
		{
			throw new IllegalArgumentException("There is already a location with this name");
		}
		
		// Update location table
		Location loc = allLocations.get(oldName);
		Set<Job> jobs = _jobManager.getJobsByLocation(loc);
		allLocations.remove(oldName);
		loc.setName(newName);
		allLocations.put(newName, loc);
		
		// Transfer jobs
		for(Job job : jobs)
		{
			_jobManager.transferJob(job, loc);
		}
		
		this.setChanged();
		this.notifyObservers(new Notification(newName, NotificationType.EDIT_LOCATION));
	}
	
	/**
	 * @param name
	 * @return Location that has given name
	 */
	public Location getLocation(String name)
	{
		return allLocations.get(name);
	}

	public Collection<Location> getAllLocations()
	{
		return allLocations.values();
	}
}
