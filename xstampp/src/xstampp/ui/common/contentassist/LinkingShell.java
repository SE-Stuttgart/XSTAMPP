/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.ui.common.contentassist;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

class LinkingShell {

  private LinkProposal[] proposals;
  private LinkProposal[] currentContent;
  private IContentProposalListener listener;
  private Point mouseLoc;
  private Shell shell;
  private Point labelShellSize;
  private Point descShellSize;
  private int shellsOffset = 10;
  private java.util.List<Label> descChildren;

  public LinkingShell() {
    this.mouseLoc = Display.getDefault().getCursorLocation();
    descShellSize = new Point(300, 300);
    labelShellSize = new Point(200, 300);
    descChildren = new ArrayList<>();
  }

  public void setNextProposal(LinkProposal[] proposals) {
    this.proposals = proposals;
  }

  public void setMousePosition(Point mouseLoc) {
    this.mouseLoc = mouseLoc;
  }

  /**
   * this method constructs the linking shell if there are proposals available and promts an error
   * when there are no proposals available. It also adds a listener that closes the shell if it's
   * not longer the active window. It closes any existing linking shells constructed with this
   * instance.
   * 
   * @param control
   *          the {@link Control} used for calculating the optimal position of the two proposal
   *          {@link Shell}'s
   */
  public void createControl(Control control) {
    if (this.shell != null && !this.shell.isDisposed()) {
      this.shell.close();
    }
    
    if (mouseLoc == null) {
      Point location = Display.getDefault().getCursorLocation();
      setMousePosition(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
          .toControl(location));
    }
    if (this.proposals.length == 0) {
      MessageDialog.openError(null, "No entrys available",
          "There are no entrys available for linking!");
      return;
    }
    this.shell = new Shell(SWT.RESIZE);

    this.shell.setLayout(new GridLayout());
    this.shell.setSize(labelShellSize);

    // calculate the correct position of the shell, so that it's not displayed beyond the
    // display bounds
    Point shellLocation = control.toDisplay(mouseLoc.x, mouseLoc.y);

    if (Display.getDefault().getBounds().width - (labelShellSize.x + shellLocation.x) < 0) {
      shellLocation.x = shellLocation.x - labelShellSize.x;
    }
    if (Display.getDefault().getBounds().height - (labelShellSize.y + shellLocation.y) < 0) {
      shellLocation.y = shellLocation.y - labelShellSize.y;
    }

    this.shell.setLocation(shellLocation);

    final Shell descShell = new Shell(SWT.RESIZE);
    descShell.setLayout(new FormLayout());
    descShell.setSize(descShellSize);
    // calculate the correct position of the shell, so that it's not displayed beyond the
    // display bounds
    Point descShellLocation = new Point(
        shell.getBounds().x + shell.getBounds().width + shellsOffset,
        shell.getBounds().y);
    if (Display.getDefault().getBounds().width - (descShellLocation.x + descShellSize.x) < 0) {
      descShellLocation.x = shell.getBounds().x - descShellSize.x - shellsOffset;
    }
    descShell.setLocation(descShellLocation);
    descShell.setBackground(new Color(null, 231, 240, 228));

    this.shell.addShellListener(new ShellAdapter() {
      @Override
      public void shellDeactivated(ShellEvent error) {
        if (!descShell.isFocusControl()) {
          LinkingShell.this.shell.close();
          descShell.close();
        }
      }

    });

    final Text searchField = new Text(shell, SWT.SINGLE | SWT.SEARCH);
    searchField.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
    searchField.setMessage("search..");

    final List proposalList = new List(shell,
        SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER_SOLID);
    proposalList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    int selected = 0;
    for (int i = 0; i < this.proposals.length; i++) {
      if (this.proposals[i].isSelected()) {
        selected = i;
      }
    }
    proposalList.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        if (proposalList.getSelectionIndex() >= 0
            && proposalList.getSelectionIndex() < LinkingShell.this.proposals.length) {
          for (int i = 0; i < descChildren.size(); i++) {
            descChildren.get(i).dispose();
          }
          Label label = null;
          if (LinkingShell.this.proposals[proposalList.getSelectionIndex()]
              .getDescription() != null) {
            for (String s : LinkingShell.this.proposals[proposalList.getSelectionIndex()]
                .getDescription().split("\n")) {
              FormData data = new FormData();
              data.top = new FormAttachment(label);
              data.left = new FormAttachment(0);
              data.right = new FormAttachment(100);
              label = new Label(descShell, SWT.WRAP | SWT.SHADOW_OUT);
              label.setBackground(new Color(null, 231, 240, 228));
              if (s.contains("\t")) {
                s = s.replace('\t', ' ').trim();
                data.left = new FormAttachment(10);
              }
              label.setText(s);
              label.setLayoutData(data);
              descChildren.add(label);
            }
            descShell.layout();
          }
        }
      }
    });
    proposalList.addMouseTrackListener(new MouseTrackAdapter() {

      @Override
      public void mouseHover(MouseEvent e) {
        if (proposalList.getSelectionIndex() >= 0
            && proposalList.getSelectionIndex() < LinkingShell.this.proposals.length) {
          proposalList.setToolTipText(
              LinkingShell.this.proposals[proposalList.getSelectionIndex()].getLabel());
        }
      }
    });

    proposalList.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseDoubleClick(MouseEvent e) {
        if (currentContent != null) {

          notifyListener(LinkingShell.this.currentContent[proposalList.getSelectionIndex()]);
          LinkingShell.this.shell.close();
          descShell.close();
        }
      }
    });

    searchField.addModifyListener(new ModifyListener() {

      @Override
      public void modifyText(ModifyEvent e) {
        ArrayList<String> entryLabels = new ArrayList<>();
        ArrayList<LinkProposal> proposalEntries = new ArrayList<>();
        for (int i = 0; i < proposals.length; i++) {
          if (proposals[i].getLabel().toLowerCase().contains(searchField.getText().toLowerCase())) {
            entryLabels.add(proposals[i].getLabel());
            proposalEntries.add(proposals[i]);
          }
        }
        currentContent = proposalEntries.toArray(new LinkProposal[0]);
        proposalList.setItems(entryLabels.toArray(new String[0]));

      }
    });
    searchField.setText(""); //$NON-NLS-1$
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
    proposalList.forceFocus();
    proposalList.setSelection(selected);
    proposalList.notifyListeners(SWT.Selection, null);
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