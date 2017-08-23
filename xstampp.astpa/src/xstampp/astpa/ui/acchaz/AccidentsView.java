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

package xstampp.astpa.ui.acchaz;

import java.util.EnumSet;
import java.util.UUID;

import messages.Messages;
import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.IAccidentViewDataModel;
import xstampp.astpa.ui.CommonTableView;
import xstampp.astpa.ui.linkingSupport.HazardLinkSupport;
import xstampp.astpa.ui.linkingSupport.Step0ConstraintsLinkSupport;
import xstampp.model.ObserverValue;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class AccidentsView extends CommonTableView<IAccidentViewDataModel> {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step1_2"; //$NON-NLS-1$

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public AccidentsView() {
    this(Messages.Accidents);
  }

  /**
   * @author Jarkko Heidenwag
   * @param tableHeader
   *          TODO
   * 
   */
  public AccidentsView(String tableHeader) {
    super(EnumSet.of(TableStyle.RESTRICTED, TableStyle.WITH_SEVERITY), tableHeader);
    setUpdateValues(EnumSet.of(ObserverValue.ACCIDENT, ObserverValue.SEVERITY,
        ObserverValue.HAZ_ACC_LINK, ObserverValue.ACC_S0_LINK));
  }

  /**
   * @author Jarkko Heidenwag
   * @param tableHeader
   *          TODO
   * @param style
   * 
   */
  public AccidentsView(String tableHeader, EnumSet<TableStyle> style) {
    super(style, tableHeader);
    setUpdateValues(EnumSet.of(ObserverValue.ACCIDENT, ObserverValue.SEVERITY,
        ObserverValue.HAZ_ACC_LINK, ObserverValue.ACC_S0_LINK));
  }

  @Override
  protected void addLinkSupports() {
    addLinkSupport(new HazardLinkSupport((DataModelController) getDataInterface(),
        ObserverValue.HAZ_ACC_LINK));
    addLinkSupport(new Step0ConstraintsLinkSupport((DataModelController) getDataInterface(),
        ObserverValue.ACC_S0_LINK));
  }

  @Override
  protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeAccident(model.getId());
  }

  /**
   * @author Jarkko Heidenwag
   * 
   */
  @Override
  public void updateTable() {
    AccidentsView.this.getTableViewer().setInput(this.getDataInterface().getAllAccidents());
  }

  @Override
  public String getId() {
    return AccidentsView.ID;
  }

  @Override
  public String getTitle() {
    return Messages.Accidents;
  }

  @Override
  public void dispose() {
    this.getDataInterface().deleteObserver(this);
    super.dispose();
  }

  @Override
  protected void moveEntry(UUID id, boolean moveUp) {
    getDataInterface().moveEntry(false, moveUp, id, ObserverValue.ACCIDENT);
  }

  @Override
  protected void addNewEntry() {
    AccidentsView.this.getDataInterface().addAccident("", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  @Override
  protected void updateDescription(UUID uuid, String description) {
    AccidentsView.this.getDataInterface().setAccidentDescription(uuid, description);
  }

  @Override
  protected void updateTitle(UUID id, String title) {
    AccidentsView.this.getDataInterface().setAccidentTitle(id, title);
  }
}
