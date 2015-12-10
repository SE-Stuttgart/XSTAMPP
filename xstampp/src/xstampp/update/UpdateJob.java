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

package xstampp.update;

import java.net.URI;
import java.net.URISyntaxException;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import xstampp.Activator;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;

/**
 * The job that handles the update routine
 * 
 * @author Fabian Toth
 * 
 */
public class UpdateJob extends Job {

	/**
	 * The location of the repository
	 */
	private static final String REPOSITORY_LOC = System.getProperty(
			"UpdateHandler.Repo", //$NON-NLS-1$
			Activator.getDefault().getPreferenceStore()
					.getString(IPreferenceConstants.UPDATE_LINK));

	private IProvisioningAgent agent;
	private Shell parent;
	private ProjectManager viewContainer;
	private boolean startUp;

	/**
	 * Constructor of the update Job
	 * 
	 * @author Fabian Toth
	 * 
	 * @param name
	 *            the name of the job
	 * @param agent
	 *            the provisioning agent
	 * @param parent
	 *            the parent shell
	 * @param startUp
	 *            true, if this is the check at the startup
	 */
	public UpdateJob(String name, IProvisioningAgent agent, Shell parent,boolean startUp) {
		super(name);
		ProjectManager.getLOGGER().debug("Search " + UpdateJob.REPOSITORY_LOC + " for updates");
		this.agent = agent;
		this.parent = parent;
		this.viewContainer = ProjectManager.getContainerInstance();
		this.startUp = startUp;
		ValidationDialogService dialogService = new ValidationDialogService();
		dialogService.bindProvisioningAgent(agent);
	}

	private boolean doInstall = false;

	@Override
	protected IStatus run(final IProgressMonitor monitor) {

		// 1. Prepare update plumbing
		final ProvisioningSession session = new ProvisioningSession(this.agent);
		final UpdateOperation operation = new UpdateOperation(session);
		URI uri = null;
		try {
			uri = new URI(UpdateJob.REPOSITORY_LOC);
		} catch (final URISyntaxException e) {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					MessageDialog.openError(UpdateJob.this.parent,
							"URI invalid", e.getMessage()); //$NON-NLS-1$
				}
			});
			return Status.CANCEL_STATUS;
		}
		// set location of artifact and metadata repo
		operation.getProvisioningContext().setArtifactRepositories(
				new URI[] { uri });
		operation.getProvisioningContext().setMetadataRepositories(
				new URI[] { uri });

		// 2. check for updates
		final IStatus status = operation.resolveModal(monitor);
		// failed to find updates (inform user and exit)
		if ((status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE)
				&& !this.startUp) {
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					MessageDialog
							.openWarning(
									UpdateJob.this.parent,
									Messages.NoUpdate,
									Messages.NoUpdatesForTheCurrentInstallationHaveBeenFound);
				}
			});
		}
		// 3. Ask if updates should be installed and run installation
		if (status.isOK() && (status.getSeverity() != IStatus.ERROR)) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					String updates = ""; //$NON-NLS-1$
					for (Update update : operation.getPossibleUpdates()) {
						updates += update.toString().replaceAll("==>", "to") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					}
					if (operation.getPossibleUpdates().length > 0) {
						UpdateJob.this.doInstall = MessageDialog.openQuestion(
								UpdateJob.this.parent,
								Messages.ReallyInstallUpdates, updates);
					} else if (!UpdateJob.this.startUp) {
						MessageDialog
								.openInformation(
										UpdateJob.this.parent,
										Messages.UpdateFromIDE,
										Messages.TheUpdateRoutineDoesNotWorkWhenASTPAIsExecutedFromTheIDE);
					}
				}
			});
		}

		// start installation
		if (this.doInstall) {
			final ProvisioningJob provisioningJob = operation
					.getProvisioningJob(monitor);
			// updates cannot run from within Eclipse IDE!!!
			if (provisioningJob == null) {
				System.err
						.println(Messages.RunningUpdateFromWithinEclipseIDEThisWontWork);
				throw new NullPointerException();
			}
			// register a job change listener to track
			provisioningJob.addJobChangeListener(new UpdateJobChangeAdapter(
					this.parent, this.viewContainer));
			provisioningJob.schedule();
		}
		return Status.OK_STATUS;
	}
}

class UpdateJobChangeAdapter extends JobChangeAdapter {

	private Shell parent;
	private ProjectManager viewContainer;

	public UpdateJobChangeAdapter(Shell parent, ProjectManager viewContainer) {
		this.parent = parent;
		this.viewContainer = viewContainer;
	}

	@Override
	public void done(final IJobChangeEvent event) {
		if (event.getResult().isOK()) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					boolean restart = MessageDialog.openQuestion(
									UpdateJobChangeAdapter.this.parent,
									Messages.UpdatesInstalledRestart,
									Messages.UpdatesHaveBeenInstalledSuccessfullyDoYouWantToRestart);
					if (UpdateJobChangeAdapter.this.viewContainer
							.getUnsavedChanges()) {
						restart = restart
								&& !MessageDialog.openQuestion(
												UpdateJobChangeAdapter.this.parent,
												Messages.PlatformName,
												Messages.ThereAreUnsafedChangesDoYouWantToStoreThemAbort);
					}if (restart) {
						PlatformUI.getWorkbench().restart();
					}
				}
			});
			super.done(event);
		}
	}
}
