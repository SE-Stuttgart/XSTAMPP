/*******************************************************************************
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.ui.common.grid;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;

/**
 * A row in the GridWrapper.
 * 
 * @author Patrick Wickenhaeuser, Lukas Balzer
 * 
 */
public class GridRow {

  private List<GridRow> childrenRows;

  private List<IGridCell> cells;

  private GridRow parentRow = null;

  private int colorDivider;

  private boolean needsRefresh;

  private int rowIndex;
  
  private Point columnSpan;
  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser
   * @param colorDivide
   *          The amount of rows that should be colored in the same color to
   *          mark multiple rows that are related
   * 
   */
  public GridRow(int colorDivide) {
    setColorDivider(colorDivide);
    this.childrenRows = new ArrayList<GridRow>();
    this.cells = new ArrayList<IGridCell>();
    this.parentRow = null;
    this.needsRefresh = false;
  }

  public void setDirty(boolean dirty) {
    if (this.parentRow != null) {
      this.parentRow.setDirty(dirty);
    } else {
      this.needsRefresh = dirty;
    }
  }

  public boolean needsRefresh() {
    boolean result = needsRefresh;
    needsRefresh = false;
    return result;
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
   * @param row
   *          the child row.
   */
  public void addChildRow(GridRow row) {
    this.childrenRows.add(row);
    row.setColorDivider(colorDivider);
    row.setParentRow(this);
  }

  /**
   * Set the parent row.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param parentRow
   *          the parent.
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
   * @param gridCell
   *          the new cell.
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

  /**
   * @return the colorDivider
   */
  public int getColorDivider() {
    return this.colorDivider;
  }

  public void setColorDivider(int colorDivider) {
    if(colorDivider == 0){
      this.colorDivider = 1;
    }else{
      this.colorDivider = colorDivider;
    }
  }
  public int getRowIndex() {
    return rowIndex;
  }

  public void setRowIndex(int rowIndex) {
    this.rowIndex = rowIndex;
  }

  public void setColumnSpan(int index, int span) {
    this.columnSpan = new Point(index, span);
  }
  
  public Point getColumnSpan() {
    return columnSpan;
  }
}