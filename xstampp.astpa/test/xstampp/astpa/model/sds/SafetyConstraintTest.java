package xstampp.astpa.model.sds;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.sds.SafetyConstraint;

/**
 * Test class for safety constraints
 * 
 * @author Fabian Toth
 * 
 */
public class SafetyConstraintTest {
	
	/**
	 * Test of a safety constraint
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void safetyConstraintTest() {
		SafetyConstraint safetyConstraint = new SafetyConstraint("Test", "Test description", 1);
		Assert.assertEquals("Test", safetyConstraint.getTitle());
		Assert.assertEquals("Test description", safetyConstraint.getDescription());
		Assert.assertEquals(1, safetyConstraint.getNumber());
		
		safetyConstraint = new SafetyConstraint();
		safetyConstraint.setTitle("Test");
		Assert.assertEquals("Test", safetyConstraint.getTitle());
		safetyConstraint.setDescription("Test description");
		Assert.assertEquals("Test description", safetyConstraint.getDescription());
		UUID id = UUID.randomUUID();
		safetyConstraint.setId(id);
		Assert.assertEquals(id, safetyConstraint.getId());
		safetyConstraint.setNumber(5);
		Assert.assertEquals(5, safetyConstraint.getNumber());
	}
}
