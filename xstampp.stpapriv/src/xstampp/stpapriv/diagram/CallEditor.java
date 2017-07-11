package xstampp.stpapriv.diagram;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import xstampp.stpapriv.model.results.ConstraintResult;

public class CallEditor extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // get the page
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        IWorkbenchPage page = window.getActivePage();
        // get the selection
        ISelection selection = HandlerUtil.getCurrentSelection(event);
//        System.out.println(selection!=null);
//        System.out.println(selection.getClass());
//        System.out.println(selection instanceof ConstraintResult);
//        if (selection != null && selection instanceof ConstraintResult) {
//        	System.out.println("Hier auch noch");
//            ConstraintResult obj = ((ConstraintResult) selection);
            // if we had a selection lets open the editor
//            if (obj != null) {
//                Task todo = (Task) obj;
//                DiagramView input = new DiagramView(obj.getTemp());
                try {
                    page.showView(DiagramView.ID);
                } catch (PartInitException e) {
                    throw new RuntimeException(e);
                }
//            }
//        }
        return null;
    }
}
