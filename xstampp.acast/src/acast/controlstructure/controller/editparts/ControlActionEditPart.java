package acast.controlstructure.controller.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.swt.SWT;

import acast.controlstructure.controller.policys.CSConnectionPolicy;
import acast.controlstructure.controller.policys.CSDeletePolicy;
import acast.controlstructure.controller.policys.CSDirectEditPolicy;
import acast.controlstructure.controller.policys.CSSelectionEditPolicy;
import acast.controlstructure.figure.IControlStructureFigure;
import acast.controlstructure.figure.TextFieldFigure;
import acast.model.controlstructure.interfaces.IRectangleComponent;
import acast.model.interfaces.IControlStructureEditorDataModel;
import messages.Messages;


/**
 * TODO
 * 
 * @author Lukas Balzer
 * 
 */
public class ControlActionEditPart extends AbstractMemberEditPart{

	/**
	 * this constructor sets the unique ID of this EditPart which is the same in
	 * its model and figure
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            the id of the current step
	 */
	public ControlActionEditPart(IControlStructureEditorDataModel model,
			String stepId) {
		super(model, stepId, 1);

	}

	@Override
	protected IFigure createFigure() {
		IControlStructureFigure tmpFigure = new TextFieldFigure(this.getId());

		tmpFigure.setToolTip(new Label(Messages.ControlAction));
		tmpFigure.addMouseMotionListener(this);
		tmpFigure.getTextField().setFontStyle(SWT.BOLD);
		tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
		return tmpFigure;
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		IRectangleComponent modelTemp = this.getDataModel().getComponent(
				this.getId());
		if (this.getDataModel().getControlActionU(
				modelTemp.getControlActionLink()) == null) {
			this.getDataModel().removeComponent(modelTemp.getId());
			this.deactivate();
		}
		this.getDataModel().setControlActionTitle(
				modelTemp.getControlActionLink(), modelTemp.getText());
		
	}
	@Override
	protected void createEditPolicies() {
		this.installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
		this.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new CSDirectEditPolicy(this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new CSDeletePolicy(this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new CSConnectionPolicy(this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new CSSelectionEditPolicy());
	}



}
