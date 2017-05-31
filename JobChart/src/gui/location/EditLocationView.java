package gui.location;

import model.Location;
import gui.common.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class EditLocationView extends LocationView implements IEditLocationView {

	public EditLocationView(GUI parent, DialogBox dialog, Location target) {
		super(parent, dialog);
		
		construct();		

		_controller = new EditLocationController(this, target);
	}

	@Override
	public IEditLocationController getController() {
		return (IEditLocationController)super.getController();
	}

	@Override
	protected void valuesChanged() {
		getController().valuesChanged();
	}

	@Override
	protected void cancel() {
		return;
	}

	@Override
	protected void ok() {
		getController().editLocation();
	}
}


