package astpa.export;

import java.util.ArrayList;

import messages.Messages;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import astpa.Activator;
import astpa.export.pages.IExportPage;
import astpa.preferences.IPreferenceConstants;

/**
 *a class to prevent code cloning in the export Pages
 *
 * @author Lukas Balzer
 *
 */
public abstract class AbstractExportPage extends WizardPage implements IExportPage{
	
	/**
	 * if a input field is essential for the Export
	 */
	public static final String ESSENTIAL="*"; //$NON-NLS-1$
	private String projectName;
	protected static final int DEMOCANVAS_HEIGHT=60;
	protected static final int COMPONENT_OFFSET = 10;
	private static final String IMAGE_PATH = "/icons/buttons/export"; //$NON-NLS-1$
	protected static final int LABEL_COLUMN= 5;
	protected static final int LABEL_WIDTH = 107;
	protected static final int TEXT_COLUMN= LABEL_COLUMN + LABEL_WIDTH+1;
	protected static final int BUTTON_COLUMN= 430;
	private int fontState = 0;
	
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();



	private int backgroundState = 1;
	

	protected AbstractExportPage(String pageName, String projectName) {
		super(pageName);
		this.projectName=projectName;
	}
	/**
	 * Set different color icons for different color shades.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param rgb RGB
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
		
		this.initShadeLists(redShades, blackShades, yellowShades, greenShades, purpleShades, whiteShades, blueShades,
			grayShades);
		
		if (state == this.fontState) {
			folder = "/font"; //$NON-NLS-1$
		} else {
			folder = "/background"; //$NON-NLS-1$
		}
		
		// set icon
		if (redShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(IMAGE_PATH + folder + "/textRed.ico") //$NON-NLS-1$
				.createImage());
		} else if (blackShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(IMAGE_PATH + folder + "/textBlack.ico") //$NON-NLS-1$
				.createImage());
		} else if (yellowShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(IMAGE_PATH + folder + "/textYellow.ico") //$NON-NLS-1$
				.createImage());
		} else if (greenShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(IMAGE_PATH + folder + "/textGreen.ico") //$NON-NLS-1$
				.createImage());
		} else if (purpleShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(IMAGE_PATH + folder + "/textPurple.ico") //$NON-NLS-1$
				.createImage());
		} else if (whiteShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(IMAGE_PATH + folder + "/textWhite.ico") //$NON-NLS-1$
				.createImage());
		} else if (blueShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(IMAGE_PATH + folder + "/textBlue.ico") //$NON-NLS-1$
				.createImage());
		} else if (grayShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(IMAGE_PATH + folder + "/textGrey.ico") //$NON-NLS-1$
				.createImage());
		}
		
	}
	
	

	/**
	 * Opens color dialog.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param srcBtn Button
	 * @param srcText Text
	 * @param srcLbl Label
	 * @param state Integer
	 * @param constant 
	 */
	protected void openColorDialog(Button srcBtn, Text srcText, Label srcLbl, int state, String constant) {
		ColorDialog colorDialog = new ColorDialog(srcBtn.getShell());
		colorDialog.setText(Messages.SelectColor);
		RGB selectedColor = colorDialog.open();
		if (selectedColor == null) {
			return;
		}
		PreferenceConverter.setValue(this.store, constant,selectedColor);
		srcText.setText(selectedColor.toString());
		this.setLabelIcon(srcLbl, selectedColor, state);
	}
	

