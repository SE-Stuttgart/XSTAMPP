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

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;

/**
 * Class that tests the components
 * 
 * @author Fabian Toth
 * 
 */
public class ComponentTest {
	
	/**
	 * Test of a component
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void componentTest() {
		// check parameterized constructor
		Rectangle layout = new Rectangle(5, 5, 5, 5);
		Rectangle layout2 = new Rectangle(10, 10, 10, 10);
		Component component = new Component("Test", layout, ComponentType.ACTUATOR);
		Assert.assertEquals("Test", component.getText());
		Assert.assertEquals(layout, component.getLayout(true));
		Assert.assertEquals(ComponentType.ACTUATOR, component.getComponentType());
		
		// check empty constructor
		component = new Component();
		component.setText("Test");
		Assert.assertEquals("Test", component.getText());
		component.setLayout(layout, true);
		Assert.assertEquals(layout, component.getLayout(true));
		component.setLayout(layout2, false);
		Assert.assertEquals(layout2, component.getLayout(false));
		component.sychronizeLayout();
		Assert.assertEquals(layout, component.getLayout(true));
		Assert.assertEquals(layout, component.getLayout(false));
		
		UUID id = UUID.randomUUID();
		component.setId(id);
		Assert.assertEquals(id, component.getId());
		component.setComponentType(ComponentType.CONTROLLED_PROCESS);
		Assert.assertEquals(ComponentType.CONTROLLED_PROCESS, component.getComponentType());
		Assert.assertEquals(0, component.getChildren().size());
		
		// check child management
		Component component2 = new Component("Test2", layout, ComponentType.CONTROLLER);
		Component component3 = new Component("Test3", layout, ComponentType.CONTROLLED_PROCESS);
		Component component4 = new Component("Test4", layout, ComponentType.SENSOR);
		Component component5 = new Component("Test5", layout, ComponentType.PROCESS_MODEL);
		Assert.assertTrue(component.addChild(component2, -1));
		Assert.assertTrue(component.addChild(component3, -1));
		Assert.assertTrue(component.addChild(component4, -1));
		Assert.assertEquals(component.getChildCount(), component.getChildren().size());
		Assert.assertEquals(3, component.getChildren().size());
		Assert.assertEquals(component3, component.getChild(component3.getId()));
		Assert.assertEquals(component4, component.getChild(component4.getId()));
		Assert.assertEquals(component2, component.getChild(component2.getId()));
		Assert.assertTrue(component.removeChild(component4.getId()));
		Assert.assertTrue(component.removeChild(component3.getId()));
		Assert.assertTrue(component.removeChild(component2.getId()));
		Assert.assertFalse(component.removeChild(component2.getId()));

		Assert.assertTrue(component2.addChild(component5, -1));
		Assert.assertTrue(component2.addChild(component3, -1));
		Assert.assertEquals(2, component2.getChildCount());
		Assert.assertEquals(1, component2.getChildren(true).size());
		
	}
}
