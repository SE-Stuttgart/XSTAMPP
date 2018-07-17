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

package xstampp.astpa.model.controlstructure.components;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xstampp.astpa.model.controlstructure.interfaces.IAnchor;

/**
 * Class that represents anchor points of a connection in the control structure
 * diagram
 * 
 * @author Fabian Toth
 */
@XmlRootElement(name = "anchor")
@XmlAccessorType(XmlAccessType.NONE)
public class Anchor implements IAnchor {

  @XmlElement
  private Boolean isFlying;

  @XmlElement
  private int xOrientation;

  @XmlElement
  private int yOrientation;

  @XmlElement
  private Integer xOrientationWithPm;

  @XmlElement
  private Integer yOrientationWithPm;

  @XmlElement
  private UUID ownerId;

  @XmlElement
  private UUID id;

  /**
   * Constructs a new anchor with the given values
   * 
   * @param isFlying
   *          true if this anchor is flying, which means that it is directly
   *          created on the root
   * @param xOrientation
   *          the xOrientation of the new anchor
   * @param yOrientation
   *          the yOrientation of the new anchor
   * @param ownerId
   *          the id of the owner component
   * 
   * @author Fabian Toth
   */
  public Anchor(boolean isFlying, int xOrientation, int yOrientation,
      UUID ownerId) {
    this.isFlying = isFlying ? true : null;
    this.xOrientation = xOrientation;
    this.yOrientation = yOrientation;
    this.xOrientationWithPm = null;
    this.yOrientationWithPm = null;
    this.ownerId = ownerId;
    this.id = UUID.randomUUID();
  }

  public Anchor clone() {
    Anchor anchor = new Anchor(isFlying, xOrientation, yOrientation, ownerId);
    anchor.id = this.id;
    return anchor;
  }

  /**
   * Empty constructor for JAXB. Do not use it!
   * 
   * @author Fabian Toth
   */
  public Anchor() {
    // empty constructor for JAXB
  }

  /**
   * @return the isSource
   */
  @Override
  public boolean isFlying() {
    return this.isFlying == null ? false : this.isFlying;
  }

  @Override
  public void setIsFlying(boolean isFlying) {
    this.isFlying = isFlying ? true : null;
  }

  /**
   * @param isSource
   *          the isSource to set
   */
  public void setSource(boolean isSource) {
    this.isFlying = isSource;
  }

  /**
   * @return the xOrientation
   */
  @Override
  public int getxOrientation(boolean withPm) {
    return withPm && this.xOrientationWithPm != null ? this.xOrientationWithPm : this.xOrientation;
  }

  /**
   * @param xOrientation
   *          the xOrientation to set
   * @param withPm
   *          <b><span style="color:blue;">true</span></b> if the given orientation is meant for the
   *          visualization of the control structure with process models<br>
   *          <b><span style="color:blue;">false</span></b> otherwise
   */
  public void setxOrientation(int xOrientation, boolean withPm) {
    if (withPm) {
      this.xOrientationWithPm = xOrientation == this.xOrientation ? null : xOrientation;
    } else {
      this.xOrientation = xOrientation;
    }
  }

  public boolean synchronizeOrientation() {
    boolean result = xOrientationWithPm == null || yOrientationWithPm == null;
    this.xOrientationWithPm = null;
    this.yOrientationWithPm = null;
    return result;
  }

  /**
   * @return the yOrientation
   */
  @Override
  public int getyOrientation(boolean withPm) {
    return withPm && this.yOrientationWithPm != null ? this.yOrientationWithPm : this.yOrientation;
  }

  /**
   * @param yOrientation
   *          the yOrientation to set
   * @param withPm
   *          <b><span style="color:blue;">true</span></b> if the given orientation is meant for the
   *          visualization of the control structure with process models<br>
   *          <b><span style="color:blue;">false</span></b> otherwise
   */
  public void setyOrientation(int yOrientation, boolean withPm) {
    if (withPm) {
      this.yOrientationWithPm = yOrientation == this.yOrientation ? null : yOrientation;
    } else {
      this.yOrientation = yOrientation;
    }
  }

  /**
   * @return the ownerId
   */
  @Override
  public UUID getOwnerId() {
    return this.ownerId;
  }

  /**
   * @param ownerId
   *          the ownerId to set
   */
  public void setOwnerId(UUID ownerId) {
    this.ownerId = ownerId;
  }

  /**
   * @return the id
   */
  public UUID getId() {
    return this.id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(UUID id) {
    this.id = id;
  }

}
