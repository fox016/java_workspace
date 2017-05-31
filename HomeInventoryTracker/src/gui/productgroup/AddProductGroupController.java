package gui.productgroup;

import gui.common.*;
import gui.inventory.*;
import model.Facade;
import model.ProductContainer;
//import model.ProductGroup;
import model.Size;
import model.SupplyType;

/**
 * Controller class for the add product group view.
 */
public class AddProductGroupController extends Controller implements
		IAddProductGroupController 
{
    /**
     * The ProductContainer we are going to add to.
     */
    private ProductContainerData parent;
    
    private Translator translator;
	
	/**
	 * Constructor.
	 * 
	 * @param view Reference to add product group view
	 * @param container Product container to which the new product group is being added
	 */
	public AddProductGroupController(IView view, ProductContainerData container)
	{
		super(view);this.parent=container;
		assert(parent != null);
		
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
	protected IAddProductGroupView getView() 
        {
		return (IAddProductGroupView)super.getView();
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
		getView().enableOK(validate());
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
		// Initialize at 0 Count (See demo)
		getView().setSupplyValue("0");
		getView().setSupplyUnit(SizeUnits.Count);
	}

	//
	// IAddProductGroupController overrides
	//

	/**
	 * This method is called when any of the fields in the
	 * add product group view is changed by the user.
	 */
	@Override
	public void valuesChanged() 
        {
            enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the add product group view.
	 */
	@Override
	public void addProductGroup() 
        {
			if(!validate())
			{
				getView().displayErrorMessage("Not valid product group");
				return;
			}
		
			String name = getView().getProductGroupName();
			double amount = Double.parseDouble(getView().getSupplyValue());
			SupplyType type =
					translator.getSupplyTypeFromSizeUnit(getView().getSupplyUnit());
			Size supply = new Size(amount, type);
			ProductContainer parentContainer =
					translator.getProductContainerFromData(parent);
			
			Facade.addProductGroup(name, supply, parentContainer);
	}
        
        private boolean validate()
        {
		  		float amount;
				try{
					amount = Float.parseFloat(getView().getSupplyValue());
				}
				catch(NumberFormatException e)
				{ return false; }
				if(Float.isNaN(amount) || Float.isInfinite(amount))
					return false;
            return ProductGroupFuncs.validate(
            		translator.getProductContainerFromData(parent),
                    getView().getProductGroupName(), getView().getSupplyValue(),
                    getView().getSupplyUnit(), null); //Yes, a null is intended here.
        }

}

