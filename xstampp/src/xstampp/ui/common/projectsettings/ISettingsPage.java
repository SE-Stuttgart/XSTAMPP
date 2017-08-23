/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer- initial API and implementation
 ******************************************************************************/
package xstampp.ui.common.projectsettings;

import java.util.UUID;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

import xstampp.ui.common.shell.ModalShell;

public interface ISettingsPage {
  Composite createControl(CTabFolder control, ModalShell parent, UUID modelId);

  boolean isVisible(UUID projectId);

  boolean validate();

  boolean doAccept();

  String getName();

  void setName(String name);

  /**
   * A with respect to all {@link ISettingsPage}'s unique identification String.
   * 
   * @return a String that must <b>not be null</b>
   */
  String getId();
}
