package xstampp.astpa.model.causalfactor;

import java.util.Optional;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.hazacc.Hazard;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;

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

  public CausalHazardEntry(DataModelController controller, Link causalHazLink) {
    Hazard hazard = controller.getHazAccController().getHazard(causalHazLink.getLinkB());
    Optional<UUID> causalSCoption = controller.getLinkController()
        .getLinksFor(LinkingType.CausalHazLink_SC2_LINK, causalHazLink.getId()).stream()
        .findFirst();

    String constraintText = controller.getCausalFactorController().getConstraintTextFor(causalSCoption.orElse(null));
    Optional<UUID> sc1Option = controller.getLinkController()
        .getLinksFor(LinkingType.SC2_SC1_LINK, causalSCoption.orElse(null)).stream()
        .findFirst();
    Optional<ITableModel> sc1Model = Optional
        .ofNullable(controller.getControlActionController().getCorrespondingSafetyConstraint(sc1Option.orElse(null)));
    Optional<UUID> designReqOption = controller.getLinkController()
        .getLinksFor(LinkingType.DR2_CausalSC_LINK, causalSCoption.orElse(null)).stream().findFirst();
    Optional<ITableModel> reqModelOption = Optional
        .ofNullable(controller.getDesignRequirement(designReqOption.orElse(null)));

    constraint = constraintText;
    text = hazard.getIdString();
    cscReference = sc1Model.isPresent() ? sc1Model.get().getIdString() : "";
    designRequirementLink = reqModelOption.isPresent() ? reqModelOption.get().getIdString() : "";
    designHint = causalHazLink.getNote();
  }
}
