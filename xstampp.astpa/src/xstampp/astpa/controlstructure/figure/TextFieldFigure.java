/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.apache.bcel.generic.NEW;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;


/**
 * 
 * TextFieldView
 * 
 * @version 1.0
 * @author Lukas
 * 
 */
public class TextFieldFigure extends CSFigure {

	/**
	 * TEXTBOX_FIGURE_DEFSIZE is the default size to which the layout is set
	 * when the user sets the Component from the palate without defining actual
	 * bounds
	 */
	public static final Dimension TEXTBOX_FIGURE_DEFSIZE = new Dimension(100,
			15);
	private boolean freeWidth;
	private boolean freeHeight;

	
	/**
	 * @author Lukas Balzer
	 * @param id
	 *            the id which the figure inherits from its model
	 */
	public TextFieldFigure(UUID id) {
		this(id, true,false);
	}


	
	public TextFieldFigure(UUID id, boolean freeWidth, boolean freeHeight) {
		super(id, false);
		this.freeWidth = freeWidth;
		this.freeHeight = freeHeight;
	}



	@Override
	public void refresh() {
	 	if(isDirty){
	 		this.isDirty = false;
			this.getTextField().setText(getText());
			int width = -1;
			int height = -1;
			if(freeWidth){
				width = rect.width;
			}
			if(freeHeight){
				height = rect.height;
			}
			getTextField().setSize(this.getTextField().getPreferredSize(width,-1));
			this.setConstraint(this.getTextField(), getTextField().getBounds());
			rect = new Rectangle(rect.getLocation(), this.getTextField().getSize());
			if(freeWidth){
				rect.width = width;
			}
			if(freeHeight){
				rect.height = height;
			}
			this.getParent().setConstraint(this, rect);
			this.setBounds(rect);
			for (Object child : getChildren()) {
				if(child instanceof IControlStructureFigure){
					((IControlStructureFigure) child).refresh();
				}
			}
	 	}
	}

	@Override
	public void setDeco(boolean deco) 
	{
		//the text field is not decorated
	}

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param dashed whether the component is dashed or not
	 */
	public void setDashed() {
		setBorder(new LineBorder(ColorConstants.black, 1, SWT.BORDER_DASH){
			@Override
			public int getStyle() {
				return SWT.LINE_CUSTOM;
			}
			@Override
			public void paint(IFigure figure, Graphics graphics,
					Insets insets) {
				graphics.setLineStyle(SWT.LINE_CUSTOM);
				graphics.setLineDashOffset(4);
				graphics.setLineDash(new int[]{4});
				super.paint(figure, graphics, insets);
			}
		});
	}
}
