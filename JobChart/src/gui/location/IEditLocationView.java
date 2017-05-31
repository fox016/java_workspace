package gui.location;

import gui.common.*;

/**
 * View interface for the add Location view.
 */
public interface IEditLocationView extends IView {

	/**
	 * Returns the value of the "Location Name" field.
	 */
	String getLocationName();
	
	/**
	 * Sets the value of the "Location Name" field.
	 * 
	 * @param value New "Location Name" value
	 */
	void setLocationName(String value);
	
	/**
	 * Sets the enable/disable state of the "Location Name" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableLocationName(boolean value);
	
	/**
	 * Sets the enable/disable state of the "OK" button.
	 * 
	 * @param value New enable/disable value
	 */
	void enableOK(boolean value);

}

