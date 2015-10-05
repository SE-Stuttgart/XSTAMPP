package acast.controlstructure.controller.commands;


import acast.controlstructure.controller.editparts.IConnectable;
import acast.controlstructure.controller.editparts.IRelative;
import acast.model.interfaces.IControlStructureEditorDataModel;

public class addRelativeCommand extends ControlStructureAbstractCommand {
	
	private IConnectable connectable;
	private IRelative relative;

	public addRelativeCommand(IControlStructureEditorDataModel model,String stepID,
							IRelative relative,IConnectable connectable) {
		super(model, stepID);
		this.relative = relative;
		this.connectable = connectable;
	}
	
	
	@Override
	public void execute() {
		this.getDataModel().setRelativeOfComponent(this.connectable.getId(), this.relative.getId());
		super.execute();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
