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
package xstampp.astpa.model.sds;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.sds.SystemGoal;

/**
 * Test class for system goals
 * 
 * @author Fabian Toth
 * 
 */
public class SystemGoalTest {
	
	/**
	 * Test of a system goal
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void systemGoalTest() {
		SystemGoal systemGoal = new SystemGoal("Test", "Test description", 1);
		Assert.assertEquals("Test", systemGoal.getTitle());
		Assert.assertEquals("Test description", systemGoal.getDescription());
		Assert.assertEquals(1, systemGoal.getNumber());
		
		systemGoal = new SystemGoal();
		systemGoal.setTitle("Test");
		Assert.assertEquals("Test", systemGoal.getTitle());
		systemGoal.setDescription("Test description");
		Assert.assertEquals("Test description", systemGoal.getDescription());
		UUID id = UUID.randomUUID();
		systemGoal.setId(id);
		Assert.assertEquals(id, systemGoal.getId());
		systemGoal.setNumber(5);
		Assert.assertEquals(5, systemGoal.getNumber());
	}
}
