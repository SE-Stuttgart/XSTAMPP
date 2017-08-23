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
package xstampp.astpa.model.interfaces;

import xstampp.astpa.messages.Messages;

public enum Severity {
  S0(0), S1(1), S2(2), S3(3);

  private int severity;

  private Severity(int severity) {
    this.severity = severity;
  }

  @Override
  public String toString() {
    return "S" + severity;
  }

  public String getDescription() {
    switch (severity) {
    case 0:
      return Messages.Severity_S0;
    case 1:
      return Messages.Severity_S1;
    case 2:
      return Messages.Severity_S2;
    case 3:
      return Messages.Severity_S3;
    }
    return "";
  }

}
