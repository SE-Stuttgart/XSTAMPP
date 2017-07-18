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

import org.eclipse.swt.widgets.Composite;

import messages.Messages;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.IDesignRequirementViewDataModel;
import xstampp.astpa.ui.CommonTableView;
import xstampp.astpa.ui.linkingSupport.Step2ConstraintsLinkSupport;
import xstampp.model.ObserverValue;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class DesignRequirementStep2View extends CommonTableView<IDesignRequirementViewDataModel> {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step3_3"; //$NON-NLS-1$

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public DesignRequirementStep2View() {
    super(EnumSet.of(TableStyle.RESTRICTED), Messages.DesignRequirements);
    setUpdateValues(EnumSet.of(ObserverValue.DESIGN_REQUIREMENT_STEP2));
  }

  @Override
  public void createCommonTableView(Composite parent, String tableHeader) {
    addLinkSupport(new Step2ConstraintsLinkSupport((DataModelController) getDataInterface()));
    super.createCommonTableView(parent, tableHeader);
  }

  @Override
  protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeDesignRequirement(model.getId());
  }

  /**
   * @author Jarkko Heidenwag
   * 
   */
  @Override
  public void updateTable() {
    DesignRequirementStep2View.this.getTableViewer().setInput(this.getDataInterface()
        .getSdsController().getAllDesignRequirements(ObserverValue.DESIGN_REQUIREMENT_STEP2));
  }

  @Override
  public String getId() {
    return DesignRequirementStep2View.ID;
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
    getDataInterface().moveEntry(false, moveUp, id, ObserverValue.DESIGN_REQUIREMENT_STEP2);
  }

  @Override
  protected void addNewEntry() {
    DesignRequirementStep2View.this.getDataInterface().getSdsController().addDesignRequirement("", //$NON-NLS-1$
        "", ObserverValue.DESIGN_REQUIREMENT_STEP2); //$NON-NLS-1$
  }

  @Override
  protected void updateDescription(UUID id, String description) {
    DesignRequirementStep2View.this.getDataInterface().getSdsController()
        .setDesignRequirementDescription(ObserverValue.DESIGN_REQUIREMENT_STEP2, id, description);
  }

  @Override
  protected void updateTitle(UUID id, String title) {
    getDataInterface().getSdsController()
        .setDesignRequirementTitle(ObserverValue.DESIGN_REQUIREMENT_STEP2, id, title);
  }
}
