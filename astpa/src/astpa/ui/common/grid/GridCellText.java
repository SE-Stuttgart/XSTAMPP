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

package astpa.ui.common.grid;

import java.util.UUID;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * A cell to display text.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridCellText extends AbstractGridCell {
	
	private String text;
	
	private static final Color TEXT_COLOR = new Color(Display.getCurrent(), 0, 0, 0);
	
	
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public GridCellText() {
		this.text= "NONE"; //$NON-NLS-1$
	}
	
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 * @param text the intial text in the cell.
	 * 
	 */
	public GridCellText(String text) {
		this.text=text;
	}
	
	@Override
	public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
		super.paint(renderer, gc, item);
		Color bgColor = gc.getBackground();
		
		Rectangle bounds = renderer.getDrawBounds();
		
		gc.setBackground(this.getBackgroundColor(renderer, gc));
		
		Color fgColor = gc.getForeground();
		gc.setForeground(TEXT_COLOR);
		gc.drawString(this.text, bounds.x + 2, bounds.y);
		
		// restore bg color
		gc.setBackground(bgColor);
		// restore fg color
		gc.setForeground(fgColor);
	}
	
	protected void setText(String text) {
		this.text= text;
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
