/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.ui.common.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.swt.graphics.Point;

/**
 * A custom autocomplete textfield adapter
 * 
 * @author Benedikt Markt
 * 
 */
public class AutoCompleteField {

  private ContentProposalProvider contentProposalProvider;
  private LinkingCommandAdapter contentProposalAdapter;
  private List<IContentProposalListener> listeners;
  private static LinkingShell shell = new LinkingShell();

  /**
   * 
   * @author Benedikt Markt
   * 
   * @param control
   *          The control to apply autocomplete to
   * @param controlContentAdapter
   *          IContentAdapter
   * @param literals
   *          the strings to link
   * @param labels
   *          short descriptive strings for each item
   * @param descriptions
   *          long descriptions for eacht item
   */
  public AutoCompleteField(LinkProposal[] proposals) {
    this.listeners = new ArrayList<>();
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
   * This function calculates a position by merging two Point and sets it as the
   * popups Position
   * 
   * @author Lukas Balzer
   * 
   * @param relativeMouse
   *          the position of the mouse relative to the active cell
   * @param cellPosition
   *          the position of the cell relative to the parent grid
   * @param topOffset
   *          an offset which can be set to move the dialog relative to the
   *          mouse
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
   * Opens the proposal popup immediately
   * 
   * @author Benedikt Markt
   * 
   */
  public void openPopup() {
//    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
      shell.createControl();
//    } else {
//      this.contentProposalAdapter.openProposalPopup();
//    }
  }

  public void openShell() {
    shell.createControl();
  }
  /**
   * Closes the proposal popup immediately
   * 
   * @author Benedikt Markt
   * 
   */
  public void closePopup() {
//    this.contentProposalAdapter.closeProposalPopup();
//    this.contentProposalAdapter.getControl().dispose();
//    for (IContentProposalListener proposalListener : listeners) {
//      this.contentProposalAdapter.removeContentProposalListener(proposalListener);
//    }
  }

  public void setProposalListener(IContentProposalListener listener) {
    listeners.add(listener);
    shell.setProposalListener(listener);
//    getContentProposalAdapter().addContentProposalListener(listener);
  }
}
