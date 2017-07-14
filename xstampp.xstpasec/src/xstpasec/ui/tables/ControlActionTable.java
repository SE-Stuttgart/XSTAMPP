/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstpasec.ui.tables;

import java.util.ArrayList;

import org.eclipse.jface.layout.AbstractColumnLayout;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import xstpasec.model.ControlActionEntry;
import xstpasec.model.XSTPADataController;
import xstpasec.ui.View;
import xstpasec.ui.tables.utils.EntryCellModifier;
import xstpasec.ui.tables.utils.MainViewContentProvider;

public class ControlActionTable extends AbstractTableComposite{
	private class ControlActionLableProvider extends LabelProvider implements
	ITableLabelProvider, IColorProvider {
		
		public String getColumnText(Object element, int columnIndex) {
			
			ControlActionEntry entry = (ControlActionEntry) element;
			switch (columnIndex) {
			case 0:
				return entry.getControlAction();
			case 1:
				return null;
			case 2:
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
					return View.BACKGROUND;
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

	public ControlActionTable(Composite parent) {
		super(parent);
		setLayout(new TableColumnLayout());
		
		controlActionViewer = new TableViewer(this, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		controlActionViewer.setContentProvider(new MainViewContentProvider());
		controlActionViewer.setLabelProvider(new ControlActionLableProvider());
		controlActionTable = controlActionViewer.getTable();
	    controlActionTable.setHeaderVisible(true);
	    controlActionTable.setLinesVisible(true);
		
		CellEditor[] controlActionEditors = new CellEditor[4];
	    controlActionEditors[1] = new CheckboxCellEditor(controlActionTable);
	    controlActionEditors[2] = new TextCellEditor(controlActionTable);
	    controlActionViewer.setColumnProperties(View.CA_PROPS_COLUMNS);
	    controlActionViewer.setCellEditors(controlActionEditors);
		
 // add Columns for Control Actions table (list of control actions)
	    
	    TableColumn caColumn = new TableColumn(controlActionTable, SWT.LEFT);
	    caColumn.setText(View.CONTROL_ACTIONS);
	    ((AbstractColumnLayout) getLayout()).setColumnData(caColumn, new ColumnWeightData(1, 30, false));
	    caColumn = new TableColumn(controlActionTable, SWT.LEFT);
	    caColumn.setText(View.SAFETY_CRITICAL);
	    ((AbstractColumnLayout) getLayout()).setColumnData(caColumn, new ColumnWeightData(1, 30, false));
	    caColumn = new TableColumn(controlActionTable, SWT.LEFT);
	    caColumn.setText(View.COMMENTS);
	    ((AbstractColumnLayout) getLayout()).setColumnData(caColumn, new ColumnWeightData(1, 30, false));

	    
	 // listener for the checkboxes in the control Action table so they get drawn right
	    controlActionTable.addListener(SWT.PaintItem, new Listener() {

	        @Override
	        public void handleEvent(Event event) {
	            // if column 7 (Safety critical), draw the right image
	        	
	            if((event.index == 1)&(event.item.getData().getClass() == ControlActionEntry.class))  {
	            	ControlActionEntry entry = (ControlActionEntry) event.item.getData();
	            	Image tmpImage = View.UNCHECKED;
	            	if (entry.getSafetyCritical()){
	                tmpImage = View.CHECKED;
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
	public void setController(XSTPADataController controller) {
	    controlActionViewer.setCellModifier(new EntryCellModifier(controlActionViewer,controller.getModel()));
		super.setController(controller);
	}
	@Override
	public void activate() {
		refreshTable();
		controlActionViewer.getControl().setFocus();
	  
	  
		for (int i = 0, n = controlActionTable.getColumnCount(); i < n; i++) {
			controlActionTable.getColumn(i).pack();
		}
		setVisible(true);
	}

	@Override
	public boolean refreshTable() {
		if(controlActionViewer.getControl() == null 
		    || controlActionViewer.getControl().isDisposed()
		    || dataController == null){
			return false;
		}
		ArrayList<ControlActionEntry> contentList = new ArrayList<>();
		contentList.addAll(dataController.getDependenciesIFProvided());
		controlActionViewer.setInput(contentList);
		return true;
		// TODO Auto-generated method stub
		
	}

}
