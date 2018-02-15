/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.ui.wizards;

public class PDFExportConfiguration {
  private boolean showCompanyFields;
  private boolean showDecorateCSButton;
  private boolean showPreviewCanvas;

  private boolean showTextConfig;
  private boolean showFormatChooser;
  private boolean showColorChooser;
  String pageName;
  String projectName;
  
  public PDFExportConfiguration(String pageName, String projectName) {
    this.pageName = pageName;
    this.projectName = projectName;
    this.showCompanyFields = true;
    this.showDecorateCSButton = true;
    this.showPreviewCanvas = true;
    this.showTextConfig = true;
    this.showFormatChooser = true;
    this.showColorChooser = true;
  }

  public boolean isShowCompanyFields() {
    return showCompanyFields;
  }
  public void setShowCompanyFields(boolean showCompanyFields) {
    this.showCompanyFields = showCompanyFields;
  }
  public boolean isShowDecorateCSButton() {
    return showDecorateCSButton;
  }
  public void setShowDecorateCSButton(boolean showDecorateCSButton) {
    this.showDecorateCSButton = showDecorateCSButton;
  }
  public boolean isShowPreviewCanvas() {
    return showPreviewCanvas;
  }
  public void setShowPreviewCanvas(boolean showPreviewCanvas) {
    this.showPreviewCanvas = showPreviewCanvas;
  }
  public boolean isShowTextConfig() {
    return showTextConfig;
  }
  public void setShowTextConfig(boolean showTextConfig) {
    this.showTextConfig = showTextConfig;
  }
  public boolean isShowFormatChooser() {
    return showFormatChooser;
  }
  public void setShowFormatChooser(boolean showFormatChooser) {
    this.showFormatChooser = showFormatChooser;
  }

  public boolean isShowColorChooser() {
    return showColorChooser;
  }

  public void setShowColorChooser(boolean showColorChooser) {
    this.showColorChooser = showColorChooser;
  }
}
