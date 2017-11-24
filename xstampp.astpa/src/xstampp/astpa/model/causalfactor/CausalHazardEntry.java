package xstampp.astpa.model.causalfactor;

import java.util.Optional;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;

import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.hazacc.Hazard;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkController;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.model.sds.ISDSController;
import xstampp.model.ObserverValue;

public class CausalHazardEntry {

  @XmlElement
  private String text;

  @XmlElement
  private String constraint;

  @XmlElement
  private String designHint;

  @XmlElement
  private String designRequirementLink;

  @XmlElement
  private String cscReference;

  public CausalHazardEntry(CausalFactorController controller, LinkController linkController,
      ISDSController sdsController, IControlActionController caController, Link causalHazLink, IHazAccController hazAccController) {
    Hazard hazard = hazAccController.getHazard(causalHazLink.getLinkB());
    Optional<UUID> causalSCoption = linkController
        .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, causalHazLink.getId()).stream()
        .findFirst();

    String constraintText = controller.getConstraintTextFor(causalSCoption.orElse(null));
    Optional<UUID> sc1Option = linkController
        .getLinksFor(LinkingType.SC2_SC1_LINK, causalSCoption.orElse(null)).stream()
        .findFirst();
    Optional<ITableModel> sc1Model = Optional
        .ofNullable(caController.getCorrespondingSafetyConstraint(sc1Option.orElse(null)));
    Optional<UUID> designReqOption = linkController
        .getLinksFor(LinkingType.DR2_CausalSC_LINK, causalSCoption.orElse(null)).stream().findFirst();
    Optional<ITableModel> reqModelOption = Optional
        .ofNullable(sdsController.getDesignRequirement(designReqOption.orElse(null), ObserverValue.DESIGN_REQUIREMENT_STEP2));

    constraint = constraintText;
    text = hazard.getIdString();
    cscReference = sc1Model.isPresent() ? sc1Model.get().getIdString() : "";
    designRequirementLink = reqModelOption.isPresent() ? reqModelOption.get().getIdString() : "";
    designHint = causalHazLink.getNote();
  }

}
