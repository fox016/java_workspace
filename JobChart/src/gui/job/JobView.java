package gui.job;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import common.util.DateUtils;

import model.TimeRequired;

import gui.common.*;
import gui.main.*;

@SuppressWarnings("serial")
public abstract class JobView extends DialogView {

	private JPanel _valuesPanel;
	private JLabel _descriptionLabel;
	private JTextField _descriptionField;
	private JLabel _timeLabel;
	private JComboBox<TimeRequired> _timeBox;
	private JLabel _lastDateLabel;
	private SpinnerModel _lastDateSpinnerModel;
	private JSpinner.DateEditor _lastDateSpinnerEditor;
	private JSpinner _lastDateSpinner;
	private JButton _setTodayButton;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;
	
	public JobView(GUI parent, DialogBox dialog) {
		super(parent, dialog);
	}
	
	@Override
	protected void createComponents() {
		createValuesPanel();
		createButtonsPanel();
	}
	
	private void createValuesPanel() {
		KeyListener keyListener = new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				return;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				valuesChanged();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				return;
			}
		};

		Date initDate = DateUtils.currentDate();
		Date latestDate = initDate;
		Date earliestDate = DateUtils.earliestDate();
		
		_valuesPanel = new JPanel();
		
		_descriptionLabel = new JLabel("Job Description:");
		_descriptionField = new JTextField(30);
		_descriptionField.addKeyListener(keyListener);
		
		_timeLabel = new JLabel("Time Required:");
		_timeBox = new JComboBox<>();
		_timeBox.addItem(TimeRequired.SHORT);
		_timeBox.addItem(TimeRequired.MEDIUM);
		_timeBox.addItem(TimeRequired.LONG);
		_timeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (eventsAreDisabled()) {
					return;
				}
				valuesChanged();
			}
		});
		
		_lastDateLabel = new JLabel("Last Completed:");
		_lastDateSpinnerModel = new SpinnerDateModel(initDate, earliestDate,
				latestDate, Calendar.YEAR);
		_lastDateSpinner = new JSpinner(_lastDateSpinnerModel);
		_lastDateSpinnerEditor = new JSpinner.DateEditor(_lastDateSpinner,
				DateUtils.DATE_FORMAT);
		_lastDateSpinner.setEditor(_lastDateSpinnerEditor);
		_lastDateSpinnerEditor.getTextField().getDocument()
				.addDocumentListener(new DocumentListener() {

					@Override
					public void changedUpdate(DocumentEvent e) {
						return;
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						processChange(e);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						processChange(e);
					}

					private void processChange(DocumentEvent e) {
						if (eventsAreDisabled()) {
							return;
						}
						if (_lastDateSpinnerEditor.getTextField().hasFocus()) {
							valuesChanged();
						}
					}
				});
		
		_setTodayButton = new JButton("Set Today");
		_setTodayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (eventsAreDisabled()) {
					return;
				}
				setToday();
			}
		});
	}
	
	private void createButtonsPanel() {
		_buttonsPanel = new ButtonBankPanel(new String[]{"OK", "Cancel"},
				new ButtonBankListener() {
					public void buttonPressed(int index, String label) {
						switch (index) {
							case 0:
								ok();
								_dialog.dispose();
								break;
							case 1:
								cancel();
								_dialog.dispose();
								break;
							default:
								assert false;
								break;
						}
					}
				}
			);

		_okButton = _buttonsPanel.getButtons()[0];
		_dialog.getRootPane().setDefaultButton(_okButton);
	}
	
	protected void setToday()
	{
		this.setLastDate(new Date());
	}
	
	protected abstract void valuesChanged();
	
	protected abstract void ok();
	
	protected abstract void cancel();

	@Override
	protected void layoutComponents() {
		layoutValuesPanel();	

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(_valuesPanel);
		add(Box.createHorizontalStrut(5));
		add(_buttonsPanel);
	}

	private void layoutValuesPanel() {
		_valuesPanel.setLayout(new GridBagLayout());

		GridBagConstraintsExt c = new GridBagConstraintsExt();
		c.ipadx = 2;
		c.ipady = 2;
		c.insets = new Insets(5, 5, 5, 5);

		c.place(0, 0, 1, 1);
		_valuesPanel.add(_descriptionLabel, c);

		c.place(1, 0, 3, 1);
		_valuesPanel.add(_descriptionField, c);

		c.place(0, 1, 1, 1);
		_valuesPanel.add(_timeLabel, c);

		c.place(1, 1, 2, 1);
		_valuesPanel.add(_timeBox, c);

		c.place(0, 2, 1, 1);
		_valuesPanel.add(_lastDateLabel, c);

		c.place(1, 2, 1, 1);
		_valuesPanel.add(_lastDateSpinner, c);
		
		c.place(2, 2, 1, 1);
		_valuesPanel.add(_setTodayButton, c);
	}
	
	public String getDescription() {
		return _descriptionField.getText();
	}

	public void setDescription(String value) {
		boolean disabledEvents = disableEvents();
		try {
			_descriptionField.setText(value);
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}
	
	public void enableDescription(boolean value) {
		_descriptionField.setEnabled(value);
	}

	public TimeRequired getTime() {
		return (TimeRequired) _timeBox.getSelectedItem();
	}

	public void setTime(TimeRequired value) {
		boolean disabledEvents = disableEvents();
		try {
			_timeBox.setSelectedItem(value);
		}
		finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableTime(boolean value) {
		_timeBox.setEnabled(value);
	}

	public Date getLastDate() {

		// return (Date)_lastDateSpinnerModel.getValue();

		String lastDateText = _lastDateSpinnerEditor.getTextField().getText();
		if (lastDateText == null) {
			return null;
		}
		try {
			return DateUtils.parseDate(lastDateText);
		}
		catch (ParseException e) {
			return null;
		}
	}

	public void setLastDate(Date value) {
		boolean disabledEvents = disableEvents();
		try {
			_lastDateSpinnerModel.setValue(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableLastDate(boolean value) {
		_lastDateSpinner.setEnabled(value);
	}
	
	public void enableOK(boolean value) {
		_okButton.setEnabled(value);
	}
}

