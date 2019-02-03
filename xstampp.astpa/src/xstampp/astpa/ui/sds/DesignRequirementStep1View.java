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
import java.util.UUID;

import messages.Messages;
import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.IDesignRequirementViewDataModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.ui.CommonTableView;
import xstampp.astpa.ui.linkingSupport.Step1ConstraintsLinkSupport;
import xstampp.model.ObserverValue;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class DesignRequirementStep1View extends CommonTableView<IDesignRequirementViewDataModel> {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step2_4"; //$NON-NLS-1$

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public DesignRequirementStep1View() {
    super(Messages.DesignRequirements + "Step 3");
    setUpdateValues(EnumSet.of(ObserverValue.DESIGN_REQUIREMENT_STEP1, ObserverValue.LINKING));
  }

  @Override
  protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeDesignRequirement(model.getId(), ObserverValue.DESIGN_REQUIREMENT_STEP1);
  }

  @Override
  protected void addLinkSupports() {
    addLinkSupport(new Step1ConstraintsLinkSupport((DataModelController) getDataInterface(),
        LinkingType.DR1_CSC_LINK));
  }

  /**
   * @author Jarkko Heidenwag
   * 
   */
  @Override
  public void updateTable() {
    DesignRequirementStep1View.this.getTableViewer().setInput(this.getDataInterface()
        .getSdsController().getAllDesignRequirements(ObserverValue.DESIGN_REQUIREMENT_STEP1));
  }

  @Override
  public String getId() {
    return DesignRequirementStep1View.ID;
  }

  @Override
  public String getTitle() {
    return Messages.DesignRequirements;
  }

  @Override
  public void dispose() {
    this.getDataInterface().deleteObserver(this);
    super.dispose();
  }

  @Override
  protected void moveEntry(UUID id, boolean moveUp) {
    getDataInterface().moveEntry(false, moveUp, id, ObserverValue.DESIGN_REQUIREMENT_STEP1);
  }

  @Override
  protected void addNewEntry() {
    getDataInterface().getSdsController().addDesignRequirement("", "", //$NON-NLS-1$ //$NON-NLS-2$
        ObserverValue.DESIGN_REQUIREMENT_STEP1);
  }

  @Override
  protected void updateDescription(UUID id, String description) {
    getDataInterface().getSdsController()
        .setDesignRequirementDescription(ObserverValue.DESIGN_REQUIREMENT_STEP1, id, description);
  }

  @Override
  protected void updateTitle(UUID id, String title) {
    getDataInterface().getSdsController()
        .setDesignRequirementTitle(ObserverValue.DESIGN_REQUIREMENT_STEP1, id, title);
  }
}
