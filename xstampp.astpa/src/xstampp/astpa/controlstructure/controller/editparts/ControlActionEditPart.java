package xstampp.astpa.controlstructure.controller.editparts;

import messages.Messages;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.SWT;

import xstampp.astpa.controlstructure.controller.policys.CSConnectionPolicy;
import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.controlstructure.figure.TextFieldFigure;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * TODO
 * 
 * @author Lukas Balzer
 * 
 */
public class ControlActionEditPart extends CSAbstractEditPart {

	/**
	 * this constructor sets the unique ID of this EditPart which is the same in
	 * its model and figure
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            TODO
	 */
	public ControlActionEditPart(IControlStructureEditorDataModel model,
			String stepId) {
		super(model, stepId, 1);

	}

	@Override
	protected IFigure createFigure() {
		IControlStructureFigure tmpFigure = new TextFieldFigure(this.getId());

		tmpFigure.setToolTip(new Label(Messages.ControlAction));

		tmpFigure.getTextField().setFontStyle(SWT.BOLD);
		tmpFigure
				.setParent(((CSAbstractEditPart) this.getParent()).getFigure());
		return tmpFigure;
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		IRectangleComponent modelTemp = this.getDataModel().getComponent(
				this.getId());
		if (this.getDataModel().getControlAction(
				modelTemp.getControlActionLink()) == null) {
			this.getDataModel().removeComponent(modelTemp.getId());
			this.deactivate();
		}
		this.getDataModel().setControlActionTitle(
				modelTemp.getControlActionLink(), modelTemp.getText());

	}
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new CSConnectionPolicy(this.getDataModel(), this.getStepId()));
	}
}
