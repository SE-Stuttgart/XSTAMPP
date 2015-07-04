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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.controlstructure.utilities.CSDirectEditor;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * A cell that contains an SWT text editor.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 * 
 */
public class GridCellTextEditor extends AbstractGridCell {

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
	 * @param uca 
	 * 
	 */
	public GridCellTextEditor(GridWrapper grid,String initialText,IUnsafeControlAction uca) {
		this.grid=grid;
		this.currentText = initialText;
		this.editField= new Rectangle(0, 0, -1, DEFAULT_CELL_HEIGHT);
		this.showSelection(false);
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
		//calculate the avaiable space and performe a wrap
		
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
			
			if(gc.stringExtent(line + space + tmpLine).x > bounds.width - 16){
							
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
		
		this.editField = new Rectangle(bounds.x, bounds.y, bounds.width-16,line_height - bounds.y);
		if(bounds.height + 2< this.editField.height){
			this.grid.resizeRows();
		}
		gc.drawImage(GridWrapper.getDeleteButton16(), bounds.x + bounds.width -16,bounds.y +  bounds.height/2 - 8);
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
		return  this.editField.height;
	}

	@Override
	public void activate() {
		CSDirectEditor editor = new CSDirectEditor(this.grid.getGrid());
		editor.activate(new TextLocator());
		editor.setTextColor(ColorConstants.black);
		editor.getControl().setBackground(this.bgColor);
		editor.setTextFont(Display.getDefault().getSystemFont());
		editor.setValue(this.currentText);
		editor.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				GridCellTextEditor.this.currentText = ((Text)e.widget).getText();
				GridCellTextEditor.this.editField = ((Text)e.widget).getBounds();
				GridCellTextEditor.this.grid.resizeRows();
			}
		});
		editor.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				Rectangle bounds = ((Text)e.widget).getBounds();
				int newHeight= ((Text)e.widget).computeSize(GridCellTextEditor.this.editField.width, SWT.DEFAULT).y;
				bounds.height = Math.max(newHeight, GridCellTextEditor.this.editField.height);
				((Text)e.widget).setBounds(bounds);
			}
		});
	}
	/**
	 * Gets called when the text changed and the cell lost focus.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param newText
	 *            the new text.
	 * 
	 */
	public void onTextChanged(@SuppressWarnings("unused") String newText) {
		// intentionally empty
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
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

}