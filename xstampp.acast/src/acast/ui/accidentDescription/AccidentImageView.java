package acast.ui.accidentDescription;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import acast.model.interfaces.IAccidentDescriptionViewDataModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.STPAEditorInput;
import xstampp.ui.editors.StandartEditorPart;

public class AccidentImageView extends EditorPart {

	/**
	 * ViewPart ID.
	 */
	public static final String ID = "acast.steps.step1_1_image";

	private boolean pictureLoaded = false;
	private Label accidentImage;
	private ImageData imageData;

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new GridLayout(1, false));
		Composite composite_AccidentImage = new Composite(parent, SWT.BORDER);
		composite_AccidentImage.setLayout(new GridLayout(1, false));
		GridData accidentDescriptionData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		composite_AccidentImage.setLayoutData(accidentDescriptionData);
		accidentImage = new Label(composite_AccidentImage, SWT.FILL);
		accidentImage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		ImageInput i = (ImageInput) this.getEditorInput();
		File f = new File(i.getPath());
		byte[] uploadedImg = null;
		try {
			double fileLen = f.length();
			uploadedImg = new byte[(int) fileLen];
			FileInputStream inputStream = new FileInputStream(f.getAbsolutePath());
			int nRead = 0;
			while ((nRead = inputStream.read(uploadedImg)) != -1) {
			}
			inputStream.close();

		} catch (Exception e2) {
		}

		BufferedInputStream inputStreamReader = new BufferedInputStream(new ByteArrayInputStream(uploadedImg));
		imageData = new ImageData(inputStreamReader);
		accidentImage.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				if (!pictureLoaded) {
					Image image = new Image(Display.getCurrent(),
							imageData.scaledTo(accidentImage.getBounds().width, accidentImage.getBounds().height));
					if (image != null) {
						accidentImage.setImage(image);
					}
					pictureLoaded = true;
				}
			}

			@Override
			public void controlMoved(ControlEvent e) {

			}
		});

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
