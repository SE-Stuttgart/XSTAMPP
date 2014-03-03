package astpa.test.model.controlaction;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import astpa.model.controlaction.ControlAction;
import astpa.model.controlaction.UnsafeControlActionType;

/**
 * Test class for control action
 * 
 * @author Fabian Toth
 * 
 */
public class ControlActionTest {
	
	/**
	 * Test of a control action
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void controlActionTest() {
		// check parameterized constructor
		ControlAction controlAction = new ControlAction("Test", "Test description", 1);
		Assert.assertEquals("Test", controlAction.getTitle());
		Assert.assertEquals("Test description", controlAction.getDescription());
		Assert.assertEquals(1, controlAction.getNumber());
		
		// add a unsafe control action
		UUID uca1 = controlAction.addUnsafeControlAction("Test NotGiven", UnsafeControlActionType.NOT_GIVEN);
		UUID uca2 =
			controlAction.addUnsafeControlAction("Test GivenIncorrectly", UnsafeControlActionType.GIVEN_INCORRECTLY);
		UUID uca3 =
			controlAction.addUnsafeControlAction("Test StoppedTooSoon", UnsafeControlActionType.STOPPED_TOO_SOON);
		UUID uca4 = controlAction.addUnsafeControlAction("Test WrongTiming", UnsafeControlActionType.WRONG_TIMING);
		Assert.assertEquals(4, controlAction.getUnsafeControlActions().size());
		Assert.assertEquals(1, controlAction.getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY).size());
		Assert.assertEquals(1, controlAction.getUnsafeControlActions(UnsafeControlActionType.NOT_GIVEN).size());
		Assert.assertEquals(1, controlAction.getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON).size());
		Assert.assertEquals(1, controlAction.getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING).size());
		
		// remove the unsafe control actions
		Assert.assertTrue(controlAction.removeUnsafeControlAction(uca4));
		Assert.assertTrue(controlAction.removeUnsafeControlAction(uca3));
		Assert.assertTrue(controlAction.removeUnsafeControlAction(uca2));
		Assert.assertTrue(controlAction.removeUnsafeControlAction(uca1));
		
		// Try to remove a unsafe control action the second time
		Assert.assertFalse(controlAction.removeUnsafeControlAction(uca4));
		
		// check empty constructor
		controlAction = new ControlAction();
		controlAction.setTitle("Test");
		Assert.assertEquals("Test", controlAction.getTitle());
		controlAction.setDescription("Test description");
		Assert.assertEquals("Test description", controlAction.getDescription());
		UUID id = UUID.randomUUID();
		controlAction.setId(id);
		Assert.assertEquals(id, controlAction.getId());
		controlAction.setNumber(5);
		Assert.assertEquals(5, controlAction.getNumber());
	}
}
