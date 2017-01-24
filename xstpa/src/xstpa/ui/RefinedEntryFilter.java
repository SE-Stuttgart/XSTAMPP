package xstpa.ui;

import messages.Messages;

import org.eclipse.jface.viewers.Viewer;

import xstampp.astpa.ui.sds.ModeFilter;
import xstampp.model.AbstractLTLProvider;

public class RefinedEntryFilter extends ModeFilter{
	private static final String[] FILTERS = new String[]{Messages.All, "Refined Unsafe Control Actions", "Refined Safety Constraints"};  	
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
