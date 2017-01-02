/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.controlstructure.figure;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.preference.IPreferenceStore;

import xstampp.astpa.haz.controlstructure.interfaces.IAnchor;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public interface IAnchorFigure extends ConnectionAnchor {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the the x and y factors with which the Anchor position of this
	 *         anchor could be restored
	 */
	Point getAnchorFactor();

	/**
	 * 
	 * @author Lukas
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 *@param owner the owner of this anchor, will only be changed if a legal owner is given and 
 *					the new owner is not equal to the old
	 */
	void updateAnchor(IAnchor model, Object owner);

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the position of this
	 */
	public Point getAnchorPosition();
	
	void setPreferenceStore(IPreferenceStore store);
}
