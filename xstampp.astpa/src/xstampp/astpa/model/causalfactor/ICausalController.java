package xstampp.astpa.model.causalfactor;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.AbstractLTLProvider;

public interface ICausalController {

  UUID addCausalFactor(IRectangleComponent csComp);

  List<UUID> getLinkedUCAList(UUID factorId);

  boolean setCausalFactorText(UUID componentId, UUID causalFactorId, String causalFactorText);

  UUID addCausalUCAEntry(UUID componentId, UUID causalFactorId, UUID ucaID);

  UUID addCausalUCAEntry(UUID componentId, UUID causalFactorId, ICausalFactorEntry entry);

  UUID addCausalHazardEntry(UUID componentId, UUID causalFactorId);

  CausalFactorEntryData changeCausalEntry(UUID componentId, UUID causalFactorId,
      CausalFactorEntryData entryData);

  boolean removeCausalFactor(UUID componentId, UUID causalFactor);

  boolean removeCausalEntry(UUID componentId, UUID causalFactorId, UUID entryId);

  ICausalComponent getCausalComponent(IRectangleComponent csComp);

  void prepareForExport(IHazAccController hazAccController, List<IRectangleComponent> children,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions);

  void prepareForSave(IHazAccController hazAccController, List<Component> list,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions);

  List<ITableModel> getSafetyConstraints();

  boolean isUseScenarios();

  void setUseScenarios(boolean useScenarios);

}