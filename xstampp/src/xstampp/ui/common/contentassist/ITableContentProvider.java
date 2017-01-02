/*******************************************************************************
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.ui.common.contentassist;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;


/**
 * 
 * Interface for connecting various resources to an table with multiple subitems
 * like the UCA table.
 * 
 * @author Benedikt Markt
 * 
 */
public interface ITableContentProvider<T extends ITableModel> {

  /**
   * Returns all items.
   * 
   * @author Benedikt Markt
   * 
   * @return all items
   */
  List<T> getAllItems();

  /**
   * Returns all linked objects of an item.
   * 
   * @author Benedikt Markt
   * 
   * @param itemId
   *          the id of the item
   * @return all linked objects
   */
  List<T> getLinkedItems(UUID itemId);

  /**
   * Associates item2 with item1.
   * 
   * @author Benedikt Markt
   * 
   * @param item1
   *          the first item
   * @param item2
   *          the second item
   */
  void addLink(UUID item1, UUID item2);

  /**
   * Removes a link from an item
   * 
   * @param item
   *          the item to remove the link from
   * @param removeItem
   *          the item to unlink
   */
  void removeLink(UUID item, UUID removeItem);
  
  
  /**
   * @return a literal which is written in front of the linked entrys id number
   */
  String getPrefix();
}
