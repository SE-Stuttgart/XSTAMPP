package xstampp.astpa.controlstructure.controller.editparts;

import java.util.UUID;

import org.eclipse.draw2d.IFigure;

import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

public abstract class AbstractMemberEditPart extends CSAbstractEditPart implements IConnectable{

	private UUID relativeID;
	private IRelative relativePart;

	public AbstractMemberEditPart(IControlStructureEditorDataModel model,
			String stepId, Integer layer) {
		super(model, stepId, layer);
		
	}
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		
		UUID currentRelativeId=getDataModel().getComponent(getId()).getRelative();
		if(this.relativeID != currentRelativeId){
			IConnection conn= getDataModel().getConnection(currentRelativeId);
			Object tmp = getViewer().getEditPartRegistry().get(conn);
			if(this.relativePart != null){
				this.relativePart.eraseFeedback();
			}
			if(tmp instanceof IRelative){
				this.relativePart = (IRelative) tmp;
				this.relativeID = currentRelativeId;
				getDataModel().getRelatedProcessVariables(getId());
			}
		}
		
		if(relativePart != null){
			this.relativePart.updateFeedback();
		}

	}

	@Override
	public IFigure getFeedback() {
		if(this.relativePart == null){
			return null;
		}
		return this.relativePart.getFeedback(this);
	}

	@Override
	public UUID getRelativeId() {
		return this.relativeID;
	}

}
