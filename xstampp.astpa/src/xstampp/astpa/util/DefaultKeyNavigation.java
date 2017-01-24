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
package xstampp.astpa.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Text;

public class DefaultKeyNavigation implements KeyListener {

	private Text control;

	public DefaultKeyNavigation(Text control) {
			this.control= control;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.keyCode){
		case SWT.ARROW_LEFT:{
			this.control.setSelection(
					this.control.getSelection().x -1, 
					this.control.getSelection().x-1);
			break;
		}
		case SWT.ARROW_RIGHT:{
			this.control.setSelection(
					this.control.getSelection().x +1,
					this.control.getSelection().x+1);
		}
		}
	}

}
