/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.systemdescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import messages.Messages;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import xstampp.Activator;
import xstampp.astpa.model.interfaces.ISystemDescriptionViewDataModel;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.ui.editors.StyledTextSelection;
import xstampp.ui.editors.interfaces.IEditorBase;
import xstampp.ui.editors.interfaces.ITextEditContribution;
import xstampp.ui.editors.interfaces.ITextEditor;
import xstampp.ui.workbench.contributions.TextToolbarContribution;

/**
 * SystemDescriptionViewPart
 * 
 * @author Patrick Wickenhaeuser, Sebastian Sieber
 * @since 2.0
 */
public class SystemDescriptionView extends StandartEditorPart implements ITextEditor,IPropertyChangeListener {
	
	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	/**
	 * Display current caret and line number.
	 */
	private Label statusBar;

	/**
	 * Label used for project name
	 */
	private Label projectNameLabel;

	/**
	 * Text arena to describe the system.
	 */
	private StyledText descriptionText;

	private ToolItem boldControl, italicControl, strikeoutControl,
			foregroundControl, backgroundControl, baselineUpControl,
			baselineDownControl, underlineControl, bulletListControl;

	private ITextEditContribution toolContributor;
	/**
	 * Contains different font sizes.
	 */
	private static final String[] FONT_SIZES = new String[] {
			"6", "8", "9", "10", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"11", "12", "14", "24", "36", "48" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	private Map<Widget,String> values;
	/**
	 * ViewPart ID.
	 */
	public static final String ID = "astpa.steps.step1_1"; //$NON-NLS-1$

	private List<ISelectionChangedListener> listener;
	private List<Font> fontHandles;


	/**
	 * Interface to communicate with the data model.
	 */
	private ISystemDescriptionViewDataModel dataInterface;

	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (ISystemDescriptionViewDataModel) dataInterface;
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
			this.resetProjectDescription();
			break;
		case PROJECT_NAME:
			this.resetProjectName();
			break;
		case SAVE:
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					applyProjectDescriptionToDataModel(false);
					
				}
			});
			break;
		default:
			break;
		}
	}

	
	/**
	 * Create UI
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param parent
	 *            Composite
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.fontHandles = new ArrayList<>();
		if(this.toolContributor== null){
			this.toolContributor= new EmptyTextContributor();
		}
		this.setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(this.getProjectID()));
		
		Activator.getDefault().getPreferenceStore()
				.addPropertyChangeListener(this);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		// UI parts
		
			this.createProjectNameText(composite);
			if(System.getProperty("os.name").toLowerCase().contains("linux")){
				Composite barControl= new Composite(composite, SWT.NONE);
				this.gridData = new GridData();
				this.gridData.horizontalSpan = 2;
				barControl.setLayoutData(this.gridData);
				TextToolbarContribution bar = new TextToolbarContribution();
				bar.drawControl(barControl);
			}
		this.createStyledText(composite);
		
		this.createContextMenu();
		this.statusBar = new Label(composite, SWT.NONE);
		this.statusBar.setFont(new Font(Display.getCurrent(),
				PreferenceConverter.getFontData(IEditorBase.STORE,
						IPreferenceConstants.DEFAULT_FONT)));

		// Initialize with color black
		final int redB = 255;
		final int greenB = 255;
		final int blueB = 255;
		this.textBackgroundColor = new Color(Display.getDefault(), new RGB(
				redB, greenB, blueB));

		// Initialize with color white
		final int redW = 0;
		final int greenW = 0;
		final int blueW = 0;
		this.textForegroundColor = new Color(Display.getDefault(), new RGB(
				redW, greenW, blueW));

		this.updateStatusBar();
//		this.setDefaultFontName();
//		this.setDefaultFontSize();
		this.resetFromDataModel();
		this.descriptionText.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(ISelectionChangedListener obj:SystemDescriptionView.this.listener){
					obj.selectionChanged(new SelectionChangedEvent(SystemDescriptionView.this, getSelection()));
					
				}
				
			}
		});
		this.getSite().setSelectionProvider(this);
		composite.pack();

	}

	/**
	 * Project name.
	 */
	private Text projectNameText;

	/**
	 * Layout
	 */
	private GridData gridData;

	/**
	 * Combo to display font size and font name.
	 */
	private Combo fontNameControl, fontSizeControl;

	/**
	 * Text fonts
	 */
	private Font textFont;

	/**
	 * Colors
	 */
	private Color textForegroundColor, textBackgroundColor;

	private List<StyleRange> allRanges = new ArrayList<>();

	/**
	 * Image path used for button images.
	 */
	private static final String IMAGE_PATH = "/icons/buttons/systemdescription"; //$NON-NLS-1$

	/**
	 * Stores the current text in the data model.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	private void applyProjectNameToDataModel() {
		this.dataInterface.setProjectName(this.projectNameText.getText());
		this.store.setValue(IPreferenceConstants.PROJECT_NAME,
				this.projectNameText.getText());
	}

	/**
	 * Stores the current description text and style ranges of the text in the
	 * data model. Style ranges are used to set the style and format of the
	 * description text.
	 * 
	 * @author Sebastian Sieber, Patrick Wickenhaeuser
	 * @param stylesChanged TODO
	 * 
	 */
	private void applyProjectDescriptionToDataModel(boolean stylesChanged) {
		if (stylesChanged) {
			this.dataInterface.getStyleRanges().clear();
			for (StyleRange styleRange : this.descriptionText.getStyleRanges()) {
				if(styleRange.font == null || !styleRange.font.isDisposed()){
					this.dataInterface.addStyleRange((StyleRange) styleRange);
				}
			}
			dataInterface.setUnsavedAndChanged();
		}
		String projectDesc= descriptionText.getText();
		if(projectDesc.contains(System.lineSeparator())){
			projectDesc = projectDesc.replaceAll(System.lineSeparator(),"\n");
		}
		this.dataInterface.setProjectDescription(projectDesc);
		
	}

	/**
	 * Sets the current text to the data stored in the data model
	 * 
	 * @author Patrick Wickenhaeuser
	 */
	private void resetFromDataModel() {
		this.resetProjectName();
		this.resetProjectDescription();
	}

	/**
	 * Gets the current project name from the data model
	 * 
	 * @author Patrick Wickenhaeuser, Sebastian Sieber
	 */
	private void resetProjectName() {
		String projectName = this.dataInterface.getProjectName();
		if (projectName != null) {
			this.projectNameText.setText(projectName);
			this.store.setValue(IPreferenceConstants.PROJECT_NAME, projectName);
		} else {
			this.projectNameText.setText(""); //$NON-NLS-1$
		}
	}

	/**
	 * Gets the current project description from the data model and set the text
	 * formation.
	 * 
	 * @author Patrick Wickenhaeuser, Sebastian Sieber
	 */
	private void resetProjectDescription() {
		
		String projectDesc = this.dataInterface.getProjectDescription();
		StyleRange[] ranges = this.dataInterface.getStyleRangesAsArray();
		if (projectDesc != null) {
			String sep= System.lineSeparator();
			if(!projectDesc.contains(sep)){
				projectDesc = projectDesc.replaceAll("\n", sep);
			}
			this.descriptionText.setText(projectDesc);
			if (ranges != null) {
				this.descriptionText.setStyleRanges(ranges);
			}
			
		} else {
			this.descriptionText.setText(""); //$NON-NLS-1$
		}
	}

	/**
	 * Create text field and label for the project name.
	 * 
	 * @author Sebastian Sieber
	 * @param composite
	 *            Composite
	 */
	private void createProjectNameText(final Composite composite) {
		this.projectNameLabel = new Label(composite, SWT.NONE);
		final int textLimit = 100;
		Font labelFont = new Font(Display.getCurrent(),
				PreferenceConverter.getFontData(IEditorBase.STORE,
						IPreferenceConstants.DEFAULT_FONT));

		this.projectNameLabel.setText(Messages.ProjectName);
		this.projectNameLabel.setFont(labelFont);

		this.projectNameText = new Text(composite, SWT.READ_ONLY);
		this.gridData = new GridData();
		this.gridData.horizontalAlignment = SWT.FILL;
		this.gridData.grabExcessHorizontalSpace = true;
		this.projectNameText.setLayoutData(this.gridData);
		this.projectNameText.setTextLimit(textLimit);

		// save and update project name every time the text in TextField got
		// modified
		this.projectNameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				SystemDescriptionView.this.handleProjectNameModify();
				SystemDescriptionView.this.forceTextfieldFocus(composite);

			}
		});
	}

	/**
	 * Create styled TextArea.
	 * 
	 * @author Sebastian Sieber
	 * @param composite
	 *            composite
	 */
	private void createStyledText(Composite composite) {
		this.descriptionText = new StyledText(composite, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL | SWT.WRAP);

		this.gridData = new GridData();
		this.gridData.horizontalSpan = 2;
		this.gridData.horizontalAlignment = SWT.FILL;
		this.gridData.grabExcessHorizontalSpace = true;
		this.gridData.verticalAlignment = SWT.FILL;
		this.gridData.grabExcessVerticalSpace = true;
		this.descriptionText.setLayoutData(this.gridData);

		// Add listeners
		this.descriptionText.addCaretListener(new CaretListener() {

			@Override
			public void caretMoved(CaretEvent event) {
				SystemDescriptionView.this.updateStatusBar();
			}
		});

		this.descriptionText
				.addExtendedModifyListener(new ExtendedModifyListener() {

					@Override
					public void modifyText(ExtendedModifyEvent event) {
						SystemDescriptionView.this.handleDescriptionModify(event);
						applyProjectDescriptionToDataModel(false);
					}
				});
		this.descriptionText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				SystemDescriptionView.this.applyProjectDescriptionToDataModel(false);
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
					SystemDescriptionView.this.descriptionText.selectAll();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// no-op
			}
		});
	}

	private void createContextMenu() {
		Menu menu = new Menu(this.descriptionText);
		final MenuItem cutItem = new MenuItem(menu, SWT.PUSH);
		cutItem.setText("Cut\tCtrl+X"); //$NON-NLS-1$
		cutItem.setAccelerator(SWT.MOD1 | 'x');
		cutItem.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/cut.ico").createImage()); //$NON-NLS-1$

		cutItem.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				SystemDescriptionView.this.descriptionText.cut();
			}
		});
		final MenuItem copyItem = new MenuItem(menu, SWT.PUSH);
		copyItem.setText("Copy\tCtrl+C"); //$NON-NLS-1$
		cutItem.setAccelerator(SWT.MOD1 | 'c');
		copyItem.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/copy.ico").createImage()); //$NON-NLS-1$

		copyItem.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				SystemDescriptionView.this.descriptionText.copy();
			}
		});
		final MenuItem pasteItem = new MenuItem(menu, SWT.PUSH);
		pasteItem.setText("Paste\tCtrl+P"); //$NON-NLS-1$
		cutItem.setAccelerator(SWT.MOD1 | 'p');
		pasteItem.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/paste.ico").createImage()); //$NON-NLS-1$

		pasteItem.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				SystemDescriptionView.this.descriptionText.paste();
			}
		});

		MenuItem menuItem = new MenuItem(menu, SWT.SEPARATOR);
		menuItem.setEnabled(true);

		final MenuItem selectAllItem = new MenuItem(menu, SWT.PUSH);
		selectAllItem.setText("Select All\tCtrl+A"); //$NON-NLS-1$
		cutItem.setAccelerator(SWT.MOD1 | 'a');
		selectAllItem.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				SystemDescriptionView.this.descriptionText.selectAll();
			}
		});
		menu.addMenuListener(new MenuAdapter() {

			@Override
			public void menuShown(MenuEvent event) {
				int selectionCount = SystemDescriptionView.this.descriptionText
						.getSelectionCount();
				cutItem.setEnabled(selectionCount > 0);
				copyItem.setEnabled(selectionCount > 0);
				selectAllItem
						.setEnabled(selectionCount < SystemDescriptionView.this.descriptionText
								.getCharCount());
			}
		});
		SystemDescriptionView.this.descriptionText.setMenu(menu);
	}



	/**
	 * Force the focus on project name and select text if text equals
	 * "New Project"
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param composite
	 *            TextField Composite
	 */
	private void forceTextfieldFocus(Composite composite) {
		if (SystemDescriptionView.this.projectNameText.getText()
				.equalsIgnoreCase(Messages.NewProject)) {
			composite.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					// set focus and selection
					SystemDescriptionView.this.projectNameText.forceFocus();
					SystemDescriptionView.this.projectNameText.setSelection(0,
							SystemDescriptionView.this.projectNameText
									.getText().length());
				}
			});
		}
	}

	/**
	 * Handles changes occurring for the project name.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param event
	 *            Fired if project name changed
	 */
	private void handleProjectNameModify() {
		String modifiedProjectName = this.projectNameText.getText();
		if (!(this.dataInterface.getProjectName().equals(modifiedProjectName))) {
			this.applyProjectNameToDataModel();
		}
		this.projectNameText.setSelection(modifiedProjectName.length());
	}

	private void setStyle(Widget widget){
		this.values=new HashMap<>();
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
//		setStyle(this.values.get(widget));
	}
	
	@Override
	public void setStyle(String style) {
		addStyleRangesFor(style, new String(), 0, 0);
	}
	
	@Override
	public void setFont(String fontString,int fontSize) {
		addStyleRangesFor(FONT_FAMILY,fontString, fontSize, SWT.NORMAL);
	}
	
	@Override
	public void setFontSize(String style, int fontSize) {
		addStyleRangesFor(style,new String(), fontSize, SWT.NORMAL);
		
	}
	
	private void addStyleRangesFor(String style, String fontName, int fontSize,int fontStyle) {
		Point selectionRange = this.descriptionText.getSelectionRange();
		if ((selectionRange == null) || (selectionRange.y == 0)) {
			return;
		}
		StyleRange newRange = null;
		int next=0;
			StyleRange[] ranges = descriptionText.getStyleRanges(selectionRange.x, selectionRange.y, false);
	//		for (int i = 0; i < ranges.length; i++) {
	//			ranges[i] = setFontItemRange(style, ranges[i], fontSize, fontName);
	//		}

			int count = 0;
			for(int i= selectionRange.x; i < selectionRange.x + selectionRange.y;i++){
				if(ranges.length > next && ranges[next].start < i){
					StyleRange range = (StyleRange) ranges[next].clone();
					range.font = FontDescriptor.createFrom(ranges[next].font).createFont(null);
					fontHandles.add(range.font);
					range.start = i;
					range.length = Math.min(selectionRange.y,ranges[next].length- (i-ranges[next].start));
					if(range.length >0){
						i = i +(ranges[next].length- (i-ranges[next].start)) -1;
						descriptionText.setStyleRange(setFontItemRange(style, range, fontSize, fontName));
						count++;
						next++;
					}
				}else if(ranges.length > next && ranges[next].start == i){
					if(newRange != null){
						descriptionText.setStyleRange(setFontItemRange(style, newRange, fontSize, fontName));
						count++;
						newRange = null;
					}
					i = i +(ranges[next].length- (i-ranges[next].start)) -1;
					descriptionText.setStyleRange(setFontItemRange(style, ranges[next], fontSize, fontName));
					count++;
					next++;
				}else if(newRange != null){
					newRange.length++;
				}else{
					newRange = new StyleRange();
					newRange.font = FontDescriptor.createFrom(TextToolbarContribution.SYSTEM_FONT).createFont(null);
					newRange.start = i;
					newRange.length = 1;
				}
			}
			
			if(newRange != null){
				descriptionText.setStyleRange(setFontItemRange(style, newRange, fontSize, fontName));
				count++;
				newRange = null;
			}
			System.out.println(count);
		applyProjectDescriptionToDataModel(true);
		getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				
				for(StyleRange range: descriptionText.getStyleRanges()){
					if(fontHandles.contains(range.font)){
						fontHandles.remove(range.font);
					}
				}
				for(Font font:fontHandles){
					font.dispose();
				}
				fontHandles.clear();
			}
		});
		selectionRange = null;
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
	private StyleRange setFontItemRange(String style, StyleRange styleRange, int height,String name) {
		FontData data = null;
		if(styleRange.font != null){
			data = styleRange.font.getFontData()[0]; 
		}else{
			data = FontDescriptor.copy(TextToolbarContribution.SYSTEM_FONT_DATA);
		}
		boolean setFont= false;
		switch(style){
			case(FOREGROUND):{
				styleRange.foreground = this.textForegroundColor;
				break;
			}case(BACKGROUND):{
				styleRange.background = this.textBackgroundColor;
				break;
			}case(INCREASE):{
				data.setHeight(data.getHeight() + 1);
				setFont= true;
				break;
			}case(DECREASE):{
				data.setHeight(data.getHeight() - 1);
				setFont= true;
				break;
			}case(FONT_SIZE):{
				data.setHeight(height);
				setFont= true;
				break;
			}case(FONT_FAMILY):{
				data.setName(name);
				setFont= true;
				break;
			}case(BOLD): {
				data.setStyle(data.getStyle() ^ SWT.BOLD);
				setFont= true;
				break;
			}case(ITALIC): {
				data.setStyle(data.getStyle() ^ SWT.ITALIC);
				setFont= true;
				break;
			}case(UNDERLINE): {
				styleRange.underline = !styleRange.underline;
				break;
			}case(STRIKEOUT): {
				styleRange.strikeout = !styleRange.strikeout;
				break;
			}
		}
		if(setFont){
			
			Font newFonte = styleRange.font;
			styleRange.font= new Font(null, data.getName(),data.getHeight(),data.getStyle());
			fontHandles.add(newFonte);
		}
		return styleRange;
	}

	@Override
	public void setStyleColor(String color, RGB rgb) {

		Display display = PlatformUI.getWorkbench().getDisplay();
		Point selectionRange = this.descriptionText.getSelectionRange();
		if(color.equals(BACKGROUND)){
			this.textBackgroundColor = new Color(display, rgb);
		}else{
			this.textForegroundColor = new Color(display, rgb);
		}
		this.setColorStyleRange(color,selectionRange);
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
		applyProjectDescriptionToDataModel(true);
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
	 * Set a bullet to TextField.
	 * 
	 * @author Sebastian Sieber
	 * @param type
	 *            Integer
	 */
	@Override
	public void setBullet(String type) {
		int typeInt;
		if(type.equals(DOT_LIST)){
			typeInt= ST.BULLET_DOT;
		}else if(type.equals(NUM_LIST)){
			typeInt= ST.BULLET_NUMBER;
		}else{
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

	/**
	 * Set line index and caret offset to status bar.
	 * 
	 * @author Sebastian Sieber
	 * 
	 */
	public void updateStatusBar() {
		int offset = this.descriptionText.getCaretOffset();
		// add one to lineIndex to start counting at one and not zero.
		int lineIndex = this.descriptionText.getLineAtOffset(offset) + 1;
		this.statusBar.setText(Messages.BackSlashSpaceOffset + offset
				+ " " + Messages.Line //$NON-NLS-1$
				+ lineIndex + "\t"); //$NON-NLS-1$
	}

	/**
	 * Update CoolBar items if caret position changed.
	 * 
	 * @author Sebastian Sieber
	 * 
	 */@Deprecated
	public void updateCoolBar() {
		boolean italicState = false, underlineState = false, strikeoutState = false, boldState = false;
		int offset = this.descriptionText.getCaretOffset();
		StyleRange styleRange;
		if (offset > 0) {
			styleRange = this.descriptionText.getStyleRangeAtOffset(offset - 1);
		} else {
			styleRange = null;
		}

		if (styleRange != null) {
			boldState = (styleRange.fontStyle & SWT.BOLD) != 0;
			this.boldControl.setSelection(boldState);

			italicState = (styleRange.fontStyle & SWT.ITALIC) != 0;
			this.italicControl.setSelection(italicState);

			if (styleRange.underline) {
				underlineState = true;
			}
			this.underlineControl.setSelection(underlineState);

			if (styleRange.strikeout) {
				strikeoutState = true;
			}
			this.strikeoutControl.setSelection(strikeoutState);
		}
	}

	/**
	 * @return the imagePath
	 */
	public static String getImagePath() {
		return SystemDescriptionView.IMAGE_PATH;
	}

	/**
	 * @return the fontSizes
	 */
	public static String[] getFontSizes() {
		return SystemDescriptionView.FONT_SIZES;
	}



	@Override
	public void dispose() {
		this.dataInterface.deleteObserver(this);
//		descriptionText.dispose();
		Activator.getDefault().getPreferenceStore()
		.removePropertyChangeListener(this);
		super.dispose();
	}

	@Override
	public ISelection getSelection(){
		Point selection=this.descriptionText.getSelectionRange();
		StyledTextSelection styledSelection= new StyledTextSelection(selection);
		StyleRange[] selctedRanges=this.descriptionText.getStyleRanges(selection.x, selection.y);
		int size=-1;
		String fontName="";
		int fontStyle = -1;
		boolean underline = selctedRanges.length != 0;
		boolean strikeout = selctedRanges.length != 0;
		for(StyleRange range:selctedRanges){
			if(!range.strikeout){
				strikeout = false;
			}if(!range.underline){
				underline = false;
			}
			if(range.font != null){
				for(FontData data:range.font.getFontData()){
					if(size<data.getHeight()){
						size =data.getHeight();
					}
					fontName=data.getName();
					if(fontStyle == -1){
						fontStyle = data.getStyle();
					}else if(data.getStyle() != fontStyle){
						fontStyle = 0;
					}
				}
			}
		}
		if(fontStyle == -1){
			fontStyle = 0;
		}
		styledSelection.setFontSize(size);
		styledSelection.setFontStyle(fontStyle);
		styledSelection.setFontName(fontName);
		styledSelection.setUnderline(underline);
		styledSelection.setStrikeout(strikeout);
		return styledSelection;
			
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener arg0) {
		if(this.listener == null){
			this.listener= new ArrayList<>();
		}
		this.listener.add(arg0);
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener arg0) {
		if(this.listener != null){
			this.listener.remove(arg0);
		}
	}

	@Override
	public void setSelection(ISelection arg0) {
		// The Selection can not be set from outside
	}
	
	@Override
	public String getId() {
		return SystemDescriptionView.ID;
	}


	@Override
	public String getTitle() {
		return Messages.SystemDescription;
	}

	@Override
	public void setEditToolContributor(ITextEditContribution contributor) {
		this.toolContributor=contributor;
	}
	
	@Override
	public void partActivated(IWorkbenchPart arg0) {
		propertyChange(null);
		super.partActivated(arg0);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.projectNameLabel.getFont().dispose();
		this.projectNameLabel.setFont(new Font(
				Display.getCurrent(),
				PreferenceConverter.getFontData(
						IEditorBase.STORE,
						IPreferenceConstants.DEFAULT_FONT)));
		this.statusBar.getFont().dispose();
		this.statusBar.setFont(new Font(
				Display.getCurrent(),
				PreferenceConverter.getFontData(
						IEditorBase.STORE,
						IPreferenceConstants.DEFAULT_FONT)));
		
	}

	
}
