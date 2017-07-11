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
package xstpapriv.ui;

import org.eclipse.jface.viewers.Viewer;

import xstampp.astpa.ui.sds.ModeFilter;
import xstampp.model.AbstractLTLProvider;

public class LTLEntryFilter extends ModeFilter{


	public LTLEntryFilter() {
		super(new String[0]);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if ((this.searchString == null) || (this.searchString.equals(".*.*"))) {
			return true;
		}
		if(element instanceof AbstractLTLProvider){
			AbstractLTLProvider entry = (AbstractLTLProvider) element;
			if(String.valueOf(entry.getNumber()).matches(searchString)){
				return true;
			}
			if(String.valueOf(entry.getNumber()).matches(searchString)){
				return true;
			}
			if(entry.getLtlProperty().toLowerCase().matches(searchString)){
				return true;
			}
			
		}
		
		return false;
	}

}
