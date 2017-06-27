/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.controlstructure.controller.editparts;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

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
	IFigure getFeedback(IMemberEditPart member, Color color);
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
