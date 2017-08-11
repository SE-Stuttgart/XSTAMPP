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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

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
public abstract class CSFigure extends Figure implements IControlStructureFigure {

	private final CSTextLabel textLabel;
	protected boolean isDirty = true;
	private final Image image;
	private boolean canConnect = false;
	private final UUID componentID;
	private int leftMargin = 0;
	protected static final int CENTER_COMPENSATION = 2;
	/**
	 * The border which is normally shown as decoration
	 * 
	 * @author Lukas Balzer
	 */
	public static final Color STANDARD_BORDER_COLOR = ColorConstants.black;
	private boolean withIcon;
	private LineBorder border;
	private IPreferenceStore store;
	protected Rectangle rect;
	private String text;
  private boolean hideBorder = false;

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
	 *            TODO
	 * 
	 */
	protected CSFigure(UUID id, Boolean isDashed) {
		this(id, null, isDashed);
	}

	/**
	 * The CSFigure constructor creates a new <code>XYLayout</code> instance and
	 * sets the Layout manager for the Components
	 * 
	 * 
	 * @author Lukas Balzer
	 * @param isDashed
	 *            TODO
	 * 
	 */
	protected CSFigure(UUID id, Image img, boolean isDashed) {
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
	}

	@Override
	public void paintChildren(Graphics graphics) {
		super.paintChildren(graphics);
		if ((this.image != null) && this.withIcon) {
      double newPos = CSFigure.IMG_WIDTH * Math.min(1, graphics.getAbsoluteScale());
      Rectangle rect = this.textLabel.getBounds();
      this.setConstraint(this.textLabel, new Rectangle((int) newPos, rect.y, rect.width, rect.height));
      graphics.scale(0.25);
      graphics.setAntialias(SWT.ON);
      graphics.drawImage(this.image, 1, 1);
      graphics.scale(4);
    }
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
	 *            the Color of the new Border
	 */
	public void setBorder(Color color) {
	  if(hideBorder) {
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
		Rectangle bounds = getBounds().getCopy().expand(2 * RootFigure.COMP_OFFSET, 2 * RootFigure.COMP_OFFSET);
		if (useOffset()) {
			boolean contains = bounds.contains(x, y);
			return contains;
		}
		return super.containsPoint(x, y);
	}

	@Override
	public void refresh() {
		if (isDirty) {
			isDirty = false;
			setBounds(rect);
			
			for (Object child : getChildren()) {
        if (child instanceof IControlStructureFigure) {
          ((IControlStructureFigure) child).refresh();
        }
      }
			if (this.getChildren().size() > 1) {
				// the height of the rectangle is set to the ideal height for
				// the
				// given width
				this.textLabel.setSize(this.getBounds().width - this.leftMargin - 4, -1);
				this.setConstraint(this.textLabel, this.textLabel.getBounds());
			} else {

				this.getTextField().setSize(new Dimension(rect.width - this.leftMargin - 4, rect.height));
				this.setConstraint(this.textLabel,
						new Rectangle(this.leftMargin, 1, rect.width - this.leftMargin - 4, rect.height));
			}
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
	 *            The Point of the request
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
	 *            the anchorPoint for which a feedback should be created
	 */
	@Override
	public void addHighlighter(Point ref) {
		if(getParent() instanceof RootFigure) {
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
		if (this.getParent() == null) {
			return;
		}
		((RootFigure) this.getParent()).removeHighlighter();
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

	protected void setDecoration(boolean withDeco) {
		this.withIcon = withDeco;
		if (withDeco) {
			this.leftMargin = CSFigure.IMG_WIDTH;
		} else {
			this.leftMargin = 0;
		}
	}

	@Override
	public boolean hasDeco() {
		return this.withIcon;
	}

	@Override
	public void showFeedback() {
		// no feedback by default
	}

	@Override
	public void disableFeedback() {
		// no feedback by default

	}

	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		getTextField().setPreferenceStore(store);
		this.store = store;
	}

	protected IPreferenceStore getPreferenceStore() {
		return this.store;
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
	 *            the canConnect to set
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
}
