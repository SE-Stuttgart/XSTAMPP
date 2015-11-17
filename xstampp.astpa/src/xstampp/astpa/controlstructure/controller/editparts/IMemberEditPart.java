package xstampp.astpa.controlstructure.controller.editparts;

import java.util.UUID;

import org.eclipse.draw2d.IFigure;


/**
 * a part that implement this interface can be related to a IRelative
 * they can thereby contribute a Feedback to the ui
 *  
 * @author Lukas Balzer
 * @since 2.0.2
 *
 */
public interface IMemberEditPart extends IControlStructureEditPart{

	IFigure getFeedback();
	UUID getRelativeId();
}
