package xstpa.ui;

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
