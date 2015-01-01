package xstampp.astpa.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import messages.Messages;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import xstampp.astpa.Activator;
import xstampp.astpa.ui.systemdescription.SystemDescriptionView;
import xstampp.ui.editors.interfaces.ITextEditContribution;
import xstampp.ui.editors.interfaces.ITextEditor;
import xstampp.ui.editors.interfaces.StyledTextSelection;
import xstampp.util.STPAPluginUtils;

/**
 *
 * @author Lukas Balzer
 *
 *@since version 2.0.0
 */
public class TextToolbarContribution extends WorkbenchWindowControlContribution implements ISelectionListener,IPartListener, ITextEditContribution{

	/**
	 * Contains different font sizes.
	 */
	private static final String[] FONT_SIZES = new String[] {
			"6", "8", "9", "10", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"11", "12", "14", "24", "36", "48" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	private Combo fontCombo;
	private Combo fontSizeCombo;
	private ToolItem boldControl, italicControl, strikeoutControl,
	foregroundControl, backgroundControl, baselineUpControl,
	baselineDownControl, underlineControl, bulletListControl;
	private SelectionListener listener;
	private final static String FONT_SIZE_PARAMETER="xstampp.commandParameter.fontsize"; //$NON-NLS-1$
	private final static String FONT_NAME_PARAMETER="xstampp.commandParameter.fontfamily"; //$NON-NLS-1$
	private final static String CHANGE_FONT_COMMAND="xstampp.command.choosefont"; //$NON-NLS-1$
	private RGB foregroundColor;
	private RGB backgroundColor;
	
	// RGB values
			final static int _255 = 255;
			final static int _0 = 0;
			final static int _64 = 64;
			final static int _128 = 128;
			final static int _192 = 192;
			final static int _160 = 160;
	@Override
	protected Control createControl(Composite arg0) {
		this.foregroundColor=new RGB(_0, _0, _0);
		this.backgroundColor=new RGB(_255, _255, _255);
		Composite comp=new Composite(arg0, SWT.None);
		comp.setLayout(new FormLayout());
		
		FormData formData = new FormData();
		formData.left= new FormAttachment(0,2);
		Composite styleComp=new Composite(comp, SWT.None);
		styleComp.setLayoutData(formData);
		styleComp.setLayout(new FormLayout());
		ToolBar styleTools= new ToolBar(styleComp, SWT.None);
		addStyleItems(styleTools);
		addColorItems(styleTools);
		
		formData = new FormData();
		formData.left= new FormAttachment(styleComp,2);
		this.fontCombo= new Combo(comp, SWT.DROP_DOWN);
		this.fontCombo.setItems(getFontNames().toArray(new String[0]));
		this.fontCombo.setText(PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getName());
		
		this.fontCombo.setLayoutData(formData);
		
		this.fontSizeCombo= new Combo(comp, SWT.DROP_DOWN);
		formData = new FormData();
		formData.left= new FormAttachment(this.fontCombo, 2);
		this.fontSizeCombo.setItems(FONT_SIZES);
		this.fontSizeCombo.setLayoutData(formData);
		this.fontSizeCombo.setText(String.valueOf(PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getHeight()));
		
		// create base toolBar and add controls
		formData = new FormData();
		formData.left= new FormAttachment(this.fontSizeCombo,2);
		Composite baseComp= new Composite(comp, SWT.None);
		baseComp.setLayoutData(formData);
		baseComp.setLayout(new FormLayout());
		ToolBar baseToolBar = new ToolBar(baseComp, SWT.FLAT);
		addBaseControls(baseToolBar);
		addListItems(baseToolBar);
				
		this.listener= new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String,String> params=new HashMap<>();
				params.put(FONT_NAME_PARAMETER,TextToolbarContribution.this.fontCombo.getText());
				params.put(FONT_SIZE_PARAMETER,TextToolbarContribution.this.fontSizeCombo.getText());
				if(params.get(FONT_NAME_PARAMETER) == null || !getFontNames().contains(params.get(FONT_NAME_PARAMETER))){
					
					params.put(FONT_NAME_PARAMETER, getFontNames().get(0));
				}
				if(params.get(FONT_SIZE_PARAMETER).equals("")){
					params.replace(FONT_SIZE_PARAMETER, FONT_SIZES[0]);
				}
				STPAPluginUtils.executeParaCommand(CHANGE_FONT_COMMAND, params);
			}
			
			
		};
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);
		this.fontCombo.addSelectionListener(this.listener);
		this.fontSizeCombo.addSelectionListener(this.listener);
		setEnabled(false);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addSelectionListener(this);
		return comp;
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

		// Bold item
		this.boldControl = new ToolItem(styleToolBar, SWT.CHECK);
		this.boldControl
				.setImage(Activator
						.getImageDescriptor(
								SystemDescriptionView.getImagePath()
										+ "/bold.ico").createImage()); //$NON-NLS-1$
		this.boldControl.setToolTipText(Messages.Bold);
		this.boldControl.addSelectionListener(new StyleSelectionListener(ITextEditor.BOLD));

		// Italic item
		this.italicControl = new ToolItem(styleToolBar, SWT.CHECK);
		this.italicControl.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/italic.ico").createImage()); //$NON-NLS-1$

		this.italicControl.setToolTipText(Messages.Italic);
		this.italicControl.addSelectionListener(new StyleSelectionListener(ITextEditor.ITALIC));

		// Underline item
		this.underlineControl = new ToolItem(styleToolBar, SWT.CHECK);
		this.underlineControl.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/underline.ico").createImage()); //$NON-NLS-1$

		this.underlineControl.setToolTipText(Messages.Underline);
		this.underlineControl.addSelectionListener(new StyleSelectionListener(ITextEditor.UNDERLINE));

		// Strike out item
		this.strikeoutControl = new ToolItem(styleToolBar, SWT.CHECK);
		this.strikeoutControl.setImage(Activator.getImageDescriptor(

		SystemDescriptionView.getImagePath() + "/strikeout.ico").createImage()); //$NON-NLS-1$

		this.strikeoutControl.setToolTipText(Messages.Strikeout);
		this.strikeoutControl.addSelectionListener(new StyleSelectionListener(ITextEditor.STRIKEOUT));
	}

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
	private void addColorItems(ToolBar textColorToolBar) {
		this.foregroundControl = new ToolItem(textColorToolBar, SWT.DROP_DOWN);
		this.foregroundControl

		.setImage(Activator.getImageDescriptor(
				SystemDescriptionView.getImagePath()
						+ "/colors/foreground/textBlack.ico").createImage()); //$NON-NLS-1$

		this.foregroundControl.setToolTipText(Messages.TextForeground);
		this.foregroundControl.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				Map<String,String> values = new HashMap<>();
				values.put("xstampp.commandParameter.color.type",ITextEditor.FOREGROUND);
				if(event.detail == SWT.NORMAL){
					values.put("xstampp.commandParameter.color.red",
								String.valueOf(TextToolbarContribution.this.foregroundColor.red));
					values.put("xstampp.commandParameter.color.green",
								String.valueOf(TextToolbarContribution.this.foregroundColor.green));
					values.put("xstampp.commandParameter.color.blue",
								String.valueOf(TextToolbarContribution.this.foregroundColor.blue));
				}
				Object obj=STPAPluginUtils.executeParaCommand("xstampp.command.choosecolor", values);
				if(obj instanceof RGB){
					TextToolbarContribution.this.foregroundColor=(RGB) obj;
					setToolItemIcon(TextToolbarContribution.this.foregroundControl, (RGB) obj, ITextEditor.FOREGROUND);
				}
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
				Map<String,String> values = new HashMap<>();
				values.put("xstampp.commandParameter.color.",ITextEditor.BACKGROUND);
				if(event.detail == SWT.NORMAL){
					values.put("xstampp.commandParameter.color.red",
								String.valueOf(TextToolbarContribution.this.backgroundColor.red));
					values.put("xstampp.commandParameter.color.green",
								String.valueOf(TextToolbarContribution.this.backgroundColor.green));
					values.put("xstampp.commandParameter.color.blue",
								String.valueOf(TextToolbarContribution.this.backgroundColor.blue));
				}
				Object obj=STPAPluginUtils.executeParaCommand("xstampp.command.choosecolor", values);
				if(obj instanceof RGB){
					TextToolbarContribution.this.backgroundColor=(RGB) obj;
					setToolItemIcon(TextToolbarContribution.this.backgroundControl, (RGB) obj, ITextEditor.BACKGROUND);
				}
			}
		});
	}
	
	
	
	

	/**
	 * Set different color icons for different color shades.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param rgb
	 *            RGB
	 */
	private void setToolItemIcon(ToolItem colorControl, RGB rgb, String state) {
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
	
	private void addBaseControls(ToolBar toolbar){
		this.baselineUpControl = new ToolItem(toolbar, SWT.PUSH);
		this.baselineUpControl.setImage(Activator.getImageDescriptor(
	
		SystemDescriptionView.getImagePath() + "/font_big.ico").createImage()); //$NON-NLS-1$
	
		this.baselineUpControl.setToolTipText("Increase font size"); //$NON-NLS-1$
		this.baselineUpControl.addSelectionListener(new SelectionAdapter() {
	
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String,String> values=new HashMap<>();
				values.put("xstampp.commandParameter.baseline", ITextEditor.INCREASE); //$NON-NLS-1$
				STPAPluginUtils.executeParaCommand("xstampp.command.baseline", values); //$NON-NLS-1$
			}
		});
	
		this.baselineDownControl = new ToolItem(toolbar, SWT.PUSH);
		this.baselineDownControl.setImage(Activator.getImageDescriptor(
	
		SystemDescriptionView.getImagePath() + "/font_sml.ico").createImage()); //$NON-NLS-1$
		this.baselineDownControl.setToolTipText(Messages.DecreaseFontSize);
		this.baselineDownControl.addSelectionListener(new SelectionAdapter() {
	
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String,String> values=new HashMap<>();
				values.put("xstampp.commandParameter.baseline", ITextEditor.DECREASE); //$NON-NLS-1$
				STPAPluginUtils.executeParaCommand("xstampp.command.baseline", values); //$NON-NLS-1$
			}
		});
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
				Map<String,String> values=new HashMap<>();
				values.put("xstampp.commandParameter.bulletlist", ITextEditor.DOT_LIST); //$NON-NLS-1$
				STPAPluginUtils.executeParaCommand("xstampp.command.addbulletlist", values); //$NON-NLS-1$
			}
		});
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(selection instanceof StyledTextSelection){
			
			this.fontSizeCombo.setText(((StyledTextSelection) selection).getFontSize() + "");
			if(((StyledTextSelection) selection).getFontSize() < 0){
				System.out.println(String.valueOf(PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getHeight()));
				this.fontSizeCombo.setText(String.valueOf(PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getHeight()));
			}
			this.fontCombo.setText(((StyledTextSelection) selection).getFontName());
			if(this.fontCombo.getText().equals("")){
				this.fontCombo.setText(PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getName());
			}
		}
		
	}

	private void setEnabled(boolean enabled){
		this.boldControl.setEnabled(enabled);
		this.backgroundControl.setEnabled(enabled);
		this.baselineDownControl.setEnabled(enabled);
		this.baselineUpControl.setEnabled(enabled);
		this.bulletListControl.setEnabled(enabled);
		this.foregroundControl.setEnabled(enabled);
		this.italicControl.setEnabled(enabled);
		this.strikeoutControl.setEnabled(enabled);
		this.fontCombo.setEnabled(enabled);
		this.fontSizeCombo.setEnabled(enabled);
		this.underlineControl.setEnabled(enabled);
		
		
	}
	@Override
	public void partActivated(IWorkbenchPart part) {
		if(part instanceof ITextEditor){
			((ITextEditor) part).setEditToolContributor(this);
			setEnabled(true);
		}else{
			setEnabled(false);
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
	
	@Override
	public boolean getBoldControl() {
		return this.boldControl.getSelection();
	}

	@Override
	public boolean getItalicControl() {
		return this.italicControl.getSelection();
	}
	@Override
	public boolean getStrikeoutControl() {
		return this.strikeoutControl.getSelection();
	}
	@Override
	public boolean getUnderlineControl() {
		return this.underlineControl.getSelection();
	}
	@Override
	public boolean getBulletListControl() {
		return this.bulletListControl.getSelection();
	}
	@Override
	public Font getFont() {
		int size=Integer.parseInt(this.fontSizeCombo.getText());
		String name= this.fontCombo.getText();
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
	
	
}

