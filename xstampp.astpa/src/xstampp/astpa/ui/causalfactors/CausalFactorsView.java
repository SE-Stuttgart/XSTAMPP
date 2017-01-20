/*******************************************************************************
 * 
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import messages.Messages;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.causalfactor.CausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.CellButtonLinking;
import xstampp.ui.common.grid.GridCellColored;
import xstampp.ui.common.grid.GridCellLinking;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridRow;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.editors.AbstractFilteredEditor;

/**
 * The view to add causal factors to control structure components, edit them and
 * add links to the related hazards.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser, Lukas Balzer
 */
public class CausalFactorsView extends AbstractFilteredEditor{

	private static final RGB PARENT_BACKGROUND_COLOR = new RGB(215, 240, 255);
	private int internalUpdates;
	private static final String CAUSALFACTORS= "Text filter for Causal Factors";
	private Map<UUID,CausalFactor> factorsToUUIDs;
	private DeleteCFAction deleteAction;
	private static String[] columns = new String[] { Messages.Component,
      Messages.CausalFactors,"Unsafe Control Action", Messages.HazardLinks,"Basic Scenarios",
      Messages.SafetyConstraint, Messages.NotesSlashRationale };
	 /**
   * ViewPart ID.
   */
  public static final String ID = "astpa.steps.step3_2";

  private ICausalFactorDataModel dataInterface = null;

  /**
   * The log4j logger.
   */
  private static final Logger LOGGER = Logger.getRootLogger();


  private GridWrapper grid;

