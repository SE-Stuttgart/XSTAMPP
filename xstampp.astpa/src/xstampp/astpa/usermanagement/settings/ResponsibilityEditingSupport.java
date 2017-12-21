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
import xstampp.model.IEntryWithId;
import xstampp.ui.common.contentassist.LinkProposal;
import xstampp.ui.common.contentassist.MultiSelectionCellEditor;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;

public class ResponsibilityEditingSupport extends EditingSupport {

  private Map<IEntryWithId, List<IUser>> localResponibilitiesMap;
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
    this.localResponibilitiesMap = new HashMap<>();
    userSystem = project.getUserSystem();
    this.userList = new ArrayList<>();
    this.userRegister = new ArrayList<>();
    for (IUser user : this.userSystem.getRegistry()) {
      if (user.checkAccess(AccessRights.ACCESS)) {
        this.userList.add(user.getUsername());
        this.userRegister.add(user);
      }
    }
  }

  private void mapResp(IEntryWithId entry, UUID uiserId, boolean remove) {
    IUser user = userRegister.stream().filter((userEntry) -> {
      return userEntry.getUserId().equals(uiserId);
    }).findFirst().orElse(null);
    if (remove && this.localResponibilitiesMap.containsKey(entry)) {
      this.localResponibilitiesMap.get(entry).remove(user);
    } else if (!this.localResponibilitiesMap.containsKey(entry)) {
      List<IUser> list = this.userSystem.getResponsibilities(entry);
      this.localResponibilitiesMap.put(entry, list);
    }
    if (!(remove || this.localResponibilitiesMap.get(entry).contains(user))) {
      this.localResponibilitiesMap.get(entry).add(user);
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
      prop.setSelected(isMapped(((ATableModel) element), user));
      proposals.add(prop);
    }
    return proposals;
  }

  private boolean isMapped(ATableModel element, IUser user) {
    List<IUser> responsibilities = this.userSystem.getResponsibilities(((ATableModel) element));
    localResponibilitiesMap.putIfAbsent(((ATableModel) element), responsibilities);
    return localResponibilitiesMap.get(element).contains(user);
  }

  protected String getStringValue(Object element) {
    List<IUser> responsibilities = this.userSystem.getResponsibilities(((ATableModel) element));
    localResponibilitiesMap.putIfAbsent(((ATableModel) element), responsibilities);
    if (!localResponibilitiesMap.get(element).isEmpty()) {
      String value = "";
      for (IUser user : localResponibilitiesMap.get(element)) {
        value += value.isEmpty() ? "" : ", ";
        value += user.getUsername();
      }
      return value;
    }
    return Messages.ResponsibilityEditingSupport_0;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void setValue(Object element, Object value) {
    assert (value instanceof List<?>);
    localResponibilitiesMap.putIfAbsent(((ATableModel) element), new ArrayList<>());
    localResponibilitiesMap.get(((ATableModel) element)).clear();
    ((List<LinkProposal>) value).stream()
        .forEach((prop) -> mapResp(((ATableModel) element), prop.getProposalId(), !prop.isSelected()));

    getViewer().refresh();
  }

  public Map<IEntryWithId, List<IUser>> save() {
    return localResponibilitiesMap;
  }
}
