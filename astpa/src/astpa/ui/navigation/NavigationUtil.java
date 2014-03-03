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

package astpa.ui.navigation;

import org.eclipse.swt.graphics.Image;

import astpa.Activator;

/**
 * Util class for the navigation. Wraps some methods that are needed by most
 * classes of the navigation
 * 
 * @author Fabian Toth
 * 
 */
public final class NavigationUtil {
	
	private static NavigationUtil instance = null;
	
	
	/**
	 * Hide the constructor
	 * 
	 * @author Fabian Toth
	 */
	private NavigationUtil() {
	}
	
	/**
	 * Returns the only instance of this class
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the only instance
	 */
	public static NavigationUtil getInstance() {
		if (NavigationUtil.instance == null) {
			NavigationUtil.instance = new NavigationUtil();
		}
		return NavigationUtil.instance;
	}
	
	/**
	 * Gets the right image for the link
	 * 
	 * @author Fabian Toth
	 * 
	 * @param imageId the ID of the Step to get the rigth icon
	 * 
	 * @return the image for the link
	 */
	public Image getImage(String imageId) {
		return Activator.getImageDescriptor("/icons/buttons/navigation/" + imageId + ".png").createImage(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}
