/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.projectdata;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.FontData;

public class DescriptionObject {

  @XmlElement(name = "fontFamily")
  private String fontFamily;

  @XmlElement(name = "descriptionPart")
  private String descriptionPart;

  @XmlElement(name = "background")
  private String background;

  @XmlElement(name = "foreground")
  private String foreground;

  @XmlElement(name = "style")
  private String style;

  @XmlElement(name = "weight")
  private String weight;

  @XmlElement(name = "fontSize")
  private String fontSize;

  @XmlElement(name = "linebreak")
  private boolean linebreak;

  @XmlElement(name = "decoration")
  private String decoration;

  public DescriptionObject() {
    this.linebreak = true;
  }

  public boolean addRanges(StyleRange range, String description) {
    try {
      this.linebreak = false;
      descriptionPart = description;
      this.weight = "normal";
      this.style = "normal";
      this.fontSize = "9pt";

      if (range == null) {
        return true;
      }
      if (range.font != null && range.font.getFontData() != null) {
        for (FontData fontData : range.font.getFontData()) {
          fontFamily = fontData.getName();
          this.fontSize = fontData.getHeight() + "pt";
          if ((fontData.getStyle() & SWT.BOLD) != 0) {
            this.weight = "bold";
          }
          if ((fontData.getStyle() & SWT.ITALIC) != 0) {
            this.style = "italic";
          }
        }
      }
      if (range.background == null) {
        background = null;
      } else {
        int red = range.background.getRGB().red;
        int green = range.background.getRGB().green;
        int blue = range.background.getRGB().blue;

        this.background = String.format("#%02x%02x%02x", red, green, blue);
        if (this.background.length() != 7) {
          this.background = null;
        }
      }
      if (range.foreground == null) {
        foreground = null;
      } else {
        int red = range.foreground.getRGB().red;
        int green = range.foreground.getRGB().green;
        int blue = range.foreground.getRGB().blue;

        this.foreground = String.format("#%02x%02x%02x", red, green, blue);
        if (this.foreground.length() != 7) {
          this.foreground = null;
        }
      }

      if (range.underline) {
        this.decoration = "underline";
      }
      if (range.strikeout) {
        this.decoration = "line-through";
      }

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public String toString() {

    return foreground + " | " + background + " | " + style + " | " + weight + " | " + fontSize
        + " | " + descriptionPart;
  }

}
