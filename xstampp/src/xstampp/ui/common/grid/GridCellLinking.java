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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.nebula.widgets.grid.GridEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import messages.Messages;
import xstampp.astpa.haz.ITableModel;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.ITableContentProvider;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * 
 * @author Benedikt Markt
 * 
 * @param <T>
 *          The content provider class for the table
 */
public class GridCellLinking<T extends ITableContentProvider> extends AbstractGridCell {

  /**
   * The log4j logger.
   */
  private static final Logger LOGGER = Logger.getRootLogger();

  private static final int DELETE_LINK_BUTTON_SIZE = 16;
  private static final Color TEXT_COLOR = new Color(Display.getCurrent(), 0, 0, 0);
  private static final Color HAZARD_LINK_COLOR = new Color(Display.getCurrent(), 230, 20, 5);

  private final String prefix;
  private final String linkEmptyText = Messages.NotHazardous;
  private final UUID assignedId;
  private final T publicInterface;
  private final transient GridWrapper grid;
  private CellButtonContainer buttonContainer;


  private int lines = 1;
  private int lastLines = 1;

  private final class PropopsalListener implements IContentProposalListener {

    @Override
    public void proposalAccepted(IContentProposal proposal) {
      GridCellLinking.this.grid.resizeRows();
      String text = proposal.getContent();
      ITableModel item = GridCellLinking.this.toLinkable(text);
      if (item != null) {
        GridCellLinking.this.publicInterface.addLink(assignedId, item.getId());
      }
    }
  }

  /**
   * Delete button display while hovering over a linking cell, used to remove
   * links using the mouse.
   * 
   * @author Patrick Wickenhaeuser, Benedikt Markt
   * 
   */
  private class DeleteLinkButton extends CellButton {

    private ITableModel tableModel;
    private ITableContentProvider provider;

    public DeleteLinkButton(Rectangle rect, Image image, ITableModel tableModel, ITableContentProvider provider) {
      super(rect, image);
      this.tableModel = tableModel;
      this.provider = provider;
    }

