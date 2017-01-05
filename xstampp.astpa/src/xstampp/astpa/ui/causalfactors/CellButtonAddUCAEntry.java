package xstampp.astpa.ui.causalfactors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import messages.Messages;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.model.IEntryFilter;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.grid.GridCellButton;

/**
 * The add button.
 * 
 * @author Patrick Wickenhaeuser
 * 
 */
public class CellButtonAddUCAEntry extends GridCellButton implements IContentProposalListener {

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
	public CellButtonAddUCAEntry(ICausalComponent component,UUID factorId, ICausalFactorDataModel dataInterface,Grid grid) {
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
    String[] literals = new String[ucaList.size()+1];
    String[] labels = new String[ucaList.size()+1];
    String[] descriptions = new String[ucaList.size()+1];
    final Map<String,UUID> ucaMap = new HashMap<>();
    int index = 1;
    literals[0]= HAZARD_ENTRY;
    labels[0]= HAZARD_ENTRY;
    descriptions[0]= HAZARD_ENTRY;
    for (ICorrespondingUnsafeControlAction uca : ucaList) {
      literals[index] = uca.getTitle();
      labels[index] = uca.getTitle();
      descriptions[index] = uca.getDescription();
      ucaMap.put(labels[index], uca.getId());
      index++;
    }
    
  
	  AutoCompleteField diag = new AutoCompleteField(null, new TextContentAdapter(), literals, labels, descriptions);
	  diag.setPopupPosition(grid.toDisplay(relativeMouse.x + cellBounds.x, relativeMouse.y + cellBounds.y));
	  diag.setProposalListener(new IContentProposalListener() {
      
      @Override
      public void proposalAccepted(IContentProposal proposal) {
        if(proposal.getLabel().equals(HAZARD_ENTRY)){
          dataInterface.addCausalHazardEntry(component.getId(), factorId);
        }else{
          dataInterface.addCausalUCAEntry(component.getId(), factorId, ucaMap.get(proposal.getLabel()));
        }
      }
    });
	  diag.openPopup();
	}

  @Override
  public void proposalAccepted(IContentProposal proposal) {
    // TODO Auto-generated method stub
    
  }
}