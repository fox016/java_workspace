/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.commands;

import model.Facade;
import model.Product;
import model.ProductContainer;

/**
 *
 * @author Talonos
 */
public class CmdCreateProduct implements Command
{
    
    Product product;

    public CmdCreateProduct(Product product) 
    {
        this.product = product;
    }

    /**
     * Adds a product to the system.
     */
    @Override
    public void doAction() 
    {
        Facade.createProduct(product);
    }

    /**
     * Removes a product from the system.
     */
    @Override
    public void undoAction() throws IllegalArgumentException
    {
        Facade.deleteProduct(product);
    }

    public Product getProduct() 
    {
        return product;
    }
    
}
