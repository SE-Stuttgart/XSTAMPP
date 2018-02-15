/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.causalfactor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.ATableModelController;
import xstampp.astpa.model.BadReferenceModel;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.model.sds.ISDSController;
import xstampp.astpa.model.service.UndoTextChange;
import xstampp.astpa.preferences.ASTPADefaultConfig;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.NumberedArrayList;
import xstampp.model.ObserverValue;

/**
 * Manager class for the causal factors
 * 
 * for reference of the causal factor model during runtime refere to
 * xstampp.astpa/docs/architecture
 */
@XmlAccessorType(XmlAccessType.NONE)
public class CausalFactorController extends ATableModelController implements ICausalController {

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

  @XmlElementWrapper(name = "componentsList")
  @XmlElement(name = "component")
  private List<CausalCSComponent> componentsList;

  /**
   * Constructor of the causal factor controller
   * 
   * @author Fabian Toth
   * 
   */
  public CausalFactorController() {
    this(false);
  }

  public CausalFactorController(boolean testable) {
    this.causalSafetyConstraints = new NumberedArrayList<>();
    this.causalFactors = new NumberedArrayList<>();
    if (!testable) {
      this.setUseScenarios(ASTPADefaultConfig.getInstance().USE_CAUSAL_SCENARIO_ANALYSIS);
    }
  }

  @Override
  public UUID addCausalFactor() {
    return this.addCausalFactor(new CausalFactor(""));
  }

  UUID addCausalFactor(CausalFactor factor) {
    if (this.causalFactors.add(factor)) {
      setChanged();
      notifyObservers(new UndoAddCausalFactor(this, factor, linkController));
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
        UndoTextChange textChange = new UndoTextChange(oldText, causalFactorText,
            ObserverValue.CAUSAL_FACTOR);
        textChange.setConsumer((text) -> setCausalFactorText(causalFactorId, text));
        setChanged();
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

  @Override
  public boolean removeSafetyConstraint(UUID constraintId) {
    Optional<CausalSafetyConstraint> removeOptional = this.causalSafetyConstraints.stream()
        .filter(factor -> factor.getId().equals(constraintId)).findFirst();
    if (removeOptional.isPresent() && this.causalSafetyConstraints.remove(removeOptional.get())) {
      setChanged();
      notifyObservers(ObserverValue.CAUSAL_FACTOR);
      return true;
    }
    return false;
  }

  public ICausalComponent getCausalComponent(IRectangleComponent csComp) {
    ICausalComponent component = null;
    if (csComp != null && validateCausalComponent(csComp.getComponentType())) {
      component = csComp;
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
  public void prepareForExport(IHazAccController hazAccController, IRectangleComponent root,
      IExtendedDataController extendedDataController,
      IControlActionController caController,
      LinkController linkController, ISDSController sdsController) {
    this.componentsList = new ArrayList<>();
    for (IRectangleComponent child : root.getChildren()) {
      if (linkController.isLinked(LinkingType.UcaCfLink_Component_LINK, child.getId())) {
        CausalCSComponent comp = new CausalCSComponent();
        comp.prepareForExport(this, hazAccController, child, extendedDataController, caController, linkController,
            sdsController);
        this.componentsList.add(comp);
      }
    }
  }

  @Override
  public SortedMap<ICausalFactor, List<Link>> getCausalFactorBasedMap(ICausalComponent component,
      LinkController linkController) {
    TreeMap<ICausalFactor, List<Link>> ucaCfLink_Component_ToCFmap = new TreeMap<>();
    for (Link link : linkController.getRawLinksFor(LinkingType.UcaCfLink_Component_LINK, component.getId())) {
      Link ucaCFLink = linkController.getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
          link.getLinkA());
      try {
        ICausalFactor factor = getCausalFactor(ucaCFLink.getLinkB());
        if (!ucaCfLink_Component_ToCFmap.containsKey(factor)) {
          ucaCfLink_Component_ToCFmap.put(factor, new ArrayList<>());
        }
        ucaCfLink_Component_ToCFmap.get(factor).add(link);
      } catch (NullPointerException exc) {

      }
    }
    return ucaCfLink_Component_ToCFmap;
  }

  @Override
  public SortedMap<IUnsafeControlAction, List<Link>> getUCABasedMap(ICausalComponent component,
      LinkController linkController, IControlActionController caController) {
    SortedMap<IUnsafeControlAction, List<Link>> ucaCfLink_Component_ToCFmap = new TreeMap<>();
    for (Link link : linkController.getRawLinksFor(LinkingType.UcaCfLink_Component_LINK, component.getId())) {
      Link ucaCFLink = linkController.getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
          link.getLinkA());
      IUnsafeControlAction factor = caController.getUnsafeControlAction(ucaCFLink.getLinkA());

      if (!ucaCfLink_Component_ToCFmap.containsKey(factor)) {
        ucaCfLink_Component_ToCFmap.put(factor, new ArrayList<>());
      }
      ucaCfLink_Component_ToCFmap.get(factor).add(link);
    }
    return ucaCfLink_Component_ToCFmap;
  }

  @Override
  public SortedMap<IUnsafeControlAction, List<Link>> getHazardBasedMap(ITableModel hazModel,
      LinkController linkController, IControlActionController caController) {
    SortedMap<IUnsafeControlAction, List<Link>> ucaCfLink_Component_ToCFmap = new TreeMap<>();
    linkController.getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK, hazModel.getId())
        .forEach((link) -> {
          Link entryLink = linkController.getLinkObjectFor(LinkingType.UcaCfLink_Component_LINK,
              link.getLinkA());
          Link ucaCfLink = linkController.getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
              entryLink.getLinkA());
          Optional<Link> sc2Option = linkController
              .getRawLinksFor(LinkingType.CausalEntryLink_SC2_LINK, entryLink.getId()).stream().findFirst();
          IUnsafeControlAction uca = caController.getUnsafeControlAction(ucaCfLink.getLinkA());
          if (sc2Option.isPresent() && uca != null) {
            ucaCfLink_Component_ToCFmap.put(uca, new ArrayList<>());
            ucaCfLink_Component_ToCFmap.get(uca).add(sc2Option.get());
          } else if (uca != null) {
            ucaCfLink_Component_ToCFmap.putIfAbsent(uca, new ArrayList<>());
            ucaCfLink_Component_ToCFmap.get(uca).add(link);
          }
        });
    return ucaCfLink_Component_ToCFmap;
  }

