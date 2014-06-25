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


import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

/**
 * A cell to display text.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridCellText extends GridCellComposite{
	
	private Text text;
	private Composite textComp;
	private static final Color TEXT_COLOR = new Color(Display.getCurrent(), 0, 0, 0);
	
	
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public GridCellText() {
		this(null,"NONE");
	}
	
	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser, Benedikt Markt
	 * 
	 * @param text the intial text in the cell.
	 * 
	 */
	public GridCellText(GridWrapper grid,String text) {
		super(grid,SWT.FILL);
		this.textComp= new Composite(this, SWT.FILL);
		this.textComp.setLayout(new FillLayout(SWT.HORIZONTAL));
		this.text= new Text(this.textComp, SWT.WRAP);
		this.text.setText(text);
		this.text.pack();
		this.textComp.setVisible(true);
		this.text.setEditable(false);
	}
	
	@Override
	public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
//		super.paint(renderer, gc, item);
//		Color bgColor = gc.getBackground();
//		
//		Rectangle bounds = renderer.getDrawBounds();
//		
//		gc.setBackground(this.getBackgroundColor(renderer, gc));
//		
		Color fgColor = gc.getForeground();
		gc.setForeground(TEXT_COLOR);
		this.textComp.setBounds(0,0,renderer.getDrawBounds().width,renderer.getDrawBounds().height);
		this.text.setBackground(this.getBackgroundColor(renderer, gc));
		this.setBounds(renderer.getDrawBounds());
		
//		for(int i=0;i<lines.length;i++){
//			gc.drawString(lines[i], bounds.x + 2,
//					      bounds.y+ i*this.text.getLineHeight());
//			System.out.println(i);
//		}
		
		// restore bg color
//		gc.setBackground(bgColor);
		// restore fg color
//		gc.setForeground(fgColor);
	}
	
	protected void setText(String text) {
		this.text.setText(text);
	}
	
	@Override
	public void cleanUp() {
		// intentionally empty
	}
	
	@Override
	public int getPreferredHeight() {
		return AbstractGridCell.DEFAULT_CELL_HEIGHT;
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
