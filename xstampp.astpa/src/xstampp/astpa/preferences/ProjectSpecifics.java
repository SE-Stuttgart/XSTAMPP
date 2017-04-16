package xstampp.astpa.preferences;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import xstampp.astpa.model.DataModelController;
import xstampp.model.IDataModel;
import xstampp.ui.common.ModalShell;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.projectSettings.ISettingsPage;

public class ProjectSpecifics implements ISettingsPage{

    private DataModelController controller;
    private BooleanSetting useScenariosSetting;

    public ProjectSpecifics(){
    }
    
    public ProjectSpecifics(UUID projectId) {
      IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(projectId);
      if(dataModel instanceof DataModelController) {
        this.controller = (DataModelController) dataModel;
      }
    }
    
    public Control getControl(Composite parent) {
      
      Composite container = new Composite(parent, SWT.None);
      container.setLayout(new GridLayout(2, false));
      container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
      String title = "Use Causal Scenarios";
      String description = "Use &Scenarios to refine the causal factors in the project,\n this adds a column 'Causal Scenarios' in the "
          + "Causal Factors Table that allows to define safety constraints per causal scenario";
      this.useScenariosSetting = new BooleanSetting(container, title, description, this.controller.isUseScenarios());
      container.setSize(400,400);
      return container;
    }

    @Override
    public Composite createControl(CTabFolder control, ModalShell parent, UUID modelId) {
      IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(modelId);
      if(dataModel instanceof DataModelController) {
        this.controller = (DataModelController) dataModel;
      }
      
      Composite container = new Composite(control, SWT.None);
      container.setLayout(new GridLayout(2, false));
      container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
      String title = "Use Causal Scenarios";
      String description = "Use &Scenarios to refine the causal factors in the project,\n this adds a column 'Causal Scenarios' in the "
          + "Causal Factors Table that allows to define safety constraints per causal scenario";
      this.useScenariosSetting = new BooleanSetting(container, title, description, this.controller.isUseScenarios());
      container.setSize(400,400);
      return container;
    }

    @Override
    public boolean validate() {
      // TODO Auto-generated method stub
      return true;
    }

    @Override
    public boolean doAccept() {
      this.controller.setUseScenarios(this.useScenariosSetting.selected);
      return true;
    }

  private class BooleanSetting {
    private boolean selected;

    public BooleanSetting(Composite parent, String title, String description, boolean initial) {

      Button boolSetting = new Button(parent, SWT.CHECK);
      Composite comp = new Composite(parent, SWT.None);
      comp.setLayout(new GridLayout());
      
      Label titleLabel = new Label(comp, SWT.None);
      
      titleLabel.setText(title);
      Label descLabel = new Label(comp, SWT.WRAP);
      descLabel.setText(description);
      boolSetting.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          selected = ((Button) e.getSource()).getSelection();
         
        }
      });
      boolSetting.setSelection(initial);
    }

  }
}