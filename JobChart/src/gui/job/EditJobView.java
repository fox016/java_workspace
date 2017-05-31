package gui.job;

import model.Job;
import gui.common.*;
import gui.main.GUI;

@SuppressWarnings("serial")
public class EditJobView extends JobView implements IEditJobView {

	public EditJobView(GUI parent, DialogBox dialog, Job job)
	{
		super(parent, dialog);
		
		construct();		

		_controller = new EditJobController(this, job);
	}

	@Override
	public IEditJobController getController() {
		return (IEditJobController)super.getController();
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
		getController().editJob();
	}
}


