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
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;

/**
 * @author Jarkko Heidenwag
 * 
 */
public class SafetyConstraintView extends CommonTableView<ISafetyConstraintViewDataModel> {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step1_5"; //$NON-NLS-1$

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public SafetyConstraintView() {
    this(Messages.SafetyConstraints);
  }

  /**
   * @author Jarkko Heidenwag
   * @param tableHeader TODO
   * 
   */
  public SafetyConstraintView(String tableHeader) {
    super(null,tableHeader);
    setUpdateValues(EnumSet.of(ObserverValue.SAFETY_CONSTRAINT));
  }

  @Override
  protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeSafetyConstraint(model.getId());
  }

  /**
   * @author Jarkko Heidenwag
   * 
   */
  @Override
  public void updateTable() {
    SafetyConstraintView.this.getTableViewer()
        .setInput(this.getDataInterface().getAllSafetyConstraints());
  }

  @Override
  public String getId() {
    return SafetyConstraintView.ID;
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
    getDataInterface().moveEntry(false, moveUp, id, ObserverValue.SAFETY_CONSTRAINT);
  }

  @Override
  protected void addNewEntry() {
    SafetyConstraintView.this.getDataInterface().addSafetyConstraint("", ""); //$NON-NLS-1$ //$NON-NLS-2$
  }

  @Override
  protected void updateDescription(UUID uuid, String description) {
    SafetyConstraintView.this.getDataInterface()
        .setSafetyConstraintDescription(getCurrentSelection(), description);
  }

  @Override
  protected void updateTitle(UUID id, String title) {
    getDataInterface().setSafetyConstraintTitle(id, title);
  }
}
