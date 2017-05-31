package gui.job;

import java.util.Date;

import model.TimeRequired;
import gui.common.*;

/**
 * View interface for the add product view.
 */
public interface IRandomJobView extends IView {
	
	boolean getTimeCheckbox();
	
	void setTimeCheckbox(boolean value);
	
	TimeRequired getTime();
	
	void setTime(TimeRequired value);
	
	void enableTime(boolean value);
	
	boolean getDateCheckbox();
	
	void setDateCheckbox(boolean value);
	
	Date getLastDate();
	
	void setLastDate(Date value);
	
	void enableLastDate(boolean value);
	
	void enableOK(boolean value);
}

