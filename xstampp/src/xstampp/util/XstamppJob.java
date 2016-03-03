package xstampp.util;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

public abstract class XstamppJob extends Job implements Observer, IJobChangeListener {


	public XstamppJob(String name) {
		super(name);
		addJobChangeListener(this);
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		if(arg.equals(ObserverValue.CLEAN_UP) ||arg.equals(ObserverValue.DELETE)){
			this.cancel();
		}
	}
	
	@Override
	public void aboutToRun(IJobChangeEvent event) {
		//do nothing
	}

	@Override
	public void awake(IJobChangeEvent event) {
		// do nothing
	}

	@Override
	public void done(IJobChangeEvent event) {
		if(getModelObserver() != null){
			getModelObserver().deleteObserver(this);
		}
	}

	@Override
	public void running(IJobChangeEvent event) {
		//this listener needs only to handle the scheduled() and the done() methode
	}

	@Override
	public void scheduled(IJobChangeEvent event) {
		STPAPluginUtils.listJob(event.getJob());
		if(getModelObserver() != null){
			getModelObserver().addObserver(this);
		}
	}

	@Override
	public void sleeping(IJobChangeEvent event) {
		//this listener needs only to handle the scheduled() and the done() methode
	}

	protected abstract Observable getModelObserver();
	

}
