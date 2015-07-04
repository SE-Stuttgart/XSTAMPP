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

package xstampp.astpa.ui.common.grid;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * The base class representing a cell in the grid.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 */
public abstract class AbstractGridCell implements IGridCell {

	/**
	 * The default height in pixel of a cell.
	 */
	public static final int DEFAULT_CELL_HEIGHT = 20;

	private static final int PARENT_RED = 215;
	private static final int PARENT_GREEN = 240;
	private static final int PARENT_BLUE = 255;

	private static final int HOVER_RED = 205;
	private static final int HOVER_GREEN = 245;
	private static final int HOVER_BLUE = 205;

	private static final int MOD_2_0_GRAY = 245;
	private static final int MOD_2_1_GRAY = 230;

	private GridRow row = null;

	private CellButtonContainer buttonContainer = null;

	private boolean showSelection;

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 */
	public AbstractGridCell() {
		this.row = null;
		this.showSelection(true);
		this.buttonContainer = new CellButtonContainer();
	}

	/**
	 * Get the button container.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the button container.
	 */
	public CellButtonContainer getButtonContainer() {
		return this.buttonContainer;
	}

	/**
	 * Get the background color of the cell.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param renderer
	 *            the renderer.
	 * @param gc
	 *            the GC.
	 * @return the background color.
	 */
	public Color getBackgroundColor(GridCellRenderer renderer, GC gc) {

		if (renderer.getGridWrapper().isCellSelected(this) && this.showSelection) {
			// selected color constant
			return new Color(gc.getDevice(), AbstractGridCell.HOVER_RED,
					AbstractGridCell.HOVER_GREEN, AbstractGridCell.HOVER_BLUE);
		}

		if ((this.row != null) && (this.row.getParentRow() == null)) {
			// parent color
			return new Color(gc.getDevice(), AbstractGridCell.PARENT_RED,
					AbstractGridCell.PARENT_GREEN, AbstractGridCell.PARENT_BLUE);
		}

		// alternating color
		// find out the index of the cell
		int index = 0;
		for (int i = 0; i < this.row.getParentRow().getChildren().size(); i++) {
			if (this.row.getParentRow().getChildren().get(i).equals(this.row)) {
				index = i;
				break;
			}
		}

		if ((index % 2) == 0) {
			return new Color(gc.getDevice(), AbstractGridCell.MOD_2_0_GRAY,
					AbstractGridCell.MOD_2_0_GRAY,
					AbstractGridCell.MOD_2_0_GRAY);
		}
		// every second row
		return new Color(gc.getDevice(), AbstractGridCell.MOD_2_1_GRAY,
				AbstractGridCell.MOD_2_1_GRAY, AbstractGridCell.MOD_2_1_GRAY);
	}

	@Override
	public void paint(GridCellRenderer renderer, GC gc,
			NebulaGridRowWrapper item) {

		this.paintFrame(renderer, gc, item);
	}

	/**
	 * Paint the frame.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param renderer
	 *            the renderer.
	 * @param gc
	 *            the GC.
	 * @param item
	 *            the nebula grid item the cell belongs to.
	 */
	public void paintFrame(GridCellRenderer renderer, GC gc,
			@SuppressWarnings("unused") NebulaGridRowWrapper item) {
		Color bgColor = gc.getBackground();

		gc.setBackground(this.getBackgroundColor(renderer, gc));

		gc.fillRectangle(renderer.getDrawBounds().x,
				renderer.getDrawBounds().y, renderer.getDrawBounds().width,
				renderer.getDrawBounds().height);

		this.buttonContainer.paintButtons(renderer, gc);

		// restore bg color
		gc.setBackground(bgColor);
	}

	@Override
	public void onMouseDown(MouseEvent e, Point relativeMouse,
			Rectangle cellBounds) {
		this.buttonContainer.onMouseDown(relativeMouse, cellBounds);
	}

	@Override
	public void onMouseUp(MouseEvent e) {
		// intentionally empty
	}

	@Override
	public void onFocusGained() {
		// intentionally empty
	}

	@Override
	public void onFocusLost() {
		// intentionally empty
	}

	@Override
	public void setGridRow(GridRow gridRow) {
		this.row = gridRow;
	}
	
	public void showSelection(boolean show) {
		this.showSelection= show;
	}
}
