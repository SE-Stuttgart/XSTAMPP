package astpa.test.model.controlstructure;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.controlstructure.components.Anchor;

/**
 * Class that tests anchors
 * 
 * @author Fabian Toth
 */
public class AnchorTest {
	
	/**
	 * Test of a connection
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void anchorTest() {
		UUID id = UUID.randomUUID();
		
		// check parameterized constructor
		Anchor anchor = new Anchor(true, 5, 10, id);
		Assert.assertTrue(anchor.isFlying());
		Assert.assertEquals(5, anchor.getxOrientation());
		Assert.assertEquals(10, anchor.getyOrientation());
		Assert.assertNotNull(anchor.getId());
		
		// check empty constructor
		anchor = new Anchor();
		anchor.setSource(false);
		Assert.assertFalse(anchor.isFlying());
		anchor.setxOrientation(5);
		Assert.assertEquals(5, anchor.getxOrientation());
		anchor.setyOrientation(10);
		Assert.assertEquals(10, anchor.getyOrientation());
		anchor.setOwnerId(id);
		Assert.assertEquals(id, anchor.getOwnerId());
		anchor.setId(id);
		Assert.assertEquals(id, anchor.getId());
	}
}
