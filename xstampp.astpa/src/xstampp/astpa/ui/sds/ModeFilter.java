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
package xstampp.astpa.ui.sds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ViewerFilter;

/**
 * A ViewerFilter which contains a category array which can be empty 
 * so that the filter selection can be taken according to a category
 * 
 * @author Lukas Balzer
 *
 */
public abstract class ModeFilter extends ViewerFilter{
	protected int cscFilterMode;
	private List<String> categorys;
	protected String searchString;

	public ModeFilter(String[] filters){
		categorys = new ArrayList<>();
		Collections.addAll(categorys, filters);
	}
	/**
	 * @return the rsc
	 */
	public int getCSCFilterMode() {
		return this.cscFilterMode;
	}

	/**
	 * @param cscFilterMode
	 *            the cscFilterMode to set
	 */
	public void setCSCFilterMode(String cscFilterMode) {
		this.cscFilterMode = categorys.indexOf(cscFilterMode);
	}
	
	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 * @param s
	 *            the string for the filter
	 */
	public void setSearchText(String s) {
		// ensure that the value can be used for matching
		this.searchString = ".*" + s.toLowerCase() + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	/**
	 * @return the categorys
	 */
	public List<String> getCategorys() {
		return this.categorys;
	}
}
