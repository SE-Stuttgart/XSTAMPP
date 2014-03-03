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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.nebula.widgets.grid.GridEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

import astpa.model.ITableModel;
import astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * 
 * @author Benedikt Markt
 * 
 * @param <T> The content provider class for the table
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
	private transient Text linkEditor;
	private transient AutoCompleteField linkField;
	private final transient GridWrapper grid;
	private transient String lastText = ""; //$NON-NLS-1$
	private CellButtonContainer buttonContainer;
	
	private boolean hasTextFocus = false;
	private boolean isPopupOpen = false;
	
	private int lines = 1;
	private int lastLines = 1;
	
	
	private final class PropopsalListener implements IContentProposalListener {
		
		@Override
		public void proposalAccepted(IContentProposal proposal) {
			GridCellLinking.this.lastText = proposal.getContent();
			GridCellLinking.this.grid.resizeRows();
			GridCellLinking.this.link();
		}
	}
	
	private final class LinkEditorKeyListener implements KeyListener {
		
		@Override
		public void keyReleased(final KeyEvent keyEvent) {
			// intentionally empty
		}
		
		@Override
		public void keyPressed(final KeyEvent keyEvent) {
			if (!GridCellLinking.this.linkField.getContentProposalAdapter().isProposalPopupOpen()) {
				if (keyEvent.keyCode == SWT.CR) {
					// link immediately if the return button is hit
					GridCellLinking.this.lastText = GridCellLinking.this.linkEditor.getText();
					GridCellLinking.this.linkEditor.dispose();
					GridCellLinking.this.grid.resizeRows();
					GridCellLinking.this.link();
					GridCellLinking.this.linkEditor.traverse(SWT.TRAVERSE_TAB_NEXT);
				}
			}
		}
	}
	
	private final class LinkEditorFocusListener implements FocusListener {
		
		@Override
		public void focusGained(FocusEvent e) {
			GridCellLinking.this.hasTextFocus = true;
			
		}
		
		@Override
		public void focusLost(FocusEvent e) {
			GridCellLinking.this.hasTextFocus = false;
			// if the popup is closed and the text editor loses focus, the input
			// is cancelled
			if (!GridCellLinking.this.isPopupOpen) {
				GridCellLinking.this.linkEditor.dispose();
			}
			
		}
	}
	
	private final class PopupListener implements IContentProposalListener2 {
		
		@Override
		public void proposalPopupOpened(ContentProposalAdapter adapter) {
			GridCellLinking.this.isPopupOpen = true;
			
		}
		
		@Override
		public void proposalPopupClosed(ContentProposalAdapter adapter) {
			GridCellLinking.this.isPopupOpen = false;
			// if the text field has no focus and the popup is closed, the input
			// is cancelled
			if (!GridCellLinking.this.hasTextFocus) {
				GridCellLinking.this.linkEditor.dispose();
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
	 * @param assignedId the id of the assigned item
	 * @param publicInterface the content provider
	 * @param grid the grid the cell is in
	 * @param prefix the prefix for item indices, like 'H-' for hazards
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
	
	private void link() {
		final String text = this.lastText;
		final ITableModel item = GridCellLinking.this.toLinkable(text);
		if (item != null) {
			GridCellLinking.this.publicInterface.addLink(this.assignedId, item.getId());
		}
		final List<ITableModel> items = GridCellLinking.this.publicInterface.getAllItems();
		final List<ITableModel> linkedItems = GridCellLinking.this.publicInterface.getLinkedItems(this.assignedId);
		
		items.removeAll(linkedItems);
		
		final LinkedList<String> proposalList = new LinkedList<String>();
		final LinkedList<String> labelList = new LinkedList<String>();
		
		for (final ITableModel model : items) {
			proposalList.add("H-" + model.getNumber()); //$NON-NLS-1$
			labelList.add(model.getTitle());
		}
		
		final String[] proposals = proposalList.toArray(new String[proposalList.size()]);
		final String[] labels = labelList.toArray(new String[labelList.size()]);
		
		GridCellLinking.this.linkField.setProposals(proposals);
		GridCellLinking.this.linkField.setLabels(labels);
		
	}
	
	@Override
	public void paint(final GridCellRenderer renderer, final GC gc, final NebulaGridRowWrapper item) {
		super.paint(renderer, gc, item);
		Color foreground = gc.getForeground();
		gc.setForeground(TEXT_COLOR);
		final Rectangle bounds = renderer.getDrawBounds();
		final int renderX = bounds.x;
		final int renderY = bounds.y;
		final int renderWidth = bounds.width;
		
		gc.setBackground(this.getBackgroundColor(renderer, gc));
		
		final List<ITableModel> linkedItems = this.publicInterface.getLinkedItems(this.assignedId);
		Collections.sort(linkedItems);
		
		if (linkedItems.isEmpty()) {
			gc.drawString(this.linkEmptyText, renderX + 2, renderY);
		} else {
			this.buttonContainer.clearButtons();
			
			int xOff = 0;
			int yOff = 0;
			this.lines = 1;
			final boolean hovered = this.grid.getHoveredCell() == GridCellLinking.this;
			for (final ITableModel model : linkedItems) {
				
				final String linkText = "[" + this.prefix + model.getNumber() //$NON-NLS-1$
					+ "]"; //$NON-NLS-1$
				final int textWidth = gc.getFontMetrics().getAverageCharWidth() * linkText.length();
				
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
					this.buttonContainer.addCellButton(new DeleteLinkButton(new Rectangle(xOff + 4, yOff + 2,
						GridCellLinking.DELETE_LINK_BUTTON_SIZE, GridCellLinking.DELETE_LINK_BUTTON_SIZE), GridWrapper
						.getDeleteButton16(), model, this.publicInterface));
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
	public void onMouseDown(final MouseEvent event, final Point relativeMouse, final Rectangle cellBounds) {
		GridCellLinking.LOGGER.debug("Link clicked"); //$NON-NLS-1$
		if (this.buttonContainer.onMouseDown(relativeMouse, cellBounds)) {
			// don't link if a delete button is hit.
			return;
		}
		
		final List<ITableModel> linkedItems = this.publicInterface.getLinkedItems(this.assignedId);
		
		/*
		 * Prevent opening linking popup while another cell is focused and a
		 * delete button is pressed. While another cell is active the button
		 * container is empty at the time the cell is activated.
		 */
		if (this.buttonContainer.isEmpty() && !linkedItems.isEmpty()) {
			return;
		}
		
		if ((this.linkEditor != null) && !this.linkEditor.isDisposed()) {
			this.linkEditor.dispose();
		}
		this.linkEditor = new Text(this.grid.getGrid(), SWT.PUSH);
		this.linkEditor.setText(""); //$NON-NLS-1$
		this.linkEditor.setEditable(true);
		this.linkEditor.setVisible(true);
		
		this.linkEditor.addFocusListener(new LinkEditorFocusListener());
		
		this.linkEditor.addKeyListener(new LinkEditorKeyListener());
		
		final Point mousePoint = new Point(event.x, event.y);
		
		final GridEditor editor = new GridEditor(this.grid.getGrid());
		editor.setEditor(this.linkEditor);
		editor.setItem(this.grid.getGrid().getItem(mousePoint));
		editor.setColumn(this.grid.getGrid().getCell(mousePoint).x);
		
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		
		this.linkEditor.setFocus();
		// Long descriptive string for using in the suggestion box
		final LinkedList<String> itemDescriptions = new LinkedList<String>();
		
		// Short descriptive string for displaying in the
		// suggestions box
		final LinkedList<String> itemShortStrings = new LinkedList<String>();
		
		// Long descripton
		final LinkedList<String> itemLongStrings = new LinkedList<String>();
		
		final List<ITableModel> items = this.publicInterface.getAllItems();
		// remove all already linked items
		items.removeAll(linkedItems);
		
		for (int i = 0; i < items.size(); i++) {
			final String itemNumber = this.prefix + items.get(i).getNumber();
			final String itemTitle = items.get(i).getTitle();
			
			itemDescriptions.add(itemTitle);
			itemShortStrings.add(itemNumber);
			itemLongStrings.add(items.get(i).getDescription());
		}
		
		final String options[] = itemShortStrings.toArray(new String[itemShortStrings.size()]);
		
		final String labels[] = itemDescriptions.toArray(new String[itemDescriptions.size()]);
		
		final String descriptions[] = itemLongStrings.toArray(new String[itemLongStrings.size()]);
		
		this.linkField =
			new AutoCompleteField(this.linkEditor, new TextContentAdapter(), options, labels, descriptions);
		// Listener for Proposal Selection to instantly link if proposal is
		// selected
		this.linkField.getContentProposalAdapter().addContentProposalListener(new PropopsalListener());
		this.linkEditor.setText(""); //$NON-NLS-1$
		this.linkEditor.setMessage(Messages.StartTyping);
		
		this.linkField.getContentProposalAdapter().addContentProposalListener(new PopupListener());
		this.linkField.openPopup();
		final ToolTip tip = new ToolTip(this.grid.getGrid().getShell(), SWT.ICON_INFORMATION);
		tip.setMessage(Messages.TypeHere);
		tip.setVisible(true);
	}
	
	@Override
	public int getPreferredHeight() {
		return this.lines * AbstractGridCell.DEFAULT_CELL_HEIGHT;
	}
	
	@Override
	public void cleanUp() {
		GridCellLinking.LOGGER.info("Clean Up!"); //$NON-NLS-1$
		if ((this.linkEditor != null) && !this.linkEditor.isDisposed()) {
			this.linkEditor.dispose();
		}
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
