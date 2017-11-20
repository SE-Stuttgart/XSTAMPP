/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.model;

import java.util.UUID;

public interface NumberedEntry extends Comparable<NumberedEntry> {
  boolean setNumber(int i);

  int getNumber();

  UUID getId();

  @Override
  default int compareTo(NumberedEntry o) {
    return this.getNumber() - o.getNumber();
  }
}
