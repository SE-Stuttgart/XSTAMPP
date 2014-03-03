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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ProcessFigure extends CSFigure {
	
	/**
	 * the offset of the process variables and values
	 * 
	 * @author Lukas Balzer
	 */
	private static final int PROCESS_MODEL_COLUMN = 10;
	
	private final int topOffset;
	
	
	/**
	 * @author Lukas Balzer
	 * @param id the id which the figure inherits from its model
	 * @param top the offset from the parent models text Label
	 */
	public ProcessFigure(UUID id, int top) {
		super(id);
		
		this.setOpaque(false);
		this.setBorder(null);
		this.topOffset = top;
	}
	
	@Override
	public void setLayout(Rectangle rect) {
		rect.setX(ProcessFigure.PROCESS_MODEL_COLUMN);
		
		rect.setWidth(this.getParent().getBounds().width - (2 * ProcessFigure.PROCESS_MODEL_COLUMN)
			- CSFigure.CENTER_COMPENSATION);
		this.getTextField().setSize(this.getBounds().width, this.getTextField().getTextBounds().getSize().height);
		this.getTextField().revalidate();
		this.setConstraint(this.getTextField(), new Rectangle(1, 1, rect.width, -1));
		// the component is drawn right below its previous child
		int previousIndex = this.getParent().getChildren().indexOf(this) - 1;
		if (previousIndex < 0) {
			rect.setY(((IControlStructureFigure) this.getParent()).getTextField().getBounds().height + this.topOffset);
		} else {
			IFigure previousChild = (IFigure) this.getParent().getChildren().get(previousIndex);
			rect.setY(previousChild.getBounds().y + previousChild.getBounds().height);
		}
		
		this.getParent().setConstraint(this, rect);
		this.getTextField().repaint();
	}
	
}
