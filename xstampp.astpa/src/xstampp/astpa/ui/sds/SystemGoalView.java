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
import xstampp.astpa.model.interfaces.ISystemGoalViewDataModel;
import xstampp.astpa.ui.CommonTableView;
import xstampp.model.ObserverValue;

/**
 * @author Jarkko Heidenwag
 * @since 1.0.0
 * @version 2.0.2
 */
public class SystemGoalView extends CommonTableView<ISystemGoalViewDataModel> {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step1_6"; //$NON-NLS-1$

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public SystemGoalView() {
    super(EnumSet.of(TableStyle.RESTRICTED), Messages.SystemGoals);
    setUpdateValues(EnumSet.of(ObserverValue.SYSTEM_GOAL));
  }

  @Override
  protected void deleteEntry(ATableModel model) {
    resetCurrentSelection();
    this.getDataInterface().removeSystemGoal(model.getId());
  }

  /**
   * @author Jarkko Heidenwag
   * 
   */
  @Override
  public void updateTable() {
    SystemGoalView.this.getTableViewer().setInput(this.getDataInterface().getAllSystemGoals());
  }

  @Override
  public String getId() {
    return SystemGoalView.ID;
  }

  @Override
  public String getTitle() {
    return Messages.SystemGoals;
  }

  @Override
  public void dispose() {
    this.getDataInterface().deleteObserver(this);
    super.dispose();
  }

  @Override
  protected void moveEntry(UUID id, boolean moveUp) {
    getDataInterface().moveEntry(false, moveUp, id, ObserverValue.SYSTEM_GOAL);
  }

  @Override
  protected void addNewEntry() {
    SystemGoalView.this.getDataInterface().addSystemGoal("", "");//$NON-NLS-1$ //$NON-NLS-2$
  }

  @Override
  protected void updateDescription(UUID uuid, String description) {
    SystemGoalView.this.getDataInterface().setSystemGoalDescription(getCurrentSelection(),
        description);
  }

  @Override
  protected void updateTitle(UUID id, String title) {
    getDataInterface().setSystemGoalTitle(id, title);
  }
}
