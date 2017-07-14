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
package xstampp.stpasec.util.jobs;

import org.eclipse.jface.dialogs.MessageDialog;

public class SaveRunnable implements Runnable {

	private SaveJob job;
	public SaveRunnable(SaveJob job) {
		this.job = job; 
	}
	@Override
	public void run() {
		
		boolean comp = MessageDialog.openQuestion(null, "Store Preferences", "Do you want to store "
				+ "the STPA file in compabillity mode?\n(Needed for usage with older versions, special characters are maybe"
				+ " not saved correctly!)");
		this.job.setCompabillityMode(comp);
	}

}
