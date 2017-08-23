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

/**
 * An empty implementation of IZoomContributor serving as a null-object in case
 * that the activeView is no IZoomContributor
 *
 * @author Lukas Balzer
 *
 */
public class EmptyZoomContributor implements IZoomContributor {

  public EmptyZoomContributor() {
    // null-object
  }

  @Override
  public void updateZoom(double zoom) {
    // null-object

  }

  @Override
  public ZoomManager getZoomManager() {
    // null-object
    return null;
  }

  @Override
  public void addPropertyListener(PropertyChangeListener listener) {
    // null-object

  }

  @Override
  public void fireToolPropertyChange(String property, Object value) {
    // null-object

  }

  @Override
  public Object getProperty(String propertyString) {
    if (propertyString.equals(IS_DECORATED)) {
      return false;
    }
    return null;
  }

  @Override
  public void removePropertyListener(PropertyChangeListener listener) {
    // null-object

  }

}
