package mastermind;

/**
 * @(#)MasterMindAdv
 *
 * MasterMindAdv application
 *
 * @author Nate Fox
 * @version 1.00 2011/1/1
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

import java.util.Random;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class MasterMindAdv extends JPanel implements MouseListener, ActionListener {
	
	// Color arrays to hold color combinations
	private Color[] compColors; // Holds 5 random colors selected by computer at initiation
	private Color[] userColors; // Holds 5 colors chosen by user each round
	private Space[][] guesses;  // Holds past guesses made by user
	private Space[][] pegs;     // Holds past correction pegs
	private final Color[] colorList = {Color.red, Color.pink, Color.yellow, Color.green, Color.blue,
								Color.cyan, Color.gray, Color.black}; // Holds possible colors to use
	private final String[] colorNames = {"Red", "Pink", "Ylw", "Grn", "Blue", "Cyan", "Gray", "Blk"};

	// Arrays of spaces used to store colors
	private Space[] currentSpaces; // Stores positions and colors of user pegs
	private Space[] currentPegs;   // Stores positions and colors of correction pegs

	// Array sizes
	private final int combination = 5; // 5 pegs in every combination
	private final int turnCount = 13;  // Number of turns allowed user

	// Objects for gameplay
	private Color current; // Current Color selected by user
	private Random random; // Our number generator
	private int row = 0; // Row that user can place pegs
	private final int rowSpace = 25; // Space between rows/columns of guesses
	private final int diameter = 20; // Diameter of pegs
	private final int corDiameter = 10; // Diameter of correction pegs

	// GUI Objects
	private static final int WIDTH = 450; // Width of window
	private static final int HEIGHT = 520; // Height of window
	private JButton[] colorButtons;  // Buttons with colors to select
	private JButton clear;      // Button to clear selections
	private JButton submit;     // Button to submit answer to computer
	private JButton newGame;    // Button to start new game
	private JPanel lowPanel;    // Panel for all buttons
	private JPanel colorPanel;  // Panel for color buttons
	private JPanel clearPanel;  // Panel for clear button
	private JPanel submitPanel; // Panel for submit button

	// Drawing coordinates
	private int rowY = rowSpace + (rowSpace * (turnCount - 1)); // Y-Coordinate to start drawing
	private int rowX = WIDTH/2 - (rowSpace * combination)/2 + 1; // X-Coordinate to start drawing
	private int pegX = rowX - (rowSpace * 2) - (rowSpace/2); // X-Coordinate to start drawing correction pegs

	// Constructor
	public MasterMindAdv() {

		// Initialize arrays
		compColors = new Color[combination];
		userColors = new Color[combination];
		guesses = new Space[turnCount][combination];
		pegs = new Space[turnCount][combination];

		// Create main panel
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		addMouseListener(this);

		// Create low panel
		lowPanel = new JPanel();
		this.add(lowPanel,BorderLayout.SOUTH);
		lowPanel.setLayout(new BorderLayout());

		// Create color panel
		colorPanel = new JPanel();
		colorPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		colorPanel.setLayout(new GridLayout(1, colorList.length));
		colorButtons = new JButton[colorList.length];
		for(int x = 0; x < colorList.length; x++) {
			
			//ImageIcon icon = new ImageIcon(classpath + x + ".png");
			//JLabel label = new JLabel(icon);
			//colorButtons[x] = new JButton(x+"");
			colorButtons[x] = new JButton(colorNames[x]);
			//colorButtons[x].add(label);
			
			colorPanel.add(colorButtons[x]);
			colorButtons[x].addActionListener(this);
		}
		lowPanel.add(colorPanel, BorderLayout.NORTH);

		// Create clear panel
		clearPanel = new JPanel();
		clearPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		clear = new JButton("Clear");
		clear.addActionListener(this);
		clearPanel.add(clear);
		lowPanel.add(clearPanel, BorderLayout.CENTER);

		// Create submit panel
		submitPanel = new JPanel();
		submitPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		submit = new JButton("Submit Guess");
		submit.addActionListener(this);
		submitPanel.add(submit);
		newGame = new JButton("New Game");
		newGame.addActionListener(this);
		submitPanel.add(newGame);
		lowPanel.add(submitPanel, BorderLayout.SOUTH);

		// Select random 5-Color combo
		random = new Random();
		for(int x = 0; x < combination; x++)
			compColors[x] = colorList[random.nextInt(colorList.length)];

		// Set up row 1
		setUpNewRow();
	}

	// Paint
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		// Draw board
		// Height of board = rowSpace * turnCount
		// Width = rowSpace * combination
		// Starting point = WIDTH/2 - (rowSpace * combination)/2, rowSpace
		g.drawRect(WIDTH/2 - (rowSpace * combination)/2, rowSpace, rowSpace * combination, rowSpace * turnCount);

		// Draw all former guesses
		for(int x = 0; x < row; x++)
			for(int y = 0; y < combination; y++)
				guesses[x][y].draw(g);

		// Draw current guesses
		for(int x = 0; x < currentSpaces.length; x++)
			currentSpaces[x].draw(g);

		// Draw former pegs
		for(int x = 0; x < row; x++)
			for(int y = 0; y < combination; y++)
				pegs[x][y].draw(g);

		// Draw current pegs
		for(int x = 0; x < currentPegs.length; x++)
			currentPegs[x].draw(g);
	}

	// Responds to buttons pressed
	public void actionPerformed(ActionEvent event) {

		// If submit guess is cleared, save to array, place hint pegs, and move to next row
		if(event.getSource() == submit) {

			// Save to array
			for(int x = 0; x < userColors.length; x++) {
				userColors[x] = currentSpaces[x].getColor(); // Store in userColors array
				if(userColors[x] == null || userColors[x].equals(Color.WHITE))
				{
					JOptionPane.showMessageDialog(this, "The color in position " + (x+1) + " is blank");
					return;
				}
				guesses[row][x] = currentSpaces[x]; // Store in guesses array
				pegs[row][x] = currentPegs[x]; // Store pegs in pegs array
			}

			// Place hint pegs
			placeHintPegs();

			// Move to next row
			nextRow();
		}

		// If new game is clicked, start new game
		else if(event.getSource() == newGame)
		{
			SwingUtilities.getWindowAncestor(this).dispose();
			main(new String[0]);
		}

		// If clear is selected, change current color to white
		else if(event.getSource() == clear)
			current = Color.white;

		// If color button is selected, change current color
		else
			current = colorList[positionOf(colorButtons, event.getSource())];
	}

	// Responds to mouse clicks on canvas
	public void mousePressed(MouseEvent event) {

		// Get x-coordinate of mouse click
		int x = event.getX();

		// Test to see which Space Object the mouse click falls in
		Space temp;
		for(int i = 0; i < currentSpaces.length; i++) {
			temp = currentSpaces[i];
			if(temp != null)
				if(x > temp.getX() && x < temp.getX() + rowSpace)
					temp.setColor(current); // Set said Space Object's color to current color
		}

		// Refresh canvas
		repaint();
	}

	// Needed to satisfy interface
	public void mouseClicked(MouseEvent event) {
	}
	public void mouseReleased(MouseEvent event) {
	}
	public void mouseEntered(MouseEvent event) {
	}
	public void mouseExited(MouseEvent event) {
	}

	// Places black and gray pegs to let user know accuracy of guess
	private void placeHintPegs() {

		// Calculate number of black pegs
		int blackPeg = compareSamePos(userColors, compColors);

		for(int x = 0; x < blackPeg; x++)
			currentPegs[x].setColor(Color.black);

		// Test for win
		if(blackPeg == 5)
		{
			JOptionPane.showMessageDialog(this, "You Win!");
			disableAll();
		}

		// Calculate number of gray pegs
		int grayPeg = compareDifPos(userColors, compColors);

		for(int x = blackPeg; x < grayPeg+blackPeg; x++)
			currentPegs[x].setColor(Color.gray);

		// Paint to board
		repaint();

	}

	// Moves from one row to the next by modifying variables
	private void nextRow() {

		rowX = WIDTH/2 - (rowSpace * combination)/2 + 1; // Reset x-coordinate
		pegX = rowX - (rowSpace * 2) - (rowSpace/2); // Reset x-coordinate for pegs
		rowY -= rowSpace; // Decrement y-coordinate
		row++; //increment row

		// You lose if you get too high without winning
		if(row == turnCount) {
			disableAll();
			current = null;
		}

		setUpNewRow();
	}
	
	private void disableAll()
	{
		clear.setEnabled(false);
		submit.setEnabled(false);
		for(JButton button : colorButtons)
		{
			button.setEnabled(false);
		}
	}

	// Sets up new row by defining 5 Space objects for user and 5 Space objects for pegs
	private void setUpNewRow() {

		// Create 5 new spaces for user colors
		if(currentSpaces == null)
			currentSpaces = new Space[combination];
		for(int x = 0; x < combination; x++) {
			currentSpaces[x] = new Space(rowX, rowY, diameter);
			rowX += rowSpace;
		}

		// Create 5 new spaces for correction pegs
		if(currentPegs == null)
			currentPegs = new Space[combination];
		for(int x = 0; x < combination; x++) {
			currentPegs[x] = new Space(pegX, rowY + rowSpace/4, corDiameter);
			pegX += rowSpace/2;
		}
	}

	// Compares two arrays, returns number of Objects within both arrays in same positions
	private int compareSamePos(Color[] user, Color[] comp) {
		int samePosCount = 0;
		for(int x = 0; x < user.length; x++)
			if(user[x].equals(comp[x]))
				samePosCount++;
		return samePosCount;
	}

	// Compares two arrays, returns number of Objects within both arrays in different positions
	private int compareDifPos(Color[] user, Color[] comp) {

		int difPosCount = 0; // This will be returned.  Incremented as similarities are found

		// Temporary linked lists to hold color data
		LinkedList<Color> userTemp = new LinkedList<>();
		LinkedList<Color> compTemp = new LinkedList<>();

		// Transfer color data from arrays to linked lists
		for(int x = 0; x < user.length; x++) {
			userTemp.add(user[x]);
			compTemp.add(comp[x]);
		}

		// Remove colors found in same positions
		for(int x = 0; x < user.length; x++) {
			if(user[x].equals(comp[x])) {
				userTemp.remove(user[x]);
				compTemp.remove(comp[x]);
			}
		}

		// Count similarities in two arrays
		for(int x = 0; x < userTemp.size(); x++) {
			if(compTemp.contains(userTemp.get(x))) {
				difPosCount++;
				compTemp.remove(userTemp.get(x));
			}
		}

		return difPosCount;
	}

	// Parameters
	//  buttonArray = array of JButton Objects
	//  button = Object in array
	//  returns position of button in buttonArray
	//  returns -1 if button not found in buttonArray
	private int positionOf(JButton[] buttonArray, Object button) {
		for(int x = 0; x < buttonArray.length; x++) {
			if(buttonArray[x].equals((JButton)button))
				return x;
		}
		return -1;
	}

	// main
	// no parameters required
	public static void main(String[] args) {

		MasterMindAdv mma = new MasterMindAdv();

		// Create frame
		JFrame frame = new JFrame("MasterMind 4.2");
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mma);

		// Set frame location and set visible
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	frame.setLocation(dim.width/2 - WIDTH/2, dim.height/2 - HEIGHT/2);
    	frame.setVisible(true);
	}
}
