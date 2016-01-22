package xstpa.ui;

import org.eclipse.jface.viewers.Viewer;

import xstampp.astpa.ui.sds.ModeFilter;
import xstpa.model.RefinedSafetyEntry;

public class RefinedEntryFilter extends ModeFilter{


	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if ((this.searchString == null) || (this.searchString.length() == 0)) {
			return true;
		}
		if(element instanceof RefinedSafetyEntry){
			RefinedSafetyEntry entry = (RefinedSafetyEntry) element;
			if(String.valueOf(entry.getNumber()).matches(searchString)){
				return true;
			}
			switch(this.cscFilterMode){
				case both:
				case rsc:
					if(entry.getRefinedRule().toLowerCase().matches(searchString)){
						return true;
					}
					if(this.cscFilterMode != both){
						break;
					}
				case uca:
					if(entry.getRefinedUCA().toLowerCase().matches(searchString)){
						return true;
					}
					
			}
		}
		
		return false;
	}

}
