package xstampp.astpa.util;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.ZoomManager;

public interface IZoomContributor {

	/**
	 * constant used for propargating the deco state of the graphical editor content
	 * @author Lukas Balzer
	 */
	public static final String IS_DECORATED="decorated"; //$NON-NLS-1$
	/**
	 * 
	 *
	 * @author Lukas Balzer
	 *
	 * @param zoom the new zoom
	 */
	void updateZoom(double zoom);
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the zoom manager
	 */
	ZoomManager getZoomManager();
	
	void addPropertyListener(PropertyChangeListener listener);

	void fireToolPropertyChange(String property, Object value);
	Object getProperty(String propertyString);
}