package xstampp.astpa.ui.unsafecontrolaction;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import xstampp.astpa.model.interfaces.ISeverityDataModel;
import xstampp.astpa.model.interfaces.ISeverityEntry;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.model.IDataModel;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.LinkProposal;
import xstampp.ui.common.grid.CellButton;

public class SeverityButton extends CellButton {
  private ISeverityDataModel model;
  private ISeverityEntry uca;
  private Severity currentSeverity;
  private Control control;

  public SeverityButton(ISeverityEntry uca, IDataModel model, Control grid) {
    
    super(new Rectangle(0, 0,40,16),"");
    this.control = grid;
    this.uca = uca;
    this.model = (ISeverityDataModel) model;
    currentSeverity = this.uca.getSeverity();
    setText(currentSeverity.toString());
  }
  
  @Override
  public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
    LinkProposal[] proposals = new LinkProposal[4];
    for(int i = 0; i < Severity.values().length;i++) {
      proposals[i] = new LinkProposal();
      proposals[i].setDescription(Severity.values()[i].getDescription());
      proposals[i].setLabel(Severity.values()[i].toString());
      if(currentSeverity.ordinal() == i) {
        proposals[i].setSelected(true);
      }
    }
    AutoCompleteField assist = new AutoCompleteField(proposals, control);
    assist.setPopupPosition(new Point(relativeMouse.x + cellBounds.x,
                                        relativeMouse.y + cellBounds.y));
    if (this.control.getDisplay() != null) {
      assist.setProposalListener(new IContentProposalListener() {

        @Override
        public void proposalAccepted(IContentProposal proposal) {
          Severity severity = Severity.valueOf(proposal.getLabel());
          if (severity != null) {
            currentSeverity = severity;
            model.setSeverity(uca, severity);
            setText(severity.toString());
            control.redraw();
          }
        }
      });
      assist.openPopup();
    } else {
      MessageDialog.openError(null, "Widget is disposed",
          "for some reason the Platform can't find a suficient display!");
    }
  }
}