	/**
	 * Initialize ArrayList with RGB values of color shades.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param redShades ArrayList<RGB>
	 * @param blackShades ArrayList<RGB>
	 * @param yellowShades ArrayList<RGB>
	 * @param greenShades ArrayList<RGB>
	 * @param purpleShades ArrayList<RGB>
	 * @param whiteShades ArrayList<RGB>
	 * @param blueShades ArrayList<RGB>
	 * @param grayShades ArrayList<RGB>
	 */
	private void initShadeLists(ArrayList<RGB> redShades, ArrayList<RGB> blackShades, ArrayList<RGB> yellowShades,
		ArrayList<RGB> greenShades, ArrayList<RGB> purpleShades, ArrayList<RGB> whiteShades, ArrayList<RGB> blueShades,
		ArrayList<RGB> grayShades) {
		
		// RGB values
		final int twoHundredAndFiftyFive = 255;
		final int zero = 0;
		final int sixtyfour = 64;
		final int oneHundredAndTwentyEight = 128;
		// initialize
		if (redShades.size() == 0) {
			redShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			redShades.add(new RGB(twoHundredAndFiftyFive, zero, zero));
			redShades.add(new RGB(oneHundredAndTwentyEight, sixtyfour, sixtyfour));
			redShades.add(new RGB(oneHundredAndTwentyEight, zero, zero));
			redShades.add(new RGB(sixtyfour, zero, zero));
		}
		
		if (blackShades.size() == 0) {
			blackShades.add(new RGB(zero, zero, zero));
		}
		
		if (yellowShades.size() == 0) {
			yellowShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, zero));
			yellowShades.add(new RGB(oneHundredAndTwentyEight, sixtyfour, zero));
			yellowShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, zero));
			yellowShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, sixtyfour));
			yellowShades.add(new RGB(twoHundredAndFiftyFive, twoHundredAndFiftyFive, zero));
			yellowShades.add(new RGB(twoHundredAndFiftyFive, twoHundredAndFiftyFive, oneHundredAndTwentyEight));
		}
		
		if (greenShades.size() == 0) {
			greenShades.add(new RGB(oneHundredAndTwentyEight, twoHundredAndFiftyFive, oneHundredAndTwentyEight));
			greenShades.add(new RGB(oneHundredAndTwentyEight, twoHundredAndFiftyFive, zero));
			greenShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, sixtyfour));
			greenShades.add(new RGB(sixtyfour, oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			greenShades.add(new RGB(zero, twoHundredAndFiftyFive, zero));
			greenShades.add(new RGB(zero, oneHundredAndTwentyEight, zero));
			greenShades.add(new RGB(zero, sixtyfour, zero));
			greenShades.add(new RGB(zero, twoHundredAndFiftyFive, oneHundredAndTwentyEight));
			greenShades.add(new RGB(zero, twoHundredAndFiftyFive, sixtyfour));
			greenShades.add(new RGB(zero, oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			greenShades.add(new RGB(zero, oneHundredAndTwentyEight, sixtyfour));
			greenShades.add(new RGB(zero, sixtyfour, sixtyfour));
		}
		
		final int onehundredAndNinetyTwo = 192;
		if (purpleShades.size() == 0) {
			purpleShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, onehundredAndNinetyTwo));
			purpleShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, twoHundredAndFiftyFive));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, onehundredAndNinetyTwo));
			purpleShades.add(new RGB(twoHundredAndFiftyFive, zero, twoHundredAndFiftyFive));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, zero, sixtyfour));
			purpleShades.add(new RGB(twoHundredAndFiftyFive, zero, oneHundredAndTwentyEight));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, zero, oneHundredAndTwentyEight));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, zero, twoHundredAndFiftyFive));
			purpleShades.add(new RGB(sixtyfour, zero, sixtyfour));
			purpleShades.add(new RGB(sixtyfour, zero, oneHundredAndTwentyEight));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, twoHundredAndFiftyFive));
		}
		
		if (whiteShades.size() == 0) {
			whiteShades.add(new RGB(twoHundredAndFiftyFive, twoHundredAndFiftyFive, twoHundredAndFiftyFive));
		}
		
		final int onehundredAndSixty = 160;
		if (blueShades.size() == 0) {
			blueShades.add(new RGB(oneHundredAndTwentyEight, twoHundredAndFiftyFive, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, twoHundredAndFiftyFive, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, sixtyfour, oneHundredAndTwentyEight));
			blueShades.add(new RGB(zero, zero, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, zero, oneHundredAndTwentyEight));
			blueShades.add(new RGB(zero, oneHundredAndTwentyEight, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, oneHundredAndTwentyEight, onehundredAndNinetyTwo));
			blueShades.add(new RGB(zero, zero, onehundredAndSixty));
			blueShades.add(new RGB(zero, zero, sixtyfour));
		}
		
		if (grayShades.size() == 0) {
			grayShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			grayShades.add(new RGB(onehundredAndNinetyTwo, onehundredAndNinetyTwo, onehundredAndNinetyTwo));
		}
		
	}
	
	protected Composite getDestinationChooser(Composite parent){
		Composite pathComposite= new Composite(parent, SWT.NONE);
		
		return pathComposite;
	}



	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @return
	 * 			the name of the project
	 */
	public String getProjectName() {
		return this.projectName;
	}



	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param projectName
	 * 			the name of the project
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	

	@Override
	public abstract String getExportPath();

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param path
	 * 			the path as String
	 */
	public abstract void setExportPath(String path);
	
	/**
	 *
	 * @author Sebastian Sieber,Lukas Balzer
	 * @param filters
	 * 			the file extensions, which shall be excepted by in the dialog
	 *
	 */
	protected void openExportDialog(String[] filters,String[] names) {
		FileDialog fileDialog = new FileDialog(this.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(filters); 
		fileDialog.setFilterNames(names); 
		fileDialog.setFileName(this.getProjectName());
		String filePath = fileDialog.open();
		if (filePath != null) {
			this.setExportPath(filePath);
		}
		this.setPageComplete(true);
	}
	
	/**
	 *
	 * @author Sebastian Sieber,Lukas Balzer
	 * @param filters
	 * 			the file extensions, which shall be excepted by in the dialog
	 */
	protected void openExportDialog(String[] filters) {
		openExportDialog(filters, filters);
	}
	protected final class PathComposite extends Composite{
		private Button pathButton;
		private Text path;
		
		/**
		 * This constructor constructs a Composite which contains
		 * a basic ui for choosing a path
		 * 
		 * @author Lukas Balzer
		 * @param parent a widget which will be the parent of the new instance (cannot be null)
		 * @param style the style of widget to construct 
		 * @param name the name which specifies the path to be chosen
		 *
		 * @see Composite#Composite(Composite, int)
		 */
		public PathComposite(Composite parent, int style, String name) {
			super(parent, style);
			this.setLayout(null);
			Label labelExport = new Label(this, SWT.NONE);
			labelExport.setText(name);
			labelExport.setBounds(LABEL_COLUMN, 0, LABEL_WIDTH, 15);
			
			
			this.path= new Text(this, SWT.BORDER|SWT.SINGLE);
			this.path.setBounds(TEXT_COLUMN, 0, 297, 21);
			this.path.setText(""); //$NON-NLS-1$
			this.path.setEditable(false);
			
			this.pathButton = new Button(this, SWT.NONE);
			this.pathButton.setBounds(BUTTON_COLUMN, 0, 42, 25);
			this.pathButton.setText("..."); //$NON-NLS-1$

		}
		
		public PathComposite(final String[] filter,Composite parent, int style){
			this(parent, style, Messages.Destination);
			addButtonListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					AbstractExportPage.this.openExportDialog(filter);
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// nothing
					
				}
			});
		}
		public void addButtonListener(SelectionListener listener){
			this.pathButton.addSelectionListener(listener);
		}
		public Text getText(){
			return this.path;
		}
		
	}
	
	protected final class ColorChooser extends Composite{

		


		private Text colorText;
		private Button	colorButton;
		private Label colorIcon;
		
		/**
		 * This constructs a Composite containing Elements for choosing a color 
		 * and storing the value in a preference store
		 * 
		 * @author Lukas Balzer
		 *
		 * @param parent  a widget which will be the parent of the new instance (cannot be null)
		 * @param style the style of widget to construct 
		 * @param text the text which shall be displayed in the label
		 * @param constant The IPreferenceConstants to which the color is linked in the preference store
		 * 
		 * @see Composite#Composite(Composite, int)
		 */
		public ColorChooser(Composite parent,int style,String text,final String constant) {
			super(parent, style); 
			Label labelBackgroundColor = new Label(this, SWT.NONE);
			labelBackgroundColor.setText(text);
			labelBackgroundColor.setBounds(LABEL_COLUMN, 0, LABEL_WIDTH, 15);
			
			//create the Text which says what color is selected at the moment
			this.colorText = new Text(this, SWT.BORDER);
			this.colorText.setBounds(TEXT_COLUMN + 30, 0, 268, 21);
			this.colorText.setEditable(false);
			String companyBackgroundColor =
				PreferenceConverter.getColor(AbstractExportPage.this.store, constant).toString();
			
			if (companyBackgroundColor != null) {
				this.colorText.setText(companyBackgroundColor);
				this.colorText.setSelection(companyBackgroundColor.length());
			} else {
				this.colorText.setText(""); //$NON-NLS-1$
			}
			
			this.colorIcon = new Label(this, SWT.NONE);
			this.colorIcon.setBounds(TEXT_COLUMN, 0, 23, 23);
			AbstractExportPage.this.setLabelIcon(this.colorIcon,
				PreferenceConverter.getColor(AbstractExportPage.this.store, constant),
				1);
			
			this.colorButton = new Button(this, SWT.NONE);
			this.colorButton.setText("..."); //$NON-NLS-1$
			this.colorButton.setBounds(BUTTON_COLUMN, 0, 42, 25);
			this.colorButton.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					AbstractExportPage.this.openColorDialog(ColorChooser.this.colorButton,
							ColorChooser.this.colorText, ColorChooser.this.colorIcon,1,constant);
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
		
		public void addColorChangeListener(ModifyListener listener){
			this.colorText.addModifyListener(listener);
		}
	}
	
	protected class DemoCanvas extends Canvas{
		private final Text sampleTitle;
		private final Font normalFont=new Font(null, "normalfont", 10, SWT.NORMAL); //$NON-NLS-1$
		private final Font previewFont=new Font(null, "font", 14, SWT.NORMAL); //$NON-NLS-1$
		
		public DemoCanvas(Composite parent, int style) {
			super(parent, style);
			this.sampleTitle=new Text(DemoCanvas.this, SWT.NONE);
			this.sampleTitle.setFont(this.previewFont);
			this.sampleTitle.setVisible(false);
			this.sampleTitle.setText( AbstractExportPage.this.store.getString(IPreferenceConstants.PROJECT_NAME));
			this.sampleTitle.pack();
			this.addPaintListener(new PaintListener() {
				private Color fontColor;
				private Color bgColor;
				private final int string_xPos=10;
				private final int string_yPos=0;
				private final int bgRec_yPos=7;
				private final int preview_xPos=8;
				private final int preview_yPos=19;
				@Override
				public void paintControl(PaintEvent e) {
					e.gc.setBackground(ColorConstants.white);
					e.gc.fillRectangle(0, this.bgRec_yPos, 400, 40);
					
					this.fontColor= new Color(null,PreferenceConverter.getColor(AbstractExportPage.this.store,
							IPreferenceConstants.COMPANY_FONT_COLOR));
					this.bgColor=new Color(null,PreferenceConverter.getColor(AbstractExportPage.this.store,
						IPreferenceConstants.COMPANY_BACKGROUND_COLOR));
					e.gc.setForeground(this.fontColor);
					e.gc.setFont(DemoCanvas.this.previewFont);
					e.gc.setBackground(this.bgColor);
					e.gc.drawString(DemoCanvas.this.sampleTitle.getText(), this.preview_xPos, this.preview_yPos, false);
					
					e.gc.setForeground(ColorConstants.black);
					e.gc.setFont(DemoCanvas.this.normalFont);
					e.gc.drawText(Messages.Preview, this.string_xPos, this.string_yPos,true);
				}
			});
		}
		
	
	}
	
	/**
	 * @return the fontState
	 */
	public int getFontState() {
		return this.fontState;
	}
	/**
	 * @param fontState the fontState to set
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
	 * @param backgroundState the backgroundState to set
	 */
	public void setBackgroundState(int backgroundState) {
		this.backgroundState = backgroundState;
	}
	
}
