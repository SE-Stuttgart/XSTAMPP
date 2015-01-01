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

package xstampp.astpa.controlstructure.utilities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * 
 * @author Lukas Balzer
 * 
 * @version 1.0
 * 
 */
public class CSDirectEditor extends TextCellEditor implements ModifyListener,
		MouseMoveListener {

	private int oldLineNumber = 0;
	private List<Integer> lineFeeds = new ArrayList<Integer>();

	/**
	 * 
	 * 
	 * @author Lukas
	 * 
	 * @param composite
	 *            the parent is a CanvasFigure by default
	 */
	public CSDirectEditor(Composite composite) {
		super(composite, SWT.WRAP);
		this.oldLineNumber = this.text.getCaretLineNumber();
		// this.text.addModifyListener(this);

		this.text.addMouseMoveListener(this);

	}

	/**
	 * sets the color of the Text which will be displayed inside the Editor
	 * 
	 * @author Lukas
	 * 
	 * @param foregroundColor
	 *            the color of the text which is displayed
	 */
	public void setTextColor(org.eclipse.swt.graphics.Color foregroundColor) {
		this.text.setForeground(foregroundColor);
	}

	/**
	 * 
	 * @author Lukas
	 * 
	 * @return the Bounds of the textEditor as
	 *         <code>org.eclipse.draw2d.geometry.Rectangle</code>
	 */
	public Rectangle getBounds() {
		Rectangle rect = new Rectangle();
		org.eclipse.swt.graphics.Rectangle textRect = this.text.getBounds();
		rect.setX(textRect.x);
		rect.setY(textRect.y);
		rect.setWidth(textRect.width);
		rect.setHeight(textRect.height);

		return rect;
	}

	/**
	 * 
	 * appends the font of the Label the Editor is going to edit
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param labelFont
	 *            The Font of the CSTextLabel
	 */
	public void setTextFont(Font labelFont) {
		this.text.setFont(labelFont);
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (this.text.getCaretLineNumber() > this.oldLineNumber) {

			this.oldLineNumber = this.text.getCaretLineNumber();
			String temp = this.text.getText();
			int lastSpace = temp.lastIndexOf(' ');

			if (!this.text.getText().endsWith(Text.DELIMITER)) {
				this.lineFeeds.add(lastSpace);
			}

		} else if (this.text.getCaretLineNumber() < this.oldLineNumber) {
			this.oldLineNumber = this.text.getCaretLineNumber();
		}

	}

	@Override
	public void mouseMove(MouseEvent e) {
		if (e.widget instanceof Text) {
			this.text.setCursor(new Cursor(null, SWT.CURSOR_IBEAM));
		}

	}
}