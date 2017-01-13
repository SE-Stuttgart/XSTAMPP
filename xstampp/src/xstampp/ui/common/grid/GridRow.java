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
import java.util.Arrays;
import java.util.Collections;
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
  
  private int[] rowSpanningCells;
  
  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser, Lukas Balzer
   * @param colorDivide
   *          The amount of rows that should be colored in the same color to
   *          mark multiple rows that are related
   * @param columnCount
   *          the total amount of columns in the parent gird
   * @param rowSpanningCells
   *          the absolute indices of the cells that should span the child rows of this row
   * 
   */
  public GridRow(int columnCount, int colorDivide, int[] rowSpanningCells) {
    setColorDivider(colorDivide);
    this.childrenRows = new ArrayList<GridRow>();
    this.cells = new ArrayList<IGridCell>(columnCount);
    for ( int i = 0; i < columnCount; i++) {
      IGridCell cell = new GridCellBlank(false);
      cell.setGridRow(this);
      cells.add(cell);
    }
    this.parentRow = null;
    this.needsRefresh = false;
    this.rowSpanningCells = rowSpanningCells;
  }

  /**
   * constructs a row for the grid it is inserted in
   *  {@link #getRowSpanningCells()} is only column 0.
   * @param colorDivide
   *          The amount of rows that should be colored in the same color to
   *          mark multiple rows that are related
   * @param columnCount
   *          the total amount of columns in the parent gird
   */
  public GridRow(int columnCount,int colorDivide) {
    this(columnCount,colorDivide, new int[]{0});
  }
  
  /**
   * constructs a row that has a color divider {@link #getColorDivider()} of 1
   * and a {@link #getRowSpanningCells()} is only column 0.
   * @param columnCount
   *          the total amount of columns in the parent gird
   */
  public GridRow(int columnCount) {
    this(columnCount,1);
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
    
//    if ( childrenRows.size() > 0 ) {
//      Assert.isTrue(row.getCells().size() == childrenRows.get(0).getCells().size());
//    }
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

  public void addCell(int columnIndex, IGridCell cell) {
    this.cells.set(columnIndex,cell);
    cell.setGridRow(this);
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

  /**
   * gets all indices relative to the parent grid
   * of the cells that should span all child rows
   * of this row.
   * The default value is one.
   * 
   * @return the indices of the cells to span the child rows of the row
   */
  public int[] getRowSpanningCells() {
    return rowSpanningCells;
  }

  /**
   * set the row indices which should span the 
   * child rows if this row has some.
   * 
   * @param spanningCells the indices of the cells to span the child rows of the row
   */
  public void setRowSpanningCells(int[] spanningCells) {
    this.rowSpanningCells = spanningCells;
  }
}