/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.IControlStructureEditor;
import xstampp.astpa.controlstructure.controller.editparts.RootEditPart;
import xstampp.preferences.IControlStructureConstants;

public class CSConnectionModeProvider extends AbstractSourceProvider implements IPropertyChangeListener{
	public static final String CONNECTION_MODE= "xstampp.astpa.connectionMode";
	public static final String PROCESS_MODEL_BORDER= "xstampp.astpa.ProcessModelBorder";
	public static final String SHOW_CALIST_BORDER= "xstampp.astpa.ShowCAListBorder";
	
	public CSConnectionModeProvider() {
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map getCurrentState() {

		Map<String,Object> map = new HashMap<>();
		boolean f=Activator.getDefault().getPreferenceStore().getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS);
		if(f){
			map.put(CONNECTION_MODE, Boolean.TRUE);
		}else{
			map.put(CONNECTION_MODE, Boolean.FALSE);
		}
		f =Activator.getDefault().getPreferenceStore().getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER); 
		map.put(PROCESS_MODEL_BORDER, f);
		
		f =Activator.getDefault().getPreferenceStore().getBoolean(IControlStructureConstants.CONTROLSTRUCTURE_SHOW_LISTOFCA_BORDER); 
		map.put(SHOW_CALIST_BORDER, f);
		return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[]{CONNECTION_MODE,PROCESS_MODEL_BORDER,SHOW_CALIST_BORDER};
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		if(event.getProperty().equals(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS)){
			boolean newValue = false;
			if(event.getNewValue() instanceof String){
				newValue=Boolean.parseBoolean((String) event.getNewValue());
			}else if(event.getNewValue() instanceof Boolean){
				newValue = (boolean) event.getNewValue();
			}
			fireSourceChanged(ISources.WORKBENCH,CSConnectionModeProvider.CONNECTION_MODE,newValue);
			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if(part instanceof IControlStructureEditor 
			&& ((IControlStructureEditor) part).getGraphicalViewer().getEditDomain().getActiveTool() instanceof ConnectionCreationTool){
				RootEditPart editpart = (RootEditPart) ((IControlStructureEditor)part).getGraphicalViewer().getContents();
				//the anchors grid is to be set if the manual connection mode value (newValue) is false
				editpart.setAnchorsGrid(!newValue);
			}
		}else if(event.getProperty().equals(IControlStructureConstants.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER)){

			boolean newValue=(boolean) event.getNewValue();
			fireSourceChanged(ISources.WORKBENCH,CSConnectionModeProvider.PROCESS_MODEL_BORDER,newValue);
			
		}else if(event.getProperty().equals(IControlStructureConstants.CONTROLSTRUCTURE_SHOW_LISTOFCA_BORDER)){

			boolean newValue=(boolean) event.getNewValue();
			fireSourceChanged(ISources.WORKBENCH,CSConnectionModeProvider.SHOW_CALIST_BORDER,newValue);
			
		}
	}

	
}
