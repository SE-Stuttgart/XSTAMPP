package xstampp.astpa.controlstructure.controller.editparts;

import org.eclipse.draw2d.IFigure;

public interface IRelative extends IControlStructureEditPart{

	IFigure getFeedback(IConnectable member);
	IFigure getFeedback();
	void eraseFeedback();
	void setMember(IConnectable member);
	void updateFeedback();
}
