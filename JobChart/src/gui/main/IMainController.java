package gui.main;

import gui.common.IController;


/**
 * Controller interface for the main view.  The main view allows the user
 * to print reports and exit the program.
 */
public interface IMainController extends IController {

	/**
	 * Returns true if and only if the "Exit" menu item should be enabled.
	 */
	boolean canExit();
	
	/**
	 * This method is called when the user exits the application.
	 */
	void exit();
}

