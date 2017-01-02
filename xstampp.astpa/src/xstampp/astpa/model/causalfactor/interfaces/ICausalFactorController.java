/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.hazacc.HazAccController;
import xstampp.astpa.model.sds.interfaces.ISafetyConstraint;

public interface ICausalFactorController {

  /**
   * Adds a causal factor to the causal component with the given id. <br>
   * Triggers an update for {@link astpa.model.ObserverValue#CAUSAL_FACTOR}
   * 
   * @author Fabian Toth, Lukas Balzer
   * @param csComp TODO
   * @return the id of the new causal factor. null if the action fails
   */
  UUID addCausalFactor(IRectangleComponent csComp);

  /**
   * Searches for the causal factor with the given id and changes the text of
   * it
   * 
   * @author Fabian Toth
   * 
   * @param componentId
   *            id of the component this causal Factor is stored for
   * @param causalFactorId
   *            the id of the causal factor
   * @param causalFactorText
   *            the new text for the causal factor
   * @return <ul>
   *          <li><b>true</b> when the text has been changed
   *          <li><b>false</b> if an entry was null or an invalid ID was given
   */
  boolean setCausalFactorText(UUID componentId,UUID causalFactorId, String causalFactorText);

  
  UUID addCausalUCAEntry(UUID component,UUID causalFactor,UUID ucaID);
  UUID addCausalHazardEntry(UUID component, UUID causalFactor);
  
  /**
   * returns the causal component entry for the controlStructure component given by the
   * <i>componentId</i>, <i>title</i> and the <i>type</i> or null if the type is not a causal type 
   * @param csComp
   *          an UUID with which a control structure component is registered in the data model
   * @return the causal component entry or null if the cs component is not a causal type 
   */
  ICausalComponent getCausalComponent(IRectangleComponent csComp);
  
  /**
   * 
   * @param component
   * @param causalFactor
   * @param entryData
   * @return
   */
  boolean changeCausalEntry(UUID component,UUID causalFactor,CausalFactorEntryData entryData);
  boolean removeCausalFactor(UUID component,UUID causalFactor);
  boolean removeCausalEntry(UUID component,UUID causalFactor ,UUID entryId);

  /**
   * @return a list containing all registered Safety Constraints in the controller
   *          returns an empty list if no constraints are available,<p>
   *          note that changes to the returned list do not change the list stored in the controller
   */
  List<ISafetyConstraint> getAllCausalSafetyConstraints();

  /**
   * 
   * @param id the uuid with of a safety constraint stored in its causal factor entry
   * @return the safety constraint as ISafetyConstraint, to receive constraint information<p>
   *         e.g.: <code>getSafetyConstraint({@link ICausalFactorEntry#getSafetyConstraintId})</code>
   */
  ISafetyConstraint getCausalSafetyConstraint(UUID id);

}