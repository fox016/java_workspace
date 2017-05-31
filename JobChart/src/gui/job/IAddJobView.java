package gui.job;

import java.util.Date;

import model.TimeRequired;
import gui.common.*;

/**
 * View interface for the add product view.
 */
public interface IAddJobView extends IView {

	/**
	 * Returns the value of the "Description" field.
	 */
	String getDescription();
	
	/**
	 * Sets the value of the "Description" field.
	 * 
	 * @param value New "Description" value
	 */
	void setDescription(String value);
	
	/**
	 * Sets the enable/disable state of the "Description" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableDescription(boolean value);
	
	/**
	 * Returns the value of the "Time Required" field.
	 */
	TimeRequired getTime();
	
	/**
	 * Sets the value of the "Time Required" field.
	 * 
	 * @param value New "Time Required" value
	 */
	void setTime(TimeRequired value);
	
	/**
	 * Sets the enable/disable state of the "Time Required" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableTime(boolean value);

	/**
	 * Returns the value of the "Last Date" field, or null if
	 * the field's value is invalid.
	 */
	Date getLastDate();
	
	/**
	 * Sets the value of the "Last Date" field.
	 * 
	 * @param value New "Last Date" value
	 */
	void setLastDate(Date value);
	
	/**
	 * Sets the enable/disable state of the "Last Date" field.
	 * 
	 * @param value New enable/disable value
	 */
	void enableLastDate(boolean value);
	
	/**
	 * Sets the enable/disable state of the "OK" button.
	 * 
	 * @param value New enable/disable value
	 */
	void enableOK(boolean value);
}

