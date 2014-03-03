package astpa.test.model.causalfactor;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import astpa.model.causalfactor.CausalSafetyConstraint;

/**
 * Test class for causal safety constraints
 * 
 * @author Fabian Toth
 * 
 */
public class CausalSafetyConstraintTest {
	
	/**
	 * Test of a corresponding safety constraint
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testCausalSafetyConstraint() {
		CausalSafetyConstraint causalSafetyConstraint = new CausalSafetyConstraint("Test");
		Assert.assertEquals("Test", causalSafetyConstraint.getText());
		Assert.assertNotNull(causalSafetyConstraint.getId());
		
		causalSafetyConstraint = new CausalSafetyConstraint();
		causalSafetyConstraint.setText("Test");
		Assert.assertEquals("Test", causalSafetyConstraint.getText());
		causalSafetyConstraint.setText("Test description");
		Assert.assertEquals("Test description", causalSafetyConstraint.getText());
		UUID id = UUID.randomUUID();
		causalSafetyConstraint.setId(id);
		Assert.assertEquals(id, causalSafetyConstraint.getId());
	}
}
