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

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;

import xstampp.astpa.controlstructure.controller.editparts.CSAbstractEditPart;
import xstampp.astpa.haz.controlstructure.interfaces.IAnchor;
import xstampp.preferences.IControlStructureConstants;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class CSAnchor extends AbstractConnectionAnchor implements IAnchorFigure {

	private static final int MAX_PERCENT = 100;
	private Point lastRef;
	private Rectangle refLayout;
	private IFigure connection;

	/**
	 * the xOrientations array which stores the locations on the x-axis as
	 * values between 0 and 1 where the user should be able to create a CSAnchor
	 */
	private static final float[] X_ORIENTATIONS = { 0.25f, 0.5f, 0.75f };
	/**
	 * the yOrientations array which stores the locations on the y-axis as
	 * values between 0 and 1 where the user should be able to create a CSAnchor
	 */
	private static final float[] Y_ORIENTATIONS = { 0.25f, 0.5f, 0.75f };

	private Map<Integer, Point> anchorPoints;
	private Point referencePoint;
	private Point anchorFactor;
	private IPreferenceStore store;

	private CSAnchor(IFigure owner) {
		super(owner);
		this.refLayout = owner.getBounds().getCopy();
		owner.translateToAbsolute(this.refLayout);
		this.referencePoint = this.refLayout.getCenter();
		this.anchorPoints = new TreeMap<Integer, Point>();
		this.anchorFactor = new Point(-1, -1);
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param owner
	 *            The Figure this AnchorPoint lies on
	 * @param ref
	 *            the reference Point in absolute coordinates on the Editor
	 * @param store2
	 */
	public CSAnchor(IFigure owner, Point ref, IPreferenceStore store2) {
		this(owner);
		setPreferenceStore(store2);
		this.lastRef = ref.getCopy();
		if (this.store.getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS)) {
			this.calulateAnchorFac(this.lastRef);
		} else {
			this.setAnchorFactor(this.calcAnchorNr());
		}

		this.calcReferencePoint();
		((CSFigure) this.getOwner()).addHighlighter(this.referencePoint);
	}

	/***
	 * expects the coordinates of the point in question relative to its the
	 * owner of this anchor object
	 * 
	 * @param x
	 * @param y
	 * @return whether or not the point is under the bisection which divides the
	 *         lower left corners' angle
	 */
	private boolean TEST_calculateLowerBisection(double x, double y) {
		y = this.getOwner().getBounds().preciseHeight() - y;
		double ratio = this.getOwner().getBounds().preciseHeight() / this.getOwner().getBounds().preciseWidth();
		boolean underBisection;
		if (x > 0) {
			// the pointRatio is the aspectRatio of the rectangle between
			// lower-left edge of the owner and point
			double pointRatio = y / Math.max(Double.MIN_NORMAL, x);
			// if x is greater than zero and the pointRatio is smaller than the
			// ratio than the point is under the angle bisecting line
			underBisection = pointRatio <= ratio;
		} else if (x < 0) {
			// the pointRatio is the aspectRatio of the rectangle between
			// lower-left edge of the owner and point
			double pointRatio = -y / Math.max(Double.MIN_NORMAL, -x);
			// if x is greater than zero and the pointRatio is smaller than the
			// ratio than the point is under the angle bisecting line
			underBisection = pointRatio >= ratio;
		} else {
			underBisection = y <= 0;
		}
		return underBisection;
	}

	/***
	 * expects the coordinates of the point in question relative to its the
	 * owner of this anchor object
	 * 
	 * @param x
	 * @param y
	 * @return whether or not the point is under the bisection which divides the
	 *         upper left corners' angle
	 */
	private boolean TEST_calculateInverseBisetion(double x, double y) {
		y = this.getOwner().getBounds().preciseHeight() - y;
		x = this.getOwner().getBounds().preciseWidth() - x;
		double ratio = this.getOwner().getBounds().preciseHeight() / this.getOwner().getBounds().preciseWidth();
		boolean underBisection;
		if (x > 0) {
			// the pointRatio is the aspectRatio of the rectangle between
			// lower-left edge of the owner and point
			double pointRatio = y / Math.max(Double.MIN_NORMAL, x);
			// if x is greater than zero and the pointRatio is smaller than the
			// ratio than the point is under the angle bisecting line
			underBisection = pointRatio <= ratio;
		} else if (x < 0) {
			// the pointRatio is the aspectRatio of the rectangle between
			// lower-left edge of the owner and point
			double pointRatio = -y / Math.max(Double.MIN_NORMAL, -x);
			// if x is greater than zero and the pointRatio is smaller than the
			// ratio than the point is under the angle bisecting line
			underBisection = pointRatio >= ratio;
		} else {
			underBisection = y <= 0;
		}
		return underBisection;
	}

	/**
	 * calculates the relative position of a point on the parent component
	 *
	 * @author Lukas Balzer
	 *
	 * @param ref
	 *            the point for which the relative position is requested
	 */
	private void calulateAnchorFac(Point ref) {
		this.getOwner().translateToRelative(ref);
		// translate the point reference so that it points to owner relative
		// by defining the left top edge as zero
		ref.performTranslate(-this.getOwner().getBounds().x, -this.getOwner().getBounds().y);
		
		boolean underBisect = TEST_calculateLowerBisection(ref.preciseX(), ref.preciseY());
		boolean underBisect_Inv = TEST_calculateInverseBisetion(ref.preciseX(), ref.preciseY());
		if (underBisect && underBisect_Inv) {
			this.anchorFactor.x = (int) (ref.x / getOwner().getBounds().preciseWidth() * MAX_PERCENT);
			this.anchorFactor.y = MAX_PERCENT;
		} else if (!underBisect && underBisect_Inv) {
			this.anchorFactor.x = 0;
			this.anchorFactor.y = (int) (ref.y / getOwner().getBounds().preciseHeight() * MAX_PERCENT);
		} else if (underBisect && !underBisect_Inv) {
			this.anchorFactor.x = MAX_PERCENT;
			this.anchorFactor.y = (int) (ref.y / getOwner().getBounds().preciseHeight() * MAX_PERCENT);
		} else if (!underBisect && !underBisect_Inv) {
			this.anchorFactor.x = (int) (ref.x / getOwner().getBounds().preciseWidth() * MAX_PERCENT);
			this.anchorFactor.y = 0;
		}
		this.anchorFactor.x = Math.max(0, this.anchorFactor.x);
		this.anchorFactor.x = Math.min(100, this.anchorFactor.x);
		this.anchorFactor.y = Math.max(0, this.anchorFactor.y);
		this.anchorFactor.y = Math.min(100, this.anchorFactor.y);
	}

	/**
	 * This constructor will be used to create Anchors directly with an
	 * specified referencePoint
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param owner
	 *            the related Figure
	 * @param model
	 *            the Anchor model which should be used as reference
	 */
	public CSAnchor(IFigure owner, IAnchor model) {
		this(owner);

		((CSFigure) this.getOwner()).removeHighlighter();
		this.anchorFactor.x = model.getxOrientation();
		this.anchorFactor.y = model.getyOrientation();
		this.calcReferencePoint();

	}

	@Override
	public void updateAnchor(IAnchor model, Object owner) {
		((CSFigure) this.getOwner()).removeHighlighter();
		this.anchorFactor.x = model.getxOrientation();
		this.anchorFactor.y = model.getyOrientation();
		if (owner instanceof CSAbstractEditPart && ((CSAbstractEditPart) owner).getFigure() != getOwner()) {
			this.setOwner(((CSAbstractEditPart) owner).getFigure());
		}
		if (getConnection() != null) {
		}
	}

	@Override
	public Point getLocation(Point reference) {
		return this.calcReferencePoint();
	}

	private Point calcReferencePoint() {
		this.refLayout = this.getOwner().getBounds().getCopy();
		this.getOwner().translateToAbsolute(this.refLayout);

		if (this.anchorFactor.x != -1) {

			this.referencePoint.y = (int) (this.refLayout.y
					+ ((this.refLayout.height * (float) this.anchorFactor.y) / CSAnchor.MAX_PERCENT));
			this.referencePoint.x = (int) (this.refLayout.x
					+ ((this.refLayout.width * (float) this.anchorFactor.x) / CSAnchor.MAX_PERCENT));
		}
		this.referencePoint.x = this.referencePoint.x;
		this.referencePoint.y = this.referencePoint.y;
		return this.referencePoint;
	}

	private void setAnchorFactor(int anchorNr) {
		int tmp;
		if (anchorNr < (2 * CSAnchor.X_ORIENTATIONS.length)) {
			this.anchorFactor.x = (int) (CSAnchor.X_ORIENTATIONS[(anchorNr / 2)] * CSAnchor.MAX_PERCENT);

			if ((anchorNr % 2) == 0) {
				this.anchorFactor.y = 0;
			} else {
				this.anchorFactor.y = CSAnchor.MAX_PERCENT;
			}

		} else {
			tmp = anchorNr - (2 * CSAnchor.X_ORIENTATIONS.length);
			this.anchorFactor.y = (int) (CSAnchor.Y_ORIENTATIONS[(tmp / 2)] * CSAnchor.MAX_PERCENT);

			if ((anchorNr % 2) == 0) {
				this.anchorFactor.x = 0;
			} else {
				this.anchorFactor.x = CSAnchor.MAX_PERCENT;
			}
		}

	}

	private int calcAnchorNr() {

		int anchorNr = 0;
		this.refreshAnchors();
		double distance = Integer.MAX_VALUE;

		int anchorCount = 2 * (CSAnchor.X_ORIENTATIONS.length + CSAnchor.Y_ORIENTATIONS.length);
		double distToCursor;
		for (int i = 0; i < anchorCount; i++) {
			Point nextAnchor = this.anchorPoints.get(i);
			distToCursor = this.lastRef.getDistance(nextAnchor);

			if (distToCursor < distance) {
				distance = distToCursor;
				this.referencePoint = nextAnchor.getCopy();
				anchorNr = i;
			}
		}
		return anchorNr;
	}

	/**
	 * This private method refreshes the Anchor points which are located on the
	 * sides of the Component, left anchors are stored at even numbers
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	private void refreshSideAnchors() {
		int anchorNr = CSAnchor.X_ORIENTATIONS.length * 2;
		for (float factor : CSAnchor.Y_ORIENTATIONS) {
			// for each entry in the array two anchorPoints are created for
			// the left and the right side
			Point positionLeft = new Point();
			positionLeft.x = this.refLayout.x;
			positionLeft.y = (int) (this.refLayout.y + (this.refLayout.height * factor));

			this.anchorPoints.put(anchorNr, positionLeft);
			anchorNr++;
			Point positionRight = new Point();
			positionRight.x = positionLeft.x + this.refLayout.width;
			positionRight.y = positionLeft.y;
			this.anchorPoints.put(anchorNr, positionRight);
			anchorNr++;
		}
	}

	/**
	 * This private method refreshes the Anchor points which are located on the
	 * top and the Bottom of the Component, the Top anchors are stored with even
	 * numbers
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	private void refreshTopBottomAnchor() {
		int anchorNr = 0;
		for (float factor : CSAnchor.X_ORIENTATIONS) {
			// for each entry in the array two anchorPoints are created for
			// the top and the bottom
			Point positionTop = new Point();
			positionTop.x = (int) (this.refLayout.x + (this.refLayout.width * factor));
			positionTop.y = this.refLayout.y;
			this.anchorPoints.put(anchorNr, positionTop);
			anchorNr++;
			Point positionBottom = new Point();
			positionBottom.x = positionTop.x;
			positionBottom.y = positionTop.y + this.refLayout.height;
			this.anchorPoints.put(anchorNr, positionBottom);

			anchorNr++;

		}

	}

	@Override
	public Point getAnchorFactor() {
		return this.anchorFactor;

	}

	/**
	 * 
	 * @author Lukas
	 * 
	 * @return the direction which leads vertically away from the component
	 */
	public Point getOrientation() {
		if (this.getAnchorFactor().x == CSAnchor.MAX_PERCENT) {
			return new Point(1, 0);
		}
		if (this.getAnchorFactor().x == 0) {
			return new Point(-1, 0);
		}
		if (this.getAnchorFactor().y == CSAnchor.MAX_PERCENT) {
			return new Point(0, 1);
		}
		if (this.getAnchorFactor().y == 0) {
			return new Point(0, -1);
		}
		return new Point(0, 0);
	}

	private void refreshAnchors() {
		this.refreshTopBottomAnchor();
		this.refreshSideAnchors();
	}

	@Override
	public Point getReferencePoint() {
		return this.lastRef;

	}

	@Override
	public Point getAnchorPosition() {
		return this.referencePoint;
	}

	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		this.store = store;
	}

	/**
	 * @return the connection
	 */
	public IFigure getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	public void setConnection(IFigure connection) {
		this.connection = connection;
	}

}
