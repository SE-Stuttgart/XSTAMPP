package astpa.test.model.controlaction;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.controlaction.safetyconstraint.CorrespondingSafetyConstraint;

/**
 * Test class for corresponding safety constraints
 * 
 * @author Fabian Toth
 * 
 */
public class CorrespondingSafetyConstraintTest {
	
	/**
	 * Test of a corresponding safety constraint
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testCorrespondingSafetyConstraint() {
		CorrespondingSafetyConstraint correspondingSafetyConstraint = new CorrespondingSafetyConstraint("Test");
		Assert.assertEquals("Test", correspondingSafetyConstraint.getText());
		Assert.assertNotNull(correspondingSafetyConstraint.getId());
		
		correspondingSafetyConstraint = new CorrespondingSafetyConstraint();
		correspondingSafetyConstraint.setTitle("Test");
		Assert.assertEquals("Test", correspondingSafetyConstraint.getText());
		correspondingSafetyConstraint.setTitle("Test description");
		Assert.assertEquals("Test description", correspondingSafetyConstraint.getText());
		UUID id = UUID.randomUUID();
		correspondingSafetyConstraint.setId(id);
		Assert.assertEquals(id, correspondingSafetyConstraint.getId());
	}
	
}
