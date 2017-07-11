package xstampp.stpapriv.ui.results;

import java.util.Observable;
import java.util.Observer;

import org.apache.poi.ss.formula.functions.Even;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.preferences.IPreferenceConstants;
import xstampp.stpapriv.diagram.DiagramView;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.stpapriv.model.results.ConstraintResult;
import xstampp.stpapriv.ui.relation.SecView;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.ui.editors.interfaces.IEditorBase;

public class ResultEditor extends StandartEditorPart implements Observer {
	private static final String CORRESPONDING_CONSTRAINT = "Corresponding Constraint";
	private static final String SECURITY_CONSTRAINT = "Privacy Constraint";
	private static final String COLUMN_ID = "ID";
	private static final String STPA_SEC_STEP = "STPA-Priv Step";
	private static final String STPA_SEC_RESULTS = "STPA-Priv Results";
	private static final String BOTH = "All three";
	private static final String SAFETY_RELATED = "Safety related";
	private static final String SECURITY_RELATED = "Security related";
	private static final String PRIVACY_RELATED = "Privacy related";
	private static final String ALL = "All";
	public static final String COMMON_TABLE_VIEW_SHOW_NUMBER_COLUMN = "commonTableView.showNumberColumn";
	public static final String[] RESULTS_COLUMNS = { STPA_SEC_STEP, COLUMN_ID, SECURITY_CONSTRAINT,
			CORRESPONDING_CONSTRAINT, PRIVACY_RELATED,SECURITY_RELATED, SAFETY_RELATED };

	private static final Image DELETE = Activator.getImageDescriptor("/icons/buttons/commontables/remove.png") //$NON-NLS-1$
			.createImage();
	private static String id;
	public static final String ID = "stpapriv.steps.step3_4";
	private TableViewer tableViewer;

	private Label itemsLabel, filterLabel2;
	private Composite tableContainer;
	private TableColumnLayout tableColumnLayout;
	private TableViewerColumn stpasecstepColumn;
	private TableViewerColumn idColumn;
	private TableViewerColumn secConstraintColumn;
	private TableViewerColumn corrConstraintColumn;
	private TableViewerColumn isSafeColumn;
	private TableViewerColumn isSecureColumn;
	private TableViewerColumn isPrivateColumn;

	private PrivacyController dataInterface;
	DiagramView view;
	private ResultsTableFilter filter;

	public ResultsTableFilter getFilter() {
		return filter;
	}

	public void setFilter(ResultsTableFilter filter) {
		this.filter = filter;
	}

	private class ControlActionLableProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {

		public String getColumnText(Object element, int columnIndex) {

			ConstraintResult entry = (ConstraintResult) element;
			switch (columnIndex) {
			case 0:
				return entry.getStpastep();
			case 1:
				return entry.getScId();
			case 2:
				return entry.getSecurityConstraint();

			case 3:
				if (entry.getIdCompare()[0] == 2) {
					return entry.getRelatedId();
				}
			case 4:

				return null;
			case 5:
				return null;
			case 6:
				return null;
			}
			return null;
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return null;
		}

		@Override
		public org.eclipse.swt.graphics.Color getForeground(Object element) {

			return null;
		}

		@Override
		public Color getBackground(Object element) {

			return null;
		}

	}

	public TableViewer getTableViewer() {
		return this.tableViewer;
	}

	public void setTableViewer(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}

	public Composite getTableContainer() {
		return this.tableContainer;
	}

	public void setTableContainer(Composite tableContainer) {
		this.tableContainer = tableContainer;
	}

	public TableColumnLayout getTableColumnLayout() {
		return this.tableColumnLayout;
	}

	public void setTableColumnLayout(TableColumnLayout tableColumnLayout) {
		this.tableColumnLayout = tableColumnLayout;
	}

	public TableViewerColumn getIdColumn() {
		return this.stpasecstepColumn;
	}

	public void setIdColumn(TableViewerColumn idColumn) {
		this.stpasecstepColumn = idColumn;
	}

	public TableViewerColumn getTitleColumn() {
		return this.idColumn;
	}

