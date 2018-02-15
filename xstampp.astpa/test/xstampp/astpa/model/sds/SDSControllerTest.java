/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.sds;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.IDesignRequirementViewDataModel;
import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;
import xstampp.astpa.model.interfaces.ISystemGoalViewDataModel;
import xstampp.astpa.model.interfaces.ITableModel;

/**
 * Class that tests the SDSController
 * 
 * @author Fabian Toth
 */
public class SDSControllerTest {
	
	/**
	 * Tests every operation of the DataModelController that depends to the
	 * design requirements
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void designRequirementTest() {
		IDesignRequirementViewDataModel dataModel = new DataModelController();
		
		// fill the dataModel with design requirements
		Assert.assertTrue(dataModel.getAllDesignRequirements().size() == 0);
		UUID id1 = dataModel.addDesignRequirement("Design Requirement 1", "Test design requirement 1");
		UUID id2 = dataModel.addDesignRequirement("Design Requirement 2", "Test design requirement 2");
		UUID id3 = dataModel.addDesignRequirement("Design Requirement 3", "Test design requirement 3");
		UUID id4 = dataModel.addDesignRequirement("Design Requirement 4", "Test design requirement 4");
		UUID id5 = dataModel.addDesignRequirement("Design Requirement 5", "Test design requirement 5");
		Assert.assertTrue(dataModel.getAllDesignRequirements().size() == 5);
		
		// delete design requirement 5
		dataModel.removeDesignRequirement(id5);
		Assert.assertTrue(dataModel.getAllDesignRequirements().size() == 4);
		Assert.assertNull(dataModel.getDesignRequirement(id5));
		
		// check what happens when the design requirement will be deleted again
		dataModel.removeDesignRequirement(id5);
		Assert.assertTrue(dataModel.getAllDesignRequirements().size() == 4);
		
		// get design requirement 1 and check the values
		ITableModel designRequirement = dataModel.getDesignRequirement(id1);
		Assert.assertEquals("Design Requirement 1", designRequirement.getTitle());
		Assert.assertEquals("Test design requirement 1", designRequirement.getDescription());
		Assert.assertEquals(id1, designRequirement.getId());
		
		// change a design requirement
		dataModel.setDesignRequirementTitle(id4, "newTitle");
		Assert.assertEquals("newTitle", dataModel.getDesignRequirement(id4).getTitle());
		dataModel.setDesignRequirementDescription(id4, "newDescription");
		Assert.assertEquals("newDescription", dataModel.getDesignRequirement(id4).getDescription());
		
		// check if the number is right when the first item is deleted
		dataModel.removeDesignRequirement(id1);
		Assert.assertTrue(dataModel.getAllDesignRequirements().size() == 3);
		Assert.assertEquals(3, dataModel.getDesignRequirement(id4).getNumber());
		Assert.assertEquals(2, dataModel.getDesignRequirement(id3).getNumber());
		Assert.assertEquals(1, dataModel.getDesignRequirement(id2).getNumber());
		
		// delete all design requirements
		dataModel.removeDesignRequirement(id4);
		dataModel.removeDesignRequirement(id3);
		dataModel.removeDesignRequirement(id2);
		Assert.assertTrue(dataModel.getAllDesignRequirements().size() == 0);
	}
	
	/**
	 * Tests every operation of the DataModelController that depends to the
	 * safety constraints
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void safetyConstraintTest() {
		ISafetyConstraintViewDataModel dataModel = new DataModelController();
		
		// fill the dataModel with safety constraints
		Assert.assertTrue(dataModel.getAllSafetyConstraints().size() == 0);
		UUID id1 = dataModel.addSafetyConstraint("Safety constraint 1", "Test safety constraint 1");
		UUID id2 = dataModel.addSafetyConstraint("Safety constraint 2", "Test safety constraint 2");
		UUID id3 = dataModel.addSafetyConstraint("Safety constraint 3", "Test safety constraint 3");
		UUID id4 = dataModel.addSafetyConstraint("Safety constraint 4", "Test safety constraint 4");
		UUID id5 = dataModel.addSafetyConstraint("Safety constraint 5", "Test safety constraint 5");
		Assert.assertTrue(dataModel.getAllSafetyConstraints().size() == 5);
		
		// delete safety constraint 5
		dataModel.removeSafetyConstraint(id5);
		Assert.assertTrue(dataModel.getAllSafetyConstraints().size() == 4);
		Assert.assertNull(dataModel.getSafetyConstraint(id5));
		
		// check what happens when the safety constraint will be deleted again
		dataModel.removeSafetyConstraint(id5);
		Assert.assertTrue(dataModel.getAllSafetyConstraints().size() == 4);
		
		// get safety constraint 1 and check the values
		ITableModel safetyConstraint = dataModel.getSafetyConstraint(id1);
		Assert.assertEquals("Safety constraint 1", safetyConstraint.getTitle());
		Assert.assertEquals("Test safety constraint 1", safetyConstraint.getDescription());
		Assert.assertEquals(id1, safetyConstraint.getId());
		
		// change a safety constraint
		dataModel.setSafetyConstraintTitle(id4, "newTitle");
		Assert.assertEquals("newTitle", dataModel.getSafetyConstraint(id4).getTitle());
		dataModel.setSafetyConstraintDescription(id4, "newDescription");
		Assert.assertEquals("newDescription", dataModel.getSafetyConstraint(id4).getDescription());
		
		// check if the number is right when the first item is deleted
		dataModel.removeSafetyConstraint(id1);
		Assert.assertTrue(dataModel.getAllSafetyConstraints().size() == 3);
		Assert.assertEquals(3, dataModel.getSafetyConstraint(id4).getNumber());
		Assert.assertEquals(2, dataModel.getSafetyConstraint(id3).getNumber());
		Assert.assertEquals(1, dataModel.getSafetyConstraint(id2).getNumber());
		
		// delete all safety constraints
		dataModel.removeSafetyConstraint(id4);
		dataModel.removeSafetyConstraint(id3);
		dataModel.removeSafetyConstraint(id2);
		Assert.assertTrue(dataModel.getAllSafetyConstraints().size() == 0);
	}
	
	/**
	 * Tests every operation of the DataModelController that depends to the
	 * safety constraints
	 * 
	 * @author Fabian Toth
	 */
	@Test
	public void systemGoalTest() {
		ISystemGoalViewDataModel dataModel = new DataModelController();
		
		// fill the dataModel with system goals
		Assert.assertTrue(dataModel.getAllSystemGoals().size() == 0);
		UUID id1 = dataModel.addSystemGoal("System Goal 1", "Test system goal 1");
		UUID id2 = dataModel.addSystemGoal("System Goal 2", "Test system goal 2");
		UUID id3 = dataModel.addSystemGoal("System Goal 3", "Test system goal 3");
		UUID id4 = dataModel.addSystemGoal("System Goal 4", "Test system goal 4");
		UUID id5 = dataModel.addSystemGoal("System Goal 5", "Test system goal 5");
		Assert.assertTrue(dataModel.getAllSystemGoals().size() == 5);
		
		// delete system goal 5
		dataModel.removeSystemGoal(id5);
		Assert.assertTrue(dataModel.getAllSystemGoals().size() == 4);
		Assert.assertNull(dataModel.getSystemGoal(id5));
		
		// check what happens when the system goal will be deleted again
		dataModel.removeSystemGoal(id5);
		Assert.assertTrue(dataModel.getAllSystemGoals().size() == 4);
		
		// get system goal 1 and check the values
		ITableModel systemGoal = dataModel.getSystemGoal(id1);
		Assert.assertEquals("System Goal 1", systemGoal.getTitle());
		Assert.assertEquals("Test system goal 1", systemGoal.getDescription());
		Assert.assertEquals(id1, systemGoal.getId());
		
		// change a system goal
		dataModel.setSystemGoalTitle(id4, "newTitle");
		Assert.assertEquals("newTitle", dataModel.getSystemGoal(id4).getTitle());
		dataModel.setSystemGoalDescription(id4, "newDescription");
		Assert.assertEquals("newDescription", dataModel.getSystemGoal(id4).getDescription());
		
		// check if the number is right when the first item is deleted
		dataModel.removeSystemGoal(id1);
		Assert.assertTrue(dataModel.getAllSystemGoals().size() == 3);
		Assert.assertEquals(3, dataModel.getSystemGoal(id4).getNumber());
		Assert.assertEquals(2, dataModel.getSystemGoal(id3).getNumber());
		Assert.assertEquals(1, dataModel.getSystemGoal(id2).getNumber());
		
		// delete all system goals
		dataModel.removeSystemGoal(id4);
		dataModel.removeSystemGoal(id3);
		dataModel.removeSystemGoal(id2);
		Assert.assertTrue(dataModel.getAllSystemGoals().size() == 0);
	}
	
