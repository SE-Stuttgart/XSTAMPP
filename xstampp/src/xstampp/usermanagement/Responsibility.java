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

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class Responsibility {
  @XmlAttribute
  private UUID userId;

  @XmlAttribute
  private UUID entryId;

  public Responsibility() {
  }

  public Responsibility(UUID userId, UUID entryId) {
    this.userId = userId;
    this.entryId = entryId;
  }

  /**
   * @return the userId
   */
  public UUID getUserId() {
    return userId;
  }

  /**
   * @return the entryId
   */
  public UUID getEntryId() {
    return entryId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Responsibility) {
      Responsibility resp = (Responsibility) obj;
      boolean userNull = resp.userId == null && userId == null;
      boolean equalUser = userId != null && userId.equals(resp.userId);
      boolean entryNull = resp.entryId == null && entryId == null;
      boolean equalEntry = entryId != null && entryId.equals(resp.entryId);
      if (equalUser && equalEntry) {
        return true;
      }
      if (userNull && equalEntry) {
        return true;
      }
      if (userNull && entryNull) {
        return true;
      }
    }
    return super.equals(obj);
  }
}
