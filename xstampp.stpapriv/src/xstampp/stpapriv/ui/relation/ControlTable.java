package xstampp.stpapriv.ui.relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.eclipse.jface.layout.AbstractColumnLayout;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import xstampp.model.ObserverValue;
import xstampp.stpapriv.model.relation.ControlEntry;
import xstampp.stpapriv.model.relation.UnsafeUnsecureController;





public class ControlTable extends AbstractTableComposite implements IStructuredContentProvider{
	private class ControlActionLableProvider extends LabelProvider implements
	ITableLabelProvider, IColorProvider {
		
		public String getColumnText(Object element, int columnIndex) {
			
			ControlEntry entry = (ControlEntry) element;
			switch (columnIndex) {
			case 0:
				return entry.getControlAction();
			case 1:
				return entry.getUnsecureControlAction();
			case 2:
				return null;
			case 3:
				return null;
			case 4:
				return null;
			case 5:
				return entry.getComments();
			
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
		
			ArrayList<?> list = (ArrayList<?>) controlActionViewer.getInput();
			try {
				int index =  list.indexOf(element);
				if ((index % 2) == 0) {
					return PrivacyRelationsView.BACKGROUND;
				} else {	    
					return null;
				}
			}
			catch (Exception e) {
				return null;
			}
		}
	}
	
	
	private TableViewer controlActionViewer;
	private Table controlActionTable;

	public ControlTable(Composite parent, String[] columns) {
		super(parent);
		setLayout(new TableColumnLayout());
		
		controlActionViewer = new TableViewer(this, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		controlActionViewer.setContentProvider(this);
		controlActionViewer.setLabelProvider(new ControlActionLableProvider());
		controlActionTable = controlActionViewer.getTable();
	    controlActionTable.setHeaderVisible(true);
	    controlActionTable.setLinesVisible(true);
		
		CellEditor[] controlActionEditors = new CellEditor[7];
		controlActionEditors[2] = new CheckboxCellEditor(controlActionTable);
	    controlActionEditors[3] = new CheckboxCellEditor(controlActionTable);
	    controlActionEditors[4] = new CheckboxCellEditor(controlActionTable);

	    controlActionEditors[5] = new TextCellEditor(controlActionTable);
	    controlActionViewer.setColumnProperties(columns);
	    controlActionViewer.setCellEditors(controlActionEditors);
		
 // add Columns for Control Actions table (list of control actions)
	    
	    TableColumn caColumn = new TableColumn(controlActionTable, SWT.LEFT);
	    caColumn.setText(PrivacyRelationsView.CONTROL_ACTIONS);
	    ((AbstractColumnLayout) getLayout()).setColumnData(caColumn, new ColumnWeightData(1, 30, false));
	    caColumn = new TableColumn(controlActionTable, SWT.LEFT);
	    caColumn.setText("Privacy-Compromising Control Actions");
	    ((AbstractColumnLayout) getLayout()).setColumnData(caColumn, new ColumnWeightData(1, 30, false));
	    caColumn = new TableColumn(controlActionTable, SWT.LEFT);
	    caColumn.setText(PrivacyRelationsView.PRIVACY_CRITICAL);
	    ((AbstractColumnLayout) getLayout()).setColumnData(caColumn, new ColumnWeightData(1, 30, false));
	    caColumn = new TableColumn(controlActionTable, SWT.LEFT);
	    caColumn.setText(PrivacyRelationsView.SECURITY_CRITICAL);
	    ((AbstractColumnLayout) getLayout()).setColumnData(caColumn, new ColumnWeightData(1, 30, false));
	    caColumn = new TableColumn(controlActionTable, SWT.LEFT);
	    caColumn.setText(PrivacyRelationsView.SAFETY_CRITICAL);
	    ((AbstractColumnLayout) getLayout()).setColumnData(caColumn, new ColumnWeightData(1, 30, false));
	    caColumn = new TableColumn(controlActionTable, SWT.LEFT);
	    caColumn.setText(PrivacyRelationsView.COMMENTS);
	    ((AbstractColumnLayout) getLayout()).setColumnData(caColumn, new ColumnWeightData(1, 30, false));
	    
	    
	 // listener for the checkboxes in the control Action table so they get drawn right
	    controlActionTable.addListener(SWT.PaintItem, new Listener() {

	    	  @Override
		        public void handleEvent(Event event) {
		        	
		            if((event.index == 4)&(event.item.getData().getClass() == ControlEntry.class))  {
		            	
		            	ControlEntry entry = (ControlEntry) event.item.getData();
		            	Image tmpImage = PrivacyRelationsView.UNCHECKED;
		            	if (entry.getSafetyCritical()){
		            		System.out.println(4);
		                tmpImage = PrivacyRelationsView.CHECKED;
		            	}

		                int tmpWidth = 0;
		                int tmpHeight = 0;
		                int tmpX = 0;
		                int tmpY = 0;

		                tmpWidth = controlActionTable.getColumn(event.index).getWidth();
		                tmpHeight = ((TableItem)event.item).getBounds().height;

		                tmpX = tmpImage.getBounds().width;
		                tmpX = (tmpWidth / 2 - tmpX / 2);
		                tmpY = tmpImage.getBounds().height;
		                tmpY = (tmpHeight / 2 - tmpY / 2);
		                if(tmpX <= 0) tmpX = event.x;
		                else tmpX += event.x;
		                if(tmpY <= 0) tmpY = event.y;
		                else tmpY += event.y;
		                event.gc.drawImage(tmpImage, tmpX, tmpY);
		            }
		            else if((event.index == 2)&(event.item.getData().getClass() == ControlEntry.class))  {
		            	
		            	ControlEntry entry = (ControlEntry) event.item.getData();
		            	Image tmpImage = PrivacyRelationsView.UNCHECKED;
		            	if (entry.getPrivacyCritical()){
		            		System.out.println(2);
		                tmpImage = PrivacyRelationsView.CHECKED;
		            	}

		                int tmpWidth = 0;
		                int tmpHeight = 0;
		                int tmpX = 0;
		                int tmpY = 0;

		                tmpWidth = controlActionTable.getColumn(event.index).getWidth();
		                tmpHeight = ((TableItem)event.item).getBounds().height;

		                tmpX = tmpImage.getBounds().width;
		                tmpX = (tmpWidth / 2 - tmpX / 2);
		                tmpY = tmpImage.getBounds().height;
		                tmpY = (tmpHeight / 2 - tmpY / 2);
		                if(tmpX <= 0) tmpX = event.x;
		                else tmpX += event.x;
		                if(tmpY <= 0) tmpY = event.y;
		                else tmpY += event.y;
		                event.gc.drawImage(tmpImage, tmpX, tmpY);
		            }
		            else if((event.index == 3)&(event.item.getData().getClass() == ControlEntry.class))  {
		            	
		            	ControlEntry entry = (ControlEntry) event.item.getData();
		            	Image tmpImage = PrivacyRelationsView.UNCHECKED;
		            	if (entry.getSecurityCritical()){
		            		System.out.println(3);
		                tmpImage = PrivacyRelationsView.CHECKED;
		            	}

		                int tmpWidth = 0;
		                int tmpHeight = 0;
		                int tmpX = 0;
		                int tmpY = 0;

		                tmpWidth = controlActionTable.getColumn(event.index).getWidth();
		                tmpHeight = ((TableItem)event.item).getBounds().height;

		                tmpX = tmpImage.getBounds().width;
		                tmpX = (tmpWidth / 2 - tmpX / 2);
		                tmpY = tmpImage.getBounds().height;
		                tmpY = (tmpHeight / 2 - tmpY / 2);
		                if(tmpX <= 0) tmpX = event.x;
		                else tmpX += event.x;
		                if(tmpY <= 0) tmpY = event.y;
		                else tmpY += event.y;
		                event.gc.drawImage(tmpImage, tmpX, tmpY);
		            }
		        }
		    });
		    setVisible(false);
	}

	@Override
	public void setController(UnsafeUnsecureController controller) {
	    controlActionViewer.setCellModifier(new EntryCellModifier(controlActionViewer,controller.getModel()));
		super.setController(controller);
	}
	@Override
	public void activate() {
		if(dataController==null){
			controlActionViewer.setInput(null);
		}else{
			refreshTable();

		}
		
	  
	  
		
		setVisible(true);
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case SAFETY_CONSTRAINT:
		case CAUSAL_FACTOR:
		case UNSAFE_CONTROL_ACTION:
		case CONTROL_STRUCTURE:	
			this.refreshTable();
			break;

		default:
			break;
		}
	}

	
	@Override
	public boolean refreshTable() {
		if(controlActionViewer.getControl() == null 
		    || controlActionViewer.getControl().isDisposed()
		    || dataController == null){
			return false;
		}
		controlActionViewer.setInput(dataController.getValuesList(false));
		for (int i = 0, n = controlActionTable.getColumnCount(); i < n; i++) {
			controlActionTable.getColumn(i).pack();
		}
		this.controlActionViewer.refresh();
		return true;
		// TODO Auto-generated method stub
		
	}
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	  /**
	   * Returns the objects for the tables
	   */
	  public Object[] getElements(Object inputElement) {
	    return ((List<?>) inputElement).toArray();
	  }

}
