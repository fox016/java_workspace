package minesweeper;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class NewGameFrame extends JFrame implements ActionListener
{
	private JPanel mainPanel;
	private JPanel presetPanel;
	private JPanel inputPanel;
	private JPanel goPanel;

	private JButton beginnerBtn;
	private JButton intermediateBtn;
	private JButton advancedBtn;
	private JButton playBtn;

	private JLabel rowLabel;
	private JLabel colLabel;
	private JLabel bombLabel;

	private JTextField rowField;
	private JTextField colField;
	private JTextField bombField;

	/**
	 * Constructor
	 */
	public NewGameFrame()
	{
		initPresetPanel();
		initInputPanel();
		inputGoPanel();
		initMainPanel();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 300;
		int height = 170;

		this.setTitle("New Game");
		this.setSize(width, height);
		this.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.add(mainPanel);
		this.setVisible(true);
	}

	/**
	 * Init main panel elements
	 */
	private void initMainPanel()
	{
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3, 1));
		mainPanel.add(presetPanel);
		mainPanel.add(inputPanel);
		mainPanel.add(goPanel);
	}

	/**
	 * Init go panel elements
	 */
	private void inputGoPanel()
	{
		goPanel = new JPanel();
		playBtn = new JButton("Play");
		goPanel.add(playBtn);
		playBtn.addActionListener(this);
	}

	/**
	 * Init input panel elements
	 */
	private void initInputPanel()
	{
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3, 2));
		rowLabel = new JLabel("Rows:");
		colLabel = new JLabel("Cols:");
		bombLabel = new JLabel("Bombs:");
		rowField = new JTextField();
		colField = new JTextField();
		bombField = new JTextField();
		inputPanel.add(rowLabel);
		inputPanel.add(rowField);
		inputPanel.add(colLabel);
		inputPanel.add(colField);
		inputPanel.add(bombLabel);
		inputPanel.add(bombField);
	}

	/**
	 * Init preset panel elements
	 */
	private void initPresetPanel()
	{
		presetPanel = new JPanel();
		presetPanel.setLayout(new GridLayout(1, 3));
		beginnerBtn = new JButton("Beginner");
		intermediateBtn = new JButton("Intermediate");
		advancedBtn = new JButton("Advanced");
		presetPanel.add(beginnerBtn);
		presetPanel.add(intermediateBtn);
		presetPanel.add(advancedBtn);
		beginnerBtn.addActionListener(this);
		intermediateBtn.addActionListener(this);
		advancedBtn.addActionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == beginnerBtn)
			setFields(9, 9, 10);
		else if(e.getSource() == intermediateBtn)
			setFields(16, 16, 40);
		else if(e.getSource() == advancedBtn)
			setFields(16, 30, 99);
		else
			startGame();
	}

	/**
	 * Set GUI text fields to show these values
	 * 
	 * @param rows
	 * @param cols
	 * @param bombs
	 */
	public void setFields(int rows, int cols, int bombs)
	{
		rowField.setText(rows + "");
		colField.setText(cols + "");
		bombField.setText(bombs + "");
	}

	/**
	 * Get number from GUI text field
	 * 
	 * @param field
	 * @throws IllegalArgumentException
	 *             if text field value is not valid integer
	 * @return
	 */
	private int getNumberFromField(JTextField field)
	{
		try
		{
			return Integer.parseInt(field.getText());
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"All input must be integers. Illegal value: "
							+ field.getText());
		}
	}

	/**
	 * Starts game using input from GUI text fields
	 */
	private void startGame()
	{
		try
		{
			new MinesweeperGUI(
					new Board(getNumberFromField(rowField),
							getNumberFromField(colField),
							getNumberFromField(bombField)));
			this.dispose();
		} catch (IllegalArgumentException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
}
