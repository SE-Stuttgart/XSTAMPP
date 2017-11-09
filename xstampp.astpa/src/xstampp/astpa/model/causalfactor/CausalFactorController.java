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
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.NumberedArrayList;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorController;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.preferences.ASTPADefaultConfig;
import xstampp.model.AbstractLTLProvider;

/**
 * Manager class for the causal factors
 * 
 * @author Fabian Toth, Benedikt Markt
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class CausalFactorController extends Observable
    implements ICausalFactorController, ICausalController {

  @XmlElementWrapper(name = "causalComponents")
  @XmlElement(name = "causalComponent")
  private Map<UUID, CausalCSComponent> causalComponents;

  @XmlAttribute(name = "useScenarios")
  private boolean useScenarios;

  @XmlElementWrapper(name = "causalSafetyConstraints")
  @XmlElement(name = "causalSafetyConstraint")
  private NumberedArrayList<CausalSafetyConstraint> causalSafetyConstraints;

  private NumberedArrayList<CausalFactor> causalFactors;

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
    CausalFactor factor = new CausalFactor();
    if (this.causalFactors.add(factor)) {
      return factor.getId();
    }

    return null;
  }

  @Override
  public boolean setCausalFactorText(UUID causalFactorId, String causalFactorText) {
    CausalFactor causalFactor = this.causalFactors.get(causalFactorId);
    if (causalFactor != null) {
      return causalFactor.setText(causalFactorText);
    }
    return false;
  }

  @Override
  public boolean removeCausalFactor(UUID causalFactor) {
    return this.causalFactors.removeIf(factor -> factor.getId().equals(causalFactor));
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

  private boolean validateCausalComponent(ComponentType type) {
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
    if (causalComponents != null) {
      removeList.addAll(causalComponents.keySet());
    }
    this.causalComponents.values().forEach((comp) -> {
      comp.prepareForSave(hazAccController, allRefinedRules, allUnsafeControlActions,
          getCausalSafetyConstraints(), linkController);
    });
    if (causalComponents != null) {
      for (UUID id : removeList) {
        this.causalComponents.remove(id);
      }
      if (this.causalComponents.isEmpty()) {
        causalComponents = null;
      }
    }
  }

  @Override
  public UUID addSafetyConstraint() {
    CausalSafetyConstraint constraint = new CausalSafetyConstraint();
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
    for (CausalSafetyConstraint constraint : getCausalSafetyConstraints()) {
      if (constraint.getId().equals(id)) {
        return constraint;
      }
    }
    return null;

  }

  @Override
  public String getConstraintTextFor(UUID id) {
    if (id == null) {
      return ""; //$NON-NLS-1$
    }
    ITableModel constraint = getSafetyConstraint(id);
    if (constraint != null) {
      return constraint.getTitle();
    }
    return ""; //$NON-NLS-1$
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

}
