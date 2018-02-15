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
import xstampp.astpa.model.controlstructure.components.CSConnection;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;

/**
 * Class that tests connections
 * 
 * @author Fabian Toth
 */
public class ConnectionTest {
	
	/**
	 * Test of a connection
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void connectionTest() {
		Anchor source = new Anchor(true, 5, 10, UUID.randomUUID());
		Anchor target = new Anchor(false, 10, 5, UUID.randomUUID());
		
		// check parameterized constructor
		CSConnection connection = new CSConnection(source, target, ConnectionType.ARROW_DASHED);
		Assert.assertEquals(source, connection.getSourceAnchor());
		Assert.assertEquals(target, connection.getTargetAnchor());
		Assert.assertEquals(ConnectionType.ARROW_DASHED, connection.getConnectionType());
		Assert.assertEquals(ComponentType.CONNECTION, connection.getComponentType());
		
		// check empty constructor
		connection = new CSConnection();
		connection.setSourceAnchor(source);
		Assert.assertEquals(source, connection.getSourceAnchor());
		connection.setTargetAnchor(target);
		Assert.assertEquals(target, connection.getTargetAnchor());
		connection.setConnectionType(ConnectionType.ARROW_SIMPLE);
		Assert.assertEquals(ConnectionType.ARROW_SIMPLE, connection.getConnectionType());
		UUID id = UUID.randomUUID();
		connection.setId(id);
		Assert.assertEquals(id, connection.getId());
	}
}
