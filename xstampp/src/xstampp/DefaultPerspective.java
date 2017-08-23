/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp;

import org.apache.log4j.Logger;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * a simple perspective for displaying the projectExplorer and editor window
 * 
 * @author Lukas Balzer
 *
 */
public class DefaultPerspective implements IPerspectiveFactory {
  public static final String ID = "xstampp.defaultPerspective"; //$NON-NLS-1$
  private static final Logger LOGGER = Logger.getRootLogger();

  @Override
  public void createInitialLayout(IPageLayout layout) {
    DefaultPerspective.LOGGER.debug("Setup perspective"); //$NON-NLS-1$
    layout.setFixed(false);
    layout.setEditorAreaVisible(true);
    layout.addView("astpa.explorer", IPageLayout.LEFT, 0.2f, layout.getEditorArea()); //$NON-NLS-1$

    layout.getViewLayout("astpa.explorer").setCloseable(false); //$NON-NLS-1$

    IFolderLayout folder = layout.createFolder("buttomfolder", IPageLayout.BOTTOM, 0.65f, //$NON-NLS-1$
        layout.getEditorArea());

    folder.addPlaceholder("A-CAST.view1"); //$NON-NLS-1$

  }

}
