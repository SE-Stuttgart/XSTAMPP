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

/**
 * A row in the GridWrapper.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridRow {
	
	private List<GridRow> childrenRows;
	
	private List<IGridCell> cells;
	
	private GridRow parentRow = null;
	
	
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public GridRow() {
		this.childrenRows = new ArrayList<GridRow>();
		this.cells = new ArrayList<IGridCell>();
		this.parentRow = null;
	}
	
	/**
	 * Get the list of cells.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the list of cells
	 */
	public List<IGridCell> getCells() {
		return this.cells;
	}
	
	/**
	 * Add a child row to the row.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param row the child row.
	 */
	public void addChildRow(GridRow row) {
		this.childrenRows.add(row);
		row.setParentRow(this);
	}
	
	/**
	 * Set the parent row.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param parentRow the parent.
	 */
	private void setParentRow(GridRow parentRow) {
		this.parentRow = parentRow;
	}
	
	/**
	 * Get the children rows.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the children rows.
	 */
	public List<GridRow> getChildren() {
		return this.childrenRows;
	}
	
	/**
	 * Add a cell to the row.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param gridCell the new cell.
	 */
	public void addCell(IGridCell gridCell) {
		this.cells.add(gridCell);
		gridCell.setGridRow(this);
	}
	
	/**
	 * Get the parent row.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the parent row.
	 */
	public GridRow getParentRow() {
		return this.parentRow;
	}
	
	/**
	 * Clean up.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public void cleanUp() {
		for (int i = 0; i < this.getCells().size(); i++) {
			this.getCells().get(i).cleanUp();
		}
		
		for (int i = 0; i < this.getChildren().size(); i++) {
			this.getChildren().get(i).cleanUp();
		}
	}
	
	/**
	 * Get the preferred height of the cell.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 * @return the preferred height.
	 */
	public int getPreferredHeight() {
		int height = 0;
		for (int cellI = 0; cellI < this.cells.size(); cellI++) {
			
			int cellHeight = this.cells.get(cellI).getPreferredHeight();
			height = Math.max(height, cellHeight);
		}
		return height;
	}
}