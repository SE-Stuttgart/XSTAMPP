/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer- initial API and implementation
 ******************************************************************************/
package xstampp.util;

public class ExportPackage {
  private String name;
  private String filePath;
  private String xslName;
  private Class<?> DataModelClazz;
  private float textSize;
  private float titleSize;
  private float tableHeadSize;

  public ExportPackage(String name, String filePath, String xslName, Class<?> clazz) {
    this.name = name;
    this.filePath = filePath;
    this.xslName = xslName;
    DataModelClazz = clazz;
    this.tableHeadSize = 14;
    this.titleSize = 24;
    this.textSize = 12;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getXslName() {
    return xslName;
  }

  public void setXslName(String xslName) {
    this.xslName = xslName;
  }

  public float getTextSize() {
    return textSize;
  }

  public void setTextSize(float textSize) {
    this.textSize = textSize;
  }

  public float getTitleSize() {
    return titleSize;
  }

  public void setTitleSize(float titleSize) {
    this.titleSize = titleSize;
  }

  public float getTableHeadSize() {
    return tableHeadSize;
  }

  public void setTableHeadSize(float tableHeadSize) {
    this.tableHeadSize = tableHeadSize;
  }

  public Class<?> getDataModelClazz() {
    return DataModelClazz;
  }

  public void setDataModelClazz(Class<?> dataModelClazz) {
    DataModelClazz = dataModelClazz;
  }
}
