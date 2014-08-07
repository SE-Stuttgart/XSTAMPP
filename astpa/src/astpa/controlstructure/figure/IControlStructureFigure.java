/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import astpa.controlstructure.utilities.CSTextLabel;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public interface IControlStructureFigure extends IFigure {
	
	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the id which the figure inherits from its model
	 */
	UUID getId();
	
	/**
	 * @author Lukas Balzer
	 * 
	 * @return the text which is displayed in the CSTextLabel
	 */
	String getText();
	
	/**
	 * @author Lukas Balzer
	 * 
	 * @param text the new Text
	 */
	void setText(String text);
	
	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the CSTextLabel
	 * @see CSTextLabel#getTextField
	 */
	CSTextLabel getTextField();
	
	/**
	 * This method is called when the Layout of the Model changes. It calls
	 * setConstraint() in the Parent Figure to change the Layout Constraint of
	 * this Figure relatively to it.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param rect the Rectangle which stores the Layout
	 */
	void setLayout(Rectangle rect);
	
	/**
	 * adds a anchor Feedback Rectangle to the Root, the Root has only on
	 * Feedback Rectangle which can be added at Points currently to be
	 * highlighted
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param ref the anchorPoint for which a feedback should be created
	 */
	public void addHighlighter(Point ref);
	
	/**
	 * removes the Feedback Recangle from the editor
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public void removeHighlighter();
	
	/**
	 * This method disables the use of the offset area around the figures bounds
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public void disableOffset();
	
	/**
	 * this method enables an offset around the child figures to find connection
	 * anchors with a certain "snap-to" effect
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public void enableOffset();
	
	public void enableDeco();
	public void disableDeco();
	public boolean hasDeco();
}
