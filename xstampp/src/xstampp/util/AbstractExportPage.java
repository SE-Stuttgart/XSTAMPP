package xstampp.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.IProjectSelection;

/**
 * a class to prevent code cloning in the export Pages
 * 
 * @author Lukas Balzer
 * 
 */
public abstract class AbstractExportPage extends AbstractWizardPage implements
		IExportPage {

	private Map<UUID, String> projects;
	private Combo chooseList;
	protected PathComposite pathChooser;
	private String pluginID;
	private String nameSuggestion;

	/**
	 * 
	 *
	 * @author Lukas Balzer
	 *
	 * @param pageName
	 * 			the name of the export page
 	 * @param pluginID
	 * 			The plugin for which this export page is created
	 */
	protected AbstractExportPage(String pageName, String pluginID) {
		super(pageName);
		this.projects = new HashMap<>();
		this.pluginID = pluginID;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param path
	 *            the path as String
	 */
	public void setExportPath(String path) {
		this.pathChooser.setText(path);

	}

	
	@Override
	protected String openExportDialog(String[] filters, String[] names) {
		FileDialog fileDialog = new FileDialog(this.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(filters);
		fileDialog.setFilterNames(names);
		if(this.nameSuggestion != null){
			fileDialog.setFileName(nameSuggestion);
		}else{
			fileDialog.setFileName(this.getProjectName());
		}
		String filePath = fileDialog.open();
		if (filePath != null) {
			this.setExportPath(filePath);
			this.setPageComplete(this.checkFinish());
			return filePath;
		}

		return ""; ////$NON-NLS-1$
	}

	/**
	 * adds a drop down list to the wizard page where the user can choose
	 * between all open projects <br>
	 * <i>the parent must have a FormLayout</i>
	 * 
	 * @author Lukas Balzer
	 * @param parent
	 *            the parent composite
	 * @param attachment
	 *            the
	 * @return the composite containing the project drop down list
	 * 
	 */
	public Composite addProjectChooser(Composite parent,
			FormAttachment attachment) {
		FormData data = new FormData();
		data.top = attachment;
		data.left = new FormAttachment(0, AbstractWizardPage.LABEL_COLUMN);
		final Composite projectChooser = new Composite(parent, SWT.NONE);
		projectChooser.setToolTipText(Messages.ChooseProjectForExport);
		projectChooser.setLayoutData(data);
		projectChooser.setLayout(new GridLayout(3, false));

		Label chooseLabel = new Label(projectChooser, SWT.None);
		chooseLabel.setLayoutData(new GridData(AbstractWizardPage.LABEL_WIDTH,
				SWT.DEFAULT));
		chooseLabel.setText(Messages.Project);

		this.chooseList = new Combo(projectChooser, SWT.DROP_DOWN);
		this.chooseList.setLayoutData(new GridData(300, SWT.DEFAULT));
		
		this.chooseList.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				projectChooser.notifyListeners(SWT.Selection, null);
				AbstractExportPage.this.setProjectID(AbstractExportPage.this.projects.keySet().
															toArray(new UUID[0])[AbstractExportPage.this.chooseList
				                                                                       .getSelectionIndex()]);
				
				AbstractExportPage.this.setPageComplete(AbstractExportPage.this
						.checkFinish());
				
			}
		});
		for (Entry<UUID, String> entry : ProjectManager.getContainerInstance().getProjects().entrySet()) {
			if(canExport(entry.getKey())){
				this.projects.put(entry.getKey(), entry.getValue());
				this.chooseList.add( entry.getValue());
			}
		}
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView("astpa.explorer").getSite()
				.getSelectionProvider().getSelection();

		if(selection instanceof IProjectSelection && this.projects.containsKey(((IProjectSelection) selection).getProjectId())){		
			this.setProjectID(((IProjectSelection) selection).getProjectId());
			this.chooseList.setText(this.projects.get(((IProjectSelection) selection).getProjectId()));
		}
		return projectChooser;

	}



	@Override
	public String getExportPath() {
		if (this.pathChooser == null) {
			ProjectManager.getLOGGER().debug(
					"AbstractExportPage must define a pathChooser"); //$NON-NLS-1$
			return null;
		}
		return this.pathChooser.getText();
	}

	@Override
	public boolean checkFinish() {
		this.setErrorMessage(null);
		if (this.getProjectID() == null) {
			this.setErrorMessage(Messages.NoProjectSelected);
			return false;
		}
		if ((this.getExportPath() == null) || this.getExportPath().equals("")) { //$NON-NLS-1$
			this.setErrorMessage(Messages.IlegalPath);
			return false;
		}
		File fileTmp = new File(this.getExportPath());
		if (fileTmp.exists()) {
			this.setMessage(String.format(Messages.DoYouReallyWantToOverwriteTheContentAt,getExportPath()),
					IMessageProvider.WARNING);
		}
		return true;
	}

	@Override
	protected void setControl(Control newControl) {
		this.setPageComplete(this.checkFinish());
		super.setControl(newControl);
	}

	/**
	 * @return the nameSuggestion
	 */
	public String getNameSuggestion() {
		return nameSuggestion;
	}

	/**
	 * @param nameSuggestion the nameSuggestion to set
	 */
	public void setNameSuggestion(String nameSuggestion) {
		this.nameSuggestion = nameSuggestion;
	}
	
	public boolean canExport(UUID id){
		return ProjectManager.getContainerInstance().getDataModel(id).getPluginID().equals(this.pluginID);
	}
	public void setProjectChoice(UUID projectId){
		if(this.projects.containsKey(projectId)){
			String projectName = this.projects.get(projectId);
			for(int i=0;i<this.chooseList.getItemCount();i++){
				if(projectName.equals(this.chooseList.getItems()[i])){
					this.chooseList.select(i);
				}
			}
		}
	}
}
