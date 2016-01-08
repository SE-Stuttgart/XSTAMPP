package xstpa.ui.tables.utils;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import xstpa.model.ControlActionEntry;
import xstpa.model.ProcessModelValue;
import xstpa.model.ProcessModelVariables;

/**
 * this private class returns the content for the dependencies view 
 * which is devided into four tables which have different entry types
 *
 */
public class DependencyViewLabelProvider extends LabelProvider implements
		ITableLabelProvider{
	
	public String getColumnText(Object element, int columnIndex) {
		
		if (element.getClass() == ProcessModelValue.class) {
			
			ProcessModelValue entry = (ProcessModelValue) element;
		    switch (columnIndex) {
		    case 0:
		      return entry.getController();
		    case 1:
		      return entry.getPMV();
		    }
		    return null;
		}
		else if (element.getClass() == ControlActionEntry.class) {
			ControlActionEntry entry = (ControlActionEntry) element;
			switch (columnIndex) {
			case 0:
				return String.valueOf(entry.getNumber());
			case 1:
					
				return entry.getControlAction();
					
			}
			return null;
		}
		
		else if (element.getClass() == ProcessModelVariables.class) {
			ProcessModelVariables entry = (ProcessModelVariables) element;
			switch (columnIndex) {
			case 0:
				return String.valueOf(entry.getNumber());
			case 1:
				return entry.getName();
			}
		}
		else {
			ProcessModelVariables entry = (ProcessModelVariables) element;
			switch (columnIndex) {
			case 0:
				return null;
			case 1:
				return entry.getName();
			}
		}
		return null;
	}

	public Image getColumnImage(Object obj, int index) {			
		return getImage(obj);		
	}
	


	public Image getImage(Object obj) {
        return null;
	}
}
