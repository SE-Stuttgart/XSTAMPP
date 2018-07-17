/*******************************************************************************
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import xstampp.astpa.controlstructure.utilities.CSTextLabel;

/**
 * 
 * CSFigure is an abstract class which describes the basic Behavior and the
 * structure of a Component in this editor
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public class CSFigure extends Figure implements IControlStructureFigure, IPropertyChangeListener {

  /**
   * COMPONENT_FIGURE_DEFWIDTH is the default width to which the layout is set when the user sets
   * the Component from the palate without defining actual bounds
   */
  public static final Dimension COMPONENT_FIGURE_DEFSIZE = new Dimension(120, 40);

  /**
   * The border which is normally shown as decoration
   * 
   * @author Lukas Balzer
   */
  public static final Color STANDARD_BORDER_COLOR = ColorConstants.black;

  protected static final int CENTER_COMPENSATION = 2;

  private final CSTextLabel textLabel;
  private final Image image;
  private final UUID componentID;
  private String stepId;
  private boolean canConnect = false;
  private boolean hasDeco;
  private boolean hideBorder = false;
  private int leftMargin = 0;
  private Color decoBorderColor;
  private LineBorder border;
  private IPreferenceStore store;
  private String colorPreference;
  private String text;
  protected Rectangle rect;
  protected boolean isDirty = true;

  /**
   * the xOrientations array which stores the locations on the x-axis as
   * values between 0 and 1 where the user should be able to create a CSAnchor
   */
  public static final float[] X_ORIENTATIONS = { 0.25f, 0.5f, 0.75f };
  /**
   * the yOrientations array which stores the locations on the y-axis as
   * values between 0 and 1 where the user should be able to create a CSAnchor
   */
  public static final float[] Y_ORIENTATIONS = { 0.25f, 0.5f, 0.75f };
  private static final int IMG_WIDTH = 24;

  /**
   * The CSFigure constructor creates a new <code>XYLayout</code> instance and
   * sets the Layout manager for the Components
   * 
   * 
   * @author Lukas Balzer
   * @param isDashed
   *          whether the border of the figure should be dashed
   * 
   */
  public CSFigure(UUID id, Boolean isDashed) {
    this(id, null, isDashed);
  }

  /**
   * 
   * 
   * @author Lukas Balzer
   * @param id
   *          the id which the figure inherits from its model
   * @param img
   *          his Image will be displayed in the upper left corner of the component
   * @param color
   *          the Color of the Border
   * 
   */
  public CSFigure(UUID id, Image img, String colorPreference) {

    this(id, img, false);
    setCanConnect(true);
    this.setForegroundColor(ColorConstants.black);
    this.colorPreference = colorPreference;
    this.decoBorderColor = CSFigure.STANDARD_BORDER_COLOR;
    this.setDeco(true);
    setBackgroundColor(ColorConstants.white);
  }

  /**
   * The CSFigure constructor creates a new <code>XYLayout</code> instance and
   * sets the Layout manager for the Components
   * 
   * 
   * @author Lukas Balzer
   * @param id
   *          the id which the figure inherits from its model
   * @param img
   *          his Image will be displayed in the upper left corner of the component
   * @param isDashed
   *          whether the border of the figure should be dashed
   * 
   */
  public CSFigure(UUID id, Image img, boolean isDashed) {

    this.decoBorderColor = CSFigure.STANDARD_BORDER_COLOR;
    this.componentID = id;
    this.setLayoutManager(new XYLayout());
    this.image = img;
    if (isDashed) {
      this.border = new LineBorder(STANDARD_BORDER_COLOR, 1, SWT.BORDER_DASH);
    } else {
      this.border = new LineBorder(STANDARD_BORDER_COLOR, 1);
    }
    this.textLabel = new CSTextLabel(this);
    this.add(this.textLabel);
    isDirty = true;
    this.setConstraint(this.textLabel, new Rectangle(1, 1, -1, -1));
    this.setOpaque(true);
    this.setBackgroundColor(ColorConstants.white);
    setDeco(true);
  }

  @Override
  public void paintChildren(Graphics graphics) {
    if ((this.image != null) && this.hasDeco) {
      graphics.scale(0.25);
      graphics.setAntialias(SWT.ON);
      graphics.drawImage(this.image, 1, 1);
      graphics.scale(4);
    }
    super.paintChildren(graphics);

  }

  @Override
  public void setText(String text) {
    if (this.text == null || !text.equals(this.text)) {
      this.text = text;
      this.textLabel.setText(text);
      setDirty();
    }
  }

  @Override
  public String getText() {
    return this.textLabel.getText();
  }

  @Override
  public CSTextLabel getTextField() {
    return this.textLabel;
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param color
   *          the Color of the new Border
   */
  public void setBorder(Color color) {
    if (hideBorder) {
      super.setBorder(null);
    } else {
      setDirty();
      this.border.setColor(color);
      if (this.getChildren().size() > 1) {
        this.border.setWidth(2);
      } else {
        this.border.setWidth(1);
      }
      super.setBorder(this.border);
    }
  }

  public void hideBorder() {
    setBorder((Border) null);
    this.hideBorder = true;
  }

  @Override
  public void setLayout(Rectangle rect) {
    if (this.rect == null || !this.rect.equals(rect)) {
      setDirty();
      this.rect = rect;
    }
  }

  @Override
  public boolean containsPoint(int x, int y) {
    Rectangle bounds = getBounds().getCopy().expand(2 * RootFigure.COMP_OFFSET,
        2 * RootFigure.COMP_OFFSET);
    if (useOffset()) {
      boolean contains = bounds.contains(x, y);
      return contains;
    }
    return super.containsPoint(x, y);
  }

  @Override
  public void refresh(boolean ignoreDirtyCheck) {
    if (ignoreDirtyCheck || isDirty) {
      isDirty = false;
      setBounds(rect);
      for (Object child : getChildren()) {
        if (child instanceof IControlStructureFigure) {
          ((IControlStructureFigure) child).refresh(ignoreDirtyCheck);
        }
      }
      this.textLabel.setLocation(new Point(this.leftMargin, 0));
      int height = this.getChildren().size() > 1 ? -1 : rect.height;
      this.textLabel.setSize(rect.width - this.leftMargin - 4, height);

      this.setConstraint(this.textLabel, this.textLabel.getBounds());
      this.textLabel.setText(text);
      this.textLabel.repaint();
      this.getParent().setConstraint(this, rect);

    }
  }

  /**
   * 
   * 
   * @author Aliaksei Babkovich, Lukas Balzer
   * @param ref
   *          The Point of the request
   * @return The Anchor on which Graphical_Nodes can link with
   * @see org.eclipse.gef.NodeEditPart
   */
  public ConnectionAnchor getConnectionAnchor(Point ref) {

    CSAnchor temp = new CSAnchor(this, ref, this.store);
    return temp;
  }

  /**
   * This private method refreshes the Anchor points which are located on the
   * sides of the Component, left anchors are stored at even numbers
   * 
   * @author Lukas Balzer, Aliaksei Babkovich
   * @return the Map which contains all AnchorPoints mapped to an anchor
   *         number
   * 
   */
  public final Map<Integer, Point> getAnchors() {
    Map<Integer, Point> anchorPoints = new TreeMap<Integer, Point>();

    int anchorNr = 0;
    for (float factor : CSFigure.X_ORIENTATIONS) {
      // for each entry in the array two anchorPoints are created for
      // the top and the bottom
      Point positionTop = new Point();
      positionTop.x = (int) (this.getBounds().x + (this.getBounds().width * factor));
      positionTop.y = this.getBounds().y;
      anchorPoints.put(anchorNr, positionTop);
      anchorNr++;
      Point positionBottom = new Point();
      positionBottom.x = positionTop.x;
      positionBottom.y = positionTop.y + this.getBounds().height;
      anchorPoints.put(anchorNr, positionBottom);

      anchorNr++;
    }
    for (float factor : CSFigure.Y_ORIENTATIONS) {
      // for each entry in the array two anchorPoints are created for
      // the left and the right side
      Point positionLeft = new Point();
      positionLeft.x = this.getBounds().x;
      positionLeft.y = (int) (this.getBounds().y + (this.getBounds().height * factor));

      anchorPoints.put(anchorNr, positionLeft);
      anchorNr++;
      Point positionRight = new Point();
      positionRight.x = positionLeft.x + this.getBounds().width;
      positionRight.y = positionLeft.y;
      anchorPoints.put(anchorNr, positionRight);
      anchorNr++;
    }

    return anchorPoints;
  }

  /**
   * adds a anchor Feedback Rectangle to the Root, the Root has only on
   * Feedback Rectangle which can be added at Points currently to be
   * highlighted
   * 
   * @author Lukas Balzer
   * 
   * @param ref
   *          the anchorPoint for which a feedback should be created
   */
  @Override
  public void addHighlighter(Point ref) {
    if (getParent() instanceof RootFigure) {
      ((RootFigure) this.getParent()).addHighlighter(ref);
    }
  }

  /**
   * removes the Feedback Recangle from the editor
   * 
   * @author Lukas Balzer
   * 
   */
  @Override
  public void removeHighlighter() {
    if (this.getParent() != null && getParent() instanceof RootFigure) {
      ((RootFigure) this.getParent()).removeHighlighter();
    }
  }

  @Override
  public void setForegroundColor(Color newColor) {
    this.textLabel.setForegroundColor(newColor);
  }

  @Override
  public Color getForegroundColor() {
    return this.textLabel.getForegroundColor();
  }

  @Override
  public UUID getId() {
    return this.componentID;
  }

  @Override
  protected boolean useLocalCoordinates() {
    return true;
  }

  @Override
  public void disableOffset() {
    ((IControlStructureFigure) this.getParent()).disableOffset();
  }

  @Override
  public void enableOffset() {
    ((IControlStructureFigure) this.getParent()).enableOffset();
  }

  @Override
  public boolean useOffset() {
    return ((IControlStructureFigure) this.getParent()).useOffset();
  }

  @Override
  public void setDeco(boolean deco) {
    this.hasDeco = deco;
    if (deco) {
      this.border.setColor(this.decoBorderColor);
      setMargin(CSFigure.IMG_WIDTH);
    } else {
      this.border.setColor(STANDARD_BORDER_COLOR);
      setMargin(0);
    }
    if (!hideBorder) {
      setBorder(this.border);
    }
    for (Object child : this.getChildren()) {
      if (child instanceof IControlStructureFigure) {
        ((IControlStructureFigure) child).setDeco(deco);
      }
    }
  }

  private void setMargin(int margin) {
    if (this.leftMargin != margin) {
      this.leftMargin = margin;
      setDirty();
    }
  }

  @Override
  public boolean hasDeco() {
    return this.hasDeco;
  }

  @Override
  public void showFeedback() {
    // no feedback by default
  }

  @Override
  public void disableFeedback() {
    // no feedback by default

  }

  protected IPreferenceStore getPreferenceStore() {
    return this.store;
  }

  @Override
  public void propertyChange(PropertyChangeEvent arg0) {
    if (arg0.getProperty().equals(this.colorPreference) && getPreferenceStore() != null) {
      this.decoBorderColor = new Color(Display.getCurrent(),
          PreferenceConverter.getColor(getPreferenceStore(), this.colorPreference));
      this.repaint();
    }
  }

  @Override
  public void setPreferenceStore(IPreferenceStore store) {
    store.addPropertyChangeListener(this);
    if (this.colorPreference != null) {
      this.decoBorderColor = new Color(Display.getCurrent(),
          PreferenceConverter.getColor(store, colorPreference));
    }
    getTextField().setPreferenceStore(store);
    this.store = store;
    setDeco(hasDeco());
  }

  /**
   * @return the canConnect
   */
  public boolean isCanConnect() {
    return this.canConnect;
  }

  /**
   * Setter that decides whether or not a component is taken into account by
   * the
   * {@link RootFigure#findFigureAt(int, int, org.eclipse.draw2d.TreeSearch)}
   * method. This also enables/disables the connection grid in the connection
   * mode
   * 
   * @param canConnect
   *          the canConnect to set
   */
  public void setCanConnect(boolean canConnect) {
    this.canConnect = canConnect;
  }

  @Override
  public void setDirty() {
    isDirty = true;
    if (getParent() instanceof IControlStructureFigure) {
      ((IControlStructureFigure) getParent()).setDirty();
    }
  }

  public String getStepId() {
    return stepId;
  }

  public void setStepId(String stepId) {
    this.stepId = stepId;
  }
}
