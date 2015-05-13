package xstampp.astpa.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.ui.systemdescription.SystemDescriptionView;

public class DefaultKeyNavigation implements KeyListener {

	private Text control;

	public DefaultKeyNavigation(Text control) {
			this.control= control;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.keyCode){
		case SWT.ARROW_LEFT:{
			this.control.setSelection(
					this.control.getSelection().x -1, 
					this.control.getSelection().x-1);
			break;
		}
		case SWT.ARROW_RIGHT:{
			this.control.setSelection(
					this.control.getSelection().x +1,
					this.control.getSelection().x+1);
		}
		}
	}

}
