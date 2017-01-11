package xstpa.ui.tables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.ui.causalScenarios.ActionMenuListener;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.AbstractLtlProviderData;
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
import xstpa.ui.View;

public class LTLPropertiesTable extends AbstractFilteredEditor{

  private static String categoryLTL = "ID";
  private static String[] columns=new String[]{View.ENTRY_ID,View.LTL_RULES};
  private static String PREFIX = "SSR1."; //$NON-NLS-1$
  private Action deleteAction;
  private GridWrapper grid;
  private IExtendedDataModel dataModel;
  
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
        
        UUID newUCA = dataModel
            .addRuleEntry(IExtendedDataModel.ScenarioType.CUSTOM_LTL,data,null, IValueCombie.TYPE_ANYTIME);
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
        AbstractLtlProviderData data = new AbstractLtlProviderData();
        data.setLtl(newValue);
        ((IExtendedDataModel) ProjectManager.getContainerInstance()
            .getDataModel(getProjectID()))
            .updateRefinedRule(getUUID(),data,null);
      }

      @Override
      protected void editorOpening() {
        // TODO Auto-generated method stub
        
      }

      @Override
      protected void editorClosing() {
        // TODO Auto-generated method stub
        
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
		return "xstpa.editor.ltl";
	}

	@Override
	public void dispose() {
		ProjectManager.getContainerInstance().getDataModel(getProjectID()).deleteObserver(LTLPropertiesTable.this);
	}
	@Override
	public void update(final Observable IExtendedDataModel, Object updatedValue) {
		final ObserverValue value= (ObserverValue) updatedValue; 
		switch(value){
			case Extended_DATA:
		default:
			break;
		}
	}
	
	@Override
  public void createPartControl(Composite parent) {
    this.setDataModelInterface(ProjectManager.getContainerInstance()
        .getDataModel(this.getProjectID()));
    parent.setLayout(new GridLayout(1, false));

    updateFilter(); 
    super.createPartControl(parent);
    this.grid = new GridWrapper(parent, columns);
    deleteAction = new deleteLTLaction(grid, dataModel,"LTL",PREFIX);
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

  private void setDataModelInterface(IDataModel dataModel) {
    this.dataModel = (IExtendedDataModel) dataModel;    
  }

  private void reloadTable() {
    if(this.grid != null){
      this.grid.clearRows();
      List<AbstractLtlProvider> rulesList = dataModel.getAllRefinedRules(true,false,true);
      for (AbstractLtlProvider rule  : rulesList) {
        if(!isFiltered(rule.getNumber(),categoryLTL)){
          GridRow ruleRow = new GridRow(columns.length);
          
          IGridCell cell =new ScenarioEditor(this.grid,PREFIX+rule.getNumber(),true,true, rule.getRuleId());
          ruleRow.addCell(0,cell);
  
          ruleRow.addCell(1,new ScenarioEditor(this.grid,rule.getLtlProperty(),false,true, rule.getRuleId()){
            @Override
            public void updateDataModel(String newValue) {
              AbstractLtlProviderData data = new AbstractLtlProviderData();
              data.setLtl(newValue);
              dataModel.updateRefinedRule(getUUID(),data, null);
            }
          });
          grid.addRow(ruleRow);
        }
      }
      GridRow addRow = new GridRow(0);
      addRow.setColumnSpan(0, 3);
      if(getFilterValue() instanceof UUID){
        addRow.addCell(0,new AddCSButton((UUID) getFilterValue(), ""));
      }
      addRow.addCell(0,new AddCSButton(null, ""));
      grid.addRow(addRow);
      this.grid.reloadTable();
    }    
  }
}

