package xstampp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import xstampp.Activator;
import xstampp.model.ObserverValue;
import xstampp.ui.common.IViewBase;
import xstampp.ui.common.ViewContainer;

/**
 * The Standard Editor Part for an STPA Project
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 * @see EditorPart
 * 
 */
public abstract class StandartEditorPart extends EditorPart implements
		IViewBase {

	private UUID projectID;
	private boolean dirty;

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (this.isDirty()) {
			Map<String, String> values = new HashMap<>();
			values.put("saveProjectId", this.projectID.toString());
			STPAPluginUtils.executeParaCommand("astpa.save", values);
		}
	}

	@Override
	public void doSaveAs() {
		Map<String, String> values = new HashMap<>();
		values.put("saveAsProjectId", this.projectID.toString());
		STPAPluginUtils.executeParaCommand("astpa.saveas", values);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setProjectID(((STPAEditorInput) input).getProjectID());

	}

	@Override
	public boolean isDirty() {
		if (ViewContainer.getContainerInstance().getUnsavedChanges(
				this.projectID)) {
			this.setStatusLine();
		}
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
		((STPAEditorInput)getEditorInput()).activate();
		
	}

	/**
	 * @return the projectID
	 */
	public UUID getProjectID() {
		return this.projectID;
	}

	/**
	 * @param projectID
	 *            the projectID to set
	 */
	public void setProjectID(UUID projectID) {
		this.projectID = projectID;
	}

	/**
	 * updates the status line
	 * 
	 * @author Lukas Balzer
	 * 
	 * 
	 */
	public void setStatusLine() {
		if (ViewContainer.getContainerInstance().getUnsavedChanges(
				this.projectID)) {
			Image image = Activator.getImageDescriptor(
					"/icons/statusline/warning.png").createImage(); //$NON-NLS-1$
			this.getEditorSite().getActionBars().getStatusLineManager()
					.setMessage(image, Messages.ThereAreUnsafedChanges);
		} else {
			this.getEditorSite().getActionBars().getStatusLineManager()
					.setMessage(null);
		}
		this.dirty = true;
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		this.setStatusLine();
		switch (type) {
		case DELETE: {
			this.getSite().getPage().closeEditor(this, false);
			break;
		}
		default:
			break;
		}

	}
}
