/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import java.util.UUID;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.GestureEvent;
import org.eclipse.swt.events.GestureListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import messages.Messages;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;
import xstampp.util.DirectEditor;

/**
 * A cell that contains an SWT text editor.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 * 
 */
public abstract class GridCellTextEditor extends AbstractGridCell {

  private Rectangle editField;
  /**
   * The default Text.
   * 
   * @author Benedikt Markt
   */
  public static final String EMPTY_CELL_TEXT = Messages.ClickToEdit;

  private GridWrapper grid = null;
  private String currentText = ""; //$NON-NLS-1$
  private Color bgColor;
  private DirectEditor editor;
  private Rectangle deleteSpace;
  private boolean showDelete;
  private boolean isReadOnly;
  private UUID entryId;

  private class TextLocator implements CellEditorLocator {

    private Rectangle bounds;
    public TextLocator(Rectangle bounds) {
      this.bounds = bounds;
    }
    @Override
    public void relocate(CellEditor celleditor) {
      getPreferredHeight();
      Text text = (Text) celleditor.getControl();

      // if the size is determined to be larger than the text lines itself
      // this the original size, will be displayed as long as it not
      // overwritten by text
      int editorHeight = Math.max(bounds.height, text.getLineHeight() * text.getLineCount());
      text.setBounds(bounds.x, bounds.y, bounds.width, editorHeight);
    }

  }
  
  /**
   * creates a Text editor which is <b>editable</b> and <b>doesn't contain a delete button</b>.
   * When created the editor is empty 
   * @param grid
   *          The grid canvas, this is needed to show the direct editor
   * 
   * @author Patrick Wickenhaeuser, Lukas Balzer
   */
  public GridCellTextEditor(GridWrapper grid){
    this(grid, new String(), false, false, null);
  }
  
  /**
   * creates a Text editor which is <b>editable</b> and <b>doesn't contain a delete button</b>.
   * the editor further contains the initial text and is displayed in the given grid
   * @param grid
   *          The grid canvas, this is needed to show the direct editor
   * @param initialText 
   *          the intitial text in the editor.
   * @param entryId
   *          the id of the entry which is represented and edited in this editor
   * @author Patrick Wickenhaeuser, Lukas Balzer
   */
  public GridCellTextEditor(GridWrapper grid,
      String initialText,
      UUID entryId){
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
   * @param readOnly whether the editor should be read only
   * @param entryId
   *          the id of the entry which is represented and edited in this editor
   * 
   */
  public GridCellTextEditor(GridWrapper grid,
                            String initialText,
                            Boolean showDelete,
                            Boolean readOnly,
                            UUID entryId) {
    
    this.showDelete = showDelete;
    this.isReadOnly = readOnly;
    this.entryId = entryId;
    this.deleteSpace = new Rectangle(0, 0, 0, 0);
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
      this.deleteSpace = new Rectangle(bounds.x + bounds.width - 16,
                                bounds.y + bounds.height / 2 - 8, 16, 16);
      buttonCollum = this.deleteSpace.width;
      gc.drawImage(GridWrapper.getDeleteButton16(), this.deleteSpace.x, this.deleteSpace.y);
    }
    int lineHeight;
    if (this.currentText.trim().isEmpty() && !isReadOnly) {
      lineHeight = wrapText(bounds, gc, EMPTY_CELL_TEXT, 2, buttonCollum);
    } else {
      lineHeight = wrapText(bounds, gc, this.currentText, 2, buttonCollum);
    }
    if ( isReadOnly ) {
      lineHeight = Math.max(lineHeight, AbstractGridCell.DEFAULT_CELL_HEIGHT);
    }
    setPreferredHeight(item,lineHeight);
    this.editField = new Rectangle(bounds.x, bounds.y, bounds.width - buttonCollum, lineHeight);
    if (bounds.height + 2 < this.editField.height || bounds.height - 2 > this.editField.height) {
      this.grid.resizeRows();
    }
    // restore bg color
    gc.setBackground(this.bgColor);
    // restore fg color
    gc.setForeground(fgColor);
  }

