/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.ui.causalfactors;

import java.util.List;
import java.util.UUID;

import org.eclipse.swt.graphics.GC;

import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.model.ObserverValue;
import xstampp.ui.common.contentassist.ITableContentProvider;
import xstampp.ui.common.grid.CellButtonAdd;
import xstampp.ui.common.grid.CellButtonLinking;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class CellEditorHazardSafetyConstraint extends GridCellTextEditor {

  private ICausalFactorDataModel causalDataInterface;
  private Link ucaHazLink;

  public CellEditorHazardSafetyConstraint(GridWrapper gridWrapper, Link ucaHazLink,
      ICausalFactorDataModel dataInterface) {
    super(gridWrapper, "",
        ucaHazLink.getId());
    this.ucaHazLink = ucaHazLink;
    setShowDelete(true);
    this.causalDataInterface = dataInterface;
  }

  @Override
  public String getCurrentText() {
    UUID linkB = causalDataInterface.getLinkController()
        .getRawLinksFor(ObserverValue.UcaHazLink_SC2_LINK, this.ucaHazLink.getId()).stream()
        .findFirst().orElse(new Link(null, null)).getLinkB();
    return this.causalDataInterface.getCausalFactorController().getConstraintTextFor(linkB);
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    if (this.causalDataInterface.getLinkController().isLinked(ObserverValue.UcaHazLink_SC2_LINK,
        this.ucaHazLink.getId())) {
      addCellButton(
          new CellButtonAdd(() ->  {
            
          }));
      addCellButton(new CellButtonLinking<CSCProvider>(getGridWrapper(), new CSCProvider(), this.ucaHazLink.getId()));
      paintFrame(renderer, gc, item);
    } else {
      super.paint(renderer, gc, item);
    }
  }

  @Override
  public void updateDataModel(String newText) {

  }

  @Override
  public void delete() {
    this.causalDataInterface.getLinkController().deleteAllFor(ObserverValue.UcaHazLink_SC2_LINK,
        this.ucaHazLink.getId());
  }

  @Override
  protected void editorOpening() {
    causalDataInterface.lockUpdate();
  }

  @Override
  protected void editorClosing() {
    causalDataInterface.releaseLockAndUpdate(new ObserverValue[] {});
  }

  private class CSCProvider implements ITableContentProvider<ITableModel> {

    @Override
    public List<ITableModel> getAllItems() {
      return causalDataInterface.getCorrespondingSafetyConstraints();
    }

    @Override
    public String getEmptyMessage() {
      return "No Corresponding Safety Constraints defined!";
    }

    @Override
    public List<ITableModel> getLinkedItems(UUID itemId) {
      return null;
    }

    @Override
    public void addLink(UUID item1, UUID item2) {
      causalDataInterface.getCorrespondingSafetyConstraints().stream().filter((x) -> x.getId().equals(item2)).findFirst().ifPresent(y -> {
      });
      //causalDataInterface.getLinkController().addLink(ObserverValue.UcaHazLink_SC2_LINK, item1, item2);
      
    }

    @Override
    public void removeLink(UUID item, UUID removeItem) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public String getPrefix() {
      // TODO Auto-generated method stub
      return null;
    }
    
  }
}
