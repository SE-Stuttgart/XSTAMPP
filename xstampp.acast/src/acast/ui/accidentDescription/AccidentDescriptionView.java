package acast.ui.accidentDescription;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javax.imageio.ImageIO;

import messages.Messages;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.ui.editors.StyledTextSelection;
import xstampp.ui.editors.interfaces.ITextEditContribution;
import xstampp.ui.editors.interfaces.ITextEditor;
import acast.Activator;
import acast.model.interfaces.IAccidentDescriptionViewDataModel;

public class AccidentDescriptionView extends StandartEditorPart implements
		ITextEditor, IPropertyChangeListener {

	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	/**
	 * ViewPart ID.
	 */
	public static final String ID = "acast.steps.step1_1";

	public static final String ImageViewID = "acast.steps.step1_1_image";

	/**
	 * Combo to display font size and font name.
	 */
	private Combo fontNameControl, fontSizeControl;

	private ITextEditContribution toolContributor;

	private Text companyInformationText;
	private Text accidentLocationText;

	private final static int stateForeground = 0;
	private final static int stateBackground = 1;

	private static final String[] FONT_SIZES = new String[] {
			"6", "8", "9", "10", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"11", "12", "14", "24", "36", "48" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	private Map<Widget, String> values;

	/**
	 * Text fonts
	 */
	private Font textFont;

	/**
	 * Colors
	 */
	private Color textForegroundColor, textBackgroundColor;

	/**
	 * Image path used for button images.
	 */
	private static final String IMAGE_PATH = "/icons/buttons/systemdescription"; //$NON-NLS-1$

	/**
	 * Label for the accident name
	 */
	private Label accidentNameLabel;

	private ArrayList<ISelectionChangedListener> listener;

	private ToolItem boldControl, italicControl, strikeoutControl,
			foregroundControl, backgroundControl, baselineUpControl,
			baselineDownControl, underlineControl, bulletListControl;

	/**
	 * Text arena to describe the accident.
	 */
	private StyledText descriptionText;

	/**
	 * Interface to communicate with the data model.
	 */
	private IAccidentDescriptionViewDataModel dataInterface;

	private DateTime dateTime;

	private Label lblpicture;

	private List pictureList;

	private Label lblCurrentPicture;

	private DateTime dateClock;

	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (IAccidentDescriptionViewDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}

	/**
	 * Update UI
	 *
	 * @author Patrick Wickenhaeuser
	 *
	 * @param dataModelController
	 *            Observable
	 * @param updatedValue
	 *            Object
	 */
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case PROJECT_DESCRIPTION:

			break;
		case SAVE:

			break;
		default:
			break;
		}
	}

	private void setStyle(Widget widget) {
		this.values = new HashMap<>();
		this.values.put(this.backgroundControl, BACKGROUND);
		this.values.put(this.baselineDownControl, FONT_SIZE_DOWN);
		this.values.put(this.baselineUpControl, FONT_SIZE_UP);
		this.values.put(this.boldControl, BOLD);
		this.values.put(this.bulletListControl, DOT_LIST);
		this.values.put(this.fontNameControl, FONT_FAMILY);
		this.values.put(this.fontSizeControl, FONT_SIZE);
		this.values.put(this.foregroundControl, FOREGROUND);
		this.values.put(this.italicControl, ITALIC);
		this.values.put(this.descriptionText, DESCRIPTION);
		setStyle(this.values.get(widget));
	}

	/**
	 * Set text background color
	 *
	 * @author Sebastian Sieber
	 *
	 * @param event
	 *            SelectionEvent
	 * @param composite
	 *            Composite
	 * @param colorControl
	 *            ToolItem
	 */
	public void setTextBackground(SelectionEvent event, Composite composite,
			ToolItem colorControl) {
		Shell shell = composite.getShell();
		if ((event.detail == SWT.ARROW) || (this.textBackgroundColor == null)) {
			ColorDialog dialog = new ColorDialog(shell);
			RGB rgb;
			if (this.textBackgroundColor != null) {
				rgb = this.textBackgroundColor.getRGB();
			} else {
				rgb = null;
			}
			dialog.setRGB(rgb);
			RGB newRgb = dialog.open();
			if (newRgb == null) {
				return;
			}
			// new color chosen
			if (!newRgb.equals(rgb)) {
				setStyleColor(this.values.get(event.widget), newRgb);
				// change icon
				this.setToolItemIcon(colorControl, newRgb,
						AccidentDescriptionView.stateBackground);
			}
		}
	}

	/**
	 * Set text foreground color
	 *
	 * @author Sebastian Sieber
	 *
	 * @param event
	 *            SelectionEvent
	 * @param composite
	 *            Composite
	 * @param colorControl
	 *            ToolItem
	 */
	public void setTextForeground(SelectionEvent event, Composite composite,
			ToolItem colorControl) {
		Shell shell = composite.getShell();
		if ((event.detail == SWT.ARROW) || (this.textForegroundColor == null)) {
			ColorDialog dialog = new ColorDialog(shell);
			RGB rgb;
			if (this.textForegroundColor != null) {
				rgb = this.textForegroundColor.getRGB();
			} else {
				rgb = null;
			}
			dialog.setRGB(rgb);
			RGB newRgb = dialog.open();
			if (newRgb == null) {
				return;
			}
			// new color chosen
			if (!newRgb.equals(this.textForegroundColor)) {
				setStyleColor(this.values.get(event.widget), newRgb);
				// change icon
				this.setToolItemIcon(colorControl, newRgb,
						AccidentDescriptionView.stateForeground);
			}

		}
	}

	/**
	 * Set different color icons for different color shades.
	 *
	 * @author Sebastian Sieber
	 *
	 * @param rgb
	 *            RGB
	 */
	private void setToolItemIcon(ToolItem colorControl, RGB rgb, int state) {
		String imagePath = ""; //$NON-NLS-1$
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

		if (state == AccidentDescriptionView.stateForeground) {
			imagePath = "/colors/foreground"; //$NON-NLS-1$
		} else {
			imagePath = "/colors/background"; //$NON-NLS-1$
		}

		// set icon
		if (redShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					AccidentDescriptionView.getImagePath() + imagePath
							+ "/textRed.ico") //$NON-NLS-1$
					.createImage());
		} else if (blackShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					AccidentDescriptionView.getImagePath() + imagePath
							+ "/textBlack.ico") //$NON-NLS-1$
					.createImage());
		} else if (yellowShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					AccidentDescriptionView.getImagePath() + imagePath
							+ "/textYellow.ico") //$NON-NLS-1$
					.createImage());
		} else if (greenShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					AccidentDescriptionView.getImagePath() + imagePath
							+ "/textGreen.ico") //$NON-NLS-1$
					.createImage());
		} else if (purpleShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					AccidentDescriptionView.getImagePath() + imagePath
							+ "/textPurple.ico") //$NON-NLS-1$
					.createImage());
		} else if (whiteShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					AccidentDescriptionView.getImagePath() + imagePath
							+ "/textWhite.ico") //$NON-NLS-1$
					.createImage());
		} else if (blueShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					AccidentDescriptionView.getImagePath() + imagePath
							+ "/textBlue.ico") //$NON-NLS-1$
					.createImage());
		} else if (grayShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					AccidentDescriptionView.getImagePath() + imagePath
							+ "/textGrey.ico") //$NON-NLS-1$
					.createImage());
		}

	}

	/**
	 * @return the imagePath
	 */
	public static String getImagePath() {
		return AccidentDescriptionView.IMAGE_PATH;
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

	/**
	 * Get font names from system and sort alphabetically.
	 *
	 * @author Sebastian Sieber
	 *
	 * @param composite
	 *            Composite
	 * @return names font names
	 */
	public static String[] getFontNames(Composite composite) {
		FontData[] fontNames = composite.getDisplay().getFontList(null, true);
		ArrayList<String> names = new ArrayList<String>();
		String[] sortedNames;
		for (FontData fontName2 : fontNames) {
			String fontName = fontName2.getName();
			if (!(names.contains(fontName))) {
				if (!(fontName.substring(0, 1).equals(Messages.CharacterAt))) {
					names.add(fontName);
				}
			}
		}
		Collections.sort(names);
		sortedNames = new String[names.size()];
		for (int i = 0; i < names.size(); i++) {
			sortedNames[i] = names.get(i);
		}
		return sortedNames;
	}

	/**
	 * Set default font sizes to combo.
	 *
	 * @author Sebastian Sieber
	 *
	 */
	public void setDefaultFontSize() {
		FontData fontData;
		if (this.textFont != null) {
			fontData = this.textFont.getFontData()[0];
		} else {
			fontData = this.descriptionText.getFont().getFontData()[0];
		}

		int index = 0;
		int count = this.fontSizeControl.getItemCount();

		int fontSize = fontData.getHeight();
		while (index < count) {
			int size = Integer.parseInt(this.fontSizeControl.getItem(index));
			if (fontSize == size) {
				this.fontSizeControl.select(index);
				break;
			}
			if (size > fontSize) {
				this.fontSizeControl.add(String.valueOf(fontSize), index);
				this.fontSizeControl.select(index);
				break;
			}
			index++;
		}
	}

	/**
	 * Set default font names to combo.
	 *
	 * @author Sebastian Sieber
	 *
	 */
	public void setDefaultFontName() {
		FontData fontData;
		if (this.textFont != null) {
			fontData = this.textFont.getFontData()[0];
		} else {
			fontData = this.descriptionText.getFont().getFontData()[0];
		}
		int index = 0;
		int count = this.fontNameControl.getItemCount();
		String fontName = fontData.getName();
		while (index < count) {
			if (this.fontNameControl.getItem(index).equals(fontName)) {
				this.fontNameControl.select(index);
				break;
			}
			index++;
		}
	}

	/**
	 * @return the fontSizes
	 */
	public static String[] getFontSizes() {
		return AccidentDescriptionView.FONT_SIZES;
	}

	/**
	 * Increase font size.
	 *
	 * @author Sebastian Sieber
	 *
	 * @param composite
	 *            Composite
	 * @param event
	 *            SelectionEvent
	 */
	public void setBaselineUp(Composite composite, SelectionEvent event) {
		Display display = composite.getDisplay();
		int currentIndex = this.fontSizeControl.getSelectionIndex();
		if (currentIndex < (this.fontSizeControl.getItemCount() - 1)) {
			currentIndex = currentIndex + 1;
			this.fontSizeControl.select(currentIndex);
			int size = Integer.parseInt(this.fontSizeControl
					.getItem(currentIndex));
			this.textFont = new Font(display, this.fontNameControl.getText(),
					size, SWT.NORMAL);
			this.setStyle(event.widget);
		}
	}

	/**
	 * Decrease font size
	 *
	 * @author Sebastian Sieber
	 *
	 * @param composite
	 *            Composite
	 * @param event
	 *            SelectionEvent
	 */
	public void setBaselineDown(Composite composite, SelectionEvent event) {
		Display display = composite.getDisplay();
		int currentIndex = this.fontSizeControl.getSelectionIndex();
		if (currentIndex > 1) {
			currentIndex = currentIndex - 1;
			this.fontSizeControl.select(currentIndex);
			int size = Integer.parseInt(this.fontSizeControl
					.getItem(currentIndex));
			this.textFont = new Font(display, this.fontNameControl.getText(),
					size, SWT.NORMAL);
			this.setStyle(event.widget);
		}
	}

	@Override
	public String getId() {
		return this.ID;
	}

	/**
	 * Gets the current accident description from the data model and set the
	 * text formation.
	 *
	 */
	private void updateAccidentDescription() {

		String accidentDesc = this.dataInterface.getAccidentDescription();
		StyleRange[] ranges = this.dataInterface.getStyleRangesAsArray();
		if (accidentDesc != null) {
			if (!accidentDesc.contains("\r")) {
				accidentDesc = accidentDesc.replaceAll("\n", "\r\n");
			}
			this.descriptionText.setText(accidentDesc);
			if (ranges != null) {
				this.descriptionText.setStyleRanges(ranges);
			}

		} else {
			this.descriptionText.setText("");
		}
	}

	private void updateAccidentLocation() {
		String accidentLocation = this.dataInterface.getAccidentLocation();
		if (accidentLocation != null) {
			if (!accidentLocation.contains("\r")) {
				accidentLocation = accidentLocation.replaceAll("\n", "\r\n");
			}
			this.accidentLocationText.setText(accidentLocation);
		} else {
			this.accidentLocationText.setText("");
		}
	}

	private void updateAccidentCompany() {
		String accidentCompany = this.dataInterface.getAccidentCompany();
		if (accidentCompany != null) {
			if (!accidentCompany.contains("\r")) {
				accidentCompany = accidentCompany.replaceAll("\n", "\r\n");
			}
			this.companyInformationText.setText(accidentCompany);
		} else {
			this.companyInformationText.setText("");
		}
	}

	private void updateAccidentDate() {
		if (!this.dataInterface.getAccidentDate().isEmpty()) {
			String accidentDate = this.dataInterface.getAccidentDate();
			SimpleDateFormat format = new SimpleDateFormat(); 
			GregorianCalendar date = new GregorianCalendar();
			try {
				date.setTime(format.parse(accidentDate));
				
				dateTime.setDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH) - 1,
						date.get(Calendar.DAY_OF_MONTH));
				dateClock.setTime(date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
						date.get(Calendar.SECOND));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void updatePictureList() {
		if (this.dataInterface.getPictureList() != null
				&& !this.dataInterface.getPictureList().isEmpty()) {
			for (String picture : this.dataInterface.getPictureList()) {
				this.pictureList.add(picture);
			}
		}
	}

	/**
	 * Stores the current description text and style ranges of the text in the
	 * data model. Style ranges are used to set the style and format of the
	 * description text.
	 *
	 *
	 */
	private void applyAccidentDescriptionToDataModel() {
		if (!this.descriptionText.getStyleRanges().equals(
				this.dataInterface.getStyleRanges())) {
			this.dataInterface.getStyleRanges().clear();
			for (StyleRange styleRange : this.descriptionText.getStyleRanges()) {
				this.dataInterface.addStyleRange(styleRange);
			}
		}
		if (!(this.descriptionText.getText().equals(this.dataInterface
				.getAccidentDescription()))) {
			this.dataInterface.setAccidentDescription(this.descriptionText
					.getText());
		}
	}

	/**
	 * Stores the current description text and style ranges of the text in the
	 * data model. Style ranges are used to set the style and format of the
	 * description text.
	 *
	 *
	 */
	private void applyAccidentCompanyToDataModel() {

		if (!(this.companyInformationText.getText().equals(this.dataInterface
				.getAccidentCompany()))) {
			this.dataInterface.setAccidentCompany(this.companyInformationText
					.getText());
		}
	}

	/**
	 * Stores the current description text and style ranges of the text in the
	 * data model. Style ranges are used to set the style and format of the
	 * description text.
	 *
	 *
	 */
	private void applyAccidentLocationToDataModel() {
		if (!(this.accidentLocationText.getText().equals(this.dataInterface
				.getAccidentLocation()))) {
			this.dataInterface.setAccidentLocation(this.accidentLocationText
					.getText());
		}
	}

	/**
	 * Stores the current description text and style ranges of the text in the
	 * data model. Style ranges are used to set the style and format of the
	 * description text.
	 *
	 *
	 */
	private void applyAccidentDateToDataModel() {
		SimpleDateFormat format = new SimpleDateFormat(); 
		GregorianCalendar datum = new GregorianCalendar(dateTime.getYear(),
				dateTime.getMonth() + 1, dateTime.getDay(),dateClock.getHours(),
				dateClock.getMinutes(), dateClock.getSeconds());
		
		String date = format.format(datum.getTime());
		if (!(date.equals(this.dataInterface.getAccidentDate()))) {
			this.dataInterface.setAccidentDate(date);
		}
	}

	private void applyPictureListToDataModel() {
		this.dataInterface.setPictureList(this.pictureList.getItems());
	}

	private void applyCurrentPictureToDataModel() {
		this.dataInterface.setCurrentPicture(lblCurrentPicture.getText());
	}

	public void addAccidentInformationComponent(final Composite parent) {
		Composite compositeAccidentInformation = new Composite(parent, SWT.NONE);
		compositeAccidentInformation.setLayout(new GridLayout(2, true));
		compositeAccidentInformation.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 1));

		Composite compositeAccidentPicture = new Composite(
				compositeAccidentInformation, SWT.BORDER);
		compositeAccidentPicture.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false, 1, 1));
		compositeAccidentPicture.setLayout(new GridLayout(4, false));

		lblpicture = new Label(compositeAccidentPicture, SWT.None);
		lblpicture.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		lblpicture.setBounds(0, 0, 55, 15);
		lblpicture.setText("Accident Picture:");
		lblpicture.setFont(new Font(Display.getCurrent(), "Segoe UI", 9,
				SWT.BOLD));

		Button btnAddPicture = new Button(compositeAccidentPicture, SWT.NONE);
		btnAddPicture.setImage(Activator.getImageDescriptor(
				"/icons/buttons/commontables/add.png").createImage());
		btnAddPicture.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false,
				false, 1, 1));
		btnAddPicture.setBounds(0, 0, 75, 25);

		btnAddPicture.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					boolean cancel = false;
					FileDialog jfc = new FileDialog(null);
					jfc.setFilterExtensions(ImageIO.getReaderFileSuffixes());
					String selectedFile = jfc.open();
					if (selectedFile != null && new File(selectedFile).isFile()) {
						File f = new File(selectedFile);
						for (String currentPicturePath : pictureList.getItems()) {
							if (currentPicturePath.contains(f.getAbsolutePath())) {
								MessageBox dialog = new MessageBox(parent
										.getShell(), SWT.ICON_INFORMATION
										| SWT.OK);
								dialog.setText("Warning");
								dialog.setMessage("Picture already exists");

								dialog.open();
								cancel = true;
							}
						}
						if (!cancel) {

							int currentPictureNumber = pictureList
									.getItemCount() + 1;
							lblCurrentPicture
									.setText("Current Picture: Picture "
											+ currentPictureNumber);
							applyCurrentPictureToDataModel();
							pictureList.add("Picture " + currentPictureNumber
									+ " -> " + f.getAbsolutePath());
							AccidentDescriptionView.this
									.applyPictureListToDataModel();
							break;
						} else {
							break;
						}
					}
				}
			}
		});

		Button btnDeletePicture = new Button(compositeAccidentPicture, SWT.NONE);
		btnDeletePicture.setImage(Activator.getImageDescriptor(
				"/icons/buttons/commontables/remove.png").createImage());
		btnDeletePicture.setBounds(0, 0, 75, 25);
		btnDeletePicture.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				int currentPicture = pictureList.getSelectionIndex();
				if (currentPicture == -1) {
					MessageBox dialog = new MessageBox(parent.getShell(),
							SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Warning");
					dialog.setMessage("No Picture selected");

					dialog.open();
				} else {
					pictureList.remove(currentPicture);
					AccidentDescriptionView.this.applyPictureListToDataModel();
					if (currentPicture > 0) {
						lblCurrentPicture.setText("Current Picture: Picture "
								+ currentPicture);
						applyCurrentPictureToDataModel();
					} else if (currentPicture == 0) {
						lblCurrentPicture.setText("Current Picture: -");
						applyCurrentPictureToDataModel();
					}
				}
			}
		});

		Button showPicture = new Button(compositeAccidentPicture, SWT.NONE);
		showPicture
				.setImage(Activator
						.getImageDescriptor(
								"/icons/buttons/navigation/Picture_frame_with_mountain_image_32.png")
						.createImage());
		showPicture.setBounds(0, 0, 75, 25);
		showPicture.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {

				if (lblCurrentPicture.getText().equals("Current Picture: -")) {
					MessageBox dialog = new MessageBox(parent.getShell(),
							SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Warning");
					dialog.setMessage("No Picture selected");

					dialog.open();
				} else {
					IWorkbenchWindow window = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow();
					IWorkbenchPage page = window.getActivePage();
					try {
						ImageInput input = new ImageInput();
						int selection = Integer.valueOf(lblCurrentPicture
								.getText().substring(25,
										lblCurrentPicture.getText().length()));
						input.setPath(pictureList
								.getItem(selection - 1)
								.substring(
										13,
										pictureList.getItem(selection - 1)
												.length()).replace(" ", ""));
						page.openEditor(input, ImageViewID);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}

			}
		});

		lblCurrentPicture = new Label(compositeAccidentPicture, SWT.NONE);
		lblCurrentPicture.setFont(new Font(Display.getCurrent(), "Segoe UI", 7,
				SWT.ITALIC));
		lblCurrentPicture.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		lblCurrentPicture.setText("Current Picture: -");
		lblCurrentPicture.setFont(new Font(Display.getCurrent(), "Segoe UI", 7,
				SWT.ITALIC));

		pictureList = new List(compositeAccidentPicture, SWT.SINGLE
				| SWT.V_SCROLL | SWT.H_SCROLL);
		pictureList.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true,
				4, 1));
		pictureList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (pictureList.getSelectionIndex() >= 0) {

					int currentPictureNumber = pictureList.getSelectionIndex() + 1;
					lblCurrentPicture.setText("Current Picture: Picture "
							+ currentPictureNumber);
					applyCurrentPictureToDataModel();

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		Color color = new Color(Display.getCurrent(), new RGB(255, 255, 255));

		Composite compositeAccidentDate = new Composite(
				compositeAccidentInformation, SWT.BORDER);
		compositeAccidentDate.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false, 1, 1));
		compositeAccidentDate.setLayout(new GridLayout(2, false));

		Label lblDate = new Label(compositeAccidentDate, SWT.NONE);
		lblDate.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1,
				1));
		lblDate.setText("Accident Date:");
		lblDate.setFont(new Font(Display.getCurrent(), "Segoe UI", 9, SWT.BOLD));

		Label lblTime = new Label(compositeAccidentDate, SWT.NONE);
		lblTime.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 1,
				1));
		lblTime.setText("Accident Time:");
		lblTime.setFont(new Font(Display.getCurrent(), "Segoe UI", 9, SWT.BOLD));

		Composite compositeAccidentCalendar = new Composite(
				compositeAccidentDate, SWT.BORDER);
		compositeAccidentCalendar.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, false, 2, 1));
		compositeAccidentCalendar.setLayout(new GridLayout(2, false));
		compositeAccidentCalendar.setBackground(color);
		dateTime = new DateTime(compositeAccidentCalendar, SWT.CALENDAR);
		dateTime.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true, 1,
				1));
		dateTime.setBackground(color);
		dateTime.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				AccidentDescriptionView.this.applyAccidentDateToDataModel();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// no-op
			}
		});

		dateClock = new DateTime(compositeAccidentCalendar, SWT.TIME);
		dateClock.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1,
				1));
		dateClock.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				AccidentDescriptionView.this.applyAccidentDateToDataModel();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// no-op
			}
		});

		updateAccidentDate();

		Composite compositeCompanyInformation = new Composite(
				compositeAccidentInformation, SWT.BORDER);
		compositeCompanyInformation.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 1));
		compositeCompanyInformation.setLayout(new GridLayout(1, false));

		Label lblCompanyInformation = new Label(compositeCompanyInformation,
				SWT.NONE);
		lblCompanyInformation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		lblCompanyInformation.setBounds(0, 0, 55, 15);
		lblCompanyInformation.setText("Company Information:");
		lblCompanyInformation.setFont(new Font(Display.getCurrent(),
				"Segoe UI", 9, SWT.BOLD));

		companyInformationText = new Text(compositeCompanyInformation,
				SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		companyInformationText.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		companyInformationText.setBounds(0, 0, 76, 21);
		companyInformationText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				AccidentDescriptionView.this.applyAccidentCompanyToDataModel();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// no-op
			}
		});

		Composite compositeAccidentLocation = new Composite(
				compositeAccidentInformation, SWT.BORDER);
		compositeAccidentLocation.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 1));
		compositeAccidentLocation.setLayout(new GridLayout(1, false));

		Label lblAccidentLocation = new Label(compositeAccidentLocation,
				SWT.NONE);
		lblAccidentLocation.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, false, 1, 1));
		lblAccidentLocation.setBounds(0, 0, 55, 15);
		lblAccidentLocation.setText("Accident Location:");
		lblAccidentLocation.setFont(new Font(Display.getCurrent(), "Segoe UI",
				9, SWT.BOLD));

		accidentLocationText = new Text(compositeAccidentLocation, SWT.BORDER
				| SWT.V_SCROLL | SWT.WRAP);
		accidentLocationText.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		accidentLocationText.setBounds(0, 0, 76, 21);
		accidentLocationText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				AccidentDescriptionView.this.applyAccidentLocationToDataModel();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// no-op
			}
		});

		updateAccidentCompany();
		updateAccidentLocation();
		updateAccidentDate();
		updatePictureList();

	}

	@Override
	public void createPartControl(Composite parent) {
		if (this.toolContributor == null) {
			this.toolContributor = new EmptyTextContributor();
		}
		this.setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(this.getProjectID()));
		Activator.getDefault().getPreferenceStore()
				.addPropertyChangeListener(this);

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addPartListener(new IPartListener2() {

					@Override
					public void partVisible(IWorkbenchPartReference partRef) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partOpened(IWorkbenchPartReference partRef) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partInputChanged(IWorkbenchPartReference partRef) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partHidden(IWorkbenchPartReference partRef) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partDeactivated(IWorkbenchPartReference partRef) {

					}

					@Override
					public void partClosed(IWorkbenchPartReference partRef) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partBroughtToTop(IWorkbenchPartReference partRef) {
						if (partRef.getId().equals("acast.steps.step1_1")) {
							PlatformUI.getPreferenceStore().firePropertyChangeEvent("currentSelection", "", "close");

						}

					}

					@Override
					public void partActivated(IWorkbenchPartReference partRef) {
						IWorkbenchPage page = PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage();
					}
				});

		parent.setLayout(new GridLayout(1, false));
		Composite composite_AccidentDescription = new Composite(parent,
				SWT.BORDER);
		composite_AccidentDescription.setLayout(new GridLayout(1, false));
		GridData accidentDescriptionData = new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1);
		composite_AccidentDescription.setLayoutData(accidentDescriptionData);

		this.accidentNameLabel = new Label(composite_AccidentDescription,
				SWT.NONE);
		accidentNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM,
				true, false, 1, 1));
		this.accidentNameLabel.setText("Accident Description:");
		this.accidentNameLabel.setFont(new Font(Display.getCurrent(),
				"Segoe UI", 10, SWT.BOLD));

		this.descriptionText = new StyledText(composite_AccidentDescription,
				SWT.H_SCROLL | SWT.WRAP);
		this.descriptionText.setAlwaysShowScrollBars(true);
		GridData descriptionTextData = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		descriptionText.setLayoutData(descriptionTextData);
		this.descriptionText.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (ISelectionChangedListener obj : AccidentDescriptionView.this.listener) {
					obj.selectionChanged(new SelectionChangedEvent(
							AccidentDescriptionView.this, getSelection()));

				}

			}
		});
		// this.descriptionText.addKeyListener(new KeyAdapter() {
		//
		// @Override
		// public void keyReleased(KeyEvent e) {
		// switch (e.keyCode) {
		// case SWT.ARROW_LEFT: {
		// AccidentDescriptionView.this.descriptionText.setSelection(
		// AccidentDescriptionView.this.descriptionText
		// .getSelection().x - 1,
		// AccidentDescriptionView.this.descriptionText
		// .getSelection().x - 1);
		// break;
		// }
		// case SWT.ARROW_RIGHT: {
		// AccidentDescriptionView.this.descriptionText.setSelection(
		// AccidentDescriptionView.this.descriptionText
		// .getSelection().x + 1,
		// AccidentDescriptionView.this.descriptionText
		// .getSelection().x + 1);
		// }
		// }
		// }
		// });
		//
		this.descriptionText
				.addExtendedModifyListener(new ExtendedModifyListener() {

					@Override
					public void modifyText(ExtendedModifyEvent event) {
						AccidentDescriptionView.this
								.handleDescriptionModify(event);

						// AccidentDescriptionView.this.setStyle(event.widget);
					}
				});
		this.descriptionText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				AccidentDescriptionView.this
						.applyAccidentDescriptionToDataModel();
				AccidentDescriptionView.this.setStyle(e.widget);
			}

			@Override
			public void focusGained(FocusEvent e) {
				// no-op
			}
		});

		this.descriptionText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if ((e.stateMask == SWT.CTRL) && (e.keyCode == 'a')) {
					AccidentDescriptionView.this.descriptionText.selectAll();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// no-op
			}
		});

		this.getSite().setSelectionProvider(this);
		updateAccidentDescription();

		addAccidentInformationComponent(parent);

	}

	/**
	 * Handles changes occurring in the TextArea depending on the CaretOffset
	 * and selection of the text.
	 *
	 * @author Sebastian Sieber
	 * @param event
	 *            Fired if description text changed
	 */
	public void handleDescriptionModify(ExtendedModifyEvent event) {
		if (event.length == 0) {
			return;
		}
		StyleRange styleRange = new StyleRange();
		styleRange.font = this.textFont;
		if ((event.length == 1)
				|| this.descriptionText.getTextRange(event.start, event.length)
						.equals(this.descriptionText.getLineDelimiter())) {
			int caretOffset = this.descriptionText.getCaretOffset();

			if (caretOffset < this.descriptionText.getCharCount()) {
				styleRange = this.descriptionText
						.getStyleRangeAtOffset(caretOffset);
			}
			if (styleRange != null) {
				styleRange = (StyleRange) styleRange.clone();
				styleRange.start = event.start;
				styleRange.length = event.length;
			} else {
				styleRange = new StyleRange(event.start, event.length, null,
						null, SWT.NONE);
			}

			if (this.toolContributor.getBoldControl()) {
				styleRange.fontStyle |= SWT.BOLD;
			}
			if (this.toolContributor.getItalicControl()) {
				styleRange.fontStyle |= SWT.ITALIC;
			}

			// update style range
			styleRange.underline = this.toolContributor.getUnderlineControl();
			styleRange.strikeout = this.toolContributor.getStrikeoutControl();
			styleRange.foreground = this.toolContributor.getForeground();
			styleRange.background = this.toolContributor.getBackground();
			styleRange.font = this.toolContributor.getFont();

			if (!styleRange.isUnstyled()) {
				this.descriptionText.setStyleRange(styleRange);
			}
		}
	}

	// TODO For Saving....
	@Override
	public void propertyChange(PropertyChangeEvent event) {

	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (this.listener == null) {
			this.listener = new ArrayList<>();
		}
		this.listener.add(listener);

	}

	@Override
	public ISelection getSelection() {
		Point selection = this.descriptionText.getSelectionRange();
		StyledTextSelection styledSelection = new StyledTextSelection(selection);
		StyleRange[] selctedRanges = this.descriptionText.getStyleRanges(
				selection.x, selection.y);
		int size = -1;
		String fontName = "";
		for (StyleRange range : selctedRanges) {
			if (range.font != null) {
				for (FontData data : range.font.getFontData()) {
					if (size < data.getHeight()) {
						size = data.getHeight();
					}
					fontName = data.getName();
				}
			}
		}
		styledSelection.setFontSize(size);
		styledSelection.setFontName(fontName);
		return styledSelection;
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		if (this.listener != null) {
			this.listener.remove(listener);
		}
	}

	@Override
	public void setSelection(ISelection selection) {

	}

	private void addStyleRangesFor(String style, String fontName, int fontSize,
			int fontStyle) {
		Point selectionRange = this.descriptionText.getSelectionRange();
		if ((selectionRange == null) || (selectionRange.y == 0)) {
			return;
		}
		int end = selectionRange.x + selectionRange.y;

		StyleRange[] ranges = this.descriptionText.getStyleRanges(
				selectionRange.x, selectionRange.y, true);
		for (int i = 0; i < ranges.length; i++) {
			StyleRange range = (StyleRange) ranges[i].clone();
			if (range.start < selectionRange.x) {
				range.length = range.length - (selectionRange.x - range.start);
				range.start = selectionRange.x;
			}
			if (range.start + range.length - 1 > end) {
				range.length = end - range.start;
			}

			this.descriptionText.setStyleRange(setFontItemRange(style, range,
					new FontData(fontName, fontSize, fontStyle)));
		}
		if (ranges.length == 0) {
			StyleRange newRange = new StyleRange();
			newRange.start = selectionRange.x;
			newRange.length = selectionRange.y;
			this.descriptionText.setStyleRange(setFontItemRange(style,
					newRange, new FontData(fontName, fontSize, fontStyle)));
		}

	}
	
	/**
	 * Set the style range if text get modified and widget is selected. Also
	 * triggers if text get selection and widget is selected.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param style
	 *            Widget
	 * @param styleRange
	 *            Set to selected widget
	 * @param newDataSet TODO
	 * @return styleRange StyleRange
	 */
	private StyleRange setFontItemRange(String style, StyleRange styleRange, FontData newDataSet) {

		if(styleRange.font == null){
			styleRange.font = Display.getDefault().getSystemFont(); 
		}
		FontData data = styleRange.font.getFontData()[0];
		
		switch(style){
			case(FOREGROUND):{
				styleRange.foreground = this.textForegroundColor;
				break;
			}case(BACKGROUND): {
				styleRange.background = this.textBackgroundColor;
				break;
			}case(INCREASE): {
				data.setHeight(data.getHeight() + 1);
				break;
			}case(DECREASE): {
				data.setHeight(data.getHeight() - 1);
				break;
			}case(FONT_SIZE): {
				data.setHeight(newDataSet.getHeight());
				break;
			}case(FONT_FAMILY):{
				data.setName(newDataSet.getName());
				break;
			}case(BOLD): {
				data.setStyle(data.getStyle() ^ SWT.BOLD);
				break;
			}case(ITALIC): {
				data.setStyle(data.getStyle() ^ SWT.ITALIC);
				break;
			}case(UNDERLINE): {
				styleRange.underline = !styleRange.underline;
				break;
			}case(STRIKEOUT): {
				styleRange.strikeout = !styleRange.strikeout;
				break;
			}
		}
		styleRange.font= new Font(null, data);
		return styleRange;
	}

	@Override
	public void setStyle(String style) {
		addStyleRangesFor(style, new String(), 0, 0);

	}

	/**
	 * Set style range for bold, italic, strike out and underline.
	 *
	 * @author Sebastian Sieber
	 *
	 * @param style
	 *            Widget
	 * @param styleRange
	 *            StyleRange
	 * @return styleRange
	 */
	private StyleRange setStyleItemRange(String style, StyleRange styleRange) {
		if (style.equals(BOLD)) {
			styleRange.fontStyle ^= SWT.BOLD;
		} else if (style.equals(ITALIC)) {
			styleRange.fontStyle ^= SWT.ITALIC;
		} else if (style.equals(UNDERLINE)) {
			styleRange.underline = !styleRange.underline;
		} else if (style.equals(STRIKEOUT)) {
			styleRange.strikeout = !styleRange.strikeout;
		}
		return styleRange;
	}

	/**
	 * Set the style range if text get modified and widget is selected. Also
	 * triggers if text get selection and widget is selected.
	 *
	 * @author Sebastian Sieber
	 *
	 * @param style
	 *            Widget
	 * @param styleRange
	 *            Set to selected widget
	 * @return styleRange StyleRange
	 */
	private StyleRange setFontItemRange(String style, StyleRange styleRange) {
		if (style.equals(FOREGROUND)) {
			styleRange.foreground = this.textForegroundColor;
		} else if (style.equals(BACKGROUND)) {
			styleRange.background = this.textBackgroundColor;
		} else if ((style.equals(FONT_SIZE_UP))
				|| (style.equals(FONT_SIZE_DOWN)) || (style.equals(FONT_SIZE))
				|| (style.equals(FONT_FAMILY))) {
			styleRange.font = this.textFont;
		}
		return styleRange;
	}

	@Override
	public void setStyleColor(String color, RGB rgb) {
		Display display = PlatformUI.getWorkbench().getDisplay();
		Point selectionRange = this.descriptionText.getSelectionRange();
		if (color.equals(BACKGROUND)) {
			this.textBackgroundColor = new Color(display, rgb);
		} else {
			this.textForegroundColor = new Color(display, rgb);
		}
		this.setColorStyleRange(color, selectionRange);

	}

	/**
	 * Set style range for given range. Used to set foreground and background
	 * color when chosen a new color from dialog.
	 *
	 * @author Sebastian Sieber
	 *
	 * @param selectionRange
	 *            selected range
	 * @param widget
	 *            color control
	 */
	private void setColorStyleRange(String style, Point selectionRange) {
		if ((selectionRange == null) || (selectionRange.y == 0)) {
			return;
		}
		StyleRange styleRange;
		for (int i = selectionRange.x; i < (selectionRange.x + selectionRange.y); i++) {
			StyleRange range = this.descriptionText.getStyleRangeAtOffset(i);
			if (range != null) {
				styleRange = (StyleRange) range.clone();
				styleRange.start = i;
				styleRange.length = 1;
			} else {
				styleRange = new StyleRange(i, 1, null, null, SWT.NORMAL);
			}
			if (style.equals(FOREGROUND)) {
				styleRange.foreground = this.textForegroundColor;
			} else if (style.equals(BACKGROUND)) {
				styleRange.background = this.textBackgroundColor;
			}
			this.descriptionText.setStyleRange(styleRange);
		}
		this.descriptionText.setSelectionRange(selectionRange.x
				+ selectionRange.y, 0);

		// mark text as selected
		this.descriptionText.setSelection(selectionRange.x, selectionRange.x
				+ selectionRange.y);
	}

	@Override
	public void setFont(String fontString, int fontSize) {
		addStyleRangesFor(FONT_FAMILY,fontString, fontSize, SWT.NORMAL);
	}

	@Override
	public void setFontSize(String style, int fontSize) {
		addStyleRangesFor(style,new String(), fontSize, SWT.NORMAL);
	}

	@Override
	public void setBullet(String type) {
		int typeInt;
		if (type.equals(DOT_LIST)) {
			typeInt = ST.BULLET_DOT;
		} else if (type.equals(NUM_LIST)) {
			typeInt = ST.BULLET_NUMBER;
		} else {
			return;
		}

		final int bulletWidth = 20;
		Bullet bullet;
		// get line
		Point selection = this.descriptionText.getSelection();
		int lineStart = this.descriptionText.getLineAtOffset(selection.x);
		int lineEnd = this.descriptionText.getLineAtOffset(selection.y);
		StyleRange styleRange = new StyleRange();
		styleRange.metrics = new GlyphMetrics(0, 0, bulletWidth);
		bullet = new Bullet(typeInt, styleRange);
		bullet.text = ". "; //$NON-NLS-1$
		// add only one bullet per line
		for (int lineIndex = lineStart; lineIndex <= lineEnd; lineIndex++) {
			Bullet oldBullet = this.descriptionText.getLineBullet(lineIndex);
			if (oldBullet != null) {
				// remove bullet if button pressed again in the same line.
				this.descriptionText.setLineBullet(lineIndex, 1, null);
			} else {
				// add new bullet to line.
				this.descriptionText.setLineBullet(lineIndex, 1, bullet);
			}

		}

	}

	@Override
	public void setEditToolContributor(ITextEditContribution contributor) {
		this.toolContributor = contributor;
	}

}
