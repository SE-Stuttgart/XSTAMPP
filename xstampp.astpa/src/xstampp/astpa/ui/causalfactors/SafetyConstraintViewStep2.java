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

package xstampp.astpa.ui.causalfactors;

import java.util.EnumSet;
import java.util.UUID;

import messages.Messages;
import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.ui.CommonTableView;
import xstampp.astpa.ui.linkingSupport.DesignReq2LinkSupport;
import xstampp.astpa.ui.linkingSupport.Step1ConstraintsLinkSupport;
import xstampp.model.ObserverValue;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class SafetyConstraintViewStep2 extends CommonTableView<ICausalFactorDataModel> {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step3_4"; //$NON-NLS-1$

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public SafetyConstraintViewStep2() {
    this(Messages.SafetyConstraints);
  }

  /**
   * @author Jarkko Heidenwag
   * @param tableHeader
   *          TODO
   * 
   */
  public SafetyConstraintViewStep2(String tableHeader) {
    super(EnumSet.of(TableStyle.WITHOUT_CONTROLS), tableHeader);
    setUpdateValues(EnumSet.of(ObserverValue.CAUSAL_FACTOR, ObserverValue.LINKING));
  }

  @Override
  protected void addLinkSupports() {
    addLinkSupport(
        new Step1ConstraintsLinkSupport((DataModelController) getDataInterface(), LinkingType.SC2_SC1_LINK, true));
    addLinkSupport(new DesignReq2LinkSupport((DataModelController) getDataInterface(), LinkingType.DR2_CausalSC_LINK));
  }

  @Override
  protected void deleteEntry(ATableModel model) {

  }

  /**
   * @author Jarkko Heidenwag
   * 
   */
  @Override
  public void updateTable() {
    SafetyConstraintViewStep2.this.getTableViewer()
        .setInput(this.getDataInterface().getCausalFactorController().getSafetyConstraints());
  }

  @Override
  public String getId() {
    return SafetyConstraintViewStep2.ID;
  }

  @Override
  public String getTitle() {
    return Messages.SafetyConstraints;
  }

  @Override
  public void dispose() {
    this.getDataInterface().deleteObserver(this);
    super.dispose();
  }

  @Override
  protected void moveEntry(UUID id, boolean moveUp) {
  }

  @Override
  protected void addNewEntry() {
  }

  @Override
  protected void updateDescription(UUID uuid, String description) {
    getDataInterface().getCausalFactorController().setSafetyConstraintDescription(uuid, description);
  }

  @Override
  protected void updateTitle(UUID id, String title) {
    getDataInterface().getCausalFactorController().setSafetyConstraintText(id, title);
  }
}
