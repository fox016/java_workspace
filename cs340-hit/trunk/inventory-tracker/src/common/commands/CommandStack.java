/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.commands;

import java.util.Stack;

/**
 *
 * @author Talonos
 */
public class CommandStack
{
    private Stack<Command> commands = new Stack<>();
    
    private Stack<Command> undoneCommands = new Stack<>();
    
    public void push(Command c)
    {
        undoneCommands = new Stack<>();
        commands.push(c);
    }
    
    public Command undo() throws IllegalArgumentException
    {
        Command c = commands.pop();
        c.undoAction();
        undoneCommands.push(c);
        return c;
    }
    
    public Command redo()
    {
        Command c = undoneCommands.pop();
        c.doAction();
        commands.push(c);
        return c;
    }

    public boolean canUndo() 
    {   //You can undo if the stack is not empty.
        return (!commands.empty());
    }
    
    public boolean canRedo() 
    {   //You can redo if the stack is not empty.
        return (!undoneCommands.empty());
    }
}
