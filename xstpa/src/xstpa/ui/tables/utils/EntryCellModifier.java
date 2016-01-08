package xstpa.ui.tables.utils;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import xstampp.astpa.model.DataModelController;
import xstpa.model.ControlActionEntry;
import xstpa.model.ProcessModelValue;
import xstpa.model.ProcessModelVariables;
import xstpa.ui.View;

public class EntryCellModifier implements ICellModifier {
	  private Viewer viewer;
	  private View view;
	private DataModelController model;
	  
	  public EntryCellModifier(Viewer viewer,DataModelController controller) {
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
			ProcessModelVariables entry = (ProcessModelVariables) element;
			if (View.IS_HAZARDOUS.equals(property)) {
		      return Boolean.valueOf(entry.getHazardous());
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
	  	      	//View.model.setCSComponentComment(entry.getId(), (String) value);      
	  	    //System.out.println(entry.getId());
	  	      //View.model.getControlActionU(controlActionId).setControlActionDescription(entry.getId(), (String)value);
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
	    	ProcessModelVariables entry = (ProcessModelVariables) element;
	    	if (View.IS_HAZARDOUS.equals(property)) {
		  	      entry.setHazardous((!(Boolean)entry.getHazardous()));
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