	/**
	 * Tests the validation of the SDScontroller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testNotValid() {
		DataModelController dataModel = new DataModelController();
		
		// Design Requirements
		Assert.assertNull(dataModel.getDesignRequirement(null));
		Assert.assertNull(dataModel.addDesignRequirement("", null));
		Assert.assertNull(dataModel.addDesignRequirement(null, ""));
		Assert.assertFalse(dataModel.removeDesignRequirement(null));
		Assert.assertFalse(dataModel.setDesignRequirementDescription(null, ""));
		Assert.assertFalse(dataModel.setDesignRequirementDescription(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setDesignRequirementDescription(UUID.randomUUID(), ""));
		Assert.assertFalse(dataModel.setDesignRequirementTitle(null, ""));
		Assert.assertFalse(dataModel.setDesignRequirementTitle(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setDesignRequirementTitle(UUID.randomUUID(), ""));
		
		// Safety Constraints
		Assert.assertNull(dataModel.addSafetyConstraint("", null));
		Assert.assertNull(dataModel.addSafetyConstraint(null, ""));
		Assert.assertFalse(dataModel.removeSafetyConstraint(null));
		Assert.assertFalse(dataModel.setSafetyConstraintDescription(null, ""));
		Assert.assertFalse(dataModel.setSafetyConstraintDescription(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setSafetyConstraintDescription(UUID.randomUUID(), ""));
		Assert.assertFalse(dataModel.setSafetyConstraintTitle(null, ""));
		Assert.assertFalse(dataModel.setSafetyConstraintTitle(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setSafetyConstraintTitle(UUID.randomUUID(), ""));
		
		// Safety Constraints
		Assert.assertNull(dataModel.getSystemGoal(null));
		Assert.assertNull(dataModel.addSystemGoal("", null));
		Assert.assertNull(dataModel.addSystemGoal(null, ""));
		Assert.assertFalse(dataModel.removeSystemGoal(null));
		Assert.assertFalse(dataModel.setSystemGoalDescription(null, ""));
		Assert.assertFalse(dataModel.setSystemGoalDescription(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setSystemGoalDescription(UUID.randomUUID(), ""));
		Assert.assertFalse(dataModel.setSystemGoalTitle(null, ""));
		Assert.assertFalse(dataModel.setSystemGoalTitle(UUID.randomUUID(), null));
		Assert.assertFalse(dataModel.setSystemGoalTitle(UUID.randomUUID(), ""));
	}
}
