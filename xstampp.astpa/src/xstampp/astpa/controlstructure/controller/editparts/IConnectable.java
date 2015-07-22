package xstampp.astpa.controlstructure.controller.editparts;

import org.eclipse.draw2d.IFigure;


public interface IConnectable extends IControlStructureEditPart{
	void setRelative(IRelative relative);
	IFigure getFeedback();
	
}
