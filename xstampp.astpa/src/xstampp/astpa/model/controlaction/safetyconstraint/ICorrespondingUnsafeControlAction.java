/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.controlaction.safetyconstraint;

import java.util.UUID;

import xstampp.astpa.model.interfaces.ISeverityEntry;
import xstampp.astpa.model.interfaces.ITableModel;

/**
 * Interface for a unsafe control action for the corresponding safety
 * constraints table
 * 
 * @author Fabian Toth
 * 
 */
public interface ICorrespondingUnsafeControlAction extends ITableModel, ISeverityEntry {

  /**
   * @return the description
   * 
   * @author Fabian Toth
   */
  @Override
  String getDescription();

  /**
   * @return the id
   * 
   * @author Fabian Toth
   */
  @Override
  UUID getId();

  /**
   * @author Fabian Toth
   * 
   * @return the correspondingSafetyConstraint
   */
  ITableModel getCorrespondingSafetyConstraint();

  /**
   * @return the links
   */
  public String getLinks();
}
