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
package xstpapriv.ui.tables.utils;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstpapriv.model.ContextTableCombination;
import xstpapriv.model.ControlActionEntry;
import xstpapriv.model.ProcessModelValue;
import xstpapriv.ui.View;

public class EntryCellModifier implements ICellModifier {
	  private Viewer viewer;
	  private View view;
	private IExtendedDataModel model;
	  
	  public EntryCellModifier(Viewer viewer,IExtendedDataModel controller) {
	    this.viewer = viewer;
	    this.model = controller;
	  }

	  /**
	   * Returns whether the property can be modified
	   * 
	   * @param element
	   *            the element
	   * @param property
	   *            the property
	   * @return boolean
	   */
	  public boolean canModify(Object element, String property) {
	    // Allow editing of all values
	    return true;
	  }

	  /**
	   * Returns the value for the property
	   * 
	   * @param element
	   *            the element
	   * @param property
	   *            the property
	   * @return Object
	   */
	  public Object getValue(Object element, String property) {
		// if Controller with PMEntry calls 
		if (ProcessModelValue.class == element.getClass()) {
			ProcessModelValue entry = (ProcessModelValue) element;
			if (View.COMMENTS.equals(property)) {

				return entry.getComments();
			}

			else {
				return null;
			}
		}
		// if Control ActionEntry calls
		else if (ControlActionEntry.class == element.getClass()){
			ControlActionEntry entry = (ControlActionEntry) element;
			if (View.SAFETY_CRITICAL.equals(property)) {
		      return Boolean.valueOf(entry.getSafetyCritical());
			}
			else if (View.COMMENTS.equals(property)) {
				return entry.getComments();
			}
			else {
				return null;
			}
		}
		
		else {
			ContextTableCombination entry = (ContextTableCombination) element;
			if (View.IS_HAZARDOUS.equals(property)) {
		      return Boolean.valueOf(entry.getGlobalHazardous());
			}
			
			else if (View.HAZ_IF_ANYTIME.equals(property)) {
			      return Boolean.valueOf(entry.getHAnytime());
				}
			
			else if (View.HAZ_IF_EARLY.equals(property)) {
			      return Boolean.valueOf(entry.getHEarly());
				}
			
			else if (View.HAZ_IF_LATE.equals(property)) {
			      return Boolean.valueOf(entry.getHLate());
				}
			else if (View.REFINED_RULES.equals(property)) {
				return entry.getRefinedSafetyRequirements();
			}
			
			
			else {
				return null;
			}
		}
	  }

	  /**
	   * Modifies the element
	   * 
	   * @param element
	   *            the element
	   * @param property
	   *            the property
	   * @param value
	   *            the value
	   */
	  public void modify(Object element, String property, Object value) {
	    if (element instanceof Item) {
	      element = ((Item) element).getData();
	    }
	    // if ControllerWithPMEntry calls
	    if (ProcessModelValue.class == element.getClass()) {
	    	ProcessModelValue entry = (ProcessModelValue) element;
	    	if (View.COMMENTS.equals(property)) {
	  	      	entry.setComments((String)value);
	  	      	model.setCSComponentComment(entry.getId(), (String) value); 
	    	}
	    }
	    // if ControlActionEntrys calls
	    else if (ControlActionEntry.class == element.getClass()){
	    	ControlActionEntry entry = (ControlActionEntry) element;
	    	if (View.SAFETY_CRITICAL.equals(property)) {
	  	      entry.setSafetyCritical(!(Boolean)entry.getSafetyCritical());
	  	      model.setCASafetyCritical(entry.getId(), entry.getSafetyCritical());
	    	}
	    	else if (View.COMMENTS.equals(property)){
	    		entry.setComments((String) value);
	    		model.setControlActionDescription(entry.getId(), (String)value);
	    	}
	    }
	    
	    else {
	    	ContextTableCombination entry = (ContextTableCombination) element;
	    	if (View.IS_HAZARDOUS.equals(property)) {
		  	      entry.setHazardous((!(Boolean)entry.getGlobalHazardous()));
		    	}
	    	if (View.HAZ_IF_ANYTIME.equals(property)) {
		  	      entry.setHAnytime((!(Boolean)entry.getHAnytime()));
		    	}
	    	if (View.HAZ_IF_EARLY.equals(property)) {
		  	      entry.setHEarly((!(Boolean)entry.getHEarly()));
		    	}
	    	if (View.HAZ_IF_LATE.equals(property)) {
		  	      entry.setHLate((!(Boolean)entry.getHLate()));
		    	}
	    	if (View.REFINED_RULES.equals(property)) {
	    		entry.setRefinedSafetyRequirements((String) value);
	    		
	    	}
	    }


	    // Force the viewer to refresh
	    viewer.refresh();
	    
	  }

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	}