package acast.ui.accidentDescription;

import java.util.UUID;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;

import acast.Activator;
import acast.model.interfaces.IResponsibilityDataModel;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;

public class RecommandationsView extends StandartEditorPart {

	/**
	 * ViewPart ID.
	 */
	public static final String ID = "acast.steps.step3_1";

	private IResponsibilityDataModel dataInterface;
	private Combo combo;

	private String selectedItem;

	private TableViewer viewer;

	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (IResponsibilityDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void applyDataToDataModel(UUID ident, String oldId, String newId, String newDescription,
			boolean responsibilityAlreadyExits, String name) {
		if (!responsibilityAlreadyExits) {
			this.dataInterface.addRecommendation(ident, oldId, newDescription, name);
		} else {
			this.dataInterface.changeRecommendation(ident, oldId, newId, newDescription, name);
		}

	}

	public void updateComponents(UUID ident, String oldId, String newId, String newDescription, String name) {
		dataInterface.changeRecommendation(ident, oldId, newId, newDescription, name);
	}

	public void removeFromDataModel(UUID ident, String id) {
		this.dataInterface.removeRecommendation(ident, id);
	}

	public void updateReponsibilites(UUID id) {
		for (Responsibility resp : this.dataInterface.getRecommendationListforComponent(id)) {
			TableItem item = new TableItem(viewer.getTable(), SWT.None);
			item.setText(new String[] { resp.getId(), resp.getDescription() });
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(new IPartListener2() {

			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
				if (partRef.getId().equals("acast.steps.step3_1")) {

					if (TableView.visible) {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(PlatformUI
								.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("A-CAST.view1"));
					}

					if (!combo.isDisposed()) {
						if (combo.getSelectionIndex() == -1) {
							combo.removeAll();
							for (String x : dataInterface.getComponentNames().keySet()) {
								combo.add(x);
							}
							if (combo.getItemCount() > 0) {
								combo.select(0);
							}
						} else {
							String se = combo.getItem(combo.getSelectionIndex());
							combo.removeAll();
							for (String x : dataInterface.getComponentNames().keySet()) {
								combo.add(x);
							}
							if (combo.getItemCount() > 0) {
								combo.select(combo.indexOf(se));
							}
						}
					}

				}

			}

			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}
		});

		final Composite parent2 = parent;
		setDataModelInterface(ProjectManager.getContainerInstance().getDataModel(getProjectID()));

		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite description = new Composite(parent, SWT.NONE);
		description.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));

		description.setLayout(new GridLayout(5, false));

		Label lblResponsibilities = new Label(description, SWT.RIGHT);

		lblResponsibilities.setText("Name of the Role: ");
		lblResponsibilities.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true, 1, 1));

		combo = new Combo(description, SWT.DROP_DOWN | SWT.RIGHT | SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.getTable().removeAll();
				selectedItem = combo.getItem(combo.getSelectionIndex());
				updateReponsibilites(dataInterface.getComponentNames().get(selectedItem));
			}
		});

		combo.setLayoutData(new GridData(SWT.NONE, SWT.FILL, true, true, 1, 1));

		for (String comp : this.dataInterface.getComponentNames().keySet()) {
			combo.add(comp);
		}

		if (combo.getItemCount() > 0) {
			combo.select(combo.getItemCount() - 1);
			selectedItem = combo.getItem(combo.getSelectionIndex());
		}

		Button btnAdd = new Button(description, SWT.CENTER);
		btnAdd.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, true, false, 1, 1));
		btnAdd.setImage(Activator.getImageDescriptor("/icons/add.png").createImage());
		btnAdd.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (combo.getItemCount() == 0) {
					MessageBox dialog = new MessageBox(parent2.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Warning");
					dialog.setMessage("Create Components in Controlstruture first");

					dialog.open();
					return;
				}
				int count;
				if (viewer.getTable().getItemCount() == 0) {
					count = 1;
				} else {
					count = viewer.getTable().getItemCount() + 1;
				}
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { count + "", "" });
				applyDataToDataModel(dataInterface.getComponentNames().get(selectedItem), item.getText(0),
						item.getText(0), "", false, selectedItem);
			}
		});

		Button btnDel = new Button(description, SWT.CENTER);
		btnDel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		btnDel.setImage(Activator.getImageDescriptor("/icons/delete.png").createImage());
		btnDel.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (viewer.getTable().getSelectionIndex() == -1) {
					MessageBox dialog = new MessageBox(parent2.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Warning");
					dialog.setMessage("No Event selected");

					dialog.open();
					return;
				}
				int itemIndex = viewer.getTable().getSelectionIndex();
				removeFromDataModel(dataInterface.getComponentNames().get(selectedItem),
						viewer.getTable().getItem(itemIndex).getText(0));
				viewer.getTable().remove(itemIndex);
				for (int i = itemIndex; i < viewer.getTable().getItemCount(); i++) {
					updateComponents(dataInterface.getComponentNames().get(selectedItem),
							viewer.getTable().getItem(i).getText(0), i + 1 + "",
							viewer.getTable().getItem(i).getText(1), selectedItem);
					viewer.getTable().getItem(i).setText(0, i + 1 + "");
				}

			}
		});

		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayout(new GridLayout(1, false));
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));

		viewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);

		TableColumn c1 = new TableColumn(viewer.getTable(), SWT.NONE);
		c1.setText("ID");
		TableColumnLayout tableColumnLayoutC1 = new TableColumnLayout();
		tableColumnLayoutC1.setColumnData(c1, new ColumnWeightData(10, 25, true));
		tableComposite.setLayout(tableColumnLayoutC1);

		TableColumn c2 = new TableColumn(viewer.getTable(), SWT.NONE);
		c2.setText("Description");
		TableColumnLayout tableColumnLayoutC2 = new TableColumnLayout();
		tableColumnLayoutC2.setColumnData(c2, new ColumnWeightData(90, 80, true));
		tableComposite.setLayout(tableColumnLayoutC2);

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
									final Text text = new Text(viewer.getTable(), SWT.NONE);
									Listener textListener = new Listener() {
										@Override
										public void handleEvent(final Event e) {
											switch (e.type) {
											case SWT.FocusOut:
												applyDataToDataModel(
														dataInterface.getComponentNames().get(selectedItem),
														item.getText(0), item.getText(0), text.getText(), true,
														selectedItem);
												item.setText(column, text.getText());
												text.dispose();

												break;
											case SWT.Traverse:
												switch (e.detail) {
												case SWT.TRAVERSE_RETURN:
													item.setText(column, text.getText());
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
		updateReponsibilites(dataInterface.getComponentNames().get(selectedItem));
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
	}

}
