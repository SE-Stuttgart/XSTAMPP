package acast.wizards.pages;

import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.wizards.AbstractExportPage;
import xstampp.ui.wizards.AbstractWizardPage;
import acast.Activator;

/**
 * Create export page.
 *
 * @author Sebastian Sieber, Lukas Balzer
 *
 */
public class PdfExportPage extends AbstractExportPage implements ModifyListener {

	private static final int ENTRY_HEIGTH = 25;
	private Composite container;
	private Text textCompany;
	private ColorChooser bgChooser, fontChooser;
	private DemoCanvas sampleComp;
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	private Button decoSwitch;
	private PathComposite logoComposite;
	private int pathConstant;

	/**
	 * Constructor.
	 *
	 *
	 * @param pageName
	 *            the name of the page
	 * @param projectName
	 *            the name of the project
	 * @param pathConstant
	 */
	public PdfExportPage(String pageName, String projectName) {
		this(pageName, projectName, PathComposite.PATH_DIALOG);
	}

	/**
	 * Constructor.
	 *
	 *
	 * @param pageName
	 *            the name of the page
	 * @param projectName
	 *            the name of the project
	 * @param pathConstant
	 */
	public PdfExportPage(String pageName, String projectName, int pathConstant) {
		super(pageName, Activator.PLUGIN_ID);
		this.setTitle(Messages.Export);
		this.setDescription(Messages.SetValuesForTheExportFile);
		this.pathConstant = pathConstant;
	}

	@Override
	public void createControl(Composite parent) {
		FormData data;
		this.container = new Composite(parent, SWT.NONE);
		this.container.setLayout(new FormLayout());

		Composite projectChooser = this.addProjectChooser(this.container,
				new FormAttachment(null, AbstractWizardPage.COMPONENT_OFFSET), 0, 0);
		// -----Create the Company Name input Composite-------------------------
		Composite labelComposite = new Composite(this.container, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(projectChooser, AbstractWizardPage.COMPONENT_OFFSET);
		data.height = ENTRY_HEIGTH;
		labelComposite.setLayoutData(data);
		labelComposite.setLayout(null);
		Label labelCompany = new Label(labelComposite, SWT.SHADOW_IN);
		labelCompany.setBounds(0, 0, 55, 15);
		labelCompany.setText(Messages.Company);

		this.textCompany = new Text(labelComposite, SWT.BORDER | SWT.SINGLE);
		this.textCompany.setBounds(AbstractWizardPage.TEXT_COLUMN, 0, 345, 21);

		String companyName = this.store.getString(IPreferenceConstants.COMPANY_NAME);

		if (companyName != null) {
			this.textCompany.setText(companyName);
			this.textCompany.setSelection(companyName.length());
		} else {
			this.textCompany.setText(""); //$NON-NLS-1$
		}

		// ----Create the logo path chooser composite---------------------------
		this.logoComposite = new PathComposite(null, null, this.container, PathComposite.LOGO_DIALOG, Messages.Logo);
		String logoName = this.store.getString(IPreferenceConstants.COMPANY_LOGO);

		if (logoName != null) {
			this.textCompany.setText(logoName);
		}
		data = new FormData();
		data.top = new FormAttachment(labelComposite, AbstractWizardPage.COMPONENT_OFFSET);
		data.height = ENTRY_HEIGTH;
		this.logoComposite.setLayoutData(data);
		this.logoComposite.setVisible(true);

		// ----Create the background color chooser
		// composite---------------------------
		this.bgChooser = new ColorChooser(this.container, SWT.NONE, Messages.BackgroundColor,
				IPreferenceConstants.COMPANY_BACKGROUND_COLOR);
		data = new FormData();
		data.top = new FormAttachment(this.logoComposite, AbstractWizardPage.COMPONENT_OFFSET);
		data.height = ENTRY_HEIGTH;
		this.bgChooser.setLayoutData(data);
		this.bgChooser.setVisible(true);
		this.bgChooser.addColorChangeListener(this);

		// ----Create the foreground color chooser
		// composite---------------------------
		this.fontChooser = new ColorChooser(this.container, SWT.NONE, Messages.FontColor,
				IPreferenceConstants.COMPANY_FONT_COLOR);
		data = new FormData();
		data.top = new FormAttachment(this.bgChooser, AbstractWizardPage.COMPONENT_OFFSET);
		data.height = ENTRY_HEIGTH;
		this.fontChooser.setLayoutData(data);
		this.fontChooser.setVisible(true);
		this.fontChooser.addColorChangeListener(this);

		// ----Create the path chooser composite---------------------------
		this.pathChooser = new PathComposite(new String[] { "*.pdf" }, //$NON-NLS-1$
				new String[] { "A-CAST Report *.pdf" }, //$NON-NLS-1$
				this.container, this.pathConstant);
		data = new FormData();
		data.top = new FormAttachment(this.fontChooser, AbstractWizardPage.COMPONENT_OFFSET);
		data.height = ENTRY_HEIGTH;
		this.pathChooser.setLayoutData(data);
		this.pathChooser.setVisible(true);

		// ----Creates a Composite for the switch which turns the decoration of
		// the control structure on/off
		Composite decoSwitchComposite = new Composite(this.container, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(this.pathChooser, AbstractWizardPage.COMPONENT_OFFSET);
		decoSwitchComposite.setLayoutData(data);
		decoSwitchComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label decoLabel = new Label(decoSwitchComposite, SWT.NONE);
		decoLabel.setText(Messages.ControlStructureDeco);
		decoLabel.setToolTipText(Messages.CSDecoToolTip);
		decoLabel.setLayoutData(new RowData(160, AbstractWizardPage.LABEL_HEIGHT));
		this.decoSwitch = new Button(decoSwitchComposite, SWT.CHECK);

		// ----Creates a Composite for a Canvas which provides a preview of the
		// projectname with the chosen fore-/background colors
		this.sampleComp = new DemoCanvas(this.container, SWT.NONE);
		data = new FormData();
		data.top = new FormAttachment(decoSwitchComposite, AbstractWizardPage.COMPONENT_OFFSET);
		data.height = AbstractWizardPage.DEMOCANVAS_HEIGHT;
		data.width = parent.getBounds().width;
		this.sampleComp.setLayoutData(data);

		// Required to avoid an error in the system
		this.setControl(this.container);

	}

	/**
	 * @return the textCompany
	 */
	public Text getTextCompany() {
		return this.textCompany;
	}

	/**
	 * @param textCompany
	 *            sets the given value
	 */
	public void setTextCompany(Text textCompany) {
		this.textCompany = textCompany;
	}

	/**
	 * @return path of the Logo img
	 */
	public String getTextLogo() {
		return this.logoComposite.getText();
	}

	/**
	 * @return the backgroundColor as an rgb string
	 */
	public String getTextBackgroundColor() {
		return this.bgChooser.getColorString();
	}

	/**
	 * @return fontColor as an rgb string
	 */
	public String getTextFontColor() {
		return this.fontChooser.getColorString();
	}

	@Override
	public boolean asOne() {
		return false;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		this.sampleComp.redraw();
	}

	/**
	 *
	 * @return if the structure is being decorated
	 */
	public boolean getDecoChoice() {
		return this.decoSwitch.getSelection();
	}

	@Override
	public void setProjectID(UUID projectID) {
		super.setProjectID(projectID);
		if (this.sampleComp != null) {
			this.sampleComp.setProjectID(projectID);
		}
	}

}
