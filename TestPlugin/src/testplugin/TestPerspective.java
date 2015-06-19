package testplugin;

import org.apache.log4j.Logger;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.internal.Perspective;

import xstampp.DefaultPerspective;

public class TestPerspective implements IPerspectiveFactory {
	private static final Logger LOGGER = Logger.getRootLogger();

	@Override
	public void createInitialLayout(IPageLayout layout) {
		TestPerspective.LOGGER.debug("Setup Test perspective"); //$NON-NLS-1$
		layout.setFixed(false);
		layout.setEditorAreaVisible(true);
	
	}
}
