package astpa.test.model.projectdata;

import org.eclipse.swt.custom.StyleRange;
import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;

/**
 * Class tahat tests the ProjectDataController
 * 
 * @author Fabian Toth
 * 
 */
public class ProjectDataControllerTest {
	
	/**
	 * Testmethod for the ProjectDataController
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testProjectDataController() {
		DataModelController dataModel = new DataModelController();
		Assert.assertEquals("New Project", dataModel.getProjectName());
		Assert.assertEquals("", dataModel.getProjectDescription());
		
		// set new values
		dataModel.setProjectName("Testproject");
		Assert.assertEquals("Testproject", dataModel.getProjectName());
		dataModel.setProjectDescription("Testproject description");
		Assert.assertEquals("Testproject description", dataModel.getProjectDescription());
		
		// test the style ranges
		Assert.assertEquals(0, dataModel.getStyleRanges().size());
		StyleRange styleRange1 = new StyleRange(0, 5, null, null);
		StyleRange styleRange2 = new StyleRange(0, 5, null, null);
		
		Assert.assertTrue(dataModel.addStyleRange(styleRange1));
		Assert.assertTrue(dataModel.addStyleRange(styleRange2));
		Assert.assertEquals(2, dataModel.getStyleRanges().size());
		Assert.assertEquals(styleRange1, dataModel.getStyleRanges().get(0));
		Assert.assertEquals(styleRange2, dataModel.getStyleRanges().get(1));
		Assert.assertEquals(styleRange1, dataModel.getStyleRangesAsArray()[0]);
		Assert.assertEquals(styleRange2, dataModel.getStyleRangesAsArray()[1]);
		
		// set values which are not valid
		Assert.assertFalse(dataModel.setProjectName(null));
		Assert.assertFalse(dataModel.setProjectDescription(null));
		Assert.assertFalse(dataModel.addStyleRange(null));
	}
}
