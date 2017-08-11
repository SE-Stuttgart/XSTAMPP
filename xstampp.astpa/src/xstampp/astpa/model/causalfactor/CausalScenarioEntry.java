/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
public class CausalScenarioEntry {

  @XmlElement(name = "description")
  private String description;

  @XmlElement(name = "constraint")
  private String constraint;

  @XmlElement
  private UUID constraintId;

  public CausalScenarioEntry(String description, String constraint) {
    if (description == null) {
      this.description = new String();
    } else {
      this.description = description;
    }
    if (constraint == null) {
      this.constraint = new String();
    } else {
      this.constraint = constraint;
    }
  }

  public CausalScenarioEntry() {

  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  void moveSafetyConstraints(List<CausalSafetyConstraint> list) {
    if (constraint != null) {
      CausalSafetyConstraint safetyConstraint = new CausalSafetyConstraint(constraint);
      constraint = null;
      constraintId = safetyConstraint.getId();
      list.add(safetyConstraint);
    }
  }

  /**
   * @param constraint
   *          the constraint to set
   */
  public void setConstraint(String constraint) {
    this.constraint = constraint;
  }
}
