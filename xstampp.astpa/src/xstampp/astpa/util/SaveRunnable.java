package xstampp.astpa.util;

import org.eclipse.jface.dialogs.MessageDialog;

import xstampp.astpa.util.jobs.SaveJob;

public class SaveRunnable implements Runnable {

	private SaveJob job;
	public SaveRunnable(SaveJob job) {
		this.job = job; 
	}
	@Override
	public void run() {
		
		boolean comp = MessageDialog.openQuestion(null, "Store Preferences", "Do you want to store "
				+ "the STPA file in compabillity mode?\n(Needed for usage with older versions, special characters are maybe"
				+ " not saved correctly!)");
		this.job.setCompabillityMode(comp);
	}

}
