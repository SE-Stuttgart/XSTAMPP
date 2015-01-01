package astpa.test.ui.systemdescription;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.ui.systemdescription.SystemDescriptionView;

/**
 * Test class for system description view.
 * 
 * @author Sebastian Sieber
 * 
 */
public class SystemDescriptionViewTest {
	
	/**
	 * Test system description view methods.
	 * 
	 * @author Sebastian Sieber
	 * 
	 */
	@Test
	public void systemDescriptionViewTest() {
		SystemDescriptionView view = new SystemDescriptionView();
		Assert.assertEquals("System Description", view.getTitle());
		Assert.assertEquals("astpa.ui.systemdescription.SystemDescriptionViewPart", view.getId());
	}
}
