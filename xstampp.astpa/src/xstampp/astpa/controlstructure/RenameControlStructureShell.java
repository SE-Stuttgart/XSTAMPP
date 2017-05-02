package xstampp.astpa.controlstructure;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.ui.common.ModalShell;

public class RenameControlStructureShell extends ModalShell {

  private TextInput nameInput;
  private IControlStructureEditorDataModel dataModel;
  private UUID rootId;

  public RenameControlStructureShell(IControlStructureEditorDataModel dataModel, UUID rootId) {
    super("Rename Control Structure");
    this.dataModel = dataModel;
    this.rootId = rootId;
  }

  @Override
  protected boolean validate() {
    return !this.nameInput.getText().isEmpty();
  }

  @Override
  protected boolean doAccept() {
    for (IRectangleComponent comp: dataModel.getRoots()) {
      if(comp.getText().equals(nameInput.getText())) {
        invalidate("The name for the control structure must be unique!");
        return false;
      }
    }
    if(dataModel.changeComponentText(rootId, nameInput.getText())) {
      setReturnValue(nameInput.getText());
      return true;
    }
    return false;
  }

  @Override
  protected void createCenter(Shell parent) {
    this.nameInput = new TextInput(parent, SWT.None, "Name");

  }

}
