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

package xstampp.astpa.ui;

import org.eclipse.jface.viewers.Viewer;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.ui.sds.ModeFilter;

/**
 * 
 * @author Jarkko Heidenwag
 * 
 */
public class ATableFilter extends ModeFilter {

  public ATableFilter() {
    super(new String[] {});
  }

  @Override
  public boolean select(Viewer viewer, Object parentElement, Object element) {
    if ((this.searchString == null) || (this.searchString.length() == 0)) {
      return true;
    }
    if (element instanceof ATableModel) {
      ATableModel p = (ATableModel) element;
      if (p.getTitle().toLowerCase().matches(this.searchString)) {
        return true;
      }
      if (p.getDescription().toLowerCase().matches(this.searchString)) {
        return true;
      }
    }
    return false;
  }


}