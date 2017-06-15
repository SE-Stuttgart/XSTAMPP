package xstampp.astpa.ui.unsafecontrolaction;

import java.util.UUID;

import org.eclipse.swt.graphics.GC;

import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellText;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class UcaIdCell extends GridCellText {

  private UcaContentProvider provider;
  private UUID ucaId;
  public UcaIdCell(String text, UcaContentProvider provider,UUID id) {
    super(text);
    this.provider = provider;
    this.ucaId = id;
    
  }
  
  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    if (!provider.getLinkedItems(ucaId).isEmpty()) {
      super.paint(renderer, gc, item);
    }else {

      this.paintFrame(renderer, gc, item);
    }
  }
}
