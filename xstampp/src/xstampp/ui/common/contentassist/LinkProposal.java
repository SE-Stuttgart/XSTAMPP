/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.common.contentassist;

import java.util.UUID;

import org.eclipse.jface.fieldassist.IContentProposal;

public class LinkProposal implements IContentProposal {
  private String label;
  private String description;
  private UUID proposalId;
  private boolean selected = false;

  @Override
  public String getContent() {
    return description;
  }

  @Override
  public int getCursorPosition() {
    // TODO Auto-generated method stub
    return 0;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public String getLabel() {
    return label;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public void setProposalId(UUID proposalId) {
    this.proposalId = proposalId;
  }

  public UUID getProposalId() {
    return proposalId;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

}
