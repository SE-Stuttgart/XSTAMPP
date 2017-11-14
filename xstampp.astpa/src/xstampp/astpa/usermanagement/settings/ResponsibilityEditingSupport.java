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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.usermanagement.Messages;
import xstampp.ui.common.contentassist.LinkProposal;
import xstampp.ui.common.contentassist.MultiSelectionCellEditor;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;

public class ResponsibilityEditingSupport extends EditingSupport {

  private Map<UUID, List<UUID>> userToResponibilitiesMap;
  private Map<UUID, List<UUID>> localResponibilitiesMap;
  private List<String> userList;
  private IUserSystem userSystem;
  private List<IUser> userRegister;

  /**
   * 
   * @param viewer
   *          must be a {@link TableViewer}
   * @param parent
   */
  public ResponsibilityEditingSupport(ColumnViewer viewer, IUserProject project) {
    super(viewer);
    Assert.isTrue(viewer instanceof TableViewer);
    this.userToResponibilitiesMap = new HashMap<>();
    this.localResponibilitiesMap = new HashMap<>();
    userSystem = project.getUserSystem();
    this.userList = new ArrayList<>();
    this.userRegister = new ArrayList<>();
    for (IUser user : this.userSystem.getRegistry()) {
      if (user.checkAccess(AccessRights.ACCESS)) {
        this.userList.add(user.getUsername());
        this.userRegister.add(user);
        for (UUID entryId : this.userSystem.getResponsibilities(user.getUserId())) {
          if (!this.userToResponibilitiesMap.containsKey(entryId)) {
            this.userToResponibilitiesMap.put(entryId, new ArrayList<>());
          }
          this.userToResponibilitiesMap.get(entryId).add(user.getUserId());
        }
      }
    }
  }

  private void mapResp(UUID entryId, UUID userId, boolean remove) {
    if (remove && this.localResponibilitiesMap.containsKey(entryId)) {
      this.localResponibilitiesMap.get(entryId).remove(userId);
    } else if (!this.localResponibilitiesMap.containsKey(entryId)) {
      List<UUID> list = this.userToResponibilitiesMap.getOrDefault(entryId, new ArrayList<>());
      this.localResponibilitiesMap.put(entryId, list);
    }
    if (!(remove || this.localResponibilitiesMap.get(entryId).contains(userId))) {
      this.localResponibilitiesMap.get(entryId).add(userId);
    }
  }

  @Override
  protected CellEditor getCellEditor(Object element) {
    List<LinkProposal> proposals = new ArrayList<>();
    for (IUser user : this.userRegister) {
      LinkProposal prop = new LinkProposal();
      prop.setLabel(user.getUsername());
      prop.setProposalId(user.getUserId());
      prop.setSelected(user.isResponsible(((ATableModel) element).getId()));
      proposals.add(prop);
    }
    return new MultiSelectionCellEditor(((TableViewer) getViewer()).getTable(),
        proposals, SWT.BORDER | SWT.READ_ONLY);
  }

  @Override
  protected boolean canEdit(Object element) {
    return true;
  }

  @Override
  protected Object getValue(Object element) {

    this.userRegister.clear();
    for (IUser user : this.userSystem.getRegistry()) {
      if (user.checkAccess(AccessRights.ACCESS)) {
        this.userList.add(user.getUsername());
        this.userRegister.add(user);
      }
    }
    List<LinkProposal> proposals = new ArrayList<>();
    for (IUser user : this.userRegister) {
      LinkProposal prop = new LinkProposal();
      prop.setLabel(user.getUsername());
      prop.setProposalId(user.getUserId());
      prop.setSelected(isMapped(((ATableModel) element).getId(), user));
      proposals.add(prop);
    }
    return proposals;
  }

  private boolean isMapped(UUID id, IUser user) {
    boolean result = false;
    if (this.userToResponibilitiesMap.containsKey(id)) {
      result = this.userToResponibilitiesMap.get(id).contains(user.getUserId());
    }
    return result;
  }

  protected String getStringValue(Object element) {
    if (userToResponibilitiesMap.containsKey(((ATableModel) element).getId())) {
      String value = "";
      for (IUser user : userRegister) {
        if (isMapped(((ATableModel) element).getId(), user)) {
          value += user.getUsername() + ", ";
        }
      }
      return value.substring(0, value.length() - 2);
    }
    return Messages.ResponsibilityEditingSupport_0;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void setValue(Object element, Object value) {
    assert (value instanceof List<?>);
    ((List<LinkProposal>) value).stream()
        .forEach((prop) -> mapResp(((ATableModel) element).getId(), prop.getProposalId(), !prop.isSelected()));

    getViewer().refresh();
  }

  public Map<UUID, List<UUID>> save() {
    return localResponibilitiesMap;
  }
}
