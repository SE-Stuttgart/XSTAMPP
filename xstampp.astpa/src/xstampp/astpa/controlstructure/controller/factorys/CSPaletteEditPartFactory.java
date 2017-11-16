package xstampp.astpa.controlstructure.controller.factorys;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.palette.PaletteEditPartFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.swt.widgets.Display;

import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.LinkProposal;

public class CSPaletteEditPartFactory extends PaletteEditPartFactory {

  private IControlStructureEditorDataModel dataModel;
  private PaletteViewer viewer;

  public enum ToolProperty {
    CHOOSE_ID_FROM
  }

  public CSPaletteEditPartFactory(IControlStructureEditorDataModel dataModel, PaletteViewer viewer) {
    this.dataModel = dataModel;
    this.viewer = viewer;
  }

  @Override
  public EditPart createEditPart(EditPart parentEditPart, Object model) {
    if (model instanceof ToolEntry) {
      Object toolProperty = ((ToolEntry) model).getToolProperty(ToolProperty.CHOOSE_ID_FROM);
      if (toolProperty != null && toolProperty.equals(ComponentType.CONTROLACTION)) {
        final AbstractGraphicalEditPart createEntryEditPart = (AbstractGraphicalEditPart) super.createEntryEditPart(
            parentEditPart, model);
        ((Clickable) createEntryEditPart.getFigure()).addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent event) {
            viewer.setActiveTool(null);
            chooseCreationId(dataModel.getAllControlActions(), (ToolEntry) createEntryEditPart.getModel());
          }
        });
        return createEntryEditPart;
      }
    }
    return super.createEditPart(parentEditPart, model);
  }

  private void chooseCreationId(List<? extends IControlAction> list, final ToolEntry toolEntry) {
    List<LinkProposal> proposals = new ArrayList<>();

    LinkProposal newProposal = new LinkProposal();
    newProposal.setLabel("New Control Action");
    newProposal.setSelected(true);
    proposals.add(newProposal);
    boolean skipDialog = true;
    for (IControlAction tableModel : list) {
      if (tableModel.getComponentLink() == null) {
        LinkProposal prop = new LinkProposal();
        prop.setDescription(tableModel.getDescription());
        prop.setLabel(tableModel.getIdString() + ": " + tableModel.getTitle());
        prop.setProposalId(tableModel.getId());
        proposals.add(prop);
        skipDialog = false;
      }
    }
    if(skipDialog) {
      viewer.setActiveTool(toolEntry);
    } else {
      AutoCompleteField field = new AutoCompleteField(proposals.toArray(new LinkProposal[0]),
          Display.getDefault().getActiveShell());
      field.setProposalListener(new IContentProposalListener() {
  
        @Override
        public void proposalAccepted(IContentProposal proposal) {
          Object property = toolEntry.getToolProperty(CreationTool.PROPERTY_CREATION_FACTORY);
          if(property instanceof CSModelCreationFactory) {
            CSModelCreationFactory toolProperty = (CSModelCreationFactory) property;
            toolProperty.setChosenUUID(((LinkProposal) proposal).getProposalId());
          }
          viewer.setActiveTool(toolEntry);
        }
      });
      field.openPopup();
    }
  }
}
