/*******************************************************************************
 * 
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.causalfactors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.swt.widgets.Composite;

import messages.Messages;
import xstampp.astpa.model.BadReferenceModel;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.ui.CommonGridView;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.grid.CellButtonLinking;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridRow;
import xstampp.ui.common.grid.IGridCell;
import xstampp.usermanagement.api.AccessRights;

/**
 * The view to add causal factors to control structure components, edit them and add links to the
 * related hazards.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser, Lukas Balzer
 */
public class CausalFactorsView extends CommonGridView<ICausalFactorDataModel> {

  private static final String CAUSALFACTORS = "Text filter for Causal Factors";

  /**
   * ViewPart ID.
   */
  public static final String ID = "astpa.steps.step3_2";

  private CausalMethodologySetting setting;

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser
   * 
   */
  public CausalFactorsView() {
    setUseFilter(true);
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
    ICausalFactorDataModel dataModel = (ICausalFactorDataModel) ProjectManager.getContainerInstance()
        .getDataModel(this.getProjectID());
    if (dataModel.getCausalFactorController().analyseFactorsPerUCA()) {
      setting = new UCAScenarioSetting();
    } else {
      setting = new LegacySetting();
    }
    super.createPartControl(parent, setting.getColumns());
    addUCAFilter();
  }

  protected void addUCAFilter() {
    List<ICorrespondingUnsafeControlAction> ucaList = this.getDataModel().getUCAList(null);
    String[] names = new String[ucaList.size()];
    UUID[] values = new UUID[ucaList.size()];
    for (int i = 0; i < ucaList.size(); i++) {
      names[i] = ucaList.get(i).getTitle();
      values[i] = ucaList.get(i).getId();
    }
    addChoices("UCA", names);
    addChoiceValues("UCA", values);
  }

  @Override
  public DeleteGridEntryAction<ICausalFactorDataModel> getDeleteAction() {
    return new DeleteCFAction(getGridWrapper(), getDataModel(), Messages.CausalFactors, null);
  }

  @Override
  protected Map<String, Boolean> getCategories() {
    Map<String, Boolean> categories = new HashMap<>();
    categories.put("ALL", false);
    categories.put(ComponentType.ACTUATOR.name(), true);
    categories.put(ComponentType.CONTROLLER.name(), true);
    categories.put(ComponentType.CONTROLLED_PROCESS.name(), true);
    categories.put(ComponentType.SENSOR.name(), true);
    categories.put(CAUSALFACTORS, false);
    categories.put("UCA", false);
    return categories;
  }

  @Override
  protected String[] getCategoryArray() {
    return new String[] { "ALL", ComponentType.ACTUATOR.name(), ComponentType.CONTROLLER.name(),
        ComponentType.CONTROLLED_PROCESS.name(), ComponentType.SENSOR.name(), CAUSALFACTORS, "UCA" };
  }

  /**
   * @return true if the component is filtered out and should not be used
   */
  protected boolean isCFFiltered(IRectangleComponent comp) {
    boolean isTypeFiltered = false;
    if (!getActiveCategory().isEmpty()) {
      isTypeFiltered = getActiveCategory().equals("ALL");
      if (!isTypeFiltered && (getActiveCategory().equals(ComponentType.ACTUATOR.name())
          || getActiveCategory().equals(ComponentType.CONTROLLER.name())
          || getActiveCategory().equals(ComponentType.CONTROLLED_PROCESS.name())
          || getActiveCategory().equals(ComponentType.SENSOR.name()))) {
        isTypeFiltered = true;
        if (!getActiveCategory().equals(comp.getComponentType().name())) {
          return true;
        }
      }

    }
    if (!isTypeFiltered) {
      return false;
    }
    return isFiltered(comp.getText());
  }

  @Override
  protected void fillTable() {
    if (this.getDataModel().isUseScenarios()) {
      this.getGridWrapper().setColumnLabels(setting.getScenarioColumns());
    } else {
      this.getGridWrapper().setColumnLabels(setting.getColumns());
    }
    List<IRectangleComponent> components = this.getDataModel().getCausalComponents();
    for (IRectangleComponent component : components) {
      if (!isCFFiltered(component)) {
        GridRow componentRow = new GridRow(this.getGridWrapper().getColumnLabels().length);
        GridCellText cell = new GridCellText(component.getText());
        cell.setToolTip(component.getText());
        componentRow.addCell(0, cell);
        if (createPrimaryFactorRows(component, componentRow)) {
          getGridWrapper().addRow(componentRow);
        }
      }
    }
  }

