package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Location implements Serializable, Comparable<Location>
{
	private static final long serialVersionUID = -1958716910728669361L;
	
	private List<Location> children;
	
	private String name;
	
	/**
	 * Creates a Location
	 * 
	 * @param name
	 */
	public Location(String name)
	{
		setName(name);
		children = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	/**
	 * Changes the name of the location. This should only be called
	 * through the LocationManager
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public int compareTo(Location o)
	{
		return name.compareTo(o.getName());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Location)
			return name.equals(((Location) o).getName());
		else
			return false;
	}

	public int getChildCount() {
		return children.size();
	}

	public Location getChild(int i) {
		return children.get(i);
	}
	
	public void addChild(Location newLocation) {
		children.add(newLocation);
	}

	public void removeAllChildren() {
		children.clear();
	}
}
