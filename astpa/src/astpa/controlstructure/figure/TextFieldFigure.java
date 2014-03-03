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

package astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

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
	public static final Dimension TEXTBOX_FIGURE_DEFSIZE = new Dimension(100, 15);
	
	
	/**
	 * @author Lukas Balzer
	 * @param id the id which the figure inherits from its model
	 */
	public TextFieldFigure(UUID id) {
		super(id);
		this.setOpaque(false);
		this.setBorder(null);
	}
	
	/**
	 * This method is called when the Layout of the Model changes. It calls
	 * setConstraint() in the Parent Figure to change the Layout Constraint of
	 * this Figure relatively to it.
	 * 
	 * @param rect describes the proportions of the changed layout
	 * @author Lukas Balzer
	 */
	@Override
	public void setLayout(Rectangle rect) {
		
		int width = this.getBounds().width;
		
		getTextField().setSize(getTextField().getPreferredSize(width, -1));
		getTextField().repaint();
		// the height of the rectangle is set to the ideal height for the given
		// width
		rect.height = getTextField().getPreferredSize(width, -1).height;
		
		super.setLayout(rect);
		getTextField().repaint();
	}
	
}
