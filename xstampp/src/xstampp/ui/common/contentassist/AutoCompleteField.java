/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.ui.common.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

/**
 * A custom autocomplete textfield adapter.
 * 
 * @author Benedikt Markt
 * 
 */
public class AutoCompleteField {

  private ContentProposalProvider contentProposalProvider;
  private LinkingCommandAdapter contentProposalAdapter;
  private List<IContentProposalListener> listeners;
  private static LinkingShell shell = new LinkingShell();
  private Control control;

  /**
   * 
   * @author Benedikt Markt
   * @author Lukas Balzer
   * @param proposals
   *          a list of {@link LinkProposal}'s that will be displayed in the linking shell
   * @param control
   *          the {@link Control} that is used to determine the position of the linking shells
   */
  public AutoCompleteField(LinkProposal[] proposals, Control control) {
    this.control = control;
    this.listeners = new ArrayList<>();
    shell.setMousePosition(null);
    shell.setNextProposal(proposals);

  }

  /**
   * 
   * @author Benedikt Markt
   * 
   * @param labels
   *          a lost of labels
   */
  public void setLabels(final String[] labels) {
    this.contentProposalProvider.setLabels(labels);
  }

  /**
   * 
   * @author Benedikt Markt
   * 
   * @return the content proposal provider
   */
  public ContentProposalProvider getContentProposalProvider() {
    return this.contentProposalProvider;
  }

  /**
   * This function calculates a position by merging two Point and sets it as the popups Position.
   * 
   * @author Lukas Balzer
   * 
   * @param relativeMouse
   *          the position of the mouse relative to the active cell
   */
  public void setPopupPosition(Point relativeMouse) {
    shell.setMousePosition(relativeMouse);
  }

  /**
   * 
   * @author Benedikt Markt
   * 
   * @return the content proposal adapter
   */
  public ContentProposalAdapter getContentProposalAdapter() {
    return this.contentProposalAdapter;
  }

  /**
   * Opens the proposal popup immediately.
   * 
   * @author Benedikt Markt
   * 
   */
  public void openPopup() {
    shell.createControl(control, false);
  }

  public void openShell() {
    shell.createControl(control, false);
  }
  public void openShell(boolean hideDescription) {
    shell.createControl(control,hideDescription);
  }

  /**
   * Closes the proposal popup immediately.
   * 
   * @author Benedikt Markt
   * 
   */
  public void closePopup() {
    // close completely handled by the popup itself
  }

  /**
   * sets the listener of the content assist popup.
   * 
   * @param listener
   *          a proposal listener that is called in the case that a proposal is accepted.
   */
  public void setProposalListener(IContentProposalListener listener) {
    listeners.add(listener);
    shell.setProposalListener(listener);
  }
}
