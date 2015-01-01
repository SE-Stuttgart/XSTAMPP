package astpa.test.model.causalfactor;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.causalfactor.CausalFactorHazardLink;
import xstampp.astpa.model.hazacc.Accident;

/**
 * Test class for links between a causal factor and a hazard
 * 
 * @author Fabian Toth
 * 
 */
public class CausalFactorHazardLinkTest {
	
	/**
	 * Test of a causal factor hazard link
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void linkTest() {
		UUID causalFactorId = UUID.randomUUID();
		UUID hazardId = UUID.randomUUID();
		CausalFactorHazardLink link = new CausalFactorHazardLink(causalFactorId, hazardId);
		Assert.assertEquals(causalFactorId, link.getCausalFactorId());
		Assert.assertEquals(hazardId, link.getHazardId());
		
		// check equals
		Assert.assertTrue(link.equals(link));
		Assert.assertTrue(link.equals(new CausalFactorHazardLink(causalFactorId, hazardId)));
		Assert.assertFalse(link.equals(new CausalFactorHazardLink(hazardId, causalFactorId)));
		Assert.assertFalse(link.equals(null));
		Assert.assertFalse(link.equals(new Accident()));
		Assert.assertFalse(link.equals(new CausalFactorHazardLink(causalFactorId, UUID.randomUUID())));
		link = new CausalFactorHazardLink();
		Assert.assertTrue(link.equals(new CausalFactorHazardLink()));
		Assert.assertFalse(link.equals(new CausalFactorHazardLink(causalFactorId, hazardId)));
		CausalFactorHazardLink link2 = new CausalFactorHazardLink();
		link2.setHazardId(hazardId);
		Assert.assertFalse(link.equals(link2));
		
		// check hashcode
		link = new CausalFactorHazardLink(causalFactorId, hazardId);
		link2 = new CausalFactorHazardLink(causalFactorId, hazardId);
		Assert.assertTrue(link.hashCode() == link2.hashCode());
		link2 = new CausalFactorHazardLink(hazardId, causalFactorId);
		Assert.assertFalse(link.hashCode() == link2.hashCode());
		Assert.assertTrue(new CausalFactorHazardLink().hashCode() == new CausalFactorHazardLink().hashCode());
		
		// check empty constructor
		link = new CausalFactorHazardLink();
		link.setCausalFactorId(causalFactorId);
		link.setHazardId(hazardId);
		Assert.assertEquals(causalFactorId, link.getCausalFactorId());
		Assert.assertEquals(hazardId, link.getHazardId());
		Assert.assertTrue(link.containsId(causalFactorId));
		Assert.assertTrue(link.containsId(hazardId));
		Assert.assertFalse(link.containsId(UUID.randomUUID()));
		
	}
}
