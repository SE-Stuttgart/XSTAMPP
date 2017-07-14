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
package xstampp.stpasec.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import xstampp.preferences.IControlStructureConstants;
import xstampp.stpasec.Activator;

public class ConnectionPreferenceHandler extends AbstractHandler {

	public ConnectionPreferenceHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if(Activator.getDefault().getPreferenceStore().getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS)){
			Activator.getDefault().getPreferenceStore().setValue(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS,Boolean.FALSE);
		}else{
			Activator.getDefault().getPreferenceStore().setValue(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS,Boolean.TRUE);
		}
		return null;
	}

}
