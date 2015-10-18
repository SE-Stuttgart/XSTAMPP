package acast.ui.accidentDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import acast.Activator;
import acast.model.interfaces.IResponsibilityDataModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;

public class TableView extends ViewPart {

	public TableView() {
		super();
	}

	public int z = 0;

	public String requirementsType;

	// button names
	public final String role = "Role";

	public String activeComponent;

	public final String responsibilities = "Safety Related Responsibilities";

	public final String unsafeActions = "Unsafe Decisions and Control Actions";

	public final String context = "Context in which decisions made";

	public final String flaws = "Process/Mental Model Flaws";

	public final String feedback = "Feedback";

	public final String coordination = "Coordination";

	public boolean visible;
	public String selectedItem;

	private int count = 0;

	/**
	 * Interface to communicate with the data model.
	 */
	public IResponsibilityDataModel dataInterface;

	/**
	 * ViewPart ID.
	 */
	public final String ID = "acast.steps.step2_2_1";

	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	private List<Composite> list = new ArrayList<Composite>();

	public String responsibility;

	public Combo combo;

	public TableViewer viewer;

	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (IResponsibilityDataModel) dataInterface;
	}

	public void updateReponsibilites(UUID id, String view) {

		switch (view) {
		case responsibilities:
			for (Responsibility resp : dataInterface
					.getResponsibilitiesListforComponent(id)) {
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { resp.getId(), resp.getDescription() });
			}
			break;

		case context:
			for (Responsibility resp : dataInterface
					.getContextListforComponent(id)) {
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { resp.getId(), resp.getDescription() });
			}
			break;

		case flaws:
			for (Responsibility resp : dataInterface
					.getFlawListforComponent(id)) {
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { resp.getId(), resp.getDescription() });
			}
			break;

		case unsafeActions:
			for (Responsibility resp : dataInterface
					.getUnsafeActionListforComponent(id)) {
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { resp.getId(), resp.getDescription() });
			}
			break;

		case feedback:
			for (Responsibility resp : dataInterface
					.getFeedbackListforComponent(id)) {
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { resp.getId(), resp.getDescription() });
			}
			break;
		case coordination:
			for (Responsibility resp : dataInterface
					.getCoordinationListforComponent(id)) {
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { resp.getId(), resp.getDescription() });
			}
			break;
		}

	}

	public void removeFromDataModel(UUID name, String id, String view) {
		switch (view) {
		case responsibilities:
			dataInterface.removeResponsibility(name, id);
			break;
		case context:
			dataInterface.removeContext(name, id);
			break;
		case flaws:
			dataInterface.removeFlaw(name, id);
			break;
		case unsafeActions:
			dataInterface.removeunsafeAction(name, id);
			break;
		case coordination:
			dataInterface.removeFeedback(name, id);
			break;
		case feedback:
			dataInterface.removeCoordination(name, id);
			break;
		}
	}

	public void updateTableComponentsInDataStructure(UUID ident, String oldId,
			String newId, String newDescription, String view, String name) {
		switch (view) {
		case responsibilities:
			dataInterface.changeResponsibility(ident, oldId, newId,
					newDescription, name);
			break;
		case context:
			dataInterface.changeContext(ident, oldId, newId, newDescription,
					name);
			break;
		case flaws:
			dataInterface.changeFlaw(ident, oldId, newId, newDescription, name);
			break;
		case unsafeActions:
			dataInterface.changeUnsafeAction(ident, oldId, newId,
					newDescription, name);
			break;
		case feedback:
			dataInterface.changeFeedback(ident, oldId, newId, newDescription,
					name);
			break;
		case coordination:
			dataInterface.changeCoordination(ident, oldId, newId,
					newDescription, name);
			break;
		}
	}

	public void applyDataToDataModel(UUID ident, String id, String description,
			String view, String name) {

		switch (view) {
		case responsibilities:
			dataInterface.addResponsibility(ident, id, description, name);
			break;
		case context:
			dataInterface.addContext(ident, id, description, name);
			break;
		case flaws:
			dataInterface.addFlaw(ident, id, description, name);
			break;
		case unsafeActions:
			dataInterface.addUnsafeAction(ident, id, description, name);
			break;
		case feedback:
			dataInterface.addFeedback(ident, id, description, name);
			break;
		case coordination:
			dataInterface.addCoordination(ident, id, description, name);
			break;
		}
	}

	@Override
	public void createPartControl(final Composite parent) {

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addPartListener(new IPartListener2() {

					@Override
					public void partVisible(IWorkbenchPartReference partRef) {

					}

					@Override
					public void partOpened(IWorkbenchPartReference partRef) {

					}

					@Override
					public void partInputChanged(IWorkbenchPartReference partRef) {
						// TODO Auto-generated method stub

					}

					@Override
					public void partHidden(IWorkbenchPartReference partRef) {
						visible = false;
					}

					@Override
					public void partDeactivated(IWorkbenchPartReference partRef) {
						visible = false;

					}

					@Override
					public void partClosed(IWorkbenchPartReference partRef) {
						visible = false;

					}

					@Override
					public void partBroughtToTop(IWorkbenchPartReference partRef) {
						visible = true;

					}

					@Override
					public void partActivated(IWorkbenchPartReference partRef) {

					}
				});

		PlatformUI.getPreferenceStore().addPropertyChangeListener(
				new IPropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent event) {
						if (!combo.isDisposed()) {
							if (event.getNewValue().equals("close")) {
								PlatformUI
										.getWorkbench()
										.getActiveWorkbenchWindow()
										.getActivePage()
										.hideView(
												PlatformUI
														.getWorkbench()
														.getActiveWorkbenchWindow()
														.getActivePage()
														.findView(
																"A-CAST.view1"));
							} else if (event.getNewValue().equals("change")) {
								combo.remove(combo.getSelectionIndex());
								combo.add(String.valueOf(event.getOldValue()));
								combo.select(combo.indexOf(String.valueOf(event
										.getOldValue())));
							} else if (event.getNewValue().equals("add")) {
								combo.add(String.valueOf(event.getOldValue()));
							} else if (event.getNewValue().equals("remove")) {
								combo.remove(String.valueOf(event.getOldValue()));
								if (combo.getItems().length > 0) {
									combo.select(0);
									viewer.getTable().removeAll();
									selectedItem = combo.getItem(combo
											.getSelectionIndex());
									updateReponsibilites(
											dataInterface.getComponentNames()
													.get(selectedItem),
											responsibility);
								} else {
									PlatformUI
											.getWorkbench()
											.getActiveWorkbenchWindow()
											.getActivePage()
											.hideView(
													PlatformUI
															.getWorkbench()
															.getActiveWorkbenchWindow()
															.getActivePage()
															.findView(
																	"A-CAST.view1"));
								}
							} else {
								combo.removeAll();
								for (String name : dataInterface
										.getComponentNames().keySet()) {
									combo.add(name);
								}
								combo.select(combo.indexOf(String.valueOf(event
										.getNewValue())));
								selectedItem = String.valueOf(event
										.getNewValue());
								viewer.getTable().removeAll();
								updateReponsibilites(dataInterface
										.getComponentNames().get(selectedItem),
										responsibility);
							}
							if (!combo.isDisposed()) {
								combo.redraw();
							}
						}
					}
				});

		StandartEditorPart e = (StandartEditorPart) (PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor());
		setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(e.getProjectID()));
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite layout = new Composite(parent, SWT.NONE);
		layout.setLayout(new GridLayout(1, false));
		layout.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite description = new Composite(layout, SWT.NONE);
		description.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false,
				1, 1));

		description.setLayout(new GridLayout(5, false));

		responsibility = responsibilities;

		Label lblResponsibilities = new Label(description, SWT.RIGHT);

		lblResponsibilities.setText("Name of the Role: ");
		lblResponsibilities.setLayoutData(new GridData(SWT.NONE, SWT.FILL,
				false, true, 1, 1));

		combo = new Combo(description, SWT.DROP_DOWN | SWT.RIGHT
				| SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.getTable().removeAll();
				selectedItem = combo.getItem(combo.getSelectionIndex());

				updateReponsibilites(
						dataInterface.getComponentNames().get(selectedItem),
						responsibility);
			}
		});

		combo.setLayoutData(new GridData(SWT.NONE, SWT.FILL, true, true, 1, 1));

		for (String name : dataInterface.getComponentNames().keySet()) {
			combo.add(name);
		}

		Button btnAdd = new Button(description, SWT.CENTER);
		btnAdd.setLayoutData(new GridData(SWT.RIGHT, SWT.None, true, false, 1,
				1));
		btnAdd.setImage(Activator.getImageDescriptor("/icons/add.png")
				.createImage());
		btnAdd.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				int count;
				if (dataInterface.getComponentNames().isEmpty()) {
					MessageBox dialog = new MessageBox(parent.getShell(),
							SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Warning");
					dialog.setMessage("Create Components in Controlstructure first");

					dialog.open();
					return;
				}
				if (viewer.getTable().getItemCount() == 0) {
					count = 1;
				} else {
					count = viewer.getTable().getItemCount() + 1;
				}
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { count + "", "" });
				applyDataToDataModel(
						dataInterface.getComponentNames().get(selectedItem),
						item.getText(0), item.getText(1), responsibility,
						selectedItem);
			}
		});

		Button btnDel = new Button(description, SWT.CENTER);
		btnDel.setLayoutData(new GridData(SWT.RIGHT, SWT.None, false, false, 1,
				1));
		btnDel.setImage(Activator.getImageDescriptor("/icons/delete.png")
				.createImage());
		btnDel.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (viewer.getTable().getSelectionIndex() == -1) {
					MessageBox dialog = new MessageBox(parent.getShell(),
							SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Warning");
					dialog.setMessage("No Event selected");

					dialog.open();
					return;
				}
				int itemIndex = viewer.getTable().getSelectionIndex();
				removeFromDataModel(
						dataInterface.getComponentNames().get(selectedItem),
						viewer.getTable().getItem(itemIndex).getText(0),
						responsibility);
				viewer.getTable().remove(itemIndex);
				for (int i = itemIndex; i < viewer.getTable().getItemCount(); i++) {
					updateTableComponentsInDataStructure(dataInterface
							.getComponentNames().get(selectedItem), viewer
							.getTable().getItem(i).getText(0), i + 1 + "",
							viewer.getTable().getItem(i).getText(1),
							responsibility, selectedItem);
					viewer.getTable().getItem(i).setText(0, i + 1 + "");
				}

			}
		});

		GridLayout buttons = new GridLayout(1, true);
		buttons.verticalSpacing = 0;

		GridLayout dataLayout = new GridLayout(5, false);
		dataLayout.horizontalSpacing = 0;
		dataLayout.verticalSpacing = 0;

		Composite data = new Composite(layout, SWT.BORDER);
		data.setLayout(dataLayout);
		data.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite tab = new Composite(data, SWT.None);
		tab.setLayout(buttons);
		tab.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true, 1, 1));

		final Button safety = new Button(tab, SWT.NONE);
		safety.setText(responsibilities);
		safety.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));
		safety.setEnabled(false);
		safety.setForeground(parent.getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));
		safety.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));

		final Button unsafeActionsBtn = new Button(tab, SWT.NONE);
		unsafeActionsBtn.setText(unsafeActions);
		unsafeActionsBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true,
				false, 1, 2));

		final Button flawsBtn = new Button(tab, SWT.NONE);
		flawsBtn.setText(flaws);
		flawsBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1,
				2));

		final Button contextBtn = new Button(tab, SWT.NONE);
		contextBtn.setText(context);
		contextBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false,
				1, 2));

		final Button feedbackBtn = new Button(tab, SWT.NONE);
		feedbackBtn.setText(feedback);
		feedbackBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false,
				1, 2));

		final Button coordinationBtn = new Button(tab, SWT.NONE);
		coordinationBtn.setText(coordination);
		coordinationBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true,
				false, 1, 2));

		final Color defaultColor = unsafeActionsBtn.getBackground();

		safety.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = responsibilities;
				contextBtn.setEnabled(true);
				flawsBtn.setEnabled(true);
				unsafeActionsBtn.setEnabled(true);
				feedbackBtn.setEnabled(true);
				coordinationBtn.setEnabled(true);
				coordinationBtn.setBackground(defaultColor);
				feedbackBtn.setBackground(defaultColor);
				contextBtn.setBackground(defaultColor);
				flawsBtn.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(defaultColor);
				safety.setBackground(parent.getDisplay().getSystemColor(
						SWT.COLOR_RED));
				safety.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell()
						.forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(
						dataInterface.getComponentNames().get(selectedItem),
						responsibility);
			}
		});

		contextBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = context;
				safety.setEnabled(true);
				flawsBtn.setEnabled(true);
				unsafeActionsBtn.setEnabled(true);
				feedbackBtn.setEnabled(true);
				coordinationBtn.setEnabled(true);
				coordinationBtn.setBackground(defaultColor);
				feedbackBtn.setBackground(defaultColor);
				safety.setBackground(defaultColor);
				flawsBtn.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(defaultColor);
				contextBtn.setBackground(parent.getDisplay().getSystemColor(
						SWT.COLOR_RED));
				contextBtn.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell()
						.forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(
						dataInterface.getComponentNames().get(selectedItem),
						responsibility);
			}
		});

		unsafeActionsBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = unsafeActions;
				safety.setEnabled(true);
				flawsBtn.setEnabled(true);
				contextBtn.setEnabled(true);
				feedbackBtn.setEnabled(true);
				coordinationBtn.setEnabled(true);
				coordinationBtn.setBackground(defaultColor);
				feedbackBtn.setBackground(defaultColor);
				safety.setBackground(defaultColor);
				flawsBtn.setBackground(defaultColor);
				contextBtn.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(parent.getDisplay()
						.getSystemColor(SWT.COLOR_RED));
				unsafeActionsBtn.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell()
						.forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(
						dataInterface.getComponentNames().get(selectedItem),
						responsibility);
			}
		});

		flawsBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = flaws;
				safety.setEnabled(true);
				unsafeActionsBtn.setEnabled(true);
				contextBtn.setEnabled(true);
				feedbackBtn.setEnabled(true);
				coordinationBtn.setEnabled(true);
				coordinationBtn.setBackground(defaultColor);
				feedbackBtn.setBackground(defaultColor);
				safety.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(defaultColor);
				contextBtn.setBackground(defaultColor);
				flawsBtn.setBackground(parent.getDisplay().getSystemColor(
						SWT.COLOR_RED));
				flawsBtn.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell()
						.forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(
						dataInterface.getComponentNames().get(selectedItem),
						responsibility);
			}
		});

		feedbackBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = feedback;
				safety.setEnabled(true);
				unsafeActionsBtn.setEnabled(true);
				contextBtn.setEnabled(true);
				coordinationBtn.setEnabled(true);
				safety.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(defaultColor);
				contextBtn.setBackground(defaultColor);
				coordinationBtn.setBackground(defaultColor);
				feedbackBtn.setBackground(parent.getDisplay().getSystemColor(
						SWT.COLOR_RED));
				feedbackBtn.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell()
						.forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(
						dataInterface.getComponentNames().get(selectedItem),
						responsibility);
			}
		});

		coordinationBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = coordination;
				safety.setEnabled(true);
				unsafeActionsBtn.setEnabled(true);
				contextBtn.setEnabled(true);
				feedbackBtn.setEnabled(true);
				safety.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(defaultColor);
				contextBtn.setBackground(defaultColor);
				feedbackBtn.setBackground(defaultColor);
				coordinationBtn.setBackground(parent.getDisplay()
						.getSystemColor(SWT.COLOR_RED));
				coordinationBtn.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell()
						.forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(
						dataInterface.getComponentNames().get(selectedItem),
						responsibility);
			}
		});

		Composite buttonComposite = new Composite(tab, SWT.BOTTOM);
		buttonComposite.setLayout(new GridLayout(2, false));
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		Composite tableComposite = new Composite(data, SWT.NONE);
		tableComposite.setLayout(new GridLayout(1, false));
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 4, 1));

		viewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);

		TableColumn c1 = new TableColumn(viewer.getTable(), SWT.NONE);
		c1.setText("ID");
		TableColumnLayout tableColumnLayoutC1 = new TableColumnLayout();
		tableColumnLayoutC1.setColumnData(c1,
				new ColumnWeightData(10, 25, true));
		tableComposite.setLayout(tableColumnLayoutC1);

		TableColumn c2 = new TableColumn(viewer.getTable(), SWT.NONE);
		c2.setText("Description");
		TableColumnLayout tableColumnLayoutC2 = new TableColumnLayout();
		tableColumnLayoutC2.setColumnData(c2, new ColumnWeightData(90,
				SWT.FILL, true));
		tableComposite.setLayout(tableColumnLayoutC2);

		viewer.getTable().addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event event) {

				Table table = (Table) event.widget;
				int columnCount = table.getColumnCount();
				if (columnCount == 0) {
					return;
				}
				Rectangle area = table.getClientArea();

				TableColumn lastCol = table.getColumns()[columnCount - 1];

				lastCol.setWidth(lastCol.getWidth()
						+ (area.width - lastCol.getWidth() + table.getColumn(0)
								.getWidth()));

			}
		});

		final TableEditor editor = new TableEditor(viewer.getTable());
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		viewer.getTable().addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Rectangle clientArea = viewer.getTable().getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = viewer.getTable().getTopIndex();
				while (index < viewer.getTable().getItemCount()) {
					boolean visible = false;
					final TableItem item = viewer.getTable().getItem(index);
					for (int i = 0; i < viewer.getTable().getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							final int column = i;
							if (column != 0) {
								if (column == 1) {
									final Text text = new Text(viewer
											.getTable(), SWT.NONE);
									Listener textListener = new Listener() {
										@Override
										public void handleEvent(final Event e) {
											switch (e.type) {
											case SWT.FocusOut:
												item.setText(column,
														text.getText());
												text.dispose();
												updateTableComponentsInDataStructure(
														dataInterface
																.getComponentNames()
																.get(selectedItem),
														item.getText(0), item
																.getText(0),
														item.getText(1),
														responsibility,
														selectedItem);
												break;
											case SWT.Traverse:
												switch (e.detail) {
												case SWT.TRAVERSE_RETURN:
													item.setText(column,
															text.getText());
												case SWT.TRAVERSE_ESCAPE:
													text.dispose();
													e.doit = false;
												}
												break;
											}
										}
									};
									text.addListener(SWT.FocusOut, textListener);
									text.addListener(SWT.Traverse, textListener);
									editor.setEditor(text, item, i);
									text.setText(item.getText(i));
									text.selectAll();
									text.setFocus();
									return;
								} else {
									return;
								}

							}
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible) {
						return;
					}
					index++;
				}
			}
		});
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		if (combo.getItemCount() > 0) {
			selectedItem = combo.getItem(0);
			combo.select(0);
			viewer.getTable().removeAll();
			updateReponsibilites(
					dataInterface.getComponentNames().get(selectedItem),
					responsibility);
		}
	}

	@Override
	public void setFocus() {
	}

}