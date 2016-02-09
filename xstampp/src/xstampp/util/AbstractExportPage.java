package xstampp.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import xstampp.Activator;
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

	public static final String EXPORT_DATA = "Export data";
	public static final String A4_LANDSCAPE = "A4Landscape";
	public static final String A4_PORTRAIT = "A4";
	public static final String NON = "no exprot";
	public static final String EXPORT = "include in exprot";
	private Map<UUID, String> projects;
	private Combo chooseList;
	protected PathComposite pathChooser;
	private String pluginID;
	private String nameSuggestion;
	protected String pageFormat;

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
		this.pageFormat = A4_PORTRAIT;
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
		FileDialog fileDialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.SAVE);
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
		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView("astpa.explorer") != null){
			ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView("astpa.explorer").getSite()
					.getSelectionProvider().getSelection();
	
			if(selection instanceof IProjectSelection && this.projects.containsKey(((IProjectSelection) selection).getProjectId())){		
				this.setProjectID(((IProjectSelection) selection).getProjectId());
				this.chooseList.setText(this.projects.get(((IProjectSelection) selection).getProjectId()));
			}
		}
		return projectChooser;

	}
	protected void addLabelWithAssist(Composite parent,Object layoutData, String title,String tip){
		Label text = new Label(parent, SWT.NONE);
		text.setText(title);
		FieldDecorationRegistry decRegistry = FieldDecorationRegistry.getDefault();

		FieldDecoration infoField = decRegistry.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION);

		ControlDecoration decoration = new ControlDecoration(text, SWT.TOP | SWT.RIGHT);
		decoration.setImage(infoField.getImage());

		decoration.setDescriptionText(tip);
		text.setLayoutData(layoutData);
	}
	protected Label addFormatWidget(Composite composite,Object layoutData, String title, final boolean chooseFormat){
			Composite widget= new Composite(composite, SWT.NONE);
			widget.setLayoutData(layoutData);
			widget.setLayout(new GridLayout(2, false));
			final Label chooser = new Label(widget, SWT.None);
			chooser.setLayoutData(new GridData(SWT.BEGINNING,SWT.FILL,false,true));
			if(chooseFormat){
				chooser.setImage(Activator.getImageDescriptor("/icons/exportPortrait.png").createImage());
				chooser.setData(EXPORT_DATA,A4_PORTRAIT);
				chooser.setToolTipText("Export in DinA4 portrait mode");
			}else{
				chooser.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
				chooser.setData(EXPORT_DATA,EXPORT);
				chooser.setToolTipText("include in export");
			}
			MouseAdapter listener =new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					if(chooser.getData(EXPORT_DATA).equals(A4_PORTRAIT)){
						chooser.setImage(Activator.getImageDescriptor("/icons/exportLandscape.png").createImage());
						chooser.setData(EXPORT_DATA,A4_LANDSCAPE);
						chooser.setToolTipText("Export in DinA4 landscape mode");
					}
					else if(chooser.getData(EXPORT_DATA).equals(A4_LANDSCAPE)){
						chooser.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVE));
						chooser.setData(EXPORT_DATA,NON);
						chooser.setToolTipText("exclude from export");
					}
					else if(chooser.getData(EXPORT_DATA).equals(NON) && !chooseFormat){
						chooser.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
						chooser.setData(EXPORT_DATA,EXPORT);
						chooser.setToolTipText("include in export");
					}
					else if(chooser.getData(EXPORT_DATA).equals(EXPORT)){
						chooser.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVE));
						chooser.setData(EXPORT_DATA,NON);
						chooser.setToolTipText("exclude from export");
					}
					else{
						chooser.setImage(Activator.getImageDescriptor("/icons/exportPortrait.png").createImage());
						chooser.setData(EXPORT_DATA,A4_PORTRAIT);
						chooser.setToolTipText("Export in DinA4 portrait mode");
					}
				}
			};
			chooser.addMouseListener(listener);
			final Label textLabel = new Label(widget, SWT.NO);
			textLabel.setText(title);
			textLabel.addMouseListener(listener);
			textLabel.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
//			widget.setSize(widget.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			return chooser;
		}
	/**
	 * @param composite
	 * @param minimal TODO
	 */
	protected Control addFormatChooser(Composite composite,Object layoutData, boolean minimal){
		if(minimal){
			final Label chooser = new Label(composite, SWT.None);
			chooser.setLayoutData(layoutData);
			chooser.setImage(Activator.getImageDescriptor("/icons/exportPortrait.png").createImage());
			chooser.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					if(pageFormat.equals(A4_PORTRAIT)){
						chooser.setImage(Activator.getImageDescriptor("/icons/exportLandscape.png").createImage());
						pageFormat = A4_LANDSCAPE;
					}else if(pageFormat.equals(A4_LANDSCAPE)){
						chooser.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ELCL_REMOVE));
						pageFormat = "";
					}else{
						chooser.setImage(Activator.getImageDescriptor("/icons/exportPortrait.png").createImage());
						pageFormat = A4_PORTRAIT;
					}
				}
			});
			return chooser;
		}else{
			Group formatGroup = new Group(composite, SWT.NONE);
			formatGroup.setLayoutData(layoutData);
			formatGroup.setLayout(new GridLayout(2, true));
			Button chFormat = null;
			for(final String format:new String[]{A4_LANDSCAPE,A4_PORTRAIT}){
				chFormat = new Button(formatGroup, SWT.RADIO);
				chFormat.setText(format);
				chFormat.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
				chFormat.addSelectionListener(new SelectionAdapter() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						pageFormat = format;
					}
				});
			}
			if(chFormat != null){
				chFormat.setSelection(true);
			}
			return formatGroup;
		}
		
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

	/**
	 * @return the pageFormat
	 */
	public String getPageFormat() {
		return this.pageFormat;
	}
}
