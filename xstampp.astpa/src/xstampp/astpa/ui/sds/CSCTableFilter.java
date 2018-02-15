/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.ui.sds;

import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.viewers.Viewer;

import messages.Messages;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.ui.common.ProjectManager;

public class CSCTableFilter extends ModeFilter {

    public CSCTableFilter() {
      super( new String[] { Messages.All,
          Messages.CorrespondingSafetyConstraints, Messages.UnsafeControlActions });
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
      if ((this.searchString != null) && (this.searchString.length() != 0)) {
        ICorrespondingUnsafeControlAction cuca = (ICorrespondingUnsafeControlAction) element;
        try {
          switch (this.getCSCFilterMode()) {
          case 1:
            return checkCSCText(cuca);
          case 2:
            return checkUcaDescription(cuca);
          case 0:
            return checkCSCText(cuca) || checkUcaDescription(cuca);
          }
        } catch (PatternSyntaxException exc) {
          ProjectManager.getLOGGER().debug("Exception in the CSC Table filter" + exc.getMessage());
        }
        return false;
      }
      return true;
    }

    private boolean checkCSCText(ICorrespondingUnsafeControlAction cuca) throws PatternSyntaxException{
      return cuca.getCorrespondingSafetyConstraint().getText().toLowerCase()
          .matches(this.searchString);
    }

    private boolean checkUcaDescription(ICorrespondingUnsafeControlAction cuca) throws PatternSyntaxException{
      return cuca.getDescription().toLowerCase().matches(this.searchString);
    }


  }