	public void setTitleColumn(TableViewerColumn titleColumn) {
		this.idColumn = titleColumn;
	}

	@Override
	public void createPartControl(Composite parent) {

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		this.tableContainer = new Composite(sashForm, SWT.NONE);
		this.tableContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.tableContainer.setLayout(new GridLayout(1, true));
		Composite leftHeadComposite = new Composite(this.tableContainer, SWT.NONE);
		leftHeadComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		final int leftHeadColumns = 6;
		leftHeadComposite.setLayout(new GridLayout(leftHeadColumns, true));
		this.itemsLabel = new Label(leftHeadComposite, SWT.LEAD);
		this.itemsLabel.setFont(new Font(Display.getCurrent(),
				PreferenceConverter.getFontData(IEditorBase.STORE, IPreferenceConstants.DEFAULT_FONT)));
		this.itemsLabel.setText(STPA_SEC_RESULTS);
		this.itemsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		Composite tableComposite = new Composite(tableContainer, SWT.NONE);
		tableComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		tableComposite.setLayout(new GridLayout(leftHeadColumns, false));

		Label filterLabel = new Label(tableComposite, SWT.RIGHT);
		filterLabel.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		filterLabel.setText(Messages.Filter);

		filterLabel2 = new Label(tableContainer, SWT.RIGHT);
		filterLabel2.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));

		Composite tableComposite2 = new Composite(this.tableContainer, SWT.NONE);
		tableComposite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.tableColumnLayout = new TableColumnLayout();
		tableComposite2.setLayout(this.tableColumnLayout);
		this.setTableViewer(new TableViewer(tableComposite2,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP));
		this.setDataModelInterface(ProjectManager.getContainerInstance().getDataModel(this.getProjectID()));
		this.getTableViewer().setContentProvider(new ArrayContentProvider());
		Button bothButton = new Button(tableComposite, SWT.RADIO);
		bothButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		bothButton.setText(ALL);
		bothButton.setSelection(true);

		Composite buttonComposite = new Composite(this.tableContainer, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		buttonComposite.setLayout(new GridLayout(6, true));
		Button deleteItemsButton = new Button(buttonComposite, SWT.PUSH);
		deleteItemsButton.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
		deleteItemsButton.setImage(DELETE);

//		tableViewer.getTable().addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseUp(MouseEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void mouseDown(MouseEvent e) {
//				if (e.button == 3) {
//					Menu menu = new Menu(tableViewer.getTable());
//					tableViewer.getTable().setMenu(menu);
//					MenuItem item = new MenuItem(menu, SWT.NONE);
//					item.setText("Show Diagram");
//					item.addListener(SWT.Selection, new Listener() {
//
//						@Override
//						public void handleEvent(Event event) {
//							try {
//								IStructuredSelection selection = (IStructuredSelection) getTableViewer().getSelection();
//								// = new DiagramView();
//
//								// view.setInput(((ConstraintResult)selection.getFirstElement()).getTemp());
//								if (view == null) {
//									view = (DiagramView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
//											.getActivePage().showView("xstampp.stpapriv.diagram");
//									view.setInput(((ConstraintResult) selection.getFirstElement()), dataInterface);
//									view.refresh();
//								} else {
//									view = (DiagramView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
//											.getActivePage().showView("xstampp.stpapriv.diagram");
//									view.setInput(((ConstraintResult) selection.getFirstElement()), dataInterface);
//									view.refresh();
//								}
//
//							} catch (PartInitException e2) {
//								// TODO Auto-generated catch block
//								e2.printStackTrace();
//							}
//
//						}
//					});
//					//
//				}
//
//			}
//
//			@Override
//			public void mouseDoubleClick(MouseEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		deleteItemsButton.addListener(SWT.Selection, new Listener() {
//
//			@Override
//			public void handleEvent(Event event) {
//				ResultEditor.this.deleteItems();
//			}
//		});
		Button bothButtonpriv = new Button(tableComposite, SWT.RADIO);
		bothButtonpriv.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		bothButtonpriv.setText(PRIVACY_RELATED);
		bothButtonpriv.setSelection(true);
		bothButtonpriv.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				filter.setFilterType(PRIVACY_RELATED);
				refreshView();
				updatelabel();
				tableViewer.refresh();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Button bothButton2 = new Button(tableComposite, SWT.RADIO);
		bothButton2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		bothButton2.setText(SECURITY_RELATED);
		bothButton2.setSelection(true);
		bothButton2.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				filter.setFilterType(SECURITY_RELATED);
				refreshView();
				updatelabel();
				tableViewer.refresh();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Button bothButton3 = new Button(tableComposite, SWT.RADIO);
		bothButton3.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		bothButton3.setText(SAFETY_RELATED);
		bothButton3.setSelection(true);
		bothButton3.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				filter.setFilterType(SAFETY_RELATED);
				refreshView();
				updatelabel();
				tableViewer.refresh();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		Button bothButton4 = new Button(tableComposite, SWT.RADIO);
		bothButton4.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
		bothButton4.setText(BOTH);
		bothButton4.setSelection(true);
		bothButton4.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				filter.setFilterType(BOTH);
				refreshView();
				updatelabel();
				tableViewer.refresh();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		getDataInterface().getConstraintController().setModel(getDataInterface());

		CellEditor[] controlActionEditors = new CellEditor[7];
		controlActionEditors[4] = new CheckboxCellEditor(tableViewer.getTable());
		controlActionEditors[5] = new CheckboxCellEditor(tableViewer.getTable());
		controlActionEditors[6] = new CheckboxCellEditor(tableViewer.getTable());

		tableViewer.setColumnProperties(RESULTS_COLUMNS);
		tableViewer.setCellEditors(controlActionEditors);

		updatelabel();
		this.stpasecstepColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.stpasecstepColumn.getColumn().setText(STPA_SEC_STEP);
		final int stpasecWeight = 20;
		final int stpasecMinWidth = 39;
		this.tableColumnLayout.setColumnData(this.stpasecstepColumn.getColumn(),
				new ColumnWeightData(stpasecWeight, stpasecMinWidth, true));

		this.idColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.idColumn.getColumn().setText(COLUMN_ID);
		final int idWeight = 5;
		final int idMinWidth = 50;
		this.tableColumnLayout.setColumnData(this.idColumn.getColumn(),
				new ColumnWeightData(idWeight, idMinWidth, true));

		this.secConstraintColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.secConstraintColumn.getColumn().setText(SECURITY_CONSTRAINT);
		final int constraintWeight = 100;
		final int constraintMinWidth = 50;
		this.tableColumnLayout.setColumnData(this.secConstraintColumn.getColumn(),
				new ColumnWeightData(constraintWeight, constraintMinWidth, true));

		this.corrConstraintColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.corrConstraintColumn.getColumn().setText(CORRESPONDING_CONSTRAINT);
		final int corrconstraintWeight = 18;
		final int corrconstraintMinWidth = 50;
		this.tableColumnLayout.setColumnData(this.corrConstraintColumn.getColumn(),
				new ColumnWeightData(corrconstraintWeight, corrconstraintMinWidth, true));
		
		this.isPrivateColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.isPrivateColumn.getColumn().setText(PRIVACY_RELATED);
		final int isPrivateWeight = 11;
		final int isPrivateMinWidth = 50;
		this.tableColumnLayout.setColumnData(this.isPrivateColumn.getColumn(),
				new ColumnWeightData(isPrivateWeight, isPrivateMinWidth, true));

		this.isSecureColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.isSecureColumn.getColumn().setText(SECURITY_RELATED);
		final int isSecureWeight = 11;
		final int isSecureMinWidth = 50;
		this.tableColumnLayout.setColumnData(this.isSecureColumn.getColumn(),
				new ColumnWeightData(isSecureWeight, isSecureMinWidth, true));

		this.isSafeColumn = new TableViewerColumn(this.getTableViewer(), SWT.NONE);
		this.isSafeColumn.getColumn().setText(SAFETY_RELATED);
		final int isSafeWeight = 10;
		final int isSafeMinWidth = 50;

		this.tableColumnLayout.setColumnData(this.isSafeColumn.getColumn(),
				new ColumnWeightData(isSafeWeight, isSafeMinWidth, true));
		this.getTableViewer().setLabelProvider(new ControlActionLableProvider());
		this.getTableViewer().getTable().setLinesVisible(true);
		this.getTableViewer().getTable().setHeaderVisible(true);

		tableViewer.getTable().addListener(SWT.PaintItem, new Listener() {

			@Override
			public void handleEvent(Event event) {

				if ((event.index == 6) & (event.item.getData().getClass() == ConstraintResult.class)) {

					ConstraintResult entry = (ConstraintResult) event.item.getData();
					if (!(entry.getIdCompare()[0] == 0)) {
						Image tmpImage = SecView.UNCHECKED;
						if (entry.isSafe()) {
							tmpImage = SecView.CHECKED;
						}

						int tmpWidth = 0;
						int tmpHeight = 0;
						int tmpX = 0;
						int tmpY = 0;

						tmpWidth = tableViewer.getTable().getColumn(event.index).getWidth();
						tmpHeight = ((TableItem) event.item).getBounds().height;

						tmpX = tmpImage.getBounds().width;
						tmpX = (tmpWidth / 2 - tmpX / 2);
						tmpY = tmpImage.getBounds().height;
						tmpY = (tmpHeight / 2 - tmpY / 2);
						if (tmpX <= 0)
							tmpX = event.x;
						else
							tmpX += event.x;
						if (tmpY <= 0)
							tmpY = event.y;
						else
							tmpY += event.y;
						event.gc.drawImage(tmpImage, tmpX, tmpY);
					}

				} else if ((event.index == 5) & (event.item.getData().getClass() == ConstraintResult.class)) {
					ConstraintResult entry = (ConstraintResult) event.item.getData();
					if (!(entry.getIdCompare()[0] == 0)) {
						Image tmpImage = SecView.UNCHECKED;
						if (entry.isSecure()) {
							tmpImage = SecView.CHECKED;
						}

						int tmpWidth = 0;
						int tmpHeight = 0;
						int tmpX = 0;
						int tmpY = 0;

						tmpWidth = tableViewer.getTable().getColumn(event.index).getWidth();
						tmpHeight = ((TableItem) event.item).getBounds().height;

						tmpX = tmpImage.getBounds().width;
						tmpX = (tmpWidth / 2 - tmpX / 2);
						tmpY = tmpImage.getBounds().height;
						tmpY = (tmpHeight / 2 - tmpY / 2);
						if (tmpX <= 0)
							tmpX = event.x;
						else
							tmpX += event.x;
						if (tmpY <= 0)
							tmpY = event.y;
						else
							tmpY += event.y;
						event.gc.drawImage(tmpImage, tmpX, tmpY);
					}

				}else if ((event.index == 4) & (event.item.getData().getClass() == ConstraintResult.class)) {
					ConstraintResult entry = (ConstraintResult) event.item.getData();
					if (!(entry.getIdCompare()[0] == 0)) {
						Image tmpImage = SecView.UNCHECKED;
						if (entry.isPrivate()) {
							tmpImage = SecView.CHECKED;
						}

						int tmpWidth = 0;
						int tmpHeight = 0;
						int tmpX = 0;
						int tmpY = 0;

						tmpWidth = tableViewer.getTable().getColumn(event.index).getWidth();
						tmpHeight = ((TableItem) event.item).getBounds().height;

						tmpX = tmpImage.getBounds().width;
						tmpX = (tmpWidth / 2 - tmpX / 2);
						tmpY = tmpImage.getBounds().height;
						tmpY = (tmpHeight / 2 - tmpY / 2);
						if (tmpX <= 0)
							tmpX = event.x;
						else
							tmpX += event.x;
						if (tmpY <= 0)
							tmpY = event.y;
						else
							tmpY += event.y;
						event.gc.drawImage(tmpImage, tmpX, tmpY);
					}

				}
			}
		});
		this.updateTable();
		this.setFilter(new ResultsTableFilter());
		this.getTableViewer().addFilter(this.getFilter());
	}

	public void refreshView() {

		this.updateTable();

		this.getTableViewer().refresh(true, true);
	}

	@Override
	public String getId() {
		return ResultEditor.id;
	}

	@Override
	public String getTitle() {
		return STPA_SEC_RESULTS;
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case SAFETY_CONSTRAINT:
		case CAUSAL_FACTOR:
		case UNSAFE_CONTROL_ACTION:
			this.refreshView();
			break;

		default:
			break;
		}
	}

	public void updateTable() {
		ResultEditor.this.getTableViewer().setInput(this.getDataInterface().getAllConstraintResults());

	};

	@SuppressWarnings("unchecked")
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (PrivacyController) dataInterface;
		tableViewer.setCellModifier(new EntryCellModifier(tableViewer, getDataInterface(), filterLabel2));

		dataInterface.addObserver(this);

	}

	public PrivacyController getDataInterface() {
		return dataInterface;
	};

	private void updatelabel() {
		filterLabel2.setText("Privacy related: " + this.dataInterface.getConstraintController().getPrivateValueCount()
				+" Security related: " + this.dataInterface.getConstraintController().getSecureValueCount()
				+ "  Safety related: " + this.dataInterface.getConstraintController().getSafeValueCount() + "  All three: "
				+ this.dataInterface.getConstraintController().getBothValueCount());
	}

	public void setDataInterface(PrivacyController data) {

		this.dataInterface = data;
	};

	@Override
	public void dispose() {
		this.getDataInterface().deleteObserver(this);
		super.dispose();
	}

	public final void deleteItems() {
		final int maxNumOfDisplayedEntries = 10;
		IStructuredSelection selection = (IStructuredSelection) getTableViewer().getSelection();
		if (selection.size() > 0 && selection.getFirstElement() instanceof ConstraintResult) {
			Object[] models = selection.toArray();
			int shownIds = Math.min(models.length, maxNumOfDisplayedEntries);
			String modelIds = new String();
			if (models.length == 1) {
				modelIds += "Do you really want to delete the following entry?\n";
			} else if (models.length == getTableViewer().getTable().getItemCount()) {
				modelIds += "Do you really want to delete all list entries?\n";
			} else {
				modelIds += "Do you really want to delete all of the following entries?\n";
			}
			modelIds += "\n";
			for (int i = 0; i < shownIds; i++) {
				ConstraintResult model = ((ConstraintResult) models[i]);
				modelIds += model.getScId() + " - " + model.getSecurityConstraint().trim() + "\n";
			}
			if (models.length > maxNumOfDisplayedEntries) {
				modelIds += "..." + (models.length - maxNumOfDisplayedEntries) + " more";
			}
			if (MessageDialog.openQuestion(this.getTableContainer().getShell(), Messages.Confirm, modelIds)) {
				getDataInterface().lockUpdate();
				for (Object model : models) {
					deleteEntry(((ConstraintResult) model));
				}
				getDataInterface().releaseLockAndUpdate(new ObserverValue[0]);
				updateTable();
				this.refreshView();
			}
		} else {
			MessageDialog.openInformation(this.getTableContainer().getShell(), Messages.Information,
					Messages.NoAccidentSelected);
		}
	}

	protected void deleteEntry(ConstraintResult model) {
		if (model.getIdCompare()[0] == 0) {
			this.getDataInterface().removeSafetyConstraint(model.getId());
		} else if (model.getIdCompare()[0] == 1) {
			this.getDataInterface().setCorrespondingSafetyConstraint(model.getId(), "");
		} else if (model.getIdCompare()[0] == 2) {
			this.getDataInterface().setRefinedSecurityConstraint(model.getId(), "");
		}
		this.getDataInterface().removeAccident(model.getId());
	}

	@Override
	public void partActivated(IWorkbenchPart arg0) {
		if (arg0 == this) {
			ResultEditor.this.itemsLabel.setFont(new Font(Display.getCurrent(),
					PreferenceConverter.getFontData(IEditorBase.STORE, IPreferenceConstants.DEFAULT_FONT)));

		}
		super.partActivated(arg0);
	}

}
