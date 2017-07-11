/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstpapriv.ui;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.controlstructure.CSEditorWithPM;

public class CSContextEditor extends CSEditorWithPM {

	public static final String ID = "xstpapriv.editor.context";
	
	public CSContextEditor() {
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
            .findView(View.ID) == null) {
          IWorkbenchPage page = PlatformUI.getWorkbench()
              .getActiveWorkbenchWindow().getActivePage();
          try {
            part = page.showView(View.ID);
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
