/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.usermanagement.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.usermanagement.Messages;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.LinkProposal;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;

public class ResponsibilityEditingSupport extends EditingSupport {

  private Map<UUID, List<UUID>> userToResponibilitiesMap;
  private List<String> userList;
  private IUserSystem userSystem;
  private List<IUser> userRegister;
  private ModalShell parent;

  /**
   * 
   * @param viewer
   *          must be a {@link TableViewer}
   * @param project
   * @param parent 
   */
  public ResponsibilityEditingSupport(ColumnViewer viewer, IUserProject project, ModalShell parent) {
    super(viewer);
    this.parent = parent;
    Assert.isTrue(viewer instanceof TableViewer);
    this.userToResponibilitiesMap = new HashMap<>();
    userSystem = project.getUserSystem();
    this.userList = new ArrayList<>();
    this.userRegister = new ArrayList<>();
    for (IUser user : this.userSystem.getRegistry()) {
      if (user.checkAccess(AccessRights.ACCESS)) {
        this.userList.add(user.getUsername());
        this.userRegister.add(user);
        for (UUID entryId : this.userSystem.getResponsibilities(user.getUserId())) {
          mapResp(user.getUserId(), entryId);
        }
      }
    }
  }

  private void mapResp(UUID userId, UUID entryId) {
    if (!this.userToResponibilitiesMap.containsKey(entryId)) {
      this.userToResponibilitiesMap.put(entryId, new ArrayList<>());
    }
    this.userToResponibilitiesMap.get(entryId).add(userId);
  }

  @Override
  protected CellEditor getCellEditor(Object element) {
    return null;
  }

  @Override
  protected boolean canEdit(Object element) {
    if (userSystem.checkAccess(AccessRights.ADMIN)) {

      List<LinkProposal> proposals = new ArrayList<>();
      for (IUser user : this.userRegister) {
        LinkProposal prop = new LinkProposal();
        prop.setLabel(user.getUsername());
        prop.setProposalId(user.getUserId());
        proposals.add(prop);
      }
      AutoCompleteField field = new AutoCompleteField(proposals.toArray(new LinkProposal[0]), getViewer().getControl());
      field.setProposalListener((proposal) -> {
        mapResp(((LinkProposal) proposal).getProposalId(), ((ATableModel) element).getId());
      });
      if (getViewer().getControl().getDisplay() != null) {
        field.openShell();
      }
    }
    return false;
  }

  @Override
  protected Object getValue(Object element) {
    return userList.indexOf(getStringValue(element));
  }

  protected String getStringValue(Object element) {
    if (userToResponibilitiesMap.containsKey(((ATableModel) element).getId())) {
      String value = "";
      for (IUser user : userRegister) {
        if(userToResponibilitiesMap.get(((ATableModel) element).getId()).contains(user.getUserId())) {
          value += user.getUsername() + ", ";
        }
      }
      return value.substring(0, value.length() - 2);
    }
    return Messages.ResponsibilityEditingSupport_0;
  }

  @Override
  protected void setValue(Object element, Object value) {
  }

  public Map<UUID, List<UUID>> save() {
    return userToResponibilitiesMap;
  }
}
