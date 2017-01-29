/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
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

import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;

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
   * changes a causal entry in the dataModel<br>
   * @param componentId the UUID of a control structure component stored in the data model
   * @param causalFactorId the UUID of a causal factor 
   * @param entryData the change data for the causal entry one of {@link CausalFactorUCAEntryData} or {@link CausalFactorEntryData}
   * @return <b>true if</b><ul>
   *              <li>both id's are valid and there where changes stored in the entryData
   *         </ul>
   *         <b>false if</b><ul>
   *              <li>one of the parameters is null
   *              <li>one of the parameters is not valid
   *              <li>the entry data does not contain any changes
   *         
   */
  boolean changeCausalEntry(UUID componentId,UUID causalFactorId,CausalFactorEntryData entryData);
  
  /**
   * removes a causal factor from the list of causal factors of the causal component
   * registered for the given componentId
   * if componentId is null than the method searches for the causal factor in all registered components
   * 
   * @param componentId a UUID for which a component should be registered, or null if the id is not known or 
   * @param causalFactorId the id of the causal factor which should be removed, when this is null or not valid the method 
   *        returns false
   * @return 
   */
  boolean removeCausalFactor(UUID componentId,UUID causalFactorId);
  boolean removeCausalEntry(UUID component,UUID causalFactor ,UUID entryId);

  /**
   * getter for a list containing all unsafe control Actions that are currently
   * existing as {@link CausalFactorEntry}
   * @param factorId TODO
   * @return a list of all uca ids of all {@link CausalFactorEntry}
   */
  List<UUID> getLinkedUCAList(UUID factorId);
  

}