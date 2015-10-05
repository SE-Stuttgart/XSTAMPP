/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package acast.controlstructure.utilities;

import java.util.Iterator;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import acast.controlstructure.CSAbstractEditor;
import acast.controlstructure.CSEditor;
import acast.controlstructure.controller.editparts.RootEditPart;
import acast.controlstructure.figure.CSAnchor;
import acast.model.controlstructure.interfaces.IRectangleComponent;


/**
 * this class routes the connections around all drawn components if needed
 * 
 * ----the algorithm currently runs probably unstable and will may cause
 * exceptions!!!----
 * 
 * @author Lukas Balzer
 * 
 */
public class CSConnectionRouter extends AbstractRouter {

	private ManhattanConnectionRouter manhattanRouter = new ManhattanConnectionRouter();
	private RootEditPart owner;
	private int[][] csMatrix;

	/**
	 * the minimal distance between a connection and a Component
	 */
	public static final int INTERVALL = 10;

	/**
	 * this value is added to fields in the matrix which should not be visited
	 * by a connection
	 */
	private static final int SPACE_RESERVED = 0;

	/**
	 * This constant says wheter a field in a matrix has been visited or not
	 */
	private static final int NOT_VISITED = Integer.MAX_VALUE;

	/**
	 * route decides whether it the connection calculated by the manhattanRouter
	 * intersects with a child or not with this tolerance
	 */
	private static final Insets INTERSECTION_THRESHOLD = new Insets(3, 3, 3, 3);
	private static final Point RESERVED_POINT = new Point(-1, -1);

	/**
	 * this constant a Point with the coordinates (-1,0), it is used to tell
	 * that the path-recursion moves up
	 */
	private static final Point RIGHT = new Point(1, 0);

	/**
	 * this constant a Point with the coordinates (1,0), it is used to tell that
	 * the path-recursion moves left
	 */
	private static final Point LEFT = new Point(1, 0);

	/**
	 * this constant a Point with the coordinates (0,-1), it is used to tell
	 * that the path-recursion moves up
	 */
	private static final Point UP = new Point(0, -1);

	/**
	 * this constant a Point with the coordinates (0,1), it is used to tell that
	 * the path-recursion moves down
	 */
	private static final Point DOWN = new Point(0, 1);

	/**
	 * this constant a Point with the coordinates (0,0), it is used to tell that
	 * the path-recursion has terminated
	 */
	private static final Point STILL = new Point(0, 0);

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param ownerPart
	 *            the Part in which the connections should be calculated in
	 */
	public CSConnectionRouter(RootEditPart ownerPart) {
		this.owner = ownerPart;
	}

