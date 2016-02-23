package xstpa;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;

import xstampp.DefaultPerspective;
import xstpa.ui.View;

public class xstpaPerspective extends DefaultPerspective {

	public static final String ID ="xstpa.perspective"; //$NON-NLS-1$
	@Override
	public void createInitialLayout(IPageLayout layout) {
		super.createInitialLayout(layout);
		IFolderLayout folder = layout.createFolder("xstpafolder", IPageLayout.BOTTOM, 0.65f, layout.getEditorArea());

		folder.addView(View.ID);
		layout.getViewLayout(View.ID).setCloseable(false);
	}

}
