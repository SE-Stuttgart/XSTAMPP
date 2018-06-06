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
package xstampp.astpa.ui.unsafecontrolaction;

import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.graphics.GC;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ISeverityEntry;
import xstampp.astpa.model.interfaces.IUnsafeControlActionDataModel;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.ui.SeverityButton;
import xstampp.astpa.ui.SeverityButton.SeverityCheck;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class UcaIdCell extends GridCellText {

  private UcaContentProvider provider;
  private UUID ucaId;
  private IUnsafeControlActionDataModel ucaDataModel;
  private IUnsafeControlAction unsafeControlAction;
  private SeverityButton button;

  public UcaIdCell(UcaContentProvider provider, IUnsafeControlAction entry,
      IUnsafeControlActionDataModel dataModel) {
    super("UCA1." + dataModel.getUCANumber(entry.getId()));
    this.provider = provider;
    this.unsafeControlAction = entry;
    this.ucaId = entry.getId();
    this.ucaDataModel = dataModel;

  }

  public void addSeverityButton(Grid grid, boolean enabled) {

    button = new SeverityButton((ISeverityEntry) unsafeControlAction,
        ucaDataModel, grid);
    button.setEnabled(enabled);
    button.setCheck(new SeverityCheck() {

      @Override
      public boolean checkSeverity(Severity serverity) {
        for (UUID uuid : ucaDataModel.getLinkController()
            .getLinksFor(LinkingType.UCA_HAZ_LINK, ucaId)) {
          Severity hazardSeverity = ((ATableModel) ucaDataModel.getHazard(uuid))
              .getSeverity();
          if (serverity.compareTo(hazardSeverity) < 0) {
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

              @Override
              public void run() {

                MessageDialog.openWarning(
                    PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                    xstampp.astpa.messages.Messages.UnsafeControlActionsView_InvalidSeverityTitle,
                    xstampp.astpa.messages.Messages.UnsafeControlActionsView_InvalidSeverityMsg);
              }
            });
            return false;
          }
        }
        return true;
      }
    });
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    if (provider.getLinkedItems(ucaId).isEmpty()) {
      paintFrame(renderer, gc, item);
    } else {
      if (ucaDataModel.isUseSeverity()) {
        addCellButton(button);
      }
      super.paint(renderer, gc, item);
    }
  }
}