  @Override
  public void prepareForSave(IHazAccController hazAccController, List<Component> list,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      LinkController linkController) {
    ArrayList<UUID> removeList = new ArrayList<>();
    if (this.causalComponents != null) {
      removeList.addAll(causalComponents.keySet());
      this.causalComponents.entrySet().forEach((comp) -> {
        this.causalFactors
            .addAll(comp.getValue().prepareForSave(comp.getKey(), hazAccController, allRefinedRules,
                allUnsafeControlActions, getCausalSafetyConstraints(), linkController));
      });
    }
    for (Link link : linkController.getLinksFor(LinkingType.UcaCfLink_Component_LINK)) {
      Link ucaCFLink = linkController.getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK, link.getLinkA());
      if (ucaCFLink != null) {
        for (UUID uuid : linkController.getLinksFor(LinkingType.UCA_HAZ_LINK, ucaCFLink.getLinkA())) {
          linkController.addLink(LinkingType.CausalEntryLink_HAZ_LINK, link.getId(), uuid);
        }
      }
    }
    causalFactors.forEach(factor -> factor.prepareForSave());
    this.causalComponents = null;
    this.componentsList = null;
  }

  @Override
  public UUID addSafetyConstraint(String text) {
    CausalSafetyConstraint constraint = new CausalSafetyConstraint(text);
    if (this.causalSafetyConstraints.add(constraint)) {
      return constraint.getId();
    }
    return null;
  }

  UUID addSafetyConstraint(ITableModel model) {
    CausalSafetyConstraint constraint = new CausalSafetyConstraint(model);
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

  public List<CausalCSComponent> getCausalComponents() {
    if (this.componentsList == null) {
      return new ArrayList<>();
    }
    Collections.sort(componentsList);
    return componentsList;
  }

  @Override
  public ITableModel getSafetyConstraint(UUID id) {
    CausalSafetyConstraint scObject = getCausalSafetyConstraints().stream().filter((constraint) -> {
      return constraint.getId().equals(id);
    }).findFirst().orElse(null);
    if (scObject == null) {
      return BadReferenceModel.getBadReference();
    }
    return scObject;
  }

  @Override
  public String getConstraintTextFor(UUID id) {
    return getSafetyConstraint(id).getTitle();
  }

  @Override
  public boolean setSafetyConstraintText(UUID constraintId, String newText) {
    return setModelTitle(getSafetyConstraint(constraintId), newText, ObserverValue.CAUSAL_FACTOR);
  }

  @Override
  public boolean setSafetyConstraintDescription(UUID constraintId, String newText) {
    return setModelDescription(getSafetyConstraint(constraintId), newText, ObserverValue.CAUSAL_FACTOR);
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

  void setCausalFactors(ArrayList<CausalFactor> causalFactors) {
    this.causalFactors.clear();
    this.causalFactors.addAll(causalFactors);
  }

  void setCausalSafetyConstraints(
      ArrayList<CausalSafetyConstraint> causalSafetyConstraints) {
    this.causalSafetyConstraints.clear();
    this.causalSafetyConstraints.addAll(causalSafetyConstraints);
  }

  public void syncContent(CausalFactorController controller) {
    for (CausalFactor other : controller.causalFactors) {
      ICausalFactor own = getCausalFactor(other.getId());
      if (own == null) {
        addCausalFactor(other);
      } else {
        setCausalFactorText(other.getId(), other.getText());
      }
    }
    for (CausalSafetyConstraint otherReq : controller.causalSafetyConstraints) {
      ITableModel ownReq = getSafetyConstraint(otherReq.getId());
      if (ownReq == null) {
        addSafetyConstraint(otherReq);
      } else {
        setSafetyConstraintText(otherReq.getId(), otherReq.getTitle());
        setSafetyConstraintDescription(otherReq.getId(), otherReq.getDescription());
      }
    }
  }
}
