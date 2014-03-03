package astpa.test.model.controlaction;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import astpa.model.controlaction.UCAHazLink;
import astpa.model.hazacc.Accident;

/**
 * Test class for links
 * 
 * @author Fabian Toth
 * 
 */
public class UcaHazLinkTest {
	
	/**
	 * Test of a link
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void linkTest() {
		UUID unsafeControlActionId = UUID.randomUUID();
		UUID hazardId = UUID.randomUUID();
		UCAHazLink link = new UCAHazLink(unsafeControlActionId, hazardId);
		Assert.assertEquals(unsafeControlActionId, link.getUnsafeControlActionId());
		Assert.assertEquals(hazardId, link.getHazardId());
		
		// check equals
		Assert.assertTrue(link.equals(link));
		Assert.assertTrue(link.equals(new UCAHazLink(unsafeControlActionId, hazardId)));
		Assert.assertFalse(link.equals(new UCAHazLink(hazardId, unsafeControlActionId)));
		Assert.assertFalse(link.equals(null));
		Assert.assertFalse(link.equals(new Accident()));
		Assert.assertFalse(link.equals(new UCAHazLink(unsafeControlActionId, UUID.randomUUID())));
		link = new UCAHazLink();
		Assert.assertTrue(link.equals(new UCAHazLink()));
		Assert.assertFalse(link.equals(new UCAHazLink(unsafeControlActionId, hazardId)));
		UCAHazLink link2 = new UCAHazLink();
		link2.setHazardId(hazardId);
		Assert.assertFalse(link.equals(link2));
		
		// check hashcode
		link = new UCAHazLink(unsafeControlActionId, hazardId);
		link2 = new UCAHazLink(unsafeControlActionId, hazardId);
		Assert.assertTrue(link.hashCode() == link2.hashCode());
		link2 = new UCAHazLink(hazardId, unsafeControlActionId);
		Assert.assertFalse(link.hashCode() == link2.hashCode());
		Assert.assertTrue(new UCAHazLink().hashCode() == new UCAHazLink().hashCode());
		
		// check empty constructor
		link = new UCAHazLink();
		link.setUnsafeControlActionId(unsafeControlActionId);
		link.setHazardId(hazardId);
		Assert.assertEquals(unsafeControlActionId, link.getUnsafeControlActionId());
		Assert.assertEquals(hazardId, link.getHazardId());
		Assert.assertTrue(link.containsId(unsafeControlActionId));
		Assert.assertTrue(link.containsId(hazardId));
		Assert.assertFalse(link.containsId(UUID.randomUUID()));
		
	}
}
