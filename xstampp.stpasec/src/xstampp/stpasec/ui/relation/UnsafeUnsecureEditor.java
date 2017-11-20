package xstampp.stpasec.ui.relation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.model.ObserverValue;

public class UnsafeUnsecureEditor extends CSEditor {

	public static final String ID = "stpasec.editor.unsafe";

	public UnsafeUnsecureEditor() {
		super();
	}

  @Override
  public void createPartControl(Composite parent) {
    SashForm form = new SashForm(parent, SWT.VERTICAL);
    super.createPartControl(form);
    addUpdateValue(ObserverValue.LINKING);
    addUpdateValue(ObserverValue.UNSAFE_CONTROL_ACTION);
    addUpdateValue(ObserverValue.CONTROL_ACTION);
    new SecView(getModelInterface()).createPartControl(form);
    form.setWeights(new int[] { 3, 1 });
  }

	@Override
	public String getTitle() {
		return "Unsafe Unsecure Editor";
	}

}
