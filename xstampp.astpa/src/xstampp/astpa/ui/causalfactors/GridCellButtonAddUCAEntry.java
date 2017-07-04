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

import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
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

  private static String HAZARD_ENTRY = "hazard based entry"; //$NON-NLS-1$
	private ICausalComponent component;
  private ICausalFactorDataModel dataInterface;
  private UUID factorId;
  private Grid grid;

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param component
	 *            the component the add buttons adds causal factors to.
	 */
	public GridCellButtonAddUCAEntry(ICausalComponent component,UUID factorId, ICausalFactorDataModel dataInterface,Grid grid) {
		super("Add Unsafe Control Action");

		this.component = component;
    this.factorId = factorId;
    this.dataInterface = dataInterface;
    this.grid = grid;
	}

	@Override
	public void onMouseDown(MouseEvent e,Point relativeMouse,
			Rectangle cellBounds) {
	  final List<UUID> linkedIds = dataInterface.getLinkedUCAList(factorId);
	  List<ICorrespondingUnsafeControlAction> ucaList = dataInterface.getUCAList(new IEntryFilter<IUnsafeControlAction>() {
      
      @Override
      public boolean check(IUnsafeControlAction model) {
        boolean canWrite = true;
        if (dataInterface instanceof IUserProject) {
          canWrite = ((IUserProject) dataInterface).getUserSystem().checkAccess(
              dataInterface.getControlActionForUca(model.getId()).getId(), AccessRights.WRITE);
        }
        return canWrite && !linkedIds.contains(model.getId()) && !dataInterface.getLinksOfUCA(model.getId()).isEmpty();
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
	  diag.setPopupPosition(new Point(relativeMouse.x + cellBounds.x, relativeMouse.y + cellBounds.y));
	  diag.setProposalListener(new IContentProposalListener() {
      
      @Override
      public void proposalAccepted(IContentProposal proposal) {
        UUID id = ((LinkProposal)proposal).getProposalId();
        if(id == null){
          dataInterface.addCausalHazardEntry(component.getId(), factorId);
        }else{
          dataInterface.addCausalUCAEntry(component.getId(), factorId, id);
        }
        grid.redraw();
      }
    });
	  diag.openPopup();
	}

}