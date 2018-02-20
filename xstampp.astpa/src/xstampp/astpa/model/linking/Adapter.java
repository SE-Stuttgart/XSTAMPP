/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.linking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

class Adapter extends XmlAdapter<ListOfLinks, Map<LinkingType, List<Link>>> {

  @Override
  public Map<LinkingType, List<Link>> unmarshal(ListOfLinks loe) throws Exception {
    Map<LinkingType, List<Link>> map = new HashMap<>();
    for (Entry entry : loe.getList()) {
      entry.getList().forEach((link) -> link.setLinkType(entry.getKey()));
      map.put(entry.getKey(), entry.getList());
    }
    return map;
  }

  @Override
  public ListOfLinks marshal(Map<LinkingType, List<Link>> map) throws Exception {
    ListOfLinks loe = new ListOfLinks();
    for (Map.Entry<LinkingType, List<Link>> mapEntry : map.entrySet()) {
      Entry entry = new Entry();
      if (mapEntry.getKey() != null) {
        entry.setKey(mapEntry.getKey());
        entry.getList().addAll(mapEntry.getValue());
        loe.getList().add(entry);
      }
    }
    return loe;
  }
}
