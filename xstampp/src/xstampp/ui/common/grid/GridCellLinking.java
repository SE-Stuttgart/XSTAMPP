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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import xstampp.model.ITableEntry;
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

  private final UUID assignedId;
  private final T publicInterface;
  private final transient GridWrapper grid;
  private CellButtonContainer buttonContainer;
  private List<CellButton> buttons;
  private Map<Rectangle, String> toolTipsToLinkAreas;
  private int lines;

  private boolean canEdit;

  /**
   * Delete button display while hovering over a linking cell, used to remove links using the mouse.
   * 
   * @author Patrick Wickenhaeuser, Benedikt Markt
   * 
   */
  private class DeleteLinkButton extends CellButton {

    private ITableEntry tableModel;
    private ITableContentProvider<? extends ITableEntry> provider;

    public DeleteLinkButton(Rectangle rect, Image image, ITableEntry tableModel,
        ITableContentProvider<? extends ITableEntry> provider) {
      super(rect, image);
      this.tableModel = tableModel;
      this.provider = provider;
    }

    @Override
    public boolean setBounds(Rectangle bounds) {
      return false;
    }

    @Override
    public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
      GridCellLinking.LOGGER.debug("Delete link"); //$NON-NLS-1$
      this.provider.removeLink(GridCellLinking.this.assignedId, this.tableModel.getId());
      grid.getGrid().redraw();
    }
  }

  /**
   * constructs a generic gridCell that is capable of containing several editable ITableEntry links.
   * 
   * @author Benedikt Markt
   * 
   * @param assignedId
   *          the id of the assigned item
   * @param publicInterface
   *          the content provider
   * @param grid
   *          the grid the cell is in
   */
  public GridCellLinking(final UUID assignedId, final T publicInterface, final GridWrapper grid) {
    this(assignedId, publicInterface, grid, true);
  }

  /**
   * constructs a generic gridCell that is capable of containing several editable ITableEntry links.
   * 
   * @author Benedikt Markt
   * 
   * @param assignedId
   *          the id of the assigned item
   * @param publicInterface
   *          the content provider
   * @param grid
   *          the grid the cell is in
   * @param canEdit
   *          TODO
   */
  public GridCellLinking(final UUID assignedId, final T publicInterface, final GridWrapper grid,
      boolean canEdit) {
    super();
    this.toolTipsToLinkAreas = new HashMap<>();
    this.assignedId = assignedId;
    this.publicInterface = publicInterface;
    this.grid = grid;
    this.canEdit = canEdit;
    this.lines = 1;
    this.buttons = new ArrayList<>();
    if (canEdit) {
      buttons.add(new CellButtonLinking<T>(grid, publicInterface, assignedId));
    }
    this.buttonContainer = new CellButtonContainer();
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

    List<? extends ITableEntry> linkedItems = this.publicInterface.getLinkedItems(this.assignedId);
    Collections.sort(linkedItems);
    this.buttonContainer.clearButtons();
    for (CellButton button : buttons) {
      this.buttonContainer.addColumButton(button);
    }
    this.toolTipsToLinkAreas.clear();
    if (linkedItems.isEmpty()) {
      gc.drawString(this.publicInterface.getEmptyMessage(), renderX + 2, renderY);
    } else {

      int xOff = 0;
      int yOff = 0;
      int tmpLines = 1;
      boolean hovered = this.grid.getHoveredCell() == GridCellLinking.this;
      for (final ITableEntry model : linkedItems) {

        String linkText = "[" + this.publicInterface.getPrefix() + model.getNumber() //$NON-NLS-1$
            + "]"; //$NON-NLS-1$
        int textWidth = gc.getFontMetrics().getAverageCharWidth() * linkText.length();

        if (xOff >= (renderWidth - 40 - textWidth)) {
          tmpLines++;
          xOff = 0;
          yOff += AbstractGridCell.DEFAULT_CELL_HEIGHT;
        }
        gc.setForeground(GridCellLinking.HAZARD_LINK_COLOR);
        Rectangle linkArea = new Rectangle(renderX + 4 + xOff, renderY + yOff, textWidth,
            AbstractGridCell.DEFAULT_CELL_HEIGHT);
        gc.drawString(linkText, linkArea.x, linkArea.y);
        toolTipsToLinkAreas.put(linkArea, model.getTitle());
        // restore old foreground color
        gc.setForeground(foreground);

        xOff += textWidth;
        if (hovered && canEdit) {
          this.buttonContainer.addCellButton(new DeleteLinkButton(
              new Rectangle(xOff + 4, yOff + 2, GridCellLinking.DELETE_LINK_BUTTON_SIZE,
                  GridCellLinking.DELETE_LINK_BUTTON_SIZE),
              GridWrapper.getDeleteButton16(), model, this.publicInterface));
        }
        xOff += GridCellLinking.DELETE_LINK_BUTTON_SIZE;

      }

      if (this.lines != tmpLines) {
        this.lines = tmpLines;
        item.setHeight(lines * AbstractGridCell.DEFAULT_CELL_HEIGHT);
      }

    }
    this.buttonContainer.paintButtons(renderer, gc);
    gc.setAntialias(SWT.ON);
  }

  @Override
  public String getToolTip(Point point) {
    for (Entry<Rectangle, String> linkEntry : toolTipsToLinkAreas.entrySet()) {
      if (linkEntry.getKey().contains(point)) {
        return linkEntry.getValue();
      }
    }
    return super.getToolTip(point);
  }

  @Override
  public void onMouseDown(final MouseEvent event, final Point relativeMouse,
      final Rectangle cellBounds) {
    GridCellLinking.LOGGER.debug("Link clicked"); //$NON-NLS-1$
    if (this.buttonContainer.onMouseDown(relativeMouse, cellBounds)) {
      // don't link if a delete button is hit.
      return;
    }
  }

  @Override
  public int getPreferredHeight() {
    return lines * AbstractGridCell.DEFAULT_CELL_HEIGHT;
  }

  @Override
  public void cleanUp() {

  }

  @Override
  public void addCellButton(CellButton button) {
    if (buttons == null) {
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
