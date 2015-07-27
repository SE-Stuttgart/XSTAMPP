package xstampp.astpa.controlstructure.controller.editparts;

import java.util.UUID;

import org.eclipse.draw2d.IFigure;


public interface IConnectable extends IControlStructureEditPart{

	IFigure getFeedback();
	UUID getRelativeId();
}
