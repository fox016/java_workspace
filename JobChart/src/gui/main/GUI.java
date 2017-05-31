
package gui.main;

import javax.swing.*;

import java.awt.event.*;

import gui.common.*;
import gui.chart.*;


@SuppressWarnings("serial")
public final class GUI extends JFrame implements IMainView {
	
	private IMainController _controller;
	private JMenuBar _menuBar;
	private FileMenu _fileMenu;
	private ChartView _chartView;

	private GUI(String[] args) {
		super("Job Chart");		
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				_fileMenu.exit();
			}			
		});	
		
		createMenus();
		createChartView();

		_controller = new MainController(this);

		_fileMenu.setController(_controller);

		display();
	}

	private void createMenus() {
		_fileMenu = new FileMenu(this);
		_fileMenu.setFont(View.createFont(_fileMenu.getFont(), View.MenuFontSize));

		_menuBar = new JMenuBar();
		_menuBar.setFont(View.createFont(_menuBar.getFont(), View.MenuFontSize));
		
		_menuBar.add(_fileMenu);

		setJMenuBar(_menuBar);
	}

	private void createChartView() {
		_chartView = new ChartView(this);
		setContentPane(_chartView);
	}

	private void display() {
        pack();
        setVisible(true);
	}
	
	public IMainController getController() {
		return _controller;
	}

    //
    // IView overrides
    //

	@Override
	public void displayInformationMessage(String message) {
		displayMessage(message, JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void displayWarningMessage(String message) {
		displayMessage(message, JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void displayErrorMessage(String message) {
		displayMessage(message, JOptionPane.ERROR_MESSAGE);
	}

	private void displayMessage(final String message, final int messageType) {

		// NOTE: Calling JOptionPane.showMessageDialog from an InputVerifier 
		// does not work (Swing's keyboard focus handling goes bonkers), so 
		// here we call JOptionPane.showMessageDialog using SwingUtilities.invokeLater 
		// to circumvent this problem in the case displayErrorMessage is
		// invoked from an InputVerifier.
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(GUI.this, message, "Job Chart", 
						messageType);
			}
		});
	}
	
	//
	// Main
	//
	
    public static void main(final String[] args) {
     	try {
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}

 		SwingUtilities.invokeLater(
			new Runnable() {
            	public void run() {
                	new GUI(args);
            	}
        	}
		);
    }

}

