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

package xstampp.astpa.controlstructure.utilities;

import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * A custom PalettePage that helps CSGraphicalEditor keep the two PaletteViewers
 * (one displayed in the editor and the other displayed in the PaletteView) in
 * sync when switching from one to the other (i.e., it helps maintain state
 * across the two viewers).
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public class CSPalettePage extends PaletteViewerPage {

	private FlyoutPaletteComposite splitter;

	/**
	 * Constructor
	 * 
	 * @param provider
	 *            the provider used to create a PaletteViewer
	 */
	public CSPalettePage(PaletteViewerProvider provider) {
		super(provider);

	}

	/**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		if (this.splitter != null) {
			this.splitter.setExternalViewer(this.viewer);
		}
	}

	/**
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	@Override
	public void dispose() {
		if (this.splitter != null) {
			this.splitter.setExternalViewer(null);
		}
		super.dispose();

	}

	/**
	 * @return the PaletteViewer created and displayed by this page
	 */
	public PaletteViewer getPaletteViewer() {
		return this.viewer;
	}

}
