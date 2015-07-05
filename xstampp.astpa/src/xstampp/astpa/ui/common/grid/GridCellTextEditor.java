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

import java.util.UUID;

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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.controlstructure.utilities.CSDirectEditor;
import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * A cell that contains an SWT text editor.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 * 
 */
public abstract class GridCellTextEditor extends AbstractGridCell {

	private Rectangle editField;
	/**
	 * The default Text
	 * 
	 * @author Benedikt Markt
	 */
	public static final String EMPTY_CELL_TEXT = Messages.ClickToEdit;

	private GridWrapper grid = null;
	private String currentText = ""; //$NON-NLS-1$
	private boolean hasFocus = false;
	private Color bgColor;
	private CSDirectEditor editor;
	private Rectangle deleteSpace;
	private boolean showDelete;
	
	
	private class TextLocator implements CellEditorLocator{

		@Override
		public void relocate(CellEditor celleditor) {
			celleditor.getControl().setBounds(GridCellTextEditor.this.editField);
		}
		
	}
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * @param initialText
	 *            the intitial text in the editor.
	 * @param showDelete 
	 * 
	 */
	public GridCellTextEditor(GridWrapper grid,String initialText,Boolean showDelete) {
		this.showDelete=showDelete;
		this.grid=grid;
		this.currentText = initialText;
		this.editField= new Rectangle(0, 0, -1, DEFAULT_CELL_HEIGHT);
		
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
		FontMetrics metrics= gc.getFontMetrics();
		int buttonCollum=0;
		//calculate the avaiable space and performe a wrap
		if(this.showDelete){
			this.deleteSpace = new Rectangle(bounds.x + bounds.width -16,bounds.y +  bounds.height/2 - 8,16,16);
			buttonCollum=this.deleteSpace.width;
			gc.drawImage(GridWrapper.getDeleteButton16(),this.deleteSpace.x,this.deleteSpace.y);
		}
		int line_height = bounds.y;
		String[] words= this.currentText.split(" ");
		String line="";
		String tmpLine=words[0];
		String space = "";
		boolean first = true;
		boolean carryOver=false;
		int i=1;
		while(i<= words.length){
			if(!first){
				space= " "; 
			}else{
				space="";
			}
			if(tmpLine.startsWith(System.lineSeparator())){
				gc.drawString(line,bounds.x + 2 ,line_height);
				line = "";
				tmpLine = tmpLine.replaceFirst(System.lineSeparator(), "");
				line_height += metrics.getHeight();
				first=true;
				carryOver= false;
				continue;
			}
			if(tmpLine.contains(System.lineSeparator())){
				words[i-1] = tmpLine.substring(tmpLine.indexOf(System.lineSeparator()));
				
				tmpLine = tmpLine.substring(0,tmpLine.indexOf(System.lineSeparator()));
				first = line.isEmpty();
				carryOver= true;
			}
			
			if(gc.stringExtent(line + space + tmpLine).x >= bounds.width -2 - buttonCollum){
							
				gc.drawString(line, bounds.x + 2 ,line_height);			
				line_height += metrics.getHeight();	
				first=true;
				line = "";
			}
			else if(carryOver){
				line += space + tmpLine;
				tmpLine = words[i-1];
				carryOver = false;
				first=false;
			}else if(i == words.length){
				line += space + tmpLine;
				gc.drawString(line, bounds.x + 2 ,line_height);			
				line_height += metrics.getHeight();	
				line = "";
				tmpLine ="";
				i++;
			}else{
				line += space + tmpLine;
				tmpLine = words[i++];
				first=false;
			}
		}
		line_height += 2;
		
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
		this.editor = new CSDirectEditor(this.grid.getGrid());
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
			}
		});
		this.editor.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Rectangle bounds = ((Text)e.widget).getBounds();
				int newHeight= ((Text)e.widget).computeSize(GridCellTextEditor.this.editField.width, SWT.DEFAULT).y;
				bounds.height = Math.max(newHeight, GridCellTextEditor.this.editField.height);
				((Text)e.widget).setBounds(bounds);
			}
		});
	}

	@Override
	public void cleanUp() {
		if(this.editor != null && !this.editor.getControl().isDisposed()){
			this.editor.getControl().dispose();
		}
		
	}

	@Override
	public void addCellButton(CellButton button) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearCellButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onMouseUp(MouseEvent e) {
		if(this.showDelete && GridCellTextEditor.this.deleteSpace.contains(e.x,e.y)){
			delete();
		}
		super.onMouseUp(e);
	}

	
	public abstract void delete();
	public abstract void updateDataModel(String description);
	
}