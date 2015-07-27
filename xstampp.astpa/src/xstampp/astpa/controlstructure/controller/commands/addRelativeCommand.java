package xstampp.astpa.controlstructure.controller.commands;

import xstampp.astpa.controlstructure.controller.editparts.IConnectable;
import xstampp.astpa.controlstructure.controller.editparts.IRelative;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.model.IDataModel;

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
		this.getDataModel().setRelativeOfComponent(connectable.getId(), relative.getId());
		super.execute();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
