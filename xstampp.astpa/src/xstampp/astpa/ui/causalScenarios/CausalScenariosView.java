/*************************************************************************
 * Copyright (c) 2014-2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **************************************************************************/

package xstampp.astpa.ui.causalScenarios;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.IDataModel;
import xstampp.model.IValueCombie;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.grid.GridCellButton;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridRow;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.IGridCell;
import xstampp.ui.editors.AbstractFilteredEditor;

public class CausalScenariosView extends AbstractFilteredEditor {
	private static final String ID = "xstampp.astpa.ui.causalScenarios";
	private static final String UCA="unsafe control actions";

  private static final String PREFIX = "S1.";
  private GridWrapper grid;
  private IExtendedDataModel dataInterface;
  private DeleteCSAction deleteAction;
  
	public CausalScenariosView() {
		setUseFilter(true);
	}
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
        UUID newUCA = dataInterface
            .addNonFormalRule(parentId, new String(), new String(), new String(), new String(), new String(), IValueCombie.TYPE_ANYTIME);
        grid.activateCell(newUCA); 
      }
      
    }
  }
	
  private class ScenarioEditor extends GridCellTextEditor{
    
    public ScenarioEditor(GridWrapper grid, String initialText, Boolean showDelete, Boolean readOnly, UUID ruleId) {
      super(grid, initialText, showDelete, readOnly,ruleId);
    }
    
    @Override
    public void delete() {
      deleteAction.run();
    }

    @Override
    public void updateDataModel(String newValue) {
      dataInterface.updateRefinedRule(getUUID(), null, null, null, newValue, null, null, -1, null, null);
    }
    
  }
  
	@Override
  public void createPartControl(Composite parent) {
    this.setDataModelInterface(ProjectManager.getContainerInstance()
        .getDataModel(this.getProjectID()));
    parent.setLayout(new GridLayout(1, false));

    updateFilter(); 
    super.createPartControl(parent);
    this.grid = new GridWrapper(parent, new String[] { "ID","Basic Scenario","Safety Constraint" });
    deleteAction = new DeleteCSAction(grid, dataInterface,"Causal Scenarios",PREFIX);
    this.grid.getGrid().setVisible(true);
    this.grid.getGrid().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    this.grid.setColumnratios(new float[]{0.1f,0.45f,0.45f});
    MenuManager menuMgr = new MenuManager();
    Menu menu = menuMgr.createContextMenu(this.grid.getGrid());
    menuMgr.addMenuListener(new ActionMenuListener(deleteAction));
    menuMgr.setRemoveAllWhenShown(true);

    this.grid.getGrid().setMenu(menu);
    this.reloadTable(); 
  }
	
	private void reloadTable() {
	  if(this.grid != null){
      this.grid.clearRows();
      List<AbstractLtlProvider> rulesList = dataInterface.getAllRefinedRules(false);
      for (AbstractLtlProvider rule  : rulesList) {
        if(!isFiltered(rule.getUCALinks(),UCA)){
          GridRow ruleRow = new GridRow(1);
          
          IGridCell cell =new ScenarioEditor(this.grid,PREFIX+rule.getNumber(),true,true, rule.getRuleId());
          ruleRow.addCell(cell);
  
          ruleRow.addCell(new ScenarioEditor(this.grid,rule.getSafetyRule(),false,false, rule.getRuleId()){
            @Override
            public void updateDataModel(String newValue) {
              dataInterface.updateRefinedRule(getUUID(), null, null, null, newValue, null, null, -1, null, null);
            }
          });
          ruleRow.addCell(new ScenarioEditor(this.grid,rule.getRefinedSafetyConstraint(),false,false, rule.getRuleId()){
            @Override
            public void updateDataModel(String newValue) {
              dataInterface.updateRefinedRule(getUUID(), null, null, newValue, null, null, null, -1, null, null);
            }
          });
          grid.addRow(ruleRow);
        }
      }
      GridRow addRow = new GridRow(0);
      addRow.setColumnSpan(0, 3);
      if(getFilterValue() instanceof UUID){
        addRow.addCell(new AddCSButton((UUID) getFilterValue(), ""));
      }
      addRow.addCell(new AddCSButton(null, ""));
      grid.addRow(addRow);
      this.grid.reloadTable();
	  }    
  }

  public void setDataModelInterface(IDataModel dataInterface) {
    this.dataInterface = (DataModelController) dataInterface;
    this.dataInterface.addObserver(this);
  }
	
  @Override
  public String getId() {
    return ID;
  }

  @Override
  protected Map<String, Boolean> getCategories() {
    Map<String, Boolean> categories = new HashMap<>();
    categories.put(UCA, true);
    return categories;
  }

  @Override
  protected String[] getCategoryArray() {
    return new String[]{UCA};
  }
  
  /**
   * refreshes the filter entries from the datamodel 
   * and also calls {@link #reloadTable()} to prevent any 
   * illegal entries
   */
  @Override
  protected void updateFilter() {
    int ucaCount = dataInterface.getAllUnsafeControlActions().size();
    String[] ucaNames=new String[ucaCount];
    UUID[] filterValues = new UUID[ucaCount];
    int index = 0;
    // the loop inserts a string value and an id for every uca to 
    // ensure readability
    for(ICorrespondingUnsafeControlAction uca: dataInterface.getAllUnsafeControlActions()) {
      ucaNames[index]= "UCA1."+dataInterface.getUCANumber(uca.getId()) + " - "
                      + uca.getDescription();
      filterValues[index++] = uca.getId();
    }
    addChoices(UCA, ucaNames);
    addChoiceValues(UCA, filterValues);
    reloadTable();
  }
  
	@Override
	public void update(Observable dataModelController, Object updatedValue) {

    super.update(dataModelController, updatedValue);
    ObserverValue type = (ObserverValue) updatedValue;
    switch (type) {
      case UNSAFE_CONTROL_ACTION:
        updateFilter();
        break;
      case Extended_DATA:
        updateFilter();
        break;
      
      default:
        break;
    }
	}
}