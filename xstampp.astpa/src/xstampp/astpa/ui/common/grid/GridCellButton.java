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

import messages.Messages;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * Cell that contains a button and text.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridCellButton extends AbstractGridCell {

	private String text;

	private static final Color TEXT_COLOR = new Color(Display.getCurrent(), 0,
			0, 0);

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public GridCellButton() {
		this.text = Messages.GridCellButton_None;
	}

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param text
	 *            set the initial text in the cell.
	 * 
	 */
	public GridCellButton(String text) {

		this.text = text;
	}

	@Override
	public void paint(GridCellRenderer renderer, GC gc,
			NebulaGridRowWrapper item) {
		super.paint(renderer, gc, item);
		Color bgColor = gc.getBackground();
		Color fgColor = gc.getForeground();

		Rectangle bounds = renderer.getDrawBounds();

		gc.setBackground(this.getBackgroundColor(renderer, gc));
		gc.setForeground(GridCellButton.TEXT_COLOR);
		gc.drawString(this.text, bounds.x + 2, bounds.y);

		gc.drawImage(GridWrapper.getAddButton16(),
				(bounds.x + bounds.width) - 22, bounds.y + 1);

		// gc.drawImage(GridWrapper.getAddButton32(), 0, 0, GridWrapper
		// .getAddButton32().getBounds().width, GridWrapper
		// .getAddButton32().getBounds().height,
		// (bounds.x + bounds.width) - 18, bounds.y, 18, 18);

		// restore bg color
		gc.setBackground(bgColor);
		// restore fg color
		gc.setForeground(fgColor);

	}

	@Override
	public void onMouseDown(MouseEvent e, Point relative, Rectangle cellBounds) {
		// intentionally empty
	}

	@Override
	public void onMouseUp(MouseEvent e) {
		// intentionally empty
	}

	protected void setText(String text) {
		this.text = text;
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
