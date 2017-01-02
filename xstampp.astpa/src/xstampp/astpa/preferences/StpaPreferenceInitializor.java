/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import xstampp.preferences.IControlStructureConstants;



/**
 *
 * @author Lukas Balzer
 * @since 2.0.0
 *
 */
public class StpaPreferenceInitializor extends AbstractPreferenceInitializer {
	
	private final IPreferenceStore store = xstampp.astpa.Activator.getDefault()
			.getPreferenceStore();

	/**
	 * The shared instance.
	 */
	private static StpaPreferenceInitializor initalizer;

	/**
	 * Default font used by labels of each view
	 */
	private static final Font DEFAUL_FONT = new Font(Display.getCurrent(),
			Display.getCurrent().getSystemFont().getFontData().toString(), 9,
			SWT.NORMAL);
	

	public StpaPreferenceInitializor(){
		initalizer=this;
	}
	
	/**
	 * Returns the shared instance.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return initalizer
	 */
	public static StpaPreferenceInitializor getDefault() {
		return initalizer;
	}

	@Override
	public void initializeDefaultPreferences() {
		PreferenceConverter.setDefault(this.store, IControlStructureConstants.CONTROLSTRUCTURE_FONT,
									   DEFAUL_FONT.getFontData());
		PreferenceConverter.setDefault(this.store, IControlStructureConstants.CONTROLSTRUCTURE_FONT_COLOR,
									   ColorConstants.black.getRGB());
		PreferenceConverter.setDefault(this.store, IControlStructureConstants.CONTROLSTRUCTURE_CONTROLLER_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_BLUE).getRGB());
		PreferenceConverter.setDefault(this.store, IControlStructureConstants.CONTROLSTRUCTURE_ACTUATOR_COLOR,
				   ColorConstants.orange.getRGB());
		PreferenceConverter.setDefault(this.store, IControlStructureConstants.CONTROLSTRUCTURE_PROCESS_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA).getRGB());
		PreferenceConverter.setDefault(this.store, IControlStructureConstants.CONTROLSTRUCTURE_SENSOR_COLOR,
				   ColorConstants.green.getRGB());
		this.store.setDefault(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS, true);
		this.store.setDefault(IControlStructureConstants.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER, true);
		this.store.setDefault(IControlStructureConstants.CONTROLSTRUCTURE_SHOW_LISTOFCA_BORDER, true);

	}
	


}
