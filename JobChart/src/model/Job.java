package model;

import java.io.Serializable;
import java.util.Calendar;

import common.util.DateUtils;

public class Job implements Serializable, Comparable<Job>
{
	private static final long serialVersionUID = 137037022350748715L;
	
	private String description;
	private Location location;
	private TimeRequired time;
	private Calendar lastCompleted;
	
	/**
	 * Creates a Job
	 * 
	 * @param desc
	 * @param time
	 * @param loc
	 */
	public Job(String desc, TimeRequired time, Location loc)
	{
		setDescription(desc);
		setTime(time);
		setLocation(loc);
		setLastCompleted(null);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public TimeRequired getTime() {
		return time;
	}

	public void setTime(TimeRequired time) {
		this.time = time;
	}

	public Calendar getLastCompleted() {
		return lastCompleted;
	}

	public void setLastCompleted(Calendar lastCompleted) {
		this.lastCompleted = lastCompleted;
	}
	
	/**
	 * @return true iff this represents a valid Job object
	 */
	public boolean isValidJob()
	{
		if(description == null || description.isEmpty())
			return false;
		if(time == null)
			return false;
		if(lastCompleted != null)
		{
			Calendar today = Calendar.getInstance();
			if(lastCompleted.compareTo(today) > 0)
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		return description + ", " + location + ", " + time + ", " + DateUtils.formatDate(lastCompleted);
	}

	@Override
	public int compareTo(Job o) {
		if(time != o.getTime())
		{
			return time.compareTo(o.getTime());
		}
		if(lastCompleted != null && o.getLastCompleted() != null)
		{
			if(!lastCompleted.equals(o.getLastCompleted()))
			{
				return lastCompleted.compareTo(o.getLastCompleted());
			}
		}
		if(location != null && o.getLocation() != null)
		{
			if(!location.equals(o.getLocation()))
			{
				return location.compareTo(o.getLocation());
			}
		}
		return description.compareTo(o.getDescription());
	}
}
