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

package xstampp.ui.common.grid;

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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class LinkingShell {

  private Control control;
  private String[] literals;
  private String[] labels;
  private String[] descriptions;
  private IContentProposalListener listener;
  private Point mouseLoc;
  private Shell shell;

  public LinkingShell() {
    this.mouseLoc = new Point(0, 0);
  }

  public void setNextProposal(Control control, String[] literals, String[] labels, String[] descriptions) {
    this.control = control;
    this.literals = literals;
    this.labels = labels;
    this.descriptions = descriptions;
  }

  public void setMousePosition(Point mouseLoc) {
    this.mouseLoc = mouseLoc;
  }

  public void createControl() {
    if (this.shell != null && !this.shell.isDisposed()) {
      this.shell.close();
    }

    if (this.labels.length == 0) {
      MessageDialog.openError(null, "No entrys available", "There are no entrys available for linking!");
      return;
    }
    this.shell = new Shell(SWT.NO_TRIM | SWT.CLOSE | SWT.MIN);
    this.shell.setLayout(new FillLayout());
    this.shell.addShellListener(new ShellAdapter() {
      @Override
      public void shellDeactivated(ShellEvent e) {
        LinkingShell.this.shell.close();
      }

    });

    Composite parent = new Composite(this.shell, SWT.BORDER);
    parent.setLayout(new FormLayout());
    this.shell.setLocation(this.mouseLoc.x, this.mouseLoc.y);
    FormData data = new FormData();
    data.left = new FormAttachment(null, 5);
    data.bottom = new FormAttachment(95);
    data.top = new FormAttachment(5);
    final List proposalList = new List(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.WRAP);
    String[] entrys = new String[this.labels.length];
    for (int i = 0; i < this.labels.length; i++) {
      entrys[i] = this.literals[i] + " - " + this.labels[i];
    }
    proposalList.setItems(entrys);

    proposalList.setLayoutData(data);
    final Label description = new Label(parent, SWT.WRAP);
    data = new FormData(300, 300);
    data.left = new FormAttachment(proposalList, 5);
    data.bottom = new FormAttachment(95);
    data.top = new FormAttachment(5);
    data.right = new FormAttachment(95);
    description.setLayoutData(data);
    this.shell.setSize(parent.computeSize(-1, 300));

    proposalList.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        if (proposalList.getSelectionIndex() >= 0
            && proposalList.getSelectionIndex() < LinkingShell.this.descriptions.length) {
          description.setText(LinkingShell.this.descriptions[proposalList.getSelectionIndex()]);
        }
      }
    });
    proposalList.addMouseTrackListener(new MouseTrackAdapter() {

      @Override
      public void mouseHover(MouseEvent e) {
        if (proposalList.getSelectionIndex() >= 0
            && proposalList.getSelectionIndex() < LinkingShell.this.descriptions.length) {
          proposalList.setToolTipText(LinkingShell.this.labels[proposalList.getSelectionIndex()]);
        }
      }
    });

    proposalList.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseDoubleClick(MouseEvent e) {
        if (proposalList.getSelectionIndex() >= 0
            && proposalList.getSelectionIndex() < LinkingShell.this.descriptions.length) {
          IContentProposal prop = new IContentProposal() {

            @Override
            public String getLabel() {
              return getContent() + " - " + LinkingShell.this.labels[proposalList.getSelectionIndex()];
            }

            @Override
            public String getDescription() {
              return LinkingShell.this.descriptions[proposalList.getSelectionIndex()];
            }

            @Override
            public int getCursorPosition() {
              return 0;
            }

            @Override
            public String getContent() {
              return LinkingShell.this.literals[proposalList.getSelectionIndex()];
            }
          };
          notifyListener(prop);
          LinkingShell.this.shell.close();
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
        }

      }

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

      }
    });
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