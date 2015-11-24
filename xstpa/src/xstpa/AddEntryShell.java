package xstpa;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;


public class AddEntryShell {

	private Shell shell;
	private List<UUID> variables;
	private DataModelController model;
	private Listener listener;
	private List<Combo> idListToCombo;

	public AddEntryShell(List<UUID> vars,DataModelController controller) {
		this.variables = vars;
		this.model = controller;
		this.idListToCombo = new ArrayList<>();
	}
	
	public void open(){

		this.shell = new Shell(Display.getCurrent().getActiveShell(),SWT.CLOSE | SWT.TITLE);
		this.shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellDeactivated(ShellEvent e) {
				AddEntryShell.this.shell.close();
			}
		});
		
		this.shell.setLayout(new GridLayout(2,false));
		
		Composite combos = new Composite(this.shell, SWT.NONE);
		combos.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 2, 1));
		combos.setLayout(new GridLayout(this.variables.size(), false));
		for(UUID id:this.variables){
			//for each variable a composite with the variable name and
			//a combo with all the values is added 
			Composite comp = new Composite(combos, SWT.None);
			comp.setLayoutData(new GridData());
			comp.setLayout(new GridLayout(1, false));
			IRectangleComponent variable = this.model.getComponent(id);
			Label label = new Label(comp, SWT.None);
			label.setText(variable.getText()+":");
			Combo varCombo = new Combo(comp, SWT.READ_ONLY);
			for(IRectangleComponent value : variable.getChildren()){
				varCombo.add(value.getText());
			}
			//all value ids are stored in a list mapped to the combo they are chosen from
			this.idListToCombo.add(varCombo);
			varCombo.select(0);		
		}
		
		Button apply = new Button(shell, SWT.PUSH);
		apply.setText("Apply");

		apply.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false));
		apply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
		    	notifyListener(getValueList());
				shell.close();
			}
		});
		
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, false, false));
		cancel.setText("Cancel");
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		
		shell.pack();
		shell.open();
	}
	
	private String[] getValueList(){
		String[] list = new String[this.idListToCombo.size()];
		for(int i=0;i < list.length;i++){
			list[i] = this.idListToCombo.get(i).getText();
		}
		return list;
	}
	public void addApplyListener(Listener listener){
		this.listener = listener;
	}
	
	private void notifyListener(String[] values){
		if(listener != null && values != null){
			Event e = new Event();
			e.data = values;
			listener.handleEvent(e);
		}
	}
}
