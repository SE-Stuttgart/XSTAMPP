package astpa.test.model.causalfactor;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.causalfactor.ICausalFactor;
import xstampp.astpa.model.controlstructure.components.ComponentType;






/**
 * Test class for the causal factor controller
 * 
 * @author Fabian Toth
 * 
 */
public class CausalFactorControllerTest {
	
	/**
	 * Test that adds, removes and changes causal factors
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testCausalFactorController() {
		DataModelController dataModel = new DataModelController();
		
		// Try to do operations before the root of the control structure has
		// been set
		Assert.assertFalse(dataModel.setCausalFactorText(UUID.randomUUID(), ""));
		Assert.assertFalse(dataModel.setNoteText(UUID.randomUUID(), ""));
		Assert.assertFalse(dataModel.setCausalSafetyConstraintText(UUID.randomUUID(), ""));
		
		// Set the root for the control structure
		UUID rootComp = dataModel.setRoot(new Rectangle(0, 0, 0, 0), "");
		
		// Try to add a causal Factor to a component that does not exist and not
		// a single component exists
		Assert.assertNull(dataModel.addCausalFactor(UUID.randomUUID(), ""));
		
		UUID comp1 = dataModel.addComponent(rootComp, new Rectangle(0, 0, 0, 0), "", ComponentType.CONTROLLER, -1);
		
		// try to add a causal factor to a component that does not exist
		Assert.assertNull(dataModel.addCausalFactor(UUID.randomUUID(), ""));
		
		// Add causal factors to the component
		UUID cfId1 = dataModel.addCausalFactor(comp1, "New Causal Factor");
		Assert.assertNotNull(cfId1);
		UUID cfId2 = dataModel.addCausalFactor(comp1, "New Causal Factor");
		Assert.assertNotNull(cfId2);
		
		// Set a new Text
		Assert.assertTrue(dataModel.setCausalFactorText(cfId1, "Changed Text"));
		
		// Set a new Text for a causal factor that does not exist
		Assert.assertFalse(dataModel.setCausalFactorText(UUID.randomUUID(), "Changed Text"));
		
		// Set the text of the Causal Safety Constraint
		for (ICausalComponent component : dataModel.getCausalComponents()) {
			for (ICausalFactor causalFactor : component.getCausalFactors()) {
				if (causalFactor.getId().equals(cfId1)) {
					Assert.assertTrue(dataModel.setCausalSafetyConstraintText(causalFactor.getSafetyConstraint()
						.getId(), "Changed Text"));
				}
			}
		}
		
		// Set the text of a causal safety constraint that does not exist
		Assert.assertFalse(dataModel.setCausalSafetyConstraintText(UUID.randomUUID(), "Changed Text"));
		
		// Set the note of the Causal Factor
		Assert.assertTrue(dataModel.setNoteText(cfId1, "New Note"));
		
		// Set the note of a causal factor that does not exist
		Assert.assertFalse(dataModel.setNoteText(UUID.randomUUID(), "Changed Note"));
		
		// Remove the causal factors
		Assert.assertTrue(dataModel.removeCausalFactor(cfId2));
		Assert.assertTrue(dataModel.removeCausalFactor(cfId1));
		
		// Try to remove it again
		Assert.assertFalse(dataModel.removeCausalFactor(cfId1));
	}
	
	/**
	 * Test for the linking mechanism of the causal factor controller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testLinking() {
		DataModelController dataModel = new DataModelController();
		
		// Try to create a link before the root of the control structure has
		// been set
		UUID hazId1 = dataModel.addHazard("Title 1", "Description 1");
		Assert.assertNotNull(hazId1);
		Assert.assertFalse(dataModel.addCausalFactorHazardLink(UUID.randomUUID(), hazId1));
		
		// Create a causal factor and some hazards
		UUID rootComp = dataModel.setRoot(new Rectangle(0, 0, 0, 0), "");
		UUID comp1 = dataModel.addComponent(rootComp, new Rectangle(0, 0, 0, 0), "", ComponentType.CONTROLLER, -1);
		UUID cfId1 = dataModel.addCausalFactor(comp1, "New Causal Factor");
		Assert.assertNotNull(cfId1);
		UUID cfId2 = dataModel.addCausalFactor(comp1, "New Causal Factor");
		Assert.assertNotNull(cfId2);
		UUID hazId2 = dataModel.addHazard("Title 2", "Description 2");
		Assert.assertNotNull(hazId2);
		
		// add links
		Assert.assertTrue(dataModel.addCausalFactorHazardLink(cfId1, hazId1));
		Assert.assertTrue(dataModel.addCausalFactorHazardLink(cfId1, hazId2));
		Assert.assertTrue(dataModel.addCausalFactorHazardLink(cfId2, hazId1));
		Assert.assertEquals(2, dataModel.getLinkedHazardsOfCf(cfId1).size());
		
		// try to get the links of a causal factor that does not exist
		Assert.assertEquals(0, dataModel.getLinkedHazardsOfCf(UUID.randomUUID()).size());
		
		// try to remove a link that does not exist
		Assert.assertFalse(dataModel.removeCausalFactorHazardLink(UUID.randomUUID(), UUID.randomUUID()));
		
		// remove a link
		Assert.assertTrue(dataModel.removeCausalFactorHazardLink(cfId1, hazId2));
		Assert.assertEquals(1, dataModel.getLinkedHazardsOfCf(cfId1).size());
		
		// remove the causal factors
		Assert.assertTrue(dataModel.removeCausalFactor(cfId1));
		Assert.assertEquals(0, dataModel.getLinkedHazardsOfCf(UUID.randomUUID()).size());
		Assert.assertTrue(dataModel.removeCausalFactor(cfId2));
	}
	
	/**
	 * Tests the validation of the CausalFactorController
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testNotValid() {
		DataModelController dataModel = new DataModelController();
		dataModel.setRoot(new Rectangle(), "");
		
		// CausalFactors
		Assert.assertNull(dataModel.addCausalFactor(null, null));
		Assert.assertNull(dataModel.addCausalFactor(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setCausalFactorText(null, null));
		Assert.assertFalse(dataModel.setCausalFactorText(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.addCausalFactorHazardLink(null, null));
		Assert.assertFalse(dataModel.addCausalFactorHazardLink(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.addCausalFactorHazardLink(UUID.randomUUID(), UUID.randomUUID()));
		UUID id = dataModel.addHazard("", "");
		Assert.assertFalse(dataModel.addCausalFactorHazardLink(UUID.randomUUID(), id));
		Assert.assertNull(dataModel.getLinkedHazardsOfCf(null));
		Assert.assertFalse(dataModel.removeCausalFactorHazardLink(null, null));
		Assert.assertFalse(dataModel.removeCausalFactorHazardLink(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.removeCausalFactor(null));
		Assert.assertFalse(dataModel.setCausalSafetyConstraintText(null, null));
		Assert.assertFalse(dataModel.setCausalSafetyConstraintText(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setNoteText(null, null));
		Assert.assertFalse(dataModel.setNoteText(UUID.randomUUID(), null));
	}
}
