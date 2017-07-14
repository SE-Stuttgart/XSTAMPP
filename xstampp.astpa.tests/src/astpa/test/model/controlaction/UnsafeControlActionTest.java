package astpa.test.model.controlaction;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.controlaction.UnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.safetyconstraint.CorrespondingSafetyConstraint;

/**
 * Test class for unsafe control action
 * 
 * @author Fabian Toth
 * 
 */
public class UnsafeControlActionTest {
	
	/**
	 * Test of a unsafe control action
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testUnsafeControlAction() {
		UnsafeControlAction unsafeControlAction =
			new UnsafeControlAction("Test", UnsafeControlActionType.GIVEN_INCORRECTLY);
		Assert.assertEquals("Test", unsafeControlAction.getDescription());
		Assert.assertEquals(UnsafeControlActionType.GIVEN_INCORRECTLY, unsafeControlAction.getType());
		Assert.assertEquals("", unsafeControlAction.getCorrespondingSafetyConstraint().getText());
		Assert.assertNotNull(unsafeControlAction.getId());
		
		unsafeControlAction = new UnsafeControlAction();
		unsafeControlAction.setDescription("Test");
		Assert.assertEquals("Test", unsafeControlAction.getDescription());
		unsafeControlAction.setDescription("Test description");
		Assert.assertEquals("Test description", unsafeControlAction.getDescription());
		UUID id = UUID.randomUUID();
		unsafeControlAction.setId(id);
		Assert.assertEquals(id, unsafeControlAction.getId());
		unsafeControlAction.setType(UnsafeControlActionType.NOT_GIVEN);
		Assert.assertEquals(UnsafeControlActionType.NOT_GIVEN, unsafeControlAction.getType());
		CorrespondingSafetyConstraint csc = new CorrespondingSafetyConstraint("Test");
		unsafeControlAction.setCorrespondingSafetyConstraint(csc);
		Assert.assertEquals(csc, unsafeControlAction.getCorrespondingSafetyConstraint());
		unsafeControlAction.setLinks("1, 2, 3");
		Assert.assertEquals("1, 2, 3", unsafeControlAction.getLinks());
	}
}
