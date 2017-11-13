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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.model.ObserverValue;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.LinkProposal;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.GridWrapper;

public class CellButtonImportConstraint extends CellButton {

  private Grid grid;
  private ICausalFactorDataModel dataModel;
  private Link ucaHazLink;

  /**
   * 
   * @param grid
   * @param ucaHazLink
   *          a Link of type {@link ObserverValue#UCAEntryLink_HAZ_LINK}
   * @param dataInterface
   */
  public CellButtonImportConstraint(Grid grid, Link ucaHazLink, ICausalFactorDataModel dataInterface) {
    super(new Rectangle(-1, -1,
        GridWrapper.getLinkButton16().getBounds().width,
        GridWrapper.getLinkButton16().getBounds().height),
        GridWrapper.getLinkButton16());

    this.grid = grid;
    this.ucaHazLink = ucaHazLink;
    dataModel = dataInterface;
  }

  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    List<ITableModel> safetyConstraints = dataModel.getCorrespondingSafetyConstraints();

    List<LinkProposal> proposals = new ArrayList<>();
    String tmp;
    for (int i = 0; i < safetyConstraints.size(); i++) {
      tmp = safetyConstraints.get(i).getText();
      if (tmp != null && tmp.length() >= 1) {
        LinkProposal proposal = new LinkProposal();
        proposal.setLabel(
            safetyConstraints.get(i).getIdString() + " - " + tmp.substring(0, Math.min(tmp.length(), 20)) + "...");
        proposal.setDescription(tmp);
        proposals.add(proposal);
      }
    }

    AutoCompleteField scLinking = new AutoCompleteField(proposals.toArray(new LinkProposal[0]),
        grid);
    scLinking.setProposalListener(
        new IContentProposalListener() {

          @Override
          public void proposalAccepted(IContentProposal proposal) {
            UUID constraintId = dataModel.getCausalFactorController().addSafetyConstraint(proposal.getDescription());
            dataModel.getLinkController().addLink(ObserverValue.CausalHazLink_SC2_LINK, ucaHazLink.getId(),
                constraintId);
            grid.redraw();
          }
        });

    scLinking.setPopupPosition(
        new Point(relativeMouse.x + cellBounds.x, relativeMouse.y + cellBounds.y));
    scLinking.openPopup();

  }

}
