/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.unsafecontrolaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;
import java.util.function.Predicate;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import messages.Messages;
import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.interfaces.ISeverityEntry;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.interfaces.IUnsafeControlActionDataModel;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.ui.CommonGridView;
import xstampp.astpa.ui.SeverityButton;
import xstampp.astpa.ui.SeverityButton.SeverityCheck;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.grid.DeleteGridEntryAction;
import xstampp.ui.common.grid.GridCellBlank;
import xstampp.ui.common.grid.GridCellButton;
import xstampp.ui.common.grid.GridCellLinking;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridRow;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.usermanagement.api.AccessRights;

/**
 * View used to handle the unsafe control actions.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser, Lukas Balzer
 */
public class UnsafeControlActionsView extends CommonGridView<IUnsafeControlActionDataModel> {

  public static final String UCA1 = "UCA1."; //$NON-NLS-1$
  /**
   * ViewPart ID.
   */
  public static final String ID = "astpa.steps.step2_2"; //$NON-NLS-1$

  private static final String CA_FILTER = "Control Action"; //$NON-NLS-1$
  private static final String UCA_FILTER = "unsafe Control Actions"; //$NON-NLS-1$
  private static final String UCAID_FILTER = "UCA ID"; //$NON-NLS-1$
  private static final String HAZ_FILTER = "Hazards"; //$NON-NLS-1$
  private static final String NOHAZ_FILTER = "not hazardous"; //$NON-NLS-1$
  private static final String HAZID_FILTER = "Hazard ID"; //$NON-NLS-1$

  private String[] columns = null;

  /**
   * Constructs an UnsafeControlActionsView with a filter and the default set of column names
   * defined in the STPA
   */
  public UnsafeControlActionsView() {
    setUseFilter(true);
  }

  /**
   * Constructs an UnsafeControlActionsView with a filter
   * 
   * @param columns
   *          must be a string array of size 4, containing the names of the columns
   */
  public UnsafeControlActionsView(String[] columns) {
    this(columns, true);
  }

  /**
   * 
   * @param columns
   *          must be a string array of size 4, containing the names of the columns
   * @param useFilter
   *          whether to use the filter or not
   */
  public UnsafeControlActionsView(String[] columns, boolean useFilter) {
    setUseFilter(useFilter);
    this.columns = columns;
  }

  @Override
  protected Map<String, Boolean> getCategories() {
    Map<String, Boolean> categories = new HashMap<>();
    categories.put(CA_FILTER, false);
    categories.put(UCA_FILTER, false);
    categories.put(UCAID_FILTER, false);
    categories.put(HAZ_FILTER, false);
    categories.put(NOHAZ_FILTER, false);
    categories.put(HAZID_FILTER, false);
    return categories;

  }

  @Override
  protected String[] getCategoryArray() {
    return new String[] { CA_FILTER, UCA_FILTER, UCAID_FILTER, HAZ_FILTER, HAZID_FILTER,
        NOHAZ_FILTER };
  }

  @Override
  protected void updateFilter() {
    reloadTable();
  }

  private class UnsafeControlActionCell extends GridCellTextEditor {

    public UnsafeControlActionCell(GridWrapper grid, String initialText, UUID uca,
        boolean canDelete) {
      super(grid, initialText, uca);
      setShowDelete(canDelete);
      setReadOnly(!canDelete);
    }

    @Override
    public void delete() {
      deleteEntry();
    }

    @Override
    public void updateDataModel(String newValue) {
      getDataModel().setUcaDescription(getUUID(), newValue);
    }

    @Override
    protected void editorOpening() {
      getDataModel().lockUpdate();
    }

    @Override
    protected void editorClosing() {
      getDataModel().releaseLockAndUpdate(new ObserverValue[] {});
    }
  }

  private class AddUcaButton extends GridCellButton {

    private IControlAction parentControlAction;
    private UnsafeControlActionType ucaType;

    public AddUcaButton(IControlAction parentItem, String text, UnsafeControlActionType type) {
      super(text);

      this.parentControlAction = parentItem;
      this.ucaType = type;
    }

    @Override
    public void onMouseDown(MouseEvent e, org.eclipse.swt.graphics.Point relativeMouse,
        Rectangle cellBounds) {
      if (e.button == 1) {
        UUID newUCA = UnsafeControlActionsView.this.getDataModel()
            .addUnsafeControlAction(this.parentControlAction.getId(), "", this.ucaType); //$NON-NLS-1$
        getGridWrapper().activateCell(newUCA);
        ProjectManager.getLOGGER().debug(Messages.AddingNewUCA);
      }

    }
  }

