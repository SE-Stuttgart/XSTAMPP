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

package xstampp.ui.welcome;

import java.io.File;
import java.util.Observable;

import messages.Messages;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import xstampp.Activator;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.editors.interfaces.IEditorBase;
import xstampp.util.STPAPluginUtils;

/**
 * Welcome page view.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class WelcomeView extends ViewPart implements IEditorBase {

	// private static final String SHOW_WELCOME_ON_STARTUP_PREFERENCES =
	// "SHOW_WELCOME_ON_STARTUP";

	private static final int MAIN_ICONS_COLUMN = 120;
	private static final String NEW_PROJECT_ICON = "/xstampp.intro/graphics/icons/new_s.png"; //$NON-NLS-1$
	private static final String NEW_PROJECT_ICON_HOVERED = "/xstampp.intro/graphics/icons/new_a-s.png"; //$NON-NLS-1$
	private static final String LOAD_PROJECT_ICON = "/xstampp.intro/graphics/icons/load_s.png"; //$NON-NLS-1$
	private static final String LOAD_PROJECT_ICON_HOVERED = "/xstampp.intro/graphics/icons/load_a-s.png"; //$NON-NLS-1$
	private static final String EXIT_ICON = "/xstampp.intro/graphics/icons/exit-s.png"; //$NON-NLS-1$
	private static final String EXIT_ICON_HOVERED = "/xstampp.intro/graphics/icons/exit_a.png"; //$NON-NLS-1$
	private static final String HELP_ICON = "/xstampp.intro/graphics/icons/help_s.png"; //$NON-NLS-1$
	private static final String HELP_ICON_HOVERED = "/xstampp.intro/graphics/icons/help_a-s.png"; //$NON-NLS-1$
	private static final String SETTINGS_ICON = "/xstampp.intro/graphics/icons/preferences_s.png"; //$NON-NLS-1$
	private static final String SETTINGS_ICON_HOVERED = "/xstampp.intro/graphics/icons/preferences_a-s.png"; //$NON-NLS-1$
	private static final String BACKGROUND_IMAGE = "/xstampp.intro/graphics/design/background_recent.png"; //$NON-NLS-1$
	private static final String LOGO_IMAGE = "/xstampp.intro/graphics/design/header.png"; //$NON-NLS-1$
	private static final String CLOSE_IMAGE = "/xstampp.intro/graphics/icons/ToWorkbench.png"; //$NON-NLS-1$
	private static final String CLOSE_IMAGE_HOVERED = "/xstampp.intro/graphics/icons/ToWorkbench_hovered.png"; //$NON-NLS-1$	

	private static Image newProjectImage = null;
	private static Image newProjectImageHovered = null;
	private static Image loadProjectImage = null;
	private static Image loadProjectImageHovered = null;
	private static Image exitImage = null;
	private static Image exitImageHovered = null;
	private static Image backgroundImage = null;
	private static Image logoImage = null;
	private static Image closeImage = null;
	private static Image closeImageHovered = null;
	private static Image settingsImage = null;
	private static Image settingsImageHovered = null;
	private static Image helpImage = null;
	private static Image helpImageHovered = null;

	private static final String NEW_PROJECT_TOOLTIP = Messages.CreateNewProject;
	private static final String LOAD_PROJECT_TOOLTIP = Messages.LoadProject;
	private static final FontData TitleFont = new FontData(
			"Calibri", 16, SWT.NORMAL); //$NON-NLS-1$
	private static final FontData DefaultFont = new FontData(
			"Calibri", 11, SWT.NORMAL); //$NON-NLS-1$
	private RecentProjectLabel[] recentProjectsList;
	private Label quickHelp;

	/**
	 * The ID.
	 */
	public static final String ID = "WelcomeView"; //$NON-NLS-1$

	@Override
	public String getId() {
		return WelcomeView.ID;
	}

	@Override
	public String getTitle() {
		return Messages.WelcomeView;
	}


	/**
	 * adds a list of recent projects to the welcome view,
	 * 
	 * </p><i>the position is fix due to the static background image </i>
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param parent
	 *            the composite on which this component will be shown
	 */
	@SuppressWarnings("unused")
	private void addRecentProjects(Composite parent) {
		Composite recentComp = new Composite(parent, SWT.NONE);
		FormData data = new FormData();
		data.left = new FormAttachment(0, 80);
		data.top = new FormAttachment(0, 286);
		data.width = 227;
		data.height = 350;
		recentComp.setLayoutData(data);
		recentComp.setLayout(new FormLayout());

		Label recentLabel = new Label(recentComp, SWT.NONE);

		recentLabel.setFont(new Font(Display.getCurrent(),
				WelcomeView.TitleFont));
		recentLabel.setText(Messages.RecentProjects);
		recentLabel.setForeground(ColorConstants.white);
		recentLabel.setVisible(true);
		data = new FormData();
		data.top = new FormAttachment(0, 2);
		data.left = new FormAttachment(0, 2);
		data.width = 227;
		data.height = 30;
		recentLabel.setLayoutData(data);

		Label lastLabel = recentLabel;
		RecentProjectLabel manager;
		this.recentProjectsList = new RecentProjectLabel[8];
		for (int i = 0; i < 8; i++) {
			data = new FormData();
			data.top = new FormAttachment(lastLabel, 5);
			data.left = new FormAttachment(0, 10);
			data.width = 200;
			data.height = 30;

			manager = new RecentProjectLabel(recentComp, "", data); //$NON-NLS-1$
			lastLabel = manager.getLabel();
			lastLabel.setLayoutData(data);
			lastLabel.setVisible(false);
			this.recentProjectsList[i] = manager;
		}
	}

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new FormLayout());

		Composite background = new Composite(parent, SWT.NONE);
		GridData layout = new GridData();
		layout.grabExcessHorizontalSpace = true;
		layout.grabExcessVerticalSpace = true;
		background.setLayoutData(layout);

		FormData backgroundFormData = new FormData();
		backgroundFormData.top = new FormAttachment(0);
		backgroundFormData.left = new FormAttachment(0);
		backgroundFormData.width = WelcomeView.getBackgroundImage().getBounds().width;
		backgroundFormData.height = WelcomeView.getBackgroundImage()
				.getBounds().height;
		background.setLayoutData(backgroundFormData);

		background.setLayout(new FormLayout());
		background.setBackgroundImage(WelcomeView.getBackgroundImage());

		parent.setBackground(new Color(null, new RGB(39, 7, 116)));
		Label logo = new Label(background, SWT.PUSH);
		logo.setImage(WelcomeView.getLogoImage());

		FormData logoFormData = new FormData();
		logoFormData.top = new FormAttachment(5);
		logoFormData.left = new FormAttachment(2);
		logoFormData.width = WelcomeView.getLogoImage().getBounds().width;
		logoFormData.height = WelcomeView.getLogoImage().getBounds().height;
		logo.setLayoutData(logoFormData);

		// this.addRecentProjects(background);

		/*
		 * ----Create Quick start
		 * icons----------------------------------------------
		 */
		final Canvas newProject = this
				.addHoveredLabel(
						background,
						WelcomeView.getNewProjectIcon(),
						"Opens a Window where you can choose between all currently installed Project Types",
						new Point(WelcomeView.MAIN_ICONS_COLUMN, 330),
						Messages.CreateNew, "org.eclipse.ui.newWizard"); //$NON-NLS-1$
		newProject.setToolTipText(WelcomeView.NEW_PROJECT_TOOLTIP);

		final Canvas loadProject = this
				.addHoveredLabel(
						background,
						WelcomeView.getLoadProjectIcon(),
						"Opens a file chooser where you can choose a *.haz project on your system",
						new Point(WelcomeView.MAIN_ICONS_COLUMN, 390),
						Messages.LoadExistingProject, "xstampp.command.load"); //$NON-NLS-1$
		loadProject.setToolTipText(WelcomeView.LOAD_PROJECT_TOOLTIP);

		final Canvas help = this
				.addHoveredLabel(
						background,
						WelcomeView.getHelpImage(),
						"Opens the a browser with where you can find help Topics and search for keywords",
						new Point(WelcomeView.MAIN_ICONS_COLUMN, 450),
						Messages.HelpContents,
						"org.eclipse.ui.help.helpContents"); //$NON-NLS-1$
		help.setToolTipText(Messages.HelpToolTip);

		final Canvas settings = this
				.addHoveredLabel(
						background,
						WelcomeView.getSettingsImage(),
						"Opens the Preference Window where you can edit the plugin custumization settings",
						new Point(WelcomeView.MAIN_ICONS_COLUMN, 510),
						Messages.Preferences, "org.eclipse.ui.window.preferences"); //$NON-NLS-1$
		settings.setToolTipText(Messages.PreferencesToolTip);

		final Canvas toWorkbench = this.addHoveredLabel(background, WelcomeView
				.getCloseImage(), "Opens the work perspective immediately",
				new Point(WelcomeView.MAIN_ICONS_COLUMN, 570), "To Workbench",
				"astpa.commands.toWorkbench"); //$NON-NLS-1$
		toWorkbench.setToolTipText(Messages.GoToWorkbench);

		/*
		 * ----adds the interactive help panel which is displayed to the right
		 * of the quickstart Buttons
		 */
		this.quickHelp = new Label(background, SWT.WRAP | SWT.READ_ONLY);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 325);
		data.left = new FormAttachment(newProject, 30);
		data.width = 250;
		data.height = 300;
		this.quickHelp.setLayoutData(data);
		this.quickHelp.setFont(new Font(null, WelcomeView.DefaultFont));

		/*
		 * ---- adds a checkbox with which the user can set the whether or not
		 * to display the welcome view on startup
		 */
		final Button checkBox = new Button(background, SWT.CHECK);
		checkBox.setText(Messages.ShowPageOnStartup);
		checkBox.setToolTipText(Messages.ShowPageOnStartup);
		checkBox.setSelection(Activator.getDefault().getPreferenceStore()
				.getBoolean(WelcomeView.getShowWelcomeOnStartupPreferences()));

		this.getViewSite().getActionBars().getStatusLineManager()
				.setMessage(""); //$NON-NLS-1$

		checkBox.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent e) {
				// intentionally empty
			}

			@Override
			public void mouseExit(MouseEvent e) {
				WelcomeView.this.getViewSite().getActionBars()
						.getStatusLineManager().setMessage(""); //$NON-NLS-1$
			}

			@Override
			public void mouseEnter(MouseEvent e) {
				WelcomeView.this.getViewSite().getActionBars()
						.getStatusLineManager()
						.setMessage(checkBox.getToolTipText());
			}
		});

		boolean state = Activator.getDefault().getPreferenceStore()
				.getBoolean(WelcomeView.getShowWelcomeOnStartupPreferences());

		checkBox.setSelection(state);
		checkBox.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// store new state in preferences
				Activator
						.getDefault()
						.getPreferenceStore()
						.setValue(
								WelcomeView
										.getShowWelcomeOnStartupPreferences(),
								checkBox.getSelection());
			}
		});

		FormData checkBoxFormData = new FormData();
		checkBoxFormData.left = new FormAttachment(2);
		checkBoxFormData.right = new FormAttachment(15);
		checkBoxFormData.top = new FormAttachment(100);
		checkBoxFormData.bottom = new FormAttachment(47);
		checkBox.setLayoutData(checkBoxFormData);
	}

	private Canvas addHoveredLabel(Composite parent, final Image icon,
			final String helpText, Point relativePosition, final String text,
			final String command) {
		final Canvas newLabel = new Canvas(parent, SWT.NONE);
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, relativePosition.x);
		formData.top = new FormAttachment(0, relativePosition.y);
		formData.width = 230;
		formData.height = icon.getBounds().height;
		newLabel.setLayoutData(formData);

		newLabel.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(icon, 0, 0);
				e.gc.setFont(new Font(null, WelcomeView.TitleFont));
				e.gc.drawString(text, icon.getBounds().width + 10,
						(icon.getBounds().height / 2) - 12, true);

			}
		});

		newLabel.addMouseTrackListener(new MouseTrackAdapter() {

			@Override
			public void mouseExit(MouseEvent e) {
				newLabel.setBackground(null);
				WelcomeView.this.getViewSite().getActionBars()
						.getStatusLineManager().setMessage(""); //$NON-NLS-1$
				WelcomeView.this.quickHelp.setText(""); //$NON-NLS-1$
			}

			@Override
			public void mouseEnter(MouseEvent e) {
					newLabel.setBackground(ColorConstants.lightBlue);
				WelcomeView.this.getViewSite().getActionBars()
						.getStatusLineManager()
						.setMessage(newLabel.getToolTipText());
				WelcomeView.this.quickHelp.setText(helpText);

			}
		});

		newLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

				if (!command.equals("")) { //$NON-NLS-1$
					STPAPluginUtils.executeCommand(command);
				}
			}
		});
		return newLabel;
	}
	@SuppressWarnings("unused")
	private Label addHoveredButton(Composite parent, final Image icon,
			final String helpText, Point relativePosition, final Image hoveredIcon,
			final String command) {
		final Label newLabel = new Label(parent, SWT.NONE);
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, relativePosition.x);
		formData.top = new FormAttachment(0, relativePosition.y);
		formData.width = icon.getBounds().height;
		formData.height = icon.getBounds().height;
		newLabel.setLayoutData(formData);
		newLabel.setImage(icon);
		newLabel.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
