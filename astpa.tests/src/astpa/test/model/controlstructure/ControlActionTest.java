package astpa.test.model.controlstructure;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;

public class ControlActionTest {
	private DataModelController controller;
	private UUID rootID;
	@Before
	public void setUp() throws Exception {
		controller = new DataModelController();
		rootID= controller.setRoot(new Rectangle(), new String());

		Assert.assertNotNull(rootID);
	}
	/**
	 * Tests the ControlAction Management from within the control structure by adding removing 
	 * control action components and lists
	 * 
	 * @author Lukas Balzer
	 */
	@Test
	public void testLifespan(){
		//test whether creating a control action component creates a control action 
		//which is than also deleted when the component is deleted
		UUID comp1ID= controller.addComponent(null, rootID, new Rectangle(), new String(), ComponentType.CONTROLACTION, -1);
		Assert.assertNotNull(comp1ID);
		UUID caID = controller.getComponent(comp1ID).getControlActionLink();
		Assert.assertNotNull(caID);
		Assert.assertNotNull(controller.getControlAction(caID));
		controller.removeComponent(comp1ID);
		Assert.assertNull("when the control action component is deleted, the ca itself should be deleted aswell!",
							controller.getControlAction(caID));
		
		comp1ID= controller.addComponent(null, rootID, new Rectangle(), new String(), ComponentType.CONTROLACTION, -1);
		Assert.assertNotNull(comp1ID);
		caID = controller.getComponent(comp1ID).getControlActionLink();
		Assert.assertNotNull(caID);
		Assert.assertNotNull(controller.getControlAction(caID));
		controller.removeControlAction(caID);
		Assert.assertNull("when the control action is deleted, the component itself should be deleted aswell!",
							controller.getComponent(comp1ID));
		

		//test whether creating a control action component as a child creates a control action 
		//which is than also deleted when the component parent is deleted
		UUID listID = controller.addComponent(rootID, new Rectangle(), new String(), ComponentType.CONTAINER, -1);
		Assert.assertNotNull(listID);
		comp1ID= controller.addComponent(null, listID, new Rectangle(), new String(), ComponentType.CONTROLACTION, -1);
		Assert.assertNotNull(comp1ID);
		caID = controller.getComponent(comp1ID).getControlActionLink();
		Assert.assertNotNull(caID);
		Assert.assertNotNull(controller.getControlAction(caID));
		controller.removeComponent(listID);
		Assert.assertNull("when the control action component's parent is deleted, the ca should be deleted aswell!",
							controller.getControlAction(caID));
		
		listID = controller.addComponent(rootID, new Rectangle(), new String(), ComponentType.CONTAINER, -1);
		Assert.assertNotNull(listID);
		comp1ID= controller.addComponent(null, listID, new Rectangle(), new String(), ComponentType.CONTROLACTION, -1);
		Assert.assertNotNull(comp1ID);
		caID = controller.getComponent(comp1ID).getControlActionLink();
		Assert.assertNotNull(caID);
		Assert.assertNotNull(controller.getControlAction(caID));
		controller.removeControlAction(caID);
		Assert.assertNull("when the control action is deleted, the component itself should be deleted aswell!",
							controller.getComponent(comp1ID));
	}

	@Test
	public void testNaming(){
		String error = "the controlaction title should be the same as the component title ";
		UUID compID = controller.addComponent(rootID, new Rectangle(), "a", ComponentType.CONTROLACTION, -1);
		UUID caID = controller.getComponent(compID).getControlActionLink();
		controller.changeComponentText(compID, "b");
		Assert.assertEquals(error,"b", controller.getControlAction(caID).getTitle());
		controller.setControlActionTitle(caID, "c");
		Assert.assertEquals(error,"c", controller.getComponent(compID).getText());
		
		UUID listID = controller.addComponent(rootID, new Rectangle(), "list", ComponentType.CONTAINER, -1);
		compID = controller.addComponent(rootID, new Rectangle(), "a", ComponentType.CONTROLACTION, -1);
		caID = controller.getComponent(compID).getControlActionLink();
		controller.changeComponentText(compID, "b");
		Assert.assertEquals(error,"b", controller.getControlAction(caID).getTitle());
		controller.setControlActionTitle(caID, "c");
		Assert.assertEquals(error,"c", controller.getComponent(compID).getText());
	}
	
	@Test
	public void testRelativeLinking(){
		UUID comp_contr= controller.addComponent(rootID, new Rectangle(), "", ComponentType.CONTROLLER, -1);
		UUID comp_act = controller.addComponent(rootID, new Rectangle(), new String(), ComponentType.ACTUATOR, -1);
		Anchor anchor_a = new Anchor(false, 0, 0, comp_contr);
		Anchor anchor_b = new Anchor(false, 0, 0, comp_act);
		UUID connID = controller.addConnection(anchor_a, anchor_b, ConnectionType.ARROW_SIMPLE);
		
		UUID comp1ID= controller.addComponent(null, rootID, new Rectangle(), new String(), ComponentType.CONTROLACTION, -1);
		controller.setRelativeOfComponent(comp1ID, connID);
		UUID caID = controller.getComponent(comp1ID).getControlActionLink();
		

		//test whether creating a control action component as a child creates a control action 
		//which is than also deleted when the component parent is deleted
		UUID listID = controller.addComponent(rootID, new Rectangle(), new String(), ComponentType.CONTAINER, -1);
		comp1ID= controller.addComponent(null, listID, new Rectangle(), new String(), ComponentType.CONTROLACTION, -1);
		caID = controller.getComponent(comp1ID).getControlActionLink();
	}
}
