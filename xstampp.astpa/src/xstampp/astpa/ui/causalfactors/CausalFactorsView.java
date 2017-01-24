/*******************************************************************************
 * 
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.causalfactors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import messages.Messages;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.ui.CommonGridView;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.CellButtonLinking;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridCellColored;
import xstampp.ui.common.grid.GridCellLinking;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridRow;
import xstampp.ui.common.grid.GridWrapper;

/**
 * The view to add causal factors to control structure components, edit them and
 * add links to the related hazards.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser, Lukas Balzer
 */
public class CausalFactorsView extends CommonGridView<ICausalFactorDataModel>{

	private static final RGB PARENT_BACKGROUND_COLOR = new RGB(215, 240, 255);
	private static final String CAUSALFACTORS= "Text filter for Causal Factors";
	private static String[] columns = new String[] { Messages.Component,
      Messages.CausalFactors,"Unsafe Control Action", Messages.HazardLinks,"Causal Scenarios",
      Messages.SafetyConstraint, Messages.NotesSlashRationale };
	 /**
   * ViewPart ID.
   */
  public static final String ID = "astpa.steps.step3_2";
  
	private class NewConstraintButton extends CellButton {

    private UUID componentId;
    private UUID factorId;
    private UUID entryId;

		public NewConstraintButton(UUID componentId, UUID factorId,UUID entryId) {
			super(new Rectangle(
          -1, -1,
          GridWrapper.getAddButton16().getBounds().width,
          GridWrapper.getAddButton16().getBounds().height),
          GridWrapper.getAddButton16());
      this.componentId = componentId;
      this.factorId = factorId;
      this.entryId = entryId;
		}