  private boolean lockreload;

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
	    dataInterface.changeCausalEntry(componentId, factorId, data);
		}
	}


	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public CausalFactorsView() {
		this.factorsToUUIDs = new HashMap<>();
		this.dataInterface = null;
		this.grid = null;
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
		this.internalUpdates = 0;
		this.setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(this.getProjectID()));
		parent.setLayout(new GridLayout(1, false));
		super.createPartControl(parent);
		this.grid = new GridWrapper(parent, columns);
		this.grid.getGrid().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.deleteAction = new DeleteCFAction(grid, dataInterface, Messages.CausalFactors, null);
		this.reloadTable();
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
	/**
	 * Fill the table.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param components
	 *            the list of components.
	 */
	private void fillTable() {
	  List<ICausalComponent> components = this.dataInterface.getCausalComponents(null);
		for (ICausalComponent component : components) {
			if(isFiltered(component)){
				continue;
			}
			GridRow csRow = new GridRow(columns.length);
			GridCellText causalComp = new GridCellText(component.getText());
			csRow.addCell(0,causalComp);
//			for(int i=0;i<6; i++){
//			  csRow.addCell(new GridCellColored(this.grid,CausalFactorsView.PARENT_BACKGROUND_COLOR));
//			}
			this.grid.addRow(csRow);
			Map<UUID,ICorrespondingUnsafeControlAction> ucaMap = new HashMap<>();
      for(ICorrespondingUnsafeControlAction uca : dataInterface.getUCAList(null)){
        ucaMap.put(uca.getId(), uca); 
      }
			//each causal factor is displayed as child row of the causal component
			for (ICausalFactor factor : component.getCausalFactors()) {
				if(isFiltered(factor.getText(),CAUSALFACTORS)){
					continue;
				}
				GridRow factorRow = new GridRow(7,1,new int[]{1});
				factorRow.addCell(1,new CellEditorCausalFactor(this.grid, dataInterface, factor
						.getText(), component.getId(),factor.getId()));
				
		   //A new row is added to the factorRow for adding additional entries
        for(int i=2; i<columns.length;i++){
          factorRow.addCell(i,new GridCellColored(this.grid,CausalFactorsView.PARENT_BACKGROUND_COLOR));
        }
				//the causal factor contains multiple child rows for each causal factor entry
        for(ICausalFactorEntry entry : factor.getAllEntries()){
          addEntryRow(factorRow, entry, factor, component, ucaMap);
        }
        //A new row is added to the factorRow for adding additional entries
        GridRow addEntriesRow = new GridRow(columns.length);
        addEntriesRow.addCell(2,new GridCellButtonAddUCAEntry(component, factor.getId(), dataInterface,grid.getGrid()));
        for(int i=3; i<columns.length;i++){
          addEntriesRow.addCell(i,new GridCellColored(this.grid,CausalFactorsView.PARENT_BACKGROUND_COLOR));
        }
        factorRow.addChildRow(addEntriesRow);
				csRow.addChildRow(factorRow);
			}
      
			
			GridRow buttonRow = new GridRow(columns.length);
			buttonRow.addCell(1,new GridCellButtonAddCausalFactor(component,dataInterface));
			for(int i=2; i <7;i++){
			  buttonRow.addCell(i,new GridCellColored(this.grid,CausalFactorsView.PARENT_BACKGROUND_COLOR));
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
    entryRow.addCell(6,new CellEditorFactorNote(this.grid,dataInterface,component.getId(), factor.getId(),entry));
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
    entryRow.addCell(2,new CellEditorCausalEntry(grid, dataInterface, ucaDescription,
                                      component.getId(), factor.getId(), entry.getId()));
    
    //add the hazard links of the uca, as read only string
    List<UUID> hazIds = dataInterface.getLinksOfUCA(entry.getUcaLink());
    String linkingString = new String();
    for(ITableModel hazard : dataInterface.getHazards(hazIds)){
      linkingString += "H-" +hazard.getNumber() + ",";
    }
    entryRow.addCell(3,new GridCellText(linkingString.substring(0, linkingString.length()-1)));

    //adding two blank cells as placeholder for the scenario rows which have two cells each
    entryRow.addCell(4,new GridCellColored(this.grid,CausalFactorsView.PARENT_BACKGROUND_COLOR));
    entryRow.addCell(5,new GridCellColored(this.grid,CausalFactorsView.PARENT_BACKGROUND_COLOR));
    
    if(entry.getScenarioLinks() != null){
      for(UUID scenarioId : entry.getScenarioLinks()){
        GridRow scenarioRow = new GridRow(columns.length);
        ScenarioType type = dataInterface.getScenarioType(scenarioId);
        if(type != null){
          scenarioRow.addCell(4,new CellEditorCausalScenario(grid, dataInterface,entry,component,factor, scenarioId, type));
          scenarioRow.addCell(5,new CellEditorCausalConstraint(grid, dataInterface, scenarioId, type));
          entryRow.addChildRow(scenarioRow);
        }
      }
    }

    GridRow addScenarioRow = new GridRow(columns.length);
    GridCellText scenarioCell = new GridCellText(new String());
    scenarioCell.addCellButton(new CellButtonLinking<ContentProviderScenarios>(grid, 
              new ContentProviderScenarios(dataInterface, component.getId(), factor.getId(), entry),factor.getId()));
    scenarioCell.addCellButton(new CellButtonAddScenario(entry, component.getId(), factor.getId(), dataInterface));
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

    entryRow.addCell(2,new CellEditorCausalEntry(grid, dataInterface, new String(),
        component.getId(), factor.getId(), entry.getId()));
    entryRow.addCell(3,new GridCellLinking<ContentProviderHazards>(
        factor.getId(), new ContentProviderHazards(dataInterface, component.getId(), factor.getId(), entry),
        this.grid));

    entryRow.addCell(4,new GridCellText(""));
    /*
     * The Safety Constraint is dispayed if available, if the text is
     * null than a new entry can be added or one of the existing constraints 
     * can be imported
     */
    if (entry.getConstraintText() == null) {
      GridCellText constraintsCell = new GridCellText(new String());
      constraintsCell.addCellButton(new NewConstraintButton(component.getId(), factor.getId(),entry.getId()));
      constraintsCell.addCellButton(new CellButtonImportConstraint(grid.getGrid(),entry,component.getId(), factor.getId(),dataInterface));
      entryRow.addCell(5,constraintsCell);
    } else {
      entryRow.addCell(5,new CellEditorSafetyConstraint(grid, dataInterface, component.getId(), factor.getId(),entry));
    }
  
  }
  
	/**
	 * Reload the whole table.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	private void reloadTable() {
		if(!this.lockreload){
			this.lockreload = true;
			int tmp= this.grid.getGrid().getVerticalBar().getSelection();
			
	
			this.factorsToUUIDs = new HashMap<>();
			this.grid.clearRows();
			this.fillTable();
			this.grid.reloadTable();
			this.lockreload = false;
			this.grid.getGrid().setTopIndex(tmp);
		}
	}

	/**
	 * sets the data model object for this editor
	 *
	 * @author Lukas
	 *
	 * @param dataInterface the data model object
	 */
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (ICausalFactorDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		if(internalUpdates > 0){
			internalUpdates--;
		}else{
			switch ((ObserverValue) updatedValue) {
			case CONTROL_STRUCTURE:
			case UNSAFE_CONTROL_ACTION:
			case HAZARD:
			case Extended_DATA:
			case CAUSAL_FACTOR:
				Display.getDefault().syncExec(new Runnable() {
	
					@Override
					public void run() {
						CausalFactorsView.this.reloadTable();
					}
				});
				break;
			default:
				break;
			}
		}
	}


	@Override
	public void dispose() {
		this.dataInterface.deleteObserver(this);
		super.dispose();
	}
	
	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) {
		if(!this.lockreload && this.factorsToUUIDs.size() > 0){
			this.lockreload = true;
			
			this.lockreload = false;
			this.factorsToUUIDs = new HashMap<>();
		}
		super.partBroughtToTop(arg0);
	}
}
