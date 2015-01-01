package xstampp.actions;

import messages.Messages;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

public class ToolbarContribution extends ExtensionContributionFactory {

	private Label label;
	private Slider scale;
	private Button undo;
	private Button redo;
	private Button decoSwitch;

	@Override
	public void createContributionItems(IServiceLocator arg0,
			IContributionRoot arg1) {
		System.out.println("created");
		IToolBarManager manager = new ToolBarManager() {
			@Override
			public ToolBar createControl(Composite parent) {
				return ToolbarContribution.this.createToolBar(parent);
			}
		};
		ToolBarContributionItem item = new ToolBarContributionItem(manager);
		item.setVisible(true);
		arg1.addContributionItem(item, null);

	}

	/**
	 * this method (re-)creates the toolbar on the given composite The parent
	 * should have a formlayout since this class only accepts one
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param parent
	 *            the composite on which the toolbar should appear
	 * @param toolbarData
	 *            the data whoch determine where the toolbar shall be displayed
	 * 
	 * @see CSAbstractEditor#toolBar
	 */
	private ToolBar createToolBar(Composite parent) {
		ToolBar toolBar = new ToolBar(parent, SWT.HORIZONTAL | SWT.SHADOW_OUT);
		GridLayout layout = new GridLayout(5, false);

		toolBar.setLayout(layout);

		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();

		// adding the undo/redo Buttons
		this.undo = new Button(toolBar, SWT.BUTTON_MASK);
		this.undo.setImage(sharedImages
				.getImage(ISharedImages.IMG_TOOL_BACK_DISABLED));
		// this.undo.setLayoutData(new GridData());
		this.redo = new Button(toolBar, SWT.BUTTON1);
		this.redo.setImage(sharedImages
				.getImage(ISharedImages.IMG_TOOL_FORWARD_DISABLED));
		// this.redo.setLayoutData(new GridData());

		this.decoSwitch = new Button(toolBar, SWT.TOGGLE);
		// this.decoSwitch.setLayoutData(new GridData());

		this.scale = new Slider(toolBar, SWT.HORIZONTAL);
		this.scale.setBounds(0, 0, 100, SWT.DEFAULT);
		this.scale.setMaximum(300);
		this.scale.setMinimum(0);
		this.scale.setPageIncrement(10);
		this.scale.setIncrement(10);
		this.scale.setToolTipText(Messages.ZoomItem);
		this.scale.setSelection(100);
		// this.scale.setLayoutData(new GridData());

		this.label = new Label(toolBar, SWT.HORIZONTAL);
		this.label.setSize(SWT.DEFAULT, SWT.DEFAULT);
		this.label.setText(Integer.toString(this.scale.getSelection()) + "%"); //$NON-NLS-1$
		// this.label.setLayoutData(new GridData());

		return toolBar;
	}
}
