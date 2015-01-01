package xstampp.astpa.util;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.ZoomManager;

public interface IZoomContributor {

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
	void setDecoration(boolean deco);

	void firePropertyChange(String property, boolean value);
}