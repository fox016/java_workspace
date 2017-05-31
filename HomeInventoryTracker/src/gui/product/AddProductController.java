package gui.product;

import common.plugin.PluginManager;
import java.util.Calendar;

import model.Facade;
import model.Product;
import model.ProductBarcode;
import model.ProductContainer;
import model.Size;
import model.SupplyType;
import gui.common.*;

/**
 * Controller class for the add item view.
 */
public class AddProductController extends Controller implements
		IAddProductController {
	
	private ProductContainer targetContainer;
	
	private Translator translator;
	
	/**
	 * Constructor.
	 * 
	 * @param view Reference to the add product view
	 * @param barcode Barcode for the product being added
	 * @param target 
	 */
	public AddProductController(IView view, String barcode, ProductContainer target) {
		super(view);
		
		translator = new Translator();
		construct();
		
		getView().enableBarcode(barcode == null || barcode.isEmpty());
                
                getView().enableDescription(false);
                getView().setDescription("Please wait - Getting description");
                
                targetContainer = target;
                
                final ProductBarcode barcode2 = new ProductBarcode(barcode);
                
                Thread t = new Thread(new Runnable()
                {
                    @Override
                    public void run() 
                    {
                        String getDescription = PluginManager.inst().getDescFromBarCode(barcode2);
                
                        getView().setDescription(getDescription);
								valuesChanged();
                        getView().enableDescription(true);                    } 
                });
                t.start();
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
	protected IAddProductView getView() {
		return (IAddProductView)super.getView();
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
		boolean enableSizeValue = getView().getSizeUnit() != SizeUnits.Count;
		getView().enableSizeValue(enableSizeValue);
		if(!enableSizeValue)
		{
			getView().setSizeValue("1");
		}
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
		getView().setDescription("");
		getView().setShelfLife("0");
		getView().setSupply("0");
	}

	//
	// IAddProductController overrides
	//
	
	/**
	 * This method is called when any of the fields in the
	 * add product view is changed by the user.
	 */
	@Override
	public void valuesChanged() 
        {
		
		enableComponents();
		
		boolean canAdd = false;
		
		// Get values from view to validate
		String barcodeStr = getView().getBarcode();
		String desc = getView().getDescription();
		SizeUnits sizeUnit = getView().getSizeUnit();
		String sizeValueStr = getView().getSizeValue();
		String shelfStr = getView().getShelfLife();
		String supplyStr = getView().getSupply();
		
		if(productValuesEmpty(barcodeStr, desc, sizeUnit, sizeValueStr, shelfStr, supplyStr))
		{
			this.getView().enableOK(canAdd);
			return;
		}
		
		// Create Product Barcode
		ProductBarcode barcode = new ProductBarcode(barcodeStr);
		
		// Create size
		double amount = 0;
		try {
			amount = (double) Float.parseFloat(sizeValueStr);
		}catch(NumberFormatException e)
		{
			this.getView().enableOK(canAdd);
			return;
		}
		if(amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount))
		{
			this.getView().enableOK(canAdd);
			return;
		}
		SupplyType supplyType = translator.getSupplyTypeFromSizeUnit(sizeUnit);
		Size size = null;
		try {
			size = new Size(amount, supplyType);
		}catch(IllegalArgumentException e)
		{
			this.getView().enableOK(canAdd);
			return;
		}
		// Parse shelf life and 3-month-supply
		int shelfLife = 0;
		int supply = 0;
		try {
			shelfLife = Integer.parseInt(shelfStr);
			supply = Integer.parseInt(supplyStr);
		}catch(NumberFormatException e)
		{
			this.getView().enableOK(canAdd);
			return;
		}
		
		// Create product and test validity
		Product product = new Product(Calendar.getInstance(), barcode,
				desc, size, shelfLife, supply);
		canAdd = Facade.canAddProductToContainer(product, targetContainer);
		this.getView().enableOK(canAdd);
	}
	
	@Override
	public void cancel()
	{
		/* This was commented out but you have to do this to clear
		 * the product barcode on the addItemBatch view!!!
		 */
		Facade.addProductToContainer(null, null);
	}
	
	/**
	 * This method is called when the user clicks the "OK"
	 * button in the add product view.
	 */
	@Override
	public void addProduct() {
		
		// Get values from view
		String barcodeStr = getView().getBarcode();
		String desc = getView().getDescription();
		SizeUnits sizeUnit = getView().getSizeUnit();
		String sizeValueStr = getView().getSizeValue();
		String shelfStr = getView().getShelfLife();
		String supplyStr = getView().getSupply();
		
		// Create Product Barcode
		ProductBarcode barcode = new ProductBarcode(barcodeStr);
		
		// Create size
		double amount = Double.parseDouble(sizeValueStr);
		SupplyType supplyType = translator.getSupplyTypeFromSizeUnit(sizeUnit);
		Size size = new Size(amount, supplyType);
		
		// Parse shelf life and 3-month-supply
		int shelfLife = Integer.parseInt(shelfStr);
		int supply = Integer.parseInt(supplyStr);
		
		// Create product and add to container
		Product product = new Product(Calendar.getInstance(), barcode,
				desc, size, shelfLife, supply);
		
		if(!Facade.canAddProductToContainer(product,  targetContainer))
		{
			getView().displayErrorMessage("Not valid product");
			return;
		}
		
		Facade.createProduct(product);
	}

	//I duplicated this code for EditProductController.  Should we make a base class?
	private boolean productValuesEmpty(String barcodeStr, String desc, SizeUnits sizeUnit,
			String sizeValueStr, String shelfStr, String supplyStr)
	{
		if(stringValueEmpty(barcodeStr) ||
				stringValueEmpty(desc) ||
				stringValueEmpty(sizeValueStr) ||
				stringValueEmpty(shelfStr) ||
				stringValueEmpty(supplyStr))
			return true;
		return sizeUnit == null;
	}
	
	private boolean stringValueEmpty(String str)
	{
		return (str == null || str.isEmpty());
	}
}

