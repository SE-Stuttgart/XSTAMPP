package acast.model.controlstructure;


import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory {

	private static final float LEFT_RATIO = 0.25F;

	private static final float RIGHT_RATIO = 0.75F;

	private static final float BOTTOM_RATIO = 0.75F;

	@Override
	public void createInitialLayout(IPageLayout layout) {

		layout.setFixed(false);
		layout.setEditorAreaVisible(true);
		
		String editorArea = layout.getEditorArea();
		layout.addView(
				"acast.explorer", IPageLayout.LEFT, 0.2f, layout.getEditorArea()); //$NON-NLS-1$
		layout.getViewLayout("acast.explorer").setCloseable(false);


		IFolderLayout folder = layout.createFolder("buttomfolder",
				IPageLayout.BOTTOM, 0.85f, layout.getEditorArea());

		folder.addPlaceholder("A-CAST.view1");


	}
}
