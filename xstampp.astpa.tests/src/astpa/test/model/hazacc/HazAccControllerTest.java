package astpa.test.model.hazacc;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.IAccidentViewDataModel;
import xstampp.astpa.model.interfaces.IHazardViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;

/**
 * Class that tests the HazAccController
 * 
 * @author Fabian Toth
 * 
 */
public class HazAccControllerTest {
	
	/**
	 * Tests every operation of the DataModelController that depends to the
	 * accidents
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void accidentTest() {
		IAccidentViewDataModel dataModel = new DataModelController();
		
		// fill the dataModel with accidents
		Assert.assertTrue(dataModel.getAllAccidents().size() == 0);
		UUID id1 = dataModel.addAccident("Accident 1", "Test accident 1");
		UUID id2 = dataModel.addAccident("Accident 2", "Test accident 2");
		UUID id3 = dataModel.addAccident("Accident 3", "Test accident 3");
		UUID id4 = dataModel.addAccident("Accident 4", "Test accident 4");
		UUID id5 = dataModel.addAccident("Accident 5", "Test accident 5");
		Assert.assertTrue(dataModel.getAllAccidents().size() == 5);
		
		// check compare to method
		Assert.assertEquals(0, dataModel.getAccident(id3).compareTo(dataModel.getAccident(id3)));
		Assert.assertEquals(1, dataModel.getAccident(id3).compareTo(dataModel.getAccident(id2)));
		Assert.assertEquals(-1, dataModel.getAccident(id3).compareTo(dataModel.getAccident(id4)));
		
		// delete accident 5
		dataModel.removeAccident(id5);
		Assert.assertTrue(dataModel.getAllAccidents().size() == 4);
		Assert.assertNull(dataModel.getAccident(id5));
		
		// check what happens when the accident will be deleted again
		dataModel.removeAccident(id5);
		Assert.assertTrue(dataModel.getAllAccidents().size() == 4);
		
		// get accident 1 and check the values
		ITableModel accident = dataModel.getAccident(id1);
		Assert.assertEquals("Accident 1", accident.getTitle());
		Assert.assertEquals("Test accident 1", accident.getDescription());
		Assert.assertEquals(id1, accident.getId());
		
		// change a accident
		dataModel.setAccidentTitle(id4, "newTitle");
		Assert.assertEquals("newTitle", dataModel.getAccident(id4).getTitle());
		dataModel.setAccidentDescription(id4, "newDescription");
		Assert.assertEquals("newDescription", dataModel.getAccident(id4).getDescription());
		
		// check if the number is right when the first item is deleted
		dataModel.removeAccident(id1);
		Assert.assertTrue(dataModel.getAllAccidents().size() == 3);
		Assert.assertEquals(3, dataModel.getAccident(id4).getNumber());
		Assert.assertEquals(2, dataModel.getAccident(id3).getNumber());
		Assert.assertEquals(1, dataModel.getAccident(id2).getNumber());
		
		// delete all accidents
		dataModel.removeAccident(id4);
		dataModel.removeAccident(id3);
		dataModel.removeAccident(id2);
		Assert.assertTrue(dataModel.getAllAccidents().size() == 0);
	}
	
	/**
	 * Tests every operation of the DataModelController that depends to the
	 * hazards
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void hazardTest() {
		IHazardViewDataModel dataModel = new DataModelController();
		
		// fill the dataModel with hazards
		Assert.assertTrue(dataModel.getAllHazards().size() == 0);
		UUID id1 = dataModel.addHazard("Hazard 1", "Test hazard 1");
		UUID id2 = dataModel.addHazard("Hazard 2", "Test hazard 2");
		UUID id3 = dataModel.addHazard("Hazard 3", "Test hazard 3");
		UUID id4 = dataModel.addHazard("Hazard 4", "Test hazard 4");
		UUID id5 = dataModel.addHazard("Hazard 5", "Test hazard 5");
		Assert.assertTrue(dataModel.getAllHazards().size() == 5);
		
		// delete hazard 5
		dataModel.removeHazard(id5);
		Assert.assertTrue(dataModel.getAllHazards().size() == 4);
		Assert.assertNull(dataModel.getHazard(id5));
		
		// check what happens when the hazard will be deleted again
		dataModel.removeHazard(id5);
		Assert.assertTrue(dataModel.getAllHazards().size() == 4);
		
		// get hazard 1 and check the values
		ITableModel hazard = dataModel.getHazard(id1);
		Assert.assertEquals("Hazard 1", hazard.getTitle());
		Assert.assertEquals("Test hazard 1", hazard.getDescription());
		Assert.assertEquals(id1, hazard.getId());
		
		// change a hazard
		dataModel.setHazardTitle(id4, "newTitle");
		Assert.assertEquals("newTitle", dataModel.getHazard(id4).getTitle());
		dataModel.setHazardDescription(id4, "newDescription");
		Assert.assertEquals("newDescription", dataModel.getHazard(id4).getDescription());
		
		// check if the number is right when the first item is deleted
		dataModel.removeHazard(id1);
		Assert.assertTrue(dataModel.getAllHazards().size() == 3);
		Assert.assertEquals(3, dataModel.getHazard(id4).getNumber());
		Assert.assertEquals(2, dataModel.getHazard(id3).getNumber());
		Assert.assertEquals(1, dataModel.getHazard(id2).getNumber());
		
		// delete all hazards
		dataModel.removeHazard(id3);
		dataModel.removeHazard(id4);
		dataModel.removeHazard(id2);
		Assert.assertTrue(dataModel.getAllHazards().size() == 0);
	}
	
	/**
	 * Tests every operation of the DataModelController that depends to the
	 * linking of accidents and hazards
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void linkingTest() {
		DataModelController dataModel = new DataModelController();
		dataModel.setRoot(new Rectangle(), "");
		// fill dataModel with accidents and hazards
		UUID accidentId1 = dataModel.addAccident("Accident 1", "Test accident 1");
		UUID accidentId2 = dataModel.addAccident("Accident 2", "Test accident 2");
		UUID accidentId3 = dataModel.addAccident("Accident 3", "Test accident 3");
		UUID hazardId1 = dataModel.addHazard("Hazard 1", "Test hazard 1");
		UUID hazardId2 = dataModel.addHazard("Hazard 2", "Test hazard 2");
		UUID hazardId3 = dataModel.addHazard("Hazard 3", "Test hazard 3");
		UUID hazardId4 = dataModel.addHazard("Hazard 4", "Test hazard 4");
		UUID hazardId5 = dataModel.addHazard("Hazard 5", "Test hazard 5");
		
		// add some links
		dataModel.addLink(accidentId1, hazardId1);
		dataModel.addLink(accidentId1, hazardId2);
		dataModel.addLink(accidentId1, hazardId3);
		dataModel.addLink(accidentId1, hazardId4);
		dataModel.addLink(accidentId2, hazardId4);
		Assert.assertEquals(4, dataModel.getLinkedHazards(accidentId1).size());
		Assert.assertEquals(1, dataModel.getLinkedHazards(accidentId2).size());
		Assert.assertEquals(0, dataModel.getLinkedHazards(accidentId3).size());
		Assert.assertEquals(1, dataModel.getLinkedAccidents(hazardId1).size());
		
		// delete a link
		dataModel.deleteLink(accidentId1, hazardId1);
		Assert.assertEquals(3, dataModel.getLinkedHazards(accidentId1).size());
		
		// delete this link again
		dataModel.deleteLink(accidentId1, hazardId1);
		Assert.assertEquals(3, dataModel.getLinkedHazards(accidentId1).size());
		
		// delete the accident with all the links
		dataModel.removeAccident(accidentId1);
		Assert.assertEquals(0, dataModel.getLinkedAccidents(hazardId2).size());
		Assert.assertEquals(0, dataModel.getLinkedAccidents(hazardId3).size());
		Assert.assertEquals(1, dataModel.getLinkedAccidents(hazardId4).size());
		Assert.assertEquals(0, dataModel.getLinkedAccidents(hazardId5).size());
	}
	
	/**
	 * Tests the validation of the hazAccController
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testNotValid() {
		DataModelController dataModel = new DataModelController();
		
		// Accidents
		Assert.assertNull(dataModel.getAccident(null));
		Assert.assertNull(dataModel.getLinkedHazards(null));
		Assert.assertNull(dataModel.addAccident("", null));
		Assert.assertNull(dataModel.addAccident(null, ""));
		Assert.assertFalse(dataModel.removeAccident(null));
		Assert.assertFalse(dataModel.setAccidentDescription(null, ""));
		Assert.assertFalse(dataModel.setAccidentDescription(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setAccidentDescription(UUID.randomUUID(), ""));
		Assert.assertFalse(dataModel.setAccidentTitle(null, ""));
		Assert.assertFalse(dataModel.setAccidentTitle(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setAccidentTitle(UUID.randomUUID(), ""));
		
		// Hazards
		Assert.assertNull(dataModel.getHazard(null));
		Assert.assertNull(dataModel.getLinkedAccidents(null));
		Assert.assertNull(dataModel.addHazard("", null));
		Assert.assertNull(dataModel.addHazard(null, ""));
		Assert.assertFalse(dataModel.removeHazard(null));
		Assert.assertFalse(dataModel.setHazardDescription(null, ""));
		Assert.assertFalse(dataModel.setHazardDescription(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setHazardDescription(UUID.randomUUID(), ""));
		Assert.assertFalse(dataModel.setHazardTitle(null, ""));
		Assert.assertFalse(dataModel.setHazardTitle(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setHazardTitle(UUID.randomUUID(), ""));
		
		// Links
		Assert.assertFalse(dataModel.addLink(null, UUID.randomUUID()));
		Assert.assertFalse(dataModel.addLink(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.addLink(UUID.randomUUID(), UUID.randomUUID()));
		UUID hazard = dataModel.addHazard("", "");
		Assert.assertFalse(dataModel.addLink(UUID.randomUUID(), hazard));
		Assert.assertFalse(dataModel.deleteLink(null, UUID.randomUUID()));
		Assert.assertFalse(dataModel.deleteLink(UUID.randomUUID(), null));
	}
}