  /**
   * User interface components.
   * 
   * @author Benedikt Markt, Patrick Wickenhaeuser
   */
  @Override
  public void createPartControl(Composite parent) {
    this.setDataModelInterface(ProjectManager.getContainerInstance()
        .getDataModel(this.getProjectID()));
    super.createPartControl(parent, columns);
    this.getGridWrapper()
        .setHeaderToolTip(xstampp.astpa.messages.Messages.UnsafeControlActionsView_HeaderToolTip);
    updateHazards();
    reloadTable();
  }

  @Override
  public void setDataModelInterface(IDataModel dataInterface) {
    super.setDataModelInterface(dataInterface);
    if (columns == null) {
      columns = new String[5];
      columns[0] = Messages.ControlAction;
      for (int i = 0; i < 4; i++) {
        columns[i + 1] = getDataModel().getControlActionController().getUCAHeaders()[i];
      }
    }
  }

  @Override
  public DeleteGridEntryAction<IUnsafeControlActionDataModel> getDeleteAction() {
    return new DeleteUcaAction(getGridWrapper(), getDataModel(), Messages.UnsafeControlActions,
        UCA1);
  }

  @Override
  protected void fillTable() throws SWTException {
    List<IControlAction> list = getDataModel().getAllControlActionsU();
    if (list.isEmpty()) {
      return;
    }
    boolean addControlAction;
    for (IControlAction cAction : list) {
      // fiter by the title of the control action

      if (isFiltered(cAction.getTitle(), CA_FILTER)) {
        continue;
      }
      GridRow controlActionRow = new GridRow(columns.length, 3);
      addControlAction = false;

      controlActionRow.addCell(0, new GridCellText(cAction.getTitle()));

      List<IUnsafeControlAction> allNotGiven = cAction
          .getUnsafeControlActions(UnsafeControlActionType.NOT_GIVEN);
      List<IUnsafeControlAction> allIncorrect = cAction
          .getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY);
      List<IUnsafeControlAction> allWrongTiming = cAction
          .getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING);
      List<IUnsafeControlAction> allTooSoon = cAction
          .getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON);
      int maxHeight = allNotGiven.size();

      maxHeight = Math.max(maxHeight, allIncorrect.size());
      maxHeight = Math.max(maxHeight, allTooSoon.size());
      maxHeight = Math.max(maxHeight, allWrongTiming.size());
      boolean addUCA = false;
      for (int i = 0; i <= maxHeight; i++) {
        addUCA = false;
        GridRow idRow = new GridRow(columns.length, 3);
        GridRow ucaRow = new GridRow(columns.length, 3);
        GridRow linkRow = new GridRow(columns.length, 3);

        addUCA |= addUCAEntry(allNotGiven, i, 1, Messages.AddNotGivenUCA,
            UnsafeControlActionType.NOT_GIVEN, idRow, ucaRow, linkRow, cAction);
        addUCA |= addUCAEntry(allIncorrect, i, 2, Messages.AddGivenIncorrectlyUCA,
            UnsafeControlActionType.GIVEN_INCORRECTLY, idRow, ucaRow, linkRow, cAction);

        addUCA |= addUCAEntry(allWrongTiming, i, 3, Messages.AddWrongTimingUCA,
            UnsafeControlActionType.WRONG_TIMING, idRow, ucaRow, linkRow, cAction);
        addUCA |= addUCAEntry(allTooSoon, i, 4, Messages.AddStoppedTooSoonUCA,
            UnsafeControlActionType.STOPPED_TOO_SOON, idRow, ucaRow, linkRow, cAction);

        addControlAction |= addUCA;
        if (addUCA) {
          controlActionRow.addChildRow(idRow);
          controlActionRow.addChildRow(ucaRow);
          controlActionRow.addChildRow(linkRow);
          addControlAction |= addUCA;
        } else {
          break;
        }
      }
      if (addControlAction) {
        getGridWrapper().addRow(controlActionRow);
      }
    }
  }

  private boolean addUCAEntry(List<IUnsafeControlAction> ucaList, int i, int columnIndex,
      String message, UnsafeControlActionType type, GridRow idRow, GridRow ucaRow, GridRow linkRow,
      IControlAction cAction) {
    while (ucaList.size() > i && isUCAFiltered(ucaList.get(i))) {
      ucaList.remove(i);
    }
    boolean canWrite = checkAccess(cAction.getId(), AccessRights.WRITE);
    if (ucaList.size() > i) {
      IUnsafeControlAction uca = ucaList.get(i);

      UcaContentProvider ucaContentProvider = new UcaContentProvider(uca, getDataModel());
      GridCellText idCell = new UcaIdCell(ucaContentProvider, uca, getDataModel());
      if (getDataModel().isUseSeverity()) {
        SeverityButton button = new SeverityButton((ISeverityEntry) uca, getDataModel(), getGrid());
        idCell.addCellButton(button);
      }
      idRow.addCell(columnIndex, idCell);
      UnsafeControlActionCell editor = new UnsafeControlActionCell(getGridWrapper(),
          uca.getDescription(), uca.getId(), canWrite);
      ucaRow.addCell(columnIndex, editor);
      linkRow.addCell(columnIndex, new GridCellLinking<UcaContentProvider>(uca.getId(),
          ucaContentProvider, getGridWrapper(), canWrite));
      return true;
    }

    if (ucaList.size() == i && canWrite) {
      // add placeholder
      idRow.addCell(columnIndex, new GridCellBlank(true));
      ucaRow.addCell(columnIndex, new AddUcaButton(cAction, message, type));
      linkRow.addCell(columnIndex, new GridCellBlank(true));
      return true;
    } else {
      // add placeholders
      idRow.addCell(columnIndex, new GridCellBlank(true));
      ucaRow.addCell(columnIndex, new GridCellBlank(true));
      linkRow.addCell(columnIndex, new GridCellBlank(true));
    }
    return false;
  }

  @Override
  public String getId() {
    ProjectManager.getLOGGER().info("getID()"); //$NON-NLS-1$
    return UnsafeControlActionsView.ID;
  }

  @Override
  public String getTitle() {
    return "Unsafe Control Actions Table"; //$NON-NLS-1$
  }

  /**
   * !isFiltered(this.getDataModel().getUCANumber(allWrongTiming.get(i).getId()),UCAID) &&
   * !isFiltered(allWrongTiming.get(i).getDescription(),UCA)
   * 
   * @param uca
   * @return true if the uca is filtered out and should not be used
   */
  private boolean isUCAFiltered(IUnsafeControlAction uca) {
    if (getActiveCategory() != null) {
      switch (getActiveCategory()) {
      case UCAID_FILTER:
        return isFiltered(this.getDataModel().getUCANumber(uca.getId()), UCAID_FILTER);
      case UCA_FILTER:
        return isFiltered(uca.getDescription(), UCA_FILTER);
      case HAZ_FILTER:
        if (this.getDataModel().getLinkedHazardsOfUCA(uca.getId()).size() == 0) {
          return true;
        }
        for (ITableModel model : this.getDataModel().getLinkedHazardsOfUCA(uca.getId())) {
          if (!isFiltered(model.getTitle(), HAZ_FILTER)
              || !isFiltered(model.getDescription(), HAZ_FILTER)) {
            return false;
          }
          return true;
        }
      case NOHAZ_FILTER:
        if (this.getDataModel().getLinkedHazardsOfUCA(uca.getId()).size() != 0) {
          return true;
        }
      case HAZID_FILTER:
        if (this.getDataModel().getLinkedHazardsOfUCA(uca.getId()).size() == 0) {
          return true;
        }
        for (ITableModel model : this.getDataModel().getLinkedHazardsOfUCA(uca.getId())) {
          if (!isFiltered(model.getNumber(), HAZID_FILTER)) {
            return false;
          }
        }
        return true;
      default:
        return isFiltered(uca.getDescription(), UCA_FILTER);
      }
    }
    return false;
  }

  private void updateHazards() {
    String[] choices = new String[getDataModel().getAllHazards().size()];
    String[] choiceIDs = new String[getDataModel().getAllHazards().size()];
    String[] choiceValues = new String[getDataModel().getAllHazards().size()];
    int index = 0;

    for (ITableModel model : getDataModel().getAllHazards()) {
      choices[index] = "H-" + model.getNumber() + ": " + model.getTitle(); //$NON-NLS-1$ //$NON-NLS-2$
      choiceIDs[index] = "" + model.getNumber(); //$NON-NLS-1$
      choiceValues[index++] = model.getTitle();
    }
    this.addChoices(HAZID_FILTER, choices);
    this.addChoiceValues(HAZID_FILTER, choiceIDs);
    this.addChoices(HAZ_FILTER, choices);
    this.addChoiceValues(HAZ_FILTER, choiceValues);
  }

  @Override
  public void update(Observable dataModelController, Object updatedValue) {
    if (!getGridWrapper().fetchUpdateLock()) {
      super.update(dataModelController, updatedValue);
      ObserverValue type = (ObserverValue) updatedValue;
      switch (type) {
      case UNSAFE_CONTROL_ACTION:
      case UCA_HAZ_LINK:
      case HAZARD:
        updateHazards();
      case CONTROL_ACTION:
        try {
          this.reloadTable();
        } catch (SWTException e) {
          dataModelController.deleteObserver(this);
        }
        break;

      default:
        break;
      }
    }
  }

  @Override
  public void dispose() {
    this.getDataModel().deleteObserver(this);
    super.dispose();
  }

}
