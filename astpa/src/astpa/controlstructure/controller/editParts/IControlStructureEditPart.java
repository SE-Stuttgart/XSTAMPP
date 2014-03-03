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

package astpa.controlstructure.controller.editParts;

import java.util.Map;
import java.util.UUID;

import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.GraphicalEditPart;

/**
 * 
 * @author Lukas
 * 
 */
public interface IControlStructureEditPart extends GraphicalEditPart {
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the id of this editParts model
	 */
	UUID getId();
	
	/**
	 * this getter returns a Map which stored all Component ids and maps them to
	 * an eventually new value.
	 * 
	 * 
	 * @author Lukas Balzer
	 * @return this map is used for problems like that:
	 * 
	 *         <ol>
	 *         <li>draw two components in the editor
	 *         <li>draw a connection between them
	 *         <li>delete the connection and the components
	 *         <li>redo all deletes
	 *         <ul>
	 *         <li>Problem : Command tries to build connection between two non
	 *         existing figures
	 *         <li>Solution : Map old component ids to newIds and update
	 *         connectionAnchors
	 *         </ul>
	 *         </ol>
	 * 
	 */
	Map<UUID, UUID> getIdMap();
	
	/**
	 * provides the right transformation to realize the a child-parent
	 * releationship in gef
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param t the Translatable which is given to it's parents figure
	 */
	void translateToRoot(Translatable t);
	
}
