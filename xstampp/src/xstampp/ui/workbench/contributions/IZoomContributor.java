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
package xstampp.ui.workbench.contributions;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.ZoomManager;

public interface IZoomContributor {

  /**
   * constant used for propargating the deco state of the graphical editor
   * content
   * 
   * @author Lukas Balzer
   */
  public static final String IS_DECORATED = "decorated"; //$NON-NLS-1$

  /**
   * 
   *
   * @author Lukas Balzer
   *
   * @param zoom
   *          the new zoom
   */
  void updateZoom(double zoom);

  /**
   * 
   * @author Lukas Balzer
   * 
   * @return the zoom manager
   */
  ZoomManager getZoomManager();

  void addPropertyListener(PropertyChangeListener listener);

  void removePropertyListener(PropertyChangeListener listener);

  void fireToolPropertyChange(String property, Object value);

  Object getProperty(String propertyString);
}