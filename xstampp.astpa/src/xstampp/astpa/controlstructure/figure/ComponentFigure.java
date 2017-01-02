/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * 
 * Class contains necessary functions for components figures
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class ComponentFigure extends CSFigure  implements IPropertyChangeListener{
	/**
	 * COMPONENT_FIGURE_DEFWIDTH is the default width to which the layout is set
	 * when the user sets the Component from the palate without defining actual
	 * bounds
	 */
	public static final Dimension COMPONENT_FIGURE_DEFSIZE = new Dimension(120,
			40);
	private Color decoBorderColor;
	private String colorPreference;
	private boolean isDecorated;
	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * @param id
	 *            the id which the figure inherits from its model
	 * @param isDashed TODO
	 * 
	 */
	public ComponentFigure(UUID id, Boolean isDashed) {

		super(id, isDashed);
		this.setForegroundColor(ColorConstants.black);
		this.decoBorderColor = CSFigure.STANDARD_BORDER_COLOR;
	}

	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * @param id
	 *            the id which the figure inherits from its model
	 * @param img
	 *            his Image will be displayed in the upper left corner of the
	 *            component
	 * @param color
	 *            the Color of the Border
	 * 
	 */
	public ComponentFigure(UUID id, Image img, String colorPreference) {

		super(id, img, false);
		setCanConnect(true);
		this.setForegroundColor(ColorConstants.black);
		this.colorPreference=colorPreference;
		this.decoBorderColor =  CSFigure.STANDARD_BORDER_COLOR;
		this.setDeco(true);
		setBackgroundColor(ColorConstants.white);
	}

	
	@Override
	public void setDeco(boolean deco) {
		this.isDecorated=deco;
		this.setDecoration(deco);
		if (deco) {
			this.setBorder(this.decoBorderColor);
		} else {
			this.setBorder(CSFigure.STANDARD_BORDER_COLOR);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if(arg0.getProperty().equals(this.colorPreference) && getPreferenceStore() != null){
			this.decoBorderColor =  new Color(Display.getCurrent(), PreferenceConverter
											  .getColor(getPreferenceStore(), this.colorPreference));
			this.setDeco(this.isDecorated);
			this.repaint();
		}
	}

	@Override
	public void setPreferenceStore(IPreferenceStore store) {
		store.addPropertyChangeListener(this);
		if(this.colorPreference != null){
			this.decoBorderColor =  new Color(Display.getCurrent(), PreferenceConverter
					.getColor(store, colorPreference));
			this.setDeco(this.isDecorated);
		}
		super.setPreferenceStore(store);
	}
}
