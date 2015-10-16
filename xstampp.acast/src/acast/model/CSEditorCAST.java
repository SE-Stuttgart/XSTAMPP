package acast.model;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;

import acast.ui.accidentDescription.TableView;
import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.controller.editparts.CSAbstractEditPart;
import xstampp.astpa.controlstructure.controller.editparts.CSConnectionEditPart;

public class CSEditorCAST extends CSEditor {

	public CSEditorCAST() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(new IPartListener2() {

			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
				if (partRef.getId().equals("acast.steps.step2_1")) {

					if (TableView.visible) {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(PlatformUI
								.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("A-CAST.view1"));
					}
				}
			}

			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		List<EditPart> selection = this.getGraphicalViewer().getSelectedEditParts();
		if ((selection.size() == 1) && !(selection.get(0) instanceof CSConnectionEditPart)) {

			try {
				if (!TableView.visible) {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.showView("A-CAST.view1");
				}
			} catch (PartInitException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Request req = new Request();
			req.setType(RequestConstants.REQ_DIRECT_EDIT);
			((CSAbstractEditPart) selection.get(0)).performRequest(req);

			if (((CSAbstractEditPart) selection.get(0)).getFigure().getText() != null
					&& !(((CSAbstractEditPart) selection.get(0)).getFigure().getText().isEmpty())) {

				if (!PlatformUI.getPreferenceStore().contains("currentSelection")) {
					PlatformUI.getPreferenceStore().putValue("currentSelection",
							((CSAbstractEditPart) selection.get(0)).getFigure().getText());
					PlatformUI.getPreferenceStore().firePropertyChangeEvent("currentSelection",
							((CSAbstractEditPart) selection.get(0)).getFigure().getText(),
							((CSAbstractEditPart) selection.get(0)).getFigure().getText());
				} else {

					PlatformUI.getPreferenceStore().firePropertyChangeEvent("currentSelection",
							((CSAbstractEditPart) selection.get(0)).getFigure().getText(),
							((CSAbstractEditPart) selection.get(0)).getFigure().getText());
				}
			}
		}

	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		getGraphicalControl().addFocusListener(new FocusListener() {

			private IContextActivation activation;

			@Override
			public void focusLost(FocusEvent e) {
				IContextService contextService = (IContextService) getSite().getService(IContextService.class);

				contextService.deactivateContext(this.activation); // $NON-NLS-1$
			}

			@Override
			public void focusGained(FocusEvent e) {
				IContextService contextService = (IContextService) getSite().getService(IContextService.class);
				this.activation = contextService.activateContext("acastContextID");
				// $NON-NLS-1$
			}
		});
	}

}
