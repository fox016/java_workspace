/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.commands;

/**
 *
 * @author Talonos
 */
public interface Command 
{
    /**
     * Does the action this command intends to do. The actual action differs on a
     * per command type basis. See the specific implementation for details.
     * 
     * Note that this does not have to have the same behavior each time. For example,
     * an "add item" command can either create and add a new item (if done the first time)
     * or re-add a previously eradicate item (if done subsequent times.) Again, see
     * the specific implementation for details.
     */
    public void doAction();
    
    /**
     * Undoes the action this command intends to do. The actual action differs on a
     * per command type basis. See the specific implementation for details.
     */
    public void undoAction();
}
