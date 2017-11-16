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

package xstampp.astpa.controlstructure.figure;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.preference.IPreferenceStore;

import xstampp.astpa.model.controlstructure.interfaces.IAnchor;

/**
 * a Flying anchor used for creating a Anchor Point on the root
 * 
 * @author Lukas Balzer
 * 
 */
public class CSFlyAnchor extends AbstractConnectionAnchor implements
    IAnchorFigure {

  private Point lastRef;

  private Dimension offset;
  private CSAnchor relatedAnchor;

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param owner
   *          The Figure this AnchorPoint lies on
   * @param ref
   *          the reference Point on the Editor
   */
  public CSFlyAnchor(IFigure owner, Point ref) {
    super(owner);
    ((CSFigure) this.getOwner()).removeHighlighter();

    this.relatedAnchor = null;
    this.lastRef = new Point();
    this.lastRef = ref;
  }

  /**
   * This constructor will be used to create Anchors directly with an
   * specified referencePoint
   * 
   * @author Lukas Balzer
   * 
   * @param owner
   *          the related Figure
   * @param relation
   *          the figure this flying anchor is related to
   * @param model
   *          the Anchor model which should be used as reference
   */
  public CSFlyAnchor(IFigure owner, IAnchorFigure relation, IAnchor model) {
    super(owner);

    ((CSFigure) this.getOwner()).removeHighlighter();
    model.setIsFlying(true);
    this.relatedAnchor = (CSAnchor) relation;
    this.lastRef = new Point();
    int xOffset = model.getxOrientation();
    int yOffset = model.getyOrientation();

    this.offset = new Dimension(xOffset, yOffset);

  }

  @Override
  public void updateAnchor(IAnchor model, Object owner) {
    this.offset.setWidth(model.getxOrientation());
    this.offset.setHeight(model.getyOrientation());
    if (this.getOwner() != null) {
      this.getOwner().repaint();
    }
  }

  @Override
  public Point getLocation(Point reference) {

    if (this.relatedAnchor != null) {
      Dimension tmpOffset = this.offset.getCopy();
      this.lastRef = this.relatedAnchor.getLocation(null);

      this.relatedAnchor.getOwner().translateToAbsolute(tmpOffset);

      if (this.relatedAnchor.getOrientation().x != 0) {

        this.lastRef.setX(this.lastRef.x + tmpOffset.width);
      } else {
        this.lastRef.setY(this.lastRef.y + tmpOffset.height);
      }

    }
    this.lastRef.x = Math.max(0, this.lastRef.x);
    this.lastRef.y = Math.max(0, this.lastRef.y);
    return this.lastRef;
  }

  /**
   * the
   */
  @Override
  public Point getAnchorFactor() {
    if (this.offset == null) {
      return new Point(1, 1);
    }

    return new Point(this.offset.width, this.offset.height);
  }

  /**
   * Sets the related Anchor, the position of this will always be calculated
   * with respect to the relatedAnchor
   * 
   * @author Lukas Balzer
   * 
   * @param relation
   *          the Anchor this flying anchor is relative to
   */
  public void setRelation(CSAnchor relation) {
    this.relatedAnchor = relation;

    int xOffset = this.lastRef.x - relation.getAnchorPosition().x;
    int yOffset = this.lastRef.y - relation.getAnchorPosition().y;

    this.offset = new Dimension(xOffset, yOffset);
    this.getOwner().translateToRelative(this.offset);
  }

  @Override
  public Point getAnchorPosition() {

    return new Point(this.offset.width, this.offset.height);
  }

  @Override
  public void setPreferenceStore(IPreferenceStore store) {
  }
}
