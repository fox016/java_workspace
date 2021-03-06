package gui.main;

import gui.common.View;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class FileMenu extends JMenu
{
	private IMainController _controller;
	private GUI _parent;
	private JMenuItem _exitMenuItem;

	public FileMenu(GUI parent) {
		
		super("File");
		
		this._parent = parent;

		this.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent e) {
				return;
			}
			
			public void menuDeselected(MenuEvent e) {
				return;
			}
			
			public void menuSelected(MenuEvent e) {
				enableMenuItems();
			}			
		});

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (evt.getSource() == _exitMenuItem) {
					exit();
				}
			}
		};
		
		_exitMenuItem = new JMenuItem("Exit");
		_exitMenuItem.setFont(View.createFont(_exitMenuItem.getFont(), View.MenuFontSize));
		_exitMenuItem.addActionListener(actionListener);
		add(_exitMenuItem);
	}
	
	public void setController(IMainController value) {
		_controller = value;
	}

	private void enableMenuItems() {
		_exitMenuItem.setEnabled(_controller.canExit());
	}
	
	void exit() {
		_controller.exit();
		_parent.dispose();
		System.exit(0);
	}
}
