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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;

import xstampp.ui.common.ProjectManager;

/**
 * this class provides useful static methods for the interaction with the plugin
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 * 
 */
public final class STPAPluginUtils {

  private static List<Job> unfinishedJobs;

  private STPAPluginUtils() {
    unfinishedJobs = new ArrayList<>();
  }

  /**
   * This static method adds the given {@link Job} to the list of jobs that need to be executed
   * before the application can be closed.
   * The method also calls {@link Job#schedule()} to prevent that the job is listed without beeing
   * executed.
   * 
   * @param job
   *          a {@link Job} that is be added to the list of jobs.
   */
  public static void listJob(Job job) {
    if (unfinishedJobs == null) {
      unfinishedJobs = new ArrayList<>();
    }
    unfinishedJobs.add(job);
    job.addJobChangeListener(new JobChangeAdapter() {

      @Override
      public void done(IJobChangeEvent event) {
        unfinishedJobs.remove(event.getJob());
      }
    });
    job.schedule();
  }

  /**
   * 
   * @return all {@link Job}'s that where added using {@link STPAPluginUtils#listJob(Job)} and are
   *         still running.
   */
  public static List<Job> getUnfinishedJobs() {
    if (unfinishedJobs == null) {
      return new ArrayList<>();
    }
    return unfinishedJobs;
  }

  /**
   * Executes a registered command without command values
   * 
   * @author Lukas Balzer
   * 
   * @param commandId
   *          the id under which the command is registered in the plugin
   * @return the command return value or null if non/ the command was not
   *         executed
   */
  public static Object executeCommand(String commandId) {
    if (commandId == null) {
      return false;
    }
    IServiceLocator serviceLocator = PlatformUI.getWorkbench();
    ICommandService commandService = (ICommandService) serviceLocator
        .getService(ICommandService.class);
    Command command = commandService.getCommand(commandId);
    if (command != null) {
      try {
        return command.executeWithChecks(new ExecutionEvent());
      } catch (ExecutionException | NotDefinedException | NullPointerException
          | NotHandledException e) {
        ProjectManager.getLOGGER().error("Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
      } catch (NotEnabledException exc) {
        ProjectManager.getLOGGER().debug(exc.getMessage());
      }
    } else {
      ProjectManager.getLOGGER().error("Command " + commandId + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return null;
  }

  /**
   * Executes a registered command with command values
   * 
   * @author Lukas Balzer
   * 
   * @param commandId
   *          the id under which the command is registered in the plugin
   * @param params
   *          a map containing values like <code> 'ParameterName,value' </code>
   * @return the command return value or null if non/ the command was not
   *         executed
   */
  public static Object executeParaCommand(String commandId, Map<String, String> params) {
    IServiceLocator serviceLocator = PlatformUI.getWorkbench();
    ICommandService commandService = (ICommandService) serviceLocator
        .getService(ICommandService.class);
    IHandlerService handlerService = (IHandlerService) serviceLocator
        .getService(IHandlerService.class);
    Command command = commandService.getCommand(commandId);
    if (command == null) {
      ProjectManager.getLOGGER().debug(commandId + " is no valid command id");
      return false;
    }
    ParameterizedCommand paraCommand = ParameterizedCommand.generateCommand(command, params);
    if (paraCommand == null) {
      ProjectManager.getLOGGER()
          .debug("One of: " + params.toString() + " is no valid parameter id");
      return false;
    }
    try {
      return handlerService.executeCommand(paraCommand, null);

    } catch (ExecutionException | NotDefinedException | NotEnabledException
        | NotHandledException e) {
      Logger.getRootLogger().error("Command " + commandId + " does not exist", e); //$NON-NLS-1$ //$NON-NLS-2$
    }

    return true;
  }

  private static void OpenInMacFileBrowser(String path) {
    Boolean openInsidesOfFolder = false;
    // try mac
    String macPath = path.replace("\\", "/"); // mac finder doesn't like
                                              // backward slashes
    File pathfile = new File(macPath);
    // if path requested is a folder, automatically open insides of that folder
    if (pathfile.isDirectory()) {
      openInsidesOfFolder = true;
    }

    if (!macPath.startsWith("\"")) {
      macPath = "\"" + macPath;
    }
    if (!macPath.endsWith("\"")) {
      macPath = macPath + "\"";
    }
    // during the process of adding backslashes it is possible that quotes are
    // added to the path
    // those are removed with this command
    macPath = macPath.replace('"', ' ').trim();
    String arguments = (openInsidesOfFolder ? "" : "-R ") + macPath;
    try {
      Runtime.getRuntime().exec("open " + arguments);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void OpenInLinuxFileBrowser(String path) {
    // try mac
    String linuxPath = path.replace("\\", "/"); // mac finder doesn't like
                                                // backward slashes
    File pathfile = new File(linuxPath);

    if (!linuxPath.startsWith("\"")) {
      linuxPath = "\"" + linuxPath;
    }
    if (!linuxPath.endsWith("\"")) {
      linuxPath = linuxPath + "\"";
    }
    // during the process of adding backslashes it is possible that quotes are
    // added to the path
    // those are removed with this command
    linuxPath = linuxPath.replace('"', ' ').trim();
    try {
      Runtime.getRuntime().exec("gnome-open" + linuxPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void OpenInWinFileBrowser(String path) {
    Boolean openInsidesOfFolder = false;

    // try windows
    String winPath = path.replace("/", "\\"); // windows explorer doesn't like
                                              // forward slashes
    File pathfile = new File(winPath);
    // if path requested is a folder, automatically open insides of that folder
    if (pathfile.isDirectory()) {
      openInsidesOfFolder = true;
    }
    try {
      Runtime.getRuntime()
          .exec("explorer.exe " + (openInsidesOfFolder ? "/root," : "/select,") + winPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void OpenInFileBrowser(String path) {
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.startsWith("win")) {
      OpenInWinFileBrowser(path);
    } else if (osName.startsWith("mac")) {
      OpenInMacFileBrowser(path);
    } else if (osName.startsWith("linux")) {
      OpenInMacFileBrowser(path);
    }
  }
}
