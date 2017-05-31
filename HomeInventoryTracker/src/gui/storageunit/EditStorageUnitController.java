package gui.storageunit;

import model.Facade;
import model.StorageUnit;
import gui.common.*;
import gui.inventory.*;
import model.House;

/**
 * Controller class for the edit storage unit view.
 */
public class EditStorageUnitController extends Controller 
										implements IEditStorageUnitController {
	
	private ProductContainerData targetData;
	
	private Translator translator;
	
	/**
	 * Constructor.
	 * 
	 * @param view Reference to edit storage unit view
	 * @param target The storage unit being edited
	 */
	public EditStorageUnitController(IView view, ProductContainerData target) {
		super(view);
		
		targetData = target;
		translator = new Translator();

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
	protected IEditStorageUnitView getView() {
		return (IEditStorageUnitView)super.getView();
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
	protected void enableComponents() 
        {
            if(validate())
            {
                getView().enableOK(true);
            }
            else
            {
                getView().enableOK(false);
            }
            //In retrospect, I could have said:
            // getView().enableOK(validate());
            //but I think this is more readable.
	}

	/**
	 * Loads data into the controller's view.
	 * 
	 *  {@pre None}
	 *  
	 *  {@post The controller has loaded data into its view}
	 */
	@Override
	protected void loadValues() 
        {
		getView().setStorageUnitName(targetData.getName());
	}

	//
	// IEditStorageUnitController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * edit storage unit view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}

	/**
	 * This method is called when the user clicks the "OK"
	 * button in the edit storage unit view.
	 */
	@Override
	public void editStorageUnit()
	{
            if(getView().getStorageUnitName().equals(targetData.getName()))
            {
                return; //He must not have wanted the name changed after all.
            }
            if(!validate())
            {
                getView().displayErrorMessage(getView().getStorageUnitName()+
                        " is not a valid name.");
                return; //Somebody is obviously intending to break our functionality!
            }
            Facade.setStorageUnitName((StorageUnit)translator.
                    getProductContainerFromData(targetData), getView().getStorageUnitName());
	}
        
            private boolean validate() 
    {
        if (House.instance().isValidName(getView().getStorageUnitName())||
                getView().getStorageUnitName().equals(targetData.getName()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}

