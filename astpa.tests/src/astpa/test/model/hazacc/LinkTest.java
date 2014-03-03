package astpa.test.model.hazacc;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import astpa.model.hazacc.Accident;
import astpa.model.hazacc.Link;

/**
 * Test class for links
 * 
 * @author Fabian Toth
 * 
 */
public class LinkTest {
	
	/**
	 * Test of a link
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void linkTest() {
		UUID accidentId = UUID.randomUUID();
		UUID hazardId = UUID.randomUUID();
		Link link = new Link(accidentId, hazardId);
		Assert.assertEquals(accidentId, link.getAccidentId());
		Assert.assertEquals(hazardId, link.getHazardId());
		
		// check equals
		Assert.assertTrue(link.equals(link));
		Assert.assertTrue(link.equals(new Link(accidentId, hazardId)));
		Assert.assertFalse(link.equals(new Link(hazardId, accidentId)));
		Assert.assertFalse(link.equals(null));
		Assert.assertFalse(link.equals(new Accident()));
		Assert.assertFalse(link.equals(new Link(accidentId, UUID.randomUUID())));
		link = new Link();
		Assert.assertTrue(link.equals(new Link()));
		Assert.assertFalse(link.equals(new Link(accidentId, hazardId)));
		Link link2 = new Link();
		link2.setHazardId(hazardId);
		Assert.assertFalse(link.equals(link2));
		
		// check hashcode
		link = new Link(accidentId, hazardId);
		link2 = new Link(accidentId, hazardId);
		Assert.assertTrue(link.hashCode() == link2.hashCode());
		link2 = new Link(hazardId, accidentId);
		Assert.assertFalse(link.hashCode() == link2.hashCode());
		Assert.assertTrue(new Link().hashCode() == new Link().hashCode());
		
		// check empty constructor
		link = new Link();
		link.setAccidentId(accidentId);
		link.setHazardId(hazardId);
		Assert.assertEquals(accidentId, link.getAccidentId());
		Assert.assertEquals(hazardId, link.getHazardId());
		Assert.assertTrue(link.containsId(accidentId));
		Assert.assertTrue(link.containsId(hazardId));
		Assert.assertFalse(link.containsId(UUID.randomUUID()));
		
	}
}
