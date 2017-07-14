package xstpasec.settings;

/*******************************************************************************
 * Copyright (c) 2015, 2016 Yannic Sowoidnich
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/



import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import xstampp.Activator;

/**
 * Class used to initialize default preference values.
 * 
 * @author Yannic Sowoidnich
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer implements XSTPAPreferenceConstants {

	public static final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	/**
	 * The shared instance.
	 */
	private static PreferenceInitializer initalizer;


	/**
	 * Constructor.
	 */
	public PreferenceInitializer() {
		PreferenceInitializer.initalizer = this;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @author Yannic Sowoidnich
	 * 
	 * @return initalizer
	 */
	public static PreferenceInitializer getDefault() {
		return PreferenceInitializer.initalizer;
	}

	@Override
	public void initializeDefaultPreferences() {
		
		// ACTS Settings
		store.setDefault("ACTS_Path", "");
		store.setDefault(ACTS_STRENGTH, 1);
		store.setDefault(ACTS_ALGORITHMUS, "ipog");
		store.setDefault(ACTS_MODE, "scratch");
		store.setDefault(ACTS_CHANDLER, "forbiddentuples");
				
	}
}
