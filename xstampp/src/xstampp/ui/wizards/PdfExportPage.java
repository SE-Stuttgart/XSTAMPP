/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.ui.wizards;

import java.io.NotActiveException;
import java.util.UUID;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import messages.Messages;
import xstampp.preferences.IPreferenceConstants;

/**
 * Create export page.
 * 
 * @author Sebastian Sieber, Lukas Balzer
 * 
 */
public class PdfExportPage extends AbstractExportPage implements ModifyListener {

  private Composite container;
  private Text textCompany;
  private ColorChooser bgChooser, fontChooser;
  private DemoCanvas sampleComp;
  private final IPreferenceStore store = xstampp.Activator.getDefault().getPreferenceStore();
  private Button decoSwitch;
  private PathComposite logoComposite;
  private boolean showCompanyFields;
  private boolean showDecorateCSButton;
  private boolean showPreviewCanvas;
  private boolean showColorChooser;
  private boolean showTextConfig;
  private boolean showFormatChooser;
  private String[] filterExtensions;
  private String[] filterNames;
  private Control topElement;

  /**
   * Constructor.
   * 
   * @author Sebastian Sieber,Lukas Balzer
   * 
   * @param pageName
   *          the name of the page
   * @param projectName
   *          the name of the project
   * @param pluginID
   *          the id of the calling plugin, used to resolve the projects suitable to the export
   * @deprecated Use {@link #PdfExportPage(String,String)} instead
   */
  public PdfExportPage(String pageName, String projectName, String pluginID) {
    this(new PDFExportConfiguration(pageName, projectName), PathComposite.PATH_DIALOG, pluginID);
  }

  /**
   * Constructor.
   * 
   * @author Sebastian Sieber,Lukas Balzer
   * 
   * @param pageName
   *          the name of the page
   * @param projectName
   *          the name of the project
   * @param pathConstant
   *          this is not longer been used
   * @param pluginID
   *          the id of the calling plugin, used to resolve the projects suitable to the export
   * @deprecated Use {@link #PdfExportPage(String,int,String)} instead
   */
  public PdfExportPage(String pageName, String projectName, int pathConstant, String pluginID) {
    this(new PDFExportConfiguration(pageName, projectName), pathConstant, pluginID);
  }
  
  public PdfExportPage(PDFExportConfiguration config, String pluginID) {
    this(config, PathComposite.PATH_DIALOG, pluginID);
  }

  /**
   * Constructor.
   * 
   * @author Sebastian Sieber,Lukas Balzer
   * 
   * @param config
   *          the configuration object contains the description of the export page
   * @param pathConstant
   *          this is not longer been used
   * @param pluginID
   *          the id of the calling plugin, used to resolve the projects suitable to the export
   */
  public PdfExportPage(PDFExportConfiguration config, int pathConstant, String pluginID) {
    super(config.pageName, pluginID);
    this.setTitle(config.projectName);
    this.setDescription(Messages.SetValuesForTheExportFile);
    showCompanyFields = config.isShowCompanyFields();
    showDecorateCSButton = config.isShowDecorateCSButton();
    showFormatChooser = config.isShowFormatChooser();
    showTextConfig = config.isShowTextConfig();
    showPreviewCanvas = config.isShowPreviewCanvas();
    showColorChooser = config.isShowColorChooser();
    this.filterExtensions = new String[] { "*.pdf" };
    this.filterNames = new String[] { "A-STPA Report *.pdf" };
  }

