/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.wizards;

import messages.Messages;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import xstampp.Activator;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.contentassist.LabelWithAssist;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.util.JAXBExportJob;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * a class to prevent code cloning in the export Pages
 * 
 * @author Lukas Balzer
 * 
 */
public abstract class AbstractExportPage extends AbstractWizardPage implements IExportPage {

  protected class DemoCanvas extends Canvas implements PaintListener {
    private String title;
    public static final int labelFormOffset = 15;
    private Font normalFont = new Font(null, "normalfont", 10, SWT.NORMAL); //$NON-NLS-1$
    private Font headerFont = new Font(null, "font", 14, SWT.NORMAL); //$NON-NLS-1$
    private Font titleFont = new Font(null, "font", 14, SWT.NORMAL); //$NON-NLS-1$
    private Font previewFont = new Font(null, "font", 14, SWT.NORMAL); //$NON-NLS-1$
    private Color fontColor;
    private Color bgColor;
    private UUID projectId;
    private static final int string_xPos = 10;
    private static final int string_yPos = 0;
    private static final int PREVIEW_XPOS = 8;

    public DemoCanvas(Composite parent, int style) {
      super(parent, style);
      this.addPaintListener(this);
    }

    public void setProjectID(UUID id) {
      this.projectId = id;
      this.redraw();
    }

    /**
     * @param contentSize
     *          the contentSize to set
     */
    public void setContentSize(int contentSize) {
      this.normalFont.dispose();
      this.normalFont = new Font(null, "normalFont", contentSize, SWT.NORMAL); //$NON-NLS-1$
      redraw();
    }

    /**
     * @param headSize
     *          the headSize to set
     */
    public void setHeadSize(int headSize) {
      this.headerFont.dispose();
      this.headerFont = new Font(null, "font", headSize, SWT.NORMAL); //$NON-NLS-1$
      redraw();
    }

    /**
     * @param headSize
     *          the headSize to set
     */
    public void setTitleSize(int size) {
      this.titleFont.dispose();
      this.titleFont = new Font(null, "font", size, SWT.NORMAL); //$NON-NLS-1$
      redraw();
    }

    @Override
    public void paintControl(PaintEvent e) {
      if (this.projectId == null) {
        title = Messages.NewProject;
      } else {
        title = ProjectManager.getContainerInstance().getTitle(this.projectId);
      }
      e.gc.setFont(DemoCanvas.this.titleFont);
      int top_offset = e.gc.getFontMetrics().getHeight();
      e.gc.setFont(DemoCanvas.this.headerFont);
      int title_offset = top_offset + e.gc.getFontMetrics().getHeight();
      e.gc.setFont(DemoCanvas.this.normalFont);
      int text_offset = title_offset + e.gc.getFontMetrics().getHeight();
      e.gc.setBackground(ColorConstants.white);
      e.gc.fillRectangle(0, top_offset / 2, 400, text_offset);

      setSize(400, text_offset);
      this.fontColor = new Color(null,
          PreferenceConverter.getColor(store, IPreferenceConstants.COMPANY_FONT_COLOR));
      this.bgColor = new Color(null,
          PreferenceConverter.getColor(store, IPreferenceConstants.COMPANY_BACKGROUND_COLOR));
      e.gc.setForeground(this.fontColor);
      e.gc.setFont(DemoCanvas.this.headerFont);
      e.gc.setBackground(this.bgColor);
      e.gc.drawString(title, DemoCanvas.PREVIEW_XPOS, top_offset, false);
      e.gc.setFont(DemoCanvas.this.normalFont);
      e.gc.drawString(Messages.AbstractExportPage_SampleText, DemoCanvas.PREVIEW_XPOS + 6,
          title_offset, true);
      e.gc.setForeground(ColorConstants.black);
      e.gc.setFont(DemoCanvas.this.titleFont);
      e.gc.drawText(Messages.Preview, DemoCanvas.string_xPos, DemoCanvas.string_yPos, true);
    }
  }

  public static final String EXPORT_DATA = Messages.AbstractExportPage_ExportTheData;

