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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import messages.Messages;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
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
import org.eclipse.swt.events.KeyAdapter;
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
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
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

	private final static int stateForeground = 0;
	private final static int stateBackground = 1;
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
			this.applyProjectDescriptionToDataModel();
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
//		this.createToolBar(composite);
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
		this.descriptionText.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				switch(e.keyCode){
				case SWT.ARROW_LEFT:{
					SystemDescriptionView.this.descriptionText.setSelection(
							SystemDescriptionView.this.descriptionText.getSelection().x -1, 
							SystemDescriptionView.this.descriptionText.getSelection().x-1);
					break;
				}
				case SWT.ARROW_RIGHT:{
					SystemDescriptionView.this.descriptionText.setSelection(
							SystemDescriptionView.this.descriptionText.getSelection().x +1,
							SystemDescriptionView.this.descriptionText.getSelection().x+1);
				}
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
	 * 
	 */
	private void applyProjectDescriptionToDataModel() {
		if (!this.descriptionText.getStyleRanges().equals(
				this.dataInterface.getStyleRanges())) {
			this.dataInterface.getStyleRanges().clear();
			for (StyleRange styleRange : this.descriptionText.getStyleRanges()) {
				this.dataInterface.addStyleRange(styleRange);
			}
		}
		if (!(this.descriptionText.getText().equals(this.dataInterface
				.getProjectDescription()))) {
			this.dataInterface.setProjectDescription(this.descriptionText
					.getText());
		}
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
			if(!projectDesc.contains("\r")){
				projectDesc = projectDesc.replaceAll("\n", "\r\n");
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
						SystemDescriptionView.this
								.handleDescriptionModify(event);
						
						SystemDescriptionView.this.setStyle(event.widget);
					}
				});
		this.descriptionText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				SystemDescriptionView.this.applyProjectDescriptionToDataModel();
				SystemDescriptionView.this.setStyle(e.widget);
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
	 * Create ToolBar to format the TextArea.
	 * 
	 * @author Sebastian Sieber
	 * @param composite
	 *            composite
	 */
	private void createToolBar(final Composite composite) {
		// Create cool bar
		CoolBar coolBar = new CoolBar(composite, SWT.FLAT);

		// create style toolBar and add controls.
		this.gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		this.gridData.widthHint = SWT.DEFAULT;
		coolBar.setLayoutData(this.gridData);

		// Add style items to coolBar.
		ToolBar styleToolBar = new ToolBar(coolBar, SWT.FLAT);
		this.addStyleItems(styleToolBar);
		CoolItem coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(styleToolBar);

		// create text color toolBar and add controls.
		ToolBar textColorToolBar = new ToolBar(coolBar, SWT.FLAT);
		this.addColorItems(textColorToolBar, composite);
		coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(textColorToolBar);

		// add combo component to coolBar
		Composite compositeCombo = new Composite(coolBar, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 1;
		compositeCombo.setLayout(layout);
		this.addFontNameSizeCombo(compositeCombo, composite);

		coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(compositeCombo);

		// create alignment toolBar and add controls
		ToolBar alignmentToolBar = new ToolBar(coolBar, SWT.FLAT);

		this.baselineUpControl = new ToolItem(alignmentToolBar, SWT.PUSH);
		this.baselineUpControl.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/font_big.ico").createImage()); //$NON-NLS-1$

		this.baselineUpControl.setToolTipText("Increase font size"); //$NON-NLS-1$
		this.baselineUpControl.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				SystemDescriptionView.this.setBaselineUp(composite, event);
				SystemDescriptionView.this.setStyle(event.widget);

			}
		});

		this.baselineDownControl = new ToolItem(alignmentToolBar, SWT.PUSH);
		this.baselineDownControl.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/font_sml.ico").createImage()); //$NON-NLS-1$
		this.baselineDownControl.setToolTipText(Messages.DecreaseFontSize);
		this.baselineDownControl.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				SystemDescriptionView.this.setBaselineDown(composite, event);
				SystemDescriptionView.this.setStyle(event.widget);
			}
		});

		coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(alignmentToolBar);

		ToolBar listToolBar = new ToolBar(coolBar, SWT.FLAT);
		// add alignment items to coolBar
		this.addListItems(listToolBar);
		coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(listToolBar);

		this.setCoolItemSize(coolBar);
		coolBar.setLocked(true);
		composite.pack();
	}

	/**
	 * Add style items to coolBar
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param selectionListener
	 *            SelectionAdapter
	 * @param styleToolBar
	 *            ToolBar
	 */
	private void addStyleItems(ToolBar styleToolBar) {
		// Add listener for toolBar items. Get selected widget.
		SelectionAdapter selectionListener = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				SystemDescriptionView.this.setStyle(event.widget);
			}
		};

		// Bold item
		this.boldControl = new ToolItem(styleToolBar, SWT.CHECK);
		this.boldControl
				.setImage(Activator
						.getImageDescriptor(
								SystemDescriptionView.getImagePath()
										+ "/bold.ico").createImage()); //$NON-NLS-1$
		this.boldControl.setToolTipText(Messages.Bold);
		this.boldControl.addSelectionListener(selectionListener);

		// Italic item
		this.italicControl = new ToolItem(styleToolBar, SWT.CHECK);
		this.italicControl.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/italic.ico").createImage()); //$NON-NLS-1$

		this.italicControl.setToolTipText(Messages.Italic);
		this.italicControl.addSelectionListener(selectionListener);

		// Underline item
		this.underlineControl = new ToolItem(styleToolBar, SWT.CHECK);
		this.underlineControl.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/underline.ico").createImage()); //$NON-NLS-1$

		this.underlineControl.setToolTipText(Messages.Underline);
		this.underlineControl.addSelectionListener(selectionListener);

		// Strike out item
		this.strikeoutControl = new ToolItem(styleToolBar, SWT.CHECK);
		this.strikeoutControl.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/strikeout.ico").createImage()); //$NON-NLS-1$

		this.strikeoutControl.setToolTipText(Messages.Strikeout);
		this.strikeoutControl.addSelectionListener(selectionListener);
	}

	/**
	 * Add font foreground and font background items to coolBar.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param textColorToolBar
	 *            ToolBar
	 * @param composite
	 *            Composite
	 */
	private void addColorItems(ToolBar textColorToolBar,
			final Composite composite) {
		this.foregroundControl = new ToolItem(textColorToolBar, SWT.DROP_DOWN);
		this.foregroundControl

		.setImage(Activator.getImageDescriptor(
				SystemDescriptionView.getImagePath()
						+ "/colors/foreground/textBlack.ico").createImage()); //$NON-NLS-1$

		this.foregroundControl.setToolTipText(Messages.TextForeground);
		this.foregroundControl.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {

				SystemDescriptionView.this.setTextForeground(event, composite,
						SystemDescriptionView.this.foregroundControl);
				SystemDescriptionView.this.setStyle(event.widget);

			}
		});

		this.backgroundControl = new ToolItem(textColorToolBar, SWT.DROP_DOWN);
		this.backgroundControl

		.setImage(Activator.getImageDescriptor(
				SystemDescriptionView.getImagePath()
						+ "/colors/background/textWhite.ico").createImage()); //$NON-NLS-1$

		this.backgroundControl.setToolTipText(Messages.TextBackground);
		this.backgroundControl.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				SystemDescriptionView.this.setTextBackground(event, composite,
						SystemDescriptionView.this.backgroundControl);
				SystemDescriptionView.this.setStyle(event.widget);
			}
		});
	}

	/**
	 * Add font name and font size combo to coolbar.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param compositeCombo
	 *            Composite
	 * @param composite
	 *            Composite
	 */
	private void addFontNameSizeCombo(Composite compositeCombo,
			final Composite composite) {

		final int visibleItemCount = 8;
		this.fontNameControl = new Combo(compositeCombo, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		this.fontNameControl.setItems(SystemDescriptionView
				.getFontNames(composite));
		this.fontNameControl.setVisibleItemCount(visibleItemCount);

		this.fontSizeControl = new Combo(compositeCombo, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		this.fontSizeControl.setItems(SystemDescriptionView.getFontSizes());
		this.fontSizeControl.setVisibleItemCount(visibleItemCount);

		// get chosen font size and font name from UI
		SelectionAdapter selectionAdapter = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				SystemDescriptionView.this.setFont(SystemDescriptionView.this.fontNameControl.getText(),-1);
				SystemDescriptionView.this.setStyle(event.widget);
			}
		};
		this.fontSizeControl.addSelectionListener(selectionAdapter);
		this.fontNameControl.addSelectionListener(selectionAdapter);
	}

	/**
	 * Add list items to coolBar
	 * 
	 * @author Sebastian Sieber
	 * @param listToolBar
	 *            ToolBar
	 */
	private void addListItems(ToolBar listToolBar) {
		this.bulletListControl = new ToolItem(listToolBar, SWT.PUSH);
		this.bulletListControl
				.setImage(Activator
						.getImageDescriptor(
								SystemDescriptionView.getImagePath()
										+ "/para_bul.ico").createImage()); //$NON-NLS-1$
		this.bulletListControl.setToolTipText(Messages.BulletList);
		this.bulletListControl.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				SystemDescriptionView.this.setBullet(DOT_LIST);
			}
		});
	}

	/**
	 * Set the size of each coolItem.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param coolBar
	 *            CoolBar
	 */
	private void setCoolItemSize(CoolBar coolBar) {
		CoolItem[] coolItems = coolBar.getItems();
		for (CoolItem item : coolItems) {
			Control control = item.getControl();
			Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			item.setMinimumSize(size);
			size = item.computeSize(size.x, size.y);
			item.setPreferredSize(size);
			item.setSize(size);
		}
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

	/**
	 * Reset style buttons to default if the description text field loses focus
	 * or another view gets aktive.
	 * 
	 * @author Sebastian Sieber
	 * 
	 */
	private void resetStyleButtons() {
		this.boldControl.setSelection(false);
		this.italicControl.setSelection(false);
		this.underlineControl.setSelection(false);
		this.strikeoutControl.setSelection(false);
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
		setStyle(this.values.get(widget));
	}
	@Override
	public void setStyle(String style) {
		Point selectionRange = this.descriptionText.getSelectionRange();
		if ((selectionRange == null) || (selectionRange.y == 0)) {
			return;
		}
		StyleRange newRange = new StyleRange();
		for (int i = selectionRange.x; i < (selectionRange.x + selectionRange.y); i++) {
			StyleRange range = this.descriptionText.getStyleRangeAtOffset(i);
			if (range != null) {
				newRange = (StyleRange) range.clone();
				newRange.start = i;
				newRange.length = 1;
			} else {
				newRange = new StyleRange(i, 1, null, null, SWT.NULL);
			}
			// called if text is selected and button pressed
			this.descriptionText.setStyleRange(this.setStyleItemRange(style,
					newRange));
			this.descriptionText.setStyleRange(this.setFontItemRange(style,
					newRange));
		}
		this.descriptionText.setSelectionRange(selectionRange.x
				+ selectionRange.y, 0);

		// mark text as selected
		this.descriptionText.setSelection(selectionRange.x, selectionRange.x
				+ selectionRange.y);
		this.descriptionText.setStyleRange(newRange);
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
				|| (style.equals(FONT_SIZE_DOWN))
				|| (style.equals(FONT_SIZE))
				|| (style.equals(FONT_FAMILY))) {
			styleRange.font = this.textFont;
		}
		return styleRange;
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
						SystemDescriptionView.stateBackground);
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
		Shell shell= composite.getShell();
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
						SystemDescriptionView.stateForeground);
			}

		}
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

		if (state == SystemDescriptionView.stateForeground) {
			imagePath = "/colors/foreground"; //$NON-NLS-1$
		} else {
			imagePath = "/colors/background"; //$NON-NLS-1$
		}

		// set icon
		if (redShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					SystemDescriptionView.getImagePath() + imagePath
							+ "/textRed.ico").createImage()); //$NON-NLS-1$
		} else if (blackShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					SystemDescriptionView.getImagePath() + imagePath
							+ "/textBlack.ico") //$NON-NLS-1$
					.createImage());
		} else if (yellowShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					SystemDescriptionView.getImagePath() + imagePath
							+ "/textYellow.ico") //$NON-NLS-1$
					.createImage());
		} else if (greenShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					SystemDescriptionView.getImagePath() + imagePath
							+ "/textGreen.ico") //$NON-NLS-1$
					.createImage());
		} else if (purpleShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					SystemDescriptionView.getImagePath() + imagePath
							+ "/textPurple.ico") //$NON-NLS-1$
					.createImage());
		} else if (whiteShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					SystemDescriptionView.getImagePath() + imagePath
							+ "/textWhite.ico") //$NON-NLS-1$
					.createImage());
		} else if (blueShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					SystemDescriptionView.getImagePath() + imagePath
							+ "/textBlue.ico") //$NON-NLS-1$
					.createImage());
		} else if (grayShades.contains(rgb)) {
			colorControl.setImage(Activator.getImageDescriptor(
					SystemDescriptionView.getImagePath() + imagePath
							+ "/textGrey.ico") //$NON-NLS-1$
					.createImage());
		}

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

	// private void setTextForeground(SelectionEvent event, Composite composite)
	// {
	// Shell shell = composite.getShell();
	// Display display = composite.getDisplay();
	// Point selectionRange = this.descriptionText.getSelectionRange();
	// if ((event.detail == SWT.ARROW)
	// || (this.editor.getTextForegroundColor() == null)) {
	// ColorDialog dialog = new ColorDialog(shell);
	// RGB rgb;
	// if (this.editor.getTextForegroundColor() != null) {
	// rgb = this.editor.getTextForegroundColor().getRGB();
	// } else {
	// rgb = null;
	// }
	// dialog.setRGB(rgb);
	// RGB newRgb = dialog.open();
	// if (newRgb == null) {
	// return;
	// }
	// // new color chosen
	// if (!newRgb.equals(this.editor.getTextForegroundColor())) {
	// // SystemDescriptionView.this.textForegroundColor = new Color(
	// // display, newRgb);
	// this.editor.setTextForegroundColor(new Color(display, newRgb));
	// this.setColorStyleRange(event.widget, selectionRange);
	// }
	// }
	//
	// }

	// private void setColorStyleRange(Widget widget, Point selectionRange) {
	// if ((selectionRange == null) || (selectionRange.y == 0)) {
	// return;
	// }
	//
	// StyleRange styleRange;
	// for (int i = selectionRange.x; i < (selectionRange.x + selectionRange.y);
	// i++) {
	// StyleRange range = this.descriptionText.getStyleRangeAtOffset(i);
	// if (range != null) {
	// styleRange = (StyleRange) range.clone();
	// styleRange.start = i;
	// styleRange.length = 1;
	// } else {
	// styleRange = new StyleRange(i, 1, null, null, SWT.NORMAL);
	// }
	// if (widget == this.foregroundControl) {
	// styleRange.foreground = this.editor.getTextForegroundColor();
	// } else if (widget == this.backgroundControl) {
	// // styleRange.background = this.textBackgroundColor;
	// }
	// this.descriptionText.setStyleRange(styleRange);
	// }
	// this.descriptionText.setSelectionRange(selectionRange.x
	// + selectionRange.y, 0);
	//
	// // mark text as selected
	// this.descriptionText.setSelection(selectionRange.x, selectionRange.x
	// + selectionRange.y);
	//
	// }

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
		List<String> names = new ArrayList<String>();
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
		Activator.getDefault().getPreferenceStore()
		.removePropertyChangeListener(this);
		super.dispose();
	}

	@Override
	public void setFont(String fontString,int fontSize) {
		getSelection();
		Display display = PlatformUI.getWorkbench().getDisplay();
		int newSize=fontSize;
		if(fontSize < 0){
			newSize = Integer.parseInt(this.fontSizeControl.getText());
		}
		this.textFont = new Font(display, fontString, newSize, SWT.NORMAL);
		setStyle(FONT_FAMILY);
		
	}

	@Override
	public void setFontSize(String style, int fontSize) {
		this.textFont.getFontData()[0].setHeight(fontSize);	
		setStyle(FONT_FAMILY);
	}

	@Override
	public ISelection getSelection(){
		Point selection=this.descriptionText.getSelectionRange();
		StyledTextSelection styledSelection= new StyledTextSelection(selection);
		StyleRange[] selctedRanges=this.descriptionText.getStyleRanges(selection.x, selection.y);
		int size=-1;
		String fontName="";
		for(StyleRange range:selctedRanges){
			if(range.font != null){
				for(FontData data:range.font.getFontData()){
					if(size<data.getHeight()){
						size =data.getHeight();
					}
					fontName=data.getName();
				}
			}
		}
		styledSelection.setFontSize(size);
		styledSelection.setFontName(fontName);
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
		this.projectNameLabel.setFont(new Font(
				Display.getCurrent(),
				PreferenceConverter.getFontData(
						IEditorBase.STORE,
						IPreferenceConstants.DEFAULT_FONT)));
		this.statusBar.setFont(new Font(
				Display.getCurrent(),
				PreferenceConverter.getFontData(
						IEditorBase.STORE,
						IPreferenceConstants.DEFAULT_FONT)));
		super.partActivated(arg0);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		SystemDescriptionView.this.statusBar.setFont(new Font(
				Display.getCurrent(),
				PreferenceConverter.getFontData(
						IEditorBase.STORE,
						IPreferenceConstants.DEFAULT_FONT)));
		SystemDescriptionView.this.projectNameLabel.setFont(new Font(
				Display.getCurrent(),
				PreferenceConverter.getFontData(
						IEditorBase.STORE,
						IPreferenceConstants.DEFAULT_FONT)));
		
	}

	
}
