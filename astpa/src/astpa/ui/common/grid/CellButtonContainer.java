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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * The container for buttons in a GridWrapper cell.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class CellButtonContainer {
	
	private List<ICellButton> buttons;
	
	
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public CellButtonContainer() {
		this.buttons = new ArrayList<ICellButton>();
	}
	
	/**
	 * Paint the buttons.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param renderer the renderer.
	 * @param gc the GC.
	 */
	public void paintButtons(GridCellRenderer renderer, GC gc) {
		for (int i = 0; i < this.buttons.size(); i++) {
			ICellButton button = this.buttons.get(i);
			
			button.onPaint(gc, renderer.getBounds());
		}
	}
	
	/**
	 * Add a button to the cell.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param button the new button.
	 */
	public void addCellButton(ICellButton button) {
		this.buttons.add(button);
	}
	
	/**
	 * Checks if any buttons is hit.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param relativeMouse the mouse position relative to the top left of the
	 *            cell.
	 * @param cellBounds the boundaries of the cell.
	 * 
	 * @return true if a buttons was been hit.
	 */
	public boolean onMouseDown(Point relativeMouse, Rectangle cellBounds) {
		boolean hit = false;
		for (int i = 0; i < this.buttons.size(); i++) {
			ICellButton button = this.buttons.get(i);
			if (button.getBounds().contains(relativeMouse)) {
				button.onButtonDown(relativeMouse, cellBounds);
				hit = true;
			}
		}
		
		return hit;
	}
	
	/**
	 * Clears all cell buttons.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public void clearButtons() {
		this.buttons.clear();
	}
	
	/**
	 * Returns if the button container is empty
	 * 
	 * @author Benedikt Markt
	 * 
	 * @return true if empty, otherwise false
	 */
	public boolean isEmpty() {
		return this.buttons.size() == 0;
	}
}
