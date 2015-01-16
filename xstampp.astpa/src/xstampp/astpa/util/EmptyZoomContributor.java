package xstampp.astpa.util;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.ZoomManager;

/**
 * An empty implementation of IZoomContributor serving as a null-object
 * in case that the activeView is no IZoomContributor
 *
 * @author Lukas Balzer
 *
 */
public class EmptyZoomContributor implements IZoomContributor {

	public EmptyZoomContributor() {
		// null-object
	}

	@Override
	public void updateZoom(double zoom) {
		// null-object

	}

	@Override
	public ZoomManager getZoomManager() {
		// null-object
		return null;
	}

	@Override
	public void addPropertyListener(PropertyChangeListener listener) {
		// null-object

	}



	@Override
	public void fireToolPropertyChange(String property, Object value) {
		// null-object

	}

	@Override
	public Object getProperty(String propertyString) {
		if(propertyString.equals(IS_DECORATED)){
			return false;
		}
		return null;
	}

}
