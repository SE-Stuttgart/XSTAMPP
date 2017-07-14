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
package xstpapriv.ui.ltl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.ui.CommonGridView;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IValueCombie;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridCellButton;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridRow;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.IGridCell;

public class LTLPropertiesTable extends CommonGridView<IExtendedDataModel>{

  private static String categoryLTL = "ID";
  private static String[] columns=new String[]{"ID","LTL"};
  private static String PREFIX = "SPR1."; //$NON-NLS-1$
  
  private class AddCSButton extends GridCellButton {

    private UUID parentId;

    public AddCSButton(UUID parentID, String text) {
      super(text);
      this.parentId = parentID;
    }

    @Override
    public void onMouseDown(MouseEvent e,
        org.eclipse.swt.graphics.Point relativeMouse,
        Rectangle cellBounds) {
      if(e.button == 1){
        AbstractLtlProviderData data = new AbstractLtlProviderData();
        UUID newUCA = getDataModel()
            .addRuleEntry(IExtendedDataModel.ScenarioType.CUSTOM_LTL,data,null, IValueCombie.TYPE_ANYTIME);
//        getGridWrapper().activateCell(newUCA); 
        getGridWrapper().activateCell(newUCA);
      }
      
    }
  }

  @Override
  protected void updateFilter() {
    reloadTable();
  }
  
  private class ScenarioEditor extends GridCellTextEditor{
      

      public ScenarioEditor(GridWrapper grid, String initialText, Boolean showDelete, Boolean readOnly, UUID ruleId) {
        super(grid, initialText, showDelete, readOnly,ruleId);
      }
      
      @Override
      public void delete() {
        deleteEntry();
      }
  
      @Override
      public void updateDataModel(String newValue) {
        AbstractLtlProviderData data = new AbstractLtlProviderData();
        data.setLtl(newValue);
        getDataModel().updateRefinedRule(getUUID(),data,null);
      }
      
      @Override
      public void activate() {
        
      }
      @Override
      protected void editorOpening() {
        getDataModel().lockUpdate();
      }

      @Override
      protected void editorClosing() {
        getDataModel().releaseLockAndUpdate(new ObserverValue[]{ObserverValue.Extended_DATA});
      }
  }
  
  public LTLPropertiesTable() {
		setUseFilter(true);
	}

  @Override
  protected Map<String, Boolean> getCategories() {
    Map<String, Boolean> categories = new HashMap<>();
    categories.put(categoryLTL, true);
    return categories;
  }

  @Override
  protected String[] getCategoryArray() {
    return new String[]{categoryLTL};
  }
	@Override
	public String getId() {
		return "xstpapriv.editor.ltl";
	}

	@Override
	public void update(final Observable IExtendedDataModel, Object updatedValue) {
		final ObserverValue value= (ObserverValue) updatedValue; 
		switch(value){
			case Extended_DATA:
			  reloadTable();
		default:
			break;
		}
	}
	@Override
  public void createPartControl(Composite parent) {
    super.createPartControl(parent,columns);
    getGridWrapper().setColumnratios(new float[]{0.1f,0.9f});
    updateFilter(); 
  }

  @Override
  public DeleteGridEntryAction<IExtendedDataModel> getDeleteAction() {
    return new DeleteLTLaction(getGridWrapper(), getDataModel(),"LTL",PREFIX);
  }

  protected void fillTable() {
    if(this.getGridWrapper() != null){
      this.getGridWrapper().clearRows();
      List<AbstractLTLProvider> rulesList = getDataModel().getAllScenarios(true,false,false);
      List<AbstractLTLProvider> scenarioList = getDataModel().getAllScenarios(false, false, true);
      int causalIndex = 0;
      int basicIndex = 0;
      int loopSize = rulesList.size() + scenarioList.size();
      for (int i = 0; i < loopSize;i++) {
        AbstractLTLProvider rule = null;
        boolean showDelete = false;
        if(basicIndex < rulesList.size()){
          rule = rulesList.get(basicIndex);
        }
        if(scenarioList.size() > causalIndex  && 
            (rule == null || scenarioList.get(causalIndex).getNumber() < rule.getNumber())){
          rule = scenarioList.get(causalIndex);
          causalIndex++;
          showDelete = true;
        }else{
          basicIndex++;
        }
        if(rule != null && !isFiltered(rule.getNumber(),categoryLTL)){
          GridRow ruleRow = new GridRow(columns.length);
          IGridCell cell =new ScenarioEditor(this.getGridWrapper(),PREFIX+rule.getNumber(),showDelete,true, rule.getRuleId());
          ruleRow.addCell(0,cell);
  
          ruleRow.addCell(1,new ScenarioEditor(this.getGridWrapper(),rule.getLtlProperty(),false,!showDelete, rule.getRuleId()){
            @Override
            public void updateDataModel(String newValue) {
              AbstractLtlProviderData data = new AbstractLtlProviderData();
              data.setLtl(newValue);
              getDataModel().updateRefinedRule(getUUID(),data, null);
            }
          });
          getGridWrapper().addRow(ruleRow);
        }
      }
      GridRow addRow = new GridRow(columns.length);
      addRow.setColumnSpan(0, 3);
      if(getFilterValue() instanceof UUID){
        addRow.addCell(0,new AddCSButton((UUID) getFilterValue(), ""));
      }
      addRow.addCell(0,new AddCSButton(null, ""));
      getGridWrapper().addRow(addRow);
      this.getGridWrapper().reloadTable();
    }    
  }
}

