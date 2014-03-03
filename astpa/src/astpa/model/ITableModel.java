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

package astpa.model;

import java.util.UUID;

/**
 * Interface for elements which can be shown in a table
 * 
 * @author Fabian Toth
 * 
 */
public interface ITableModel extends Comparable<ITableModel> {
	
	/**
	 * Getter for the id. The id is a unique identifier for the element
	 * 
	 * @return the id
	 * 
	 * @author Fabian Toth
	 */
	UUID getId();
	
	/**
	 * Getter for the title
	 * 
	 * @return the title
	 * 
	 * @author Fabian Toth
	 */
	String getTitle();
	
	/**
	 * Getter for the description
	 * 
	 * @return the description
	 * 
	 * @author Fabian Toth
	 */
	String getDescription();
	
	/**
	 * Getter for the number. The number defines the row in which the element
	 * will be shown
	 * 
	 * @return the number
	 * 
	 * @author Fabian Toth
	 */
	int getNumber();
}
