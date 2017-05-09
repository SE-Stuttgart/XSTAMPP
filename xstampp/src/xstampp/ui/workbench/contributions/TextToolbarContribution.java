/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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

/**
 * This toolbar can be activated/used by implementing the ITextEditor Interface
 * 
 * @author Lukas Balzer
 * @author Sebastian Sieber
 *
 * @since version 2.0
 */
public class TextToolbarContribution extends WorkbenchWindowControlContribution
    implements ISelectionListener, IPartListener, ITextEditContribution {

  private static final String XSTAMPP_COMMAND_CHOOSECOLOR = "xstampp.command.choosecolor"; //$NON-NLS-1$
  private static final String XSTAMPP_COMMAND_PARAMETER_COLOR_BLUE = "xstampp.commandParameter.color.blue"; //$NON-NLS-1$
  private static final String XSTAMPP_COMMAND_PARAMETER_COLOR_GREEN = "xstampp.commandParameter.color.green"; //$NON-NLS-1$
  private static final String XSTAMPP_COMMAND_PARAMETER_COLOR_RED = "xstampp.commandParameter.color.red"; //$NON-NLS-1$
  private static final String XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE = "xstampp.commandParameter.color.type"; //$NON-NLS-1$
  private final static String FONT_SIZE_PARAMETER = "xstampp.commandParameter.fontsize"; //$NON-NLS-1$
  private final static String FONT_NAME_PARAMETER = "xstampp.commandParameter.fontfamily"; //$NON-NLS-1$
  private final static String CHANGE_FONT_COMMAND = "xstampp.command.choosefont"; //$NON-NLS-1$
  /**
   * Contains different font sizes.
   */
  private static final String[] FONT_SIZES = new String[] { "6", "8", "9", "10", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      "11", "12", "14", "24", "36", "48" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

  public static final FontData SYSTEM_FONT_DATA = Display.getDefault().getSystemFont()
      .getFontData()[0];
  public static final Font SYSTEM_FONT = Display.getDefault().getSystemFont();
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

  private class StyleSelectionListener extends SelectionAdapter {
    String style;

    public StyleSelectionListener(String style) {
      this.style = style;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
      Map<String, String> values = new HashMap<>();
      values.put("xstampp.commandParameter.style", this.style);
      STPAPluginUtils.executeParaCommand("xstampp.command.style", values);
    }
  }

  public void drawControl(Composite comp) {
    forceDraw = true;
    fill(comp);
  }

  @Override
  public boolean isDynamic() {
    return true;
  }

  @Override
  protected Control createControl(Composite arg0) {
    arg0.getParent().setRedraw(true);

    ToolBarManager manager = new ToolBarManager(SWT.HORIZONTAL);
    addStyleItems(manager);
    addColorItems(manager);
    addFontControl(manager);
    addBaseControls(manager);
    addListItems(manager);
    if (!forceDraw && System.getProperty("os.name").toLowerCase().contains("linux")) {
      return new ToolBar(arg0, SWT.NONE);
    }
    ToolBar toolBar = manager.createControl(arg0);

    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addSelectionListener(this);
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(this);

    arg0.getParent().setRedraw(true);
    return toolBar;
  }

  private void addFontControl(ToolBarManager manager) {
    this.foregroundColor = new RGB(_0, _0, _0);
    this.backgroundColor = new RGB(_255, _255, _255);

    this.fontCombo = new ComboContribiution("xstampp.text.styleCombo",
        SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL) {
      @Override
      protected Control createControl(Composite parent) {
        Control control = super.createControl(parent);
        getComboControl().setItems(getFontNames().toArray(new String[0]));
        getComboControl().setText(
            PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getName());
        getComboControl().addSelectionListener(new SelectionAdapter() {

          @Override
          public void widgetSelected(SelectionEvent e) {
            Map<String, String> params = new HashMap<>();
            params.put(FONT_NAME_PARAMETER,
                TextToolbarContribution.this.fontCombo.getComboControl().getText());
            if (params.get(FONT_NAME_PARAMETER) == null
                || !getFontNames().contains(params.get(FONT_NAME_PARAMETER))) {
              params.put(FONT_NAME_PARAMETER, getFontNames().get(0));
            }
            STPAPluginUtils.executeParaCommand(CHANGE_FONT_COMMAND, params);
          }

        });
        return control;
      }
    };

    this.fontSizeCombo = new ComboContribiution("xstampp.text.sizeCombo",
        SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL) {
      @Override
      protected Control createControl(Composite parent) {
        Control control = super.createControl(parent);
        getComboControl().setItems(FONT_SIZES);
        getComboControl().setText(String.valueOf(
            PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getHeight()));
        getComboControl().addSelectionListener(new SelectionAdapter() {

          @Override
          public void widgetSelected(SelectionEvent e) {
            Map<String, String> params = new HashMap<>();
            params.put(FONT_SIZE_PARAMETER,
                TextToolbarContribution.this.fontSizeCombo.getComboControl().getText());
            if (params.get(FONT_SIZE_PARAMETER).equals("")) {
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
   *          SelectionAdapter
   * @param manager
   *          ToolBar
   */
  private void addStyleItems(ToolBarManager manager) {

    // Bold item
    this.boldControl = new ButtonContribution("boldControl", SWT.TOGGLE);
    boldControl.setImage(Activator.getImageDescriptor(getImagePath() + "/bold.ico").createImage()); //$NON-NLS-1$
    boldControl.setToolTipText(Messages.Bold);
    boldControl.addSelectionListener(new StyleSelectionListener(ITextEditor.BOLD));
    manager.add(boldControl);

    // Italic item
    this.italicControl = new ButtonContribution("italicControl", SWT.TOGGLE);
    this.italicControl
        .setImage(Activator.getImageDescriptor(getImagePath() + "/italic.ico").createImage()); //$NON-NLS-1$

    this.italicControl.setToolTipText(Messages.Italic);
    this.italicControl.addSelectionListener(new StyleSelectionListener(ITextEditor.ITALIC));
    this.italicControl.setEnabled(true);
    manager.add(this.italicControl);

    // Underline item
    this.underlineControl = new ButtonContribution("underlineControl", SWT.TOGGLE);
    Image image = Activator.getImage(getImagePath() + "/underline.ico");
    this.underlineControl.setImage(image); // $NON-NLS-1$

    this.underlineControl.setToolTipText(Messages.Underline);
    this.underlineControl.addSelectionListener(new StyleSelectionListener(ITextEditor.UNDERLINE));
    manager.add(underlineControl);
    // Strike out item
    this.strikeoutControl = new ButtonContribution("strikeoutControl", SWT.TOGGLE);
    image = Activator.getImage(getImagePath() + "/strikeout.ico");
    this.strikeoutControl.setImage(image); // $NON-NLS-1$

    this.strikeoutControl.setToolTipText(Messages.Strikeout);
    this.strikeoutControl.addSelectionListener(new StyleSelectionListener(ITextEditor.STRIKEOUT));
    manager.add(strikeoutControl);
  }

  /**
   * Add font foreground and font background items to coolBar.
   * 
   * @author Sebastian Sieber
   * 
   * @param manager
   *          ToolBar
   * @param composite
   *          Composite
   */
  private void addColorItems(ToolBarManager manager) {
    this.foregroundControl = new ButtonContribution("foregroundControl");

    this.foregroundControl.setImage(Activator
        .getImageDescriptor(getImagePath() + "/colors/foreground/textBlack.ico").createImage()); //$NON-NLS-1$

    this.foregroundControl.setToolTipText(Messages.TextForeground);
    this.foregroundControl.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        Map<String, String> values = new HashMap<>();
        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE, ITextEditor.FOREGROUND);

        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_RED,
            String.valueOf(TextToolbarContribution.this.foregroundColor.red));
        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_GREEN,
            String.valueOf(TextToolbarContribution.this.foregroundColor.green));
        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_BLUE,
            String.valueOf(TextToolbarContribution.this.foregroundColor.blue));

        Object obj = STPAPluginUtils.executeParaCommand(XSTAMPP_COMMAND_CHOOSECOLOR, values);
        if (obj instanceof RGB) {
          TextToolbarContribution.this.foregroundColor = (RGB) obj;
          setToolItemIcon(TextToolbarContribution.this.foregroundControl, (RGB) obj,
              ITextEditor.FOREGROUND);
        }
      }
    });
    ButtonContribution forgroundChooser = new ButtonContribution("forgroundChooser",
        SWT.ARROW | SWT.DOWN, 15);
    forgroundChooser.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        Map<String, String> values = new HashMap<>();
        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE, ITextEditor.FOREGROUND);

        Object obj = STPAPluginUtils.executeParaCommand(XSTAMPP_COMMAND_CHOOSECOLOR, values);
        if (obj instanceof RGB) {
          TextToolbarContribution.this.foregroundColor = (RGB) obj;
          setToolItemIcon(TextToolbarContribution.this.foregroundControl, (RGB) obj,
              ITextEditor.FOREGROUND);
        }
      }
    });
    manager.add(foregroundControl);
    manager.add(forgroundChooser);

    this.backgroundControl = new ButtonContribution("backgroundControl");
    this.backgroundControl.setImage(Activator
        .getImageDescriptor(getImagePath() + "/colors/background/textWhite.ico").createImage()); //$NON-NLS-1$

    this.backgroundControl.setToolTipText(Messages.TextBackground);
    this.backgroundControl.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        Map<String, String> values = new HashMap<>();
        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE, ITextEditor.BACKGROUND);
        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_RED,
            String.valueOf(TextToolbarContribution.this.backgroundColor.red));
        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_GREEN,
            String.valueOf(TextToolbarContribution.this.backgroundColor.green));
        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_BLUE,
            String.valueOf(TextToolbarContribution.this.backgroundColor.blue));
        Object obj = STPAPluginUtils.executeParaCommand(XSTAMPP_COMMAND_CHOOSECOLOR, values);
        if (obj instanceof RGB) {
          TextToolbarContribution.this.backgroundColor = (RGB) obj;
          setToolItemIcon(TextToolbarContribution.this.backgroundControl, (RGB) obj,
              ITextEditor.BACKGROUND);
        }
      }
    });

    ButtonContribution backgroundChooser = new ButtonContribution("backgroundControl",
        SWT.ARROW | SWT.DOWN, 15);
    backgroundChooser.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        Map<String, String> values = new HashMap<>();
        values.put(XSTAMPP_COMMAND_PARAMETER_COLOR_TYPE, ITextEditor.BACKGROUND);
        Object obj = STPAPluginUtils.executeParaCommand(XSTAMPP_COMMAND_CHOOSECOLOR, values);
        if (obj instanceof RGB) {
          TextToolbarContribution.this.backgroundColor = (RGB) obj;
          setToolItemIcon(TextToolbarContribution.this.backgroundControl, (RGB) obj,
              ITextEditor.BACKGROUND);
        }
      }
    });
    manager.add(backgroundControl);
    manager.add(backgroundChooser);
  }

  private void addBaseControls(ToolBarManager manager) {
    this.baselineUpControl = new ButtonContribution("baselineUpControl");
    this.baselineUpControl.setImage(Activator.getImageDescriptor(

        getImagePath() + "/font_big.ico").createImage()); //$NON-NLS-1$

    this.baselineUpControl.setToolTipText("Increase font size"); //$NON-NLS-1$
    this.baselineUpControl.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        Map<String, String> values = new HashMap<>();
        values.put("xstampp.commandParameter.baseline", ITextEditor.INCREASE); //$NON-NLS-1$
        STPAPluginUtils.executeParaCommand("xstampp.command.baseline", values); //$NON-NLS-1$
      }
    });

    this.baselineDownControl = new ButtonContribution("baselineDownControl");
    baselineDownControl.setImage(Activator.getImageDescriptor(

        getImagePath() + "/font_sml.ico").createImage()); //$NON-NLS-1$
    baselineDownControl.setToolTipText(Messages.DecreaseFontSize);
    baselineDownControl.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        Map<String, String> values = new HashMap<>();
        values.put("xstampp.commandParameter.baseline", ITextEditor.DECREASE); //$NON-NLS-1$
        STPAPluginUtils.executeParaCommand("xstampp.command.baseline", values); //$NON-NLS-1$
      }
    });
    manager.add(baselineUpControl);
    manager.add(baselineDownControl);
  }

  /**
   * Add list items to coolBar
   * 
   * @author Sebastian Sieber
   * @param manager
   *          ToolBar
   */
  private void addListItems(ToolBarManager manager) {
    this.bulletListControl = new ButtonContribution("bulletListControl");
    this.bulletListControl
        .setImage(Activator.getImageDescriptor(getImagePath() + "/para_bul.ico").createImage()); //$NON-NLS-1$
    this.bulletListControl.setToolTipText(Messages.BulletList);
    this.bulletListControl.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        Map<String, String> values = new HashMap<>();
        values.put("xstampp.commandParameter.bulletlist", ITextEditor.DOT_LIST); //$NON-NLS-1$
        STPAPluginUtils.executeParaCommand("xstampp.command.addbulletlist", values); //$NON-NLS-1$
      }
    });
  }

  /**
   * Set different color icons for different color shades.
   * 
   * @author Sebastian Sieber
   * 
   * @param rgb
   *          RGB
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

    this.initShadeLists(redShades, blackShades, yellowShades, greenShades, purpleShades,
        whiteShades, blueShades, grayShades);

    if (state == ITextEditor.FOREGROUND) {
      imagePath = "/colors/foreground"; //$NON-NLS-1$
    } else {
      imagePath = "/colors/background"; //$NON-NLS-1$
    }

    // set icon
    String string = null;
    if (redShades.contains(rgb)) {
      string = ((getImagePath() + imagePath + "/textRed.ico")); //$NON-NLS-1$
    } else if (blackShades.contains(rgb)) {
      string = ((getImagePath() + imagePath + "/textBlack.ico")); //$NON-NLS-1$
      colorControl.setImage(Activator.getImage(string));
    } else if (yellowShades.contains(rgb)) {
      string = ((getImagePath() + imagePath + "/textYellow.ico")); //$NON-NLS-1$
      colorControl.setImage(Activator.getImage(string));
    } else if (greenShades.contains(rgb)) {
      string = ((getImagePath() + imagePath + "/textGreen.ico")); //$NON-NLS-1$
      colorControl.setImage(Activator.getImage(string));
    } else if (purpleShades.contains(rgb)) {
      string = ((getImagePath() + imagePath + "/textPurple.ico")); //$NON-NLS-1$
    } else if (whiteShades.contains(rgb)) {
      string = ((getImagePath() + imagePath + "/textWhite.ico")); //$NON-NLS-1$
    } else if (blueShades.contains(rgb)) {
      string = ((getImagePath() + imagePath + "/textBlue.ico")); //$NON-NLS-1$
    } else if (grayShades.contains(rgb)) {
      string = ((getImagePath() + imagePath + "/textGrey.ico")); //$NON-NLS-1$
    }
    colorControl.setImage(Activator.getImage(string));

  }

  /**
   * Initialize ArrayList with RGB values of color shades.
   * 
   * @author Sebastian Sieber
   * 
   */
  private void initShadeLists(ArrayList<RGB> redShades, ArrayList<RGB> blackShades,
      ArrayList<RGB> yellowShades, ArrayList<RGB> greenShades, ArrayList<RGB> purpleShades,
      ArrayList<RGB> whiteShades, ArrayList<RGB> blueShades, ArrayList<RGB> grayShades) {

    // initialize
    if (redShades.size() == 0) {
      redShades.add(new RGB(_255, _128, _128));
      redShades.add(new RGB(_255, _0, _0));
      redShades.add(new RGB(_128, _64, _64));
      redShades.add(new RGB(_128, _0, _0));
      redShades.add(new RGB(_64, _0, _0));
    }

    if (blackShades.size() == 0) {
      blackShades.add(new RGB(_0, _0, _0));
    }

    if (yellowShades.size() == 0) {
      yellowShades.add(new RGB(_128, _128, _0));
      yellowShades.add(new RGB(_128, _64, _0));
      yellowShades.add(new RGB(_255, _128, _0));
      yellowShades.add(new RGB(_255, _128, _64));
      yellowShades.add(new RGB(_255, _255, _0));
      yellowShades.add(new RGB(_255, _255, _128));
    }

    if (greenShades.size() == 0) {
      greenShades.add(new RGB(_128, _255, _128));
      greenShades.add(new RGB(_128, _255, _0));
      greenShades.add(new RGB(_128, _128, _64));
      greenShades.add(new RGB(_64, _128, _128));
      greenShades.add(new RGB(_0, _255, _0));
      greenShades.add(new RGB(_0, _128, _0));
      greenShades.add(new RGB(_0, _64, _0));
      greenShades.add(new RGB(_0, _255, _128));
      greenShades.add(new RGB(_0, _255, _64));
      greenShades.add(new RGB(_0, _128, _128));
      greenShades.add(new RGB(_0, _128, _64));
      greenShades.add(new RGB(_0, _64, _64));
    }

    if (purpleShades.size() == 0) {
      purpleShades.add(new RGB(_255, _128, _192));
      purpleShades.add(new RGB(_255, _128, _255));
      purpleShades.add(new RGB(_128, _128, _192));
      purpleShades.add(new RGB(_255, _0, _255));
      purpleShades.add(new RGB(_128, _0, _64));
      purpleShades.add(new RGB(_255, _0, _128));
      purpleShades.add(new RGB(_128, _0, _128));
      purpleShades.add(new RGB(_128, _0, _255));
      purpleShades.add(new RGB(_64, _0, _64));
      purpleShades.add(new RGB(_64, _0, _128));
      purpleShades.add(new RGB(_128, _128, _255));
    }

    if (whiteShades.size() == 0) {
      whiteShades.add(new RGB(_255, _255, _255));
    }
    if (blueShades.size() == 0) {
      blueShades.add(new RGB(_128, _255, _255));
      blueShades.add(new RGB(_0, _255, _255));
      blueShades.add(new RGB(_0, _64, _128));
      blueShades.add(new RGB(_0, _0, _255));
      blueShades.add(new RGB(_0, _0, _128));
      blueShades.add(new RGB(_0, _128, _255));
      blueShades.add(new RGB(_0, _128, _192));
      blueShades.add(new RGB(_0, _0, _160));
      blueShades.add(new RGB(_0, _0, _64));
    }

    if (grayShades.size() == 0) {
      grayShades.add(new RGB(_128, _128, _128));
      grayShades.add(new RGB(_192, _192, _192));
    }

  }

  @Override
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    if (selection instanceof StyledTextSelection) {
      StyledTextSelection styleSelection = (StyledTextSelection) selection;
      this.boldControl.setSelection((styleSelection.getFontStyle() & SWT.BOLD) != 0);
      this.italicControl.setSelection((styleSelection.getFontStyle() & SWT.ITALIC) != 0);

      this.underlineControl.setSelection(styleSelection.isUnderline());
      this.strikeoutControl.setSelection(styleSelection.isStrikeout());

      this.fontSizeCombo.getComboControl().setText(styleSelection.getFontSize() + "");
      if (styleSelection.getFontSize() < 0) {
        this.fontSizeCombo.getComboControl().setText(String.valueOf(
            PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getHeight()));
      }
      this.fontCombo.getComboControl().setText(styleSelection.getFontName());
      if (this.fontCombo.getComboControl().getText().equals("")) {
        this.fontCombo.getComboControl().setText(
            PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getName());
      }
    } else {
      this.boldControl.setSelection(false);
      this.italicControl.setSelection(false);

      this.underlineControl.setSelection(false);
      this.strikeoutControl.setSelection(false);
      this.fontSizeCombo.getComboControl().setText(String.valueOf(
          PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getHeight()));
      this.fontCombo.getComboControl().setText(
          PlatformUI.getWorkbench().getDisplay().getSystemFont().getFontData()[0].getName());
    }

  }

  @Override
  public void partActivated(IWorkbenchPart part) {
    if (part instanceof ITextEditor) {
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
    int size = Integer.parseInt(this.fontSizeCombo.getComboControl().getText());
    String name = this.fontCombo.getComboControl().getText();
    return new Font(null, name, size, SWT.NORMAL);
  }

  @Override
  public Color getForeground() {
    return new Color(null, this.foregroundColor);
  }

  @Override
  public Color getBackground() {
    return new Color(null, this.backgroundColor);
  }

  @Override
  public void dispose() {
    try {
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
          .removeSelectionListener(this);
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().removePartListener(this);
    } catch (NullPointerException e) {
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
   *          Composite
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
