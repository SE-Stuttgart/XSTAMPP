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
package xstampp.astpa.ui.systemdescription;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.editors.interfaces.ITextEditContribution;

public class EmptyTextContributor implements ITextEditContribution {

  private static IPreferenceStore store = xstampp.Activator.getDefault().getPreferenceStore();

  @Override
  public boolean getBoldControl() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean getItalicControl() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean getStrikeoutControl() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean getUnderlineControl() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean getBulletListControl() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Color getBackground() {
    return new Color(null,
        PreferenceConverter.getColor(store, IPreferenceConstants.COMPANY_BACKGROUND_COLOR));
  }

  @Override
  public Color getForeground() {
    return new Color(null,
        PreferenceConverter.getColor(store, IPreferenceConstants.COMPANY_FONT_COLOR));
  }

  @Override
  public Font getFont() {
    return new Font(null,
        PreferenceConverter.getDefaultFontData(store, IPreferenceConstants.DEFAULT_FONT));
  }

}
