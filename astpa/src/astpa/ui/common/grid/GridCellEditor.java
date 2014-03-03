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

import messages.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * A cell that contains an SWT text editor.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 * 
 */
public class GridCellEditor extends GridCellComposite {
	
	/**
	 * The default Text
	 * 
	 * @author Benedikt Markt
	 */
	public static final String EMPTY_CELL_TEXT = Messages.ClickToEdit;
	
	private GridWrapper grid = null;
	private Composite compositeArea = null;
	private Text description = null;
	private String currentText = ""; //$NON-NLS-1$
	private boolean hasFocus = false;
	
	
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param gridWrapper the grid.
	 * @param initialText the intitial text in the editor.
	 * 
	 */
	public GridCellEditor(GridWrapper gridWrapper, String initialText) {
		super(gridWrapper, SWT.PUSH);
		
		this.grid = gridWrapper;
		
		this.compositeArea = new Composite(this, SWT.FILL);
		this.compositeArea.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		this.description = new Text(this.compositeArea, SWT.PUSH | SWT.WRAP);
		
		if (initialText.trim().isEmpty()) {
			this.description.setText(GridCellEditor.EMPTY_CELL_TEXT);
		} else {
			this.description.setText(initialText);
		}
		this.currentText = this.description.getText();
		this.description.setVisible(true);
		this.description.setEditable(true);
		// redirect the mouse events
		this.description.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				GridCellEditor.this.onMouseUp(e);
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// relative mouse position not known here
				GridCellEditor.this.onMouseDown(e, null, null);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// intentionally empty
			}
		});
		
		this.description.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				GridCellEditor.this.currentText = GridCellEditor.this.description.getText();
				
				GridCellEditor.this.grid.resizeRows();
			}
		});
		
		this.description.addListener(SWT.FocusOut, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				GridCellEditor.this.onTextChanged(GridCellEditor.this.currentText);
				GridCellEditor.this.hasFocus = false;
			}
		});
		
		this.description.addListener(SWT.FocusIn, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				GridCellEditor.this.hasFocus = true;
				GridCellEditor.this.onEditorFocus();
			}
		});
		
		this.description.addListener(SWT.KeyUp, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (event.character == SWT.CR) {
					GridCellEditor.this.description.traverse(SWT.TRAVERSE_TAB_NEXT);
				}
			}
			
		});
		
		this.description.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent event) {
				// verifies if character is no line break
				event.doit = event.character != SWT.CR;
				
			}
			
		});
	}
	
	@Override
	public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
		this.compositeArea.setBounds(0, 0, renderer.getDrawBounds().width - 1, renderer.getDrawBounds().height);
		
		if (this.hasFocus) {
			this.description.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		} else {
			this.description.setBackground(this.getBackgroundColor(renderer, gc));
		}
		
		super.paint(renderer, gc, item);
	}
	
	@Override
	public void onMouseDown(MouseEvent e, org.eclipse.swt.graphics.Point relativeMouse, Rectangle cellBounds) {
		this.activate();
	}
	
	/**
	 * Gets called when the swt.text gets focus
	 * 
	 * @author Benedikt Markt
	 * 
	 */
	public void onEditorFocus() {
		// intentionally empty
	}
	
	@Override
	public int getPreferredHeight() {
		
		// upper limit
		
		// lower limit
		int minHeight = AbstractGridCell.DEFAULT_CELL_HEIGHT * 2;
		int textSize = this.description.getSize().x;
		if (textSize == 0) {
			this.description.setSize(new Point((this.grid.getGrid().getColumn(1).getWidth() - 33), 0));
		}
		int preferredHeight =
			(this.description.getLineHeight() * this.description.getLineCount()) + AbstractGridCell.DEFAULT_CELL_HEIGHT;
		this.description.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		return Math.max(minHeight, preferredHeight);
	}
	
	@Override
	public void activate() {
		this.description.setFocus();
	}
	
	/**
	 * Gets called when the text changed and the cell lost focus.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param newText the new text.
	 * 
	 */
	public void onTextChanged(@SuppressWarnings("unused") String newText) {
		// intentionally empty
	}
	
	/**
	 * Get the composite area filling the cell.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the composite area filling the cell.
	 */
	protected Composite getCompositeArea() {
		return this.compositeArea;
	}
	
	/**
	 * Get the text editor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the text editor.
	 */
	protected Text getTextEditor() {
		return this.description;
	}
	
}