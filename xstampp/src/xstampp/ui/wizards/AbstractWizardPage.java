package xstampp.ui.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import messages.Messages;

import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import xstampp.Activator;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public abstract class AbstractWizardPage extends WizardPage {

	private int backgroundState = 1;
	/**
	 * if a input field is essential for the Export
	 */
	public static final String ESSENTIAL = "*"; //$NON-NLS-1$
	private String projectName;
	private UUID projectID;
	protected static final int DEMOCANVAS_HEIGHT = 60;
	protected static final int COMPONENT_OFFSET = 10;
	private static final String IMAGE_PATH = "/icons/buttons/export"; //$NON-NLS-1$
	protected static final int LABEL_COLUMN = 5;
	protected static final int LABEL_WIDTH = 107;
	protected static final int LABEL_HEIGHT = 25;
	protected static final int TEXT_COLUMN = AbstractWizardPage.LABEL_COLUMN
			+ AbstractWizardPage.LABEL_WIDTH + 1;
	protected static final int BUTTON_COLUMN = 430;
	private int fontState = 0;
	protected final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	/**
	 * creates a page where the name of the project is already known
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param pageName
	 *            the page name
	 * @param projectName
	 *            the name of the project
	 */
	public AbstractWizardPage(String pageName, String projectName) {
		super(pageName);
		this.projectName = projectName;
	}

	/**
	 * creates the page with a banner
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param pageName
	 *            the page name
	 * @param title
	 *            the title which is displayed in the banner
	 * @param titleImage
	 *            the descriptor of the banner image in 64x64
	 */
	public AbstractWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/**
	 * a constructor which creates the page without banner
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param pageName
	 *            the Page Name
	 */
	public AbstractWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * Set different color icons for different color shades.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param rgb
	 *            RGB
	 */
	protected void setLabelIcon(Label lbColorIcon, RGB rgb, int state) {
		String folder = ""; //$NON-NLS-1$
		// Shade lists
		ArrayList<RGB> redShades = new ArrayList<>();
		ArrayList<RGB> blackShades = new ArrayList<>();
		ArrayList<RGB> yellowShades = new ArrayList<>();
		ArrayList<RGB> greenShades = new ArrayList<>();
		ArrayList<RGB> purpleShades = new ArrayList<>();
		ArrayList<RGB> whiteShades = new ArrayList<>();
		ArrayList<RGB> blueShades = new ArrayList<>();
		ArrayList<RGB> grayShades = new ArrayList<>();

		this.initShadeLists(redShades, blackShades, yellowShades, greenShades,
				purpleShades, whiteShades, blueShades, grayShades);

		if (state == this.fontState) {
			folder = "/font"; //$NON-NLS-1$
		} else {
			folder = "/background"; //$NON-NLS-1$
		}

		// set icon
		if (redShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(
					AbstractWizardPage.IMAGE_PATH + folder + "/textRed.ico") //$NON-NLS-1$
					.createImage());
		} else if (blackShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(
					AbstractWizardPage.IMAGE_PATH + folder + "/textBlack.ico") //$NON-NLS-1$
					.createImage());
		} else if (yellowShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(
					AbstractWizardPage.IMAGE_PATH + folder + "/textYellow.ico") //$NON-NLS-1$
					.createImage());
		} else if (greenShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(
					AbstractWizardPage.IMAGE_PATH + folder + "/textGreen.ico") //$NON-NLS-1$
					.createImage());
		} else if (purpleShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(
					AbstractWizardPage.IMAGE_PATH + folder + "/textPurple.ico") //$NON-NLS-1$
					.createImage());
		} else if (whiteShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(
					AbstractWizardPage.IMAGE_PATH + folder + "/textWhite.ico") //$NON-NLS-1$
					.createImage());
		} else if (blueShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(
					AbstractWizardPage.IMAGE_PATH + folder + "/textBlue.ico") //$NON-NLS-1$
					.createImage());
		} else if (grayShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(
					AbstractWizardPage.IMAGE_PATH + folder + "/textGrey.ico") //$NON-NLS-1$
					.createImage());
		}

	}

	/**
	 * Opens color dialog.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param srcBtn
	 *            Button
	 * @param srcText
	 *            Text
	 * @param srcLbl
	 *            Label
	 * @param state
	 *            Integer
	 * @param constant
	 */
	protected void openColorDialog(Button srcBtn, Text srcText, Label srcLbl,
			int state, String constant) {
		ColorDialog colorDialog = new ColorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		colorDialog.setText(Messages.SelectColor);
		RGB selectedColor = colorDialog.open();
		if (selectedColor == null) {
			return;
		}
		PreferenceConverter.setValue(this.store, constant, selectedColor);
		srcText.setText(selectedColor.toString());
		this.setLabelIcon(srcLbl, selectedColor, state);
	}

	/**
	 * Initialize ArrayList with RGB values of color shades.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param redShades
	 *            ArrayList<RGB>
	 * @param blackShades
	 *            ArrayList<RGB>
	 * @param yellowShades
	 *            ArrayList<RGB>
	 * @param greenShades
	 *            ArrayList<RGB>
	 * @param purpleShades
	 *            ArrayList<RGB>
	 * @param whiteShades
	 *            ArrayList<RGB>
	 * @param blueShades
	 *            ArrayList<RGB>
	 * @param grayShades
	 *            ArrayList<RGB>
	 */
	private void initShadeLists(ArrayList<RGB> redShades,
			ArrayList<RGB> blackShades, ArrayList<RGB> yellowShades,
			ArrayList<RGB> greenShades, ArrayList<RGB> purpleShades,
			ArrayList<RGB> whiteShades, ArrayList<RGB> blueShades,
			ArrayList<RGB> grayShades) {

		// RGB values
		final int twoHundredAndFiftyFive = 255;
		final int zero = 0;
		final int sixtyfour = 64;
		final int oneHundredAndTwentyEight = 128;
		// initialize
		if (redShades.size() == 0) {
			redShades.add(new RGB(twoHundredAndFiftyFive,
					oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			redShades.add(new RGB(twoHundredAndFiftyFive, zero, zero));
			redShades.add(new RGB(oneHundredAndTwentyEight, sixtyfour,
					sixtyfour));
			redShades.add(new RGB(oneHundredAndTwentyEight, zero, zero));
			redShades.add(new RGB(sixtyfour, zero, zero));
		}

		if (blackShades.size() == 0) {
			blackShades.add(new RGB(zero, zero, zero));
		}

		if (yellowShades.size() == 0) {
			yellowShades.add(new RGB(oneHundredAndTwentyEight,
					oneHundredAndTwentyEight, zero));
			yellowShades
					.add(new RGB(oneHundredAndTwentyEight, sixtyfour, zero));
			yellowShades.add(new RGB(twoHundredAndFiftyFive,
					oneHundredAndTwentyEight, zero));
			yellowShades.add(new RGB(twoHundredAndFiftyFive,
					oneHundredAndTwentyEight, sixtyfour));
			yellowShades.add(new RGB(twoHundredAndFiftyFive,
					twoHundredAndFiftyFive, zero));
			yellowShades.add(new RGB(twoHundredAndFiftyFive,
					twoHundredAndFiftyFive, oneHundredAndTwentyEight));
		}

		if (greenShades.size() == 0) {
			greenShades.add(new RGB(oneHundredAndTwentyEight,
					twoHundredAndFiftyFive, oneHundredAndTwentyEight));
			greenShades.add(new RGB(oneHundredAndTwentyEight,
					twoHundredAndFiftyFive, zero));
			greenShades.add(new RGB(oneHundredAndTwentyEight,
					oneHundredAndTwentyEight, sixtyfour));
			greenShades.add(new RGB(sixtyfour, oneHundredAndTwentyEight,
					oneHundredAndTwentyEight));
			greenShades.add(new RGB(zero, twoHundredAndFiftyFive, zero));
			greenShades.add(new RGB(zero, oneHundredAndTwentyEight, zero));
			greenShades.add(new RGB(zero, sixtyfour, zero));
			greenShades.add(new RGB(zero, twoHundredAndFiftyFive,
					oneHundredAndTwentyEight));
			greenShades.add(new RGB(zero, twoHundredAndFiftyFive, sixtyfour));
			greenShades.add(new RGB(zero, oneHundredAndTwentyEight,
					oneHundredAndTwentyEight));
			greenShades.add(new RGB(zero, oneHundredAndTwentyEight, sixtyfour));
			greenShades.add(new RGB(zero, sixtyfour, sixtyfour));
		}

		final int onehundredAndNinetyTwo = 192;
		if (purpleShades.size() == 0) {
			purpleShades.add(new RGB(twoHundredAndFiftyFive,
					oneHundredAndTwentyEight, onehundredAndNinetyTwo));
			purpleShades.add(new RGB(twoHundredAndFiftyFive,
					oneHundredAndTwentyEight, twoHundredAndFiftyFive));
			purpleShades.add(new RGB(oneHundredAndTwentyEight,
					oneHundredAndTwentyEight, onehundredAndNinetyTwo));
			purpleShades.add(new RGB(twoHundredAndFiftyFive, zero,
					twoHundredAndFiftyFive));
			purpleShades
					.add(new RGB(oneHundredAndTwentyEight, zero, sixtyfour));
			purpleShades.add(new RGB(twoHundredAndFiftyFive, zero,
					oneHundredAndTwentyEight));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, zero,
					oneHundredAndTwentyEight));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, zero,
					twoHundredAndFiftyFive));
			purpleShades.add(new RGB(sixtyfour, zero, sixtyfour));
			purpleShades
					.add(new RGB(sixtyfour, zero, oneHundredAndTwentyEight));
			purpleShades.add(new RGB(oneHundredAndTwentyEight,
					oneHundredAndTwentyEight, twoHundredAndFiftyFive));
		}

		if (whiteShades.size() == 0) {
			whiteShades.add(new RGB(twoHundredAndFiftyFive,
					twoHundredAndFiftyFive, twoHundredAndFiftyFive));
		}

		final int onehundredAndSixty = 160;
		if (blueShades.size() == 0) {
			blueShades.add(new RGB(oneHundredAndTwentyEight,
					twoHundredAndFiftyFive, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, twoHundredAndFiftyFive,
					twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, sixtyfour, oneHundredAndTwentyEight));
			blueShades.add(new RGB(zero, zero, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, zero, oneHundredAndTwentyEight));
			blueShades.add(new RGB(zero, oneHundredAndTwentyEight,
					twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, oneHundredAndTwentyEight,
					onehundredAndNinetyTwo));
			blueShades.add(new RGB(zero, zero, onehundredAndSixty));
			blueShades.add(new RGB(zero, zero, sixtyfour));
		}

		if (grayShades.size() == 0) {
			grayShades.add(new RGB(oneHundredAndTwentyEight,
					oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			grayShades.add(new RGB(onehundredAndNinetyTwo,
					onehundredAndNinetyTwo, onehundredAndNinetyTwo));
		}

	}

	protected Composite getDestinationChooser(Composite parent) {
		Composite pathComposite = new Composite(parent, SWT.NONE);

		return pathComposite;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the name of the project
	 */
	public String getProjectName() {
		return this.projectName;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param projectName
	 *            the name of the project
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * 
	 * @author Sebastian Sieber,Lukas Balzer
	 * @param filters
	 *            the file extensions, which shall be excepted by in the dialog
	 * @param nameSuggestion suggestion for the name that should be choosen for the file
	 * 
	 */
	protected String openExportDialog(String[] filters, String[] names) {
		FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OPEN);
		fileDialog.setFilterExtensions(filters);
		fileDialog.setFilterNames(names);
		fileDialog.setFileName(this.getProjectName());
		String filePath = fileDialog.open();
		if (filePath != null) {
			return filePath;
		}
		return ""; ////$NON-NLS-1$
	}
	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	protected String openDirectoryDialog() {
		DirectoryDialog fileDialog = new DirectoryDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OPEN);
		fileDialog.setFilterPath(Platform.getInstanceLocation().getURL().getPath().toString());
		String filePath = fileDialog.open();
		if (filePath != null) {
			return filePath;
		}
		return ""; ////$NON-NLS-1$
	}
	/**
	 * 
	 * @author Sebastian Sieber,Lukas Balzer
	 * @param filters
	 *            the file extensions, which shall be excepted by in the dialog
	 */
	protected String openExportDialog(String[] filters) {
		return this.openExportDialog(filters, filters);
	}

	/**
	 * Opens export picture dialog
	 * 
	 * @author Sebastian Sieber
	 * 
	 */
	private String openLogoDialog() {
		final String[] filterNames = {
				"JPEG (*.jpg;*.jpeg;*.jpe;*.jfif)", //$NON-NLS-1$
				"PNG (*.png)", "TIFF (*.tif;*.tiff)", "GIF (*.gif)", "Bitmap (*.bmp;*.dib)", "SVG (*.svg;*.svgz)" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		final String[] filterExtensions = {
				"*.jpg", "*.png", "*.jpe", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"*.jfif", "*.jpeg", "*.tif", "*.tiff", "*.gif", "*.bmp", "*.dib", "*.svg", "*.svgz" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
		FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OPEN);
		dlg.setFilterExtensions(filterExtensions);
		dlg.setFilterNames(filterNames);
		dlg.setText(Messages.Open);
		String path = dlg.open();
		if (path == null) {
			return ""; //$NON-NLS-1$
		}
		if (path.contains(".")) { //$NON-NLS-1$
			String ext = "*" + path.substring(path.lastIndexOf('.')); //$NON-NLS-1$
			boolean supported = false;
			for (String filterExtension : filterExtensions) {
				if (filterExtension.equalsIgnoreCase(ext)) {
					supported = true;
					break;
				}
			}
			if (supported && new File(path).exists()) {
				return path;
			}
			MessageDialog.openWarning(this.getShell(), Messages.Warning,
					Messages.FileFormatNotSupported);
		}
		return ""; //$NON-NLS-1$
	}


	/**
	 * @return the fontState
	 */
	public int getFontState() {
		return this.fontState;
	}

	/**
	 * @param fontState
	 *            the fontState to set
	 */
	public void setFontState(int fontState) {
		this.fontState = fontState;
	}

	/**
	 * @return the backgroundState
	 */
	public int getBackgroundState() {
		return this.backgroundState;
	}

	/**
	 * @param backgroundState
	 *            the backgroundState to set
	 */
	public void setBackgroundState(int backgroundState) {
		this.backgroundState = backgroundState;
	}

	/**
	 * checks if the page is finished
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return if the page is finished
	 */
	public abstract boolean checkFinish();

	/**
	 * @return the id for the currently selected project or null if the chooser
	 *         has not be instantiated
	 */
	public UUID getProjectID() {
		return this.projectID;
	}

	/**
	 * @param projectID the projectID to set
	 */
	public void setProjectID(UUID projectID) {
		
		this.projectID = projectID;
		setProjectName(ProjectManager.getContainerInstance().getDataModel(projectID).getProjectName());
	}
	
	/**
	 * a class which can be used by every child to add a Comosite to the editor page with which the user can
	 * than choose a file path 
	 *
	 * @author Lukas Balzer
	 *
	 */
	protected final class PathComposite extends Composite implements SelectionListener{

		public static final int LOGO_DIALOG = 1;
		public static final int PATH_DIALOG = 2;
		public static final int DIR_DIALOG = 3;
		private Button pathButton;
		private final Text path;
		private final Label labelExport;
		private String[] filter;
		private String[] filterNames;
		private int dialogStyle;

		/**
		 * This constructor constructs a Composite which contains a basic ui for
		 * choosing a path
		 * 
		 * @author Lukas Balzer
		 * @param parent
		 *            a widget which will be the parent of the new instance
		 *            (cannot be null)
		 * @param name
		 *            the name which specifies the path to be chosen
		 * 
		 * @see Composite#Composite(Composite, int)
		 */
		public PathComposite(Composite parent, String name) {
			super(parent, SWT.NONE);
			this.setLayout(null);
			this.labelExport = new Label(this, SWT.NONE);
			this.labelExport.setText(name);
			this.labelExport.setBounds(AbstractWizardPage.LABEL_COLUMN, 0,
					AbstractWizardPage.LABEL_WIDTH, 15);

			this.path = new Text(this, SWT.BORDER | SWT.SINGLE);
			this.path.setBounds(AbstractWizardPage.TEXT_COLUMN, 0, 297, 21);
			this.path.setText(""); //$NON-NLS-1$
			this.path.setEditable(false);

			this.pathButton = new Button(this, SWT.NONE);
			this.pathButton.setBounds(AbstractWizardPage.BUTTON_COLUMN, 0, 42,
					25);
			this.pathButton.setText("..."); //$NON-NLS-1$

		}
		/**
		 * calls {@link #AbstractWizardPage(String[], String[], Composite, int, String)} 
		 * with <code>Messages.Destination</code> as name 
		 *
		 * @author Lukas Balzer
		 *
		 * @param filter a string array containing the filter extensionsBsp.: {"*.haz","*.hazx"}
		 * @param parent the parent composite
		 * @param style the style, must be one of the constants defined in PathComposite
		 */
		public PathComposite(final String[] filter, Composite parent, int style) {
			this(filter, filter, parent, style, Messages.Destination);
		}

		/**
		 * calls {@link #AbstractWizardPage(String[], String[], Composite, int, String)} 
		 * with <code>Messages.Destination</code> as name 
		 *
		 * @author Lukas Balzer
		 *
		 * @param filter a string array containing the filter extensionsBsp.: {"*.haz","*.hazx"}
		 * @param parent the parent composite
		 * @param style the style, must be one of the constants defined in PathComposite
		 * @param filterNames a string array containing a description of the extension
		 */
		public PathComposite(final String[] filter, final String[] filterNames,
				Composite parent, int style) {
			this(filter, filterNames, parent, style, Messages.Destination);
		}

		/**
		 * 
		 *
		 * @author Lukas
		 *
		 * @param filter a string array containing the filter extensionsBsp.: {"*.haz","*.hazx"}
		 * @param filterNames a string array containing a description of the extension
		 * @param parent the parent composite
		 * @param style the style, must be one of the constants defined in PathComposite
		 * @param name the name which specifies the path to be chosen
		 */
		public PathComposite(String[] filter, String[] filterNames,
				Composite parent, int style, String name) {
			this(parent, name);
			this.addButtonListener(this);
			this.filter = filter;
			this.filterNames = filterNames;
			this.dialogStyle = style;
		}

		
		public void addButtonListener(SelectionListener listener) {
			this.pathButton.addSelectionListener(listener);
		}

		public String getText() {
			return this.path.getText();
		}

		public Text getTextWidget() {
			return this.path;
		}

		public void setText(String text) {
			this.path.setText(text);
		}

		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			this.path.setEnabled(enabled);
			this.pathButton.setEnabled(enabled);
			this.labelExport.setEnabled(enabled);
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (this.dialogStyle == PathComposite.LOGO_DIALOG) {
				PathComposite.this.path.setText(AbstractWizardPage.this
						.openLogoDialog());
			} else if(this.dialogStyle == PathComposite.PATH_DIALOG) {
				PathComposite.this.path.setText(AbstractWizardPage.this
						.openExportDialog(this.filter, this.filterNames));
			} else if(this.dialogStyle == PathComposite.DIR_DIALOG) {
				PathComposite.this.path.setText(AbstractWizardPage.this
						.openDirectoryDialog());
			}
			AbstractWizardPage.this
					.setPageComplete(AbstractWizardPage.this
							.checkFinish());
		}

	}

	protected final class ColorChooser extends Composite {

		private Text colorText;
		private Button colorButton;
		private Label colorIcon;

		/**
		 * This constructs a Composite containing Elements for choosing a color
		 * and storing the value in a preference store
		 * 
		 * @author Lukas Balzer
		 * 
		 * @param parent
		 *            a widget which will be the parent of the new instance
		 *            (cannot be null)
		 * @param style
		 *            the style of widget to construct
		 * @param text
		 *            the text which shall be displayed in the label
		 * @param constant
		 *            The IPreferenceConstants to which the color is linked in
		 *            the preference store
		 * 
		 * @see Composite#Composite(Composite, int)
		 */
		public ColorChooser(Composite parent, int style, String text,
				final String constant) {
			super(parent, style);
			Label labelBackgroundColor = new Label(this, SWT.NONE);
			labelBackgroundColor.setText(text);
			labelBackgroundColor.setBounds(AbstractWizardPage.LABEL_COLUMN, 0,
					AbstractWizardPage.LABEL_WIDTH, 15);

			// create the Text which says what color is selected at the moment
			this.colorText = new Text(this, SWT.BORDER);
			this.colorText.setBounds(AbstractWizardPage.TEXT_COLUMN + 30, 0,
					268, 21);
			this.colorText.setEditable(false);
			String companyBackgroundColor = PreferenceConverter.getColor(
					AbstractWizardPage.this.store, constant).toString();

			if (companyBackgroundColor != null) {
				this.colorText.setText(companyBackgroundColor);
				this.colorText.setSelection(companyBackgroundColor.length());
			} else {
				this.colorText.setText(""); //$NON-NLS-1$
			}

			this.colorIcon = new Label(this, SWT.NONE);
			this.colorIcon.setBounds(AbstractWizardPage.TEXT_COLUMN, 0, 23, 23);
			AbstractWizardPage.this.setLabelIcon(this.colorIcon,
					PreferenceConverter.getColor(AbstractWizardPage.this.store,
							constant), 1);

			this.colorButton = new Button(this, SWT.NONE);
			this.colorButton.setText("..."); //$NON-NLS-1$
			this.colorButton.setBounds(AbstractWizardPage.BUTTON_COLUMN, 0, 42,
					25);
			this.colorButton.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					AbstractWizardPage.this.openColorDialog(
							ColorChooser.this.colorButton,
							ColorChooser.this.colorText,
							ColorChooser.this.colorIcon, 1, constant);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// no-op
				}
			});
		}

		/**
		 * @return the colorText
		 */
		public String getColorString() {
			return this.colorText.getText();
		}

		public void addColorChangeListener(ModifyListener listener) {
			this.colorText.addModifyListener(listener);
		}
	}




}
