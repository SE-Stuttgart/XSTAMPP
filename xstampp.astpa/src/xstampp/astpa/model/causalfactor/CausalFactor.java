/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.causalfactor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.AbstractNumberedEntry;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.IEntryWithNameId;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.ObserverValue;

/**
 * A causal factor
 * 
 * @author Fabian
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class CausalFactor extends AbstractNumberedEntry implements ICausalFactor, IEntryWithNameId {

  @XmlElement(name = "id")
  private UUID id;

  @XmlElement(name = "text")
  private String text;

  @XmlElementWrapper(name = "causalEntries")
  @XmlElement(name = "causalEntry")
  private List<CausalFactorEntry> entries;

  @XmlElement
  private UUID constraintId;

  /**
   * Constructor of a causal factor
   * 
   * @author Fabian Toth
   * 
   * @param text
   *          the text of the new causal factor
   */
  public CausalFactor(String text) {
    this.id = UUID.randomUUID();
    this.text = text;
  }

  /**
   * Empty constructor used for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public CausalFactor() {
    this.text = "";
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  @Override
  public String getText() {
    return this.text;
  }

  /**
   * @param text
   *          the text to set
   */
  boolean setText(String text) {
    if (this.text.equals(text)) {
      this.text = text;
      return true;
    }
    return false;
  }

  public void prepareForExport(IHazAccController hazAccController,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      List<CausalSafetyConstraint> safetyConstraints) {
    getEntries().forEach(
        entry -> entry.prepareForExport(hazAccController, allRefinedRules, allUnsafeControlActions,
            safetyConstraints));
  }

  public void prepareForSave(Component component, IHazAccController hazAccController,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      List<CausalSafetyConstraint> safetyConstraints, LinkController linkController) {
    getEntries().forEach(entry -> {
      linkController.addLink(ObserverValue.UCA_CausalFactor_LINK, entry.get, b)
      entry.prepareForSave(component, this, linkController, safetyConstraints);
    });
    this.entries = null;

  }

  private List<CausalFactorEntry> getEntries() {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    return this.entries;
  }

  @Override
  public ITableModel getSafetyConstraint() {
    // Not supported
    return null;
  }

  @Override
  public String getNote() {
    // Notes are not supported by Causal Factors
    return null;
  }

}
