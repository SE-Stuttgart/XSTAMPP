package xstampp.astpa.controlstructure.controller.policys;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;

import xstampp.astpa.controlstructure.controller.editparts.IConnectable;

public class CSSelectionEditPolicy extends SelectionEditPolicy {

	public CSSelectionEditPolicy() {

	}
	@Override
	protected void showFocus() {
		System.out.println("focus");
		super.showFocus();
	}
	@Override
	protected void hideSelection() {
		if(getHost() != null){
			IFigure figure= getHost().getFeedback();
			if(figure != null && figure.getParent() == getFeedbackLayer()){
				removeFeedback(figure);
			}
		}
	}

	@Override
	protected void showSelection() {
		if(getHost() != null){
			IFigure figure= getHost().getFeedback();
			if(figure != null && figure.getParent() != getFeedbackLayer()){
				addFeedback(figure);
			}
		}
	}
	@Override
	public IConnectable getHost() {
		if(super.getHost() instanceof IConnectable){
			return ((IConnectable) super.getHost());
		}
		return null;
	}
	
}