  @Override
  public void createControl(Composite parent) {
    topElement = null;
    FormData data;
    this.container = new Composite(parent, SWT.NONE);
    this.container.setLayout(new FormLayout());
    Composite projectChooser = null;
    if (needsAProject()) {
      projectChooser = this.addProjectChooser(this.container,
          new FormAttachment(null, AbstractWizardPage.COMPONENT_OFFSET));
      topElement = projectChooser;
      // -----Create the Company Name input Composite-------------------------
    }

    if (isShowCompanyFields()) {
      Composite labelComposite = new Composite(this.container, SWT.NONE);
      data = new FormData();
      data.top = new FormAttachment(topElement, AbstractWizardPage.COMPONENT_OFFSET);
      data.height = SWT.DEFAULT;
      data.right = new FormAttachment(100);
      data.left = new FormAttachment(0);
      labelComposite.setLayoutData(data);
      labelComposite.setLayout(new FormLayout());
      Label labelCompany = new Label(labelComposite, SWT.SHADOW_IN);
      data = new FormData();
      data.top = new FormAttachment(null);
      data.width = LABEL_WIDTH;
      data.left = LABEL_FORM_OFFSET;
      labelCompany.setLayoutData(data);
      labelCompany.setText(Messages.Company);

      this.textCompany = new Text(labelComposite, SWT.BORDER | SWT.SINGLE);
      data = new FormData();
      data.top = new FormAttachment(null);
      data.height = SWT.DEFAULT;
      data.left = new FormAttachment(labelCompany, COMPONENT_OFFSET);
      data.right = new FormAttachment(80);
      textCompany.setLayoutData(data);
      String companyName = this.store.getString(IPreferenceConstants.COMPANY_NAME);
      this.textCompany.addModifyListener(new ModifyListener() {

        @Override
        public void modifyText(ModifyEvent e) {
          store.setValue(IPreferenceConstants.COMPANY_NAME, textCompany.getText());

        }
      });
      if (companyName != null) {
        this.textCompany.setText(companyName);
        this.textCompany.setSelection(companyName.length());
      } else {
        this.textCompany.setText(""); //$NON-NLS-1$
      }

      // ----Create the logo path chooser composite---------------------------
      this.logoComposite = new PathComposite(null, null, this.container, PathComposite.LOGO_DIALOG,
          Messages.Logo);
      this.logoComposite.addTextListener(new ModifyListener() {

        @Override
        public void modifyText(ModifyEvent e) {
          store.setValue(IPreferenceConstants.COMPANY_LOGO, logoComposite.getText());
        }
      });
      String logoName = this.store.getString(IPreferenceConstants.COMPANY_LOGO);

      if (logoName != null) {
        this.logoComposite.setText(logoName);
      }
      data = new FormData();
      data.top = new FormAttachment(labelComposite, AbstractWizardPage.COMPONENT_OFFSET);
      data.height = SWT.DEFAULT;
      data.right = new FormAttachment(100);
      data.left = new FormAttachment(0);
      this.logoComposite.setLayoutData(data);
      this.logoComposite.setVisible(true);
      topElement = this.logoComposite;
    }
    // ----Create the background color chooser
    // composite---------------------------
    if(isShowColorChooser()) {
      this.bgChooser = new ColorChooser(this.container, SWT.NONE, Messages.BackgroundColor,
          IPreferenceConstants.COMPANY_BACKGROUND_COLOR);
      data = new FormData();
      data.top = new FormAttachment(topElement, AbstractWizardPage.COMPONENT_OFFSET);
      topElement = this.bgChooser;
      data.height = SWT.DEFAULT;
      data.right = new FormAttachment(100);
      data.left = new FormAttachment(0);
      this.bgChooser.setLayoutData(data);
      this.bgChooser.setVisible(true);
      this.bgChooser.addColorChangeListener(this);
  
      // ----Create the foreground color chooser
      // composite---------------------------
      this.fontChooser = new ColorChooser(this.container, SWT.NONE, Messages.FontColor,
          IPreferenceConstants.COMPANY_FONT_COLOR);
      data = new FormData();
      data.top = new FormAttachment(this.bgChooser, AbstractWizardPage.COMPONENT_OFFSET);
      topElement = this.fontChooser;
      data.height = SWT.DEFAULT;
      data.right = new FormAttachment(100);
      data.left = new FormAttachment(0);
      this.fontChooser.setLayoutData(data);
      this.fontChooser.setVisible(true);
      this.fontChooser.addColorChangeListener(this);
    }
    // ----Create the path chooser composite---------------------------
    this.pathChooser = new PathComposite(filterExtensions, // $NON-NLS-1$
        filterNames, // $NON-NLS-1$
        this.container, PathComposite.PATH_DIALOG);
    data = new FormData();
    data.top = new FormAttachment(topElement, AbstractWizardPage.COMPONENT_OFFSET);
    topElement = this.pathChooser;
    data.height = SWT.DEFAULT;
    data.right = new FormAttachment(100);
    data.left = new FormAttachment(0);
    this.pathChooser.setLayoutData(data);
    this.pathChooser.setVisible(true);

    if (isShowDecorateCSButton()) {
      // ----Creates a Composite for the switch which turns the decoration of
      // the control structure on/off
      Composite decoSwitchComposite = new Composite(this.container, SWT.NONE);
      data = new FormData();
      data.top = new FormAttachment(topElement, AbstractWizardPage.COMPONENT_OFFSET);
      decoSwitchComposite.setLayoutData(data);
      decoSwitchComposite.setLayout(new FormLayout());
      Label decoLabel = new Label(decoSwitchComposite, SWT.WRAP);
      decoLabel.setText(Messages.ControlStructureDeco);
      decoLabel.setToolTipText(Messages.CSDecoToolTip);
      FormData decoData = new FormData();
      decoData.left = LABEL_FORM_OFFSET;
      decoData.top = new FormAttachment(0);
      decoData.width = LABEL_WIDTH;
      decoLabel.setLayoutData(decoData);
      this.decoSwitch = new Button(decoSwitchComposite, SWT.CHECK);
      decoData = new FormData();
      decoData.left = new FormAttachment(decoLabel, COMPONENT_OFFSET);
      decoData.top = new FormAttachment(0);

      decoSwitch.setLayoutData(decoData);

      topElement = decoSwitchComposite;
    }

    if (showFormatChooser) {
      data = new FormData();
      data.left = new FormAttachment(4);
      data.right = new FormAttachment(96);
      data.top = new FormAttachment(topElement, AbstractWizardPage.COMPONENT_OFFSET);
      topElement = addFormatChooser(container, data, false);
    }

    if (showTextConfig) {
      data = new FormData();
      data.left = new FormAttachment(4);
      data.right = new FormAttachment(96);
      data.top = new FormAttachment(topElement, AbstractWizardPage.COMPONENT_OFFSET);
      Composite fontComposite = new Composite(container, SWT.NONE);
      fontComposite.setLayoutData(data);
      fontComposite.setLayout(new GridLayout(6, false));
      GridData textData = new GridData(SWT.FILL, SWT.CENTER, true, false);
      GridData gData = new GridData(SWT.FILL, SWT.CENTER, true, false);
      Label text = new Label(fontComposite, SWT.READ_ONLY);
      text.setText("Title size:");
      text.setLayoutData(textData);

      Combo textCombo = new Combo(fontComposite, SWT.None);
      textCombo.setItems(new String[] { "6", "8", "10", "12", "14", "16", "18", "20", "24" });
      textCombo.setText("12");
      textCombo.setLayoutData(gData);
      textCombo.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
          setTitleSize(Integer.parseInt(((Combo) e.getSource()).getText()));
        }
      });

      text = new Label(fontComposite, SWT.READ_ONLY);
      text.setText("Header size:");
      text.setLayoutData(textData);
      textCombo = new Combo(fontComposite, SWT.None);
      textCombo.setItems(new String[] { "6", "8", "10", "12", "14", "16", "18" });
      textCombo.setText("10");
      textCombo.setLayoutData(gData);
      textCombo.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
          setHeadSize(Integer.parseInt(((Combo) e.getSource()).getText()));
        }
      });

      text = new Label(fontComposite, SWT.READ_ONLY);
      text.setText("Text size:");
      text.setLayoutData(textData);
      textCombo = new Combo(fontComposite, SWT.DROP_DOWN);
      textCombo.setItems(new String[] { "6", "8", "10", "12", "14" });
      textCombo.setText("8");
      textCombo.setLayoutData(gData);
      textCombo.addSelectionListener(new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
          setContentSize(Integer.parseInt(((Combo) e.getSource()).getText()));
        }
      });

      topElement = fontComposite;
    }

    // ----Creates a Composite for a Canvas which provides a preview of the
    // projectname with the chosen fore-/background colors
    if (showPreviewCanvas) {
      this.sampleComp = new DemoCanvas(this.container, SWT.NONE);
      this.sampleComp.setTitleSize(12);
      this.sampleComp.setHeadSize(10);
      this.sampleComp.setContentSize(8);
      if (getProjectID() != null) {
        this.sampleComp.setProjectID(getProjectID());
      }
      data = new FormData();
      data.top = new FormAttachment(topElement, AbstractWizardPage.COMPONENT_OFFSET);
      data.left = new FormAttachment(4);
      data.right = new FormAttachment(96);
      data.height = DEMOCANVAS_HEIGHT;
      data.width = parent.getBounds().width;
      this.sampleComp.setLayoutData(data);
      topElement = this.sampleComp;
    }
    // Required to avoid an error in the system
    this.setControl(this.container);

  }

  protected Control getBottomControl() {
    return topElement;
  }

  /**
   * @return the textCompany
   */
  public Text getTextCompany() {
    return this.textCompany;
  }

  /**
   * @param textCompany
   *          sets the given value
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
    if (this.sampleComp != null) {
      this.sampleComp.redraw();
    }
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

  /**
   * @return the showCompanyFields
   */
  public boolean isShowCompanyFields() {
    return this.showCompanyFields;
  }

  /**
   * @param showCompanyFields
   *          the showCompanyFields to set
   */
  public void setShowCompanyFields(boolean showCompanyFields) {
    this.showCompanyFields = showCompanyFields;
  }

  /**
   * @return the showDecorateCSButton
   */
  public boolean isShowDecorateCSButton() {
    return this.showDecorateCSButton;
  }

  /**
   * @param showDecorateCSButton
   *          the showDecorateCSButton to set
   */
  public void setShowDecorateCSButton(boolean showDecorateCSButton) {
    this.showDecorateCSButton = showDecorateCSButton;
  }

  /**
   * @return the showFormatChooser
   */
  public boolean isShowFormatChooser() {
    return this.showFormatChooser;
  }

  /**
   * @param showFormatChooser
   *          the showFormatChooser to set
   */
  public void setShowFormatChooser(boolean showFormatChooser) {
    this.showFormatChooser = showFormatChooser;
  }

  /**
   * @param filterExtensions
   *          the filterExtensions to set
   */
  public void setFilterExtensions(String[] filterExtensions, String[] filterNames) {
    this.filterExtensions = filterExtensions;
    this.filterNames = filterNames;
  }

  @Override
  public void setContentSize(int contentSize) {
    super.setContentSize(contentSize);

    if (this.sampleComp != null) {
      this.sampleComp.setContentSize(contentSize);
    }
  }

  @Override
  public void setTitleSize(int titleSize) {
    super.setTitleSize(titleSize);

    if (this.sampleComp != null) {
      this.sampleComp.setTitleSize(titleSize);
    }
  }

  @Override
  public void setHeadSize(int headSize) {
    if (this.sampleComp != null) {
      this.sampleComp.setHeadSize(headSize);
    }
    super.setHeadSize(headSize);
  }

  /**
   * @param showTextConfig
   *          the showTextConfig to set
   */
  public void setShowTextConfig(boolean showTextConfig) {
    this.showTextConfig = showTextConfig;
  }

  /**
   * @return the showPreviewCanvas
   */
  public boolean isShowPreviewCanvas() {
    return this.showPreviewCanvas;
  }

  /**
   * @param showPreviewCanvas
   *          the showPreviewCanvas to set
   */
  public void setShowPreviewCanvas(boolean showPreviewCanvas) {
    this.showPreviewCanvas = showPreviewCanvas;
  }

  public boolean isShowColorChooser() {
    return showColorChooser;
  }

  public void setShowColorChooser(boolean showColorChooser) {
    this.showColorChooser = showColorChooser;
  }
}
