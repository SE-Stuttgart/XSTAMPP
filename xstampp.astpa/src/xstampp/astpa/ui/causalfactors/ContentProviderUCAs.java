/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.causalfactors;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.ui.unsafecontrolaction.UnsafeControlActionsView;
import xstampp.model.IEntryFilter;
import xstampp.ui.common.contentassist.ITableContentProvider;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUserProject;

/**
 * 
 * @author Lukas Balzer
 * 
 */
public class ContentProviderUCAs
    implements ITableContentProvider<ICorrespondingUnsafeControlAction> {

  private final transient ICausalFactorDataModel caInterface;

  /**
   * 
   * @author Benedikt Markt
   * 
   * @param caInterface
   *          The data model interface
   * 
   */
  public ContentProviderUCAs(final ICausalFactorDataModel caInterface) {
    this.caInterface = caInterface;
  }

  @Override
  public List<ICorrespondingUnsafeControlAction> getAllItems() {
    return this.caInterface.getUCAList(new IEntryFilter<IUnsafeControlAction>() {

      @Override
      public boolean check(IUnsafeControlAction model) {
        boolean canWrite = true;
        if (caInterface instanceof IUserProject) {
          canWrite = ((IUserProject) caInterface).getUserSystem().checkAccess(
              caInterface.getControlActionForUca(model.getId()).getId(), AccessRights.WRITE);
        }
        return canWrite && !caInterface.getLinksOfUCA(model.getId()).isEmpty();
      }
    });
  }

  @Override
  public List<ICorrespondingUnsafeControlAction> getLinkedItems(final UUID itemId) {
    return null;
  }

  @Override
  public void addLink(final UUID item1, final UUID item2) {
    this.caInterface.addCausalUCAEntry(null, item1, item2);

  }

  @Override
  public void removeLink(final UUID item, final UUID removeItem) {
    this.caInterface.removeCausalEntry(null, item, removeItem);

  }

  @Override
  public String getPrefix() {
    return UnsafeControlActionsView.UCA1;
  }

  @Override
  public String getEmptyMessage() {
    return ""; //$NON-NLS-1$
  }

}
