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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;

import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * A colored cell.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridCellColored extends AbstractGridCell {

	private Color backgroundColor;

	private static final RGB SELECTED_COLOR = new RGB(205, 240, 205);

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param grid
	 *            the swt device.
	 * @param r
	 *            red color value from 0 to 255 of the background.
	 * @param g
	 *            green color value from 0 to 255 of the background.
	 * @param b
	 *            blue color value from 0 to 255 of the background.
	 * 
	 */
	public GridCellColored(GridWrapper grid, int r, int g, int b) {

		this.backgroundColor = new Color(grid.getGrid().getDisplay(), r, g, b);
	}

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param grid
	 *            the swt device.
	 * @param color
	 *            the color of the background.
	 * 
	 */
	public GridCellColored(GridWrapper grid, RGB color) {

		this.backgroundColor = new Color(grid.getGrid().getDisplay(),
				color.red, color.green, color.blue);
	}

	/**
	 * Set the new background color
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param bgColor
	 *            the new color.
	 */
	public void setBackgroundColor(Color bgColor) {
		this.backgroundColor = bgColor;
	}

	@Override
	public void paintFrame(GridCellRenderer renderer, GC gc,
			NebulaGridRowWrapper item) {
		Color oldBgColor = gc.getBackground();

		if (renderer.getGridWrapper().isCellSelected(this)) {
			// selected color constant
			gc.setBackground(new Color(gc.getDevice(),
					GridCellColored.SELECTED_COLOR));
		} else {
			gc.setBackground(this.backgroundColor);
		}

		gc.fillRectangle(renderer.getDrawBounds().x,
				renderer.getDrawBounds().y, renderer.getDrawBounds().width,
				renderer.getDrawBounds().height);

		// restore bg color
		gc.setBackground(oldBgColor);
	}

	@Override
	public void cleanUp() {
		// intentionally empty
	}

	@Override
	public int getPreferredHeight() {
		return AbstractGridCell.DEFAULT_CELL_HEIGHT;
	}

	@Override
	public void addCellButton(CellButton button) {
		this.getButtonContainer().addCellButton(button);
	}

	@Override
	public void clearCellButtons() {
		this.getButtonContainer().clearButtons();
	}

	@Override
	public UUID getUUID() {
		return null;
	}

	@Override
	public void activate() {
		// intentionally empty

	}
}
