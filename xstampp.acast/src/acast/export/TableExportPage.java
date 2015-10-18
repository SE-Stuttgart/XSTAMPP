package acast.export;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import acast.Activator;
import messages.Messages;
import xstampp.preferences.IPreferenceConstants;
import xstampp.util.AbstractExportPage;
import xstampp.util.AbstractWizardPage;

/**
 * Creates a Page which collects basic informations about formatting the table
 * Export
 *
 *
 */
public class TableExportPage extends AbstractExportPage implements ModifyListener {
	private Composite control;
	private Button singlePage;
	private Button multiPage;
	private final String[] filters;
	private DemoCanvas sampleCanvas;

	/**
	 *
	 * @param filters
	 *            the file extensions, which shall be excepted by in the dialog
	 * @param pageName
	 *            the Name of this page, that is displayed in the header of the
	 *            wizard
	 */
	public TableExportPage(String[] filters, String pageName) {
		super(pageName, Activator.PLUGIN_ID);
		this.setTitle(pageName);
		this.filters = filters;
		this.setDescription(Messages.SetValuesForTheExportFile);
	}

	@Override
	public void createControl(Composite parent) {
		this.control = new Composite(parent, SWT.NONE);
		this.control.setLayout(new FormLayout());

		Composite projectChooser = this.addProjectChooser(this.control,
				new FormAttachment(null, AbstractWizardPage.COMPONENT_OFFSET));
		FormData data;

		ColorChooser bgChooser = new ColorChooser(this.control, SWT.NONE, Messages.BackgroundColor,
				IPreferenceConstants.COMPANY_BACKGROUND_COLOR);
		data = new FormData();
		data.top = new FormAttachment(projectChooser, AbstractWizardPage.COMPONENT_OFFSET);
		bgChooser.setLayoutData(data);
		bgChooser.addColorChangeListener(this);

		ColorChooser fontChooser = new ColorChooser(this.control, SWT.NONE, Messages.FontColor,
				IPreferenceConstants.COMPANY_FONT_COLOR);
		data = new FormData();
		data.top = new FormAttachment(bgChooser, AbstractWizardPage.COMPONENT_OFFSET);
		fontChooser.setLayoutData(data);
		fontChooser.addColorChangeListener(this);

		this.pathChooser = new PathComposite(this.filters, this.control, PathComposite.PATH_DIALOG);
		data = new FormData();
		data.top = new FormAttachment(fontChooser, AbstractWizardPage.COMPONENT_OFFSET);
		this.pathChooser.setLayoutData(data);

		this.sampleCanvas = new DemoCanvas(this.control, SWT.NONE);
		this.sampleCanvas.setProjectID(this.getProjectID());
		data = new FormData();
		data.width = parent.getBounds().width;
		data.height = AbstractWizardPage.DEMOCANVAS_HEIGHT;
		data.top = new FormAttachment(this.pathChooser, AbstractWizardPage.COMPONENT_OFFSET);
		this.sampleCanvas.setLayoutData(data);
		// Required to avoid an error in the system
		this.setControl(this.control);

	}

	@Override
	public boolean asOne() {
		return this.singlePage.getSelection();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		this.sampleCanvas.redraw();
	}

	@Override
	public void setProjectID(UUID projectID) {
		super.setProjectID(projectID);
		if (this.sampleCanvas != null) {
			this.sampleCanvas.setProjectID(projectID);
		}
	}
}
