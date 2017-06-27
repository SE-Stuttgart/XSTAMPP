/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.controlstructure.controller.editparts;

import java.util.UUID;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

/**
 * a part that implement this interface can be related to a IRelative they can thereby contribute a
 * Feedback to the ui
 * 
 * @author Lukas Balzer
 * @since 2.0.2
 *
 */
public interface IMemberEditPart extends IControlStructureEditPart {

  IFigure getFeedback();

  UUID getRelativeId();

  Color getFeedbackColor();
}
