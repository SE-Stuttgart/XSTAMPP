/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.ui.menu.file.commands;

import java.util.Dictionary;

import messages.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.Bundle;

import xstampp.Activator;

/**
 * Handler for displaying the about dialog
 * 
 * @author Jarkko Heidenwag
 * 
 */
public class AboutHandler extends AbstractHandler {

  /**
   * The A-STPA logo to be displayed on the left of the dialog.
   */
  private static final Image DIALOG_IMAGE = Activator.getImageDescriptor("icons/Logo.png").createImage(); //$NON-NLS-1$
  /**
   * The string displayed on the bar of the dialog.
   */
  private static final String DIALOG_TITLE = Messages.About + Messages.PlatformName;
  /**
   * The type of the dialog. Not used in our case.
   */
  private static final int NONE = 0;
  /**
   * Label for the only button on the dialogue.
   */
  private static final String[] DIALOG_BUTTONS_LABELS = new String[] { IDialogConstants.OK_LABEL };
  /**
   * The index for the selected button by default.
   */
  private static final int DEFAULT_INDEX = 0;
  /**
   * First line displayed in the text dialogue area.
   */
  private static final String DIALOG_MESSAGE_TITLE = "\n" + Messages.PlatformName; //$NON-NLS-1$
  /**
   * The copyright line
   */
  private static final String COPYRIGHT_LINE = Messages.StuPro;

  /**
   * Custom dialogue displaying information about the console version and build.
   */
  class AboutDialog extends MessageDialog {

    /**
     * Constructor for the custom dialog.
     * 
     * @param parentShell
     *          the shell containing the dialog.
     * @param dialogTitle
     *          title displayed in bar of the dialog.
     * @param dialogTitleImage
     *          image to display in the bar of the dialogue. None is used.
     * @param dialogMessage
     *          The message to be displayed in the dialogue area. the version
     *          and build numbers plus the copyright notice.
     * @param dialogImageType
     *          no used.
     * @param dialogButtonLabels
     *          only the OK button is displayed.
     * @param defaultIndex
     *          only one button is used on this dialog so it must be 0.
     */
    public AboutDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
        int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
      super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels,
          defaultIndex);

    }

    /**
     * Overridden to return the company logo.
     * 
     * @return NSPA logo.
     */
    @Override
    public Image getImage() {
      return AboutHandler.DIALOG_IMAGE;
    }

  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    Shell parentShell = HandlerUtil.getActiveShell(event);
    String dialogMessage = this.getDialogMessage();
    AboutDialog dialog = new AboutDialog(parentShell, AboutHandler.DIALOG_TITLE, AboutHandler.DIALOG_IMAGE,
        dialogMessage, AboutHandler.NONE, AboutHandler.DIALOG_BUTTONS_LABELS, AboutHandler.DEFAULT_INDEX);

    dialog.open();
    return null;
  }

  /**
   * Computes the line to be displayed in the text area. This label contains the
   * number of the product, the version, the build and a copyright notice.
   * 
   * @return the text in the dialog
   */
  private String getDialogMessage() {

    Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
    Dictionary<?, ?> dictionary = bundle.getHeaders();
    String visitLine = Messages.AboutKontakt + ": " + "https://sourceforge.net/projects/astpa/";  //$NON-NLS-1$//$NON-NLS-2$
    String contactPerson = "Contact Person : Asim Abdulkhaleq \n Email: Asim.Abdulkhaleq@informatik.uni-stuttgart.de"; //$NON-NLS-1$
    String versionLine = this.getVersionLine((String) dictionary.get(Messages.BundleVersion));
    String team = Messages.DevelopmentTeam + "\t  " + "Aleksander Zotov, Aliaksei Babkovich, Benedikt Markt," //$NON-NLS-1$//$NON-NLS-2$
        + "\n\t\t  " + "Fabian Toth, Jarkko Heidenwag, Jaqueline Patzek, Adam " //$NON-NLS-1$ //$NON-NLS-2$
        + "\n\t\t  " + "Grahovac, Lukas Balzer, Patrick Wickenhäuser, Sebastian Sieber"; //$NON-NLS-1$ //$NON-NLS-2$
    return AboutHandler.DIALOG_MESSAGE_TITLE + "\n" + versionLine + "\n\n" + AboutHandler.COPYRIGHT_LINE //$NON-NLS-1$ //$NON-NLS-2$
        + "\n\n" + team + "\n\n" + visitLine + "\n\n" + contactPerson; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  /**
   * Extract the version part of the bundle version.
   * 
   * @param bundleVersion
   *          the version of the bundle in the format v.m.n.qualifier
   * @return the version part, i.e. "v.m.n"
   */
  private String getVersionLine(String bundleVersion) {
    return Messages.Version + bundleVersion;
  }
}
