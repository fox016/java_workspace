package NateSketch;

/**
 * @(#)NateSketchpad.java
 *
 * NateSketchpad application
 *
 * @author Nate Fox
 * @version 1.00 2010/11/24
 * @version 4.20 2013/08/01
 */

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.BorderFactory;

public class NateSketchpad extends JPanel implements ActionListener, MouseListener, MouseMotionListener, ItemListener
{
	private static final long serialVersionUID = -3619656884274327863L;

	private JFrame frame;

	// Panels
	private JPanel topPanel;
	private JPanel colorPanel;
	private JPanel toolPanel;
	private JPanel thicknessPanel;
	private JPanel axisPanel;
	private JPanel erasePanel;
	private JPanel undoPanel;

	// Buttons
	private JButton colorButton;
	private JButton backgroundButton;
	private JButton axesColorButton;
	private JButton eraseButton;
	private JButton clearButton;
	private JButton exitButton;
	private JButton undoButton;
	private JButton redoButton;
	private JRadioButton xButton;
	private JRadioButton yButton;
	private JRadioButton pencilButton;
	private JRadioButton brushButton;
	private JRadioButton conButton;
	private ButtonGroup toolGroup;

	// GUI Objects required for list of line thicknesses
	private JTextField thickField;
	private int thickness;

	// Color box: uses JColorChooser to help you select colors
	private ColorBox colorBox;
	private ColorBox backgroundBox;
	private ColorBox axisColorBox;
	private Color axisColor = Color.white;

	// Final GUI variables
	private final int size = 800;

	// Integers that record mouse movements in order to draw
	private int x1, y1, x2, y2;

	// Booleans that record axis information
	private boolean xAxis, yAxis;
	
	// List of all shapes drawn
	private ShapeStack shapes;
	private ShapeStack discardedShapes;
	private final int initCapacity = 512;
	
	private Thread redoThread;
	private Thread undoThread;
	private final int threadSleep = 50;

	// Constructor
	//   Instantiates all GUI Objects and initializes draw variables
	public NateSketchpad()
	{
		// Create main panel
		shapes = new ShapeStack(initCapacity);
		discardedShapes = new ShapeStack(initCapacity);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.black);

		// Create top panel
		topPanel = new JPanel();
		this.add(topPanel, BorderLayout.NORTH);

		// Create other panels within top panel
		createColorPanel();
		createToolPanel();
		createThicknessPanel();
		createAxisPanel();
		createUndoPanel();
		createErasePanel();

		// Create frame
		frame = new JFrame();
		frame.setSize(size, size);
		frame.setResizable(false);
		frame.setTitle("Fox Sketchpad");
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setLocation(dim.width/2 - size/2, 0);
    	frame.setVisible(true);

		// Add listeners
		colorButton.addActionListener(this);
		backgroundButton.addActionListener(this);
		axesColorButton.addActionListener(this);
		xButton.addItemListener(this);
		yButton.addItemListener(this);
		eraseButton.addActionListener(this);
		clearButton.addActionListener(this);
		undoButton.addActionListener(this);
		redoButton.addActionListener(this);
		undoButton.addMouseListener(this);
		redoButton.addMouseListener(this);
		undoButton.addMouseMotionListener(this);
		redoButton.addMouseMotionListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		// Alter cursor
    	Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    	this.setCursor(cursor);

		// Initialize draw variables
		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;

