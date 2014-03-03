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

package astpa.model.sds;

import astpa.model.ATableModel;

/**
 * Class for system goals
 * 
 * @author Fabian Toth
 * 
 */
public class SystemGoal extends ATableModel {
	
	/**
	 * Constructor of a system goal
	 * 
	 * @param title the title of the new system goal
	 * @param description the description of the new system goal
	 * @param number the number of the new system goal
	 * 
	 * @author Fabian Toth, Jaqueline Patzek
	 */
	public SystemGoal(String title, String description, int number) {
		super(title, description, number);
	}
	
	/**
	 * Empty constructor for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth, Jaqueline Patzek
	 */
	public SystemGoal() {
		// empty constructor for JAXB
	}
}
