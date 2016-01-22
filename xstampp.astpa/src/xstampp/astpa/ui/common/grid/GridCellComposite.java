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

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * A cell that can be used like a composite.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridCellComposite extends Composite implements IGridCell {

	private GridRow row;

	private CompositeMouseListener mouseListener = null;

	private static final RGB SELECTED_COLOR = new RGB(205, 240, 205);
	private static final RGB PARENT_COLOR = new RGB(215, 240, 255);
	private static final RGB EVEN_COLOR = new RGB(245, 245, 245);
	private static final RGB ODD_COLOR = new RGB(230, 230, 230);

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param gridWrapper
	 *            the grid.
	 * @param style
	 *            SWT style constant of the composite.
	 */
	public GridCellComposite(GridWrapper gridWrapper, int style) {
		super(gridWrapper.getGrid(), style|SWT.DOUBLE_BUFFERED);
		this.init();
	}

	private void init() {
		this.mouseListener = new CompositeMouseListener(this);
		this.addMouseListener(this.mouseListener);
	}

	/**
	 * Redirects the composites mouse events to the cells.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	private class CompositeMouseListener implements MouseListener {

		private GridCellComposite owner;

		public CompositeMouseListener(GridCellComposite owner) {
			this.owner = owner;
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			// intentionally empty
		}

		@Override
		public void mouseDown(MouseEvent e) {
			// relative mouse position not known here
			this.owner.onMouseDown(e, null, null);
		}

		@Override
		public void mouseUp(MouseEvent e) {
			this.owner.onMouseUp(e);
		}
	}

	/**
	 * Get the background color of the cell.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param renderer
	 *            the renderer.
	 * 
	 * @param gc
	 *            the GC.
	 * 
	 * @return the background color of the cell.
	 */
	public Color getBackgroundColor(GridCellRenderer renderer, GC gc) {

		if (renderer.getGridWrapper().isCellSelected(this)) {
			// selected color constant
			return new Color(gc.getDevice(), GridCellComposite.SELECTED_COLOR);
		}

		if (this.row != null) {
			if (this.row.getParentRow() == null) {
				// parent color
				return new Color(gc.getDevice(), GridCellComposite.PARENT_COLOR);
			}
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
			return new Color(gc.getDevice(), GridCellComposite.EVEN_COLOR);
		}
		// every second row
		return new Color(gc.getDevice(), GridCellComposite.ODD_COLOR);
	}

	@Override
	public void paint(GridCellRenderer renderer, GC gc,
			NebulaGridRowWrapper item) {
		this.setBounds(renderer.getDrawBounds());
	}

	@Override
	public void onMouseDown(MouseEvent e,
			org.eclipse.swt.graphics.Point relativeMouse, Rectangle cellBounds) {
		// intentionally empty
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

	@Override
	public void cleanUp() {
		this.dispose();
	}

	@Override
	public int getPreferredHeight() {
		return AbstractGridCell.DEFAULT_CELL_HEIGHT;
	}

	@Override
	public void addCellButton(CellButton button) {
		// Buttons can't be added to cells containing composites
	}

	@Override
	public void clearCellButtons() {
		// Buttons can't be added to cells containing composites
	}

	@Override
	public UUID getUUID() {
		return null;
	}

	@Override
	public void activate() {
		// intentionally empty

	}
	
	@Override
	public boolean needsRefresh() {
		// TODO Auto-generated method stub
		return false;
	}
}
