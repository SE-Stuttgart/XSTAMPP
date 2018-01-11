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
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.hazacc.Hazard;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.model.sds.ISDSController;
import xstampp.model.AbstractLTLProvider;

@XmlAccessorType(XmlAccessType.NONE)
public class CausalFactorEntry {

  @XmlAttribute(name = "id", required = true)
  private UUID id;

  @XmlElement(name = "hazardLinks")
  private String hazardLinks;

  @XmlElement(name = "constraintText")
  private String constraintText;

  @XmlElement
  private UUID constraintId;

  @XmlElementWrapper(name = "hazardIds")
  @XmlElement(name = "id")
  private List<UUID> hazardIds;

  @XmlElement(name = "note")
  private String note;

  @XmlElement(name = "ucaDescription")
  private String ucaDescription;

  @XmlElement(name = "ucaLink")
  private UUID ucaLink;

  @XmlElementWrapper(name = "scenarioEntries")
  @XmlElement(name = "scenarioEntry")
  private List<CausalScenarioEntry> scenarioEntries;

  @XmlElementWrapper(name = "scenarioLinks")
  @XmlElement(name = "id")
  private List<UUID> scenarioLinks;

  @XmlElementWrapper(name = "hazardEntries")
  @XmlElement(name = "entry")
  private List<CausalHazardEntry> hazardEntries;

  public CausalFactorEntry() {
    id = UUID.randomUUID();

  }

  boolean prepareForSave(UUID componentId, CausalFactor causalFactor, LinkController linkController,
      List<CausalSafetyConstraint> list) {
    // get rid of redundant entries which should not be stored
    if (constraintText != null) {
      CausalSafetyConstraint safetyConstraint = new CausalSafetyConstraint(constraintText);
      this.constraintId = safetyConstraint.getId();
      list.add(safetyConstraint);
    }
    if (ucaLink != null) {
      UUID ucaCfLink = linkController.addLink(LinkingType.UCA_CausalFactor_LINK, ucaLink,
          causalFactor.getId());
      UUID UcaCfCompLink = linkController.addLink(LinkingType.UcaCfLink_Component_LINK, ucaCfLink,
          componentId);
      // if a constraint is already linked to this entry than the link is re-added as
      // UcaHazLink_SC2_LINK to the LinkController

      linkController.addLink(LinkingType.CausalEntryLink_SC2_LINK, UcaCfCompLink,
          this.constraintId);
      linkController.changeLinkNote(UcaCfCompLink, LinkingType.UcaCfLink_Component_LINK, note);
    }
    constraintText = null;
    ucaLink = null;
    scenarioEntries = null;
    ucaDescription = null;
    hazardEntries = null;
    hazardIds = null;
    hazardLinks = null;
    return true;
  }

  public void prepareForExport(IHazAccController hazAccController,
      IExtendedDataController extendedDataController, IControlActionController caController,
      CausalFactorController controller, Link causalEntryList, LinkController linkController,
      ISDSController sdsController) {
    Link ucaCfLink = linkController.getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
        causalEntryList.getLinkA());
    if (ucaCfLink.isLinkAPresent()) {
      IUnsafeControlAction uca = caController.getUnsafeControlAction(ucaCfLink.getLinkA());
      ucaDescription = uca.getDescription();
      hazardLinks = "";
      if (!controller.isUseScenarios()) {
        hazardEntries = new ArrayList<>();
        for (Link causalHazLink : linkController.getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK,
            causalEntryList.getId())) {
          CausalHazardEntry hazEntry = new CausalHazardEntry(controller, linkController, sdsController, caController,
              causalHazLink, hazAccController);
          hazardEntries.add(hazEntry);
        }
      } else {
        scenarioEntries = new ArrayList<>();
        hazardLinks = "";
        for (Link causalHazLink : linkController.getRawLinksFor(LinkingType.CausalEntryLink_HAZ_LINK,
            causalEntryList.getId())) {
          Hazard hazard = hazAccController.getHazard(causalHazLink.getLinkB());
          hazardLinks += hazardLinks.isEmpty() ? "" : ",";
          hazardLinks += hazard.getIdString();
        }
        for (Link causalHazLink : linkController.getRawLinksFor(
            LinkingType.CausalEntryLink_Scenario_LINK,
            causalEntryList.getId())) {
          AbstractLTLProvider refinedScenario = extendedDataController
              .getRefinedScenario(causalHazLink.getLinkB());

          String constraintText = refinedScenario.getRefinedSafetyConstraint();
          CausalScenarioEntry entry = new CausalScenarioEntry(refinedScenario.getDescription(), constraintText);
          scenarioEntries.add(entry);
        }
      }
    }
  }

}