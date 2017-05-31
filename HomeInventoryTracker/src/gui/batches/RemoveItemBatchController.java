package gui.batches;

import common.commands.CmdRemoveItem;
import common.commands.CommandStack;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import model.Facade;
import model.Item;
import model.ItemBarcode;
import model.ItemManager;
import model.Product;
import model.ProductBarcode;
import gui.common.*;
import gui.item.ItemData;
import gui.product.*;
import java.util.Arrays;

/**
 * Controller class for the remove item batch view.
 */
public class RemoveItemBatchController extends Controller implements
		IRemoveItemBatchController {
	
        private CommandStack stack = new CommandStack();
	private boolean useScanner;
	private Thread timer;
	private Set<ProductBarcode> productBarcodes;
	private Map<Product, Set<Item>> deadBatchItems;
	//For some reason, useScannerChanged() gets called sometimes even when 
	//it is not selected by a user.  For this reason, we keep a boolean of its value
	//to determine whether it truly changed by a user (and thus set the barcode to
	//the empty string
	private boolean curScanValue = true;
	
	private Translator translator;
	
	/**
	 * Constructor.
	 * 
	 * @param view Reference to the remove item batch view.
	 */
	public RemoveItemBatchController(IView view) 
        {
		super(view);

		construct();
		translator = new Translator();
		productBarcodes = new HashSet<ProductBarcode>();
		deadBatchItems = new HashMap<Product, Set<Item>>();
		useScanner = true;
		getView().enableItemAction(false);
		getView().setUseScanner(useScanner);
	}
	
	/**
	 * Returns a reference to the view for this controller.
	 */
	@Override
	protected IRemoveItemBatchView getView() 
        {
		return (IRemoveItemBatchView)super.getView();
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
		getView().setBarcode("");
		this.barcodeChanged();
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
		getView().enableItemAction(isValidBarcode() && !useScanner);
		getView().enableRedo(stack.canRedo());
		getView().enableUndo(stack.canUndo());
	}

	/**
	 * This method is called when the "Item Barcode" field is changed
	 * in the remove item batch view by the user.
	 */
	@Override
	public void barcodeChanged() {
		enableComponents();
		if(timer != null)
			timer.interrupt();
		timer = null;
		
		if(useScanner)
		{
			getView().enableItemAction(false);
			if(!getView().getBarcode().isEmpty())
			{
				timer = new Thread()
				{
					@Override
					public void run()
					{
						try
						{
							sleep(1000);
						}
						catch(InterruptedException e)
						{
							return;
						}
						removeItem();
					}
				};
				timer.start();
			}
		}		
	}
	
	/**
	 * In this context, a valid barcode is a non-null, nonempty string.
	 * Nonexistant barcodes will bring up an error dialogue box.
	 * @return if the current view barcode is valid
	 */
	private boolean isValidBarcode()
        {
		String barcode = getView().getBarcode();
		return barcode != null && !barcode.isEmpty();
	}
	
	/**
	 * This method is called when the "Use Barcode Scanner" setting is changed
	 * in the remove item batch view by the user.
	 */
	@Override
	public void useScannerChanged()
	{
		//For some reason, useScannerChanged() gets called sometimes even
		//when the use does not select it.  This is why we use a second boolean
		//to determine whether or not to set the barcode to the empty string.
		curScanValue = getView().getUseScanner();
		if(curScanValue != useScanner){
			getView().setBarcode("");
		}
		useScanner = getView().getUseScanner();
		enableComponents();
	}
	
	/**
	 * This method is called when the selected product changes
	 * in the remove item batch view.
	 */
	@Override
	public void selectedProductChanged() 
        {
		ProductData productData = getView().getSelectedProduct();
		Product selectedProduct = translator.getProductFromData(productData);
		if(selectedProduct == null)
			return;
		
		Set<Item> itemsOfProduct = deadBatchItems.get(selectedProduct);
		ItemData[] itemData = translator.getDataFromItems(itemsOfProduct);
		if(itemsOfProduct == null)
			return;
		
		getView().setItems(itemData);
	}
	
	/**
	 * This method is called when the user clicks the "Remove Item" button
	 * in the remove item batch view.
	 */
	@Override
	public void removeItem() 
        {
		
		String barcodeStr = getView().getBarcode();
		try 
                {
			ItemBarcode itemBarcode = new ItemBarcode(barcodeStr);
			if(!ItemManager.instance().contains(itemBarcode))
                        {
				getView().displayErrorMessage("The specified item does not exist");
				return;
			}
			Item itemToRemove = ItemManager.instance().getItem(itemBarcode);
			if(ItemManager.instance().isRemoved(itemToRemove.getItemBarcode()))
                        {
				getView().displayErrorMessage("That item has already been removed");
				return;
			}
                        
                        //At this point, we know the item is valid to remove, so
                        //we wrap it in a command and tell the command to execute.

                        CmdRemoveItem command = new CmdRemoveItem(itemToRemove);
                        command.doAction();
                        stack.push(command);
                        
			resetBarcode();
			
                        updateMaps(itemToRemove);
			
		} 
                catch (IllegalArgumentException e) 
                {
			getView().displayErrorMessage("The specified item does not exist");
			resetBarcode();
			return;
		}			
	}
	
	/* This function appears to be as dead as the item it was trying to add.
	private void addDeadItem(){
		
	}
	*/
	
	private void setCountsForData(ProductData[] pData)
        {
		for(ProductData data : pData)
                {
			Product prod = translator.getProductFromData(data);
			Set<Item> itemsOfProd = deadBatchItems.get(prod);
			int removedCount = 0;
			if(itemsOfProd != null)
				removedCount = itemsOfProd.size();
			data.setCount(String.valueOf(removedCount));
		}
		
	}
	
	public void resetBarcode(){
		getView().setBarcode("");
		barcodeChanged();
	}
	
	/**
	 * This method is called when the user clicks the "Redo" button
	 * in the remove item batch view.
	 */
	@Override
	public void redo() 
        {
            if (stack.canRedo())
            {
                Item item = ((CmdRemoveItem)stack.redo()).getItem();
                updateMaps(item);
                enableComponents();
            }
            else
            {
                this.getView().displayErrorMessage("Nothing to redo!");
            }
	}

	/**
	 * This method is called when the user clicks the "Undo" button
	 * in the remove item batch view.
	 */
	@Override
	public void undo() 
        {
            if (stack.canUndo())
            {
                Item item = ((CmdRemoveItem)stack.undo()).getItem();
                updateMaps(item);
                enableComponents();
            }
            else
            {
                this.getView().displayErrorMessage("Nothing to undo!");
            }
	}

	/**
	 * This method is called when the user clicks the "Done" button
	 * in the remove item batch view.
	 */
	@Override
	public void done() 
        {
		getView().close();
	}

    //Just so you know, this would have been way easier if the view observed the
    //facade!
    private void updateMaps(Item itemToRemove) 
    {
	ProductData selection = getView().getSelectedProduct();
        if (selection != null)
        {
            System.out.println("Selection: "+selection.getDescription());
        }
        else
    {
        System.out.println("NULL!");
    }
        
        if (ItemManager.instance().isRemoved(itemToRemove.getItemBarcode()))
        { //Implies that the item is being deleted.
            //Add deadItem to the Map
            if(deadBatchItems.containsKey(itemToRemove.getProduct()))
            {
                Set<Item> items = deadBatchItems.get(itemToRemove.getProduct());
                items.add(itemToRemove);
            }
            else
            {   //If there is no map to add it to, make one.
                Set<Item> items = new HashSet<Item>();
                items.add(itemToRemove);	
                deadBatchItems.put(itemToRemove.getProduct(), items);
            }
            
            //update view to contain the items' product
            ProductBarcode prodBarcodeOfItem = itemToRemove.getProductBarcode();
            productBarcodes.add(prodBarcodeOfItem);
        }
        else
        {  //Implies that the item is being brought back from the grave.
            //Remove the item.
            deadBatchItems.get(itemToRemove.getProduct()).remove(itemToRemove);
            
            if (deadBatchItems.get(itemToRemove.getProduct()).isEmpty())
            {
                deadBatchItems.remove(itemToRemove.getProduct());
                productBarcodes.remove(itemToRemove.getProductBarcode());
            }
        }
        //Update the view.
        ProductData[] pData = translator.getDataFromProductBarcodes(productBarcodes);
        setCountsForData(pData);
        getView().setProducts(pData);
        
        if (Arrays.asList(pData).contains(selection))
        {
            System.out.println("I am here!");
            getView().selectProduct(selection);
            this.selectedProductChanged();
        }
        else
        {
            System.out.println("I am here now!");
            getView().selectProduct(null);
        }
    }
}

