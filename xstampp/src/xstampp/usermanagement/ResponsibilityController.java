/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.usermanagement;

import java.util.ArrayList;
import java.util.UUID;

public class ResponsibilityController extends ArrayList<Responsibility> {

  /**
   * 
   */
  private static final long serialVersionUID = -8243551413032695246L;

  public ResponsibilityController() {
  }

  public boolean add(UUID userId, UUID entryId) {
    if (!this.contains(new Responsibility(userId, entryId))) {
      remove(new Responsibility(null, entryId));
      return add(new Responsibility(userId, entryId));
    }
    return false;
  }
}
