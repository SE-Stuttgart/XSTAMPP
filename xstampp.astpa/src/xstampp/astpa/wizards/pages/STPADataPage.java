/**
 * 
 * @author Lukas Balzer
 */
package xstampp.astpa.wizards.pages;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import messages.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import xstampp.astpa.util.jobs.ICSVExportConstants;
import xstampp.util.AbstractWizardPage;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class STPADataPage extends CSVExportPage {

	private Composite control;
	Map<Button, String> steps;
	private List<String> stepViews;

	/**
	 * @author Lukas Balzer
	 * @param filters
	 *            the file extensions, which shall be excepted by in the dialog
	 * @param pageName
	 *            the Name of this page, that is displayed in the header of the
	 *            wizard
	 * @param projectName
	 *            The Name of the project
	 */
	public STPADataPage(String[] filters, String pageName, String projectName) {
		super(filters, pageName);
		this.setTitle(pageName);
		this.stepViews = new ArrayList<>();
		for (Field f : ICSVExportConstants.class.getDeclaredFields()) {
			if (f.getType() == String.class) {
				try {
					String string = (String) f.get(null);
					this.stepViews.add(string);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		this.setDescription(Messages.SetValuesForTheExportFile);
	}

	@Override
	public void createControl(Composite parent) {
		this.control = new Composite(parent, SWT.NONE);
		this.control.setBounds(parent.getBounds());
		this.control.setLayout(new FormLayout());

		Group group = new Group(this.control, SWT.SHADOW_IN);
		group.setText(Messages.ChangeExportValues);
		group.setLayout(new RowLayout(SWT.VERTICAL));
		FormData data = new FormData();
		data.top = new FormAttachment(AbstractWizardPage.COMPONENT_OFFSET);
		data.width = parent.getBounds().width;
		group.setLayoutData(data);
		this.steps = new HashMap<>();
		for (String ref : this.stepViews) {
			Button stepButton = new Button(group, SWT.CHECK);
			stepButton.setText(ref);
			stepButton.setSelection(true);
			// each button is mapped to a view id, so it can later be tracked
			this.steps.put(stepButton, ref);
		}
		Composite selectionComp = new Composite(group, SWT.NONE);
		selectionComp.setLayout(new RowLayout(SWT.HORIZONTAL));
		Button chooseAll = new Button(selectionComp, SWT.PUSH);
		chooseAll.setText(Messages.AddAll);
		chooseAll.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Button selector : STPADataPage.this.steps.keySet()) {
					selector.setSelection(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing by default

			}
		});

		Button removeAll = new Button(selectionComp, SWT.PUSH);
		removeAll.setText(Messages.RemoveAll);
		removeAll.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Button selector : STPADataPage.this.steps.keySet()) {
					selector.setSelection(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing by default

			}
		});

		super.createControl(this.control);
		Composite inheritedComp = (Composite) this.getControl();
		data = new FormData();
		data.top = new FormAttachment(group,
				AbstractWizardPage.COMPONENT_OFFSET);
		inheritedComp.setLayoutData(data);
		this.setControl(this.control);

	}

	@Override
	public boolean checkFinish() {
		if (this.getSteps().size() < 1) {
			this.setErrorMessage(Messages.NoDataSelected);
			return false;
		}
		return super.checkFinish();
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return return an array containing all
	 */
	public ArrayList<String> getSteps() {
		ArrayList<String> stepArray = new ArrayList<>();
		for (Button stepSelector : this.steps.keySet()) {
			if (stepSelector.getSelection()) {
				stepArray.add(this.steps.get(stepSelector));
			}
		}

		return stepArray;

	}

	@Override
	public boolean asOne() {
		return true;
	}

}