/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.commands;

import model.Facade;
import model.Item;
import model.StorageUnit;

/**
 *
 * @author Talonos
 */
public class CmdAddItem implements Command
{
    private Item i;

    public CmdAddItem(Item i) 
    {
        this.i=i;
    }

    /**
     * Adds an item to the system. This should not be called the first time an item 
     * is added! That should be handled by whatever class makes the item. Instead, 
     * when called subsequent times, it re-adds a previously eradicated object.
     */
    @Override
    public void doAction() 
    {
        Facade.uneradicateItem(i);
    }

    /**
     * Eradicated an item from the system. Note that this is not the same as removing
     * it! An eradicated item is not moved to the "dead items" pile. All trace of its
     * existence is removed from the universe.. If items had souls, this would destroy it. 
     * That should make you think twice about undoing an added item. You monster.
     */
    @Override
    public void undoAction() 
    {
        Facade.eradicateItem(i);
    }

    public Item getItem() 
    {
        return i;
    }
    
}
