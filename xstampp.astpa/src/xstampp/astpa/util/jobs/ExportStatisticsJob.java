package xstampp.astpa.util.jobs;

import java.util.Observable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

import xstampp.astpa.model.DataModelController;
import xstampp.util.XstamppJob;

public class ExportStatisticsJob extends XstamppJob {

  private DataModelController controller;

  public ExportStatisticsJob(String name, DataModelController controller ) {
    super(name);
    this.controller = controller;
  }

  @Override
  protected Observable getModelObserver() {
    return controller;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
    
    return null;
  }

}
