/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.sds;

import java.util.EnumSet;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import messages.Messages;
import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlActionViewDataModel;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class ControlActionView extends CommonTableView<IControlActionViewDataModel> {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step2_1"; //$NON-NLS-1$

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public ControlActionView() {
    super(true);
    setUpdateValues(EnumSet.of(ObserverValue.CONTROL_ACTION));
  }

  /**
   * Create contents of the view part.
   * 
   * @author Jarkko Heidenwag
   * @param parent
   *          The parent composite
   */
  @Override
  public void createPartControl(Composite parent) {

    super.createPartControl(parent);
    // the source column is for the unsafe control actions
    TableViewerColumn sourceColumn = new TableViewerColumn(this.getTableViewer(), SWT.CENTER);
    sourceColumn.getColumn().setText("Source"); //$NON-NLS-1$
    getTableColumnLayout().setColumnData(sourceColumn.getColumn(),
        new ColumnWeightData(10, 100, true));

    sourceColumn.setLabelProvider(new ColumnLabelProvider() {

      @Override
      public String getText(Object element) {
        if (element instanceof IControlAction) {
          IRectangleComponent comp = ControlActionView.this.getDataInterface()
              .getComponent(((IControlAction) element).getComponentLink());
          if (comp == null) {
            return null;
          }
          IConnection conn = ControlActionView.this.getDataInterface()
              .getConnection(comp.getRelative());
          if (conn == null) {
            return null;
          }
          comp = ControlActionView.this.getDataInterface()
              .getComponent(conn.getSourceAnchor().getOwnerId());
          return comp.getText();
        }
        return null;
      }
    });
    // the target column is for the unsafe control actions
    TableViewerColumn distanceColumn = new TableViewerColumn(this.getTableViewer(), SWT.CENTER);
    distanceColumn.getColumn().setText("Destination");//$NON-NLS-1$
    getTableColumnLayout().setColumnData(distanceColumn.getColumn(),
        new ColumnWeightData(10, 100, true));

    distanceColumn.setLabelProvider(new ColumnLabelProvider() {

      @Override
      public String getText(Object element) {
        if (element instanceof IControlAction) {
          IRectangleComponent comp = ControlActionView.this.getDataInterface()
              .getComponent(((IControlAction) element).getComponentLink());
          if (comp == null) {
            return null;
          }
          IConnection conn = ControlActionView.this.getDataInterface()
              .getConnection(comp.getRelative());
          if (conn == null) {
            return null;
          }
          comp = ControlActionView.this.getDataInterface()
              .getComponent(conn.getTargetAnchor().getOwnerId());
          return comp.getText();
        }
        return null;
      }
    });
    this.updateTable();
    getAddNewItemButton().setEnabled(false);
    getAddNewItemButton().setToolTipText(Messages.ControlActionView_1);
  }

  @Override
  protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeControlAction(model.getId());
  }

  /**
   * @author Jarkko Heidenwag
   * 
   */
  @Override
  public void updateTable() {
    ControlActionView.this.getTableViewer()
        .setInput(this.getDataInterface().getAllControlActions());
  }

  @Override
  public void update(Observable dataModelController, Object updatedValue) {
    super.update(dataModelController, updatedValue);
    ObserverValue type = (ObserverValue) updatedValue;
    switch (type) {
    case CONTROL_ACTION:
      this.refreshView();
      break;
    default:
      break;
    }
  }

  @Override
  public String getId() {
    return ControlActionView.ID;
  }

  @Override
  public String getTitle() {
    return Messages.ControlActions;
  }

  @Override
  public void dispose() {
    this.getDataInterface().deleteObserver(this);
    super.dispose();
  }

  @Override
  protected void moveEntry(UUID id, boolean moveUp) {
    getDataInterface().moveEntry(false, moveUp, id, ObserverValue.CONTROL_ACTION);
  }

  @Override
  protected void addNewEntry() {
  }

  @Override
  protected void updateDescription(UUID uuid, String description) {
    ControlActionView.this.getDataInterface().setControlActionDescription(uuid, description);
  }

  @Override
  protected void updateTitle(UUID id, String title) {
    getDataInterface().setControlActionTitle(id, title);
  }
}
