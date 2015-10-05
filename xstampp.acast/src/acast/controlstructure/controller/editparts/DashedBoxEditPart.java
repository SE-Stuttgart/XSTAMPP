/**
 * 
 * @author Lukas Balzer
 */
package acast.controlstructure.controller.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

import acast.controlstructure.controller.policys.CSDeletePolicy;
import acast.controlstructure.controller.policys.CSDirectEditPolicy;
import acast.controlstructure.figure.TextFieldFigure;
import acast.model.interfaces.IControlStructureEditorDataModel;



/**
 *
 * @author Lukas Balzer
 *
 */
public class DashedBoxEditPart extends CSAbstractEditPart {

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param model
	 * @param stepId
	 */
	public DashedBoxEditPart(IControlStructureEditorDataModel model,
			String stepId) {
		super(model, stepId, 2);
	}
	
	@Override
	protected IFigure createFigure() {
		TextFieldFigure tmpFigure = new TextFieldFigure(this.getId());
		tmpFigure.setDashed();
		tmpFigure.setBackgroundColor(null);
		tmpFigure.setParent(((IControlStructureEditPart) this.getParent()).getContentPane());
		return tmpFigure;
	}
	@Override
	protected void createEditPolicies() {
		/*
		 * the Edit role is a constant which tells the program in what policy is
		 * to use in what situation when performed,
		 * performRequest(EditPolicy.constant) is called
		 */
//		this.installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
		this.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new CSDirectEditPolicy(this.getDataModel(), this.getStepId()));
//		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new CSEditPolicy(
//				this.getDataModel(), this.getStepId()));
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new CSDeletePolicy(
				this.getDataModel(), this.getStepId()));
	}
}
