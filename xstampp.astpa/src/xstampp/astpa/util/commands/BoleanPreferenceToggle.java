/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.astpa.Activator;

public class BoleanPreferenceToggle extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String preference = event.getParameter("xstampp.astpa.command.preference");
		if(preference == null){
			return null;
		}
		
		Boolean current = Activator.getDefault().getPreferenceStore().getBoolean(preference);
		Activator.getDefault().getPreferenceStore().setValue(preference, !current);
		return null;
	}

}
