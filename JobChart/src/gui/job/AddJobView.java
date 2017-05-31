package gui.job;

import model.Location;
import gui.common.*;
import gui.main.GUI;

@SuppressWarnings("serial")
public class AddJobView extends JobView implements IAddJobView {

	public AddJobView(GUI parent, DialogBox dialog, Location location)
	{
		super(parent, dialog);
		
		construct();		

		_controller = new AddJobController(this, location);
	}

	@Override
	public IAddJobController getController() {
		return (IAddJobController)super.getController();
	}

	@Override
	protected void valuesChanged() {
		getController().valuesChanged();
	}

	@Override
	protected void cancel() {
		getController().cancel();
	}

	@Override
	protected void ok() {
		getController().addJob();
	}
}


