package xstpa;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.setFixed(false);

		
		
		layout.addView("xstpa.view", IPageLayout.TOP, 0.5f, layout.getEditorArea());
		layout.addView("astpa.explorer", IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		//IViewLayout vLayout = layout.getViewLayout(View.ID);
		//vLayout.setCloseable(false);
		
	}

}
