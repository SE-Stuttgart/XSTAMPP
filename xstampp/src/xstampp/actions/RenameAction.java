package xstampp.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import messages.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import xstampp.ui.common.ViewContainer;

public class RenameAction extends Action implements ModifyListener {
	private UUID projectId;
	private Button submit;
	private String textOut;
	private Text textIn;
	private Shell renameShell;
	private MouseListener mainListener;
	private List<MouseListener> listeners;

	public RenameAction(UUID projectId) {
		super("Rename...");
		this.projectId = projectId;
		this.listeners = new ArrayList<>();
		this.init();
	}

	private void init() {
		this.mainListener = new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				for (MouseListener tmp : RenameAction.this.listeners) {
					tmp.mouseUp(e);
				}
				RenameAction.this.renameShell.close();
			}
		};
	}

	@Override
	public void run() {
		this.renameShell = new Shell();
		Point loc = Display.getCurrent().getCursorLocation();
		this.renameShell.setBounds(loc.x, loc.y, 200, 25);
		this.renameShell.setText("Rename Project");
		Composite container = new Composite(this.renameShell, SWT.None);
		this.renameShell.setLayout(new FormLayout());
		container.setLayoutData(new FormData(300, 200));
		container.setLayout(new FormLayout());

		FormData data = new FormData();
		data.top = new FormAttachment(5);
		data.left = new FormAttachment(5);
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(data);
		label.setText(Messages.ProjectName + ": "); //$NON-NLS-1$

		data = new FormData();
		data.top = new FormAttachment(5);
		data.left = new FormAttachment(label, 5);
		data.right = new FormAttachment(95);
		this.textIn = new Text(container, SWT.BORDER | SWT.SINGLE);
		this.textIn.setLayoutData(data);
		this.textIn.setText(ViewContainer.getContainerInstance().getTitle(
				this.projectId));
		this.textIn.addModifyListener(this);

		data = new FormData();
		data.bottom = new FormAttachment(95);
		data.right = new FormAttachment(98);
		Button cancelButton = new Button(container, SWT.PUSH);
		cancelButton.setLayoutData(data);
		cancelButton.setText("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				RenameAction.this.renameShell.close();
			}
		});

		data = new FormData();
		data.bottom = new FormAttachment(95);
		data.right = new FormAttachment(cancelButton, -5);
		this.submit = new Button(container, SWT.PUSH);
		this.submit.setLayoutData(data);
		this.submit.setText("Rename");
		this.submit.addMouseListener(this.mainListener);
		this.renameShell.setDefaultButton(this.submit);
		this.renameShell.pack();
		this.renameShell.open();
		super.run();
	}

	public String getNewProjectName() {
		return this.textOut;
	}

	public void addRenameListener(MouseListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void modifyText(ModifyEvent e) {
		this.textOut = this.textIn.getText();

	}

}
