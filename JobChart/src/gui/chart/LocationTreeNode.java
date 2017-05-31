package gui.chart;

import javax.swing.tree.DefaultMutableTreeNode;

import model.Location;

@SuppressWarnings("serial")
public class LocationTreeNode extends DefaultMutableTreeNode {
	
	public LocationTreeNode(Location location)
	{
		super(location);
	}
	
	public Location getLocation()
	{
		return (Location)getUserObject();
	}
	
	public boolean isAllLocations()
	{
		return this.isRoot();
	}
	
	@Override
	public String toString()
	{
		if(isAllLocations())
		{
			return "Locations";
		}
		else
		{
			return getLocation().getName();
		}
	}

	public boolean isLocation() {
		return !isAllLocations();
	}
}

