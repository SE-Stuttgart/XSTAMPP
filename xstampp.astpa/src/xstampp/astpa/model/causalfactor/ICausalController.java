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

import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.AbstractLTLProvider;

public interface ICausalController {

  UUID addCausalFactor();

  /**
   * 
   * @param causalFactorId
   *          the {@link UUID} of a {@link ICausalFactor}
   * @param causalFactorText
   *          the text that should be set as the {@link ICausalFactor#getText()}
   * @return <b style="color:blue">true</b> if the given id is valid and the text is different to
   *         the current <br><b style="color:blue">false</b> otherwise
   */
  boolean setCausalFactorText(UUID causalFactorId, String causalFactorText);

  boolean removeCausalFactor(UUID causalFactor);

  void prepareForExport(IHazAccController hazAccController, List<IRectangleComponent> children,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions);

  void prepareForSave(IHazAccController hazAccController, List<Component> list,
      List<AbstractLTLProvider> allRefinedRules,
      List<ICorrespondingUnsafeControlAction> allUnsafeControlActions,
      LinkController linkController);

  boolean isUseScenarios();

  void setUseScenarios(boolean useScenarios);

  List<ITableModel> getSafetyConstraints();

  UUID addSafetyConstraint(String text);

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
   * @return the description of the {@link ICausalSafetyConstraint}, or an empty String if no
   *         constraint
   *         was found for the given id.
   */
  String getConstraintTextFor(UUID id);

  /**
   * 
   * @param type
   *          One of {@link ComponentType}
   * @return true if the type is a valid component for the causal analysis
   */
  boolean validateCausalComponent(ComponentType type);

  /**
   * getter for a causal factor stored in the causal factor controller
   * 
   * @param causalFactorId
   *          the {@link UUID} that was given to a causal factor by creation
   * @return the {@link ICausalFactor} for the given id or <b>null</b> if no {@link ICausalFactor}
   *         was found with the given id
   */
  ICausalFactor getCausalFactor(UUID causalFactorId);

  boolean setSafetyConstraintText(UUID linkB, String newText);
}
