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

import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.DataModelController;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.contentassist.LabelWithAssist;
import xstampp.ui.common.projectsettings.ISettingsPage;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.api.AccessRights;

public class ProjectSpecifics implements ISettingsPage {

  private DataModelController controller;
  private BooleanSetting useScenariosSetting;
  private BooleanSetting useHazardSeverity;
  private ModalShell modalShell;

  public ProjectSpecifics() {
  }

  public ProjectSpecifics(UUID projectId) {
    IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(projectId);
    if (dataModel instanceof DataModelController) {
      this.controller = (DataModelController) dataModel;
    }
  }

  @Override
  public Composite createControl(CTabFolder control, ModalShell parent, UUID modelId) {
    this.modalShell = parent;
    IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(modelId);
    if (dataModel instanceof DataModelController) {
      this.controller = (DataModelController) dataModel;
    }

    Composite container = new Composite(control, SWT.None);
    container.setLayout(new GridLayout(2, false));
    container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
    String title = Messages.ProjectSpecifics_UseCausalScenarios;
    String description = Messages.ProjectSpecifics_UseCausalScenariosTip;
    this.useScenariosSetting = new BooleanSetting(container, title, description,
        this.controller.isUseScenarios());

    title = Messages.ProjectSpecifics_UseSeverity;
    description = Messages.ProjectSpecifics_UseSeverityTip;
    this.useHazardSeverity = new BooleanSetting(container, title, description,
        this.controller.isUseSeverity());
    this.useHazardSeverity.setMessage(Messages.ProjectSpecifics_ReopenHazardView);
    container.setSize(400, 400);
    return container;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public boolean doAccept() {
    this.controller.setUseScenarios(this.useScenariosSetting.selected);
    this.controller.setUseSeverity(this.useHazardSeverity.selected);
    return true;
  }

  private class BooleanSetting {
    private boolean selected;
    private String message = null;
    private boolean useMessage = false;
    public BooleanSetting(Composite parent, String title, String description, boolean initial) {

      Button boolSetting = new Button(parent, SWT.CHECK);
      Composite comp = new Composite(parent, SWT.None);
      comp.setLayout(new GridLayout());
      boolSetting.setSelection(initial);
      this.selected = initial;
      boolSetting.setEnabled(controller.getUserSystem().checkAccess(AccessRights.ADMIN));
      
      new LabelWithAssist(comp, SWT.None, title, description);

      boolSetting.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          selected = ((Button) e.getSource()).getSelection();
          if(modalShell.canAccept() && useMessage ) {
            modalShell.setMessage(getMessage());
          }
        }
      });
    }
    /**
     * @return the message
     */
    public String getMessage() {
      return message;
    }
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
      this.message = message;
      this.useMessage = true;
    }

  }

  @Override
  public boolean isVisible(UUID projectId) {
    return true;
  }

  @Override
  public String getName() {
    return Messages.ProjectSpecifics_GeneralSettings;
  }

  @Override
  public void setName(String name) {
    //The name cannot be changed
  }

  @Override
  public String getId() {
    return "xstampp.astpa.settings.general"; //$NON-NLS-1$
  }
}