		// Initialize booleans to false
		axisColor = Color.white;
		xAxis = false;
		yAxis = false;
	}

	// Allows painting to occur
	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		
		// Paint or erase axes
		resetAxes(graphics);
		
		// Enable/disable undo/redo
		undoButton.setEnabled(!shapes.isEmpty());
		redoButton.setEnabled(!discardedShapes.isEmpty());
		
		// Repaint all shapes
		for(Shape shape : shapes)
			shape.draw(graphics);
	}
	
	// Paints or erases axes based on booleans xAxis/yAxis
	public void resetAxes(Graphics graphics)
	{
		if(graphics == null)
			graphics = this.getGraphics();
		graphics.setColor(xAxis? axisColor : this.getBackground());
		graphics.drawLine(0, size/2, size, size/2);
		graphics.setColor(yAxis? axisColor : this.getBackground());
		graphics.drawLine(size/2, 0, size/2, size);
	}
	
	public void setAxisColor(Color color)
	{
		this.axisColor = color;
	}

	// Responds to axis-related radio buttons
	public void itemStateChanged(ItemEvent event)
	{
		if(event.getSource() == xButton)
			xAxis = !xAxis;

		else if(event.getSource() == yButton)
			yAxis = !yAxis;
		
		repaint();
	}

	// Responds to all other buttons
	public void actionPerformed(ActionEvent event)
	{
		// Change background to current color when background button is clicked
		if(event.getSource() == backgroundButton)
		{
			if(backgroundBox != null)
				backgroundBox.close();
			backgroundBox = new ColorBox(new BackgroundChanger(this));
		}
		
		// Bring up color box when color button is clicked
		else if(event.getSource() == colorButton)
		{
			if(colorBox != null)
				colorBox.close();
			colorBox = new ColorBox(null);
		}
		
		else if(event.getSource() == axesColorButton)
		{
			if(axisColorBox != null)
				axisColorBox.close();
			axisColorBox = new ColorBox(new AxisChanger(this));
		}

		// Set color to background color when erase button is clicked
		else if(event.getSource() == eraseButton) {
			if(colorBox != null)
				colorBox.setColor(this.getBackground());
			else colorBox.setColor(Color.black);
		}

		// Clear when clear button is clicked
		else if(event.getSource() == clearButton)
		{
			discardedShapes.clear();
			while(!shapes.isEmpty())
			{
				discardedShapes.push(shapes.pop());
			}
			repaint();
		}

		// Exit when exit button is clicked
		else if(event.getSource() == exitButton)
			System.exit(0);
	}
	
	// Sets beginning coordinates of line to draw
	public void mousePressed(MouseEvent event)
	{
		if(event.getSource() == undoButton)
		{
			undoThread = new Thread()
			{
				@Override
				public void run()
				{
					while(!shapes.isEmpty())
					{
						try
						{
								sleep(threadSleep);
								discardedShapes.push(shapes.pop());
								repaint();
						}
						catch(InterruptedException e)
						{
							return;
						}
					}
				}
			};
			undoThread.start();
		}
		
		if(event.getSource() == redoButton)
		{
			redoThread = new Thread()
			{
				@Override
				public void run()
				{
					while(!discardedShapes.isEmpty())
					{
						try
						{
								sleep(threadSleep);
								shapes.push(discardedShapes.pop());
								repaint();
						}
						catch(InterruptedException e)
						{
							return;
						}
					}
				}
			};
			redoThread.start();
		}
		
		// When mouse is pressed, get x and y coordinates to start drawing
		if(!conButton.isSelected()) {
			x1 = event.getX();
			y1 = event.getY();
		}
	}

	// Continually updates coordinates of line to draw
	public void mouseDragged(MouseEvent event)
	{
		if(event.getSource() == undoButton)
		{
			if(shapes.isEmpty()) return;
			discardedShapes.push(shapes.pop());
			repaint();
			return;
		}
		if(event.getSource() == redoButton)
		{
			if(discardedShapes.isEmpty()) return;
			shapes.push(discardedShapes.pop());
			repaint();
			return;
		}
		
		// When mouse is dragged, get x and y coordinates to draw
		x2 = event.getX();
		y2 = event.getY();

		// Import graphics and set color
		Color color = Color.white;
		if(colorBox != null)
			color = colorBox.getColor();

		// If pencil tool, draw line
		if(pencilButton.isSelected() || conButton.isSelected())
		{
			// Draw line
			shapes.push(new Line(x1, y1, x2, y2, color));

			// Draw reflected shapes over axes
			if(yAxis)
				shapes.push(new Line(size-x1, y1, size-x2, y2, color));
			if(xAxis)
				shapes.push(new Line(x1, size-y1, x2, size-y2, color));
			if(xAxis && yAxis)
				shapes.push(new Line(size-x1, size-y1, size-x2, size-y2, color));
		}

		// If brush tool, draw thick lines (ovals)
		else
		{
			// Get thickness from text field
			thickness = Integer.parseInt(thickField.getText());
			int dim = thickness/2;

			// Draw line
			shapes.push(new Oval(x2-dim, y2-dim, thickness, color));
			shapes.push(new Oval(x1-dim, y1-dim, thickness, color));

			// Draw reflected lines over axes
			if(yAxis) {
				shapes.push(new Oval(size-x2-dim, y2-dim, thickness, color));
				shapes.push(new Oval(size-x1-dim, y1-dim, thickness, color));
			}
			if(xAxis) {
				shapes.push(new Oval(x2-dim, size-y2-dim, thickness, color));
				shapes.push(new Oval(x1-dim, size-y1-dim, thickness, color));
			}
			if(xAxis && yAxis) {
				shapes.push(new Oval(size-x2-dim, size-y2-dim, thickness, color));
				shapes.push(new Oval(size-x1-dim, size-y1-dim, thickness, color));
			}
		}

		// Update x and y coordinates
		x1=x2;
		y1=y2;
		repaint();
	}

	// Creates panel with color-related buttons, called in constructor
	private void createColorPanel()
	{
		// Create color panel
		colorPanel = new JPanel();
		colorPanel.setBorder(BorderFactory.createTitledBorder("Color"));
		colorButton = new JButton("Line"); // Create color button to go in panel
		backgroundButton = new JButton("Bkgrnd"); // Create background button to go in panel
		axesColorButton = new JButton("Axes"); // Create axes color button to go in panel
		colorPanel.add(colorButton);
		colorPanel.add(backgroundButton);
		colorPanel.add(axesColorButton);
		topPanel.add(colorPanel); // Add color panel to top panel
	}

	// Creates panel with tool-related buttons, called in constructor
	private void createToolPanel()
	{
		toolPanel = new JPanel();
		toolPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		toolPanel.setLayout(new GridLayout(3,1));
		pencilButton = new JRadioButton("Pencil"); // Create pencil button to go in panel
		brushButton = new JRadioButton("Brush"); // Create brush button to go in panel
		conButton = new JRadioButton("Connect"); // Create connect button to go in panel
		toolPanel.add(pencilButton);
		toolPanel.add(brushButton);
		toolPanel.add(conButton);

		// Create button group, set pencil to default
		toolGroup = new ButtonGroup();
		toolGroup.add(pencilButton);
		toolGroup.add(brushButton);
		toolGroup.add(conButton);
		pencilButton.setSelected(true);

		topPanel.add(toolPanel); // Add tool panel to top panel
	}

	// Creates panel with thickness-related objects, called in constructor
	private void createThicknessPanel()
	{
		// Create thickness panel
		thicknessPanel = new JPanel();
		thicknessPanel.setBorder(BorderFactory.createTitledBorder("Thick"));

		// Create text field to add to panel
		thickField = new JTextField(2);
		thickField.setText("1");
		thicknessPanel.add(thickField);
		topPanel.add(thicknessPanel); // Add thickness panel to top panel
	}

	// Creates panel with axis-related buttons, called in constructor
	private void createAxisPanel()
	{
		// Create axis panel
		axisPanel = new JPanel();
		axisPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		axisPanel.setLayout(new BorderLayout());
		xButton = new JRadioButton("X Axis"); // Create x button to go in panel
		yButton = new JRadioButton("Y Axis"); // Create y butotn to go in panel
		axisPanel.add(xButton, BorderLayout.NORTH);
		axisPanel.add(yButton, BorderLayout.SOUTH);
		topPanel.add(axisPanel); // Add axis panel to top panel
	}

	// Creates panel with eraser-related buttons, called in constructor
	private void createErasePanel()
	{
		// Create erase panel
		erasePanel = new JPanel();
		erasePanel.setBorder(BorderFactory.createTitledBorder("Erase"));
		eraseButton = new JButton("Erase"); // Create erase button to go in panel
		clearButton = new JButton("Clear"); // Create clear butotn to go in panel
		erasePanel.add(eraseButton);
		erasePanel.add(clearButton);
		topPanel.add(erasePanel); // Add erase panel to top panel
	}
	
	// Creates panel with undo/redo buttons, called in constructor
	private void createUndoPanel()
	{
		undoPanel = new JPanel();
		undoPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		undoPanel.setLayout(new BorderLayout());
		undoButton = new JButton("Undo");
		redoButton = new JButton("Redo");
		undoPanel.add(undoButton, BorderLayout.NORTH);
		undoPanel.add(redoButton, BorderLayout.SOUTH);
		topPanel.add(undoPanel);
	}

	// Unused methods necessary for interfaces
	public void mouseClicked(MouseEvent event) {
	}
	public void mouseReleased(MouseEvent event) {
		if(undoThread != null)
			undoThread.interrupt();
		if(redoThread != null)
			redoThread.interrupt();
	}
	public void mouseExited(MouseEvent event) {
	}
	public void mouseEntered(MouseEvent event) {
	}
	public void mouseMoved(MouseEvent event) {
	}

	// Main
	// No arguments required
    public static void main(String[] args)
    {
    	new NateSketchpad();
    }
}

