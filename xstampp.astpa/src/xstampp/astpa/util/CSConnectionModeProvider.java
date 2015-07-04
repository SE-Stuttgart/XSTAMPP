package xstampp.astpa.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.controlstructure.IControlStructureEditor;
import xstampp.astpa.controlstructure.controller.editparts.RootEditPart;
import xstampp.astpa.preferences.IAstpaPreferences;

public class CSConnectionModeProvider extends AbstractSourceProvider implements IPropertyChangeListener{
	public static final String CONNECTION_MODE= "xstampp.astpa.connectionMode";
	public static final String PROCESS_MODEL_BORDER= "xstampp.astpa.ProcessModelBorder";
	
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
		boolean f=Activator.getDefault().getPreferenceStore().getBoolean(IAstpaPreferences.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS);
		if(f){
			map.put(CONNECTION_MODE, Boolean.TRUE);
		}else{
			map.put(CONNECTION_MODE, Boolean.FALSE);
		}
		f =Activator.getDefault().getPreferenceStore().getBoolean(IAstpaPreferences.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER); 
		map.put(PROCESS_MODEL_BORDER, f);
		return map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[]{CONNECTION_MODE,PROCESS_MODEL_BORDER};
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getProperty().equals(IAstpaPreferences.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS)){
			boolean newValue=(boolean) event.getNewValue();
			fireSourceChanged(ISources.WORKBENCH,CSConnectionModeProvider.CONNECTION_MODE,newValue);
			IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if(part instanceof IControlStructureEditor){
				RootEditPart editpart = (RootEditPart) ((IControlStructureEditor)part).getGraphicalViewer().getContents();
				editpart.setAnchorsGrid(!newValue);
			}
		}else if(event.getProperty().equals(IAstpaPreferences.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER)){

			boolean newValue=(boolean) event.getNewValue();
			fireSourceChanged(ISources.WORKBENCH,CSConnectionModeProvider.PROCESS_MODEL_BORDER,newValue);
			
		}
	}

	
}
