/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;

import xstampp.Activator;
import xstampp.model.ObserverValue;

public abstract class XstamppJob extends Job implements Observer, IJobChangeListener {
  private Throwable error;
  private List<String> errors;

  public XstamppJob(String name) {
    super(name);
    addJobChangeListener(this);
    this.errors = new ArrayList<>();
  }

  @Override
  public void update(Observable o, Object arg) {
    if (arg.equals(ObserverValue.CLEAN_UP) || arg.equals(ObserverValue.DELETE)) {
      this.cancel();
    }
  }

  @Override
  public void aboutToRun(IJobChangeEvent event) {
    // do nothing
  }

  @Override
  public void awake(IJobChangeEvent event) {
    // do nothing
  }

  @Override
  public void done(IJobChangeEvent event) {
    if (getModelObserver() != null) {
      getModelObserver().deleteObserver(this);
    }
    if (XstamppJob.this.error != null) {
      Display.getDefault().asyncExec(new Runnable() {

        @Override
        public void run() {
          Display.getDefault().beep();
          final String PID = Activator.PLUGIN_ID;
          String errorMsg = "";
          if (!XstamppJob.this.errors.isEmpty()) {
            errorMsg += XstamppJob.this.errors.get(0) + "\n";
          }
          errorMsg += error.getMessage();
          MultiStatus info = new MultiStatus(PID, 1, errorMsg, null);
          for( int i = 1; i < XstamppJob.this.errors.size(); i++) {
            info.add(new Status(IStatus.ERROR, PID, 1, XstamppJob.this.errors.get(i), null));
          }
          for (StackTraceElement traceElement : XstamppJob.this.error.getStackTrace()) {
            info.add(new Status(IStatus.ERROR, PID, 1, "   " + traceElement.toString(), null));
          }
          ErrorDialog.openError(Display.getDefault().getActiveShell(), "Save Failed!",
              getName() + " failed to execute!", info);
        }
      });
    }
  }

  protected void addErrorMsg(String msg) {
    this.errors.add(msg);
  }

  /**
   *
   * @author Lukas Balzer
   *
   * @return a list of errors which occured during the job
   */
  public List<String> getErrors() {
    return this.errors;
  }

  @Override
  public void running(IJobChangeEvent event) {
    // this listener needs only to handle the scheduled() and the done() methode
  }

  @Override
  public void scheduled(IJobChangeEvent event) {
    STPAPluginUtils.listJob(event.getJob());
    if (getModelObserver() != null) {
      getModelObserver().addObserver(this);
    }
  }

  @Override
  public void sleeping(IJobChangeEvent event) {
    // this listener needs only to handle the scheduled() and the done() methode
  }

  /**
   * if this method returns a valid observable than this job will be
   * added/removed as an observer
   * 
   * @return the controller object this job should observe during its runtime
   */
  protected abstract Observable getModelObserver();

  /**
   * @return the error
   */
  public Throwable getError() {
    return this.error;
  }

  /**
   * @param error
   *          the error to set
   */
  public void setError(Throwable error) {
    this.error = error;
  }

}
