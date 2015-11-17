package xstampp.astpa.controlstructure.controller.editparts;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * an IControlStructureEditPart which can be related to one or many IMemberEditParts 
 * therefore this class provides several functions to generate a Feedback Figure to visualize a relation
 * 
 * @author Lukas Balzer
 * @since 2.0.2
 *
 */
public interface IRelativePart extends IControlStructureEditPart{

	/**
	 *	
	 * @author Lukas Balzer
	 *
	 * @param member a component whioch implements IConnectable
	 * @return a PolylineFigure
	 */
	IFigure getFeedback(IMemberEditPart member);
	IFigure getFeedback(Rectangle bounds);
	IFigure getFeedback();
	
	/**
	 * deletes what ever feedback is stored 
	 *
	 * @author Lukas Balzer
	 *
	 */
	void eraseFeedback();
	
	/**
	 * this method should add a IMemberEditPart to a Set of
	 * members which are related to this part
	 * 
	 * @param member a edit of type IMemberEditPart which should be add to the set of members
	 */
	void addMember(IMemberEditPart member);
	List<IMemberEditPart> getMembers();
	void removeMember(IMemberEditPart member);
	void updateFeedback();
}
