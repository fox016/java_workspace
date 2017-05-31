package gui.main;

import java.io.IOException;

import model.DataSerializer;
import gui.common.*;

/**
 * Controller class for the main view.  The main view allows the user
 * to print reports and exit the application.
 */
public class MainController extends Controller implements IMainController {

	/**
	 * Constructor.
	 *  
	 * @param view Reference to the main view
	 */
	public MainController(IMainView view)
	{
		super(view);
		
		construct();
	}
	
	/**
	 * Returns a reference to the view for this controller.
	 */
	@Override
	protected IMainView getView()
	{
		return (IMainView)super.getView();
	}

	//
	// IMainController overrides
	//

	/**
	 * Returns true if and only if the "Exit" menu item should be enabled.
	 */
	@Override
	public boolean canExit()
	{
		return true;
	}

	/**
	 * This method is called when the user exits the application.
	 */
	@Override
	public void exit()
	{
		try {
			DataSerializer.save("./");
		} catch (IOException e)
		{
			System.out.println("Save failed: " + e.getMessage());
		}
	}
}

