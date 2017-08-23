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
package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import xstampp.astpa.controlstructure.controller.editparts.IMemberEditPart;
import xstampp.astpa.controlstructure.utilities.CSTextLabel;

public class ConnectionFigure extends PolylineConnection implements IControlStructureFigure {

  private static final int[] arrowHead = new int[] { 0, 0, -10, 4, -10, -4 };
  private PolylineConnection feedback;
  private UUID currentFeedbackId;
  private UUID id;
  private IPreferenceStore store;
  private IAnchorFigure targetAnchor;
  private IAnchorFigure sourceAnchor;
  float alpha;
  private Rectangle rect;
  private Point oldPoint;
  private boolean fixed;

  public ConnectionFigure(UUID id) {
    super();
    this.id = id;
  }

  public void setFixed(boolean fixed) {
    this.fixed = fixed;
  }

  @Override
  protected boolean useLocalCoordinates() {
    return true;
  }

  private static final int ARROW_WIDTH = 6;
  private static final int ARROW_HEIGHT = 6;

  @Override
  public void paintFigure(Graphics graphics) {
    super.paintFigure(graphics);
    if (fixed && getPoints().size() >= 2) {
      // graphics.pushState();
      Point p1 = getPoints().getPoint(getPoints().size() - 1);
      Point p2 = getPoints().getPoint(getPoints().size() - 2);
      int x = p1.x - p2.x;
      int y = p1.y - p2.y;
      Point _point = new Point(p1);
      alpha = 0;
      if (x < 0) {
        alpha = 180;
        _point.translate(-1, 0);
      } else if (y < 0) {
        alpha = 270;
        _point.translate(0, -1);
      } else if (y > 0) {
        alpha = 90;
        _point.translate(0, 1);
      } else {
        _point.translate(1, 0);
      }
      rect = new Rectangle(p1.getTranslated(-10, -10), p1.getTranslated(10, 10));
      graphics.setAntialias(SWT.ON);
      graphics.setClip(rect);
      graphics.setBackgroundColor(xstampp.util.ColorManager.COLOR_BLACK);
      graphics.translate(_point.x, _point.y);
      graphics.rotate(alpha);
      graphics.fillPolygon(arrowHead);
      graphics.rotate(-alpha);
      graphics.translate(-_point.x, -_point.y);
      this.oldPoint = _point;
      // graphics.restoreState();
    }

  }

  @Override
  protected void paintChildren(Graphics graphics) {
    super.paintChildren(graphics);
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  @Override
  public String getText() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setText(String text) {
    // TODO Auto-generated method stub

  }

  @Override
  public CSTextLabel getTextField() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setLayout(Rectangle rect) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addHighlighter(Point ref) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeHighlighter() {
    // TODO Auto-generated method stub

  }

  @Override
  public void disableOffset() {
    // TODO Auto-generated method stub

  }

  @Override
  public void enableOffset() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean useOffset() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setDeco(boolean deco) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean hasDeco() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void refresh() {
    // TODO Auto-generated method stub

  }

  @Override
  public void showFeedback() {
    setForegroundColor(ColorConstants.lightBlue);
  }

  @Override
  public void disableFeedback() {
    setForegroundColor(ColorConstants.black);

  }

  @Override
  protected boolean childrenContainsPoint(int x, int y) {
    Point p = new Point(x, y);
    translateFromParent(p);
    return super.childrenContainsPoint(p.x, p.y);
  }

  @Override
  public Rectangle getBounds() {
    Rectangle rct = getPoints().getBounds().getCopy();
    Rectangle bounds = super.getBounds();
    return rct.setSize(bounds.getSize());
  }

  public void eraseFeedback() {
    if (this.feedback != null) {
      this.feedback.setVisible(false);
    }

  }

  public IFigure getFeedback(IMemberEditPart member, Color color) {

    if (member != null && this.currentFeedbackId != member.getId()) {
      return getFeedback(member.getFigure(), color);
    }
    return null;
  }

  public IFigure getFeedback(Color color) {
    return getFeedback((IFigure) null, color);
  }

  public IFigure getFeedback(IFigure member, Color color) {
    if (this.feedback == null) {
      this.feedback = new PolylineConnection();
      this.feedback.setAlpha(150);
      this.feedback.setAntialias(SWT.ON);
      this.feedback.setLineWidth(4);
    }
    this.feedback.setForegroundColor(color);
    this.feedback.setVisible(true);
    this.feedback.setConnectionRouter(new FeedbackRouter(member));

    updateFeedback();
    return this.feedback;
  }

  private class FeedbackRouter extends AbstractRouter {
    private IFigure targetFigure;
    private ManhattanConnectionRouter router;

    public FeedbackRouter(IFigure member) {
      this.targetFigure = member;
      this.router = new ManhattanConnectionRouter();
    }

    public FeedbackRouter(Rectangle rect) {
      this.router = new ManhattanConnectionRouter();

    }

    @Override
    public void route(Connection connection) {
      connection.setSourceAnchor(getSourceAnchor());
      connection.setTargetAnchor(getTargetAnchor());
      this.router.route(connection);
      if (this.targetFigure == null) {
        return;
      }
      PointList list = connection.getPoints().getCopy();

      Point a = list.removePoint(list.size() - 1);

      Point b = list.getPoint(list.size() - 1);
      Dimension diff = a.getDifference(b);
      Point middle = b.getTranslated(diff.width / 2, diff.height / 2);
      Rectangle targetBounds = this.targetFigure.getBounds().getCopy();
      this.targetFigure.translateToAbsolute(targetBounds);
      connection.translateToRelative(targetBounds);
      Point center = targetBounds.getCenter().getCopy();

      for (int i = 0; i < 10; i++) {
        // this.targetFigure.translateToAbsolute(center);
        Rectangle loop_tmp = new Rectangle(center, middle);
        // this.targetFigure.translateToAbsolute(tmp);
        targetBounds = targetBounds.intersect(loop_tmp);
        center = targetBounds.getCenter().getCopy();
      }
      list.addPoint(middle);
      list.addPoint(center);
      list.addPoint(middle);
      list.addPoint(b);
      list.addPoint(a);
      // this.targetFigure.translateToAbsolute(list);
      connection.setPoints(list);
    }

  }

  public void updateFeedback() {
    if (this.feedback != null) {
      this.feedback.getConnectionRouter().route(this.feedback);
    }
  }

  @Override
  public void setPreferenceStore(IPreferenceStore store) {
    this.store = store;
  }

  @Override
  public void setDirty() {
    // TODO Auto-generated method stub
  }

  @Override
  public PointList getPoints() {
    PointList list = super.getPoints();
    for (int i = list.size() - 1; i >= 1; i--) {
      if (list.getPoint(i).equals(list.getPoint(i - 1))) {
        list.removePoint(i);
      }
    }
    return list;
  }
}