//				e.gc.drawImage(icon, 0, 0);
			}
		});

		newLabel.addMouseTrackListener(new MouseTrackAdapter() {

			@Override
			public void mouseExit(MouseEvent e) {
				newLabel.setImage(icon);
				WelcomeView.this.getViewSite().getActionBars()
						.getStatusLineManager().setMessage(""); //$NON-NLS-1$
				WelcomeView.this.quickHelp.setText(""); //$NON-NLS-1$
			}

			@Override
			public void mouseEnter(MouseEvent e) {
					newLabel.setImage(hoveredIcon);
				WelcomeView.this.getViewSite().getActionBars()
						.getStatusLineManager()
						.setMessage(newLabel.getToolTipText());
				WelcomeView.this.quickHelp.setText(helpText);

			}
		});

		newLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

				if (!command.equals("")) { //$NON-NLS-1$
					STPAPluginUtils.executeCommand(command);
				}
			}
		});
		return newLabel;
	}

	/**
	 * The String of the variable in the prefernces, in which the boolean is
	 * stored that controls whether the welcome page is shown on startup.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the String of the variable in the prefernces.
	 */
	public static String getShowWelcomeOnStartupPreferences() {
		return IPreferenceConstants.SHOW_WELCOME_ON_STARTUP_PREFERENCES;
	}


	@SuppressWarnings("unused")
	private void updateRecentProjects() {
		RecentProjectLabel newRecentEntry;
		String recentProjectsTest = Activator.getDefault().getPreferenceStore()
				.getString(IPreferenceConstants.RECENT_PROJECTS);
		String[] projects = recentProjectsTest.split(";"); //$NON-NLS-1$

		for (int i = 0; i < 8; i++) {
			if ((i < projects.length) && !projects[i].equals("")) { //$NON-NLS-1$
				final String temp = projects[i];
				newRecentEntry = this.recentProjectsList[i];
				newRecentEntry.setLink(temp);
				newRecentEntry.getLabel().setVisible(true);

			} else {
				this.recentProjectsList[i].getLabel().setVisible(false);
			}
		}
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		// intentionally empty
	}

	/**
	 * Get the image for the new project button.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the new project button.
	 */
	public static final Image getNewProjectIcon() {
		if (WelcomeView.newProjectImage == null) {
			WelcomeView.newProjectImage = Activator.getImageDescriptor(
					WelcomeView.NEW_PROJECT_ICON).createImage();
		}

		return WelcomeView.newProjectImage;
	}

	/**
	 * Get the hovered image for the new project button.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the new project button.
	 */
	public static final Image getNewProjectIconHovered() {
		if (WelcomeView.newProjectImageHovered == null) {
			WelcomeView.newProjectImageHovered = Activator.getImageDescriptor(
					WelcomeView.NEW_PROJECT_ICON_HOVERED).createImage();
		}

		return WelcomeView.newProjectImageHovered;
	}

	/**
	 * Get the image for the load project button.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the load button.
	 */
	public static final Image getLoadProjectIcon() {
		if (WelcomeView.loadProjectImage == null) {
			WelcomeView.loadProjectImage = Activator.getImageDescriptor(
					WelcomeView.LOAD_PROJECT_ICON).createImage();
		}

		return WelcomeView.loadProjectImage;
	}

	/**
	 * Get the hovered image for the load project button.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the load button.
	 */
	public static final Image getLoadProjectIconHovered() {
		if (WelcomeView.loadProjectImageHovered == null) {
			WelcomeView.loadProjectImageHovered = Activator.getImageDescriptor(
					WelcomeView.LOAD_PROJECT_ICON_HOVERED).createImage();
		}

		return WelcomeView.loadProjectImageHovered;
	}

	/**
	 * Get the image for the exit button.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the add button.
	 */
	public static final Image getExitIcon() {
		if (WelcomeView.exitImage == null) {
			WelcomeView.exitImage = Activator.getImageDescriptor(
					WelcomeView.EXIT_ICON).createImage();
		}

		return WelcomeView.exitImage;
	}

	/**
	 * Get the hovered image for the exit button.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the exit button.
	 */
	public static final Image getExitIconHovered() {
		if (WelcomeView.exitImageHovered == null) {
			WelcomeView.exitImageHovered = Activator.getImageDescriptor(
					WelcomeView.EXIT_ICON_HOVERED).createImage();
		}

		return WelcomeView.exitImageHovered;
	}

	/**
	 * Get the background image.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the background.
	 */
	public static final Image getBackgroundImage() {
		if (WelcomeView.backgroundImage == null) {
			WelcomeView.backgroundImage = Activator.getImageDescriptor(
					WelcomeView.BACKGROUND_IMAGE).createImage();
		}

		return WelcomeView.backgroundImage;
	}

	/**
	 * Get the logo image.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the logo.
	 */
	public static final Image getLogoImage() {
		if (WelcomeView.logoImage == null) {
			WelcomeView.logoImage = Activator.getImageDescriptor(
					WelcomeView.LOGO_IMAGE).createImage();
		}

		return WelcomeView.logoImage;
	}

	/**
	 * Get the close image.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the close image.
	 */
	public static final Image getCloseImage() {
		if (WelcomeView.closeImage == null) {
			WelcomeView.closeImage = Activator.getImageDescriptor(
					WelcomeView.CLOSE_IMAGE).createImage();
		}

		return WelcomeView.closeImage;
	}

	/**
	 * Get the hovered close image.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the hovered close image.
	 */
	public static final Image getCloseImageHovered() {
		if (WelcomeView.closeImageHovered == null) {
			WelcomeView.closeImageHovered = Activator.getImageDescriptor(
					WelcomeView.CLOSE_IMAGE_HOVERED).createImage();
		}

		return WelcomeView.closeImageHovered;
	}

	/**
	 * Get the settings image.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the settings image.
	 */
	public static final Image getSettingsImage() {
		if (WelcomeView.settingsImage == null) {
			WelcomeView.settingsImage = Activator.getImageDescriptor(
					WelcomeView.SETTINGS_ICON).createImage();
		}

		return WelcomeView.settingsImage;
	}

	/**
	 * Get the hovered settings image.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the hovered settings image.
	 */
	public static final Image getSettingsImageHovered() {
		if (WelcomeView.settingsImageHovered == null) {
			WelcomeView.settingsImageHovered = Activator.getImageDescriptor(
					WelcomeView.SETTINGS_ICON_HOVERED).createImage();
		}

		return WelcomeView.settingsImageHovered;
	}

	/**
	 * Get the help image.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the help image.
	 */
	public static final Image getHelpImage() {
		if (WelcomeView.helpImage == null) {
			WelcomeView.helpImage = Activator.getImageDescriptor(
					WelcomeView.HELP_ICON).createImage();
		}

		return WelcomeView.helpImage;
	}

	/**
	 * Get the hovered help image.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the hovered help image.
	 */
	public static final Image getHelpImageHovered() {
		if (WelcomeView.helpImageHovered == null) {
			WelcomeView.helpImageHovered = Activator.getImageDescriptor(
					WelcomeView.HELP_ICON_HOVERED).createImage();
		}

		return WelcomeView.helpImageHovered;
	}

	/**
	 * 
	 *
	 * @author Lukas Balzer
	 * @since 1.0
	 */
	private class RecentProjectLabel implements MouseListener,
			MouseTrackListener {
		@SuppressWarnings("unused")
		private String link;
		private Label label;

		RecentProjectLabel(Composite parent, String path, FormData data) {

			this.label = new Label(parent, SWT.NONE);
			this.link = path;
			Font recentFont = new Font(Display.getCurrent(),
					WelcomeView.DefaultFont);

			this.label.setFont(recentFont);
			this.label.setLayoutData(data);
			this.label.addMouseTrackListener(this);
			this.label.addMouseListener(this);
		}

		public Label getLabel() {
			return this.label;
		}

		public void setLink(String link) {
			this.link = link;
			int pathSplitter = Math.max(0, link.lastIndexOf(File.separator));
			this.label.setText(link.substring(pathSplitter));
			this.label.setToolTipText(Messages.LoadProject + ": " + link); //$NON-NLS-1$

		}

		@Override
		public void mouseHover(MouseEvent e) {
			//
		}

		@Override
		public void mouseExit(MouseEvent e) {
			this.label.setBackground(null);
		}

		@Override
		public void mouseEnter(MouseEvent e) {
			this.label.setBackground(ColorConstants.lightBlue);
		}

		@Override
		public void mouseDoubleClick(MouseEvent arg0) {
			// Do nothing on this event

		}

		@Override
		public void mouseDown(MouseEvent e) {
			// Map<String,String> values= new HashMap<>();
			//			values.put("loadRecentProject",this.link); //$NON-NLS-1$ //$NON-NLS-2$
			// STPAPluginUtils.executeParaCommand("astpa.load",values);
		}

		@Override
		public void mouseUp(MouseEvent e) {
			// Do nothing on this event

		}

	}

	@Override
	public void setFocus() {
		//methode call not used in this implementation of IWorkbenchPart
	}

}
