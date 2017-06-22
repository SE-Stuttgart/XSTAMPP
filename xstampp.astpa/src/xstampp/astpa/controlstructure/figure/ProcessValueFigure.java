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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.controlstructure.controller.editparts.ProcessVariableEditPart;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ProcessValueFigure extends CSFigure {

	/**
	 * the offset of the process variables and values
	 * 
	 * @author Lukas Balzer
	 */
	private static final int PROCESS_MODEL_COLUMN = 10;
	private static final int ROW_OFFSET = 4;
	private final int topOffset;
	private boolean autoPositioning;

	/**
	 * @author Lukas Balzer
	 * @param id
	 *            the id which the figure inherits from its model
	 * @param top
	 *            the offset from the parent models text Label
	 */
	public ProcessValueFigure(UUID id, int top) {
		super(id, false);
    this.setAutoPositioning(true);
		this.setOpaque(false);
		this.topOffset = top;
	}

	@Override
	public void refresh() {
	  
		this.getTextField().setLocation(new Point(0,0));
		this.getTextField().setSize(this.getTextField().getPreferredSize(rect.width,-1));
		this.getTextField().revalidate();
		this.setConstraint(this.getTextField(),this.getTextField().getBounds());
		Dimension size = this.getTextField().getSize();
		for (Object child : getChildren()) {
      if(child instanceof IControlStructureFigure) {
        Dimension dimension = ((IControlStructureFigure) child).getBounds().getSize();
        size.height += dimension.height + topOffset;
        size.width += dimension.width;
      }
    }
    rect.setSize(size);
		// the component is drawn right below its previous child
    if(this.isAutoPositioning()) {
      rect.setX(ProcessValueFigure.PROCESS_MODEL_COLUMN);
  		int previousIndex = this.getParent().getChildren().indexOf(this) - 1;
  		if (previousIndex < 0) {
  			rect.setY(((IControlStructureFigure) this.getParent())
  					.getTextField().getBounds().height + this.topOffset);
  		} else {
  			IFigure previousChild = (IFigure) this.getParent().getChildren()
  					.get(previousIndex);
  			rect.setY(previousChild.getBounds().y
  					+ previousChild.getBounds().height
  					+ ProcessValueFigure.ROW_OFFSET);
  			
  		}
    }

		this.getParent().setConstraint(this, rect);
		setBounds(rect);
		this.getTextField().repaint();
	}

	@Override
	public void setDeco(boolean deco) {
		// there's no decoration on process components
	}

  /**
   * @return the autoPositioning
   */
  public boolean isAutoPositioning() {
    return autoPositioning;
  }

  /**
   * @param autoPositioning the autoPositioning to set
   */
  public void setAutoPositioning(boolean autoPositioning) {
    this.autoPositioning = autoPositioning;
  }
}
