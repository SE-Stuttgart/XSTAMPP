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

package xstampp;

import org.apache.log4j.Logger;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The perspective for A-STPA. Responsible for creating the instance of the view
 * container.
 * 
 * @author Patrick Wickenhaeuser
 */
public class Perspective implements IPerspectiveFactory {

  private static final Logger LOGGER = Logger.getRootLogger();

  private static final float VIEW_CONTAINER_RATIO = 0.75f;

  @Override
  public void createInitialLayout(IPageLayout layout) {
    Perspective.LOGGER.debug("Setup perspective"); //$NON-NLS-1$
    layout.setFixed(true);
    layout.setEditorAreaVisible(false);
    // create the view container
    layout.addStandaloneView("astpa.Welcome", false, IPageLayout.LEFT, //$NON-NLS-1$
        Perspective.VIEW_CONTAINER_RATIO, layout.getEditorArea());
    layout.addPerspectiveShortcut("stpaVerifier.perspective"); //$NON-NLS-1$
    layout.setFixed(true);
  }
}
