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

package xstampp.astpa.controlstructure.figure;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;

import xstampp.astpa.model.controlstructure.interfaces.IAnchor;

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
	 */
	void updateAnchor(IAnchor model);

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the position of this
	 */
	public Point getAnchorPosition();
}
