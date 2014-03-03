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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * The interface of a cell in the GridWrapper.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 * 
 */
public interface IGridCell {
	
	/**
	 * Paint function of the cell.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 * @param renderer the renderer.
	 * @param gc the gc.
	 * @param item the nebula grid row this cell belongs to.
	 * 
	 */
	void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item);
	
	/**
	 * Gets called whenever a mouse button pressed while the cell is hovered.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 * @param e the mouse event.
	 * @param relativeMouse the mouse position relative to the top left corner
	 *            of the cell.
	 * @param cellBounds the boundaries of the cell.
	 * 
	 */
	void onMouseDown(MouseEvent e, Point relativeMouse, Rectangle cellBounds);
	
	/**
	 * Gets called whenever a mouse button releases while the cell is hovered.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 * @param e the mouse event.
	 */
	void onMouseUp(MouseEvent e);
	
	/**
	 * Called whenever the cell gains focus.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	void onFocusGained();
	
	/**
	 * Called whenever the cell loses focus.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	void onFocusLost();
	
	/**
	 * Set the row in the grid, the cell is in.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 * @param gridRow the row in which the cell is.
	 */
	void setGridRow(GridRow gridRow);
	
	/**
	 * Clean up function gets called when the cell gets removed.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 */
	void cleanUp();
	
	/**
	 * Get the preferred height of the cell.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the preferred height of the cell.
	 * 
	 */
	int getPreferredHeight();
	
	/**
	 * Add a button inside the cell.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param button the new button.
	 * 
	 */
	void addCellButton(CellButton button);
	
	/**
	 * Remove all cell buttons.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	void clearCellButtons();
	
	/**
	 * Returns the UUID if the cell represents a Unsafe Control Action or Causal
	 * Factor
	 * 
	 * @author Benedikt Markt
	 * 
	 * @return the uuid of the element, if applicable
	 */
	UUID getUUID();
	
	/**
	 * Activates a cells contents. i.e. the text editor of a GridCellEditor
	 * 
	 * @author Benedikt Markt
	 * 
	 */
	void activate();
}