  /**
   * 
   * @param component
   *          The {@link IRectangleComponent} for which the Causal Factor should be derived.
   * @param componentRow
   *          The parent row that has been added for the Component
   * @param ucaCfLink_Component_ToCFmap
   */
  private boolean createPrimaryFactorRows(IRectangleComponent component, GridRow componentRow) {
    return setting.addPrimaryFactor(component, componentRow);
  }

  private void createScenarioRow(Link factorComponentLink, GridRow entryRow) {
    GridRow scenarioRow = new GridRow(this.getGridWrapper().getColumnLabels().length);
    GridCellText scenarioCell = new GridCellText("Add a new scenario");
    Link ucaCFLink = getDataModel().getLinkController().getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
        factorComponentLink.getLinkA());
    IUnsafeControlAction uca = getDataModel().getControlActionController()
        .getUnsafeControlAction(ucaCFLink.getLinkA());
    scenarioCell.addCellButton(new CellButtonAddScenario(getDataModel(), factorComponentLink, uca));
    scenarioCell.addCellButton(new CellButtonLinking<ContentProviderScenarios>(getGridWrapper(),
        new ContentProviderScenarios(getDataModel(), factorComponentLink, uca), factorComponentLink.getLinkA()));
    scenarioRow.setColumnSpan(4, 1);
    scenarioRow.addCell(4, scenarioCell);
    entryRow.addChildRow(scenarioRow);
  }

  /**
   * 
   * @param entryRow
   *          the row in which the cells will be added
   * @param ucaHazLink
   *          a Link of type {@link LinkingType#CausalEntryLink_HAZ_LINK}
   * @return
   */
  private GridRow createHazardRow(GridRow entryRow, Link ucaHazLink) {
    ITableModel hazard = getDataModel().getHazard(ucaHazLink.getLinkB());
    String hazText = hazard != null ? hazard.getIdString() + " - " + hazard.getTitle() : "no hazard";
    GridCellText hazCell = new GridCellText(hazText);
    if (hazard != null && hazard.getDescription() != null) {
      String toolTip = hazard.getDescription();
      int maxLength = 70;
      String wrappedToolTip = "";
      int nextIndex = 0;
      while (nextIndex < toolTip.length()) {
        int indexOf = toolTip.indexOf(' ', nextIndex + maxLength);
        int endIndex = indexOf < 0 ? toolTip.length() : indexOf + 1;
        wrappedToolTip += toolTip.substring(nextIndex, endIndex) + "\n";
        nextIndex = endIndex;
      }
      hazCell.setToolTip(wrappedToolTip);
    }
    entryRow.addCell(3, hazCell);

    CellEditorSingleSafetyConstraint cell = new CellEditorSingleSafetyConstraint(getGridWrapper(),
        getDataModel(), ucaHazLink);
    Link factorComponentLink = getDataModel().getLinkController().getLinkObjectFor(LinkingType.UcaCfLink_Component_LINK,
        ucaHazLink.getLinkA());
    Link ucaCfLink = getDataModel().getLinkController().getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
        factorComponentLink.getLinkA());
    ITableModel actionForUca = getDataModel().getControlActionForUca(ucaCfLink.getLinkA());
    if (actionForUca != null && !checkAccess(actionForUca.getId(), AccessRights.WRITE)) {
      cell.setReadOnly(true);
      cell.setShowDelete(false);
    }
    entryRow.addCell(4, cell);

    Optional<Link> safetyOption = getDataModel().getLinkController()
        .getRawLinksFor(LinkingType.CausalHazLink_SC2_LINK, ucaHazLink.getId()).stream()
        .findFirst();

    IGridCell hintCell = new GridCellText("");
    if (safetyOption.isPresent()) {
      hintCell = new CellEditorFactorNote(getGridWrapper(), getDataModel(), safetyOption.get());
      ((GridCellTextEditor) hintCell).setDefaultText("Design hint...");
    }
    // in column 5 the Design Hint cell for the CausalHazLink_SC2_LINK is added
    entryRow.addCell(5, hintCell);
    // In column 6 the Note/Rational cell for the CausalEntryLink_HAZ_LINK is created
    entryRow.addCell(6, new CellEditorFactorNote(getGridWrapper(), getDataModel(), ucaHazLink));
    return null;

  }

  /**
   * 
   * @param entryRow
   *          the row in which the cells will be added
   * @param factorConstraintLink
   *          a Link of type {@link ObserverValue#CausalEntryLink_SC2_LINK}
   * @param uca
   *          the {@link IUnsafeControlAction} to which this is linked
   * @return
   */
  private GridRow createSingleConstraintRow(GridRow entryRow, Link factorConstraintLink) {
    Link factorComponentLink = getDataModel().getLinkController().getLinkObjectFor(LinkingType.UcaCfLink_Component_LINK,
        factorConstraintLink.getLinkA());
    addHazardCell(entryRow, factorComponentLink);
    Link ucaCfLink = getDataModel().getLinkController().getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
        factorComponentLink.getLinkA());
    CellEditorSingleSafetyConstraint cell = new CellEditorSingleSafetyConstraint(getGridWrapper(),
        getDataModel(), factorConstraintLink);
    ITableModel actionForUca = getDataModel().getControlActionForUca(ucaCfLink.getLinkA());
    if (actionForUca != null && !checkAccess(actionForUca.getId(), AccessRights.WRITE)) {
      cell.setReadOnly(true);
      cell.setShowDelete(false);
    }
    entryRow.addCell(4, cell);

    CellEditorFactorNote hintCell = new CellEditorFactorNote(getGridWrapper(), getDataModel(), factorConstraintLink);
    if (factorConstraintLink.getLinkB() == null) {
      hintCell.setReadOnly(true);
    } else {
      ((GridCellTextEditor) hintCell).setDefaultText("Design hint...");
    }

    // in column 5 the Design Hint cell for the CausalEntryLink_SC2_LINK is added
    entryRow.addCell(5, hintCell);
    return null;

  }

  private void addHazardCell(GridRow entryRow, Link factorComponentLink) {
    Link ucaCfLink = getDataModel().getLinkController().getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK,
        factorComponentLink.getLinkA());
    String hazString = "";
    String tooltip = "";
    for (UUID hazId : getDataModel().getLinkController().getLinksFor(LinkingType.UCA_HAZ_LINK,
        ucaCfLink.getLinkA())) {
      ITableModel hazard = getDataModel().getHazard(hazId);
      hazString += hazString.isEmpty() ? "" : ", ";
      hazString += hazard.getIdString();
      tooltip += tooltip.isEmpty() ? "" : "\n";
      tooltip += hazard.getIdString();
      for(int i=0; i < hazard.getTitle().length();) {
        int nextI = hazard.getTitle().indexOf(' ', i+ 30);
        nextI = nextI == -1 ? hazard.getTitle().length(): nextI;
        tooltip += "\n   " + hazard.getTitle().substring(i, nextI);
        i = nextI;
      }
    }
    entryRow.setRowSpanningCells(new int[] { 2, 3 });
    GridCellText cell = new GridCellText(hazString);
    cell.setToolTip(tooltip);
    entryRow.addCell(3, cell);
  }

  /**
   * 
   * @param entryRow
   *          the row in which the cells will be added
   * @param scenarioLink
   *          a Link of type {@link LinkingType#CausalEntryLink_Scenario_LINK}
   * @return
   */
  private void createScenarioRow(GridRow entryRow, Link scenarioLink) {
    Link factorComponentLink = getDataModel().getLinkController().getLinkObjectFor(LinkingType.UcaCfLink_Component_LINK,
        scenarioLink.getLinkA());
    addHazardCell(entryRow, factorComponentLink);
    ScenarioType type = getDataModel().getExtendedDataController()
        .getScenarioType(scenarioLink.getLinkB());
    entryRow.addCell(4, new CellEditorCausalScenario(getGridWrapper(), getDataModel(), scenarioLink,
        scenarioLink.getLinkB(), type));
    entryRow.addCell(5, new CellEditorCausalScenarioConstraint(getGridWrapper(), getDataModel(),
        scenarioLink.getLinkB(), type));

    entryRow.addCell(6, new CellEditorFactorNote(getGridWrapper(), getDataModel(), scenarioLink));

  }

  @Override
  public void update(Observable dataModelController, Object updatedValue) {
    IDataModel controller = (IDataModel) dataModelController;
    if (controller.getProjectName().equals(getDataModel().getProjectName())) {
      super.update(dataModelController, updatedValue);
      switch ((ObserverValue) updatedValue) {
      case UNSAFE_CONTROL_ACTION: {
        addUCAFilter();
      }
      case CONTROL_STRUCTURE:
      case LINKING:
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

  interface CausalMethodologySetting {
    /**
     * 
     * @param component
     *          The {@link IRectangleComponent} for which the Causal Factor should be derived.
     * @param componentRow
     *          The parent row that has been added for the Component
     * @param ucaCfLink_Component_ToCFmap
     */
    boolean addPrimaryFactor(IRectangleComponent component, GridRow componentRow);

    public String[] getScenarioColumns();

    public String[] getColumns();
  }

  private class LegacySetting implements CausalMethodologySetting {

    private List<String> _withScenarioColumns = Arrays.asList(Messages.Component,
        Messages.CausalFactors, "Unsafe Control Action", Messages.HazardLinks, "Causal Scenarios",
        Messages.SafetyConstraint, Messages.NotesSlashRationale);
    private List<String> _withoutScenarioColumns = Arrays.asList(Messages.Component,
        Messages.CausalFactors, "Unsafe Control Action", Messages.HazardLinks,
        Messages.SafetyConstraint, "Design Hint", Messages.NotesSlashRationale);

    public String[] getScenarioColumns() {
      return _withScenarioColumns.toArray(new String[0]);
    }

    public String[] getColumns() {
      return _withoutScenarioColumns.toArray(new String[0]);
    }

    @Override
    public boolean addPrimaryFactor(IRectangleComponent component, GridRow componentRow) {
      Map<ITableModel, List<Link>> ucaCfLink_Component_To_CF_map = getDataModel()
          .getCausalFactorController()
          .getCausalFactorBasedMap(component, getDataModel().getLinkController());
      for (ITableModel factor : ucaCfLink_Component_To_CF_map.keySet()) {
        if (factor != null && !isFiltered(factor.getText(), CAUSALFACTORS)) {
          GridRow causalFactorRow;
          causalFactorRow = new GridRow(getGridWrapper().getColumnLabels().length, 1, new int[] { 1 });
          CellEditorCausalFactor cell = new CellEditorCausalFactor(getGridWrapper(), getDataModel(),
              factor.getId());
          // If the current user of the project is an Administrator than he/she can delete or edit
          // causal factors
          if (!checkAccess(AccessRights.ADMIN)) {
            cell.setReadOnly(true);
            cell.setShowDelete(false);
          }
          causalFactorRow.addCell(1, cell);
          List<IUnsafeControlAction> ucaList = new ArrayList<>();
          List<Link> factorComponentLinkList = ucaCfLink_Component_To_CF_map.get(factor);
          for (Link factorComponentLink : factorComponentLinkList) {
            /*
             * Depending on whether the entry is linked to a uca or not the uca column is filled and
             * the
             * hazards are either based on the uca or linkable
             */
            Link ucaCFLink = getDataModel().getLinkController()
                .getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK, factorComponentLink.getLinkA());
            IUnsafeControlAction uca = getDataModel().getControlActionController()
                .getUnsafeControlAction(ucaCFLink.getLinkA());
            ucaList.add(uca);
            if (!(uca == null || isFiltered(uca.getId(), "UCA"))) {
              addSecondaryFactor(causalFactorRow, factorComponentLink, uca);
            }
          }

          // A new row is added to the factorRow for adding additional entries
          GridRow addEntriesRow = new GridRow(getGridWrapper().getColumnLabels().length);
          if (checkAccess(AccessRights.WRITE)) {
            addEntriesRow.addCell(2, new GridCellButtonAddUCAEntry(component, factor.getId(),
                getDataModel(), getGrid(), ucaList));
          }

          addEntriesRow.setColumnSpan(2, getGridWrapper().getColumnLabels().length - 3);
          causalFactorRow.addChildRow(addEntriesRow);
          componentRow.addChildRow(causalFactorRow);
        }
      }
      GridRow buttonRow = new GridRow(getGridWrapper().getColumnLabels().length);
      buttonRow.addCell(1, new GridCellButtonAddCausalFactor(component, getDataModel(), null));
      buttonRow.setColumnSpan(1, getGridWrapper().getColumnLabels().length - 2);
      componentRow.addChildRow(buttonRow);
      return true;
    }

    /**
     * 
     * @param uca
     *          a model of type {@link IUnsafeControlAction}
     * @param factorComponentLink
     *          a Link of type {@link LinkingType#UcaCfLink_Component_LINK}
     */
    public void addSecondaryFactor(GridRow factorRow, Link factorComponentLink, IUnsafeControlAction uca) {

      GridRow entryRow = new GridRow(getGridWrapper().getColumnLabels().length, 1, new int[] { 2 });
      CellEditorCausalEntry cell = new CellEditorCausalEntry(getGridWrapper(), getDataModel(),
          factorComponentLink, uca, factorComponentLink.getId());
      cell.setToolTip(uca.getIdString());
      UUID controlAction = getDataModel().getControlActionForUca(uca.getId()).getId();
      cell.setReadOnly(checkAccess(controlAction, AccessRights.WRITE));
      cell.setShowDelete(checkAccess(controlAction, AccessRights.WRITE));

      entryRow.addCell(2, cell);

      LinkingType entryType = getDataModel().isUseScenarios() ? LinkingType.CausalEntryLink_Scenario_LINK
          : LinkingType.CausalEntryLink_HAZ_LINK;
      // If a CausalEntryLink_SC2_LINK exists for the given UcaCfLink_Component_LINK than and
      // scenarios are not used than a single row containing only that safety constraint is created
      if (getDataModel().getLinkController().isLinked(LinkingType.CausalEntryLink_SC2_LINK,
          factorComponentLink.getId())) {
        entryType = getDataModel().isUseScenarios() ? entryType
            : LinkingType.CausalEntryLink_SC2_LINK;
      }
      for (UUID hazId : getDataModel().getLinkController().getLinksFor(LinkingType.UCA_HAZ_LINK,
          uca.getId())) {
        ITableModel hazard = getDataModel().getHazard(hazId);
        if (hazard != null) {
          getDataModel().getLinkController().addLink(LinkingType.CausalEntryLink_HAZ_LINK,
              factorComponentLink.getId(), hazId, false);
        }
      }
      // depending on the choice of entryType a set of links is iterated to create the scenarios or
      // dafetyConstaint row/s
      for (Link link : getDataModel().getLinkController().getRawLinksFor(entryType,
          factorComponentLink.getId())) {
        GridRow row = new GridRow(getGridWrapper().getColumnLabels().length, 1);
        switch (entryType) {
        case CausalEntryLink_Scenario_LINK: {
          if (getDataModel().getExtendedDataController().getRefinedScenario(link.getLinkB()) == null) {
            ProjectManager.getLOGGER().error("Causal Factor Scenario link with illegal Scenario id!");
          } else {
            createScenarioRow(row, link);
          }
          break;
        }
        case CausalEntryLink_HAZ_LINK: {
          createHazardRow(row, link);
          break;
        }
        case CausalEntryLink_SC2_LINK: {
          createSingleConstraintRow(row, link);
          // In column 6 the Note/Rational cell for the UCACfLink_Component_LINK is created
          row.addCell(6,
              new CellEditorFactorNote(getGridWrapper(), getDataModel(), factorComponentLink));
          break;
        }
        default:
          ProjectManager.getLOGGER().debug(
              "Constant " + entryType.name() + " is not a valid enum for a causal factor entry");
          break;
        }
        entryRow.addChildRow(row);
      }
      if (getDataModel().isUseScenarios()) {
        createScenarioRow(factorComponentLink, entryRow);
      }

      factorRow.addChildRow(entryRow);
    }

  }

  /**
   * This setting configures the Causal Factors Table in the way described in the STPA-Handbook by
   * Leveson et al. 2018
   * 
   * @author Lukas Balzer
   *
   */
  private class UCAScenarioSetting implements CausalMethodologySetting {

    private List<String> _withScenarioColumns = Arrays.asList(Messages.Component,
        "Unsafe Control Action", Messages.CausalFactors, Messages.HazardLinks, "Causal Scenarios",
        Messages.SafetyConstraint, Messages.NotesSlashRationale);
    private List<String> _withoutScenarioColumns = Arrays.asList(Messages.Component,
        "Unsafe Control Action", Messages.CausalFactors, Messages.HazardLinks,
        Messages.SafetyConstraint, "Design Hint", Messages.NotesSlashRationale);

    public String[] getScenarioColumns() {
      return _withScenarioColumns.toArray(new String[0]);
    }

    public String[] getColumns() {
      return _withoutScenarioColumns.toArray(new String[0]);
    }

    @Override
    public boolean addPrimaryFactor(IRectangleComponent component, GridRow componentRow) {
      boolean addComponentRow = false;
      Map<IUnsafeControlAction, List<Link>> ucaCfLink_Component_To_UCA_map = getDataModel()
          .getCausalFactorController()
          .getUCABasedMap(component, getDataModel().getLinkController(), getDataModel().getControlActionController());
      List<IUnsafeControlAction> ucaList = new ArrayList<>();
      for (IUnsafeControlAction uca : ucaCfLink_Component_To_UCA_map.keySet()) {
        if (uca != null && !isFiltered(uca.getId(), "UCA")) {
          addComponentRow = true;
          List<Link> causalEntryLinks = ucaCfLink_Component_To_UCA_map.get(uca);
          ucaList.add(uca);
          ITableModel ca = getDataModel().getControlActionForUca(uca.getId());
          GridRow ucaFactorRow;
          ucaFactorRow = new GridRow(getGridWrapper().getColumnLabels().length, 1, new int[] { 1 });
          CellEditorCausalEntry cell = new CellEditorCausalEntry(getGridWrapper(), getDataModel(),
              causalEntryLinks, uca, uca.getId());
          String tooltip = uca.getIdString() + "\n\n" + ca.getIdString();
          for(int i=0; i < ca.getTitle().length();) {
            int nextI = ca.getTitle().indexOf(' ', i+ 200);
            nextI = nextI == -1 ? ca.getTitle().length(): nextI;
            tooltip += "\n   " + ca.getTitle().substring(i, nextI);
            i = nextI;
          }
          cell.setToolTip(tooltip);
          // If the current user of the project is an Administrator than he/she can delete or edit
          // causal factors

          cell.setReadOnly(true);
          if (!checkAccess(ca.getId(), AccessRights.WRITE)) {
            cell.setShowDelete(false);
          }
          ucaFactorRow.addCell(1, cell);
          for (Link link : causalEntryLinks) {
            /*
             * Depending on whether the entry is linked to a uca or not the uca column is filled and
             * the
             * hazards are either based on the uca or linkable
             */
            Link ucaCFLink = getDataModel().getLinkController()
                .getLinkObjectFor(LinkingType.UCA_CausalFactor_LINK, link.getLinkA());
            ITableModel factor = getDataModel().getCausalFactorController()
                .getCausalFactor(ucaCFLink.getLinkB());
            if (!(factor == null || isFiltered(factor.getId(), CAUSALFACTORS))) {
              addSecondaryFactor(ucaFactorRow, link, ucaCFLink, factor);
            }
          }

          // A new row is added to the factorRow for adding additional entries
          GridRow addEntriesRow = new GridRow(getGridWrapper().getColumnLabels().length);
          if (checkAccess(AccessRights.WRITE)) {
            addEntriesRow.addCell(2, new GridCellButtonAddCausalFactor(component, getDataModel(), uca.getId()));
          }

          addEntriesRow.setColumnSpan(2, getGridWrapper().getColumnLabels().length - 3);
          ucaFactorRow.addChildRow(addEntriesRow);
          componentRow.addChildRow(ucaFactorRow);
        }
      }
      // The Button Bar to add more Unsafe Control Action Entries to the component is only added if
      // the filter is not set to filter for a specific UCA
      if (!getActiveCategory().equals("UCA")) {
        GridRow buttonRow = new GridRow(getGridWrapper().getColumnLabels().length);
        buttonRow.addCell(1, new GridCellButtonAddUCAEntry(component, null,
            getDataModel(), getGrid(), ucaList));
        buttonRow.setColumnSpan(1, getGridWrapper().getColumnLabels().length - 2);
        componentRow.addChildRow(buttonRow);
        addComponentRow = true;
      }
      return addComponentRow;
    }

    /**
     * 
     * @param factorComponentLink
     *          a Link of type {@link LinkingType#UcaCfLink_Component_LINK}
     * @param ucaCFLink
     *          a Link of type {@link LinkingType#UCA_CausalFactor_LINK}
     * @param factor
     *          a model of type {@link IUnsafeControlAction}
     */
    public void addSecondaryFactor(GridRow factorRow, Link factorComponentLink, Link ucaCFLink,
        ITableModel factor) {
      //if the link between uca and causal Factor contains a non existent uuid than it should be deleted,
      //however if the value is null than the link is treated as the initial link that links the uca to the
      //Component without linking it to a causal factor 
      if (ucaCFLink.getLinkB() != null && getDataModel().getCausalFactorController()
          .getCausalFactor(ucaCFLink.getLinkB()) instanceof BadReferenceModel) {
        getDataModel().getLinkController().deleteLink(factorComponentLink.getLinkType(),
            factorComponentLink.getId());
        getDataModel().getLinkController().deleteLink(ucaCFLink.getLinkType(), ucaCFLink.getId());
        return;
      }
      if(ucaCFLink.getLinkB() == null) {
        return;
      }
      GridRow entryRow = new GridRow(getGridWrapper().getColumnLabels().length, 1, new int[] { 2 });
      CellEditorCausalFactor cell = new CellEditorCausalFactor(getGridWrapper(), getDataModel(), factor.getId(),
          factorComponentLink);

      entryRow.addCell(2, cell);

      LinkingType entryType = getDataModel().isUseScenarios() ? LinkingType.CausalEntryLink_Scenario_LINK
          : LinkingType.CausalEntryLink_HAZ_LINK;
      // If a CausalEntryLink_SC2_LINK exists for the given UcaCfLink_Component_LINK than and
      // scenarios are not used than a single row containing only that safety constraint is created
      if (getDataModel().getLinkController().isLinked(LinkingType.CausalEntryLink_SC2_LINK,
          factorComponentLink.getId())) {
        entryType = getDataModel().isUseScenarios() ? entryType
            : LinkingType.CausalEntryLink_SC2_LINK;
      }
      // depending on the choice of entryType a set of links is iterated to create the scenarios or
      // safetyConstaint row/s
      for (Link link : getDataModel().getLinkController().getRawLinksFor(entryType,
          factorComponentLink.getId())) {
        GridRow row = new GridRow(getGridWrapper().getColumnLabels().length, 1);
        switch (entryType) {
        case CausalEntryLink_Scenario_LINK: {
          if (getDataModel().getExtendedDataController().getRefinedScenario(link.getLinkB()) == null) {
            ProjectManager.getLOGGER().error("Causal Factor Scenario link with illegal Scenario id!");
          } else {
            createScenarioRow(row, link);
          }
          entryRow.addChildRow(row);
          break;
        }
        case CausalEntryLink_HAZ_LINK: {
          createHazardRow(row, link);
          entryRow.addChildRow(row);
          break;
        }
        case CausalEntryLink_SC2_LINK: {
          createSingleConstraintRow(entryRow, link);
          // In column 6 the Note/Rational cell for the UCACfLink_Component_LINK is created
          entryRow.addCell(6,
              new CellEditorFactorNote(getGridWrapper(), getDataModel(), factorComponentLink));
          break;
        }
        default:
          ProjectManager.getLOGGER().debug(
              "Constant " + entryType.name() + " is not a valid enum for a causal factor entry");
          break;
        }
      }
      if (getDataModel().isUseScenarios()) {
        createScenarioRow(factorComponentLink, entryRow);
      }

      factorRow.addChildRow(entryRow);
    }

  }
}
