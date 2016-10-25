package xstampp.ui.editors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.interfaces.ITextEditor;

/**
 * this Handler changes the current Style of the text which is either written or
 * selected in a text editor this is done by calling
 * {@link ITextEditor#setStyle(String)} with the value of the parameter
 * <code>xstampp.commandParameter.style</Code>
 * 
 * @author Lukas Balzer
 * 
 * @see ITextEditor
 * @since 1.0
 */
public class StyleHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    if (activeEditor instanceof ITextEditor) {
      String parameterStyle = event.getParameter("xstampp.commandParameter.style"); //$NON-NLS-1$
      ((ITextEditor) activeEditor).setStyle(parameterStyle);
    }
    return null;
  }

}
