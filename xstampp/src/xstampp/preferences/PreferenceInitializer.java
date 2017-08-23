/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.preferences;

import messages.Messages;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import xstampp.Activator;

/**
 * Class used to initialize default preference values.
 * 
 * @author Sebastian Sieber
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

  /**
   * The shared instance.
   */
  private static PreferenceInitializer initalizer;

  /**
   * Default colors.
   */
  private static final int DEFAULT_NAVIGATION_ITEM_SELECTED = SWT.COLOR_TITLE_BACKGROUND;

  private static final int DEFAULT_NAVIGATION_ITEM_UNSELECTED = SWT.COLOR_LIST_BACKGROUND;

  private static final int DEFAULT_HOVER_ITEM = SWT.COLOR_TITLE_BACKGROUND_GRADIENT;

  private static final int DEFAULT_SPLITTER_FOREGROUND = SWT.COLOR_BLUE;

  private static final int DEFAULT_SPLITTER_BACKGROUND = SWT.COLOR_DARK_BLUE;

  /**
   * Link to update the application.
   */
  private static final String DEFAULT_UPDATE_LINK = "https://sourceforge.net/projects/stampp/files/updatesite/"; //$NON-NLS-1$

  /**
   * Title font used by navigation title, view title and splitter title.
   */
  private static final Font DEFAUL_TITLE_FONT = new Font(Display.getCurrent(),
      Display.getCurrent().getSystemFont().getFontData().toString(), 11, SWT.BOLD);

  /**
   * Default font used by labels of each view
   */
  private static final Font DEFAUL_FONT = new Font(Display.getCurrent(),
      Display.getCurrent().getSystemFont().getFontData().toString(), 9, SWT.NORMAL);

  /**
   * Constructor.
   */
  public PreferenceInitializer() {
    PreferenceInitializer.initalizer = this;
  }

  /**
   * Returns the shared instance.
   * 
   * @author Sebastian Sieber
   * 
   * @return initalizer
   */
  public static PreferenceInitializer getDefault() {
    return PreferenceInitializer.initalizer;
  }

  @Override
  public void initializeDefaultPreferences() {
    this.store.setDefault(IPreferenceConstants.NAVIGATION_EXTENSION_SORT, 1);
    this.store.setDefault(IPreferenceConstants.NAVIGATION_NAME_SORT, 1);
    // Colors
    PreferenceConverter.setDefault(this.store, IPreferenceConstants.NAVIGATION_ITEM_SELECTED,
        Display.getCurrent().getSystemColor(PreferenceInitializer.DEFAULT_NAVIGATION_ITEM_SELECTED)
            .getRGB());

    PreferenceConverter.setDefault(this.store, IPreferenceConstants.NAVIGATION_ITEM_UNSELECTED,
        Display.getCurrent()
            .getSystemColor(PreferenceInitializer.DEFAULT_NAVIGATION_ITEM_UNSELECTED).getRGB());

    PreferenceConverter.setDefault(this.store, IPreferenceConstants.HOVER_ITEM,
        Display.getCurrent().getSystemColor(PreferenceInitializer.DEFAULT_HOVER_ITEM).getRGB());

    PreferenceConverter.setDefault(this.store, IPreferenceConstants.SPLITTER_FOREGROUND, Display
        .getCurrent().getSystemColor(PreferenceInitializer.DEFAULT_SPLITTER_FOREGROUND).getRGB());

    PreferenceConverter.setDefault(this.store, IPreferenceConstants.SPLITTER_BACKGROUND, Display
        .getCurrent().getSystemColor(PreferenceInitializer.DEFAULT_SPLITTER_BACKGROUND).getRGB());

    PreferenceConverter.setDefault(this.store, IPreferenceConstants.CONTROLSTRUCTURE_FONT_COLOR,
        new RGB(0, 0, 0));

    // Fonts
    PreferenceConverter.setDefault(this.store, IPreferenceConstants.NAVIGATION_TITLE_FONT,
        PreferenceInitializer.DEFAUL_TITLE_FONT.getFontData());

    PreferenceConverter.setDefault(this.store, IPreferenceConstants.DEFAULT_FONT,
        PreferenceInitializer.DEFAUL_FONT.getFontData());

    // Export
    this.store.setDefault(IPreferenceConstants.COMPANY_NAME, ""); //$NON-NLS-1$
    this.store.setDefault(IPreferenceConstants.COMPANY_LOGO, ""); //$NON-NLS-1$
    this.store.setDefault(IPreferenceConstants.PROJECT_NAME, Messages.NewProject);
    PreferenceConverter.setDefault(this.store, IPreferenceConstants.COMPANY_BACKGROUND_COLOR,
        Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE).getRGB());

    PreferenceConverter.setDefault(this.store, IPreferenceConstants.COMPANY_FONT_COLOR,
        Display.getCurrent().getSystemColor(SWT.COLOR_BLACK).getRGB());

    this.store.setDefault(IPreferenceConstants.SHOW_WELCOME_ON_STARTUP_PREFERENCES, true);

    // Update
    this.store.setDefault(IPreferenceConstants.UPDATE_LINK,
        PreferenceInitializer.DEFAULT_UPDATE_LINK);
  }
}
