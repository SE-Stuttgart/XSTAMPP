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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * The base class representing a cell in the grid.
 * 
 * @author Patrick Wickenhaeuser, Benedikt Markt
 */
public abstract class AbstractGridCell implements IGridCell {

	/**
	 * The default height in pixel of a cell.
	 */
	public static final int DEFAULT_CELL_HEIGHT = 20;

	private static final int PARENT_RED = 215;
	private static final int PARENT_GREEN = 240;
	private static final int PARENT_BLUE = 255;

	private static final int HOVER_RED = 205;
	private static final int HOVER_GREEN = 245;
	private static final int HOVER_BLUE = 205;

	private static final int MOD_2_0_GRAY = 245;
	private static final int MOD_2_1_GRAY = 230;

	private GridRow row = null;

	private CellButtonContainer buttonContainer = null;

	private boolean showSelection;

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 */
	public AbstractGridCell() {
		this.row = null;
		this.showSelection(true);
		this.buttonContainer = new CellButtonContainer();
	}

	/**
	 * Get the button container.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the button container.
	 */
	public CellButtonContainer getButtonContainer() {
		return this.buttonContainer;
	}

	/**
	 * Get the background color of the cell.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param renderer
	 *            the renderer.
	 * @param gc
	 *            the GC.
	 * @return the background color.
	 */
	public Color getBackgroundColor(GridCellRenderer renderer, GC gc) {

		if (renderer.getGridWrapper().isCellSelected(this) && this.showSelection) {
			// selected color constant
			return new Color(gc.getDevice(), AbstractGridCell.HOVER_RED,
					AbstractGridCell.HOVER_GREEN, AbstractGridCell.HOVER_BLUE);
		}

		if ((this.row != null) && (this.row.getParentRow() == null)) {
			// parent color
			return new Color(gc.getDevice(), AbstractGridCell.PARENT_RED,
					AbstractGridCell.PARENT_GREEN, AbstractGridCell.PARENT_BLUE);
		}

		// alternating color
		// find out the index of the cell
		int index = 0;
		GridRow colorSelectRow =this.row.getParentRow();
		for (int i = 0; i < colorSelectRow.getChildren().size(); i++) {
			if (colorSelectRow.getChildren().get(i).equals(this.row)) {
				index = i / this.row.getColorDivider();
				break;
			}
		}

		if ((index % 2) == 0) {
			return new Color(gc.getDevice(), AbstractGridCell.MOD_2_0_GRAY,
					AbstractGridCell.MOD_2_0_GRAY,
					AbstractGridCell.MOD_2_0_GRAY);
		}
		// every second row
		return new Color(gc.getDevice(), AbstractGridCell.MOD_2_1_GRAY,
				AbstractGridCell.MOD_2_1_GRAY, AbstractGridCell.MOD_2_1_GRAY);
	}

	@Override
	public void paint(GridCellRenderer renderer, GC gc,
			NebulaGridRowWrapper item) {

		this.paintFrame(renderer, gc, item);
	}

	/**
	 * Paint the frame.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param renderer
	 *            the renderer.
	 * @param gc
	 *            the GC.
	 * @param item
	 *            the nebula grid item the cell belongs to.
	 */
	public void paintFrame(GridCellRenderer renderer, GC gc,
			@SuppressWarnings("unused") NebulaGridRowWrapper item) {
		Color bgColor = gc.getBackground();

		gc.setBackground(this.getBackgroundColor(renderer, gc));

		gc.fillRectangle(renderer.getDrawBounds().x,
				renderer.getDrawBounds().y, renderer.getDrawBounds().width,
				renderer.getDrawBounds().height);

		this.buttonContainer.paintButtons(renderer, gc);

		// restore bg color
		gc.setBackground(bgColor);
	}

	@Override
	public void onMouseDown(MouseEvent e, Point relativeMouse,
			Rectangle cellBounds) {
		this.buttonContainer.onMouseDown(relativeMouse, cellBounds);
	}

	@Override
	public void onMouseUp(MouseEvent e) {
		// intentionally empty
	}

	@Override
	public void onFocusGained() {
		// intentionally empty
	}

	@Override
	public void onFocusLost() {
		// intentionally empty
	}

	@Override
	public void setGridRow(GridRow gridRow) {
		this.row = gridRow;
	}
	
	public void showSelection(boolean show) {
		this.showSelection= show;
	}
	
	/**
	 * draws a string in the given bounds on the gc element,
	 * this method also performs linebreaks
	 *
	 * @author Lukas Balzer
	 *
	 * @param bounds the bounds the funktion can draw in, 
	 * @param gc the GC element to draw on
	 * @param text the given string line( given CRs are considert)
	 * @param left_space the offset from the bounds left end in points
	 * @param right_space the offset from the right side of the bounds
	 * @return
	 * 			the height of the written text sequence
	 */
	protected final int wrapText(Rectangle bounds, GC gc, String text,
								 int left_space, int right_space){
		if(text.isEmpty()){
			return 0;
		}
		FontMetrics metrics= gc.getFontMetrics();
		//the line height is set absolute so that the strings are drawn on the right position
		int line_height = bounds.y;
		String[] words= text.split(" ");
		String line="";
		String tmpLine=words[0];
		String space = "";
		boolean first = true;
		boolean carryOver=false;
		int i=1;
		int width=bounds.width -2 - right_space;
		while(i<= words.length){
			if(!first){
				space= " "; 
			}else{
				space="";
			}
			if(tmpLine.startsWith(System.lineSeparator())){
				gc.drawString(line,bounds.x + left_space ,line_height);
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
			}else if(gc.stringExtent(tmpLine).x >= width){
				int end= wrap(gc, tmpLine, width-1, 0,tmpLine.length()-1, 0,1,1);
				gc.drawString(tmpLine.substring(0, end), bounds.x + left_space ,line_height);			
				line_height += metrics.getHeight();	
				tmpLine=tmpLine.substring(end);
			}else
			
			if(gc.stringExtent(line + space + tmpLine).x >= width){
							
				gc.drawString(line, bounds.x + left_space ,line_height);			
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
				gc.drawString(line, bounds.x + left_space ,line_height);			
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
		
		//since line_height was initialized in absolute scale but this function should calculate the
		//relative text height the y -coordinate must be subtracted
		return line_height -(bounds.y -2);
		
	}
	
	/**
	 * 
	 *
	 * @author Lukas Balzer
	 *
	 * @param gc
	 * @param line the String line
	 * @param width the max width of the line 
	 * @param start the index the line should start with ( usually 0)
	 * @param end
	 * @param i
	 * @param res
	 * @param ch
	 * @return the maximal end index to ensure that the line is still visible
	 */
	protected final int wrap(GC gc,String line, int width,int start,int end,int i,double res,double ch){
		double _res;
		double _end=res * end;
		if(gc.stringExtent(line.substring(start, (int) (_end))).x <= width && ch > 0.1 ){
			_res = Math.min(1, res + ch * 0.5);
			return wrap(gc, line, width, start, end,i+1,_res,ch/2);
		}else if(gc.stringExtent(line.substring(start, (int) (_end))).x > width && ch> 0.1){
			_res = Math.min(1, res - ch * 0.5);
			return wrap(gc, line, width, start, end,i+1,_res,ch/2);
		}
		return (int) _end;
	}
}
