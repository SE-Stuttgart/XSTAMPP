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

package acast.controlstructure.utilities;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;

/**
 * This class implements <code>FlyoutPreferences
 * 
 * 
 * @author Lukas Balzer
 * @see FlyoutPreferences
 * 
 */
public class CSPalettePreferences implements FlyoutPreferences {

	private static final int INITIAL_PALETTE_WIDTH = 200;

	/**
	 * the state represented by an integer value
	 * 
	 * @see #setPaletteState
	 */
	private int paletteState;

	private int dockLocation;

	private int paletteWidth;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public CSPalettePreferences() {
		this.dockLocation = PositionConstants.EAST;
		this.paletteWidth = CSPalettePreferences.INITIAL_PALETTE_WIDTH;
		this.paletteState = FlyoutPaletteComposite.STATE_PINNED_OPEN;
	}

	@Override
	public int getDockLocation() {
		return this.dockLocation;
	}

	@Override
	public int getPaletteState() {
		return this.paletteState;
	}

	@Override
	public int getPaletteWidth() {
		return this.paletteWidth;
	}

	@Override
	public void setDockLocation(int location) {
		this.dockLocation = location;

	}

	@Override
	public void setPaletteState(int state) {
		this.paletteState = state;

	}

	@Override
	public void setPaletteWidth(int width) {
		this.paletteWidth = width;

	}

}
