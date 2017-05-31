package NateSketch;

/**
 * @(#)ColorBox.java
 *
 * ColorBox
 *
 * @author Nate Fox
 * @version 1.00 2010/11/24
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class ColorBox extends JPanel implements ChangeListener {
	
	private static final long serialVersionUID = 2182451463148676623L;
	
	private JColorChooser colorChooser;
    private JFrame frame;
    private Color color;
    
    private Command cmd;
    
    public ColorBox(Command cmd)
    {
        super(new BorderLayout());
        
        this.cmd = cmd;

        //Set up color chooser for setting color
        color = Color.white;
        colorChooser = new JColorChooser(color);
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setBorder(BorderFactory.createTitledBorder("Choose Color"));

        add(colorChooser, BorderLayout.PAGE_END);

        // Create frame
        frame = new JFrame("Color Box");
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void stateChanged(ChangeEvent e) {
        color = colorChooser.getColor();
        if(cmd != null)
        {
        	cmd.setColor(color);
        	cmd.run();
        }
    }

    public Color getColor() {
    	return color;
    }

    public void setColor(Color color) {
    	this.color = color;
    }
    
    public void close() {
    	frame.setVisible(false);
    	frame.dispose();
    }
}

