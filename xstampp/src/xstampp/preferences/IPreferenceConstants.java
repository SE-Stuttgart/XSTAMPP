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

package xstampp.preferences;

/**
 * Interface to define all constants related to the preferences of this package.
 * 
 * @author Sebastian Sieber,Lukas Balzer
 * 
 */
public interface IPreferenceConstants {

	// Colors
	/**
	 * 
	 * @author Lukas Balzer
	 */
	String USE_NAVIGATION_COLORS="usenavigationcolors"; //$NON-NLS-1$
	/**
	 * Constant for the navigation item selected color.
	 */
	String NAVIGATION_ITEM_SELECTED = "navigationItemSelectedColor"; //$NON-NLS-1$

	/**
	 * Constant for the navigation item unselected color.
	 */
	String NAVIGATION_ITEM_UNSELECTED = "navigationItemUnselectedColor"; //$NON-NLS-1$

	/**
	 * Constant for the hover item color.
	 */
	String HOVER_ITEM = "hoverItemColor"; //$NON-NLS-1$

	/**
	 * Constant for the splitter foreground color.
	 */
	String SPLITTER_FOREGROUND = "splitterForegroundColor"; //$NON-NLS-1$

	/**
	 * Constant for the splitter background color.
	 */
	String SPLITTER_BACKGROUND = "splitterBackgroundColor"; //$NON-NLS-1$

	/**
	 * Constant for the splitter font color.
	 */
	String CONTROLSTRUCTURE_FONT_COLOR = "splitterFontColor"; //$NON-NLS-1$

	// Fonts
	/**
	 * Constant for the navigation title font.
	 */
	String NAVIGATION_TITLE_FONT = "navigationTitleFont"; //$NON-NLS-1$

	/**
	 * Constant for the navigation text font.
	 */
	String NAVIGATION_TEXT_FONT = "navigationTextFont"; //$NON-NLS-1$

	/**
	 * Constant for the view title font.
	 */
	String VIEW_TITLE_FONT = "viewTitleFont"; //$NON-NLS-1$

	/**
	 * Constant for the default font.
	 */
	String DEFAULT_FONT = "defaultFont"; //$NON-NLS-1$

	/**
	 * Constant for the company name.
	 */
	String COMPANY_NAME = "companyName"; //$NON-NLS-1$

	/**
	 * Constant for the company logo.
	 */
	String COMPANY_LOGO = "companyLogo"; //$NON-NLS-1$

	/**
	 * Constant for the company background color.
	 */
	String COMPANY_BACKGROUND_COLOR = "companyBackgroundColor"; //$NON-NLS-1$

	/**
	 * Constant for the company font color.
	 */
	String COMPANY_FONT_COLOR = "companyFontColor"; //$NON-NLS-1$

	/**
	 * Constant for a List with all recent projects
	 */
	String RECENT_PROJECTS = "recentProjects";

	/**
	 * Constant for show welcome screen on startup option.
	 */
	String SHOW_WELCOME_ON_STARTUP_PREFERENCES = "SHOW_WELCOME_ON_STARTUP"; //$NON-NLS-1$

	/**
	 * Constant for link to update the application.
	 */
	String UPDATE_LINK = "updateLink"; //$NON-NLS-1$

	/**
	 * Constant for the project name.
	 */
	String PROJECT_NAME = "projectName"; //$NON-NLS-1$

	/**
	 * 
	 * Constant for storing the last accessed workspace path
	 */
	String WS_LAST_LOCATION = "lastWSLocation"; //$NON-NLS-1$

	/**
	 * Constant for storing a list decoded as string with recent workspaces
	 */
	String WS_RECENT = "recentWSLocations"; //$NON-NLS-1$

	/**
	 * Constant that stored the decision whether the last workspace should be
	 * used or if the user should be ask every time the program starts
	 */
	String WS_REMEMBER = "remember_WS"; //$NON-NLS-1$

}
