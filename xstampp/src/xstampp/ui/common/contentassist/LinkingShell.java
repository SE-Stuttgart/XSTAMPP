/*******************************************************************************
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class LinkingShell {

  private LinkProposal[] proposals;
  private IContentProposalListener listener;
  private Point mouseLoc;
  private Shell shell;
  private Point labelShellSize;
  private Point descShellSize;
  private int shellsOffset = 10;
  public LinkingShell() {
    this.mouseLoc = new Point(0, 0);
    descShellSize = new Point(300, 300);
    labelShellSize = new Point(200, 300);
  }

  /**
   * 
   * @param proposalIds the UUID's of the proposal entries if available 
   *                      this can be used to identify an entry
   * @param literals the literals e.g. the ids of the proposed entries
   * @param labels the titles or short descriptions of the proposals
   * @param descriptions the descriptions, e.g. the case description
   */
  public void setNextProposal(LinkProposal[] proposals) {
    this.proposals = proposals;
  }

  public void setMousePosition(Point mouseLoc) {
    this.mouseLoc = mouseLoc;
  }

  /**
   * this method constructs the linking shell if there are proposals available and promts an
   * error when there are no proposals available.
   * It also adds a listener that closes the shell if it's not longer the active window.
   * It closes any existing linking shells constructed with this instance.
   * @param control TODO
   */
  public void createControl(Control control) {
    if (this.shell != null && !this.shell.isDisposed()) {
      this.shell.close();
    }

    if (this.proposals.length == 0) {
      MessageDialog.openError(null,
                  "No entrys available", "There are no entrys available for linking!");
      return;
    }
    this.shell = new Shell( SWT.RESIZE );
    
    this.shell.setLayout(new FillLayout());
    this.shell.setSize(labelShellSize);
    //calculate the correct position of the shell, so that it's not displayed beyond the 
    //display bounds
    Point shellLocation = new Point(mouseLoc.x, mouseLoc.y);
    if ( control.getBounds().width - (labelShellSize.x + mouseLoc.x) < 0 ) {
      shellLocation.x = mouseLoc.x - labelShellSize.x;
    }
    if ( control.getBounds().height - (labelShellSize.y + mouseLoc.y) < 0 ) {
      shellLocation.y = mouseLoc.y - labelShellSize.y;
    }
    this.shell.setLocation(shellLocation);
    final Shell descShell = new Shell( SWT.RESIZE );
    descShell.setLayout(new FillLayout());
    descShell.setSize(descShellSize);
    //calculate the correct position of the shell, so that it's not displayed beyond the 
    //display bounds
    shellLocation = new Point(shell.getBounds().x + shell.getBounds().width + shellsOffset,
                              shell.getBounds().y);
    if ( control.getBounds().width - (shellLocation.x + descShellSize.x) < 0 ) {
      shellLocation.x = shell.getBounds().x - descShellSize.x - shellsOffset;
    }
    descShell.setLocation(shellLocation);
    

    this.shell.setLocation(control.toDisplay(shell.getLocation().x, shell.getLocation().y));
    descShell.setLocation(control.toDisplay(descShell.getLocation().x, descShell.getLocation().y));
    this.shell.addShellListener(new ShellAdapter() {
      @Override
      public void shellDeactivated(ShellEvent error) {
        if ( !descShell.isFocusControl() ) {
          LinkingShell.this.shell.close();
          descShell.close();
        }
      }

    });
    
   
    final List proposalList = new List(shell, SWT.SINGLE | SWT.H_SCROLL |SWT.V_SCROLL | SWT.WRAP |SWT.BORDER_SOLID);
    String[] entrys = new String[this.proposals.length];
    for (int i = 0; i < this.proposals.length; i++) {
      entrys[i] = this.proposals[i].getLabel();
    }
    proposalList.setItems(entrys);
    final Label description = new Label(descShell, SWT.WRAP | SWT.SHADOW_OUT);
    description.setBackground(new Color(null, 231, 240, 228));
    proposalList.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        if (proposalList.getSelectionIndex() >= 0
            && proposalList.getSelectionIndex() < LinkingShell.this.proposals.length) {
          description.setText(LinkingShell.this.proposals[proposalList.getSelectionIndex()].getDescription());
        }
      }
    });
    proposalList.addMouseTrackListener(new MouseTrackAdapter() {

      @Override
      public void mouseHover(MouseEvent e) {
        if (proposalList.getSelectionIndex() >= 0
            && proposalList.getSelectionIndex() < LinkingShell.this.proposals.length) {
          proposalList.setToolTipText(LinkingShell.this.proposals[proposalList.getSelectionIndex()].getLabel());
        }
      }
    });

    proposalList.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseDoubleClick(MouseEvent e) {
        if (proposalList.getSelectionIndex() >= 0
            && proposalList.getSelectionIndex() < LinkingShell.this.proposals.length) {
          
          notifyListener(LinkingShell.this.proposals[proposalList.getSelectionIndex()]);
          LinkingShell.this.shell.close();
          descShell.close();
        }
      }
    });

    proposalList.addKeyListener(new KeyListener() {

      @Override
      public void keyReleased(KeyEvent e) {
        if (e.keyCode == SWT.CR) {
          proposalList.notifyListeners(SWT.MouseDoubleClick, null);
        }
        if (e.keyCode == SWT.ESC) {
          LinkingShell.this.shell.close();
          descShell.close();
        }

      }

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

      }
    });
    descShell.open();
    this.shell.open();
  }

  public void setProposalListener(IContentProposalListener listener) {
    this.listener = listener;
  }

  private void notifyListener(IContentProposal prop) {
    if (this.listener != null) {
      this.listener.proposalAccepted(prop);
    }
  }
}