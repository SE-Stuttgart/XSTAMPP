/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.stpapriv.ui.sds;

import java.util.List;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;
import xstampp.astpa.ui.sds.AbstractFilteredTableView;
import xstampp.model.ObserverValue;
import xstampp.stpapriv.messages.PrivMessages;
import xstampp.stpapriv.ui.SecATableFilter;
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
	public static final String ID = "stpapriv.steps.step2_3"; //$NON-NLS-1$

	/**
	 * 
	 * @author Jarkko Heidenwag
	 * 
	 */
	public CSCView() {
		super(new SecATableFilter(), new String[] { Messages.ID, PrivMessages.UnsecureControlActions, Messages.ID,
				PrivMessages.CorrespondingSecurityConstraints });
		setColumnWeights(new int[] { -1, 5, -1, 5 });
		addEditingSupport(3, new EditSupportProvider() {
			@Override
      protected boolean canEdit(Object element) {
        return CSCView.this.canEdit(element);
      }

      @Override
			protected Object getEditingValue(Object element) {
				if (element instanceof ICorrespondingUnsafeControlAction) {
          return ((ICorrespondingUnsafeControlAction) element).getCorrespondingSafetyConstraint()
              .getText();
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
	}

	@Override
	public String getId() {
		return CSCView.ID;
	}

	@Override
	public String getTitle() {
		return PrivMessages.CorrespondingSecurityConstraints;
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
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
					return "PCA1." + CSCView.this.getDataInterface()
							.getUCANumber(((ICorrespondingUnsafeControlAction) element).getId());
				}
			};
		case 1:
			return new CSCLabelProvider() {
				@Override
				public String getText(Object element) {
					return ((ICorrespondingUnsafeControlAction) element).getDescription();
				}
			};
		case 2:
			return new CSCLabelProvider() {
				@Override
				public String getText(Object element) {
					return "PC1." + CSCView.this.getDataInterface()
							.getUCANumber(((ICorrespondingUnsafeControlAction) element).getId());
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
