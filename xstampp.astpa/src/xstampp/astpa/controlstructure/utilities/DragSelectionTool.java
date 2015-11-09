package xstampp.astpa.controlstructure.utilities;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.gef.tools.PanningSelectionTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

public class DragSelectionTool extends PanningSelectionTool{
	
	@Override
	protected boolean stateTransition(int start, int end) {
		
		return super.stateTransition(start, end);
	}
	
	@Override
	public void keyDown(KeyEvent evt, EditPartViewer viewer) {
		if(getDragTracker() == null &&(evt.keyCode & 1<<24) != 0){
			DragTracker tracker = new DragEditPartsTracker(viewer.getFocusEditPart());
			setDragTracker(tracker);
			setState(STATE_ACCESSIBLE_DRAG_IN_PROGRESS);
		}
		super.keyDown(evt, viewer);
	}
	@Override
	protected boolean acceptArrowKey(KeyEvent e) {
	
	
		return super.acceptArrowKey(e);
	}
	@Override
	protected boolean acceptSpaceBar(KeyEvent e) {
//		if(e.keyCode == SWT.CTRL){
//			return true;
//		}
		return super.acceptSpaceBar(e);
	}
}

