package xstpa.ui;

import org.eclipse.jface.viewers.Viewer;

import xstampp.astpa.ui.sds.ModeFilter;
import xstampp.model.ILTLProvider;

public class RefinedEntryFilter extends ModeFilter{


	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if ((this.searchString == null) || (this.searchString.equals(".*.*"))) {
			return true;
		}
		if(element instanceof ILTLProvider){
			ILTLProvider entry = (ILTLProvider) element;
			if(String.valueOf(entry.getNumber()).matches(searchString)){
				return true;
			}
			if(String.valueOf(entry.getNumber()).matches(searchString)){
				return true;
			}
			switch(this.cscFilterMode){
				case both:
				case rsc:
					if(entry.getSafetyRule().toLowerCase().matches(searchString)){
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
