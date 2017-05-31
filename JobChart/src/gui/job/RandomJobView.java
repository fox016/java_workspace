package gui.job;

import gui.common.ButtonBankListener;
import gui.common.ButtonBankPanel;
import gui.common.DialogBox;
import gui.common.DialogView;
import gui.common.GridBagConstraintsExt;
import gui.main.GUI;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Location;
import model.TimeRequired;

import common.util.DateUtils;

@SuppressWarnings("serial")
public class RandomJobView extends DialogView implements IRandomJobView
{
	private JPanel _valuesPanel;
	private JCheckBox _timeCheckbox;
	private JComboBox<TimeRequired> _timeBox;
	private JCheckBox _lastDateCheckbox;
	private SpinnerModel _lastDateSpinnerModel;
	private JSpinner.DateEditor _lastDateSpinnerEditor;
	private JSpinner _lastDateSpinner;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;
	
	public RandomJobView(GUI parent, DialogBox dialog, Location location) {
		super(parent, dialog);
		construct();
		_controller = new RandomJobController(this, location);
	}
	
	@Override
	protected void createComponents() {
		createValuesPanel();
		createButtonsPanel();
	}
	
	private void createValuesPanel() {

		Date initDate = DateUtils.currentDate();
		Date latestDate = initDate;
		Date earliestDate = DateUtils.earliestDate();
		
		_valuesPanel = new JPanel();
		
		_timeCheckbox = new JCheckBox("Job Time Required:");
		_timeCheckbox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (eventsAreDisabled()) {
					return;
				}
				timeFilterChanged();
			}
		});
		
		_lastDateCheckbox = new JCheckBox("Job Last Completed Before:");
		_lastDateCheckbox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (eventsAreDisabled()) {
					return;
				}
				dateFilterChanged();
			}
		});
		
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
	
	@Override
	public IRandomJobController getController() {
		return (IRandomJobController)super.getController();
	}
	
	protected void valuesChanged() {
		getController().valuesChanged();
	}
	
	protected void ok(){
		getController().showRandomJob();
	}
	
	protected void cancel(){
		getController().cancel();
	}
	
	private void timeFilterChanged()
	{
		getController().timeFilterChanged();
	}
	
	private void dateFilterChanged()
	{
		getController().dateFilterChanged();
	}

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

		c.place(0, 1, 1, 1);
		_valuesPanel.add(_timeCheckbox, c);

		c.place(1, 1, 1, 1);
		_valuesPanel.add(_timeBox, c);

		c.place(0, 2, 1, 1);
		_valuesPanel.add(_lastDateCheckbox, c);

		c.place(1, 2, 1, 1);
		_valuesPanel.add(_lastDateSpinner, c);
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

	@Override
	public boolean getTimeCheckbox() {
		return _timeCheckbox.isSelected();
	}

	@Override
	public void setTimeCheckbox(boolean value) {
		_timeCheckbox.setSelected(value);
	}

	@Override
	public boolean getDateCheckbox() {
		return _lastDateCheckbox.isSelected();
	}

	@Override
	public void setDateCheckbox(boolean value) {
		_lastDateCheckbox.setSelected(value);
	}
}
