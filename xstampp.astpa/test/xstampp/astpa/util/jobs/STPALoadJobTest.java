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
package xstampp.astpa.util.jobs;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

public class STPALoadJobTest {
  
  @Test
  public void loadTest() {
    STPALoadJob loadJob = new STPALoadJob();
    loadJob.setFile("Adaptive Cruise Control System.haz");
    loadJob.setSaveFile("Adaptive Cruise Control System.haz");
    loadJob.run(new NullProgressMonitor());
  }
  
}
