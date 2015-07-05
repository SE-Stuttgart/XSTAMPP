package testplugin;

import org.apache.log4j.Logger;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.internal.Perspective;

import xstampp.DefaultPerspective;

public class TestPerspective implements IPerspectiveFactory {
	private static final Logger LOGGER = Logger.getRootLogger();

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);
		IFolderLayout consoleFolder = layout.createFolder("folder", 
	            IPageLayout.BOTTOM, 0.75f,  layout.getEditorArea());
		IFolderLayout naviFolder = layout.createFolder("navi", 
	            IPageLayout.LEFT, 0.3f,  layout.getEditorArea());
		
		naviFolder.addView("astpa.explorer");

		consoleFolder.addView("astpa.Welcome");
	
	}
}
