package xstampp.ui.editors.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.editors.interfaces.ITextEditor;

/**
 * this Handler changes the current Styöe of the text which is either written or
 * selected in a text editor this is done by calling
 * {@link ITextEditor#setStyle(String)} with the value of the parameter
 * <code>xstampp.commandParameter.baseline</Code>
 * <p>
 * TODO this is not the normal increase/decrease Baseline function since it sets
 * the whole selection to one level <br>
 * instead of setting the baseline
 * 
 * @author Lukas Balzer
 * 
 * @see ITextEditor
 * @since 1.0
 */
public class TextBaselineHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    String baselineHandle = event.getParameter("xstampp.commandParameter.baseline"); //$NON-NLS-1$
    if (activeEditor instanceof ITextEditor) {
      ((ITextEditor) activeEditor).setFontSize(baselineHandle, 0);
    }
    return null;
  }

}
