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

package xstampp.astpa.ui.common.grid;

import messages.Messages;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;
import xstampp.astpa.util.DirectEditor;

/**
 * A cell that contains an SWT text editor.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 * 
 */
public abstract class GridCellTextEditor extends AbstractGridCell{

	private Rectangle editField;
	/**
	 * The default Text
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
 	
	
	private class TextLocator implements CellEditorLocator{

		@Override
		public void relocate(CellEditor celleditor) {
			getPreferredHeight();
			Rectangle rect = GridCellTextEditor.this.editField;
			Text text = (Text) celleditor.getControl();

			// if the size is determined to be larger than the text lines itself
			// this the original size, will be displayed as long as it not
			// overwritten by text
			int editorHeight = Math.max(rect.height,
					text.getLineHeight() * text.getLineCount());
			text.setBounds(rect.x, rect.y, rect.width, editorHeight);
		}
		
	}
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * @param grid The grid canvas, this is needed to show the direct editor
	 * @param initialText
	 *            the intitial text in the editor.
	 * @param showDelete if the delete button should be drawn in this component
	 * 
	 */
	public GridCellTextEditor(GridWrapper grid,String initialText,Boolean showDelete) {
		this.showDelete=showDelete;
		this.deleteSpace = new Rectangle(0, 0, 0, 0);
		this.grid=grid;
		
		if (initialText.trim().isEmpty()) {
			this.currentText = new String();
		} else {
			this.currentText = initialText;
		}
		
		this.editField= new Rectangle(0, 0, -1, 2 * DEFAULT_CELL_HEIGHT);
		grid.getGrid().addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseScrolled(MouseEvent e) {
				if(GridCellTextEditor.this.editor != null && !GridCellTextEditor.this.editor.getControl().isDisposed()){
				GridCellTextEditor.this.editor.getControl().dispose();
			}
			}
		});
		
	}

	@Override
	public void paint(GridCellRenderer renderer, GC gc,
			NebulaGridRowWrapper item) {
		super.paint(renderer, gc, item);
		this.bgColor = gc.getBackground();
		Rectangle bounds = renderer.getDrawBounds();
		gc.setBackground(this.getBackgroundColor(renderer, gc));
		Color fgColor = gc.getForeground();
		gc.setForeground(ColorConstants.black);
		int buttonCollum=0;
		//calculate the avaiable space and performe a wrap
		if(this.showDelete){
			this.deleteSpace = new Rectangle(bounds.x + bounds.width -16,bounds.y +  bounds.height/2 - 8,16,16);
			buttonCollum=this.deleteSpace.width;
			gc.drawImage(GridWrapper.getDeleteButton16(),this.deleteSpace.x,this.deleteSpace.y);
		}
		int line_height;
		if (this.currentText.trim().isEmpty()) {
			line_height = wrapText(bounds,gc,EMPTY_CELL_TEXT,2,buttonCollum);
		} else {
			line_height = wrapText(bounds,gc,this.currentText,2,buttonCollum);
		}
		
		this.editField = new Rectangle(bounds.x, bounds.y, bounds.width-buttonCollum,line_height - bounds.y);
		if(bounds.height + 2< this.editField.height){
			this.grid.resizeRows();
		}
		// restore bg color
		gc.setBackground(this.bgColor);
		// restore fg color
		gc.setForeground(fgColor);
	}

	
	@Override
	public void onMouseDown(MouseEvent e,
			org.eclipse.swt.graphics.Point relativeMouse, Rectangle cellBounds) {
		this.activate();
	}

	@Override
	public int getPreferredHeight() {
		this.editField.height = Math.max(DEFAULT_CELL_HEIGHT*2,  this.editField.height);
		return  this.editField.height;
	}

	@Override
	public Color getBackgroundColor(GridCellRenderer renderer, GC gc) {
		Color color = super.getBackgroundColor(renderer, gc);
		this.bgColor = color;
		if(this.editor != null && !this.editor.getControl().isDisposed()){
			this.editor.getControl().setBackground(color);
		}
		return color;
	}
	@Override
	public void activate() {
		this.editor = new DirectEditor(this.grid.getGrid(), SWT.None);
		this.grid.setEditClient(this);
		this.editor.activate(new TextLocator());
		this.editor.setTextColor(ColorConstants.black);
		this.editor.getControl().setBackground(this.bgColor);
		this.editor.setTextFont(Display.getDefault().getSystemFont());
		this.editor.setValue(this.currentText);
		
		this.editor.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				GridCellTextEditor.this.currentText = ((Text)e.widget).getText();
				GridCellTextEditor.this.editField = ((Text)e.widget).getBounds();
				GridCellTextEditor.this.grid.resizeRows();
				updateDataModel(((Text)e.widget).getText());
				onTextChange();
			}
		});
		this.editor.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(e.getSource() instanceof Text){
					getPreferredHeight();
					Rectangle rect = GridCellTextEditor.this.editField;
					Text text = (Text) e.getSource();

					// if the size is determined to be larger than the text lines itself
					// this the original size, will be displayed as long as it not
					// overwritten by text
					int editorHeight = Math.max(rect.height,
							text.getLineHeight() * text.getLineCount());
					text.setBounds(rect.x, rect.y, rect.width, editorHeight);
				}
			}
		});
	}

	@Override
	public void cleanUp() {
		if(this.editor != null && !this.editor.getControl().isDisposed()){
			this.editor.getControl().dispose();
		}
		
	}

	public void onTextChange(){
		//do nothing by default
	}
	@Override
	public void addCellButton(CellButton button) {
		//the delete Button is aded manually to allow a better handling of the delete process
	}

	@Override
	public void clearCellButtons() {
		// not needed either
		
	}

	/**
	 * 
	 * @param e
	 */
	@Override
	public void onMouseUp(MouseEvent e) {
		if(this.showDelete && GridCellTextEditor.this.deleteSpace.contains(e.x,e.y)){
			delete();
		}
		super.onMouseUp(e);
	}
	
	/**
	 *The abstract methode which is linked with the delete button
	 *note that if showDelete is set to false, this function is never called
	 *
	 * @author Lukas
	 *
	 */
	public abstract void delete();
	
	/**
	 * This methode is called when ever a change is made to the displayed data
	 * @author Lukas
	 *
	 * @param newValue the changed text
	 */
	public abstract void updateDataModel(String newValue);
	
}