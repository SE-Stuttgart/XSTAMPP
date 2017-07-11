/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.stpapriv.ui;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.TreeMap;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.ObserverValue;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;

public class STPAResultsTable extends StandartEditorPart{
	private class LtlViewLabelProvider extends LabelProvider implements
	ITableLabelProvider,IColorProvider{
		private boolean isEven=false;
		private int count =2;
		@Override
		public Image getColumnImage(Object element, int columnIndex) {

			return null;
		}
		@Override
		public Color getBackground(Object element) {
			if(count == 0){
				isEven= !isEven;
				count = 2;
			}
			count--;
			if(isEven){
				return new Color(null, 230,230,230);
			}
			return new Color(null, 255,255,255);
		}
		@Override
		public String getColumnText(Object element, int columnIndex) {
			if(element instanceof Entry<?,?>){
				switch(columnIndex){
				case 0: 
					return (String) ((Entry) element).getKey();
				case 1:
					return (String) ((Entry) element).getValue();
				}
				
			}
			return null;
		}
		@Override
		public Color getForeground(Object element) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	private TableViewer scViewer;
	private Table ltlTable;
	private PrivacyController dataModel;

	public void createPartControl(Composite parent) {
		this.dataModel =(PrivacyController) ProjectManager.getContainerInstance().getDataModel(getProjectID());
		this.dataModel.addObserver(this);
		Composite back = new Composite(parent, SWT.NONE);
		TableColumnLayout tLayout = new TableColumnLayout();
		back.setLayout(tLayout);
		
		scViewer = new TableViewer(back, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL );
		scViewer.setContentProvider(new ArrayContentProvider());
		scViewer.setLabelProvider(new LtlViewLabelProvider());
	    ltlTable = scViewer.getTable();
	    ltlTable.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				ProjectManager.getContainerInstance().getDataModel(getProjectID()).deleteObserver(STPAResultsTable.this);
			}
		});
	    ltlTable.setHeaderVisible(true);
	    ltlTable.setLinesVisible(true);
	    ltlTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    // add columns for ltl tables	   
	    TableColumn ltlColumn = new TableColumn(ltlTable, SWT.LEFT);
	    ltlColumn.setText("ID");
	    tLayout.setColumnData(ltlColumn, new ColumnWeightData(1, 10, false));
	    
	    ltlColumn = new TableColumn(ltlTable, SWT.LEFT);
	    ltlColumn.setText("Privacy Constraint");
	    tLayout.setColumnData(ltlColumn, new ColumnWeightData(8, 30, false));
	    ltlTable.pack();
	    fetchInput();
	}

	private void fetchInput(){
		Map<String,String> content = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String stepIdO1 =o1.split("\\.")[0];
				String stepIdO2 =o2.split("\\.")[0];
				int res = stepIdO1.compareTo(stepIdO2); 
				if(res == 0){
					int nrO1 = Integer.parseInt(o1.split("\\.")[1]);
					int nrO2 = Integer.parseInt(o2.split("\\.")[1]);
					res = nrO1 - nrO2; 
				}
				return res;
			};
		});
		
		for(ICorrespondingUnsafeControlAction uca : dataModel.getAllUnsafeControlActions()){
			ITableModel constraint = uca.getCorrespondingSafetyConstraint();
			if(!(constraint == null || constraint.getText().isEmpty())){
				content.put("PC1."+dataModel.getUCANumber(uca.getId()), constraint.getText());
			}
		}
		for(AbstractLTLProvider rule: dataModel.getAllScenarios(true,true,false)){
			content.put("PC2."+rule.getNumber(), rule.getRefinedSafetyConstraint());
		}
		scViewer.setInput(content.entrySet());
	}
	@Override
	public String getId() {
		return "stpa.results.editor.ltl";
	}

	@Override
	public void dispose() {
		ProjectManager.getContainerInstance().getDataModel(getProjectID()).deleteObserver(STPAResultsTable.this);
	}
	@Override
	public void update(final Observable dataModelController, Object updatedValue) {
		final ObserverValue value= (ObserverValue) updatedValue; 
		switch(value){
			case UNSAFE_CONTROL_ACTION:
			    fetchInput();
		default:
			break;
		}
	}
}
