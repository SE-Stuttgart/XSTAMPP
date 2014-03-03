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

package astpa.ui.common;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import astpa.Activator;
import astpa.preferences.IPreferenceConstants;

/**
 * Splitter thats splits the different steps of the STPA analysis in the
 * navigation
 * 
 * @author Fabian Toth, Sebastian Sieber
 */
public class ViewTitle extends Canvas {
	
	private String text;
	
	
	/**
	 * Constructor of the splitter
	 * 
	 * @author Fabian Toth
	 * 
	 * @param parent the parent composite
	 * @param style the style
	 * @param text the text that will be shown
	 */
	public ViewTitle(Composite parent, int style, String text) {
		super(parent, style);
		this.text = text;
		this.addPaintListener(new ViewTitlePaintListener(this));
	}
	
	/**
	 * Getter for the text of the splitter
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the text of the splitter
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Setter for the text
	 * 
	 * @author Fabian Toth
	 * 
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
		this.redraw();
	}
}

class ViewTitlePaintListener implements PaintListener {
	
	private static final int FONT_SIZE = 11;
	private static final int TEXT_POSITION_X = 5;
	private static final int TEXT_POSITION_Y = 5;
	
	private ViewTitle viewTitle;
	
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	
	
	public ViewTitlePaintListener(ViewTitle viewTitle) {
		this.viewTitle = viewTitle;
	}
	
	@Override
	public void paintControl(PaintEvent e) {
		Rectangle clientArea = this.viewTitle.getClientArea();
		Display display = Display.getCurrent();
		e.gc.setFont(new Font(display, "Arial", //$NON-NLS-1$
			ViewTitlePaintListener.FONT_SIZE, SWT.BOLD));
		e.gc.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(this.store,
			IPreferenceConstants.SPLITTER_BACKGROUND)));
		e.gc.setForeground(new Color(Display.getCurrent(), PreferenceConverter.getColor(this.store,
			IPreferenceConstants.SPLITTER_FOREGROUND)));
		e.gc.fillGradientRectangle(0, 0, clientArea.width, clientArea.height, false);
		e.gc.setForeground(new Color(Display.getCurrent(), PreferenceConverter.getColor(this.store,
			IPreferenceConstants.SPLITTER_FONT)));
		e.gc.drawText(this.viewTitle.getText(), ViewTitlePaintListener.TEXT_POSITION_X,
			ViewTitlePaintListener.TEXT_POSITION_Y, true);
	}
}
