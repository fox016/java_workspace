package gui.batches;

import common.commands.*;
import gui.common.*;
import gui.inventory.*;
import gui.item.ItemData;
import gui.product.*;
import java.util.*;
import model.*;

/**
 * Controller class for the transfer item batch view.
 */
public class TransferItemBatchController extends Controller implements
		ITransferItemBatchController {
	
	private boolean useScanner;
        private ProductContainerData targetData;
        private StorageUnit targetContainer;
        //private List<Item> allBatchItems;
        private Thread timer;
        private CommandStack stack = new CommandStack();
        
        private Translator translator;
        
        private Map<ProductBarcode,Collection<Item>> transferredStuff = new HashMap<>();
	
	/**
	 * Constructor.
	 * 
	 * @param view Reference to the transfer item batch view.
	 * @param target Reference to the storage unit to which items are being transferred.
	 */
	public TransferItemBatchController(IView view, ProductContainerData target)
	{
		super(view);
		targetData = target;
		
		translator = new Translator();
                
		construct();
                
	}
	
	/**
	 * Returns a reference to the view for this controller.
	 */
	@Override
	protected ITransferItemBatchView getView() 
        {
		return (ITransferItemBatchView)super.getView();
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
		targetContainer = translator.getProductContainerFromData(targetData).getRoot();
		
		//allBatchItems = new ArrayList<>();
                getView().setUseScanner(true);
                useScanner = getView().getUseScanner();
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
            getView().enableRedo(stack.canRedo());
	    getView().enableUndo(stack.canUndo());
            if (!getView().getBarcode().equals(""))
            {
                getView().enableItemAction(true);
            }
            else
            {
                getView().enableItemAction(false);
            }
                
	}

	/**
	 * This method is called when the "Item Barcode" field in the
	 * transfer item batch view is changed by the user.
	 */
	@Override
	public void barcodeChanged() 
        {
            enableComponents();
            
            if(timer != null)
                timer.interrupt();
            timer = null;
		
            // If using the scanner, start timer
            if(useScanner)
            {
                if(!getView().getBarcode().isEmpty())
                {
                    timer = new Thread(){
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
                            transferItem();
                        }
                    };
                    timer.start();
                }
            }
	}
	
	/**
	 * This method is called when the "Use Barcode Scanner" setting in the
	 * transfer item batch view is changed by the user.
	 */
	@Override
	public void useScannerChanged() 
        {
            useScanner = getView().getUseScanner();
	}
	
	/**
	 * This method is called when the selected product changes
	 * in the transfer item batch view.
	 */
	@Override
	public void selectedProductChanged() 
        {
            updateItemPane();
	}
	
	/**
	 * This method is called when the user clicks the "Transfer Item" button
	 * in the transfer item batch view.
	 */
	@Override
	public void transferItem() 
        {
            if (!validate())
            {
                getView().displayErrorMessage("Invalid barcode: Does not exist!");
                return;
            }
            
            if (transferredAlready())
            {
                getView().setBarcode("");
                enableComponents();
                return;
            }
            
            //The command we will create.
            Command c = null;
            
            Item itemToTransfer = ItemManager.instance().getItem(
                    new ItemBarcode(getView().getBarcode()));
            if (!targetContainer.hasProduct(itemToTransfer.getProductBarcode()))
            {
                //We need two commands wrapped in one.
                List<Command> listOfCommands = new ArrayList<>();
                listOfCommands.add(new CmdAddProduct(itemToTransfer.getProductBarcode(), 
                                                     targetContainer));
                listOfCommands.add(new CmdTransferItem(itemToTransfer, targetContainer, 
                                                       itemToTransfer.getStorageUnit()));
                c = new CmdMultiCommand(listOfCommands);
            }
            else
            {
                //We just need a single command for this.
                c = new CmdTransferItem(itemToTransfer, targetContainer, itemToTransfer.getStorageUnit());
            }
            
            //Do the thing we said we would do.
            c.doAction();
            stack.push(c);
        
            ProductBarcode productBarcode = itemToTransfer.getProductBarcode();
            Product product = ProductManager.instance().getProductByBarcode(productBarcode);
		
	    // If product doesn't yet exist, throw an error. Needless to say, this
            // should never be the case. If it is, something seriously messed up happened.
	    if(product == null)
	    {
	        throw new IllegalArgumentException("This item appears to lack a product!");
            }
            
            //Put it in our set.
            if (!transferredStuff.containsKey(productBarcode))
            {
                transferredStuff.put(productBarcode, new HashSet<Item>());
            }
            transferredStuff.get(productBarcode).add(itemToTransfer);
            
            updateProductPane(productBarcode);
            
            updateItemPane();
            

            getView().setBarcode("");
            
            enableComponents();
	}
	
	/**
	 * This method is called when the user clicks the "Redo" button
	 * in the transfer item batch view.
	 */
	@Override
	public void redo() 
        {
            Command c = stack.redo();
            enableComponents();
            handleViewRefreshRedo(c);
	}

	/**
	 * This method is called when the user clicks the "Undo" button
	 * in the transfer item batch view.
	 */
	@Override
	public void undo() 
        {
            Command c = stack.undo();
            enableComponents();
            handleViewRefreshUndo(c);
	}

	/**
	 * This method is called when the user clicks the "Done" button
	 * in the transfer item batch view.
	 */
	@Override
	public void done() 
        {
		getView().close();
	}

    private boolean validate() 
    {
        try
        {
            //Note: There should have been a function on the facade for this, I think.
            //We might need to change this later.
            if(!ItemManager.instance().contains(new ItemBarcode(getView().getBarcode())))
            {
                return false;
            }
            return true;
        }
        catch (IllegalArgumentException e)
        {
            //Implies it's not valid.
            return false;
        }
    }

    /**
     * 
     * @param productBarcode the product to select after you are done updating. Null deselects.
     */
    private void updateProductPane(ProductBarcode productBarcode) 
    {   
        ProductData[] productDatas = new ProductData[transferredStuff.keySet().size()];
        int iterator = 0;
        ProductData productToSelect = null;
        for (ProductBarcode tCode : transferredStuff.keySet())
        {
            //Create a productData.
            ProductData toStore = translator.getDataFromProduct(
                    ProductManager.instance().getProductByBarcode(tCode));
            //If this is the one to select, then save it elsewhere for future reference.
            if (tCode.equals(productBarcode))
            {
                productToSelect = toStore;
            }
            //Set the count on this data.
            toStore.setCount(transferredStuff.get(tCode).size()+"");
            //Put it in our array.
            productDatas[iterator++] = toStore;
        }
        getView().setProducts(productDatas);
        getView().selectProduct(productToSelect);
    }

    private void updateItemPane() 
    {
        if (getView().getSelectedProduct()==null)
        {   //If nothing is selected, set the items table to be empty.
            getView().setItems(new ItemData[0]);
            return;
        }
        Collection<Item> items = transferredStuff.get(
                new ProductBarcode(getView().getSelectedProduct().getBarcode()));
        ItemData[] itemDatas = new ItemData[items.size()];
        int iterator = 0;
        for (Item tItem : items)
        {
            itemDatas[iterator++] = translator.getDataFromItem(tItem);
        }
        getView().setItems(itemDatas);
    }

    private void handleViewRefreshRedo(Command c) 
    {
        //What product did we have selected? Save it for later.
        Product selection = null;
        if (getView().getSelectedProduct()!= null)
        {
            selection = translator.getProductFromData(getView().getSelectedProduct());
        }
        
        //We know that one way or another, we are adding an item to our set. First,
        //let's get that item so we know what it is.
        Item i;
        if (c instanceof CmdTransferItem)
        {
            i = ((CmdTransferItem)c).getItem();
        }
        else
        {   //Ugly casting ahead...
            //This assumes the command is a Multi-command with an item addition in its
            //second slot. That's a reasonable assumption, as long as the transferItem
            //command isn't changed.
            i = ((CmdTransferItem)((CmdMultiCommand)c).getCommands().get(1)).getItem();
        }
        //With our item safely in hand, we can increment its count in the map.
        if (!transferredStuff.containsKey(i.getProductBarcode()))
        {
            transferredStuff.put(i.getProductBarcode(), new HashSet<Item>());
        }
        transferredStuff.get(i.getProductBarcode()).add(i);
        //Update our views.
        updateProductPane((selection==null?null:selection.getBarcode()));
        updateItemPane();
    }

    private void handleViewRefreshUndo(Command c) 
    {
        //What product did we have selected? Save it for later.
        Product selection = null;
        if (getView().getSelectedProduct()!= null)
        {
            selection = translator.getProductFromData(getView().getSelectedProduct());
        }
        
        //We know that one way or another, we are removing an item from our set. First,
        //let's get that item so we know what it is.
        Item i;
        if (c instanceof CmdTransferItem)
        {
            i = ((CmdTransferItem)c).getItem();
        }
        else
        {   //Ugly casting ahead...
            //This assumes the command is a Multi-command with an item addition in its
            //second slot. That's a reasonable assumption, as long as the transferItem
            //command isn't changed.
            i = ((CmdTransferItem)((CmdMultiCommand)c).getCommands().get(1)).getItem();
        }
        //With our item safely in hand, we can remove it from the map.
        Collection<Item> listOfItems = transferredStuff.get(i.getProductBarcode());
        listOfItems.remove(i);
        if(listOfItems.isEmpty()) //If that's the last item of that type, remove it as well.
        {
            transferredStuff.remove(i.getProductBarcode());
            if (selection != null && selection.equals(i.getProduct())) 
            {                      //If we removed the last item of the selected product...
                selection = null;  //then we should set it to null to deselect it.
            }
        }
        //Update our views.
        updateProductPane((selection==null?null:selection.getBarcode()));
        updateItemPane();
    }

    private boolean transferredAlready() 
    {
        String code = getView().getBarcode();
        ItemBarcode itemCode = new ItemBarcode(code);
        for (Collection<Item> tList : transferredStuff.values())
        {
            for (Item tItem : tList)
            {
                if (tItem.getItemBarcode().equals(itemCode))
                {
                    return true;
                }
            }
        }
        return false;
    }
}

