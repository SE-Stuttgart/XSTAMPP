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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * The renderer of a cell in the GridWrapper.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 * 
 */
@SuppressWarnings("restriction")
public class GridCellRenderer extends org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer {
	
	private GridWrapper gridWrapper;
	
	private final int borderSize = 2;
	
	
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param grid the GridWrapper.
	 */
	public GridCellRenderer(GridWrapper grid) {
		this.gridWrapper = grid;
		
		this.setWordWrap(true);
	}
	
	@Override
	public void paint(GC gc, Object value) {
		if (value instanceof NebulaGridRowWrapper) {
			NebulaGridRowWrapper item = (NebulaGridRowWrapper) value;
			
			IGridCell cell = item.getCell(this.getColumn());
			
			if (cell != null) {
				
				cell.paint(this, gc, item);
				Color fColor = gc.getForeground();
				gc.setForeground(new Color(Display.getCurrent(), 255, 255, 255));
				gc.drawRectangle(this.getBounds().x, this.getBounds().y, this.getBounds().width,
					this.getBounds().height);
				// restore old foreground color
				gc.setForeground(fColor);
			}
		}
	}
	
	/**
	 * Get the GridWrapper.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the GridWrapper.
	 */
	public GridWrapper getGridWrapper() {
		return this.gridWrapper;
	}
	
	/**
	 * Returns the drawable area of a cell
	 * 
	 * @author Benedikt Markt
	 * 
	 * @return the Dimensions of the drawable area
	 */
	public Rectangle getDrawBounds() {
		Rectangle renderBounds = this.getBounds();
		Rectangle bounds =
			new Rectangle(renderBounds.x + this.borderSize + 1, renderBounds.y + this.borderSize, renderBounds.width
				- this.borderSize, renderBounds.height - this.borderSize);
		return bounds;
	}
}