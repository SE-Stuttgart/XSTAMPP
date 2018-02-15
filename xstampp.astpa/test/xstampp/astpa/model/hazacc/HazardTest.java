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
package xstampp.astpa.model.hazacc;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.hazacc.Hazard;

/**
 * Test class for a hazards
 * 
 * @author Fabian Toth
 * 
 */
public class HazardTest {
	
	/**
	 * Test of a hazard
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void accidentTest() {
		Hazard hazard = new Hazard("Test", "Test description", 1);
		Assert.assertEquals("Test", hazard.getTitle());
		Assert.assertEquals("Test description", hazard.getDescription());
		Assert.assertEquals(1, hazard.getNumber());
		Assert.assertNull(hazard.getLinks());
		
		hazard = new Hazard();
		hazard.setTitle("Test");
		Assert.assertEquals("Test", hazard.getTitle());
		hazard.setDescription("Test description");
		Assert.assertEquals("Test description", hazard.getDescription());
		UUID id = UUID.randomUUID();
		hazard.setId(id);
		Assert.assertEquals(id, hazard.getId());
		hazard.setNumber(5);
		Assert.assertEquals(5, hazard.getNumber());
		hazard.setLinks("1, 2, 3");
		Assert.assertEquals("1, 2, 3", hazard.getLinks());
	}
}
