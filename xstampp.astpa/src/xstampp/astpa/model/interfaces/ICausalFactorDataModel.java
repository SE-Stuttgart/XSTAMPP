/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.causalfactor.ICausalController;
import xstampp.astpa.model.controlaction.IControlActionController;
import xstampp.astpa.model.controlaction.UCAHazLink;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.IDataModel;
import xstampp.model.IEntryFilter;

/**
 * Interface to the Data Model for the Causal Factors View
 * 
 * @author Fabian Toth, Benedikt Markt
 * 
 */
public interface ICausalFactorDataModel extends IDataModel {

  /**
   * @return a list of {@link xstampp.astpa.model.controlaction.UCAHazLink}
   */
  List<UCAHazLink> getAllUCALinks();

 /**
 * {@link ISafetyConstraintViewDataModel#getSafetyConstraint(UUID)}
 */
  ITableModel getSafetyConstraint(UUID safetyConstraintId);
  
  /**
   * Adds a causal factor to the causal component with the given id. <br>
   * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
   * 
   * @author Fabian Toth, Lukas Balzer
   * @param id
   *          the id of the component for which a new factor should be added
   * @return the id of the new causal factor. null if the action fails
   */
  UUID addCausalFactor(UUID id);

  /**
   * Adds a causal factor to the causal component with the given id. <br>
   * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
   * 
   * @author Lukas Balzer
   * @param id
   *          the id of the component for which a new factor should be added
   * @param ucaID
   *          The id of the uca for which this causal factor should be created, a
   *          {@link LinkingType#UCA_CausalFactor_LINK} and a
   *          {@link LinkingType#UcaCfLink_Component_LINK} will be created accordingly.
   * @return the id of the new causal factor. null if the action fails
   */
  UUID addCausalFactor(UUID id, UUID ucaID);

  /**
   * Gets the list of all corresponding safety constraints
   * 
   * @author Fabian Toth
   * 
   * @return the list of all corresponding safety constraints
   */
  List<ITableModel> getCorrespondingSafetyConstraints();

  /**
   * {@link IHazardViewDataModel#getHazards(UUID[])}
   */
  List<ITableModel> getHazards(List<UUID> list);

  /**
   * {@link IHazardViewDataModel#getAllHazards()}
   */
  List<ITableModel> getAllHazards();

  /**
   * {@link IHazardViewDataModel#getHazard(UUID)}
   */
  ITableModel getHazard(UUID id);

  /**
   * {@link IUnsafeControlActionDataModel#getUCAList(IEntryFilter)}
   */
  List<ICorrespondingUnsafeControlAction> getUCAList(IEntryFilter<IUnsafeControlAction> filter);

  /**
   * {@link IUnsafeControlActionDataModel#getLinksOfUCA(UUID)}
   */
  List<UUID> getLinksOfUCA(UUID unsafeControlActionId);

  /**
   * Returns the the control action as {@link ITableModel} for which the unsafe control action
   * belonging to the given {@link UUID}. If the id is not registered for a uca than <i>null</i> is
   * returned.
   * 
   * @param ucaId
   *          an {@link UUID} for a registered uca in the system
   * @return a {@link ITableModel} for a control action or <i>null</i>
   */
  ITableModel getControlActionForUca(UUID ucaId);

  ICausalController getCausalFactorController();

  IExtendedDataController getExtendedDataController();

  LinkController getLinkController();

  /**
   * {@link ICausalController#setUseScenarios(boolean)}
   */
  void setUseScenarios(boolean useScenarios);

  /**
   * {@link ICausalController#setCausalFactorText(UUID, String)}
   */
  boolean setCausalFactorText(UUID causalFactorId, String causalFactorText);

  /**
   * {@link ICausalController#isUseScenarios()}
   */
  boolean isUseScenarios();

  /**
   * {@link ICausalController#addCausalFactor()}
   */
  UUID addCausalFactor();

  List<IRectangleComponent> getCausalComponents();

  IControlActionController getControlActionController();

}