  @Override
  public void onMouseDown(MouseEvent error, Point relativeMouse, Rectangle cellBounds) {
    if (this.showDelete 
        && GridCellTextEditor.this.deleteSpace.contains(error.x, error.y) && error.button == 1) {
      delete();
    } else if ( !isReadOnly ) {
      if ( editor == null || editor.getControl().isDisposed()) {
        editor = new DirectEditor(this.grid.getGrid(), SWT.WRAP);
        grid.getGrid().getVerticalBar().addListener(SWT.Selection, new Listener() {
          
          @Override
          public void handleEvent(Event event) {
            editor.deactivate();
          }
        });
        grid.getGrid().addMouseWheelListener(new MouseWheelListener() {
          
          @Override
          public void mouseScrolled(MouseEvent error) {
            editor.deactivate();
          }
        });
        editor.getControl().addFocusListener(new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent error) {
            editor.deactivate();
            editorClosing();
          }
          
          @Override
          public void focusGained(FocusEvent error) {
            editorOpening();
          }
        });
        editor.addModifyListener(new ModifyListener() {

          @Override
          public void modifyText(ModifyEvent error) {
            if (error.getSource() instanceof Text 
                && !currentText.equals(((Text) error.widget).getText())) {
              GridCellTextEditor.this.currentText = ((Text) error.widget).getText();
              Rectangle rect = GridCellTextEditor.this.editField;
              Text text = (Text) error.getSource();
              updateDataModel(currentText);
              onTextChange(GridCellTextEditor.this.currentText);
              // if the size is determined to be larger than the text lines itself
              // this the original size, will be displayed as long as it not
              // overwritten by text
              if(text.isDisposed()){
                editor.deactivate();
              }else{
                int editorHeight = text.getLineHeight() * text.getLineCount();
                text.setBounds(rect.x, rect.y, rect.width, editorHeight);
              }
              grid.getGrid().redraw();
            }
          }
        });
      }
      
      editor.activate(new TextLocator(editField));
      editor.setTextColor(ColorConstants.black);
      editor.getControl().setBackground(HOVER_COLOR);
      editor.setTextFont(Display.getDefault().getSystemFont());
      editor.setValue(this.currentText);
      editor.setFocus();
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
   * This method does nothing by default it is called
   * when the editor is activated and can be used to execute actions 
   * to prepare the dataModel.
   */
  protected abstract void editorOpening();

  /**
   * This method does nothing by default it is called
   * when the editor is disposed and can be used to execute actions 
   * to prepare the dataModel.
   */
  protected abstract void editorClosing();
  
  @Override
  public void onFocusLost() {
    // TODO Auto-generated method stub
    super.onFocusLost();
  }
  @Override
  public void cleanUp() {
    if (this.editor != null && !this.editor.getControl().isDisposed()) {
      this.editor.getControl().dispose();
    }

  }

  @Override
  public UUID getUUID() {
    return entryId;
  }
  
  public void onTextChange(String newValue) {
    // do nothing by default
  }

  
  @Override
  public void addCellButton(CellButton button) {
    // the delete Button is aded manually to allow a better handling of the
    // delete process
  }

  @Override
  public void clearCellButtons() {
    // not needed either

  }

  /**
   * The abstract method which is linked with the delete button note that if
   * showDelete is set to false, this function is never called.
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
   * @param showDelete the showDelete to set
   */
  public void setShowDelete(boolean showDelete) {
    this.showDelete = showDelete;
  }

  /**
   * @return the isReadOnly
   */
  public boolean isReadOnly() {
    return isReadOnly;
  }

  /**
   * @param isReadOnly the isReadOnly to set
   */
  public void setReadOnly(boolean isReadOnly) {
    this.isReadOnly = isReadOnly;
  }

}