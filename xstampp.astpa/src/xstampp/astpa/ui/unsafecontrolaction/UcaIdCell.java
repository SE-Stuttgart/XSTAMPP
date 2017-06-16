package xstampp.astpa.ui.unsafecontrolaction;

import java.util.UUID;

import org.eclipse.swt.graphics.GC;

import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.ISeverityEntry;
import xstampp.astpa.model.interfaces.IUnsafeControlActionDataModel;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class UcaIdCell extends GridCellText {

  private UcaContentProvider provider;
  private UUID ucaId;
  private IUnsafeControlActionDataModel ucaDataModel;
  private IUnsafeControlAction unsafeControlAction;
  public UcaIdCell(UcaContentProvider provider,IUnsafeControlAction entry, IUnsafeControlActionDataModel dataModel) {
    super("UCA1." + dataModel.getUCANumber(entry.getId()));
    this.provider = provider;
    this.unsafeControlAction = entry;
    this.ucaId = entry.getId();
    this.ucaDataModel = dataModel;
    
  }
  
  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    if (provider.getLinkedItems(ucaId).isEmpty()) {
      super.paintFrame(renderer, gc, item);
    }else {
      clearCellButtons();
      if(ucaDataModel.isUseSeverity()) {
        SeverityButton button = new SeverityButton((ISeverityEntry) unsafeControlAction,ucaDataModel, item.getParent());
        button.setHovered(renderer.getGridWrapper().getHoveredCell() == this);
        addCellButton(button);
      }
      super.paint(renderer, gc, item);
    }
  }
}
