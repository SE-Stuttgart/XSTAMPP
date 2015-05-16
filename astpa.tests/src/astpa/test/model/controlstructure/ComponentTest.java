package astpa.test.model.controlstructure;

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
		Assert.assertTrue(component.addChild(component2, -1));
		Assert.assertTrue(component.addChild(component3, -1));
		Assert.assertTrue(component.addChild(component4, -1));
		Assert.assertEquals(3, component.getChildren().size());
		Assert.assertEquals(component3, component.getChild(component3.getId()));
		Assert.assertEquals(component4, component.getChild(component4.getId()));
		Assert.assertEquals(component2, component.getChild(component2.getId()));
		Assert.assertTrue(component.removeChild(component4.getId()));
		Assert.assertTrue(component.removeChild(component3.getId()));
		Assert.assertTrue(component.removeChild(component2.getId()));
		
		Assert.assertFalse(component.removeChild(component2.getId()));
	}
}
