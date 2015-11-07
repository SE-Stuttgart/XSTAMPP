package xstampp.ui.workbench.contributions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import messages.Messages;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import xstampp.Activator;
import xstampp.ui.editors.StyledTextSelection;
import xstampp.ui.editors.interfaces.ITextEditContribution;
import xstampp.ui.editors.interfaces.ITextEditor;
import xstampp.util.STPAPluginUtils;

/**This toolbar can be activated/used by implementing the ITextEditor Interface
 * 
 * @author Lukas Balzer
 *
 *@since version 2.0
 */
public class TextToolbarContribution extends WorkbenchWindowControlContribution implements 
										ISelectionListener,IPartListener, ITextEditContribution{

	private static final String XSTAMPP_COMMAND_CHOOSECOLOR = "xstampp.command.choosecolor"; //$NON-NLS-1$
	private static final String XSTAMPP_COMMAND_PARAMETER_COLOR_BLUE = "xstampp.commandParameter.color.blue"; //$NON-NLS-1$
	private static final String XSTAMPP_COMMAND_PARAMETER_COLOR_GREEN = "xstampp.commandParameter.color.green"; //$NON-NLS-1$
	private static final String XSTAMPP_COMMAND_PARAMETER_COLOR_RED = "xstampp.commandParameter.color.red"; //$NON-NLS-1$
	private static final String XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE = "xstampp.commandParameter.color.type"; //$NON-NLS-1$
	private final static String FONT_SIZE_PARAMETER="xstampp.commandParameter.fontsize"; //$NON-NLS-1$
	private final static String FONT_NAME_PARAMETER="xstampp.commandParameter.fontfamily"; //$NON-NLS-1$
	private final static String CHANGE_FONT_COMMAND="xstampp.command.choosefont"; //$NON-NLS-1$
	/**
	 * Contains different font sizes.
	 */
	private static final String[] FONT_SIZES = new String[] {
			"6", "8", "9", "10", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"11", "12", "14", "24", "36", "48" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	private ComboContribiution fontCombo;
	private ComboContribiution fontSizeCombo;
	private ButtonContribution boldControl;
	private ButtonContribution italicControl;
	private ButtonContribution strikeoutControl;
	private ButtonContribution foregroundControl;
	private ButtonContribution backgroundControl;
	private ButtonContribution baselineUpControl;
	private ButtonContribution bulletListControl;
	private ButtonContribution baselineDownControl;
	private ButtonContribution underlineControl;
	private SelectionListener listener;
	private RGB foregroundColor;
	private RGB backgroundColor;
	private boolean forceDraw = false;
	// RGB values
			final static int _255 = 255;
			final static int _0 = 0;
			final static int _64 = 64;
			final static int _128 = 128;
			final static int _192 = 192;
			final static int _160 = 160;
			
	private class StyleSelectionListener extends SelectionAdapter{
		String style;
		public StyleSelectionListener(String style) {
			this.style=style;
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			Map<String,String> values=new HashMap<>();
			values.put("xstampp.commandParameter.style", this.style);
			STPAPluginUtils.executeParaCommand("xstampp.command.style", values);
		}
	}
	public void drawControl(Composite comp){
		forceDraw = true;
		fill(comp);
	}
	@Override
	protected Control createControl(Composite arg0) {
	
		ToolBarManager manager = new ToolBarManager(SWT.HORIZONTAL);
	    addStyleItems(manager);
	    addColorItems(manager);
		addFontControl(manager);
	    addBaseControls(manager);
	    addListItems(manager);
	    if(!forceDraw && System.getProperty("os.name").toLowerCase().contains("linux")){
	    	return new ToolBar(arg0, SWT.NONE);
	    }
	    ToolBar toolBar = manager.createControl(arg0);
	
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addSelectionListener(this);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);
		
	    return toolBar;
	}


	private void addFontControl(ToolBarManager manager){
		this.foregroundColor=new RGB(_0, _0, _0);
		this.backgroundColor=new RGB(_255, _255, _255);
		
		this.fontCombo= new ComboContribiution("xstampp.text.styleCombo", SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getComboControl().setItems(getFontNames().toArray(new String[0]));
				getComboControl().setText(PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getName());
				getComboControl().addSelectionListener(new SelectionAdapter() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						Map<String,String> params=new HashMap<>();
						params.put(FONT_NAME_PARAMETER,TextToolbarContribution.this.fontCombo.getComboControl().getText());
						if(params.get(FONT_NAME_PARAMETER) == null || !getFontNames().contains(params.get(FONT_NAME_PARAMETER))){
							params.put(FONT_NAME_PARAMETER, getFontNames().get(0));
						}
						STPAPluginUtils.executeParaCommand(CHANGE_FONT_COMMAND, params);
					}
					
				});
				return control;
			}
		};
		
		
		this.fontSizeCombo=new ComboContribiution("xstampp.text.sizeCombo", SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getComboControl().setItems(FONT_SIZES);
				getComboControl().setText(String.valueOf(PlatformUI.getWorkbench().getDisplay()
						 .getSystemFont().getFontData()[0].getHeight()));
				getComboControl().addSelectionListener(new SelectionAdapter() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						Map<String,String> params=new HashMap<>();
						params.put(FONT_SIZE_PARAMETER,TextToolbarContribution.this.fontSizeCombo.getComboControl().getText());
						if(params.get(FONT_SIZE_PARAMETER).equals("")){
							params.put(FONT_SIZE_PARAMETER, FONT_SIZES[0]);
						}
						STPAPluginUtils.executeParaCommand(CHANGE_FONT_COMMAND, params);
					}
					
				});
				return control;
			}
		};
		
	    manager.add(fontCombo);
	    manager.add(fontSizeCombo);
	}

	/**
	 * Add style items to coolBar
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param selectionListener
	 *            SelectionAdapter
	 * @param manager
	 *            ToolBar
	 */
	private void addStyleItems(ToolBarManager manager) {

		// Bold item
		this.boldControl = new ButtonContribution("boldControl",SWT.None){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl()
						.setImage(Activator
								.getImageDescriptor(
										getImagePath()
												+ "/bold.ico").createImage()); //$NON-NLS-1$
				getButtonControl().setToolTipText(Messages.Bold);
				getButtonControl().addSelectionListener(new StyleSelectionListener(ITextEditor.BOLD));
				return control;
			}
		};
		manager.add(boldControl);
		
		// Italic item
		this.italicControl = new ButtonContribution("italicControl",SWT.None){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().setImage(Activator.getImageDescriptor(
						getImagePath() + "/italic.ico").createImage()); //$NON-NLS-1$

				getButtonControl().setToolTipText(Messages.Italic);
				getButtonControl().addSelectionListener(new StyleSelectionListener(ITextEditor.ITALIC));
				return control;
			}
		};
		this.italicControl.setEnabled(true);
		manager.add(italicControl);
		
		// Underline item
		this.underlineControl = new ButtonContribution("underlineControl",SWT.TOGGLE){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().setImage(Activator.getImageDescriptor(
						getImagePath() + "/underline.ico").createImage()); //$NON-NLS-1$

				getButtonControl().setToolTipText(Messages.Underline);
				getButtonControl().addSelectionListener(new StyleSelectionListener(ITextEditor.UNDERLINE));
				return control;
			}
		};
		manager.add(underlineControl);
		// Strike out item
		this.strikeoutControl = new ButtonContribution("strikeoutControl",SWT.TOGGLE){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().setImage(Activator.getImageDescriptor(
						getImagePath() + "/strikeout.ico").createImage()); //$NON-NLS-1$

				getButtonControl().setToolTipText(Messages.Strikeout);
				getButtonControl().addSelectionListener(new StyleSelectionListener(ITextEditor.STRIKEOUT));
				return control;
			}
		};
		manager.add(strikeoutControl);
	}

	
	/**
	 * Add font foreground and font background items to coolBar.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param manager
	 *            ToolBar
	 * @param composite
	 *            Composite
	 */
	private void addColorItems(ToolBarManager manager) {
		this.foregroundControl =new ButtonContribution("foregroundControl"){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().setImage(Activator.getImageDescriptor(
						getImagePath() + "/colors/foreground/textBlack.ico").createImage()); //$NON-NLS-1$

				getButtonControl().setToolTipText(Messages.TextForeground);
				getButtonControl().addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent event) {
						Map<String,String> values = new HashMap<>();
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE,ITextEditor.FOREGROUND);
						
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_RED,
									String.valueOf(TextToolbarContribution.this.foregroundColor.red));
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_GREEN,
									String.valueOf(TextToolbarContribution.this.foregroundColor.green));
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_BLUE,
									String.valueOf(TextToolbarContribution.this.foregroundColor.blue));
						
						Object obj=STPAPluginUtils.executeParaCommand(XSTAMPP_COMMAND_CHOOSECOLOR, values);
						if(obj instanceof RGB){
							TextToolbarContribution.this.foregroundColor=(RGB) obj;
							setToolItemIcon(TextToolbarContribution.this.foregroundControl, (RGB) obj, ITextEditor.FOREGROUND);
						}
					}
				});
				return control;
			}
		};
		ButtonContribution forgroundChooser =new ButtonContribution("forgroundChooser",SWT.ARROW|SWT.DOWN,15){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent event) {
						Map<String,String> values = new HashMap<>();
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE,ITextEditor.FOREGROUND);
					
						Object obj=STPAPluginUtils.executeParaCommand(XSTAMPP_COMMAND_CHOOSECOLOR, values);
						if(obj instanceof RGB){
							TextToolbarContribution.this.foregroundColor=(RGB) obj;
							setToolItemIcon(TextToolbarContribution.this.foregroundControl, (RGB) obj, ITextEditor.FOREGROUND);
						}
					}
				});
				return control;
			}
		};
		manager.add(foregroundControl);
		manager.add(forgroundChooser);
		
		this.backgroundControl = new ButtonContribution("backgroundControl"){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().setImage(Activator.getImageDescriptor(
						getImagePath() + "/colors/background/textWhite.ico").createImage()); //$NON-NLS-1$

				getButtonControl().setToolTipText(Messages.TextBackground);
				getButtonControl().addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent event) {
						Map<String,String> values = new HashMap<>();
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE,ITextEditor.BACKGROUND);
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_RED,
								String.valueOf(TextToolbarContribution.this.backgroundColor.red));
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_GREEN,
									String.valueOf(TextToolbarContribution.this.backgroundColor.green));
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_BLUE,
								String.valueOf(TextToolbarContribution.this.backgroundColor.blue));
						Object obj=STPAPluginUtils.executeParaCommand(XSTAMPP_COMMAND_CHOOSECOLOR, values);
						if(obj instanceof RGB){
							TextToolbarContribution.this.backgroundColor=(RGB) obj;
							setToolItemIcon(TextToolbarContribution.this.backgroundControl, (RGB) obj, ITextEditor.BACKGROUND);
						}
					}
				});
				return control;
			}
		};
		
		ButtonContribution backgroundChooser = new ButtonContribution("backgroundControl",SWT.ARROW|SWT.DOWN,15){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent event) {
						Map<String,String> values = new HashMap<>();
						values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE,ITextEditor.BACKGROUND);
						Object obj=STPAPluginUtils.executeParaCommand(XSTAMPP_COMMAND_CHOOSECOLOR, values);
						if(obj instanceof RGB){
							TextToolbarContribution.this.backgroundColor=(RGB) obj;
							setToolItemIcon(TextToolbarContribution.this.backgroundControl, (RGB) obj, ITextEditor.BACKGROUND);
						}
					}
				});
				return control;
			}
		};
		manager.add(backgroundControl);
		manager.add(backgroundChooser);
	}

	
	private void addBaseControls(ToolBarManager manager){
		this.baselineUpControl = new ButtonContribution("baselineUpControl"){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().setImage(Activator.getImageDescriptor(
						
						getImagePath() + "/font_big.ico").createImage()); //$NON-NLS-1$
					
				getButtonControl().setToolTipText("Increase font size"); //$NON-NLS-1$
				getButtonControl().addSelectionListener(new SelectionAdapter() {
			
					@Override
					public void widgetSelected(SelectionEvent e) {
						Map<String,String> values=new HashMap<>();
						values.put("xstampp.commandParameter.baseline", ITextEditor.INCREASE); //$NON-NLS-1$
						STPAPluginUtils.executeParaCommand("xstampp.command.baseline", values); //$NON-NLS-1$
					}
				});
				return control;
			}
		};
		
		this.baselineDownControl = new ButtonContribution("baselineDownControl"){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().setImage(Activator.getImageDescriptor(
						
						getImagePath() + "/font_sml.ico").createImage()); //$NON-NLS-1$
				getButtonControl().setToolTipText(Messages.DecreaseFontSize);
				getButtonControl().addSelectionListener(new SelectionAdapter() {
			
					@Override
					public void widgetSelected(SelectionEvent e) {
						Map<String,String> values=new HashMap<>();
						values.put("xstampp.commandParameter.baseline", ITextEditor.DECREASE); //$NON-NLS-1$
						STPAPluginUtils.executeParaCommand("xstampp.command.baseline", values); //$NON-NLS-1$
					}
				});
				return control;
			}
		};
		manager.add(baselineUpControl);
		manager.add(baselineDownControl);
	}
	/**
	 * Add list items to coolBar
	 * 
	 * @author Sebastian Sieber
	 * @param manager
	 *            ToolBar
	 */
	private void addListItems(ToolBarManager manager) {
		this.bulletListControl = new ButtonContribution("bulletListControl"){
			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				getButtonControl().setImage(Activator
								.getImageDescriptor(getImagePath()	+ "/para_bul.ico").createImage()); //$NON-NLS-1$
				getButtonControl().setToolTipText(Messages.BulletList);
				getButtonControl().addSelectionListener(new SelectionAdapter() {
		
					@Override
					public void widgetSelected(SelectionEvent event) {
						Map<String,String> values=new HashMap<>();
						values.put("xstampp.commandParameter.bulletlist", ITextEditor.DOT_LIST); //$NON-NLS-1$
						STPAPluginUtils.executeParaCommand("xstampp.command.addbulletlist", values); //$NON-NLS-1$
					}
				});
				return control;
			}
		};
	}
	
	

	/**
	 * Set different color icons for different color shades.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param rgb
	 *            RGB
	 */
	private void setToolItemIcon(ButtonContribution colorControl, RGB rgb, String state) {
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

		if (state == ITextEditor.FOREGROUND) {
			imagePath = "/colors/foreground"; //$NON-NLS-1$
		} else {
			imagePath = "/colors/background"; //$NON-NLS-1$
		}

		// set icon
		if (redShades.contains(rgb)) {
			colorControl.getButtonControl().setImage(Activator.getImageDescriptor(
					getImagePath() + imagePath
							+ "/textRed.ico").createImage()); //$NON-NLS-1$
		} else if (blackShades.contains(rgb)) {
			colorControl.getButtonControl().setImage(Activator.getImageDescriptor(
					getImagePath() + imagePath
							+ "/textBlack.ico") //$NON-NLS-1$
					.createImage());
		} else if (yellowShades.contains(rgb)) {
			colorControl.getButtonControl().setImage(Activator.getImageDescriptor(
					getImagePath() + imagePath
							+ "/textYellow.ico") //$NON-NLS-1$
					.createImage());
		} else if (greenShades.contains(rgb)) {
			colorControl.getButtonControl().setImage(Activator.getImageDescriptor(
					getImagePath() + imagePath
							+ "/textGreen.ico") //$NON-NLS-1$
					.createImage());
		} else if (purpleShades.contains(rgb)) {
			colorControl.getButtonControl().setImage(Activator.getImageDescriptor(
					getImagePath() + imagePath
							+ "/textPurple.ico") //$NON-NLS-1$
					.createImage());
		} else if (whiteShades.contains(rgb)) {
			colorControl.getButtonControl().setImage(Activator.getImageDescriptor(
					getImagePath() + imagePath
							+ "/textWhite.ico") //$NON-NLS-1$
					.createImage());
		} else if (blueShades.contains(rgb)) {
			colorControl.getButtonControl().setImage(Activator.getImageDescriptor(
					getImagePath() + imagePath
							+ "/textBlue.ico") //$NON-NLS-1$
					.createImage());
		} else if (grayShades.contains(rgb)) {
			colorControl.getButtonControl().setImage(Activator.getImageDescriptor(
					getImagePath() + imagePath
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

		
		// initialize
		if (redShades.size() == 0) {
			redShades.add(new RGB(_255,_128, _128));
			redShades.add(new RGB(_255, _0, _0));
			redShades.add(new RGB(_128, _64,_64));
			redShades.add(new RGB(_128, _0, _0));
			redShades.add(new RGB(_64, _0, _0));
		}

		if (blackShades.size() == 0) {
			blackShades.add(new RGB(_0, _0, _0));
		}

		if (yellowShades.size() == 0) {
			yellowShades.add(new RGB(_128,_128, _0));
			yellowShades.add(new RGB(_128, _64, _0));
			yellowShades.add(new RGB(_255,_128, _0));
			yellowShades.add(new RGB(_255,_128, _64));
			yellowShades.add(new RGB(_255,_255, _0));
			yellowShades.add(new RGB(_255,_255, _128));
		}

		if (greenShades.size() == 0) {
			greenShades.add(new RGB(_128,_255, _128));
			greenShades.add(new RGB(_128,_255, _0));
			greenShades.add(new RGB(_128,_128, _64));
			greenShades.add(new RGB(_64, _128,_128));
			greenShades.add(new RGB(_0, _255, _0));
			greenShades.add(new RGB(_0, _128, _0));
			greenShades.add(new RGB(_0, _64, _0));
			greenShades.add(new RGB(_0, _255,_128));
			greenShades.add(new RGB(_0, _255, _64));
			greenShades.add(new RGB(_0, _128,_128));
			greenShades.add(new RGB(_0, _128, _64));
			greenShades.add(new RGB(_0, _64, _64));
		}

		if (purpleShades.size() == 0) {
			purpleShades.add(new RGB(_255,_128, _192));
			purpleShades.add(new RGB(_255,_128, _255));
			purpleShades.add(new RGB(_128,_128, _192));
			purpleShades.add(new RGB(_255, _0,_255));
			purpleShades.add(new RGB(_128, _0, _64));
			purpleShades.add(new RGB(_255, _0,_128));
			purpleShades.add(new RGB(_128, _0,_128));
			purpleShades.add(new RGB(_128, _0,_255));
			purpleShades.add(new RGB(_64, _0, _64));
			purpleShades.add(new RGB(_64, _0, _128));
			purpleShades.add(new RGB(_128,_128, _255));
		}

		if (whiteShades.size() == 0) {
			whiteShades.add(new RGB(_255,_255, _255));
		}
		if (blueShades.size() == 0) {
			blueShades.add(new RGB(_128,_255, _255));
			blueShades.add(new RGB(_0, _255,_255));
			blueShades.add(new RGB(_0, _64, _128));
			blueShades.add(new RGB(_0, _0, _255));
			blueShades.add(new RGB(_0, _0, _128));
			blueShades.add(new RGB(_0, _128,_255));
			blueShades.add(new RGB(_0, _128,_192));
			blueShades.add(new RGB(_0, _0, _160));
			blueShades.add(new RGB(_0, _0, _64));
		}

		if (grayShades.size() == 0) {
			grayShades.add(new RGB(_128,_128, _128));
			grayShades.add(new RGB(_192,_192, _192));
		}

	}


	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(selection instanceof StyledTextSelection){
			
			this.fontSizeCombo.getComboControl().setText(((StyledTextSelection) selection).getFontSize() + "");
			if(((StyledTextSelection) selection).getFontSize() < 0){
				
				this.fontSizeCombo.getComboControl().setText(String.valueOf(PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getHeight()));
			}
			this.fontCombo.getComboControl().setText(((StyledTextSelection) selection).getFontName());
			if(this.fontCombo.getComboControl().getText().equals("")){
				this.fontCombo.getComboControl().setText(PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getName());
			}
		}
		
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		if(part instanceof ITextEditor){
			((ITextEditor) part).setEditToolContributor(this);
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) {
		// nothing to do here
	}

	@Override
	public void partClosed(IWorkbenchPart arg0) {
		// nothing to do here
	}

	@Override
	public void partDeactivated(IWorkbenchPart arg0) {
		// nothing to do here
	}

	@Override
	public void partOpened(IWorkbenchPart arg0) {
		// nothing to do here
	}

	private String getImagePath() {
		// TODO Auto-generated method stub
		return "icons/buttons/systemdescription";
	}
	@Override
	public boolean getBoldControl() {
		return this.boldControl.getButtonControl().getSelection();
	}

	@Override
	public boolean getItalicControl() {
		return this.italicControl.getButtonControl().getSelection();
	}
	@Override
	public boolean getStrikeoutControl() {
		return this.strikeoutControl.getButtonControl().getSelection();
	}
	@Override
	public boolean getUnderlineControl() {
		return this.underlineControl.getButtonControl().getSelection();
	}
	@Override
	public boolean getBulletListControl() {
		return this.bulletListControl.getButtonControl().getSelection();
	}
	@Override
	public Font getFont() {
		int size=Integer.parseInt(this.fontSizeCombo.getComboControl().getText());
		String name= this.fontCombo.getComboControl().getText();
		return new Font(null, name, size, SWT.NORMAL);
	}
	@Override
	public Color getForeground(){
		return new Color(null, this.foregroundColor);
	}
	@Override
	public Color getBackground(){
		return new Color(null, this.backgroundColor);
	}
	
	@Override
	public void dispose() {
		try{
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removeSelectionListener(this);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(this);
		}catch(NullPointerException e){
			// if there is a nullpointer one can't delete anything
		}
		super.dispose();
	}
	
	/**
	 * Get font names from system and sort alphabetically.
	 * 
	 * @author Sebastian Sieber,Lukas Balzer
	 * 
	 * @param composite
	 *            Composite
	 * @return names font names
	 */
	private static List<String> getFontNames() {
		FontData[] fontNames = PlatformUI.getWorkbench().getDisplay().getFontList(null, true);
		List<String> names = new ArrayList<String>();
		
		for (FontData fontName2 : fontNames) {
			String fontName = fontName2.getName();
			if (!(names.contains(fontName))) {
				if (!(fontName.substring(0, 1).equals(Messages.CharacterAt))) {
					names.add(fontName);
				}
			}
		}
		Collections.sort(names);
		
		return names;
	}
}

