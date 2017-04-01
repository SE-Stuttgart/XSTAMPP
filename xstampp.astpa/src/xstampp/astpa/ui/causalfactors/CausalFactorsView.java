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

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

import messages.Messages;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.ui.CommonGridView;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.CellButtonLinking;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridCellColored;
import xstampp.ui.common.grid.GridCellLinking;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridRow;

/**
 * The view to add causal factors to control structure components, edit them and
 * add links to the related hazards.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser, Lukas Balzer
 */
public class CausalFactorsView extends CommonGridView<ICausalFactorDataModel>{

	private static final RGB PARENT_BACKGROUND_COLOR = new RGB(215, 240, 255);
	private static final String CAUSALFACTORS= "Text filter for Causal Factors";
  private static String[] _withScenarioColumns = new String[] { Messages.Component,
      Messages.CausalFactors,"Unsafe Control Action", Messages.HazardLinks,"Causal Scenarios",
      Messages.SafetyConstraint, Messages.NotesSlashRationale };
  private static String[] _withoutColumns = new String[] { Messages.Component,
      Messages.CausalFactors,"Unsafe Control Action", Messages.HazardLinks,
      Messages.SafetyConstraint, Messages.NotesSlashRationale };
	 /**
   * ViewPart ID.
   */
  public static final String ID = "astpa.steps.step3_2";


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
		super.createPartControl(parent,_withoutColumns);
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

