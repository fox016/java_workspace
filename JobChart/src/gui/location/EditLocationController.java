package gui.location;

import model.Location;
import model.LocationManager;
import gui.common.*;

/**
 * Controller class for the add storage unit view.
 */
public class EditLocationController extends Controller implements
		IEditLocationController {
	
	Location target;
	
	/**
	 * Constructor.
	 * 
	 * @param view Reference to add storage unit view
	 */
	public EditLocationController(IView view, Location location)
	{
		super(view);
		target = location;
		construct();
	}

	//
	// Controller overrides
	//
	
	/**
	 * Returns a reference to the view for this controller.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns a reference to the view for this controller.}
	 */
	@Override
	protected IEditLocationView getView() {
		return (IEditLocationView)super.getView();
	}

	/**
	 * Sets the enable/disable state of all components in the controller's view.
	 * A component should be enabled only if the user is currently
	 * allowed to interact with that component.
	 * 
	 * {@pre None}
	 * 
	 * {@post The enable/disable state of all components in the controller's view
	 * have been set appropriately.}
	 */
	@Override
	protected void enableComponents() {
		String newName = this.getView().getLocationName();
		boolean canEdit = LocationManager.instance().canEditLocation(target, newName);
		this.getView().enableOK(canEdit);
	}

	/**
	 * Loads data into the controller's view.
	 * 
	 *  {@pre None}
	 *  
	 *  {@post The controller has loaded data into its view}
	 */
	@Override
	protected void loadValues() {
		getView().setLocationName(target.getName());
	}

	//
	// IEditLocationController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * add storage unit view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the add storage unit view.
	 */
	@Override
	public void editLocation()
	{
		String newName = this.getView().getLocationName();
                if (!LocationManager.instance().canEditLocation(target, newName))
                {
                    getView().displayErrorMessage(newName+" is not a valid name.");
                    return;
                }
		LocationManager.instance().editLocation(target.getName(), newName);
	}
}