  /**
   * a constant which is used by a {@link JAXBExportJob} sets it as a parameter in the creation of
   * the pdf to print in landscape mode
   */
  public static final String A4_LANDSCAPE = "A4Landscape"; //$NON-NLS-1$

  /**
   * a constant which is used by a {@link JAXBExportJob} sets it as a parameter in the creation of
   * the pdf to print in portrait mode
   */
  public static final String A4_PORTRAIT = "A4"; //$NON-NLS-1$
  public static final String NON = Messages.AbstractExportPage_4;
  public static final String EXPORT = Messages.AbstractExportPage_5;
  private Map<String, UUID> projects;
  private Combo chooseList;
  protected PathComposite pathChooser;
  private String pluginID;
  private String nameSuggestion;
  protected String pageFormat;
  private boolean needProjectID;
  private int contentSize;
  private int headSize;
  private int titleSize;

  /**
   * 
   *
   * @author Lukas Balzer
   *
   * @param pageName
   *          the name of the export page
   * @param pluginID
   *          The plugin for which this export page is created
   */
  protected AbstractExportPage(String pageName, String pluginID) {
    super(pageName);
    this.projects = new HashMap<>();
    this.pluginID = pluginID;
    this.pageFormat = A4_LANDSCAPE;
    this.needProjectID = true;
    contentSize = 10;
    headSize = 12;
    titleSize = 14;
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param path
   *          the path as String
   */
  public void setExportPath(String path) {
    this.pathChooser.setText(path);

  }

  @Override
  protected String openExportDialog(String[] filters, String[] names) {
    FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
        SWT.SAVE);
    fileDialog.setFilterExtensions(filters);
    fileDialog.setFilterNames(names);
    if (this.nameSuggestion != null) {
      fileDialog.setFileName(nameSuggestion);
    } else {
      fileDialog.setFileName(this.getProjectName());
    }
    String filePath = fileDialog.open();
    if (filePath != null) {
      this.setExportPath(filePath);
      this.setPageComplete(this.checkFinish());
      return filePath;
    }

    return ""; ////$NON-NLS-1$
  }

