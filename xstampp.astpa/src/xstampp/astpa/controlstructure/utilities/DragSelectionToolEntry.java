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
package xstampp.astpa.controlstructure.utilities;

import org.eclipse.gef.palette.SelectionToolEntry;

public class DragSelectionToolEntry extends SelectionToolEntry {

  /**
   * Creates a new PanningSelectionToolEntry.
   */
  public DragSelectionToolEntry() {
    this(null);
  }

  /**
   * Constructor for PanningSelectionToolEntry.
   * 
   * @param label
   *          the label
   */
  public DragSelectionToolEntry(String label) {
    this(label, null);
  }

  /**
   * Constructor for PanningSelectionToolEntry.
   * 
   * @param label
   *          the label
   * @param shortDesc
   *          the description
   */
  public DragSelectionToolEntry(String label, String shortDesc) {
    super(label, shortDesc);
    setToolClass(DragSelectionTool.class);
  }

}