		@Override
		public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
	    CausalFactorEntryData data = new CausalFactorEntryData(entryId);
	    data.setConstraint(new String());
	    getDataModel().changeCausalEntry(componentId, factorId, data);
		}
	}


	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public CausalFactorsView() {
		setUseFilter(true);
		setGlobalCategory("ALL");
	}

	@Override
	protected void updateFilter() {

		this.reloadTable();
	}
	@Override
	public String getId() {
		return CausalFactorsView.ID;
	}

	@Override
	public String getTitle() {
		return Messages.CausalFactorsTable;
	}
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent,columns);
	}
	
	@Override
	public DeleteGridEntryAction<ICausalFactorDataModel> getDeleteAction() {
	  return new DeleteCFAction(getGridWrapper(), getDataModel(), Messages.CausalFactors, null);
	}

	@Override
	protected Map<String, Boolean> getCategories() {
		Map<String, Boolean> categories= new HashMap<>();
		categories.put("ALL", false);
		categories.put(ComponentType.ACTUATOR.name(), true);
		categories.put(ComponentType.CONTROLLER.name(), true);
		categories.put(ComponentType.CONTROLLED_PROCESS.name(), true);
		categories.put(ComponentType.SENSOR.name(), true);
		categories.put(CAUSALFACTORS, false);
		return categories;
	}
	
	@Override
	protected String[] getCategoryArray() {
		return new String[]{"ALL",ComponentType.ACTUATOR.name(), 
				ComponentType.CONTROLLER.name(),
				ComponentType.CONTROLLED_PROCESS.name(),
				ComponentType.SENSOR.name(),
				CAUSALFACTORS};
	}
	
	private boolean isFiltered(ICausalComponent component) {
		//filters for the text filter 
		if(getActiveCategory().equals("ALL")){
			return isFiltered(component.getText());
		}
		else if(getActiveCategory().equals(CAUSALFACTORS)){
			for(ICausalFactor factor: component.getCausalFactors()){
				if(!isFiltered(factor.getText())){
					return false;
				}
			}
			return true;
		}
		return isFiltered(component.getText(),component.getComponentType().name());
	}
	
	@Override
	protected void fillTable() {
	  List<ICausalComponent> components = this.getDataModel().getCausalComponents(null);
		for (ICausalComponent component : components) {
			if(isFiltered(component)){
				continue;
			}
			GridRow csRow = new GridRow(columns.length);
			GridCellText causalComp = new GridCellText(component.getText());
			csRow.addCell(0,causalComp);
//			for(int i=0;i<6; i++){
//			  csRow.addCell(new GridCellColored(getGridWrapper(),CausalFactorsView.PARENT_BACKGROUND_COLOR));
//			}
			getGridWrapper().addRow(csRow);
			Map<UUID,ICorrespondingUnsafeControlAction> ucaMap = new HashMap<>();
      for(ICorrespondingUnsafeControlAction uca : getDataModel().getUCAList(null)){
        ucaMap.put(uca.getId(), uca); 
      }
			//each causal factor is displayed as child row of the causal component
			for (ICausalFactor factor : component.getCausalFactors()) {
				if(isFiltered(factor.getText(),CAUSALFACTORS)){
					continue;
				}
				GridRow factorRow = new GridRow(7,1,new int[]{1});
				factorRow.addCell(1,new CellEditorCausalFactor(getGridWrapper(), getDataModel(), factor
						.getText(), component.getId(),factor.getId()));
				
		   //A new row is added to the factorRow for adding additional entries
//        for(int i=2; i<columns.length;i++){
//          factorRow.addCell(i,new GridCellColored(getGridWrapper(),CausalFactorsView.PARENT_BACKGROUND_COLOR));
//        }
				//the causal factor contains multiple child rows for each causal factor entry
        for(ICausalFactorEntry entry : factor.getAllEntries()){
          addEntryRow(factorRow, entry, factor, component, ucaMap);
        }
        //A new row is added to the factorRow for adding additional entries
        GridRow addEntriesRow = new GridRow(columns.length);
        addEntriesRow.addCell(2,new GridCellButtonAddUCAEntry(component, factor.getId(), getDataModel(),getGrid()));
        for(int i=3; i<columns.length;i++){
          addEntriesRow.addCell(i,new GridCellColored(getGridWrapper(),CausalFactorsView.PARENT_BACKGROUND_COLOR));
        }
        factorRow.addChildRow(addEntriesRow);
				csRow.addChildRow(factorRow);
			}
      
			
			GridRow buttonRow = new GridRow(columns.length);
			buttonRow.addCell(1,new GridCellButtonAddCausalFactor(component,getDataModel()));
			for(int i=2; i <7;i++){
			  buttonRow.addCell(i,new GridCellColored(getGridWrapper(),CausalFactorsView.PARENT_BACKGROUND_COLOR));
      }
			csRow.addChildRow(buttonRow);
		}
	}

	/**
	 * 
	 * @param factorRow
	 * @param entry
	 * @param factor
	 * @param component
	 * @param ucaMap
	 */
	private void addEntryRow(GridRow factorRow, ICausalFactorEntry entry,
	                         ICausalFactor factor,
	                         ICausalComponent component,
	                         Map<UUID,ICorrespondingUnsafeControlAction> ucaMap){
	  GridRow entryRow = new GridRow(columns.length,1,new int[]{2,3,6});
    
    /*
     * Depending on whether the entry is linked to a uca or not
     * the uca column is filled and the hazards are either based on the uca
     * or linkable
     */
    if(entry.getUcaLink() != null && ucaMap.containsKey(entry.getUcaLink())){
      addUCAEntry(entryRow, entry, factor, component, ucaMap);
      
    }else{
      addHazardEntry(entryRow, entry, factor, component);
    }
    entryRow.addCell(6,new CellEditorFactorNote(getGridWrapper(),getDataModel(),component.getId(), factor.getId(),entry));
    factorRow.addChildRow(entryRow);
	}
	
	/**
	 * adds the:</br><h5>uca row with</h5><ul>
	 * <li>uca text (read only)
	 * <li>hazard links (read only)
	 * <li>a row for each scenario
	 * </ul>
	 */
  private void addUCAEntry(GridRow entryRow, ICausalFactorEntry entry,
                          ICausalFactor factor,
                          ICausalComponent component,
                          Map<UUID,ICorrespondingUnsafeControlAction> ucaMap){
	  //add the uca id + description in a read only cell with an delete button
	  String ucaDescription = ucaMap.get(entry.getUcaLink()).getTitle() + "\n"+ucaMap.get(entry.getUcaLink()).getDescription();
    entryRow.addCell(2,new CellEditorCausalEntry(getGridWrapper(), getDataModel(), ucaDescription,
                                      component.getId(), factor.getId(), entry.getId()));
    
    //add the hazard links of the uca, as read only string
    List<UUID> hazIds = getDataModel().getLinksOfUCA(entry.getUcaLink());
    String linkingString = new String();
    List<ITableModel> hazards = getDataModel().getHazards(hazIds);
    if(!hazards.isEmpty()){
      for(ITableModel hazard : hazards){
        linkingString += "H-" +hazard.getNumber() + ",";
      }
      linkingString = linkingString.substring(0, linkingString.length()-1);
    }
    entryRow.addCell(3,new GridCellText(linkingString));

   
    if(entry.getScenarioLinks() != null){
      for(UUID scenarioId : entry.getScenarioLinks()){
        GridRow scenarioRow = new GridRow(columns.length);
        ScenarioType type = getDataModel().getScenarioType(scenarioId);
        if(type != null){
          scenarioRow.addCell(4,new CellEditorCausalScenario(getGridWrapper(), getDataModel(),entry,component,factor, scenarioId, type));
          scenarioRow.addCell(5,new CellEditorCausalConstraint(getGridWrapper(), getDataModel(), scenarioId, type));
          entryRow.addChildRow(scenarioRow);
        }
      }
    }
    //adding two blank cells as placeholder for the scenario rows which have two cells each
    entryRow.addCell(4,new GridCellColored(getGridWrapper(),CausalFactorsView.PARENT_BACKGROUND_COLOR));
    entryRow.addCell(5,new GridCellColored(getGridWrapper(),CausalFactorsView.PARENT_BACKGROUND_COLOR));
    
    GridRow addScenarioRow = new GridRow(columns.length);
    GridCellText scenarioCell = new GridCellText(new String());
    scenarioCell.addCellButton(new CellButtonLinking<ContentProviderScenarios>(getGridWrapper(), 
              new ContentProviderScenarios(getDataModel(), component.getId(), factor.getId(), entry),factor.getId()));
    scenarioCell.addCellButton(new CellButtonAddScenario(entry, component.getId(), factor.getId(), getDataModel()));
    addScenarioRow.addCell(4,scenarioCell);
    addScenarioRow.addCell(5,new GridCellText("test"));
    entryRow.addChildRow(addScenarioRow);
  }
	
  /**
   * adds the:</br><h5>uca row with</h5><ul>
   * <li>an empty cell with a delete (read only)
   * <li>hazard linking cell
   * <li>an empty scenario cell
   * <li>a cell for importing or creating a new safety constraint
   * </ul>
   */
  private void addHazardEntry(GridRow entryRow, ICausalFactorEntry entry,
      ICausalFactor factor,
      ICausalComponent component){

    entryRow.addCell(2,new CellEditorCausalEntry(getGridWrapper(), getDataModel(), new String(),
        component.getId(), factor.getId(), entry.getId()));
    entryRow.addCell(3,new GridCellLinking<ContentProviderHazards>(
        factor.getId(), new ContentProviderHazards(getDataModel(), component.getId(), factor.getId(), entry),
        getGridWrapper()));

    entryRow.addCell(4,new GridCellText(""));
    /*
     * The Safety Constraint is dispayed if available, if the text is
     * null than a new entry can be added or one of the existing constraints 
     * can be imported
     */
    if (entry.getConstraintText() == null) {
      GridCellText constraintsCell = new GridCellText(new String());
      constraintsCell.addCellButton(new NewConstraintButton(component.getId(), factor.getId(),entry.getId()));
      constraintsCell.addCellButton(new CellButtonImportConstraint(getGrid(),entry,component.getId(), factor.getId(),getDataModel()));
      entryRow.addCell(5,constraintsCell);
    } else {
      entryRow.addCell(5,new CellEditorSafetyConstraint(getGridWrapper(), getDataModel(), component.getId(), factor.getId(),entry));
    }
  
  }

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
			switch ((ObserverValue) updatedValue) {
			case CONTROL_STRUCTURE:
			case UNSAFE_CONTROL_ACTION:
			case HAZARD:
			case Extended_DATA:
			case CAUSAL_FACTOR:
        reloadTable();
				break;
			default:
				break;
			}
	}


}
