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
package xstpa.ui;

import xstampp.astpa.ui.causalScenarios.CausalScenariosView;

public class BasicScenariosTable extends CausalScenariosView {
  public BasicScenariosTable() {
    super(true, false, true);
    columns[1] = "Basic Scenarios";
  }
}
