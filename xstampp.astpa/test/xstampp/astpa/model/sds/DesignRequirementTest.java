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

import xstampp.astpa.model.sds.DesignRequirement;

/**
 * Test class for design requirements
 * 
 * @author Fabian Toth
 * 
 */
public class DesignRequirementTest {
	
	/**
	 * Test of a design requirement
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void designRequirementTest() {
		DesignRequirement designRequirement = new DesignRequirement("Test", "Test description", 1);
		Assert.assertEquals("Test", designRequirement.getTitle());
		Assert.assertEquals("Test description", designRequirement.getDescription());
		Assert.assertEquals(1, designRequirement.getNumber());
		
		designRequirement = new DesignRequirement();
		designRequirement.setTitle("Test");
		Assert.assertEquals("Test", designRequirement.getTitle());
		designRequirement.setDescription("Test description");
		Assert.assertEquals("Test description", designRequirement.getDescription());
		UUID id = UUID.randomUUID();
		designRequirement.setId(id);
		Assert.assertEquals(id, designRequirement.getId());
		designRequirement.setNumber(5);
		Assert.assertEquals(5, designRequirement.getNumber());
	}
}
