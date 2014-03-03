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

package astpa.ui.navigation;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import astpa.Activator;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.common.ViewContainer;

/**
 * A item of the navigation list
 * 
 * @author Fabian Toth, Sebastian Sieber
 * 
 */
public class NavigationItem extends Composite {
	
	private int style;
	private String imageId;
	private String text;
	private NavigationView navigationView;
	private String viewId;
	private boolean active;
	
	
	/**
	 * Constructor of a navigation item
	 * 
	 * @author Fabian Toth
	 * 
	 * @param parent the parent composite
	 * @param style the style in which the components should be shown
	 * @param imageId the ID for the image
	 * @param text the text for the link
	 * @param navigationView the navigation View to which this item belongs
	 * @param viewId the ID of the view which this item should open
	 */
	public NavigationItem(Composite parent, int style, String imageId, String text, NavigationView navigationView,
		String viewId) {
		super(parent, style);
		this.style = style;
		this.imageId = imageId;
		this.text = text;
		this.navigationView = navigationView;
		this.viewId = viewId;
		this.active = false;
		this.initializeLayout();
		this.initializeLink();
	}
	
	/**
	 * Sets this item to inactive
	 * 
	 * @author Fabian Toth
	 */
	public void setInactive() {
		this.active = false;
	}
	
	/**
	 * Sets this item to active
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public void setActive() {
		this.active = true;
	}
	
	/**
	 * Checks if the item is active
	 * 
	 * @author Fabian Toth
	 * 
	 * @return whether the item is active or not
	 */
	public boolean getActive() {
		return this.active;
	}
	
	/**
	 * Creates and sets the layout of this navigation item
	 * 
	 * @author Fabian Toth
	 * 
	 */
	private void initializeLayout() {
		FillLayout layout = new FillLayout();
		layout.type = SWT.VERTICAL;
		this.setLayout(layout);
	}
	
	/**
	 * Creates the ImageHyperlink of this navigation item
	 * 
	 * @author Fabian Toth
	 * 
	 */
	private void initializeLink() {
		ImageHyperlink link = new ImageHyperlink(this, this.style);
		link.setText(this.text);
		link.setImage(NavigationUtil.getInstance().getImage(this.imageId));
		link.addMouseTrackListener(new NavigationMouseTrackListener(this));
		link.addMouseListener(new NavigationMouseListener(this));
	}
	
	/**
	 * Getter for the navigation View that contains this item
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the navigation View
	 */
	public NavigationView getNavigationView() {
		return this.navigationView;
	}
	
	/**
	 * Gets the Id of the view that should be opened
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the Id of the view
	 */
	public String getViewId() {
		return this.viewId;
	}
}

/**
 * The mouse Track listener for the navigation
 * 
 * @author Fabian Toth
 * 
 */
class NavigationMouseTrackListener implements MouseTrackListener {
	
	private NavigationItem navigationItem;
	
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	
	
	public NavigationMouseTrackListener(NavigationItem item) {
		this.navigationItem = item;
	}
	
	@Override
	public void mouseEnter(MouseEvent e) {
		if (!this.navigationItem.getActive()) {
			this.navigationItem.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(this.store,
				IPreferenceConstants.HOVER_ITEM)));
		}
	}
	
	@Override
	public void mouseExit(MouseEvent e) {
		
		if (!this.navigationItem.getActive()) {
			this.navigationItem.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(this.store,
				IPreferenceConstants.NAVIGATION_ITEM_UNSELECTED)));
		} else {
			this.navigationItem.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(this.store,
				IPreferenceConstants.NAVIGATION_ITEM_SELECTED)));
		}
	}
	
	@Override
	public void mouseHover(MouseEvent e) {
		if (!this.navigationItem.getActive()) {
			this.navigationItem.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(this.store,
				IPreferenceConstants.HOVER_ITEM)));
		}
	}
}

/**
 * The mouse listener for the Navigation
 * 
 * @author Fabian Toth
 * 
 */
class NavigationMouseListener implements MouseListener {
	
	private NavigationItem navigationItem;
	
	
	public NavigationMouseListener(NavigationItem item) {
		this.navigationItem = item;
	}
	
	@Override
	public void mouseUp(MouseEvent e) {
		// Nothing to do here
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		ViewContainer container =
			(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ViewContainer.ID);
		container.activateView(this.navigationItem.getViewId());
		this.navigationItem.getNavigationView().setItemActive(this.navigationItem);
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// Nothing to do here
	}
	
}
