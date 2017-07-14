package astpa.test.model.controlaction;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.interfaces.IControlActionViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;

/**
 * Controller-class for working with control actions
 * 
 * @author Fabian Toth
 * 
 */
public class ControlActionControllerTest {
	
	/**
	 * Tests every operation of the DataModelController that depends to the
	 * control actions view
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void controlActionTest() {
		IControlActionViewDataModel dataModel = new DataModelController();
		
		// fill the dataModel with control actions
		Assert.assertTrue(dataModel.getAllControlActions().size() == 0);
		UUID id1 = dataModel.addControlAction("Control Action 1", "Test control action 1");
		UUID id2 = dataModel.addControlAction("Control Action 2", "Test control action 2");
		UUID id3 = dataModel.addControlAction("Control Action 3", "Test control action 3");
		UUID id4 = dataModel.addControlAction("Control Action 4", "Test control action 4");
		UUID id5 = dataModel.addControlAction("Control Action 5", "Test control action 5");
		Assert.assertTrue(dataModel.getAllControlActions().size() == 5);
		
		// delete control action 5
		dataModel.removeControlAction(id5);
		Assert.assertTrue(dataModel.getAllControlActions().size() == 4);
		Assert.assertNull(dataModel.getControlAction(id5));
		
		// check what happens when the control action will be deleted again
		dataModel.removeControlAction(id5);
		Assert.assertTrue(dataModel.getAllControlActions().size() == 4);
		
		// get control action 1 and check the values
		ITableModel controlaction = dataModel.getControlAction(id1);
		Assert.assertEquals("Control Action 1", controlaction.getTitle());
		Assert.assertEquals("Test control action 1", controlaction.getDescription());
		Assert.assertEquals(id1, controlaction.getId());
		
		// change a control action
		dataModel.setControlActionTitle(id4, "newTitle");
		Assert.assertEquals("newTitle", dataModel.getControlAction(id4).getTitle());
		dataModel.setControlActionDescription(id4, "newDescription");
		Assert.assertEquals("newDescription", dataModel.getControlAction(id4).getDescription());
		
		// check if the number is right when the first item is deleted
		dataModel.removeControlAction(id1);
		Assert.assertTrue(dataModel.getAllControlActions().size() == 3);
		Assert.assertEquals(3, dataModel.getControlAction(id4).getNumber());
		Assert.assertEquals(2, dataModel.getControlAction(id3).getNumber());
		Assert.assertEquals(1, dataModel.getControlAction(id2).getNumber());
		
		// delete all control actions
		dataModel.removeControlAction(id4);
		dataModel.removeControlAction(id3);
		dataModel.removeControlAction(id2);
		Assert.assertTrue(dataModel.getAllControlActions().size() == 0);
	}
	
	/**
	 * Tests every operation of the DataModelController that depends to the
	 * unsafe control actions view
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void unsafeControlActionTest() {
		DataModelController dataModel = new DataModelController();
		
		// fill the dataModel with control actions
		UUID id1 = dataModel.addControlAction("Control Action 1", "Test control action 1");
		UUID id2 = dataModel.addControlAction("Control Action 2", "Test control action 2");
		UUID id3 = dataModel.addControlAction("Control Action 3", "Test control action 3");
		UUID id4 = dataModel.addControlAction("Control Action 4", "Test control action 4");
		UUID id5 = dataModel.addControlAction("Control Action 5", "Test control action 5");
		
		// check if all control actions were added
		Assert.assertTrue(dataModel.getAllControlActionsU().size() == 5);
		
		// check if all control actions can be found
		Assert.assertEquals(id1, dataModel.getControlActionU(id1).getId());
		Assert.assertEquals(id2, dataModel.getControlActionU(id2).getId());
		Assert.assertEquals(id3, dataModel.getControlActionU(id3).getId());
		Assert.assertEquals(id4, dataModel.getControlActionU(id4).getId());
		Assert.assertEquals(id5, dataModel.getControlActionU(id5).getId());
		
		// check the get method with an id that does not exist
		Assert.assertNull(dataModel.getControlActionU(UUID.randomUUID()));
		
		// add unsafe control actions to the first control action
		UUID ucaId1 = dataModel.addUnsafeControlAction(id1, "Test 1", UnsafeControlActionType.GIVEN_INCORRECTLY);
		Assert.assertNotNull(ucaId1);
		UUID ucaId2 = dataModel.addUnsafeControlAction(id1, "Test 2", UnsafeControlActionType.NOT_GIVEN);
		Assert.assertNotNull(ucaId2);
		UUID ucaId3 = dataModel.addUnsafeControlAction(id1, "Test 3", UnsafeControlActionType.STOPPED_TOO_SOON);
		Assert.assertNotNull(ucaId3);
		UUID ucaId4 = dataModel.addUnsafeControlAction(id1, "Test 4", UnsafeControlActionType.WRONG_TIMING);
		Assert.assertNotNull(ucaId4);
		
		// check if they were added
		Assert.assertEquals(4, dataModel.getControlActionU(id1).getUnsafeControlActions().size());
		Assert.assertEquals(ucaId1,
			dataModel.getControlActionU(id1).getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY).get(0)
				.getId());
		Assert.assertEquals(ucaId2,
			dataModel.getControlActionU(id1).getUnsafeControlActions(UnsafeControlActionType.NOT_GIVEN).get(0).getId());
		Assert.assertEquals(ucaId3,
			dataModel.getControlActionU(id1).getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON).get(0)
				.getId());
		Assert.assertEquals(ucaId4,
			dataModel.getControlActionU(id1).getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING).get(0)
				.getId());
		
		// check what happens when a unsafe control action is added to a id that
		// does not exist in the model
		Assert.assertNull(dataModel.addUnsafeControlAction(UUID.randomUUID(), "Test",
			UnsafeControlActionType.GIVEN_INCORRECTLY));
		
		// change the description of a unsafe control action
		dataModel.setUcaDescription(ucaId1, "New Test");
		Assert.assertEquals("New Test",
			dataModel.getControlActionU(id1).getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY).get(0)
				.getDescription());
		
		// change the description of a unsafe control action that does not exist
		Assert.assertFalse(dataModel.setUcaDescription(UUID.randomUUID(), ""));
		
		// create some hazard objects
		UUID hazId1 = dataModel.addHazard("Test 1", "Test description");
		UUID hazId2 = dataModel.addHazard("Test 2", "Test description");
		UUID hazId3 = dataModel.addHazard("Test 3", "Test description");
		
		// add some links to a control action
		dataModel.addUCAHazardLink(ucaId1, hazId1);
		dataModel.addUCAHazardLink(ucaId1, hazId2);
		dataModel.addUCAHazardLink(ucaId1, hazId3);
		dataModel.addUCAHazardLink(ucaId2, hazId3);
		
		// set corresponding safety constraints
		dataModel.setCorrespondingSafetyConstraint(ucaId1, "New corresponding safety constraint");
		Assert.assertNotNull(dataModel.getAllUnsafeControlActions().get(0).getCorrespondingSafetyConstraint().getId());
		Assert.assertEquals("New corresponding safety constraint", dataModel.getAllUnsafeControlActions().get(0)
			.getCorrespondingSafetyConstraint().getText());
		
		// get the corresponding safety constraints
		Assert.assertEquals(2, dataModel.getCorrespondingSafetyConstraints().size());
		Assert.assertEquals("New corresponding safety constraint", dataModel.getCorrespondingSafetyConstraints().get(0)
			.getText());
		
		// try to set the corresponding safety constraint where the
		// unsafe control action does not exist
		Assert.assertNull(dataModel.setCorrespondingSafetyConstraint(UUID.randomUUID(), ""));
		
		// get the links
		Assert.assertEquals(hazId1, dataModel.getLinkedHazardsOfUCA(ucaId1).get(0).getId());
		
		// check what happens when the links of a unsafe control action that not
		// exist are requested
		Assert.assertEquals(0, dataModel.getLinkedHazardsOfUCA(UUID.randomUUID()).size());
		
		// delete the link
		dataModel.removeUCAHazardLink(id4, id5);
		
		// remove the unsafe Control actions
		Assert.assertTrue(dataModel.removeUnsafeControlAction(ucaId1));
		
		// check if all links were removed
		Assert.assertEquals(0, dataModel.getLinkedHazardsOfUCA(ucaId1).size());
		
		// remove all other unsafe control actions
		Assert.assertTrue(dataModel.removeUnsafeControlAction(ucaId4));
		Assert.assertTrue(dataModel.removeUnsafeControlAction(ucaId3));
		Assert.assertTrue(dataModel.removeUnsafeControlAction(ucaId2));
		
		// try to remove a unsafe control action that does not exist in the
		// model
		Assert.assertFalse(dataModel.removeUnsafeControlAction(UUID.randomUUID()));
	}
	
	/**
	 * Tests the validation of the ControlActionController
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testNotValid() {
		DataModelController dataModel = new DataModelController();
		
		// Control Actions
		Assert.assertNull(dataModel.getControlAction(null));
		Assert.assertNull(dataModel.getControlActionU(null));
		Assert.assertNull(dataModel.addControlAction("", null));
		Assert.assertNull(dataModel.addControlAction(null, ""));
		Assert.assertFalse(dataModel.removeControlAction(null));
		Assert.assertFalse(dataModel.setControlActionDescription(null, ""));
		Assert.assertFalse(dataModel.setControlActionDescription(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setControlActionDescription(UUID.randomUUID(), ""));
		Assert.assertFalse(dataModel.setControlActionTitle(null, ""));
		Assert.assertFalse(dataModel.setControlActionTitle(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setControlActionTitle(UUID.randomUUID(), ""));
		
		// Unsafe Control Actions
		Assert.assertNull(dataModel.addUnsafeControlAction(null, null, null));
		Assert.assertNull(dataModel.addUnsafeControlAction(UUID.randomUUID(), null, null));
		Assert.assertNull(dataModel.addUnsafeControlAction(UUID.randomUUID(), "", null));
		Assert.assertFalse(dataModel.removeUnsafeControlAction(null));
		Assert.assertNull(dataModel.getLinkedHazardsOfUCA(null));
		Assert.assertFalse(dataModel.addUCAHazardLink(null, null));
		Assert.assertFalse(dataModel.addUCAHazardLink(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.addUCAHazardLink(UUID.randomUUID(), UUID.randomUUID()));
		UUID id = dataModel.addHazard("", "");
		Assert.assertFalse(dataModel.addUCAHazardLink(UUID.randomUUID(), id));
		Assert.assertFalse(dataModel.removeUCAHazardLink(null, null));
		Assert.assertFalse(dataModel.removeUCAHazardLink(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setUcaDescription(null, null));
		Assert.assertFalse(dataModel.setUcaDescription(UUID.randomUUID(), null));
		Assert.assertNull(dataModel.setCorrespondingSafetyConstraint(null, null));
		Assert.assertNull(dataModel.setCorrespondingSafetyConstraint(UUID.randomUUID(), null));
	}
}
