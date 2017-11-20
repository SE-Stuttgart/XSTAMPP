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
package xstpasec.ui;

import org.eclipse.jface.viewers.Viewer;

import messages.Messages;
import xstampp.astpa.ui.sds.ModeFilter;
import xstampp.model.AbstractLTLProvider;

public class RefinedEntryFilter extends ModeFilter{
	private static final String[] FILTERS = new String[]{Messages.All, "Refined Unsecure Control Actions", "Refined Security Constraints"};  	
	public RefinedEntryFilter() {
		super(FILTERS);
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
			switch(this.cscFilterMode){
				case 0:
				case 2:
					if(entry.getSafetyRule().toLowerCase().matches(searchString)){
						return true;
					}
					if(this.cscFilterMode != 0){
						break;
					}
				case 1:
					if(entry.getRefinedUCA().toLowerCase().matches(searchString)){
						return true;
					}
					
			}
			
		}
		
		return false;
	}

}
