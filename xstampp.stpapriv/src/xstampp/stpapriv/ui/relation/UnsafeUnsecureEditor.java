package xstampp.stpapriv.ui.relation;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.controlstructure.CSEditor;



public class UnsafeUnsecureEditor extends CSEditor{

	public static final String ID = "stpapriv.editor.unsafe";
	public UnsafeUnsecureEditor() {
	    super();
	    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
	    .addPartListener(new IPartListener2() {
	      private IViewPart part = null;
	      @Override
	      public void partVisible(IWorkbenchPartReference partRef) {
	        if (partRef.getId().equals(ID)){
	          partOpened(partRef);
	        }
	      }
	      
	      @Override
	      public void partOpened(IWorkbenchPartReference partRef) {
	        if (partRef.getId().equals(ID) && PlatformUI.getWorkbench()
	            .getActiveWorkbenchWindow().getActivePage()
	            .findView(PrivacyRelationsView.ID) == null) {
	          IWorkbenchPage page = PlatformUI.getWorkbench()
	              .getActiveWorkbenchWindow().getActivePage();
	          try {
	            part = page.showView(PrivacyRelationsView.ID);
	          } catch (PartInitException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	          }

	        }
	      }
	      
	      @Override
	      public void partInputChanged(IWorkbenchPartReference partRef) {
	        // TODO Auto-generated method stub
	        
	      }
	      
	      @Override
	      public void partHidden(IWorkbenchPartReference partRef) {
	        if(partRef.getId().equals(ID) && part != null){
	          IWorkbenchPage page = PlatformUI.getWorkbench()
	              .getActiveWorkbenchWindow().getActivePage();
	          page.hideView(part);
	        }
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
	        // TODO Auto-generated method stub
	        
	      }
	      
	      @Override
	      public void partActivated(IWorkbenchPartReference partRef) {
	        // TODO Auto-generated method stub
	        
	      }
	    });
		}
		
}
