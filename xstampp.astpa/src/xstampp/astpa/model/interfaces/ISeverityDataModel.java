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
package xstampp.astpa.model.interfaces;

public interface ISeverityDataModel {

  /**
   * overwrites the current severity with a new one if the given newSeverity doesn't equal the
   * currently assigned
   * 
   * @param entry
   *          the entry in which the given severity should be stored, if entry is no instance of
   *          {@link ISeverityEntry}
   *          than <i>null</i> is returned
   * @param severity
   *          the new severity value
   * @return null if the new severity equals the current severity, otherwise this method returns the
   *         current value
   */
  public boolean setSeverity(Object entry, Severity severity);

  /**
   * @return the useSeverity
   */
  public boolean isUseSeverity();

  /**
   * @param useSeverity
   *          the useSeverity to set
   * @return if the value was really changed
   */
  public boolean setUseSeverity(boolean useSeverity);
}
