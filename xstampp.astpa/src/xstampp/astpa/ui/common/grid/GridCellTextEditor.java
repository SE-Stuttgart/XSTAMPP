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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
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

	private class TextLocator implements CellEditorLocator{

		@Override
		public void relocate(CellEditor celleditor) {
			if(GridCellTextEditor.this.editField != null)
			celleditor.getControl().setBounds(editField);
		}
		
	}
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param gridWrapper
	 *            the grid.
	 * @param initialText
	 *            the intitial text in the editor.
	 * 
	 */
	public GridCellTextEditor(GridWrapper grid,String initialText) {
		this.grid = grid;
		this.currentText = initialText;
	}

	@Override
	public void paint(GridCellRenderer renderer, GC gc,
			NebulaGridRowWrapper item) {
		super.paint(renderer, gc, item);
		Color bgColor = gc.getBackground();

		Rectangle bounds = renderer.getDrawBounds();
		gc.setBackground(this.getBackgroundColor(renderer, gc));

		Color fgColor = gc.getForeground();
		FontMetrics metrics= gc.getFontMetrics();
		//calculate the cvaiable space and performe a wrap
		int char_wrapper= bounds.width/metrics.getAverageCharWidth() -1;
		int lines = this.currentText.length() / char_wrapper;
		int start = 0;
		int end = Math.min(this.currentText.length(),char_wrapper);
		int line_height = bounds.y;
		while(end < this.currentText.length()-1){
			gc.drawText(this.currentText.substring(start, end), bounds.x + 2 ,line_height);
			start += char_wrapper;
			end += char_wrapper;
			line_height += metrics.getHeight();
		}
		gc.drawText(this.currentText.substring(start), bounds.x + 2 ,line_height);
		this.editField = new Rectangle(bounds.x, bounds.y, bounds.width-16, bounds.height);
		gc.drawImage(GridWrapper.getDeleteButton16(), bounds.x + bounds.width -16,bounds.y +  bounds.height/2 - 8);
		// restore bg color
		gc.setBackground(bgColor);
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
		int minHeight = AbstractGridCell.DEFAULT_CELL_HEIGHT * 2;
		GC gc = new GC(this.grid.getGrid());
		FontMetrics fm= gc.getFontMetrics();
		int string_h= fm.getHeight();
		int string_w= fm.getAverageCharWidth();
		int preferredHeight=minHeight;
		int string_pref=this.grid.getGrid().getBounds().width/4;
		if(string_pref > 0){
			string_pref = (string_w * this.currentText.length())/string_pref +1;
			
		}else{
			return minHeight;
		}
		
		// upper limit

		// lower limit
		
		return Math.max(minHeight, preferredHeight);
	}

	@Override
	public void activate() {
		CSDirectEditor editor = new CSDirectEditor(this.grid.getGrid());
		editor.activate(new TextLocator(),this.grid.getGrid());
		editor.setTextColor(ColorConstants.black);
		editor.setTextFont(Display.getDefault().getSystemFont());
		editor.setValue(currentText);
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