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

package astpa.ui.welcome;

import java.util.Observable;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.services.ISourceProviderService;

import astpa.Activator;
import astpa.model.interfaces.IDataModel;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.common.IViewBase;
import astpa.ui.common.ViewContainer;
import astpa.ui.menu.file.commands.SaveState;
import astpa.ui.menu.file.commands.Welcome;

/**
 * Welcome page view.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class WelcomeView implements IViewBase {
	
	// private static final String SHOW_WELCOME_ON_STARTUP_PREFERENCES =
	// "SHOW_WELCOME_ON_STARTUP";
	
	private static final String NEW_PROJECT_ICON = "/astpa.intro/graphics/icons/new.png"; //$NON-NLS-1$
	private static final String NEW_PROJECT_ICON_HOVERED = "/astpa.intro/graphics/icons/new_a.png"; //$NON-NLS-1$
	private static final String LOAD_PROJECT_ICON = "/astpa.intro/graphics/icons/load.png"; //$NON-NLS-1$
	private static final String LOAD_PROJECT_ICON_HOVERED = "/astpa.intro/graphics/icons/load_a.png"; //$NON-NLS-1$
	private static final String EXIT_ICON = "/astpa.intro/graphics/icons/exit.png"; //$NON-NLS-1$
	private static final String EXIT_ICON_HOVERED = "/astpa.intro/graphics/icons/exit_a.png"; //$NON-NLS-1$
	private static final String BACKGROUND_IMAGE = "/astpa.intro/graphics/design/background.png"; //$NON-NLS-1$
	private static final String LOGO_IMAGE = "/astpa.intro/graphics/design/header.png"; //$NON-NLS-1$
	private static final String CLOSE_IMAGE = "/icons/buttons/commontables/remove.png"; //$NON-NLS-1$
	private static final String CLOSE_IMAGE_HOVERED = "/icons/buttons/commontables/remove_hovered.png"; //$NON-NLS-1$
	
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
	
	private static final String NEW_PROJECT_TOOLTIP = Messages.CreateNewProject;
	private static final String LOAD_PROJECT_TOOLTIP = Messages.LoadProject;
	private static final String EXIT_PROJECT_TOOLTIP = Messages.ExitASTPA;
	private static final String CLOSE_TOOLTIP = Messages.CloseWelcomeView;
	
	private Label close;
	
	
	/**
	 * Get the image for the new project button.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @return the new project button.
	 */
	public static final Image getNewProjectIcon() {
		if (WelcomeView.newProjectImage == null) {
			WelcomeView.newProjectImage = Activator.getImageDescriptor(WelcomeView.NEW_PROJECT_ICON).createImage();
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
			WelcomeView.newProjectImageHovered =
				Activator.getImageDescriptor(WelcomeView.NEW_PROJECT_ICON_HOVERED).createImage();
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
			WelcomeView.loadProjectImage = Activator.getImageDescriptor(WelcomeView.LOAD_PROJECT_ICON).createImage();
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
			WelcomeView.loadProjectImageHovered =
				Activator.getImageDescriptor(WelcomeView.LOAD_PROJECT_ICON_HOVERED).createImage();
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
			WelcomeView.exitImage = Activator.getImageDescriptor(WelcomeView.EXIT_ICON).createImage();
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
			WelcomeView.exitImageHovered = Activator.getImageDescriptor(WelcomeView.EXIT_ICON_HOVERED).createImage();
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
			WelcomeView.backgroundImage = Activator.getImageDescriptor(WelcomeView.BACKGROUND_IMAGE).createImage();
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
			WelcomeView.logoImage = Activator.getImageDescriptor(WelcomeView.LOGO_IMAGE).createImage();
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
			WelcomeView.closeImage = Activator.getImageDescriptor(WelcomeView.CLOSE_IMAGE).createImage();
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
			WelcomeView.closeImageHovered = Activator.getImageDescriptor(WelcomeView.CLOSE_IMAGE_HOVERED).createImage();
		}
		
		return WelcomeView.closeImageHovered;
	}
	
	
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
	
	@Override
	public void onActivateView() {
		// intentionally empty
		
		ISourceProviderService sourceProviderService =
			(ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
		SaveState saveStateService = (SaveState) sourceProviderService.getSourceProvider(SaveState.STATE);
		
		boolean closeEnabled = saveStateService.getCurrentState().get(SaveState.STATE) == SaveState.S_ENABLED;
		
		this.close.setVisible(closeEnabled);
	}
	
	private void executeCommand(String commandId) {
		IServiceLocator serviceLocator = PlatformUI.getWorkbench();
		ICommandService commandService = (ICommandService) serviceLocator.getService(ICommandService.class);
		Command command = commandService.getCommand(commandId);
		if (command != null) {
			try {
				command.executeWithChecks(new ExecutionEvent());
			} catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
				Logger.getRootLogger().error("Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			Logger.getRootLogger().error("Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
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
		backgroundFormData.height = WelcomeView.getBackgroundImage().getBounds().height;
		background.setLayoutData(backgroundFormData);
		
		background.setLayout(new FormLayout());
		background.setBackgroundImage(WelcomeView.getBackgroundImage());
		
		Label logo = new Label(background, SWT.PUSH);
		logo.setImage(WelcomeView.getLogoImage());
		
		FormData logoFormData = new FormData();
		logoFormData.top = new FormAttachment(5);
		logoFormData.left = new FormAttachment(2);
		logoFormData.width = WelcomeView.getLogoImage().getBounds().width;
		logoFormData.height = WelcomeView.getLogoImage().getBounds().height;
		logo.setLayoutData(logoFormData);
		
		final Label newProject =
			this.addHoveredLabel(background, WelcomeView.getNewProjectIcon(), WelcomeView.getNewProjectIconHovered(),
				new Point(30, 40));
		newProject.setToolTipText(WelcomeView.NEW_PROJECT_TOOLTIP);
		
		newProject.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				WelcomeView.this.executeCommand("astpa.newproject"); //$NON-NLS-1$
				Welcome.shutWelcome();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// intentionally empty
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// intentionally empty
			}
		});
		
		final Label loadProject =
			this.addHoveredLabel(background, WelcomeView.getLoadProjectIcon(), WelcomeView.getLoadProjectIconHovered(),
				new Point(40, 40));
		loadProject.setToolTipText(WelcomeView.LOAD_PROJECT_TOOLTIP);
		
		loadProject.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				WelcomeView.this.executeCommand("astpa.load"); //$NON-NLS-1$
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// intentionally empty
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// intentionally empty
			}
		});
		
		final Label exit =
			this.addHoveredLabel(background, WelcomeView.getExitIcon(), WelcomeView.getExitIconHovered(), new Point(50,
				40));
		exit.setToolTipText(WelcomeView.EXIT_PROJECT_TOOLTIP);
		
		exit.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				WelcomeView.this.executeCommand("astpa.exit"); //$NON-NLS-1$
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// intentionally empty
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// intentionally empty
			}
		});
		
		ISourceProviderService sourceProviderService =
			(ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
		SaveState saveStateService = (SaveState) sourceProviderService.getSourceProvider(SaveState.STATE);
		
		boolean closeEnabled = saveStateService.getCurrentState().get(SaveState.STATE) == SaveState.S_ENABLED;
		
		this.close =
			this.addHoveredLabel(background, WelcomeView.getCloseImage(), WelcomeView.getCloseImageHovered(),
				new Point(60, 0));
		this.close.setToolTipText(WelcomeView.CLOSE_TOOLTIP);
		
		this.close.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				Welcome.shutWelcome();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// intentionally empty
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// intentionally empty
			}
		});
		
		this.close.setVisible(closeEnabled);
		
		final Button checkBox = new Button(background, SWT.CHECK);
		checkBox.setText(Messages.ShowPageOnStartup);
		checkBox.setToolTipText(Messages.ShowPageOnStartup);
		checkBox.setSelection(Activator.getDefault().getPreferenceStore()
			.getBoolean(WelcomeView.getShowWelcomeOnStartupPreferences()));
		
		final ViewContainer viewContainer =
			(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ViewContainer.ID);
		viewContainer.updateStatus(""); //$NON-NLS-1$
		
		checkBox.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// intentionally empty
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				viewContainer.updateStatus(""); //$NON-NLS-1$
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				viewContainer.updateStatus(checkBox.getToolTipText());
			}
		});
		
		boolean state =
			Activator.getDefault().getPreferenceStore().getBoolean(WelcomeView.getShowWelcomeOnStartupPreferences());
		
		checkBox.setSelection(state);
		checkBox.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// store new state in preferences
				Activator.getDefault().getPreferenceStore()
					.setValue(WelcomeView.getShowWelcomeOnStartupPreferences(), checkBox.getSelection());
			}
		});
		
		FormData checkBoxFormData = new FormData();
		checkBoxFormData.left = new FormAttachment(2);
		checkBoxFormData.right = new FormAttachment(15);
		checkBoxFormData.top = new FormAttachment(45);
		checkBoxFormData.bottom = new FormAttachment(47);
		checkBox.setLayoutData(checkBoxFormData);
	}
	
	@Override
	public void setDataModelInterface(IDataModel dataInterface) {
		// intentionally empty
	}
	
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		// intentionally empty
	}
	
	private Label addHoveredLabel(Composite parent, final Image icon, final Image hoveredIcon, Point relativePosition) {
		final Label newLabel = new Label(parent, SWT.PUSH);
		newLabel.setImage(icon);
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(relativePosition.x);
		formData.top = new FormAttachment(relativePosition.y);
		formData.width = WelcomeView.getNewProjectIcon().getBounds().width;
		formData.height = WelcomeView.getNewProjectIcon().getBounds().height;
		newLabel.setLayoutData(formData);
		
		final ViewContainer viewContainer =
			(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(ViewContainer.ID);
		viewContainer.updateStatus(""); //$NON-NLS-1$
		
		newLabel.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// intentionally empty
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				newLabel.setImage(icon);
				
				viewContainer.updateStatus(""); //$NON-NLS-1$
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				newLabel.setImage(hoveredIcon);
				
				viewContainer.updateStatus(newLabel.getToolTipText());
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

	@Override
	public boolean triggerExport() {
		// TODO Auto-generated method stub
		return false;
	}
}
