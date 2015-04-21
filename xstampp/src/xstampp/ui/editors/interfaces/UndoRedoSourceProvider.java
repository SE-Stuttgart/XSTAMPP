package xstampp.ui.editors.interfaces;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.ui.AbstractSourceProvider;

/**
 * a class for managing undo redo content
 * @author Lukas Balzer
 * @since 1.0
 */
public class UndoRedoSourceProvider extends AbstractSourceProvider implements CommandStackListener{

	private static final String CAN_UNDO="xstampp.handler.canUndo";
	private static final String CAN_REDO="xstampp.handler.canRedo";
	private Map<String,Boolean> stringToBool;
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public UndoRedoSourceProvider() {
		super();
		this.stringToBool= new HashMap<>();
		this.stringToBool.put(CAN_REDO, false);
		this.stringToBool.put(CAN_UNDO, false);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return this.stringToBool.keySet().toArray(new String[0]);
	}

	/**
	 * adds this object as stack listener to the given stack
	 * @author Lukas Balzer
	 *
	 * @param commandStack the stack which want to listen
	 */
	public void registerStack(CommandStack commandStack){
		commandStack.addCommandStackListener(this);
		
	}
	@Override
	public void commandStackChanged(EventObject event) {
		
		//TODO
	}

}