  /**
   * adds a drop down list to the wizard page where the user can choose between all open projects
   * <br>
   * <i>the parent must have a FormLayout</i>
   * 
   * @author Lukas Balzer
   * @param parent
   *          the parent composite
   * @param attachment
   *          the
   * @return the composite containing the project drop down list
   * 
   */
  public Composite addProjectChooser(Composite parent, FormAttachment attachment) {
    FormData data = new FormData();
    data.top = attachment;
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(100);
    final Composite projectChooser = new Composite(parent, SWT.NONE);
    projectChooser.setToolTipText(Messages.ChooseProjectForExport);
    projectChooser.setLayoutData(data);
    projectChooser.setLayout(new FormLayout());

    Label chooseLabel = new Label(projectChooser, SWT.None);
    data = new FormData();
    data.top = new FormAttachment(null);
    data.left = new FormAttachment(0);
    data.width = LABEL_WIDTH;
    chooseLabel.setLayoutData(data);
    chooseLabel.setText(Messages.Project);

    this.chooseList = new Combo(projectChooser, SWT.DROP_DOWN);
    data = new FormData();
    data.top = new FormAttachment(null);
    data.left = new FormAttachment(chooseLabel, COMPONENT_OFFSET);
    data.right = new FormAttachment(80);
    this.chooseList.setLayoutData(data);

    this.chooseList.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        projectChooser.notifyListeners(SWT.Selection, null);
        setProjectID(projects.get(chooseList.getText()));

        AbstractExportPage.this.setPageComplete(AbstractExportPage.this.checkFinish());

      }
    });
    for (Entry<UUID, String> entry : ProjectManager.getContainerInstance().getProjects()
        .entrySet()) {
      if (canExport(entry.getKey())) {
        this.projects.put(entry.getValue(), entry.getKey());
        this.chooseList.add(entry.getValue());
      }
    }
    if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView("astpa.explorer") != null) { //$NON-NLS-1$
      ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
          .findView("astpa.explorer").getSite().getSelectionProvider().getSelection(); //$NON-NLS-1$

      if (selection instanceof IProjectSelection && this.projects
          .containsKey(((IProjectSelection) selection).getProjectData().getProjectName())) {
        this.setProjectID(((IProjectSelection) selection).getProjectId());
        this.chooseList.setText(((IProjectSelection) selection).getProjectData().getProjectName());
      }
    }
    return projectChooser;

  }

  protected void addLabelWithAssist(Composite parent, Object layoutData, String title, String tip) {
    Composite text = new LabelWithAssist(parent, SWT.None, title, tip);
    text.setLayoutData(layoutData);
  }

  protected Label addFormatWidget(Composite composite, Object layoutData, String title,
      final boolean chooseFormat) {
    Composite widget = new Composite(composite, SWT.NONE);
    widget.setLayoutData(layoutData);
    widget.setLayout(new GridLayout(2, false));
    final Label chooser = new Label(widget, SWT.None);
    chooser.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, true));
    if (chooseFormat) {
      chooser.setImage(Activator.getImageDescriptor("/icons/exportPortrait.png").createImage()); // $NON-NLS-1$
      chooser.setData(EXPORT_DATA, A4_PORTRAIT);
      chooser.setToolTipText(Messages.AbstractExportPage_ExportInA4PortraitFormat);
    } else {
      chooser.setImage(
          PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
      chooser.setData(EXPORT_DATA, EXPORT);
      chooser.setToolTipText(Messages.AbstractExportPage_includeInExport);
    }
    MouseAdapter listener = new MouseAdapter() {
      @Override
      public void mouseUp(MouseEvent e) {
        if (chooser.getData(EXPORT_DATA).equals(A4_PORTRAIT)) {
          chooser
              .setImage(Activator.getImageDescriptor("/icons/exportLandscape.png").createImage()); //$NON-NLS-1$
          chooser.setData(EXPORT_DATA, A4_LANDSCAPE);
          chooser.setToolTipText(Messages.AbstractExportPage_ExportInA4LandscapeFormat);
        } else if (chooser.getData(EXPORT_DATA).equals(A4_LANDSCAPE)) {
          chooser.setImage(
              PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVE));
          chooser.setData(EXPORT_DATA, NON);
          chooser.setToolTipText(Messages.AbstractExportPage_excludeInExport);
        } else if (chooser.getData(EXPORT_DATA).equals(NON) && !chooseFormat) {
          chooser.setImage(
              PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
          chooser.setData(EXPORT_DATA, EXPORT);
          chooser.setToolTipText(Messages.AbstractExportPage_includeInExport);
        } else if (chooser.getData(EXPORT_DATA).equals(EXPORT)) {
          chooser.setImage(
              PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVE));
          chooser.setData(EXPORT_DATA, NON);
          chooser.setToolTipText(Messages.AbstractExportPage_excludeInExport);
        } else {
          chooser.setImage(Activator.getImageDescriptor("/icons/exportPortrait.png").createImage()); //$NON-NLS-1$
          chooser.setData(EXPORT_DATA, A4_PORTRAIT);
          chooser.setToolTipText(Messages.AbstractExportPage_ExportInA4PortraitFormat);
        }
      }
    };
    chooser.addMouseListener(listener);
    final Label textLabel = new Label(widget, SWT.NO);
    textLabel.setText(title);
    textLabel.addMouseListener(listener);
    textLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    // widget.setSize(widget.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    return chooser;
  }

  /**
   * @param composite
   * @param minimal
   *          TODO
   */
  protected Control addFormatChooser(Composite composite, Object layoutData, boolean minimal) {
    if (minimal) {
      final Label chooser = new Label(composite, SWT.None);
      chooser.setLayoutData(layoutData);
      chooser.setImage(Activator.getImageDescriptor("/icons/exportPortrait.png").createImage()); //$NON-NLS-1$
      chooser.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseUp(MouseEvent e) {
          if (pageFormat.equals(A4_PORTRAIT)) {
            chooser
                .setImage(Activator.getImageDescriptor("/icons/exportLandscape.png").createImage()); //$NON-NLS-1$
            pageFormat = A4_LANDSCAPE;
          } else if (pageFormat.equals(A4_LANDSCAPE)) {
            chooser.setImage(PlatformUI.getWorkbench().getSharedImages()
                .getImage(ISharedImages.IMG_ELCL_REMOVE));
            pageFormat = ""; //$NON-NLS-1$
          } else {
            chooser
                .setImage(Activator.getImageDescriptor("/icons/exportPortrait.png").createImage()); //$NON-NLS-1$
            pageFormat = A4_PORTRAIT;
          }
        }
      });
      return chooser;
    } else {
      Group formatGroup = new Group(composite, SWT.NONE);
      formatGroup.setLayoutData(layoutData);
      formatGroup.setLayout(new GridLayout(2, true));
      Button chFormat = null;
      for (final String format : new String[] { A4_LANDSCAPE, A4_PORTRAIT }) {
        chFormat = new Button(formatGroup, SWT.RADIO);
        chFormat.setText(format);
        chFormat.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        chFormat.addSelectionListener(new SelectionAdapter() {

          @Override
          public void widgetSelected(SelectionEvent e) {
            pageFormat = format;
          }
        });
        if (format.equals(pageFormat)) {
          chFormat.setSelection(true);
        }
      }
      return formatGroup;
    }

  }

  @Override
  public String getExportPath() {
    if (this.pathChooser == null) {
      ProjectManager.getLOGGER().debug("AbstractExportPage must define a pathChooser"); //$NON-NLS-1$
      return null;
    }
    return this.pathChooser.getText();
  }

  public void setNeedProject(boolean needProjectID) {
    this.needProjectID = needProjectID;

  }

  public boolean needsAProject() {
    return needProjectID;
  }

  @Override
  public boolean checkFinish() {
    this.setErrorMessage(null);
    if (this.getProjectID() == null && needProjectID) {
      this.setErrorMessage(Messages.NoProjectSelected);
      return false;
    }
    if ((this.getExportPath() == null) || this.getExportPath().equals("")) { //$NON-NLS-1$
      this.setErrorMessage(Messages.IlegalPath);
      return false;
    }
    File fileTmp = new File(this.getExportPath());
    if (fileTmp.exists()) {
      this.setMessage(
          String.format(Messages.DoYouReallyWantToOverwriteTheContentAt, getExportPath()),
          IMessageProvider.WARNING);
    }
    return true;
  }

  @Override
  protected void setControl(Control newControl) {
    this.setPageComplete(this.checkFinish());
    super.setControl(newControl);
  }

  /**
   * @return the nameSuggestion
   */
  public String getNameSuggestion() {
    return nameSuggestion;
  }

  /**
   * @param nameSuggestion
   *          the nameSuggestion to set
   */
  public void setNameSuggestion(String nameSuggestion) {
    this.nameSuggestion = nameSuggestion;
  }

  public boolean canExport(UUID id) {
    return ProjectManager.getContainerInstance().getDataModel(id).getPluginID()
        .equals(this.pluginID);
  }
  // public void setProjectChoice(UUID projectId){
  // if(this.projects.containsKey(projectId)){
  // String projectName = this.projects.get(projectId);
  // for(int i=0;i<this.chooseList.getItemCount();i++){
  // if(projectName.equals(this.chooseList.getItems()[i])){
  // this.chooseList.select(i);
  // }
  // }
  // }
  // }

  /**
   * @return the pageFormat
   */
  public String getPageFormat() {
    return this.pageFormat;
  }

  /**
   * @return the contentSize
   */
  public int getContentSize() {
    return this.contentSize;
  }

  /**
   * @param contentSize
   *          the contentSize to set
   */
  public void setContentSize(int contentSize) {
    this.contentSize = contentSize;
  }

  /**
   * @return the headSize
   */
  public int getHeadSize() {
    return this.headSize;
  }

  /**
   * @param headSize
   *          the headSize to set
   */
  public void setHeadSize(int headSize) {
    this.headSize = headSize;
  }

  /**
   * @return the titleSize
   */
  public int getTitleSize() {
    return this.titleSize;
  }

  /**
   * @param titleSize
   *          the titleSize to set
   */
  public void setTitleSize(int titleSize) {
    this.titleSize = titleSize;
  }
}
