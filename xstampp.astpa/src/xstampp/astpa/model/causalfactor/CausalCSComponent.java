/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.sds.ISDSController;
import xstampp.model.AbstractLTLProvider;

@XmlRootElement(name = "causalComponent")
@XmlAccessorType(XmlAccessType.NONE)
public class CausalCSComponent implements Comparable<CausalCSComponent> {

  @XmlElement(name = "title")
  private String text;

  @XmlElementWrapper(name = "causalFactors")
  @XmlElement(name = "factor")
  private List<CausalFactor> factors;

  @XmlElement(name = "type")
  private ComponentType type;

  public void prepareForExport(CausalFactorController controller,
      IHazAccController hazAccController, IRectangleComponent child,
      IExtendedDataController extendedDataController,
      IControlActionController caController,
      LinkController linkController, ISDSController sdsController) {
    this.text = child.getText();
    Map<ITableModel, List<Link>> factorBasedMap = controller.getCausalFactorBasedMap(child, linkController);
    for (Entry<ITableModel, List<Link>> entry : factorBasedMap.entrySet()) {
      ((CausalFactor) entry.getKey()).prepareForExport(hazAccController, extendedDataController, caController,
          controller, entry.getValue(), linkController, sdsController);
      getFactors().add(((CausalFactor) entry.getKey()));
    }

  }

  public List<CausalFactor> getFactors() {
    if (factors == null) {
      this.factors = new ArrayList<>();
    }
    return factors;
  }

  public List<CausalFactor> prepareForSave(UUID componentId, IHazAccController hazAccController,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      List<CausalSafetyConstraint> safetyConstraints, LinkController linkController) {
    for (CausalFactor causalFactor : getFactors()) {
      causalFactor.prepareForSave(componentId, hazAccController, allRefinedRules,
          allUnsafeControlActions, safetyConstraints, linkController);
    }
    return getFactors();
  }

  @Override
  public int compareTo(CausalCSComponent o) {
    return this.text.compareTo(o.text);
  }

  public String getText() {
    return text;
  }

  public ComponentType getType() {
    return type;
  }
}
