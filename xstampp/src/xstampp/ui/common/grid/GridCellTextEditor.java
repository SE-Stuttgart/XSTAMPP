/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.ui.common.grid;

import java.util.UUID;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import messages.Messages;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;
import xstampp.util.ColorManager;
import xstampp.util.DirectEditor;

/**
 * A cell that contains an SWT text editor.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 * 
 */
public abstract class GridCellTextEditor extends AbstractGridCell {

  private Rectangle editField;

  private GridWrapper grid = null;
  private String currentText = ""; //$NON-NLS-1$
  private Color bgColor;
  private DirectEditor editor;
  private boolean showDelete;
  private boolean isReadOnly;
  private UUID entryId;
  private GridTextEditorProvider editorProvider;
  private String defaultText;

  /**
   * creates a Text editor which is <b>editable</b> and <b>doesn't contain a delete button</b>. When
   * created the editor is empty
   * 
   * @param grid
   *          The grid canvas, this is needed to show the direct editor
   * 
   * @author Patrick Wickenhaeuser, Lukas Balzer
   */
  public GridCellTextEditor(GridWrapper grid) {
    this(grid, new String(), false, false, null);
  }

  /**
   * creates a Text editor which is <b>editable</b> and <b>doesn't contain a delete button</b>. the
   * editor further contains the initial text and is displayed in the given grid
   * 
   * @param grid
   *          The grid canvas, this is needed to show the direct editor
   * @param initialText
   *          the intitial text in the editor.
   * @param entryId
   *          the id of the entry which is represented and edited in this editor
   * @author Patrick Wickenhaeuser, Lukas Balzer
   */
  public GridCellTextEditor(GridWrapper grid, String initialText, UUID entryId) {
    this(grid, initialText, false, false, entryId);
  }

  /**
   * creates a Text editor which contains the initial text and is displayed in the given grid
   * 
   * @author Patrick Wickenhaeuser, Lukas Balzer
   * @param grid
   *          The grid canvas, this is needed to show the direct editor
   * @param initialText
   *          the intitial text in the editor.
   * @param showDelete
   *          if the delete button should be drawn in this component
   * @param readOnly
   *          whether the editor should be read only
   * @param entryId
   *          the id of the entry which is represented and edited in this editor
   * 
   */
  public GridCellTextEditor(GridWrapper grid, String initialText, Boolean showDelete,
      Boolean readOnly, UUID entryId) {
    editorProvider = new GridTextEditorProvider();
    this.defaultText = Messages.ClickToEdit;
    this.showDelete = showDelete;
    clearCellButtons();
    this.isReadOnly = readOnly;
    this.entryId = entryId;
    this.grid = grid;

    if (initialText == null || initialText.trim().isEmpty()) {
      this.currentText = new String();
    } else {
      this.currentText = initialText;
    }

    this.editField = new Rectangle(0, 0, -1, 2 * DEFAULT_CELL_HEIGHT);
    grid.getGrid().addMouseWheelListener(new MouseWheelListener() {

      @Override
      public void mouseScrolled(MouseEvent e) {
        if (GridCellTextEditor.this.editor != null
            && !GridCellTextEditor.this.editor.getControl().isDisposed()) {
          GridCellTextEditor.this.editor.getControl().dispose();
        }
      }
    });

  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    super.paint(renderer, gc, item);
    this.bgColor = gc.getBackground();
    Rectangle bounds = renderer.getDrawBounds();
    gc.setBackground(this.getBackgroundColor(renderer, gc));
    Color fgColor = gc.getForeground();
    gc.setForeground(ColorConstants.black);
    int buttonCollum = 0;
    // calculate the avaiable space and performe a wrap
    if (this.showDelete) {
      buttonCollum = 16;
    }
    Point textBounds = new Point(0, 0);
    currentText = getCurrentText();
    if ((this.currentText == null || this.currentText.trim().isEmpty()) && !isReadOnly()) {
      gc.setForeground(ColorManager.COLOR_grey);
      textBounds = wrapText(bounds, gc, defaultText, 2, buttonCollum, item);
    } else if (this.currentText != null) {
      textBounds = wrapText(bounds, gc, this.currentText, 2, buttonCollum, item);
    }
    if (isReadOnly) {
      textBounds.y = Math.max(textBounds.y, AbstractGridCell.DEFAULT_CELL_HEIGHT);
    }
    setPreferredHeight(item, textBounds.y);
    this.editField = new Rectangle(bounds.x, bounds.y, textBounds.x, textBounds.y);
    if (bounds.height + 2 < this.editField.height || bounds.height - 2 > this.editField.height) {
      this.grid.resizeRows();
    }
    // restore bg color
    gc.setBackground(this.bgColor);
    // restore fg color
    gc.setForeground(fgColor);
  }

