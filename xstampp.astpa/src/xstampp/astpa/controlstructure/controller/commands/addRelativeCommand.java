package xstampp.astpa.controlstructure.controller.commands;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

import xstampp.astpa.controlstructure.controller.editparts.IConnectable;
import xstampp.astpa.controlstructure.controller.editparts.IRelative;

public class addRelativeCommand extends Command {
	
	private IConnectable connectable;
	private IRelative relative;

	public addRelativeCommand(IRelative relative,IConnectable connectable) {
		super();
		System.out.println("create " +getClass());
		this.relative = relative;
		this.connectable = connectable;
	}
	
	
	@Override
	public void execute() {
		this.connectable.setRelative(this.relative);
		super.execute();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
