package xstampp.astpa.controlstructure.controller.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

public interface IRelative extends IControlStructureEditPart{

	/**
	 *	
	 * @author Lukas Balzer
	 *
	 * @param member a component whioch implements IConnectable
	 * @return a PolylineFigure
	 */
	IFigure getFeedback(IConnectable member);
	IFigure getFeedback(Rectangle bounds);
	IFigure getFeedback();
	
	/**
	 * deletes what ever feedback is stored 
	 *
	 * @author Lukas Balzer
	 *
	 */
	void eraseFeedback();
	
	
	void setMember(IConnectable member);
	void updateFeedback();
}