  public String getCurrentText() {
    return currentText;
  }

  @Override
  public void onMouseDown(MouseEvent error, Point relativeMouse, Rectangle cellBounds) {
    if (!getButtonContainer().onMouseDown(relativeMouse, cellBounds) && !isReadOnly) {
      Point point = grid.getGrid().toDisplay(editField.x, editField.y);
      point = Display.getDefault().map(grid.getGrid(), null, editField.x, editField.y);
      Rectangle rectangle = new Rectangle(point.x, point.y, editField.width, cellBounds.height);
      String string = editorProvider.open(currentText, rectangle);
      if (string != null) {
        currentText = string.trim();
        grid.setUpdateLock();
        updateDataModel(currentText);
        grid.getGrid().redraw();
      }
    }
  }

  @Override
  public Color getBackgroundColor(GridCellRenderer renderer, GC gc) {
    Color color = super.getBackgroundColor(renderer, gc);
    this.bgColor = color;
    if (this.editor != null && !this.editor.getControl().isDisposed()) {
      this.editor.getControl().setBackground(color);
    }
    return color;
  }

  @Override
  public void activate() {
  }

  /**
   * This method does nothing by default it is called when the editor is activated and can be used
   * to execute actions to prepare the dataModel.
   */
  protected abstract void editorOpening();

  /**
   * This method does nothing by default it is called when the editor is disposed and can be used to
   * execute actions to prepare the dataModel.
   */
  protected abstract void editorClosing();

  @Override
  public void onFocusLost() {
    super.onFocusLost();
  }

  @Override
  public void cleanUp() {
    if (this.editor != null && !this.editor.getControl().isDisposed()) {
      this.editor.getControl().dispose();
    }

  }

  @Override
  public void clearCellButtons() {
    super.clearCellButtons();
    if (this.showDelete) {
      CellButton button = new CellButton(GridWrapper.getDeleteButton16(), () -> delete());
      button.setToolTip(Messages.GridCellTextEditor_DeleteButtonToolTip);
      addCellButton(button);
    }
  }

  @Override
  public UUID getUUID() {
    return entryId;
  }

  public void onTextChange(String newValue) {
    // do nothing by default
  }

  /**
   * The abstract method which is linked with the delete button note that if showDelete is set to
   * false, this function is never called.
   *
   * @author Lukas Balzer
   *
   */
  public abstract void delete();

  /**
   * This method is called when ever a change is made to the displayed data.
   * 
   * @author Lukas Balzer
   *
   * @param newValue
   *          the changed text
   */
  public abstract void updateDataModel(String newValue);

  /**
   * @return the showDelete
   */
  public boolean isShowDelete() {
    return showDelete;
  }

  /**
   * @param showDelete
   *          the showDelete to set
   */
  public void setShowDelete(boolean showDelete) {
    this.showDelete = showDelete;
    clearCellButtons();
  }

  /**
   * @return the isReadOnly
   */
  public boolean isReadOnly() {
    return isReadOnly;
  }

  @Override
  public String getToolTip(Point point) {
    String toolTip = super.getToolTip(point);
    if (toolTip == null && !isReadOnly) {
      toolTip = Messages.ClickToEdit;
    }
    return toolTip;
  }

  /**
   * @param isReadOnly
   *          the isReadOnly to set
   */
  public void setReadOnly(boolean isReadOnly) {
    this.isReadOnly = isReadOnly;
  }

  public GridWrapper getGridWrapper() {
    return grid;
  }

  public void setDefaultText(String defaultText) {
    this.defaultText = defaultText;
  }
}