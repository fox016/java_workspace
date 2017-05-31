package model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class JobManager extends Observable implements Serializable
{
	private static final long serialVersionUID = 7826969719058374207L;
	
	private Set<Job> allJobs;
	private Map<Location, Set<Job>> jobsByLocation;
	
	private static JobManager _jobManager;
	
	public static JobManager instance()
	{
		if(_jobManager == null)
			_jobManager = new JobManager();
		return _jobManager;
	}
	
	public static void setInstance(JobManager jm)
	{
		_jobManager = jm;
	}
	
	private JobManager()
	{
		allJobs = new TreeSet<>();
		jobsByLocation = new TreeMap<>();
	}
	
	/**
	 * Adds a job to the system
	 * 
	 * @param newJob
	 * @throws IllegalArgumentException if newJob is not valid
	 */
	public void addJob(Job newJob) throws IllegalArgumentException
	{
		if(newJob == null || !newJob.isValidJob())
		{
			throw new IllegalArgumentException("Cannot add invalid job to system");
		}
		
		allJobs.add(newJob);
		
		if(newJob.getLocation() != null)
			addToLocation(newJob.getLocation(), newJob);
		
		this.setChanged();
		this.notifyObservers(new Notification(newJob, NotificationType.ADD_JOB));
	}
	
	/**
	 * Removes a job from the system
	 * 
	 * @param removedJob
	 * @throws IllegalArgumentException
	 */
	public void removeJob(Job removedJob) throws IllegalArgumentException
	{
		if(removedJob == null || !removedJob.isValidJob())
		{
			throw new IllegalArgumentException("Cannot remove invalid job");
		}
		
		if(!allJobs.contains(removedJob))
		{
			throw new IllegalArgumentException("This job was not found in the system");
		}
		
		allJobs.remove(removedJob);
		
		if(removedJob.getLocation() != null)
		{
			removeFromLocation(removedJob.getLocation(), removedJob);
		}
		
		this.setChanged();
		this.notifyObservers(new Notification(removedJob, NotificationType.DELETE_JOB));
	}
	
	/**
	 * Edits a job
	 * 
	 * @param oldJob
	 * @param newJob
	 * @throws IllegalArgumentException
	 */
	public void editJob(Job oldJob, Job newJob) throws IllegalArgumentException
	{
		if(oldJob == null || newJob == null)
		{
			throw new IllegalArgumentException("Expected Jobs, got Null pointers");
		}
		if(!oldJob.isValidJob() || !newJob.isValidJob())
		{
			throw new IllegalArgumentException("Cannot make a job invalid");
		}
		
		removeFromLocation(oldJob.getLocation(), oldJob);
		
		oldJob.setDescription(newJob.getDescription());
		oldJob.setLastCompleted(newJob.getLastCompleted());
		oldJob.setLocation(newJob.getLocation());
		oldJob.setTime(newJob.getTime());
		
		addToLocation(oldJob.getLocation(), oldJob);
		
		this.setChanged();
		this.notifyObservers(new Notification(oldJob, NotificationType.EDIT_JOB));
	}
	
	/**
	 * @return All jobs in system
	 */
	public Set<Job> getAllJobs()
	{
		return allJobs;
	}
	
	/**
	 * @param location
	 * @return All jobs in given location
	 * @throws IllegalArgumentException
	 */
	public Set<Job> getJobsByLocation(Location location) throws IllegalArgumentException
	{
		if(location == null)
		{
			throw new IllegalArgumentException("Cannot get jobs of Null location");
		}
		
		if(jobsByLocation.get(location) == null)
			jobsByLocation.put(location,  new TreeSet<Job>());
		return jobsByLocation.get(location);
	}
	
	/**
	 * Transfers a job to a new location
	 * 
	 * @param job
	 * @param newLocation
	 * @throws IllegalArgumentException
	 */
	public void transferJob(Job job, Location newLocation) throws IllegalArgumentException
	{
		if(job == null)
		{
			throw new IllegalArgumentException("Cannot transfer Null job");
		}
		if(!allJobs.contains(job))
		{
			throw new IllegalArgumentException("Cannot transfer job that is not in system");
		}
		
		if(job.getLocation() != null)
			removeFromLocation(job.getLocation(), job);
		if(newLocation != null)
			addToLocation(newLocation, job);
		
		job.setLocation(newLocation);
		
		this.setChanged();
		this.notifyObservers(new Notification(job, NotificationType.EDIT_JOB));
	}
	
	/**
	 * Gets a random job that fits the search criteria
	 * 
	 * @param location Location where job must be
	 * @param time Time length job requires
	 * @param lastCompleted Job hasn't been done since this date
	 * @return A random job
	 */
	public Job getRandomJob(Location location, TimeRequired time, Calendar lastCompleted)
	{
		Set<Job> possibleJobs = new TreeSet<>();
		Set<Job> badJobs = new TreeSet<>();
		possibleJobs.addAll(allJobs);
		
		if(location != null)
		{
			for(Job job : possibleJobs)
			{
				if(job.getLocation() == null || !job.getLocation().equals(location))
				{
					badJobs.add(job);
				}
			}
		}
		
		if(time != null)
		{
			for(Job job : possibleJobs)
			{
				if(job.getTime() != time)
				{
					badJobs.add(job);
				}
			}
		}
		
		
		if(lastCompleted != null)
		{
			for(Job job : possibleJobs)
			{
				if(job.getLastCompleted() != null && job.getLastCompleted().compareTo(lastCompleted) > 0)
				{
					badJobs.add(job);
				}
			}
		}
		
		possibleJobs.removeAll(badJobs);
		System.out.println("Possible Jobs: " + possibleJobs);
		
		Job randomJob = getRandomSetValue(possibleJobs);
		
		this.setChanged();
		this.notifyObservers(new Notification(randomJob, NotificationType.RANDOM_JOB));
		
		return randomJob;
	}
	
	/**
	 * @param jobs
	 * @return Random Job from set of jobs
	 * @throws IllegalStateException
	 */
	private Job getRandomSetValue(Set<Job> jobs) throws IllegalStateException
	{
		if(jobs.isEmpty())
			return null;
		
		Random rand = new Random();
		int index = rand.nextInt() % jobs.size();
		index = Math.abs(index);
		
		int i = 0;
		for(Job job : jobs)
		{
			if(i == index)
				return job;
			else
				i++;
		}
		
		throw new IllegalStateException("Should never get here: index=" +
				index + ", i=" + i);
	}
	
	/**
	 * Adds a job to the location mapping
	 * 
	 * @param location
	 * @param job
	 */
	private void addToLocation(Location location, Job job)
	{
		if(location == null)
			return;
		if(job == null)
			return;
		
		Set<Job> jobset = jobsByLocation.get(location);
		if(jobset == null)
		{
			jobset = new TreeSet<>();
			jobsByLocation.put(location, jobset);
		}
		
		jobset.add(job);
	}
	
	/**
	 * Removes a job from the location mapping
	 * 
	 * @param location
	 * @param job
	 */
	private void removeFromLocation(Location location, Job job)
	{
		if(location == null)
			return;
		if(job == null)
			return;
		
		Set<Job> jobset = jobsByLocation.get(location);
		if(jobset == null)
		{
			jobset = new TreeSet<>();
			jobsByLocation.put(location, jobset);
		}
		
		jobset.remove(job);
	}
}
