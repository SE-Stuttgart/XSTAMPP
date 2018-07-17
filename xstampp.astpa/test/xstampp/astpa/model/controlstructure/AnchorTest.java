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
package xstampp.astpa.model.controlstructure;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.controlstructure.components.Anchor;

/**
 * Class that tests anchors
 * 
 * @author Fabian Toth
 */
public class AnchorTest {
	
	/**
	 * Test of a connection
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void anchorTest() {
		UUID id = UUID.randomUUID();
		
		// check parameterized constructor
		Anchor anchor = new Anchor(true, 5, 10, id);
		Assert.assertTrue(anchor.isFlying());
		Assert.assertEquals(5, anchor.getxOrientation(false));
		Assert.assertEquals(10, anchor.getyOrientation(false));
		Assert.assertNotNull(anchor.getId());
		
		// check empty constructor
		anchor = new Anchor();
		anchor.setSource(false);
		Assert.assertFalse(anchor.isFlying());
		anchor.setxOrientation(5, false);
		Assert.assertEquals(5, anchor.getxOrientation(false));
		anchor.setyOrientation(10, false);
		Assert.assertEquals(10, anchor.getyOrientation(false));
		anchor.setOwnerId(id);
		Assert.assertEquals(id, anchor.getOwnerId());
		anchor.setId(id);
		Assert.assertEquals(id, anchor.getId());
	}
}
