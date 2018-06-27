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
import xstampp.astpa.model.interfaces.IHazardViewDataModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.ui.CommonTableView;
import xstampp.astpa.ui.linkingSupport.AccidentLinkSupport;
import xstampp.astpa.ui.linkingSupport.Step0ConstraintsLinkSupport;
import xstampp.model.ObserverValue;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class HazardsView extends CommonTableView<IHazardViewDataModel> {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step1_3"; //$NON-NLS-1$

  public HazardsView() {
    this(Messages.Hazards);
  }

  public HazardsView(String tableHeader) {
    super(EnumSet.of(TableStyle.RESTRICTED, TableStyle.WITH_SEVERITY), tableHeader);
    setUpdateValues(
        EnumSet.of(ObserverValue.HAZARD, ObserverValue.SEVERITY, ObserverValue.LINKING));
  }

  public HazardsView(String tableHeader, EnumSet<TableStyle> style) {
    super(style, tableHeader);
    setUpdateValues(
        EnumSet.of(ObserverValue.HAZARD, ObserverValue.SEVERITY, ObserverValue.LINKING));
  }

  @Override
  protected void addLinkSupports() {
    addLinkSupport(new AccidentLinkSupport((DataModelController) getDataInterface(),
        LinkingType.HAZ_ACC_LINK));
    if (((DataModelController) getDataInterface()).getHazAccController().isUseHazardConstraints()) {
      addLinkSupport(new Step0ConstraintsLinkSupport((DataModelController) getDataInterface(),
          LinkingType.HAZ_S0_LINK));
    }
  }

  @Override
  protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeHazard(model.getId());
  }

  /**
   * @author Jarkko Heidenwag
   * 
   */
  @Override
  public void updateTable() {
    HazardsView.this.getTableViewer().setInput(this.getDataInterface().getAllHazards());
  }

  @Override
  public String getId() {
    return HazardsView.ID;
  }

  @Override
  public String getTitle() {
    return Messages.Hazards;
  }

  @Override
  public void dispose() {
    this.getDataInterface().deleteObserver(this);
    super.dispose();
  }

  @Override
  protected void moveEntry(UUID id, boolean moveUp) {
    getDataInterface().moveEntry(false, moveUp, id, ObserverValue.HAZARD);
  }

  @Override
  protected void addNewEntry() {
    HazardsView.this.getDataInterface().addHazard("", "");
  }

  @Override
  protected void updateDescription(UUID uuid, String description) {
    HazardsView.this.getDataInterface().setHazardDescription(uuid, description);
  }

  @Override
  protected void updateTitle(UUID id, String title) {
    HazardsView.this.getDataInterface().setHazardTitle(id, title);
  }
}
