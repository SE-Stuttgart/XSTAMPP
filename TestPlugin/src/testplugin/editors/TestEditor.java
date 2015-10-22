package testplugin.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import testplugin.datamodel.TestController;
import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;

public class TestEditor extends StandartEditorPart	{
	TestController controller;

	public TestEditor() {
		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createPartControl(Composite arg0) {
		this.controller = (TestController) ProjectManager.getContainerInstance().getDataModel(this.getProjectID());
		Composite editorArea = new Composite(arg0, SWT.NONE);
		editorArea.setLayout(new FillLayout());
		IControlAction i;
		final Text text = new Text(editorArea, SWT.WRAP | SWT.MULTI);
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				TestEditor.this.controller.setDescription(text.getText());
				System.out.println("modifieed Text");
			}
		});
	}

}
