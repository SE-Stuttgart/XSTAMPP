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

package xstampp.ui.editors.interfaces;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.preference.IPreferenceStore;

import xstampp.Activator;

/**
 * Interface all views that can be put into the view container implement. This
 * interface is still compatible with window builder. Select createPartControl
 * as the entry point or use the wbp.parser.entryPoint tag.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public interface IEditorBase extends Observer {

	/**
	 * Stores preferences
	 * 
	 * @author Sebastian Sieber
	 */
	IPreferenceStore STORE = Activator.getDefault().getPreferenceStore();

	/**
	 * Gets the ID of the view. It doesn't matter what the ID is, as long as it
	 * is unique.
	 * 
	 * @return returns the view's ID.
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	String getId();

	/**
	 * Get the title of the view.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the title of the view.
	 */
	String getTitle();

	@Override
	void update(Observable dataModelController, Object updatedValue);

}
