/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.controlstructure.figure;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class PaletteDropDownFigure extends Figure {

  public PaletteDropDownFigure() {
    
    addMouseMotionListener(new MouseMotionListener() {
      
      @Override
      public void mouseMoved(MouseEvent me) {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public void mouseHover(MouseEvent me) {
        System.out.println("hover");
      }
      
      @Override
      public void mouseExited(MouseEvent me) {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public void mouseEntered(MouseEvent me) {
        // TODO Auto-generated method stub
        
      }
      
      @Override
      public void mouseDragged(MouseEvent me) {
        // TODO Auto-generated method stub
        
      }
    });
  }

  @Override
  protected void paintFigure(Graphics graphics) {
    Rectangle bounds = getBounds();
    graphics.setBackgroundColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
    graphics.setAntialias(SWT.ON);
    List<?> children = getParent().getChildren();
    Rectangle parentBnds = ((IFigure) children.get(0)).getClientArea();
    graphics.fillOval(bounds.x + parentBnds.width - 20, bounds.y + (bounds.height / 2) - 10, 20,
        20);
    super.paintFigure(graphics);
  }

}
