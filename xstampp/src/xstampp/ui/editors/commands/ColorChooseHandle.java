package xstampp.ui.editors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.interfaces.ITextEditor;

public class ColorChooseHandle extends AbstractHandler {


	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object activeEditor = PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		String colorType= event.getParameter("xstampp.commandParameter.color.type");
		String redValue = event.getParameter("xstampp.commandParameter.color.red");
		String greenValue = event.getParameter("xstampp.commandParameter.color.green");
		String blueValue = event.getParameter("xstampp.commandParameter.color.blue");
		if(activeEditor instanceof ITextEditor && colorType != null){
			RGB newRgb;
			if(redValue != null && greenValue != null && blueValue != null){
				newRgb=new RGB(Integer.parseInt(redValue),
						Integer.parseInt(greenValue),
						Integer.parseInt(blueValue));
			}else{
				ColorDialog dialog = new ColorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				
				newRgb = dialog.open();
				((ITextEditor)activeEditor).setStyleColor(colorType, newRgb);
			}
			return newRgb;
		}
		return null;
	}

}
