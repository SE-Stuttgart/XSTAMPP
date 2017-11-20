/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.ui.causalfactors;

import java.util.List;
import java.util.UUID;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.IEntryFilter;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.LinkProposal;
import xstampp.ui.common.grid.GridCellButton;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUserProject;

/**
 * The add button.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class GridCellButtonAddUCAEntry extends GridCellButton {

  private IRectangleComponent component;
  private ICausalFactorDataModel dataInterface;
  private UUID factorId;
  private Grid grid;
  private List<IUnsafeControlAction> ucas;

  /**
   * Ctor.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param component
   *          the component the add buttons adds causal factors to.
   * 
   */
  public GridCellButtonAddUCAEntry(IRectangleComponent component, UUID factorId,
      ICausalFactorDataModel dataInterface, Grid grid, List<IUnsafeControlAction> ucas) {
    super("Add Unsafe Control Action");

    this.component = component;
    this.factorId = factorId;
    this.dataInterface = dataInterface;
    this.grid = grid;
    this.ucas = ucas;
  }

  @Override
  public void onMouseDown(MouseEvent e, Point relativeMouse,
      Rectangle cellBounds) {
    List<ICorrespondingUnsafeControlAction> ucaList = dataInterface
        .getUCAList(new IEntryFilter<IUnsafeControlAction>() {

          @Override
          public boolean check(IUnsafeControlAction model) {
            boolean canWrite = true;
            if (dataInterface instanceof IUserProject) {
              canWrite = ((IUserProject) dataInterface).getUserSystem().checkAccess(
                  dataInterface.getControlActionForUca(model.getId()).getId(), AccessRights.WRITE);
            }
            return canWrite && !ucas.contains(model)
                && !dataInterface.getLinksOfUCA(model.getId()).isEmpty();
          }
        });
    LinkProposal[] proposals = new LinkProposal[ucaList.size()];
    int index = 0;
    for (ICorrespondingUnsafeControlAction uca : ucaList) {

      proposals[index] = new LinkProposal();
      proposals[index].setLabel(uca.getTitle());
      proposals[index].setDescription(uca.getDescription());
      proposals[index].setProposalId(uca.getId());
      index++;
    }

    AutoCompleteField diag = new AutoCompleteField(proposals, grid);
    diag.setPopupPosition(
        new Point(relativeMouse.x + cellBounds.x, relativeMouse.y + cellBounds.y));
    diag.setProposalListener(new IContentProposalListener() {

      @Override
      public void proposalAccepted(IContentProposal proposal) {
        UUID id = ((LinkProposal) proposal).getProposalId();
        UUID ucaCFLinkId = dataInterface.getLinkController().addLink(LinkingType.UCA_CausalFactor_LINK, id, factorId);
        UUID linkId = dataInterface.getLinkController().addLink(LinkingType.UcaCfLink_Component_LINK, ucaCFLinkId,
            component.getId());
        for (UUID hazId : dataInterface.getLinkController().getLinksFor(LinkingType.UCA_HAZ_LINK, id)) {
          dataInterface.getLinkController().addLink(LinkingType.CausalEntryLink_HAZ_LINK, linkId, hazId);
        }
        grid.redraw();
      }
    });
    diag.openPopup();
  }

}