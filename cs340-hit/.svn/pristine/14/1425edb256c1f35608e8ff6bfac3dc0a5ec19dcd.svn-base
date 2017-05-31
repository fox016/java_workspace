/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.commands;

import model.Facade;
import model.Product;
import model.ProductBarcode;
import model.StorageUnit;

/**
 *
 * @author Talonos
 */
public class CmdAddProduct implements Command
{
    private StorageUnit targetUnit;
    private ProductBarcode product;

    public CmdAddProduct(ProductBarcode productBarcode, StorageUnit targetContainer) 
    {
        product = productBarcode;
        targetUnit = targetContainer;
    }

    /**
     * Adds a product to a storage unit. Note that this implies the storage unit didn't have
     * the product anywhere inside of it to begin with.
     */
    @Override
    public void doAction() 
    {
        Facade.addProductToStorageUnitByBarcode(product, targetUnit);
    }

    /**
     * Removes a product from a storage unit. Note that this is sufficient to undo the
     * above, because we assume that the storage unit never had the product in it to begin with.
     */
    @Override
    public void undoAction() 
    {
        Facade.removeProductFromStorageUnitByBarcode(product, targetUnit);
    }
    
}
