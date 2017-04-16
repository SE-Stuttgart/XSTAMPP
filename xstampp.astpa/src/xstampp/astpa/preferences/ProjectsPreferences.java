package xstampp.astpa.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderRenderer;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import xstampp.astpa.model.DataModelController;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.util.ColorManager;

public class ProjectsPreferences extends PreferencePage implements IWorkbenchPreferencePage {
  private List<ProjectSpecifics> specificsList;
  private List<Font> fontList;
  private Label note;

  @Override
  public void init(IWorkbench workbench) {
    this.fontList = new ArrayList<>();
  }

  @Override
  protected Control createContents(Composite parent) {
    this.specificsList = new ArrayList<>();
    Composite control = new Composite(parent, SWT.None);
    control.setLayout(new GridLayout());
    control.setSize(400,400);
    CTabFolder folder =  new CTabFolder(control, SWT.MULTI | SWT.BORDER);
    folder.setSize(400,300);
    folder.setSimple(false);
    folder.setUnselectedImageVisible(false);
    folder.setUnselectedCloseVisible(false);
    folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    for (UUID projectId : ProjectManager.getContainerInstance().getProjectKeys()) {
      ProjectSpecifics specific = new ProjectSpecifics(projectId);
      if(specific.controller != null) {
        CTabItem item = new CTabItem(folder, SWT.CLOSE);
        item.setText(ProjectManager.getContainerInstance().getTitle(projectId));
        item.setControl(specific.getControl(folder));
        this.specificsList.add(specific);

        folder.setInsertMark(item, true);
      }
    }
    if(folder.getItemCount() > 0) {
      folder.setSelection(0);
    }
    note = new Label(control, SWT.None);
    note.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
    note.setText("changes of these settings are only persisted after the project has been saved in the main view!");
    FontData f = parent.getFont().getFontData()[0];
    f.setStyle(SWT.ITALIC);
    Font noteFont = new Font(null, f);
    this.fontList.add(noteFont);
    note.setFont(noteFont);
    note.setForeground(ColorManager.COLOR_RED);
    this.note.setVisible(false);
    control.pack();
    return control;
  }

  @Override
  protected void performApply() {
    for (ProjectSpecifics projectSpecifics : specificsList) {
      projectSpecifics.save();
    }
  }

  @Override
  public void dispose() {
    for (Font font : fontList) {
      font.dispose();
    }
    super.dispose();
  }

  private void setChanged(boolean changed) {
    if(this.note != null) {
      this.note.setVisible(changed);
    }
  }

  private class ProjectSpecifics {

    private DataModelController controller;
    private BooleanSetting useScenariosSetting;

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

    public void save() {
      this.controller.setUseScenarios(this.useScenariosSetting.selected);
    }
  }

  private class BooleanSetting {
    private boolean selected;

    public BooleanSetting(Composite parent, String title, String description, boolean initial) {

      Button boolSetting = new Button(parent, SWT.CHECK);
      Composite comp = new Composite(parent, SWT.None);
      comp.setLayout(new GridLayout());
      
      Label titleLabel = new Label(comp, SWT.None);
      
      FontData f = parent.getFont().getFontData()[0];
      f.setStyle(SWT.BOLD);
      Font noteFont = new Font(null, f);
      ProjectsPreferences.this.fontList.add(noteFont);
      titleLabel.setFont(noteFont);
      titleLabel.setText(title);
      Label descLabel = new Label(comp, SWT.WRAP);
      descLabel.setText(description);
      boolSetting.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          selected = ((Button) e.getSource()).getSelection();
          setChanged(true);
        }
      });
      boolSetting.setSelection(initial);
    }

  }
}
