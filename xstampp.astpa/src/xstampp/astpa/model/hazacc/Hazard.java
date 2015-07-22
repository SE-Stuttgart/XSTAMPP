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

package xstampp.astpa.model.hazacc;

import javax.xml.bind.annotation.XmlRootElement;

import xstampp.astpa.model.ATableModel;

/**
 * Class for hazards
 *
 * @author Aleksander Zotov, Fabian Toth
 *
 */
@XmlRootElement(name = "hazard")
public class Hazard extends ATableModel {

	/**
	 * Constructor of a hazard
	 *
	 * @param title
	 *            the title of the new hazard
	 * @param description
	 *            the description of the new hazard
	 * @param number
	 *            the number of the new hazard
	 *
	 * @author Fabian Toth
	 */
	public Hazard(String title, String description, int number) {
		super(title, description, number);
	}

	/**
	 * Empty constructor used for JAXB. Do not use it!
	 *
	 * @author Fabian Toth
	 */
	public Hazard() {
		// empty constructor for JAXB
	}

}
