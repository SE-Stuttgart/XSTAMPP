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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

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
public class GridCellLinking<T extends ITableContentProvider<?>> extends AbstractGridCell {

  /**
   * The log4j logger.
   */
  private static final Logger LOGGER = Logger.getRootLogger();

  private static final int DELETE_LINK_BUTTON_SIZE = 16;
  private static final Color TEXT_COLOR = new Color(Display.getCurrent(), 0, 0, 0);
  private static final Color HAZARD_LINK_COLOR = new Color(Display.getCurrent(), 230, 20, 5);

  private final String linkEmptyText = Messages.NotHazardous;
  private final UUID assignedId;
  private final T publicInterface;
  private final transient GridWrapper grid;
  private CellButtonContainer buttonContainer;
  private List<CellButton> buttons;

  private int lines = 1;
  private int lastLines = 1;

  private final class PropopsalListener implements IContentProposalListener {

    @Override
    public void proposalAccepted(IContentProposal proposal) {
      GridCellLinking.this.grid.resizeRows();
      String text = proposal.getContent();
      UUID linkId = GridCellLinking.this.toLinkable(text);
      if (linkId != null) {
        GridCellLinking.this.publicInterface.addLink(assignedId, linkId);
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
    private ITableContentProvider<? extends ITableModel> provider;

    public DeleteLinkButton(Rectangle rect, Image image,
                          ITableModel tableModel,
                          ITableContentProvider<? extends ITableModel> provider) {
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
   * constructs a generic gridCell that is capable of containing 
   * several editable ITableModel links.
   * @author Benedikt Markt
   * 
   * @param assignedId
   *          the id of the assigned item
   * @param publicInterface
   *          the content provider
   * @param grid
   *          the grid the cell is in
   */
  public GridCellLinking(final UUID assignedId,
                         final T publicInterface,
                         final GridWrapper grid) {
    super();

    this.assignedId = assignedId;
    this.publicInterface = publicInterface;
    this.grid = grid;

    this.buttonContainer = new CellButtonContainer();
  }

  /**
   * Returns the UUID of an object of the generic type given by the interface implementation
   * 
   * @param linkableString a string which must be mappable to a link object
   * @return the UUID of an object of the generic type given by the interface implementation 
   */
  private UUID toLinkable(final String linkableString) {
    final List<? extends ITableModel> items = this.publicInterface.getAllItems();
    for (ITableModel model : items) {
      String tmp = this.publicInterface.getPrefix() + model.getNumber();
      if (linkableString.equals(tmp)) {
        return model.getId();
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

    List<? extends ITableModel> linkedItems = this.publicInterface.getLinkedItems(this.assignedId);
    Collections.sort(linkedItems);

    if (linkedItems.isEmpty()) {
      gc.drawString(this.linkEmptyText, renderX + 2, renderY);
    } else {
      this.buttonContainer.clearButtons();
      for(CellButton button: buttons){
        this.buttonContainer.addCellButton(button);
      }
      int xOff = 0;
      int yOff = 0;
      this.lines = 1;
      boolean hovered = this.grid.getHoveredCell() == GridCellLinking.this;
      for (final ITableModel model : linkedItems) {

        String linkText = "[" + this.publicInterface.getPrefix() + model.getNumber() //$NON-NLS-1$
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
  
    List<? extends ITableModel> linkedItems = this.publicInterface.getLinkedItems(this.assignedId);

    /*
     * Prevent opening linking popup while another cell is focused and a delete
     * button is pressed. While another cell is active the button container is
     * empty at the time the cell is activated.
     */
    if (this.buttonContainer.isEmpty() && !linkedItems.isEmpty()) {
      return;
    }
   
    List<? extends ITableModel> items = this.publicInterface.getAllItems();
    // remove all already linked items
    items.removeAll(linkedItems);

    // Long descriptive string for using in the suggestion box
    String[] options = new String[items.size()];
    // Short descriptive string for displaying in the
    // suggestions box
    String[] labels = new String[items.size()];
    // Long descripton
    String[] descriptions = new String[items.size()];
    
    for (int i = 0; i < items.size(); i++) {
      String itemNumber = this.publicInterface.getPrefix() + items.get(i).getNumber();
      String itemTitle = items.get(i).getTitle();

      options[i] = itemTitle;
      labels[i] = itemNumber;
      descriptions[i] = items.get(i).getDescription();
    }
    
    AutoCompleteField linkField = new AutoCompleteField(null,
                        new TextContentAdapter(), options, labels, descriptions);

    linkField.setPopupPosition(grid.getGrid().toDisplay(relativeMouse.x + cellBounds.x,
                                                    relativeMouse.y + cellBounds.y));

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
    if(buttons == null){
      buttons = new ArrayList<>();
    }
    this.buttons.add(button);
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
