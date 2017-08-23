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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.model.ObserverValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class Entry {
  @XmlAttribute
  private ObserverValue key;
  @XmlElementWrapper(name = "links")
  @XmlElement(name = "link")
  private List<Link> list;

  public Entry() {
    this.list = new ArrayList<>();
  }

  public ObserverValue getKey() {
    return key;
  }

  public void setKey(ObserverValue value) {
    key = value;
  }

  public List<Link> getList() {
    return list;
  }
}
