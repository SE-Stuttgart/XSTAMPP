/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick
 * Wickenhäuser, Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.ui.sds;

import java.util.List;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;
import xstampp.astpa.model.BadReferenceModel;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;
import xstampp.astpa.model.interfaces.ISTPADataModel;
import xstampp.astpa.model.interfaces.ISeverityEntry;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.ui.ATableFilter;
import xstampp.model.ObserverValue;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUserProject;

/**
 * 
 * @author Jarkko Heidenwag
 * 
 */
public class CSCView extends AbstractFilteredTableView {

  /**
   * @author Jarkko Heidenwag
   * 
   */
  public static final String ID = "astpa.steps.step2_3"; //$NON-NLS-1$

  /**
   * 
   * @author Jarkko Heidenwag
   * 
   */
  public CSCView() {
    super(new CSCTableFilter(), new String[] { Messages.ID, Messages.UnsafeControlActions,
        Messages.ID, Messages.CorrespondingSafetyConstraints, "Links" });
    setColumnWeights(new int[] { -1, 5, -1, 5, -1 });
    addEditingSupport(3, new EditSupportProvider() {
      @Override
      protected boolean canEdit(Object element) {
        return CSCView.this.canEdit(element);
      }

      @Override
      protected Object getEditingValue(Object element) {
        if (element instanceof ICorrespondingUnsafeControlAction) {
          String text = ((ICorrespondingUnsafeControlAction) element).getCorrespondingSafetyConstraint()
              .getText();
          return text == null ? "" : text;
        }
        return null;
      }

      @Override
      protected void setEditValue(Object element, Object value) {
        if (element instanceof ICorrespondingUnsafeControlAction) {
          getDataInterface().setCorrespondingSafetyConstraint(
              ((ICorrespondingUnsafeControlAction) element).getId(), String.valueOf(value));
        }
      }
    });
    addEditingSupport(1, new EditSupportProvider() {
      @Override
      protected boolean canEdit(Object element) {
        return CSCView.this.canEdit(element);
      }

      @Override
      protected Object getEditingValue(Object element) {
        if (element instanceof ICorrespondingUnsafeControlAction) {
          String text = ((ICorrespondingUnsafeControlAction) element).getDescription();
          return text == null ? "" : text;
        }
        return null;
      }

      @Override
      protected void setEditValue(Object element, Object value) {
        if (element instanceof ICorrespondingUnsafeControlAction) {
          getDataInterface().getControlActionController()
              .setUcaDescription(((ICorrespondingUnsafeControlAction) element).getId(), String.valueOf(value));
        }
      }
    });
  }

  public CSCView(ATableFilter aTableFilter, String[] strings) {
    super(aTableFilter, strings);
  }

  @Override
  public String getId() {
    return CSCView.ID;
  }

  @Override
  public String getTitle() {
    return Messages.CorrespondingSafetyConstraints;
  }

  @Override
  public void update(Observable dataModelController, Object updatedValue) {
    super.update(dataModelController, updatedValue);
    ObserverValue type = (ObserverValue) updatedValue;
    switch (type) {
    case LINKING:
    case UNSAFE_CONTROL_ACTION:
      packColumns();
      break;
    default:
      break;
    }
  }

  @Override
  protected ICorrespondingSafetyConstraintDataModel getDataInterface() {
    return (ICorrespondingSafetyConstraintDataModel) super.getDataInterface();
  }

  protected List<?> getInput() {
    return this.getDataInterface().getAllUnsafeControlActions();
  }

  @Override
  public void dispose() {
    this.getDataInterface().deleteObserver(this);
    super.dispose();
  }

  protected CSCLabelProvider getColumnProvider(int columnIndex) {
    switch (columnIndex) {
    case 0:
      return new CSCLabelProvider() {
        @Override
        public String getText(Object element) {
          return "UCA1." + CSCView.this.getDataInterface()
              .getUCANumber(((ICorrespondingUnsafeControlAction) element).getId());
        }
      };
    case 1:
      return new CSCLabelProvider() {
        @Override
        public String getText(Object element) {
          return ((ICorrespondingUnsafeControlAction) element).getDescription();
        }

        @Override
        public String getToolTipText(Object element) {
          ISeverityEntry entry = (ISeverityEntry) element;
          List<UUID> hazLinkIds = getDataInterface().getLinkController().getLinksFor(LinkingType.UCA_HAZ_LINK,
              entry.getId());
          boolean first = true;
          String links = "";
          for (ITableModel haz : getDataInterface().getHazards(hazLinkIds)) {
            if (first) {
              first = false;
            } else {
              links += ", ";
            }
            links += "H-" + haz.getNumber();
          }
          return "UCA1." + CSCView.this.getDataInterface()
              .getUCANumber(entry.getId()) + " - " + entry.getSeverity().toString() + "\n" + links;
        }
      };
    case 2:
      return new CSCLabelProvider() {
        @Override
        public String getText(Object element) {
          return ((ICorrespondingUnsafeControlAction) element).getCorrespondingSafetyConstraint()
              .getIdString();
        }
      };
    case 3:
      return new CSCLabelProvider() {
        @Override
        public String getText(Object element) {
          return ((ICorrespondingUnsafeControlAction) element).getCorrespondingSafetyConstraint()
              .getText();
        }
      };
    case 4:
      return new CSCLabelProvider() {
        @Override
        public String getText(Object element) {
          String links = "";
          ISTPADataModel dataModel = (ISTPADataModel) getDataInterface();
          for (UUID uuid : dataModel.getLinkController().getLinksFor(LinkingType.SC2_SC1_LINK,
              ((ICorrespondingUnsafeControlAction) element).getCorrespondingSafetyConstraint().getId())) {
            ITableModel constraint = dataModel.getCausalFactorController().getSafetyConstraint(uuid);
            if (constraint != null) {
              links += links.isEmpty() ? "" : ", ";
              links += constraint.getIdString();
            }
          }
          for (UUID uuid : dataModel.getLinkController().getLinksFor(LinkingType.DR1_CSC_LINK,
              ((ICorrespondingUnsafeControlAction) element).getCorrespondingSafetyConstraint().getId())) {
            ITableModel requirement = dataModel.getSdsController().getDesignRequirement(uuid,
                ObserverValue.DESIGN_REQUIREMENT_STEP1);
            if (!(requirement instanceof BadReferenceModel)) {
              links += links.isEmpty() ? "" : ", ";
              links += requirement.getIdString();
            }
          }
          return links;
        }
      };
    }
    return null;
  }

  @Override
  protected boolean hasEditSupport() {
    return true;
  }

  @Override
  protected boolean canEdit(Object element) {
    if (getDataInterface() instanceof IUserProject) {
      UUID caId = getDataInterface()
          .getControlActionForUca(((IUnsafeControlAction) element).getId()).getId();
      return ((IUserProject) getDataInterface()).getUserSystem().checkAccess(caId,
          AccessRights.WRITE);
    }
    return true;
  }

}
