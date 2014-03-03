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

package astpa.ui.menu.file.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

/**
 * Class that stores whether the save menu should be enabled or not
 * 
 * @author Fabian Toth
 * 
 */
public class SaveState extends AbstractSourceProvider {
	
	/**
	 * The id of the state
	 * 
	 * @author Fabian Toth
	 */
	public static final String STATE = "astpa.ui.menu.file.commands.savestate"; //$NON-NLS-1$
	/**
	 * The string representation of the ENABLED value
	 * 
	 * @author Fabian Toth
	 */
	public static final String S_ENABLED = "ENABLED"; //$NON-NLS-1$
	/**
	 * The string representation of the DISABLED value
	 * 
	 * @author Fabian Toth
	 */
	public static final String S_DISABLED = "DISABLED"; //$NON-NLS-1$
	
	
	enum State {
		ENABLED, DISABLED
	}
	
	
	private static State curState = State.DISABLED;
	
	
	@Override
	public void dispose() {
		// Nothing to do here
	}
	
	@Override
	public String[] getProvidedSourceNames() {
		return new String[] {SaveState.STATE};
	}
	
	@Override
	public Map<String, String> getCurrentState() {
		Map<String, String> map = new HashMap<>(1);
		if (SaveState.curState == State.ENABLED) {
			map.put(SaveState.STATE, SaveState.S_ENABLED);
		} else if (SaveState.curState == State.DISABLED) {
			map.put(SaveState.STATE, SaveState.S_DISABLED);
		}
		return map;
	}
	
	/**
	 * Sets the save entries in the menu to enabled
	 * 
	 * @author Fabian Toth
	 */
	public void setEnabled() {
		SaveState.curState = State.ENABLED;
		this.fireSourceChanged(ISources.WORKBENCH, SaveState.STATE, SaveState.S_ENABLED);
	}
}
