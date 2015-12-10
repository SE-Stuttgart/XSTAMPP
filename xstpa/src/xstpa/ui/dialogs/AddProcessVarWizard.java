package xstpa.ui.dialogs;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import xstpa.model.ProcessModelVariables;
import xstpa.ui.View;

public class AddProcessVarWizard {
	

    // ==================== 2. Instance Fields ============================

    private Shell shell;
    private View view;
    private String[] args;
    private Label[] labelArray;
    
    private Text[] textArray;
    // ==================== 4. Constructors ===============================


	public AddProcessVarWizard(String[] args, View view)
    {
        shell = new Shell(Display.getCurrent(),SWT.SHELL_TRIM & (~SWT.RESIZE));
        shell.setLayout(new GridLayout(2, false));
        shell.setText("Add a new Entry for the Context Table");
        shell.setImage(View.LOGO);

        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width)/4;
        int y = (dim.height)/2;
        // set the Location
        shell.setLocation(x,y);
        this.args = args;
        labelArray = new Label[args.length];
        textArray = new Text[args.length];
        this.view = view;
        createContents(shell);
        shell.pack();
        
    }
    
    // ==================== 5. Creators =============================
    
    private void createContents(final Composite parent) {
    	
    	
    	for (int i = 0; i<args.length;i++) {
    	    // Add the Labels for The new Entry
    		labelArray[i] = new Label(shell, SWT.NONE);
    		labelArray[i].setText(args[i]);
    		
    		textArray[i] = new Text(shell, SWT.BORDER);
    		textArray[i].setLayoutData(new GridData(140, SWT.DEFAULT));
    		textArray[i].setText("");

    	}
	    // The OK Button
	    Button applyBtn = new Button (shell, SWT.PUSH);
	    applyBtn.setText("OK");
	    applyBtn.setLayoutData(new GridData(100, 30));
    	
    	
	    // The Cancel Button
	    Button cancelBtn = new Button (shell, SWT.PUSH);
	    cancelBtn.setText("Cancel");
	    cancelBtn.setLayoutData(new GridData(100, 30));
	    
	    applyBtn.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		
	    		for (int i=0; i<textArray.length; i++) {
	    			if (textArray[i].getText().isEmpty()) {	    				
	    				MessageDialog.openWarning(null,"Empty Fields","Please Fill Out every Field!");
	    				return;
	    			}
	    			if (i == textArray.length-1) {
	    				ProcessModelVariables newVal = new ProcessModelVariables();
	    				for (int j=0; j<textArray.length; j++) {
	    					newVal.addValue(textArray[j].getText());
	    				}
    					
						view.contextRightContent.add(newVal);
						view.contextRightViewer.refresh();
						close();
	    			}

	    		}
	    		
    			
	    	}
	    });
	    
	    cancelBtn.addSelectionListener(new SelectionAdapter() {
	    	public void widgetSelected(SelectionEvent event) {
	    		close();	    			    		
	    	}
	    });
	    
    }
    
    public void open()
    {
    	
        shell.open();     
    }

    public void close()
    {
    	
        shell.setVisible(false);
    }
}
