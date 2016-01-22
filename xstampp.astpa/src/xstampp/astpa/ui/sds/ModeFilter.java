package xstampp.astpa.ui.sds;

import org.eclipse.jface.viewers.ViewerFilter;

public abstract class ModeFilter extends ViewerFilter{
	protected static final int both = 0;
	protected static final int uca = 1;
	protected static final int rsc = 2;
	protected int cscFilterMode;
	protected String searchString;

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
	public void setCSCFilterMode(int cscFilterMode) {
		this.cscFilterMode = cscFilterMode;
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
}
