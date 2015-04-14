/**
 * 
 * @author Lukas Balzer
 */
package xstampp.astpa.controlstructure.controller.editparts;

import org.eclipse.draw2d.IFigure;

import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.controlstructure.figure.TextFieldFigure;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 *
 * @author Lukas Balzer
 *
 */
public class DashedBoxEditPart extends CSAbstractEditPart {

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param model
	 * @param stepId
	 */
	public DashedBoxEditPart(IControlStructureEditorDataModel model,
			String stepId) {
		super(model, stepId);
	}
	
	@Override
	protected IFigure createFigure() {
		IControlStructureFigure tmpFigure = new TextFieldFigure(this.getId());
		tmpFigure.setDeco(true);
		tmpFigure
				.setParent(((CSAbstractEditPart) this.getParent()).getFigure());
		return tmpFigure;
	}
}
