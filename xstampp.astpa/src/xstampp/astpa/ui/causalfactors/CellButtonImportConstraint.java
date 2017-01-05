package xstampp.astpa.ui.causalfactors;

import java.util.List;
import java.util.UUID;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.sds.interfaces.ISafetyConstraint;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.grid.CellButton;
import xstampp.ui.common.grid.GridWrapper;

public class CellButtonImportConstraint extends CellButton {

  private ICausalFactorEntry entry = null;
  private UUID componentId;
  private UUID factorId;
  private Grid grid;
  private ICausalFactorDataModel dataModel;

  public CellButtonImportConstraint(Grid grid, ICausalFactorEntry entry,UUID componentId,UUID factorId,ICausalFactorDataModel dataInterface) {
    super(new Rectangle(40, 1,
        GridWrapper.getLinkButton16().getBounds().width,
        GridWrapper.getLinkButton16().getBounds().height),
    GridWrapper.getLinkButton16());
    
    this.grid = grid;
    this.entry = entry;
    this.componentId = componentId;
    this.factorId = factorId;
    dataModel = dataInterface;
  }

  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    List<ISafetyConstraint> safetyConstraints = dataModel.getCorrespondingSafetyConstraints();
    Text selectedString = new Text(grid, SWT.PUSH);
    if (safetyConstraints.size() <= 0) {
      return;
    }
    String[] labels = new String[safetyConstraints.size()];
    String[] description = new String[safetyConstraints.size()];
    String[] literals = new String[safetyConstraints.size()];
    String tmp;
    for (int i = 0; i < safetyConstraints.size(); i++) {
      tmp = safetyConstraints.get(i).getText();
      if (tmp.length() >= 1) {
        labels[i]=tmp.substring(0, Math.min(tmp.length(), 40))+  "...";
        description[i]=tmp;
        literals[i]=new String();
      }
    }

    AutoCompleteField scLinking = new AutoCompleteField(selectedString,
        new TextContentAdapter(), literals, labels,description);
    scLinking.setProposalListener(
        new IContentProposalListener() {

          @Override
          public void proposalAccepted(IContentProposal proposal) {
            CausalFactorEntryData entryData = new CausalFactorEntryData(entry.getId());
            entryData.setConstraint(proposal.getContent());
            dataModel.changeCausalEntry(componentId, factorId, entryData);
          }
        });

    scLinking.setPopupPosition(relativeMouse, cellBounds, 0);
    scLinking.openPopup();

  }

}
