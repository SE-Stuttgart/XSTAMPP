package xstpa;

import java.util.List;
import java.util.UUID;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.STPAEditorInput;

class EntryCellModifier implements ICellModifier {
	  private Viewer viewer;
	  private View view;
	  public EntryCellModifier(Viewer viewer) {
	    this.viewer = viewer;
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
		if (View.CWPMCLASS == element.getClass().getName()) {
			ControllerWithPMEntry entry = (ControllerWithPMEntry) element;
			if (View.COMMENTS.equals(property)) {

				return entry.getComments();
			}

			else {
				return null;
			}
		}
		// if Control ActionEntry calls
		else if (View.CAECLASS == element.getClass().getName()){
			ControlActionEntrys entry = (ControlActionEntrys) element;
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
			if (View.HAZARDOUS.equals(property)) {
		      return Boolean.valueOf(entry.getHazardous());
			}
			
			else if (View.HANYTIME.equals(property)) {
			      return Boolean.valueOf(entry.getHAnytime());
				}
			
			else if (View.HEARLY.equals(property)) {
			      return Boolean.valueOf(entry.getHEarly());
				}
			
			else if (View.HLATE.equals(property)) {
			      return Boolean.valueOf(entry.getHLate());
				}
			else if (View.REFINED_SAFETY.equals(property)) {
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
	    if (View.CWPMCLASS == element.getClass().getName()) {
	    	ControllerWithPMEntry entry = (ControllerWithPMEntry) element;
	    	if (View.COMMENTS.equals(property)) {
	  	      	entry.setComments((String)value);
	  	      	View.model.setCSComponentComment(entry.getId(), (String) value); 
	  	      	//View.model.setCSComponentComment(entry.getId(), (String) value);      
	  	    //System.out.println(entry.getId());
	  	      //View.model.getControlActionU(controlActionId).setControlActionDescription(entry.getId(), (String)value);
	    	}
	    }
	    // if ControlActionEntrys calls
	    else if (View.CAECLASS == element.getClass().getName()){
	    	ControlActionEntrys entry = (ControlActionEntrys) element;
	    	if (View.SAFETY_CRITICAL.equals(property)) {
	  	      entry.setSafetyCritical(!(Boolean)entry.getSafetyCritical());
	  	      View.model.setCASafetyCritical(entry.getId(), entry.getSafetyCritical());
	    	}
	    	else if (View.COMMENTS.equals(property)){
	    		entry.setComments((String) value);
	    		View.model.setControlActionDescription(entry.getId(), (String)value);
	    	}
	    }
	    
	    else {
	    	ProcessModelVariables entry = (ProcessModelVariables) element;
	    	if (View.HAZARDOUS.equals(property)) {
		  	      entry.setHazardous((!(Boolean)entry.getHazardous()));
		    	}
	    	if (View.HANYTIME.equals(property)) {
		  	      entry.setHAnytime((!(Boolean)entry.getHAnytime()));
		    	}
	    	if (View.HEARLY.equals(property)) {
		  	      entry.setHEarly((!(Boolean)entry.getHEarly()));
		    	}
	    	if (View.HLATE.equals(property)) {
		  	      entry.setHLate((!(Boolean)entry.getHLate()));
		    	}
	    	if (View.REFINED_SAFETY.equals(property)) {
	    		entry.setRefinedSafetyRequirements((String) value);
	    		//View.setRefinedSafetyCanBeStored(true);
	    		view.storeRefinedSafety();
	    		
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