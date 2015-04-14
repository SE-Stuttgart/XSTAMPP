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

package xstampp.astpa.controlstructure.figure;

import java.util.UUID;

import org.apache.fop.render.txt.border.DashedBorderElement;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
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

	private boolean isDashed;
	/**
	 * @author Lukas Balzer
	 * @param id
	 *            the id which the figure inherits from its model
	 */
	public TextFieldFigure(UUID id) {
		super(id);
		this.isDashed = false;
		this.setOpaque(false);
		this.getTextField().setOpaque(false);
	}

	/**
	 * This method is called when the Layout of the Model changes. It calls
	 * setConstraint() in the Parent Figure to change the Layout Constraint of
	 * this Figure relatively to it.
	 * 
	 * @param rect
	 *            describes the proportions of the changed layout
	 * @author Lukas Balzer
	 */
	@Override
	public void setLayout(Rectangle rect) {

		int width = this.getBounds().width;

		this.getTextField().setSize(
				this.getTextField().getPreferredSize(width, -1));
		this.getTextField().repaint();
		// the height of the rectangle is set to the ideal height for the given
		// width
		if(!this.isDashed){
			rect.height = this.getTextField().getPreferredSize(width, -1).height;
		}

		super.setLayout(rect);
		this.getTextField().repaint();
	}

	@Override
	public void setDeco(boolean deco) 
	{
		if(deco){
			this.isDashed = true;
			setBorder(new LineBorder(ColorConstants.black, 1, SWT.BORDER_DASH)); 
		}
	}

}
