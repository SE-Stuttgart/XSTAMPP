/*************************************************************************
 * Copyright (c) 2014-2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.astpa.model.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.UCAHazLink;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.controlaction.ControlAction;
import xstampp.astpa.model.controlaction.NotProvidedValuesCombi;
import xstampp.astpa.model.controlaction.ProvidedValuesCombi;
import xstampp.astpa.model.controlaction.interfaces.IHAZXControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.IDataModel;
import xstampp.model.IValueCombie;

public interface IExtendedDataModel extends IDataModel,IExtendedDataController{
  
  /**
   * an enum that defines three rule types that can be stored deperately in the dataModelController
   * 
   * @author Lukas Balzer
   *
   */
  public static enum ScenarioType{
    BASIC_SCENARIO,
    CAUSAL_SCENARIO,
    CUSTOM_LTL
  }

  List<AbstractLtlProvider> getLTLPropertys();

  
  IRectangleComponent getComponent(UUID key);

  void setCSComponentComment(UUID id, String value);
  
  /**
   *  this function returns or creates (if null is stored)
   *  a value component which can be referenced as null ignore component by
   *  ACTS, so that in XSTPA a value can be set to '(don't care)' to
   *  ignore its value in the LTL Formula
   *    
   * @return a value Component with the label '(don't care)'
   */
  IRectangleComponent getIgnoreLTLValue();

  List<ICausalComponent> getCausalComponents();
  
  boolean setCASafetyCritical(UUID id, boolean safetyCritical);

  boolean setControlActionDescription(UUID id, String value);

  /**
   * {@link ControlAction#addProvidedVariable(UUID)}
   * @param caID the control action id which is used to look up the action
   * 
   * @param providedVariable the providedVariable to add
   */
  void addCAProvidedVariable(UUID id, UUID id2);
  
  /**
   * 
   * {@link ControlAction#getProvidedVariables()}
   * 
   * @param caID the control action id which is used to look up the action
   * @param notProvidedVariable the notProvidedVariables to set
   */
  void addCANotProvidedVariable(UUID id, UUID id2);

  /**
   * 
   * remove the uuid of a process variable component from the list
   * of variables depending on this control action when provided
   * 
   * @param caID the control action id which is used to look up the action
   * @param providedVariable the providedVariable to remove
   * @return return whether the remove was successful or not, also returns false
   *      if the list is null or the uuid is not contained in the list 
   */
  boolean removeCAProvidedVariable(UUID caID, UUID providedVariable);
  
  /**
   * 
   * remove the uuid of a process variable component from the list
   * of variables depending on this control action when not provided
   * 
   * @param caID the control action id which is used to look up the action
   * @param notProvidedVariable the notProvidedVariables to remove
   * @return return whether the remove was successful or not, also returns false
   *      if the list is null or the uuid is not contained in the list 
   */
  boolean removeCANotProvidedVariable(UUID id, UUID id2);
  
  /**
   * @param caID the control action id which is used to look up the action
   * @param valuesWhenProvided the valuesWhenProvided to set
   */
  void setValuesWhenCAProvided(UUID id, List<ProvidedValuesCombi> valuesIfProvided);

  /**
   * @param caID the control action id which is used to look up the action
   * @param valuesWhenNotProvided the valuesWhenNotProvided to set
   */
  void setValuesWhenCANotProvided(UUID id, List<NotProvidedValuesCombi> valuesIfProvided);

  List<IValueCombie> getIvaluesWhenCAProvided(UUID id);

  List<IValueCombie> getIValuesWhenCANotProvided(UUID id);
  

  int getUCANumber(UUID ucaID);
  List<UCAHazLink> getAllUCALinks();

  List<ITableModel> getLinkedHazardsOfUCA(UUID ucaID);
  
  /**
   *  {@link ICorrespondingSafetyConstraintDataModel#getAllUnsafeControlActions()}
   */
  List<ICorrespondingUnsafeControlAction> getAllUnsafeControlActions();

  /**
   * {@link IUnsafeControlActionDataModel#getAllControlActionsU()}
   */
  List<IHAZXControlAction> getAllControlActionsU();
  
  /**
   * {@link IControlActionViewDataModel#getAllControlActions()}
   */
  List<IControlAction> getAllControlActions();
  
  /**
   * {@link IHazardViewDataModel#getAllHazards()}
   */
  ITableModel getHazard(UUID hazardId);
  
  /**
   * return whether the control action for the given id is safetycritical or not
   *
   * @author Lukas Balzer
   *
   * @param caID must be an id of a control action
   * @return whether the ca is safety critical or not 
   */
  boolean isCASafetyCritical(UUID id);


}
