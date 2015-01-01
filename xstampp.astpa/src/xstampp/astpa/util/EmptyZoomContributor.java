package xstampp.astpa.util;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.ZoomManager;

public class EmptyZoomContributor implements IZoomContributor {

	public EmptyZoomContributor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateZoom(double zoom) {
		// TODO Auto-generated method stub

	}

	@Override
	public ZoomManager getZoomManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPropertyListener(PropertyChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDecoration(boolean deco) {
		// TODO Auto-generated method stub

	}

	@Override
	public void firePropertyChange(String property, boolean value) {
		// TODO Auto-generated method stub

	}

}
