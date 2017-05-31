package common.commands;

import java.util.List;

/**
 * Represents a command that is the sum result of multiple commands. Used to
 * wrap many different command objects into one so you can "undo" or "redo" the
 * whole thing at once as though it were a single command.
 * 
 * @author Talonos
 */
public class CmdMultiCommand implements Command
{
    /**
     * The commands contained in this multi-command object.
     */
    private List<Command> commands;

    public CmdMultiCommand(List<Command> listOfCommands) 
    {
        commands = listOfCommands;
    }

    /**
     * Does the action of all the commands at once.
     */
    @Override
    public void doAction() 
    {
        for (int x = 0; x < commands.size(); x++)
        {
            commands.get(x).doAction();
        }
    }

    /**
     * Undoes the action of each command, in reverse order.
     */
    @Override
    public void undoAction() throws IllegalArgumentException
    {
        for (int x = commands.size()-1; x >= 0; x--)
        {
            commands.get(x).undoAction();
        }
    }

    public List<Command> getCommands() 
    {
        return commands;
    }
    
}
