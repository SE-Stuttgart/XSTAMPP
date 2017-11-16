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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.preferences.IControlStructureConstants;

/**
 * 
 * This class contains methods which creating a root figure. Other figures will
 * be displayed on it
 * 
 * @version 1.0
 * @author Aliaksei Babkoivch, Lukas Balzer
 * 
 */

public class RootFigure extends CSFigure implements MouseMotionListener {

  private Figure highlighter;
  private boolean useOffset = false;
  private static final int ACTIVE_ANCHOR_HIGHLIGHTER_WIDTH = 10;
  private static final int NONACTIVE_ANCHOR_HIGHLIGHTER_WIDTH = 6;
  public static final int COMP_OFFSET = 20;

  private List<IFigure> componentList = new ArrayList<>();
  private boolean generalEnable = false;

  /**
   * Constructs the RootFigure which is used as the basis of the
   * GraphicalEditor, this method is only called once in CSEditor
   * 
   * @author Lukas Balzer
   * @param id
   *          the id which the figure inherits from its model
   * 
   */
  public RootFigure(UUID id) {
    super(id, false);
    this.useLocalCoordinates();
    this.addMouseMotionListener(this);
  }

  @Override
  protected boolean useLocalCoordinates() {
    return true;
  }

  @Override
  public ConnectionAnchor getConnectionAnchor(Point ref) {

    IAnchorFigure temp = new CSFlyAnchor(this, ref);

    return temp;
  }

  @Override
  public void addHighlighter(Point ref) {

    this.translateToRelative(ref);
    int width = RootFigure.ACTIVE_ANCHOR_HIGHLIGHTER_WIDTH;
    int offset = (width / 2) - 1;
    if (this.highlighter == null) {
      this.highlighter = new Figure() {
        @Override
        public boolean containsPoint(int x, int y) {
          return false;
        }
      };
      this.highlighter.setBorder(new LineBorder(ColorConstants.blue, 3));
      this.highlighter.setOpaque(false);
      this.add(this.highlighter);
    }
    Rectangle rec = new Rectangle(ref.x - offset, ref.y - offset, width,
        width);
    this.translateFromParent(rec);
    this.highlighter.setBounds(rec);
    this.highlighter.setVisible(true);
  }

  /**
   * 
   * @author Aliaksei Babkovich, Lukas Balzer
   * @param childrenList
   *          a list of all children model classes
   * 
   */
  @SuppressWarnings("unchecked")
  public void addAnchorsGrid(List<IRectangleComponent> childrenList) {

    if (getPreferenceStore()
        .getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS)) {
      return;
    }
    int width = RootFigure.NONACTIVE_ANCHOR_HIGHLIGHTER_WIDTH;
    int offset = (width / 2);
    List<Point> childrenAnchorsPoints = new ArrayList<>();
    Rectangle childLayout;
    Figure anchorHighlighter = null;
    List<?> childFigures = new ArrayList<>();
    childFigures.addAll(getChildren());
    for (Object child : childFigures) {
      if (child instanceof CSFigure && ((CSFigure) child).isCanConnect()) {
        childLayout = new Rectangle(((CSFigure) child).getBounds());
        for (float factor : CSFigure.X_ORIENTATIONS) {
          // for each entry in the array two anchorPoints are created
          // for
          // the top and the bottom
          Point positionTop = new Point();
          positionTop.x = (int) (childLayout.x + (childLayout.width * factor));
          positionTop.y = childLayout.y;
          childrenAnchorsPoints.add(positionTop);

          Point positionBottom = new Point();
          positionBottom.x = positionTop.x;
          positionBottom.y = positionTop.y + childLayout.height;
          childrenAnchorsPoints.add(positionBottom);
        }
        for (float factor : CSFigure.Y_ORIENTATIONS) {
          // for each entry in the array two anchorPoints are created
          // for
          // the left and the right side
          Point positionLeft = new Point();
          positionLeft.x = childLayout.x;
          positionLeft.y = (int) (childLayout.y + (childLayout.height * factor));
          childrenAnchorsPoints.add(positionLeft);

          Point positionRight = new Point();
          positionRight.x = positionLeft.x + childLayout.width;
          positionRight.y = positionLeft.y;
          childrenAnchorsPoints.add(positionRight);
        }

        for (Point anchor : childrenAnchorsPoints) {
          anchorHighlighter = new Figure();
          anchorHighlighter
              .setBackgroundColor(ColorConstants.lightBlue);
          anchorHighlighter.setOpaque(true);
          this.add(anchorHighlighter);
          Rectangle rec = new Rectangle(anchor.x - offset, anchor.y
              - offset, width, width);
          this.translateFromParent(rec);
          anchorHighlighter.setBounds(rec);
          anchorHighlighter.setVisible(true);
          this.componentList.add(anchorHighlighter);
        }
      }
    }

  }

  public void setConstraint(Rectangle r) {
    setBounds(getBounds().union(r));
  }

  @Override
  public void removeHighlighter() {
    if (this.highlighter != null) {
      this.highlighter.setVisible(false);
    }
  }

  /**
   * removes the anchors grid from the editorview
   * 
   * @author Lukas Balzer
   * 
   */
  public void removeAnchorsGrid() {
    for (IFigure component : this.componentList) {
      component.setVisible(false);
      this.remove(component);
    }
    this.componentList.clear();
  }

  @Override
  public IFigure findFigureAt(int x, int y, TreeSearch search) {
    return super.findFigureAt(x, y, search);
  }

  @Override
  public void repaint() {
    super.repaint();
    this.removeHighlighter();
  }

  @Override
  public void paintChildren(Graphics graphics) {
    super.paintChildren(graphics);
  }

  @Override
  public void setLayout(Rectangle rect) {
    this.setBounds(rect);
  }

  @Override
  public void disableOffset() {
    this.useOffset = false;
  }

  @Override
  public void enableOffset() {
    this.useOffset = true;
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   */
  public void generalEnableOffset() {
    this.generalEnable = true;
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   */
  public void generalDisableOffset() {
    this.generalEnable = false;
  }

  @Override
  public void mouseDragged(MouseEvent me) {
    // Does nothing by default
  }

  @Override
  public void mouseEntered(MouseEvent me) {
    if (this.generalEnable) {
      this.enableOffset();
    }

  }

  @Override
  public void mouseExited(MouseEvent me) {
    this.removeHighlighter();
    this.disableOffset();
  }

  @Override
  public void mouseHover(MouseEvent me) {

    // Does nothing by default
  }

  @Override
  public void mouseMoved(MouseEvent me) {
    // // Does nothing by default
  }

  @Override
  public void setDirty() {
    // root component is always refreshed
  }

  @Override
  public boolean useOffset() {
    return useOffset;
  }
}
