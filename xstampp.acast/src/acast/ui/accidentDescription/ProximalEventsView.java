package acast.ui.accidentDescription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
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

import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;
import acast.Activator;
import acast.model.interfaces.IProximalEventsViewDataModel;

public class ProximalEventsView extends StandartEditorPart implements IPropertyChangeListener {

	// Table column names/properties
	public static final String Id = "ID";

	public static final String Date = "Date";

	public static final String Time = "Time";

	public static final String Description = "Description";

	private int count;

	private List<ProximalEvent> events;

	private ProximalEvent event;

	private int tableSize = 0;

	/**
	 * Interface to communicate with the data model.
	 */
	private IProximalEventsViewDataModel dataInterface;

	/**
	 * ViewPart ID.
	 */
	public static final String ID = "acast.steps.step1_3";

	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	private TableViewer viewer;

	@Override
	public String getId() {
		return this.ID;
	}

	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (IProximalEventsViewDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {

	}

	public void applyDataToDataModel() {
		events = new ArrayList<ProximalEvent>();
		for (TableItem i : viewer.getTable().getItems()) {
			event = new ProximalEvent();
			event.setID(Integer.valueOf(i.getText(0)));
			event.setDate(i.getText(1));
			event.setTime(i.getText(2));
			event.setDescription(i.getText(3));
			events.add(event);
		}
		this.dataInterface.setEventList(events);
	}

	public void updateEventList() {
		int i = 1;
		events = this.dataInterface.getEventList();
		if (events != null && events.size() > 0) {
			for (ProximalEvent e : events) {
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { e.getID() + "", e.getDate(), e.getTime(), e.getDescription() });
				if (i % 2 != 0) {
					item.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
				}
				i++;
			}
		}
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case UNSAVED_CHANGES:

			break;
		case SAVE:
			break;
		default:
			break;
		}
	}

	@Override
	public void createPartControl(final Composite parent) {
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
				if (partRef.getId().equals("acast.steps.step1_3")) {
					PlatformUI.getPreferenceStore().firePropertyChangeEvent("currentSelection", "", "close");

				}
			}

			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}
		});

		this.setDataModelInterface(ProjectManager.getContainerInstance().getDataModel(this.getProjectID()));
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);

		if (this.dataInterface.getEventList() != null) {
			count = this.dataInterface.getEventList().size() + 1;
		} else {
			count = 1;
		}

		parent.setLayout(new GridLayout(1, false));
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		container.setLayout(new GridLayout(1, false));

		Label lblProximalEvents = new Label(container, SWT.NONE);
		lblProximalEvents.setBounds(0, 0, 55, 15);
		lblProximalEvents.setText("Identify Proximal Events");

		final Composite tableComposite = new Composite(container, SWT.NONE);
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		viewer = new TableViewer(tableComposite, SWT.FULL_SELECTION | SWT.H_SCROLL);

		TableColumn c1 = new TableColumn(viewer.getTable(), SWT.NONE);
		c1.setText(ProximalEventsView.Id);
		TableColumnLayout tableColumnLayoutC1 = new TableColumnLayout();
		tableColumnLayoutC1.setColumnData(c1, new ColumnWeightData(10, 40, true));
		tableComposite.setLayout(tableColumnLayoutC1);

		TableColumn c2 = new TableColumn(viewer.getTable(), SWT.NONE);
		c2.setText(ProximalEventsView.Date);
		TableColumnLayout tableColumnLayoutC2 = new TableColumnLayout();
		tableColumnLayoutC2.setColumnData(c2, new ColumnWeightData(25, 80, true));
		tableComposite.setLayout(tableColumnLayoutC2);

		TableColumn c3 = new TableColumn(viewer.getTable(), SWT.NONE);
		c3.setText(ProximalEventsView.Time);
		TableColumnLayout tableColumnLayoutC3 = new TableColumnLayout();
		tableColumnLayoutC3.setColumnData(c3, new ColumnWeightData(25, 80, true));
		tableComposite.setLayout(tableColumnLayoutC3);

		TableColumn c4 = new TableColumn(viewer.getTable(), SWT.NONE);
		c4.setText(ProximalEventsView.Description);
		TableColumnLayout tableColumnLayoutC4 = new TableColumnLayout();
		tableColumnLayoutC4.setColumnData(c4, new ColumnWeightData(65, SWT.FILL, true));
		tableComposite.setLayout(tableColumnLayoutC4);

		tableComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent event) {
				if (viewer.getTable().getClientArea().width > 0) {
					int width = 0;
					Table table = viewer.getTable();
					for (TableColumn c : table.getColumns()) {
						width += c.getWidth();
					}
					int columnCount = table.getColumnCount();
					Rectangle area = table.getClientArea();

					TableColumn lastCol = table.getColumns()[columnCount - 1];
					lastCol.setWidth(lastCol.getWidth() + (tableComposite.getClientArea().width - width));
				}
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
									final DateTime date = new DateTime(viewer.getTable(), SWT.DATE);
									Listener dateListener = new Listener() {
										@Override
										public void handleEvent(final Event e) {
											switch (e.type) {
											case SWT.FocusOut:
												String day;
												String month;
												String year = date.getYear() + "";
												if (date.getDay() < 10) {
													day = "0" + date.getDay();
												} else {
													day = date.getDay() + "";
												}
												int monthT = date.getMonth() + 1;
												if (monthT < 10) {

													month = "0" + monthT;
												} else {
													month = monthT + "";
												}
												String currentDate = day + "." + month + "." + year;
												item.setText(column, currentDate);
												date.dispose();
												applyDataToDataModel();
												break;
											case SWT.Traverse:
												switch (e.detail) {
												case SWT.TRAVERSE_RETURN:
													String curDay;
													String curMonth;
													String curYear = date.getYear() + "";
													if (date.getDay() < 10) {
														curDay = "0" + date.getDay();
													} else {
														curDay = date.getDay() + "";
													}
													int monthR = date.getMonth() + 1;
													if (monthR < 10) {

														curMonth = "0" + monthR;
													} else {
														curMonth = monthR + "";
													}
													String currentDate2 = curDay + "." + curMonth + "." + curYear;
													item.setText(column, currentDate2);
												case SWT.TRAVERSE_ESCAPE:
													date.dispose();
													e.doit = false;
												}
												break;
											}
										}
									};
									date.addListener(SWT.FocusOut, dateListener);
									date.addListener(SWT.Traverse, dateListener);
									editor.setEditor(date, item, i);

									date.setDay(Integer.valueOf(item.getText(i).substring(0, 2)));
									date.setMonth(Integer.valueOf(item.getText(i).substring(3, 5)) - 1);
									date.setYear(Integer.valueOf(item.getText(i).substring(6, 10)));

									date.setFocus();
									return;

								} else if (column == 2) {
									final DateTime date = new DateTime(viewer.getTable(), SWT.TIME);
									Listener dateListener = new Listener() {
										@Override
										public void handleEvent(final Event e) {
											switch (e.type) {
											case SWT.FocusOut:
												String hour;
												String min;
												String sec;
												if (date.getHours() < 10) {
													hour = "0" + date.getHours();
												} else {
													hour = date.getHours() + "";
												}
												if (date.getMinutes() < 10) {
													min = "0" + date.getMinutes();
												} else {
													min = date.getMinutes() + "";
												}
												if (date.getSeconds() < 10) {
													sec = "0" + date.getSeconds();
												} else {
													sec = date.getSeconds() + "";
												}
												String currentDate = hour + ":" + min + ":" + sec;
												item.setText(column, currentDate);
												date.dispose();
												applyDataToDataModel();
												break;
											case SWT.Traverse:
												switch (e.detail) {
												case SWT.TRAVERSE_RETURN:
													String curHour;
													String curMin;
													String curSec;
													if (date.getHours() < 10) {
														curHour = "0" + date.getHours();
													} else {
														curHour = date.getHours() + "";
													}
													if (date.getMinutes() < 10) {
														curMin = "0" + date.getMinutes();
													} else {
														curMin = date.getMinutes() + "";
													}
													if (date.getSeconds() < 10) {
														curSec = "0" + date.getSeconds();
													} else {
														curSec = date.getSeconds() + "";
													}
													String currentDate2 = curHour + ":" + curMin + ":" + curSec;
													item.setText(column, currentDate2);
												case SWT.TRAVERSE_ESCAPE:
													date.dispose();
													e.doit = false;
												}
												break;
											}
										}
									};
									date.addListener(SWT.FocusOut, dateListener);
									date.addListener(SWT.Traverse, dateListener);
									editor.setEditor(date, item, i);

									date.setHours(Integer.valueOf(item.getText(i).substring(0, 2)));
									date.setMinutes(Integer.valueOf(item.getText(i).substring(3, 5)));
									date.setSeconds(Integer.valueOf(item.getText(i).substring(6, 8)));
									date.setFocus();
									return;
								} else if (column == 3) {
									final Text text = new Text(viewer.getTable(), SWT.NONE);
									Listener textListener = new Listener() {
										@Override
										public void handleEvent(final Event e) {
											switch (e.type) {
											case SWT.FocusOut:
												item.setText(column, text.getText());
												text.dispose();
												applyDataToDataModel();
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

		updateEventList();

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.setBounds(0, 0, 75, 25);
		btnAdd.setImage(Activator.getImageDescriptor("/icons/buttons/commontables/add.png").createImage());

		btnAdd.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				String hour;
				String min;
				String sec;

				count = viewer.getTable().getItemCount() + 1;
				TableItem item = new TableItem(viewer.getTable(), SWT.None);

				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 1);
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String formatted = format1.format(cal.getTime());
				String[] dates = formatted.split("-");
				if (cal.getTime().getHours() < 10) {
					hour = "0" + cal.getTime().getHours();
				} else {
					hour = "" + cal.getTime().getHours();
				}
				if (cal.getTime().getMinutes() < 10) {
					min = "0" + cal.getTime().getMinutes();
				} else {
					min = "" + cal.getTime().getMinutes();
				}
				if (cal.getTime().getSeconds() < 10) {
					sec = "0" + cal.getTime().getSeconds();
				} else {
					sec = "" + cal.getTime().getSeconds();
				}

				item.setText(new String[] { count + "", dates[2] + "." + dates[1] + "." + dates[0],
						hour + ":" + min + ":" + sec, "" });
				if (count % 2 != 0) {
					item.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
				}
				applyDataToDataModel();
			}
		});

		Button btnDelete = new Button(composite, SWT.NONE);
		btnDelete.setBounds(0, 0, 75, 25);
		btnDelete.setImage(Activator.getImageDescriptor("/icons/buttons/commontables/remove.png").createImage());
		btnDelete.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (viewer.getTable().getSelectionIndex() == -1) {
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Warning");
					dialog.setMessage("No Event selected");

					dialog.open();
					return;
				}
				int itemIndex = viewer.getTable().getSelectionIndex();
				viewer.getTable().remove(itemIndex);
				for (int i = itemIndex; i < viewer.getTable().getItemCount(); i++) {
					viewer.getTable().getItem(i).setText(0, i + 1 + "");
				}

				applyDataToDataModel();

			}
		});

	}

}
