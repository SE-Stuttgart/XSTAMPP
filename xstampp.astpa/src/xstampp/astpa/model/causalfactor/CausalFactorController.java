/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.NumberedArrayList;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.service.UndoTextChange;
import xstampp.astpa.preferences.ASTPADefaultConfig;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.ObserverValue;

/**
 * Manager class for the causal factors
 * 
 * @author Fabian Toth, Benedikt Markt
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class CausalFactorController extends Observable
    implements ICausalController {

  @XmlElementWrapper(name = "causalComponents")
  @XmlElement(name = "causalComponent")
  private Map<UUID, CausalCSComponent> causalComponents;

  @XmlAttribute(name = "useScenarios")
  private boolean useScenarios;

  @XmlElementWrapper(name = "causalSafetyConstraints")
  @XmlElement(name = "causalSafetyConstraint")
  private NumberedArrayList<CausalSafetyConstraint> causalSafetyConstraints;

  @XmlElementWrapper(name = "causalFactors")
  @XmlElement(name = "causalFactor")
  private NumberedArrayList<CausalFactor> causalFactors;

  private LinkController linkController;

  /**
   * Constructor of the causal factor controller
   * 
   * @author Fabian Toth
   * 
   */
  public CausalFactorController() {
    this.causalSafetyConstraints = new NumberedArrayList<>();
    this.causalFactors = new NumberedArrayList<>();
    this.setUseScenarios(ASTPADefaultConfig.getInstance().USE_CAUSAL_SCENARIO_ANALYSIS);
  }

  @Override
  public UUID addCausalFactor() {
    CausalFactor factor = new CausalFactor("");
    if (this.causalFactors.add(factor)) {
      notifyObservers(ObserverValue.CAUSAL_FACTOR);
      return factor.getId();
    }
    return null;
  }

  UUID addCausalFactor(CausalFactor factor) {
    if (this.causalFactors.add(factor)) {
      notifyObservers(ObserverValue.CAUSAL_FACTOR);
      return factor.getId();
    }
    return null;
  }

  public ICausalFactor getCausalFactor(UUID causalFactorId) {
    return this.causalFactors.stream().filter((factor) -> factor.getId().equals(causalFactorId))
        .findFirst().orElse(null);
  }

  @Override
  public boolean setCausalFactorText(UUID causalFactorId, String causalFactorText) {
    CausalFactor causalFactor = this.causalFactors.get(causalFactorId);
    if (causalFactor != null) {

      String oldText = causalFactor.getText();
      if (causalFactor.setText(causalFactorText)) {
        UndoTextChange textChange = new UndoTextChange(oldText, causalFactorText, ObserverValue.CAUSAL_FACTOR);
        textChange.setConsumer((text) -> setCausalFactorText(causalFactorId, text));
        notifyObservers(textChange);
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean removeCausalFactor(UUID causalFactor) {
    Optional<CausalFactor> removeOptional = this.causalFactors.stream()
        .filter(factor -> factor.getId().equals(causalFactor)).findFirst();
    if (removeOptional.isPresent() && this.causalFactors.remove(removeOptional.get())) {
      setChanged();
      notifyObservers(new UndoRemoveCausalFactor(this, removeOptional.get(), this.linkController));
      return true;
    }
    return false;
  }

  public ICausalComponent getCausalComponent(IRectangleComponent csComp) {
    CausalCSComponent component = null;
    if (csComp != null && validateCausalComponent(csComp.getComponentType())) {
      if (causalComponents == null) {
        causalComponents = new HashMap<>();
      }
      if (!causalComponents.containsKey(csComp.getId())) {
        causalComponents.put(csComp.getId(), new CausalCSComponent());
      }

      component = causalComponents.get(csComp.getId());
      component.setText(csComp.getText());
      component.setId(csComp.getId());
      component.setType(csComp.getComponentType());
    }
    return component;
  }

  @Override
  public boolean validateCausalComponent(ComponentType type) {
    switch (type) {
    case ACTUATOR:
    case CONTROLLED_PROCESS:
    case CONTROLLER:
    case UNDEFINED:
    case SENSOR:
      return true;
    default:
      return false;

    }
  }

  @Override
  public void prepareForExport(IHazAccController hazAccController,
      List<IRectangleComponent> children, List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions) {

    for (IRectangleComponent child : children) {
      if (getCausalComponent(child) != null) {
        this.causalComponents.get(child.getId()).prepareForExport(hazAccController, child,
            allRefinedRules, allUnsafeControlActions, getCausalSafetyConstraints());
      }
    }
  }

  @Override
  public void prepareForSave(IHazAccController hazAccController,
      List<Component> list,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      LinkController linkController) {
    ArrayList<UUID> removeList = new ArrayList<>();
    if (this.causalComponents != null) {
      removeList.addAll(causalComponents.keySet());
      this.causalComponents.entrySet().forEach((comp) -> {
        this.causalFactors.addAll(comp.getValue().prepareForSave(comp.getKey(), hazAccController, allRefinedRules,
            allUnsafeControlActions,
            getCausalSafetyConstraints(), linkController));
      });
    }
    this.causalComponents = null;
  }

  @Override
  public UUID addSafetyConstraint(String text) {
    CausalSafetyConstraint constraint = new CausalSafetyConstraint(text);
    if (this.causalSafetyConstraints.add(constraint)) {
      return constraint.getId();
    }
    return null;
  }

  @Override
  public List<ITableModel> getSafetyConstraints() {
    List<ITableModel> list = new ArrayList<>();
    if (causalSafetyConstraints != null) {
      list.addAll(causalSafetyConstraints);
    }
    return list;
  }

  @Override
  public ITableModel getSafetyConstraint(UUID id) {
    return getCausalSafetyConstraints().stream().filter((constraint) -> {
      return constraint.getId().equals(id);
    }).findFirst().orElse(new CausalSafetyConstraint(""));
  }

  @Override
  public String getConstraintTextFor(UUID id) {
    return getSafetyConstraint(id).getDescription();
  }

  @Override
  public boolean setSafetyConstraintText(UUID linkB, String newText) {

    String description = ((ATableModel) getSafetyConstraint(linkB)).setDescription(newText);
    if (!newText.equals(description)) {
      UndoTextChange textChange = new UndoTextChange(description, newText, ObserverValue.CAUSAL_FACTOR);
      textChange.setConsumer((text) -> setSafetyConstraintText(linkB, text));
      notifyObservers(textChange);
      return true;
    }
    return false;
  }

  private NumberedArrayList<CausalSafetyConstraint> getCausalSafetyConstraints() {
    if (causalSafetyConstraints == null) {
      this.causalSafetyConstraints = new NumberedArrayList<>();
    }
    return causalSafetyConstraints;
  }

  @Override
  public boolean isUseScenarios() {
    return useScenarios;
  }

  @Override
  public void setUseScenarios(boolean useScenarios) {
    this.useScenarios = useScenarios;
  }

  public void setLinkController(LinkController linkController) {
    this.linkController = linkController;
  }
}
