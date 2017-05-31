/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.commands;

import model.Facade;
import model.Item;
import model.ItemManager;

/**
 *
 * @author Talonos
 */
public class CmdRemoveItem implements Command
{
    private Item itemToRemove;

    public CmdRemoveItem(Item itemToRemove) 
    {
        this.itemToRemove = itemToRemove;
    }

    /**
     * Moves an item to the dead pile. This is not the same as eradicating it.
     */
    @Override
    public void doAction() 
    {
        Facade.removeItemFromStorage(itemToRemove);
    }

    /**
     * Moves an item from the dead pile to be alive again.
     */
    @Override
    public void undoAction() 
    {
        Facade.resurrectItemToStorage(itemToRemove);
    }
    
    public Item getItem()
    {
        return itemToRemove;
    }
    
}
