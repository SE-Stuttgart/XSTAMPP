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

package xstampp.ui.navigation;

import org.eclipse.swt.widgets.TreeItem;

/**
 * @see AbstractSelector
 * @author Lukas Balzer
 * @since 1.0
 */
public class CategorySelector extends AbstractSelector {

  /**
   * constructs a selector for a category item in the project tree.
   * 
   * @author Lukas Balzer
   *
   * @param item
   *          {@link AbstractSelector#getItem()}
   */
  public CategorySelector(TreeItem item, TreeItemDescription descriptor) {
    super(item,descriptor);
  }

}