	@Override
	public void route(Connection connection) {
		this.manhattanRouter.route(connection);
		if ((connection.getTargetAnchor() instanceof CSAnchor)
				&& (connection.getSourceAnchor() instanceof CSAnchor)
				&& this.insectOwner(connection)) {
			// idealize is only called in the case that the connection
			// intersects with a component
			this.idealize(connection);
		}
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param connection
	 *            the connection which should be idealized
	 */
	public void idealize(Connection connection) {
		if (!this.fillMatrix()) {
			return;
		}
		PointList tempPoints = new PointList();
		Point endOrientation = ((CSAnchor) connection.getTargetAnchor())
				.getOrientation();

		Point startOrientation = ((CSAnchor) connection.getSourceAnchor())
				.getOrientation();

		Point start = connection.getPoints().getFirstPoint();
		tempPoints.addPoint(connection.getPoints().getFirstPoint());
		Point end = connection.getPoints().getLastPoint();

		do {
			start.translate(startOrientation);
		} while (this.csMatrix[start.x][start.y] == CSConnectionRouter.SPACE_RESERVED);

		if (connection.getTargetAnchor() instanceof CSAnchor) {
			do {
				end.translate(endOrientation);
			} while (this.csMatrix[end.x][end.y] == CSConnectionRouter.SPACE_RESERVED);

		}

		tempPoints = this.idealPath(tempPoints, start.x, start.y, 0,
				startOrientation, end, false, false);
		tempPoints.addPoint(connection.getPoints().getLastPoint());

		connection.setPoints(tempPoints);
	}

	/**
	 * this function moves recursively in the direction to the end point and
	 * marks the shortest path,moving a straight line
	 * 
	 * @author Lukas
	 * 
	 * @param list
	 * @param x
	 *            the current x position
	 * @param y
	 *            the current y position
	 * @param dist
	 *            the current distance of the path
	 * @param direction
	 *            the direction in which the algorithm moves, this information
	 *            is needed to calculate the distance
	 * @param end
	 *            the end point
	 * @param forceY
	 * @param forceX
	 * @return
	 */
	private PointList idealPath(PointList list, int x, int y, int distance,
			Point direction, Point end, boolean forceY, boolean forceX) {

		boolean yForced = forceY;
		boolean xForced = forceX;
		int changeX;
		int changeY;
		// the initial value is one, dist sums up the knots needed to reech the
		// end
		int dist = distance + list.size();
		PointList idealPoints = list.getCopy();

		if (end.equals(x, y)) {
			idealPoints.addPoint(end);
		} else if (this.csMatrix[x][y] == CSConnectionRouter.SPACE_RESERVED) {
			return null;
		} else if (direction.equals(CSConnectionRouter.STILL)) {
			return null;
		} else if (this.csMatrix[x][y] < dist) {
			idealPoints.addPoint(CSConnectionRouter.RESERVED_POINT);
		} else {
			this.csMatrix[x][y] = dist;
			PointList xList = this.calcXlists(direction, x, y);
			PointList yList = this.calcYlists(direction, x, y);
			PointList yTemp = null;
			PointList xTemp = null;

			// if the algorithm cannot move in y direction, the x-direction is
			// forced
			changeY = this.signOf(end.y - y, yForced, direction.y);
			xForced = (this.csMatrix[x][y + changeY] == CSConnectionRouter.SPACE_RESERVED);

			// if the algorithm cannot move in x direction, the y-direction is
			// forced
			changeX = this.signOf(end.x - x, xForced, direction.x);
			yForced = (this.csMatrix[x + changeX][y] == CSConnectionRouter.SPACE_RESERVED);

			xTemp = this.idealPath(xList, x + changeX, y, dist,
					this.xChange(changeX, direction.x), end, yForced, xForced);

			yTemp = this.idealPath(yList, x, y + changeY, dist,
					this.yChange(changeY, direction.y), end, yForced, xForced);
			if (this.foundEnd(yTemp, end)) {
				idealPoints.addAll(yTemp);
				return idealPoints;
			}
			if (this.isRedundant(yTemp, xTemp)) {
				return null;
			}

			// if the algorithm does not find a path in x- or y-direction
			// xTemp is calculated again with forced attributes
			if (this.isNull(yTemp, xTemp)) {
				changeX = this.absMax(direction.x);
				xTemp = this.idealPath(xList, x + changeX, y, dist,
						this.xChange(changeX, direction.x), end, false, true);

				// if xTemp is still null yTemp is recalculated
				if (xTemp == null) {
					changeY = this.absMax(direction.y);
					yTemp = this.idealPath(xList, x, y + changeY, dist,
							this.yChange(changeY, direction.y), end, true,
							false);
				}

			}
			if (!this.isNull(yTemp, xTemp)) {
				idealPoints.addAll(this.choose(xTemp, yTemp));
			}

		}

		return idealPoints;
	}

	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param a
	 *            a path
	 * @param b
	 *            an other path
	 * @return if both paths are null
	 */
	private boolean isNull(PointList a, PointList b) {
		if ((a == null) && (b == null)) {
			return true;
		}

		return false;
	}

	/**
	 * The list which leads in the opposite direction gets the current Point as
	 * a member
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	private PointList calcXlists(Point currentDir, int x, int y) {
		PointList xList = new PointList();
		if (currentDir.equals(CSConnectionRouter.UP)
				|| currentDir.equals(CSConnectionRouter.DOWN)) {
			xList.addPoint(new Point(x, y));
		}
		return xList;
	}

	/**
	 * The list which leads in the opposite direction gets the current Point as
	 * a member
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	private PointList calcYlists(Point currentDir, int x, int y) {
		PointList xList = new PointList();
		if (currentDir.equals(CSConnectionRouter.LEFT)
				|| currentDir.equals(CSConnectionRouter.RIGHT)) {
			xList.addPoint(new Point(x, y));
		}
		return xList;
	}

	/**
	 * this normalizes a given value and returns the normal value
	 */
	private int absMax(int direct) {
		if (direct == 0) {
			return 1;
		}
		return direct;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * @return the smaller list, or null if the lists are empty
	 */
	private PointList choose(PointList a, PointList b) {
		if ((a == null) && (b == null)) {
			return null;
		} else if ((a == null) && (b != null)) {
			return b;
		} else if ((a != null) && (b == null)) {
			return a;
		} else if ((a != null) && (a.size() >= b.size())) {
			return b;
		}
		return a;
	}

	private boolean foundEnd(PointList list, Point end) {
		if ((list != null) && list.getLastPoint().equals(end)) {
			return true;
		}
		return false;
	}

	private boolean isRedundant(PointList a, PointList b) {
		boolean bRedundant = false;
		boolean aRedundant = false;
		if ((a != null)
				&& a.getLastPoint().equals(CSConnectionRouter.RESERVED_POINT)) {
			aRedundant = true;
		}
		if ((b != null)
				&& b.getLastPoint().equals(CSConnectionRouter.RESERVED_POINT)) {
			bRedundant = true;

		}
		if ((a == null) && bRedundant) {
			return true;
		}
		if ((b == null) && aRedundant) {
			return true;
		}
		return aRedundant && bRedundant;
	}

	/**
	 * calculates the leading sign of delta or returns one if it's forced
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param delta
	 *            the int the leading sign is needed
	 * @param force
	 *            if the function should return 1
	 * @param movement
	 *            says if the algorithm moves in positive or negative direction
	 * @return the sign of delta or, if forced, the offset
	 */
	private int signOf(int delta, boolean force, int movement) {

		if (force) {
			return movement;
		}
		if (delta < 0) {
			return -1;
		}
		if (delta > 0) {
			return 1;
		}

		return 0;
	}

	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param dist
	 * @param direction
	 * @return
	 */
	private Point xChange(int move, int direction) {
		if (move == -direction) {
			return CSConnectionRouter.STILL;
		} else if (move < 0) {
			return CSConnectionRouter.LEFT;
		} else if (move > 0) {
			return CSConnectionRouter.RIGHT;
		} else {
			return CSConnectionRouter.STILL;
		}
	}

	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param dist
	 * @param direction
	 * @return
	 */
	private Point yChange(int move, int direction) {
		if (move < 0) {
			return CSConnectionRouter.UP;
		} else if (move > 0) {
			return CSConnectionRouter.DOWN;
		} else if (move == -direction) {
			return CSConnectionRouter.STILL;
		} else {
			return CSConnectionRouter.STILL;
		}
	}

	/**
	 * 
	 * creates a matrix containing:<br/>
	 * if(child stored at this position)<br/>
	 * --SPACE_RESERVED<br/>
	 * else<br/>
	 * --NOT_VISITED
	 * 
	 * @return whether the matrix could be created or not
	 * @author Lukas Balzer
	 * 
	 * @see SPACE_RESERVED
	 * @see NOT_VISITED
	 */
	private boolean fillMatrix() {

		int xThreshold = this.owner.getFigure().getBounds().width + 1;

		int yThreshold = this.owner.getFigure().getBounds().height + 1;
		int[][] childMap = this.raster(xThreshold, yThreshold);

		Iterator<IRectangleComponent> childIterator = this.owner
				.getModelChildren().iterator();
		int x;
		int y;
		while (childIterator.hasNext()) {
			Rectangle childRect = childIterator.next().getLayout(
					this.owner.getViewer()
							.getProperty(CSAbstractEditor.STEP_EDITOR)
							.toString().equals(CSEditor.ID));

			// iterates over all points of the child and sets them to 0
			// in the childMap
			for (int xOffset = -CSConnectionRouter.INTERVALL; xOffset < (childRect.width + CSConnectionRouter.INTERVALL); xOffset++) {
				x = xOffset + childRect.x;
				if ((x < 0) || (x > xThreshold)) {
					x = 0;
				}
				for (int yOffset = -CSConnectionRouter.INTERVALL; yOffset < (childRect.height + CSConnectionRouter.INTERVALL); yOffset++) {
					y = yOffset + childRect.y;
					if ((y < 0) || (y > yThreshold)) {
						y = 0;
					}
					try {
						childMap[x][y] = CSConnectionRouter.SPACE_RESERVED;
					} catch (IndexOutOfBoundsException e) {
						return false;
					}

				}
			}

		}

		this.createCsMatrix(childMap);
		return true;
	}

	private int[][] raster(int xThreshold, int yThreshold) {
		int[][] childMap = new int[xThreshold][yThreshold];
		for (int x = 0; x < xThreshold; x++) {
			for (int y = 0; y < yThreshold; y++) {
				if ((x == 0) || (y == 0)) {
					childMap[x][y] = CSConnectionRouter.SPACE_RESERVED;
				} else if ((x == (xThreshold - 1)) || (y == (yThreshold - 1))) {
					childMap[x][y] = CSConnectionRouter.SPACE_RESERVED;
				} else {
					childMap[x][y] = CSConnectionRouter.NOT_VISITED;
				}
			}
		}
		return childMap;
	}

	private boolean insectOwner(Connection conn) {
		Iterator<IRectangleComponent> childIterator = this.owner
				.getModelChildren().iterator();
		Rectangle childRect;
		while (childIterator.hasNext()) {

			childRect = childIterator.next().getLayout(true).getCopy();

			childRect.shrink(CSConnectionRouter.INTERSECTION_THRESHOLD);
			if (conn.getPoints().intersects(childRect)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param newMatrix
	 *            a 2D array of integers which stores all pixels Whether there
	 *            is an component or not
	 */
	public void createCsMatrix(int[][] newMatrix) {
		this.csMatrix = new int[newMatrix.length][newMatrix[0].length];
		this.csMatrix = newMatrix.clone();
	}

}
