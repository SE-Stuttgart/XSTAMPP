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

package astpa.model.controlaction;

/**
 * The enum for the different kinds of unsafe control actions
 * 
 * @author Fabian Toth
 * 
 */
public enum UnsafeControlActionType {
	
	/**
	 * The enum value for unsafe control action in the not given category
	 * 
	 * @author Fabian Toth
	 */
	NOT_GIVEN,
	/**
	 * The enum value for unsafe control action in the given incorrectly
	 * category
	 * 
	 * @author Fabian Toth
	 */
	GIVEN_INCORRECTLY,
	/**
	 * The enum value for unsafe control action in the wrong timing category
	 * 
	 * @author Fabian Toth
	 */
	WRONG_TIMING,
	/**
	 * The enum value for unsafe control action in the stopped too soon category
	 * 
	 * @author Fabian Toth
	 */
	STOPPED_TOO_SOON;
	
}
