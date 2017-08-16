/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstpasec.ui;

import java.util.Collections;
import java.util.List;
import java.util.Observable;

import messages.Messages;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.ui.sds.AbstractFilteredTableView;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.ObserverValue;

public class RefinedSafetyConstraintsView extends AbstractFilteredTableView {

  public RefinedSafetyConstraintsView() {
    super(new RefinedEntryFilter(), new String[] { Messages.ID, "Refined Unsecure Control Actions",
        Messages.ID, "Refined Security Constraints" });

    setColumnWeights(new int[] { -1, 5, -1, 5 });
  }

  @Override
  protected List<?> getInput() {
    if (getDataInterface() == null) {
      return null;
    }
    List<AbstractLTLProvider> allRUCA = ((IExtendedDataModel) getDataInterface())
        .getAllScenarios(true, false, false);
    Collections.sort(allRUCA);

    return allRUCA;
  }

  @Override
  public String getTitle() {
    return "Refined Security Constraints";
  }

  @Override
  public void update(Observable IExtendedDataModel, Object updatedValue) {
    ObserverValue type = (ObserverValue) updatedValue;
    switch (type) {
    case Extended_DATA:
      packColumns();
    default:
      break;
    }
  }

  @Override
  protected CSCLabelProvider getColumnProvider(int columnIndex) {

    switch (columnIndex) {
    case 0:
      return new CSCLabelProvider() {
        @Override
        public String getText(Object element) {
          return "RSR1." + ((AbstractLTLProvider) element).getNumber();
        }
      };
    case 1:
      return new CSCLabelProvider() {

        @Override
        public String getText(Object element) {
          return ((AbstractLTLProvider) element).getRefinedUCA();
        }
      };
    case 2:
      return new CSCLabelProvider() {
        @Override
        public String getText(Object element) {
          return "SC2." + ((AbstractLTLProvider) element).getNumber();
        }
      };
    case 3:
      return new CSCLabelProvider() {
        @Override
        public String getText(Object element) {
          return ((AbstractLTLProvider) element).getRefinedSafetyConstraint();
        }
      };

    }
    return null;

  }

  @Override
  public String getId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected boolean hasEditSupport() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected boolean canEdit(Object element) {
    // TODO Auto-generated method stub
    return true;
  }

}
