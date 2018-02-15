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
package xstampp.astpa.model.controlaction;

import org.junit.Test;

import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;

/**
 * 
 * Test class for the UnsafeControlActionType enum. Is needed to get full code
 * coverage because of the methods that will be added in the bytecode
 * 
 * @author Fabian Toth
 * 
 */
public class UnsafeControlActionTypeTest {
	
	/**
	 * Dummy test to get full code coverage
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testConnectionType() {
		Class<UnsafeControlActionType> enumClass = UnsafeControlActionType.class;
		try {
			for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
				enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
