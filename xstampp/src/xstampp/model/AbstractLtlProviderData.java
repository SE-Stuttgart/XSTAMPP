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
package xstampp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AbstractLtlProviderData {

  private String combies;

  private String rule;

  private String refinedUca;

  private String refinedConstraint;

  private String ltl;

  private List<UUID> relatedUcas;

  /**
   * @return A formula which formulates the critical combination in Linear Temporal Logic.
   */
  public String getLtlProperty() {
    return this.ltl;
  }

  /**
   * @return A refined UCA entry which describes the interaction
   *         between control action and process values.
   */
  public String getRefinedUca() {
    return this.refinedUca;
  }

  /**
   * @return A rule to prevent the critical state to happen.
   */
  public String getSafetyRule() {
    return this.rule;
  }

  /**
   * 
   * @return A list with UUIDs which must belong to UCA's {@link #getAllUnsafeControlActions()}.
   */
  public List<UUID> getUcaLinks() {
    return this.relatedUcas;
  }

  /**
   * @return The constraint which should always hold in the system.
   */
  public String getRefinedSafetyConstraint() {
    return this.refinedConstraint;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public void setRefinedUca(String refinedUca) {
    this.refinedUca = refinedUca;
  }

  public void setRefinedConstraint(String refinedConstraint) {
    this.refinedConstraint = refinedConstraint;
  }

  public void setLtl(String ltl) {
    this.ltl = ltl;
  }

  public void setRelatedUcas(List<UUID> relatedUcas) {
    this.relatedUcas = relatedUcas;
  }

  public boolean addRelatedUcas(UUID link) {
    if (this.relatedUcas == null) {
      this.relatedUcas = new ArrayList<>();
    }
    return this.relatedUcas.add(link);
  }

  public String getCombies() {
    return combies;
  }

  public void setCombies(String combies) {
    this.combies = combies;
  }

}