    if(this.getDataModel().isUseScenarios()) {
      this.getGridWrapper().setColumnLabels(_withScenarioColumns);
    } else {
      this.getGridWrapper().setColumnLabels(_withoutColumns);
    }
	  List<ICausalComponent> components = this.getDataModel().getCausalComponents(null);
		for (ICausalComponent component : components) {
			if(isFiltered(component)){
				continue;
			}
			GridRow componentRow = new GridRow(this.getGridWrapper().getColumnLabels().length);
			int cellNumber = 0;
			componentRow.addCell(cellNumber,new GridCellText(component.getText()));
			getGridWrapper().addRow(componentRow);
			Map<UUID,ICorrespondingUnsafeControlAction> ucaMap = new HashMap<>();
      for(ICorrespondingUnsafeControlAction uca : getDataModel().getUCAList(null)){
        ucaMap.put(uca.getId(), uca); 
      }
			//each causal factor is displayed as child row of the causal component
			for (ICausalFactor factor : component.getCausalFactors()) {
				if(!isFiltered(factor.getText(),CAUSALFACTORS)){
			    componentRow.addChildRow(createCausalFactorRow(cellNumber, component, factor, ucaMap));
				}
			}
			GridRow buttonRow = new GridRow(this.getGridWrapper().getColumnLabels().length);
			buttonRow.addCell(++cellNumber,new GridCellButtonAddCausalFactor(component,getDataModel()));
			buttonRow.setColumnSpan(cellNumber, getGridWrapper().getColumnLabels().length - cellNumber - 1);
			componentRow.addChildRow(buttonRow);
		}
	}

	private GridRow createCausalFactorRow(int cellNumber,
	     ICausalComponent component,
	     ICausalFactor factor,
	     Map<UUID,ICorrespondingUnsafeControlAction> ucaMap) {
	  GridRow factorRow = new GridRow(this.getGridWrapper().getColumnLabels().length-1,1,new int[]{1});
    factorRow.addCell(++cellNumber,new CellEditorCausalFactor(getGridWrapper(), getDataModel(), factor
        .getText(), component.getId(),factor.getId()));
    
    //the causal factor contains multiple child rows for each causal factor entry
    for(ICausalFactorEntry entry : factor.getAllEntries()){
      factorRow.addChildRow(createEntryRow(entry, factor, component, ucaMap,cellNumber));
    }
    //A new row is added to the factorRow for adding additional entries
    GridRow addEntriesRow = new GridRow(this.getGridWrapper().getColumnLabels().length);
    addEntriesRow.addCell(++cellNumber,new GridCellButtonAddUCAEntry(component, factor.getId(), getDataModel(),getGrid()));

    addEntriesRow.setColumnSpan(cellNumber, getGridWrapper().getColumnLabels().length - cellNumber - 1);
    factorRow.addChildRow(addEntriesRow);
    return factorRow;
	}
	/**
	 * 
	 * @param factorRow
	 * @param entry
	 * @param factor
	 * @param component
	 * @param ucaMap
	 * @param cellNumber 
	 */
	private GridRow createEntryRow(ICausalFactorEntry entry,
	                         ICausalFactor factor,
	                         ICausalComponent component,
	                         Map<UUID,ICorrespondingUnsafeControlAction> ucaMap, int cellNumber){
	  GridRow entryRow;
    /*
     * Depending on whether the entry is linked to a uca or not
     * the uca column is filled and the hazards are either based on the uca
     * or linkable
     */
    if(entry.getUcaLink() != null && ucaMap.containsKey(entry.getUcaLink())){
      entryRow = createUCAEntry(entry, factor, component, ucaMap,cellNumber);
      
    }else{
      entryRow = createHazardEntry(entry, factor, component,cellNumber);
    }
    entryRow.addCell(++cellNumber,new CellEditorFactorNote(getGridWrapper(),getDataModel(),component.getId(), factor.getId(),entry));
    return entryRow;
	}
	
	/**
	 * adds the:</br><h5>uca row with</h5><ul>
	 * <li>uca text (read only)
	 * <li>hazard links (read only)
	 * <li>a row for each scenario
	 * </ul>
	 * @param cellNumber 
	 */
  private GridRow createUCAEntry(ICausalFactorEntry entry,
                          ICausalFactor factor,
                          ICausalComponent component,
                          Map<UUID,ICorrespondingUnsafeControlAction> ucaMap, int cellNumber){
    GridRow entryRow = new GridRow(this.getGridWrapper().getColumnLabels().length,1,new int[]{2,3,6});
    //add the uca id + description in a read only cell with an delete button
	  String ucaDescription = ucaMap.get(entry.getUcaLink()).getTitle() + "\n"+ucaMap.get(entry.getUcaLink()).getDescription();
    entryRow.addCell(++cellNumber,new CellEditorCausalEntry(getGridWrapper(), getDataModel(), ucaDescription,
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
    entryRow.addCell(++cellNumber,new GridCellText(linkingString));

    cellNumber = createConstraints(entryRow, ++cellNumber, entry, component, factor);
    
    entryRow.addCell(++cellNumber, new CellEditorFactorNote(getGridWrapper(), getDataModel(), getProjectID(), factor.getId(), entry));
    return entryRow;
  }
	
  private int createConstraints(GridRow entryRow,
      int cellNumber,
      ICausalFactorEntry entry,
      ICausalComponent component,
      ICausalFactor factor){

    if(this.getDataModel().isUseScenarios()) {
      entryRow.addCell(cellNumber,new GridCellColored(getGridWrapper(),CausalFactorsView.PARENT_BACKGROUND_COLOR));
  
      //Creating a child row for displaying and editing of the causal scenarios
      createScenarioRow(entryRow, entry, factor, component,cellNumber);
      cellNumber++;
    }
    /*
     * The Safety Constraint is dispayed if available, if the text is
     * null than a new entry can be added or one of the existing constraints 
     * can be imported
     */
    if (entry.getConstraintText() == null) {
      GridCellText constraintsCell = new GridCellText(new String());
      constraintsCell.addCellButton(new NewConstraintButton(component.getId(), factor.getId(),entry.getId(), getDataModel()));
      constraintsCell.addCellButton(new CellButtonImportConstraint(getGrid(),entry,component.getId(), factor.getId(),getDataModel()));
      entryRow.addCell(cellNumber,constraintsCell);
    } else {
      entryRow.addCell(cellNumber,new CellEditorSafetyConstraint(getGridWrapper(), getDataModel(), component.getId(), factor.getId(),entry));
    }
    return cellNumber;
  }
  
  private void createScenarioRow(GridRow row, ICausalFactorEntry entry,
      ICausalFactor factor,
      ICausalComponent component, int cellNumber){
    
    if(entry.getScenarioLinks() != null){
      for(UUID scenarioId : entry.getScenarioLinks()){
        ScenarioType type = getDataModel().getScenarioType(scenarioId);
        if(type != null){
          GridRow scenarioRow = new GridRow(this.getGridWrapper().getColumnLabels().length);
          scenarioRow.addCell(cellNumber,new CellEditorCausalScenario(getGridWrapper(),
                              getDataModel(),
                              entry,component,factor, scenarioId, type));
          scenarioRow.addCell(cellNumber + 1,new CellEditorCausalScenarioConstraint(getGridWrapper(),
                              getDataModel(), scenarioId, type));

          row.addChildRow(scenarioRow);
        }
      }
    }

    GridRow scenarioRow = new GridRow(this.getGridWrapper().getColumnLabels().length);
    GridCellText scenarioCell = new GridCellText("Add a new scenario");
    scenarioCell.addCellButton(new CellButtonLinking<ContentProviderScenarios>(getGridWrapper(), 
              new ContentProviderScenarios(getDataModel(),
                  component.getId(), factor.getId(), entry),factor.getId()));
    scenarioCell.addCellButton(new CellButtonAddScenario(entry, component.getId(), factor.getId(), getDataModel()));
    scenarioRow.setColumnSpan(cellNumber, 1);
    scenarioRow.addCell(cellNumber,scenarioCell);
    row.addChildRow(scenarioRow);
  }
  
  /**
   * adds the:</br><h5>uca row with</h5><ul>
   * <li>an empty cell with a delete (read only)
   * <li>hazard linking cell
   * <li>an empty scenario cell
   * <li>a cell for importing or creating a new safety constraint
   * </ul>
   * @param cellNumber 
   */
  private GridRow createHazardEntry(ICausalFactorEntry entry,
      ICausalFactor factor,
      ICausalComponent component, int cellNumber){
    GridRow entryRow = new GridRow(this.getGridWrapper().getColumnLabels().length,1,new int[]{2,3,6});

    entryRow.addCell(++cellNumber,new CellEditorCausalEntry(getGridWrapper(), getDataModel(), new String(),
        component.getId(), factor.getId(), entry.getId()));
    entryRow.addCell(++cellNumber,new GridCellLinking<ContentProviderHazards>(
        factor.getId(), new ContentProviderHazards(getDataModel(), component.getId(), factor.getId(), entry),
        getGridWrapper()));

    entryRow.addCell(++cellNumber,new GridCellText(""));
    /*
     * The Safety Constraint is dispayed if available, if the text is
     * null than a new entry can be added or one of the existing constraints 
     * can be imported
     */
    if (entry.getConstraintText() == null) {
      GridCellText constraintsCell = new GridCellText(new String());
      constraintsCell.addCellButton(new NewConstraintButton(component.getId(), factor.getId(),entry.getId(), getDataModel()));
      constraintsCell.addCellButton(new CellButtonImportConstraint(getGrid(),entry,component.getId(), factor.getId(),getDataModel()));
      entryRow.addCell(++cellNumber,constraintsCell);
    } else {
      entryRow.addCell(++cellNumber,new CellEditorSafetyConstraint(getGridWrapper(), getDataModel(), component.getId(), factor.getId(),entry));
    }
    return entryRow;
  }

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
    IDataModel controller = (IDataModel) dataModelController;
    if(controller.getProjectName().equals(getDataModel().getProjectName())){
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


}
