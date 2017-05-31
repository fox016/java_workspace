package gui.location;

import gui.common.*;
import gui.main.GUI;


@SuppressWarnings("serial")
public class AddLocationView extends LocationView implements IAddLocationView {

	public AddLocationView(GUI parent, DialogBox dialog) {
		super(parent, dialog);
		
		construct();		

		_controller = new AddLocationController(this);
	}

	@Override
	public IAddLocationController getController() {
		return (IAddLocationController)super.getController();
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
		getController().addLocation();
	}

}


