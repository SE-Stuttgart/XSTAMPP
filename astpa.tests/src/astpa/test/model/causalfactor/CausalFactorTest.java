package astpa.test.model.causalfactor;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.causalfactor.CausalFactor;
import xstampp.astpa.model.causalfactor.CausalSafetyConstraint;

/**
 * Test class for a causal factor
 * 
 * @author Fabian Toth
 * 
 */
public class CausalFactorTest {
	
	/**
	 * Test of a causal factor
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void causalFactorTest() {
		CausalFactor causalFactor = new CausalFactor("Test");
		Assert.assertEquals("Test", causalFactor.getText());
		Assert.assertNotNull(causalFactor.getId());
		Assert.assertEquals("", causalFactor.getNote());
		Assert.assertNotNull(causalFactor.getSafetyConstraint());
		
		causalFactor = new CausalFactor();
		causalFactor.setText("Test");
		Assert.assertEquals("Test", causalFactor.getText());
		UUID id = UUID.randomUUID();
		causalFactor.setId(id);
		Assert.assertEquals(id, causalFactor.getId());
		causalFactor.setNote("Note");
		Assert.assertEquals("Note", causalFactor.getNote());
		CausalSafetyConstraint causalSafetyConstraint = new CausalSafetyConstraint("");
		causalFactor.setSafetyConstraint(causalSafetyConstraint);
		Assert.assertEquals(causalSafetyConstraint, causalFactor.getSafetyConstraint());
		causalFactor.setLinks("1, 2, 3");
		Assert.assertEquals("1, 2, 3", causalFactor.getLinks());
	}
}
