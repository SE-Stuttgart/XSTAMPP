package xstampp.astpa.ui.causalfactors;

import java.util.List;
import java.util.UUID;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.model.IEntryFilter;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.LinkProposal;
import xstampp.ui.common.grid.GridCellButton;

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
		super("Add Causal Entry");

		this.component = component;
    this.factorId = factorId;
    this.dataInterface = dataInterface;
    this.grid = grid;
	}

	@Override
	public void onMouseDown(MouseEvent e,Point relativeMouse,
			Rectangle cellBounds) {
	  final List<UUID> linkedIds = dataInterface.getLinkedUCAList();
	  List<ICorrespondingUnsafeControlAction> ucaList = dataInterface.getUCAList(new IEntryFilter<IUnsafeControlAction>() {
      
      @Override
      public boolean check(IUnsafeControlAction model) {
        return !linkedIds.contains(model.getId());
      }
    });
	  LinkProposal[] proposals = new LinkProposal[ucaList.size()+1];
    int index = 1;
    proposals[0] = new LinkProposal();
    proposals[0].setLabel(HAZARD_ENTRY);
    proposals[0].setDescription(HAZARD_ENTRY);
    proposals[0].setProposalId(null);
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
      }
    });
	  diag.openPopup();
	}

}