    @Override
    public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
      GridCellLinking.LOGGER.debug("Delete link"); //$NON-NLS-1$
      this.provider.removeLink(GridCellLinking.this.assignedId, this.tableModel.getId());
    }
  }

  /**
   * 
   * @author Benedikt Markt
   * 
   * @param assignedId
   *          the id of the assigned item
   * @param publicInterface
   *          the content provider
   * @param grid
   *          the grid the cell is in
   * @param prefix
   *          the prefix for item indices, like 'H-' for hazards
   */
  public GridCellLinking(final UUID assignedId, final T publicInterface, final GridWrapper grid, final String prefix) {
    super();

    this.assignedId = assignedId;
    this.publicInterface = publicInterface;
    this.prefix = prefix;
    this.grid = grid;

    this.buttonContainer = new CellButtonContainer();
  }

  
  private ITableModel toLinkable(final String linkableString) {
    final List<ITableModel> items = this.publicInterface.getAllItems();
    for (final ITableModel model : items) {
      final String tmp = this.prefix + model.getNumber();
      if (linkableString.equals(tmp)) {
        return model;
      }
    }
    return null;

  }

  @Override
  public void paint(final GridCellRenderer renderer, final GC gc, final NebulaGridRowWrapper item) {
    super.paint(renderer, gc, item);
    Color foreground = gc.getForeground();
    gc.setForeground(GridCellLinking.TEXT_COLOR);
    final Rectangle bounds = renderer.getDrawBounds();
    final int renderX = bounds.x;
    final int renderY = bounds.y;
    final int renderWidth = bounds.width;

    gc.setBackground(this.getBackgroundColor(renderer, gc));

    List<ITableModel> linkedItems = this.publicInterface.getLinkedItems(this.assignedId);
    Collections.sort(linkedItems);

    if (linkedItems.isEmpty()) {
      gc.drawString(this.linkEmptyText, renderX + 2, renderY);
    } else {
      this.buttonContainer.clearButtons();

      int xOff = 0;
      int yOff = 0;
      this.lines = 1;
      boolean hovered = this.grid.getHoveredCell() == GridCellLinking.this;
      for (final ITableModel model : linkedItems) {

        String linkText = "[" + this.prefix + model.getNumber() //$NON-NLS-1$
            + "]"; //$NON-NLS-1$
        int textWidth = gc.getFontMetrics().getAverageCharWidth() * linkText.length();

        if (xOff >= (renderWidth - 40 - textWidth)) {
          this.lines++;
          xOff = 0;
          yOff += AbstractGridCell.DEFAULT_CELL_HEIGHT;

        }

        gc.setForeground(GridCellLinking.HAZARD_LINK_COLOR);
        gc.drawString(linkText, renderX + 4 + xOff, renderY + yOff);
        // restore old foreground color
        gc.setForeground(foreground);

        xOff += textWidth;
        if (hovered) {
          this.buttonContainer.addCellButton(new DeleteLinkButton(
              new Rectangle(xOff + 4, yOff + 2, GridCellLinking.DELETE_LINK_BUTTON_SIZE,
                  GridCellLinking.DELETE_LINK_BUTTON_SIZE),
              GridWrapper.getDeleteButton16(), model, this.publicInterface));
        }
        xOff += GridCellLinking.DELETE_LINK_BUTTON_SIZE;

      }

      if (this.lines != this.lastLines) {
        this.lastLines = this.lines;
        this.grid.resizeRows();
      }

      if (hovered) {
        this.buttonContainer.paintButtons(renderer, gc);
      }
    }

    final int height = 18;
    gc.setAntialias(SWT.ON);
    gc.drawImage(GridWrapper.getLinkButton16(), (renderX + renderWidth) - height - 4, renderY + 1);
  }

  @Override
  public void onMouseDown(final MouseEvent event,
                          final Point relativeMouse,
                          final Rectangle cellBounds) {
    GridCellLinking.LOGGER.debug("Link clicked"); //$NON-NLS-1$
    if (this.buttonContainer.onMouseDown(relativeMouse, cellBounds)) {
      // don't link if a delete button is hit.
      return;
    }
  
    List<ITableModel> linkedItems = this.publicInterface.getLinkedItems(this.assignedId);

    /*
     * Prevent opening linking popup while another cell is focused and a delete
     * button is pressed. While another cell is active the button container is
     * empty at the time the cell is activated.
     */
    if (this.buttonContainer.isEmpty() && !linkedItems.isEmpty()) {
      return;
    }
   
    final List<ITableModel> items = this.publicInterface.getAllItems();
    // remove all already linked items
    items.removeAll(linkedItems);

    // Long descriptive string for using in the suggestion box
    final String[] options = new String[items.size()];
    // Short descriptive string for displaying in the
    // suggestions box
    final String[] labels = new String[items.size()];
    // Long descripton
    final String[] descriptions = new String[items.size()];
    
    for (int i = 0; i < items.size(); i++) {
      final String itemNumber = this.prefix + items.get(i).getNumber();
      final String itemTitle = items.get(i).getTitle();

      options[i] = itemTitle;
      labels[i] = itemNumber;
      descriptions[i] = items.get(i).getDescription();
    }

  
    //start creating the grid editor linkEditor

    final Text linkEditor = new Text(this.grid.getGrid(), SWT.PUSH);

    linkEditor.setText(""); //$NON-NLS-1$
    linkEditor.setEditable(true);
    linkEditor.setVisible(true);

    // Listener for Proposal Selection to instantly link if proposal is
    // selected
    linkEditor.setText(""); //$NON-NLS-1$
    linkEditor.setMessage(Messages.StartTyping);
    
    Point mousePoint = new Point(event.x, event.y);
    GridEditor editor = new GridEditor(this.grid.getGrid());
    editor.setEditor(linkEditor);
    editor.setItem(this.grid.getGrid().getItem(mousePoint));
    editor.setColumn(this.grid.getGrid().getCell(mousePoint).x);

    editor.grabHorizontal = true;
    editor.grabVertical = true;

    linkEditor.setFocus();
    
    AutoCompleteField linkField = new AutoCompleteField(linkEditor,
                        new TextContentAdapter(), options, labels, descriptions);

    linkField.setPopupPosition(relativeMouse, cellBounds, cellBounds.height);

    if (this.grid.getGrid().getDisplay() != null) {
      linkField.setProposalListener(new PropopsalListener());
      linkField.openPopup();
    } else {
      MessageDialog.openError(null, "Widget is disposed",
          "for some reason the Platform can't find a suficient display!");
    }
  }

  @Override
  public int getPreferredHeight() {
    return this.lines * AbstractGridCell.DEFAULT_CELL_HEIGHT;
  }

  @Override
  public void cleanUp() {
    
  }

  @Override
  public void addCellButton(CellButton button) {
    // intentionally empty
  }

  @Override
  public void clearCellButtons() {
    // intentionally empty
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
