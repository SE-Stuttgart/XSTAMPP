package astpa.test.model.controlstructure;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * Class that tests the ControlStructureController
 * 
 * @author Fabian Toth
 */
public class ControlStructureControllerTest {
	
	/**
	 * Tests every methods that depends to the component management
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testComponentManagement() {
		DataModelController dataModel = new DataModelController();
		
		Rectangle layout = new Rectangle(5, 10, 15, 20);
		
		// add a component with only null as parameter
		Assert.assertNull(dataModel.addComponent(null, null, null, null, -1));
		
		// add a component with a parent id that does not exist in the model
		Assert.assertNull(dataModel.addComponent(UUID.randomUUID(), layout, "Test", ComponentType.ACTUATOR, -1));
		
		// try to change the layout of a component before the root is set
		Assert.assertFalse(dataModel.changeComponentLayout(UUID.randomUUID(), new Rectangle(), true));
		
		// try to get the causal components before the root is initialized
		Assert.assertEquals(0, dataModel.getCausalComponents().size());
		
		// try to synchronize the layouts before the root is initialized
		Assert.assertFalse(dataModel.synchronizeLayouts());
		
		// Build chain of components with the actuator as root
		UUID id1 = dataModel.setRoot(layout, "Testroot");
		Assert.assertNotNull(id1);
		
		// Check if getComponent with the id of the root and getRoot return the
		// same
		Assert.assertEquals(dataModel.getRoot(), dataModel.getComponent(id1));
		
		UUID id2 = dataModel.addComponent(id1, layout, "Testcontroller", ComponentType.CONTROLLER, -1);
		Assert.assertNotNull(id2);
		UUID id3 = dataModel.addComponent(id2, layout, "Testprocess", ComponentType.CONTROLLED_PROCESS, -1);
		Assert.assertNotNull(id3);
		UUID id4 = dataModel.addComponent(id3, layout, "Testsensor", ComponentType.SENSOR, -1);
		Assert.assertNotNull(id3);
		UUID id5 = dataModel.addComponent(id4, layout, "Testtextfield", ComponentType.TEXTFIELD, -1);
		Assert.assertNotNull(id4);
		Assert.assertNotNull(dataModel.addComponent(id1, layout, "Textfield 2", ComponentType.TEXTFIELD, -1));
		
		//check the recover function
		Assert.assertFalse(dataModel.recoverComponent(null, null));
		Assert.assertFalse(dataModel.recoverComponent(null, UUID.randomUUID()));
		Assert.assertFalse(dataModel.recoverComponent(UUID.randomUUID(),null));
		Assert.assertFalse(dataModel.recoverComponent(id1, null));
		Assert.assertFalse(dataModel.recoverComponent(null, id2));
		Assert.assertFalse(dataModel.recoverComponent(id1, id2));
		
		// Check the get method with the first component
		IRectangleComponent component = dataModel.getComponent(id1);
		Assert.assertNotNull(component);
		Assert.assertEquals(id1, component.getId());
		Assert.assertEquals("Testroot", component.getText());
		
		// Check the get method with the last component
		component = dataModel.getComponent(id5);
		Assert.assertNotNull(component);
		Assert.assertEquals(id5, component.getId());
		Assert.assertEquals("Testtextfield", component.getText());
		
		// Check the get method an id that does not exist
		component = dataModel.getComponent(UUID.randomUUID());
		Assert.assertNull(component);
		
		// Check the getCausalComponents Method
		Assert.assertEquals(1, dataModel.getCausalComponents().size());
		Assert.assertEquals(id2, dataModel.getCausalComponents().get(0).getId());
		
		// Change the layout of an component
		Rectangle layout2 = new Rectangle(30, 30, 30, 30);
		Rectangle layout3 = new Rectangle(15, 15, 15, 15);
		Assert.assertTrue(dataModel.changeComponentLayout(id3, layout2, true));
		Assert.assertEquals(layout2, dataModel.getComponent(id3).getLayout(true));
		Assert.assertTrue(dataModel.changeComponentLayout(id3, layout3, false));
		Assert.assertEquals(layout3, dataModel.getComponent(id3).getLayout(false));
		Assert.assertTrue(dataModel.synchronizeLayouts());
		Assert.assertTrue(dataModel.changeComponentLayout(id3, layout2, false));
		Assert.assertEquals(layout2, dataModel.getComponent(id3).getLayout(false));
		
		// Change the layout of a component that does not exist
		Assert.assertFalse(dataModel.changeComponentLayout(UUID.randomUUID(), layout2, true));
		
		// Change the text of a component
		String newText = "newText";
		Assert.assertTrue(dataModel.changeComponentText(id4, newText));
		Assert.assertEquals(newText, dataModel.getComponent(id4).getText());
		
		// Change the text of a component that does not exist
		Assert.assertFalse(dataModel.changeComponentText(UUID.randomUUID(), newText));
		
		// Create connections
		Anchor source = new Anchor(true, 5, 5, id5);
		Anchor target = new Anchor(false, 5, 5, id4);
		Anchor random = new Anchor(false, 5, 5, UUID.randomUUID());
		UUID cId1 = dataModel.addConnection(source, target, ConnectionType.ARROW_SIMPLE);
		Assert.assertNotNull(cId1);
		UUID cId2 = dataModel.addConnection(target, source, ConnectionType.ARROW_SIMPLE);
		Assert.assertNotNull(cId2);
		UUID cId3 = dataModel.addConnection(random, random, ConnectionType.ARROW_SIMPLE);
		Assert.assertNotNull(cId3);
		
		// Remove the last component
		Assert.assertTrue(dataModel.removeComponent(id5));
		Assert.assertTrue(dataModel.getComponentTrashSize() == 1);
		
		Assert.assertNull(dataModel.getComponent(id5));
		Assert.assertTrue(dataModel.recoverComponent(id4, id5));
		Assert.assertNotNull(dataModel.getComponent(id5));
		// check if the connections were also removed
		Assert.assertNotNull(dataModel.getConnection(cId1));
		Assert.assertNotNull(dataModel.getConnection(cId2));
		Assert.assertNotNull(dataModel.getConnection(cId3));
		
		// Remove a component that does not exist
		Assert.assertFalse(dataModel.removeComponent(UUID.randomUUID()));
		
		// Set a new root and look for an old id
		UUID id6 = dataModel.setRoot(layout, "Anotherroot");
		Assert.assertNotNull(id6);
		Assert.assertNull(dataModel.getComponent(id4));
	}
	
	/**
	 * Tests every method that depends to the connection management
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testConnectionManagement() {
		IControlStructureEditorDataModel dataModel = new DataModelController();
		
		Anchor source = new Anchor(true, 5, 10, UUID.randomUUID());
		Anchor target = new Anchor(false, 10, 5, UUID.randomUUID());
		
		// Add two connections
		UUID id1 = dataModel.addConnection(source, target, ConnectionType.ARROW_DASHED);
		Assert.assertNotNull(id1);
		UUID id2 = dataModel.addConnection(source, target, ConnectionType.ARROW_SIMPLE);
		Assert.assertNotNull(id2);
		Assert.assertEquals(2, dataModel.getConnections().size());
		
		//check the recover function
				Assert.assertFalse(dataModel.recoverConnection(null));
				Assert.assertFalse(dataModel.recoverConnection(UUID.randomUUID()));
				Assert.assertFalse(dataModel.recoverConnection(id1));
				Assert.assertEquals(0,dataModel.getConnectionTrashSize());
				Assert.assertTrue(dataModel.removeConnection(id2));
				Assert.assertTrue(dataModel.removeConnection(id1));
				Assert.assertEquals(2,dataModel.getConnectionTrashSize());
				Assert.assertTrue(dataModel.recoverConnection(id1));
				Assert.assertTrue(dataModel.recoverConnection(id2));
				Assert.assertNotNull(dataModel.getConnection(id2));
				Assert.assertNotNull(dataModel.getConnection(id1));
				
				
		// Get the connections back
		IConnection connection = dataModel.getConnection(id1);
		Assert.assertNotNull(connection);
		Assert.assertEquals(id1, connection.getId());
		
		connection = dataModel.getConnection(id2);
		Assert.assertNotNull(connection);
		Assert.assertEquals(id2, connection.getId());
		
		// change a connection
		Assert.assertTrue(dataModel.changeConnectionSource(id2, target));
		Assert.assertEquals(target, dataModel.getConnection(id2).getSourceAnchor());
		Assert.assertTrue(dataModel.changeConnectionTarget(id2, source));
		Assert.assertEquals(source, dataModel.getConnection(id2).getTargetAnchor());
		Assert.assertTrue(dataModel.changeConnectionType(id2, ConnectionType.ARROW_DASHED));
		Assert.assertEquals(ConnectionType.ARROW_DASHED, dataModel.getConnection(id2).getConnectionType());
		
		// change a connection that does not exist
		UUID id3 = UUID.randomUUID();
		Assert.assertFalse(dataModel.changeConnectionSource(id3, target));
		Assert.assertFalse(dataModel.changeConnectionTarget(id3, source));
		Assert.assertFalse(dataModel.changeConnectionType(id3, ConnectionType.ARROW_DASHED));
		
		// remove both connections and check the Trash function
		Assert.assertEquals(0,dataModel.getConnectionTrashSize());
		Assert.assertTrue(dataModel.removeConnection(id2));
		Assert.assertNull(dataModel.getConnection(id2));
		Assert.assertTrue(dataModel.removeConnection(id1));
		Assert.assertNull(dataModel.getConnection(id1));
		Assert.assertEquals(2,dataModel.getConnectionTrashSize());
		Assert.assertEquals(0, dataModel.getConnections().size());
		Assert.assertTrue(dataModel.recoverConnection(id1));
		Assert.assertNotNull(dataModel.getConnection(id1));
		// remove a connection that does not exist
		Assert.assertFalse(dataModel.removeConnection(id3));
	}
	
	/**
	 * Tests the validation of the controlStructureController
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testNotValid() {
		DataModelController dataModel = new DataModelController();
		
		// Components
		Assert.assertNull(dataModel.addComponent(null, null, null, null, -1));
		Assert.assertNull(dataModel.addComponent(UUID.randomUUID(), null, null, null, -1));
		Assert.assertNull(dataModel.addComponent(UUID.randomUUID(), new Rectangle(), null, null, -1));
		Assert.assertNull(dataModel.addComponent(UUID.randomUUID(), new Rectangle(), "", null, -1));
		Assert.assertNull(dataModel.setRoot(null, null));
		Assert.assertNull(dataModel.setRoot(new Rectangle(), null));
		Assert.assertFalse(dataModel.changeComponentLayout(null, null, true));
		Assert.assertFalse(dataModel.changeComponentLayout(UUID.randomUUID(), null, true));
		Assert.assertFalse(dataModel.changeComponentText(null, null));
		Assert.assertFalse(dataModel.changeComponentText(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.removeComponent(null));
		Assert.assertNull(dataModel.getComponent(null));
		
		// Connections
		Assert.assertNull(dataModel.addConnection(null, null, null));
		Assert.assertNull(dataModel.addConnection(new Anchor(), null, null));
		Assert.assertNull(dataModel.addConnection(new Anchor(), new Anchor(), null));
		Assert.assertFalse(dataModel.changeConnectionType(null, null));
		Assert.assertFalse(dataModel.changeConnectionType(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.changeConnectionSource(null, null));
		Assert.assertFalse(dataModel.changeConnectionSource(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.changeConnectionTarget(null, null));
		Assert.assertFalse(dataModel.changeConnectionTarget(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.removeConnection(null));
		Assert.assertNull(dataModel.getConnection(null));
	}
}
