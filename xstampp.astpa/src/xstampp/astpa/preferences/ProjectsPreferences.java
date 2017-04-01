package xstampp.astpa.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import xstampp.astpa.model.DataModelController;
import xstampp.ui.common.ProjectManager;

public class ProjectsPreferences extends PreferencePage implements IWorkbenchPreferencePage {
  private List<ProjectSpecifics> specificsList;
  
  @Override
  public void init(IWorkbench workbench) {
  }

  @Override
  protected Control createContents(Composite parent) {
    this.specificsList = new ArrayList<>();
    Composite control = new Composite(parent, SWT.None);
    control.setLayout(new GridLayout());
    TabFolder folder = new TabFolder(control, SWT.None);
    folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    for(UUID projectId : ProjectManager.getContainerInstance().getProjectKeys()) {
      TabItem item = new TabItem(folder, SWT.NONE);
      item.setText(ProjectManager.getContainerInstance().getTitle(projectId));
      ProjectSpecifics specific = new ProjectSpecifics(projectId);
      item.setControl(specific.getControl(folder));
      this.specificsList.add(specific);
    }
    return control;
  }
  
  @Override
  protected void performApply() {
    for (ProjectSpecifics projectSpecifics : specificsList) {
      projectSpecifics.save();
    }
  }
  
  private class ProjectSpecifics{

    private DataModelController controller;
    private BooleanSetting useScenariosSetting;
    
    public ProjectSpecifics(UUID projectId) {
      this.controller = (DataModelController) ProjectManager.getContainerInstance().getDataModel(projectId);
    }
    
    public Control getControl(Composite parent) {
      Composite container = new Composite(parent, SWT.None);
      container.setLayout(new GridLayout());
      String description = "Use Scenarios to refine the causal factors in the project,\n this adds a column 'Causal Scenarios' in the "
          + "Causal Factors Table that allows to define safety constraints per causal scenario";
      this.useScenariosSetting = new BooleanSetting(container, description, this.controller.isUseScenarios());
      return container;
    }
    
    public void save() {
      this.controller.setUseScenarios(this.useScenariosSetting.selected);
    }
  }
  
  private class BooleanSetting{
    private boolean selected;
    
    public BooleanSetting(Composite parent,String description, boolean initial) {
      
      Button boolSetting = new Button(parent, SWT.CHECK);
      boolSetting.setText(description);
      selected = initial;
      boolSetting.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          selected = ((Button)e.getSource()).getSelection();
        }
      });
      
    }
    
  }
}


