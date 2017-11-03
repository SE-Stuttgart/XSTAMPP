/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
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
import xstampp.astpa.model.linking.LinkController;
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
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions, LinkController linkController);

  List<ITableModel> getSafetyConstraints();

  boolean isUseScenarios();

  void setUseScenarios(boolean useScenarios);

  /**
   * 
   * @param id
   *          the id of a constraint that has been created in the causal analysis
   * @return the {@link ITableModel} or null if the {@link UUID} doesn't exist
   */
  public ITableModel getSafetyConstraint(UUID id);

  /**
   * Returns the constraint text for the causal factor entry or an empty String if no
   * safetyConstraint was found.
   * 
   * @param id
   *          the {@link UUID} which was assigned to an {@link CausalFactorEntry}
   * @return the title of the {@link CausalSafetyConstraint}, or an empty String if no constraint
   *         was found for the given id.
   */
  String getConstraintTextFor(UUID id);
}
