package xstampp.ui.common.grid;

import java.util.List;
import java.util.UUID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.astpa.haz.ITableModel;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.ITableContentProvider;
import xstampp.ui.common.contentassist.LinkProposal;

public class CellButtonLinking<T extends ITableContentProvider<?>> extends CellButton {
  private final class PropopsalListener implements IContentProposalListener {

    @Override
    public void proposalAccepted(IContentProposal proposal) {
      UUID linkId = ((LinkProposal)proposal).getProposalId();
      if (linkId != null) {
        publicInterface.addLink(assignedId, linkId);
      }
    }
  }
  private T publicInterface;
  private UUID assignedId;
  private GridWrapper grid;

  public CellButtonLinking(GridWrapper grid, T publicInterface,UUID assignedId) {
    super(new Rectangle(-1, -1,
        GridWrapper.getLinkButton16().getBounds().width,
        GridWrapper.getLinkButton16().getBounds().height),
    GridWrapper.getLinkButton16());
    this.grid = grid;
    // TODO Auto-generated constructor stub
    this.publicInterface = publicInterface;
    this.assignedId = assignedId;
    
  }

  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    List<? extends ITableModel> linkedItems = this.publicInterface.getLinkedItems(this.assignedId);
   
    List<? extends ITableModel> items = this.publicInterface.getAllItems();
    // remove all already linked items
    items.removeAll(linkedItems);

    LinkProposal[] proposals = new LinkProposal[items.size()];
    
    for (int i = 0; i < items.size(); i++) {
      String itemNumber = this.publicInterface.getPrefix() + items.get(i).getNumber();
      String itemTitle = items.get(i).getTitle();
      proposals[i] = new LinkProposal();
      proposals[i].setProposalId(items.get(i).getId());
      proposals[i].setLabel(itemNumber +" - " + itemTitle);
      proposals[i].setDescription(items.get(i).getDescription());
    }
    
    AutoCompleteField linkField = new AutoCompleteField(proposals);

    linkField.setPopupPosition(grid.getGrid().toDisplay(relativeMouse.x + cellBounds.x,
                                                    relativeMouse.y + cellBounds.y));

    if (this.grid.getGrid().getDisplay() != null) {
      linkField.setProposalListener(new PropopsalListener());
      linkField.openPopup();
    } else {
      MessageDialog.openError(null, "Widget is disposed",
          "for some reason the Platform can't find a suficient display!");
    }
  }
}
