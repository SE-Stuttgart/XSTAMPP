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
package xstampp.astpa.model.causalfactor;

import java.util.UUID;

public class CausalFactorUCALink extends AbstractLinkModel {

  private UUID causalFactorId;
  private UUID ucaId;

  /**
   * Constructor for a link
   * 
   * @author Lukas Balzer
   * 
   * @param causalFactorId
   *            the id of the accident
   * @param ucaId
   *            the id of the unsafeControlAction
   */
  public CausalFactorUCALink(UUID causalFactorId, UUID ucaId) {
    this.causalFactorId = causalFactorId;
    this.ucaId = ucaId;
  }

  /**
   * Empty constructor for JAXB. Do not use it!
   * 
   * @author Lukas Balzer
   * 
   */
  public CausalFactorUCALink() {
    // empty constructor for JAXB
  }

  public UUID getCausalFactorId() {
    return this.causalFactorId;
  }

  /**
   * @param causalFactorId
   *            the accidentId to set
   */
  public void setCausalFactorId(UUID causalFactorId) {
    this.causalFactorId = causalFactorId;
  }

  public UUID getUCAId() {
    return this.ucaId;
  }

  /**
   * @param hazardId
   *            the hazardId to set
   */
  public void setHazardId(UUID hazardId) {
    this.ucaId = hazardId;
  }

  @Override
  protected UUID getKeyId(){
    return getCausalFactorId();
  }
  
  @Override
  protected UUID getValueId(){
    return getUCAId();
  }

}
