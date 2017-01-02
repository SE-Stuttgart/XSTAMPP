package astpa.test.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.haz.controlaction.UnsafeControlActionType;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;
import xstampp.astpa.model.interfaces.IAccidentViewDataModel;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IControlActionViewDataModel;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.astpa.model.interfaces.ICorrespondingSafetyConstraintDataModel;
import xstampp.astpa.model.interfaces.IDesignRequirementViewDataModel;
import xstampp.astpa.model.interfaces.IHazardViewDataModel;
import xstampp.astpa.model.interfaces.ILinkingViewDataModel;
import xstampp.astpa.model.interfaces.INavigationViewDataModel;
import xstampp.astpa.model.interfaces.ISafetyConstraintViewDataModel;
import xstampp.astpa.model.interfaces.IStatusLineDataModel;
import xstampp.astpa.model.interfaces.ISystemDescriptionViewDataModel;
import xstampp.astpa.model.interfaces.ISystemGoalViewDataModel;
import xstampp.astpa.model.interfaces.IUnsafeControlActionDataModel;
import xstampp.model.ObserverValue;

/**
 * Test class for the observers
 * 
 * @author Fabian Toth
 * 
 */
public class ObserverTest implements Observer {
	
	private List<ObserverValue> nominal;
	private List<ObserverValue> actual;
	private DataModelController dataModel;
	
	
	/**
	 * Test case for the {@link IAccidentViewDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testAccidentViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID accident = this.dataModel.addAccident("", "");
		this.addToNominal(ObserverValue.ACCIDENT);
		this.dataModel.getAccident(accident);
		this.dataModel.getAllAccidents();
		this.dataModel.setAccidentTitle(accident, "new title");
		this.dataModel.setAccidentDescription(accident, "");
		this.addToNominal(ObserverValue.ACCIDENT);
		this.dataModel.removeAccident(accident);
		this.addToNominal(ObserverValue.ACCIDENT);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link ICausalFactorDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testCausalFactorViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID component =
			this.dataModel.addComponent(this.dataModel.setRoot(new Rectangle(), ""), new Rectangle(), "",
				ComponentType.ACTUATOR, -1);
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		UUID causalFactor = this.dataModel.addCausalFactor(null);
		this.addToNominal(ObserverValue.CAUSAL_FACTOR);
		this.dataModel.setCausalFactorText(causalFactor, "");
		this.addToNominal(ObserverValue.CAUSAL_FACTOR);
		UUID hazard = this.dataModel.addHazard("", "");
		this.addToNominal(ObserverValue.HAZARD);
		this.dataModel.addCausalFactorHazardLink(causalFactor, hazard);
		this.addToNominal(ObserverValue.CAUSAL_FACTOR);
		this.dataModel.removeCausalFactorHazardLink(causalFactor, hazard);
		this.addToNominal(ObserverValue.CAUSAL_FACTOR);
		this.dataModel.setCausalSafetyConstraintText(this.dataModel.getCausalComponents().get(0).getCausalFactors()
			.get(0).getSafetyConstraint().getId(), "");
		this.addToNominal(ObserverValue.CAUSAL_FACTOR);
		this.dataModel.setNoteText(causalFactor, "");
		this.addToNominal(ObserverValue.CAUSAL_FACTOR);
		this.dataModel.getCausalComponents();
		this.dataModel.getAllHazards();
		this.dataModel.getLinkedHazardsOfCf(causalFactor);
		this.dataModel.getCorrespondingSafetyConstraints();
		this.dataModel.removeCausalFactor(causalFactor);
		this.addToNominal(ObserverValue.CAUSAL_FACTOR);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link IControlActionViewDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testControlActionViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID controlAction = this.dataModel.addControlAction("", "");
		this.addToNominal(ObserverValue.CONTROL_ACTION);
		this.dataModel.getAllControlActions();
		this.dataModel.getAllControlActionsU();
		this.dataModel.getControlAction(controlAction);
		this.dataModel.setControlActionTitle(controlAction, "");
		this.dataModel.setControlActionDescription(controlAction, "desc");
		this.addToNominal(ObserverValue.CONTROL_ACTION);
		this.dataModel.removeControlAction(controlAction);
		this.addToNominal(ObserverValue.CONTROL_ACTION);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the image paths of the {@link DataModelController}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testImagePathsObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		this.dataModel.setCSImagePath("");
		this.dataModel.setCSPMImagePath("");
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link IControlStructureEditorDataModel}
	 */
	@Test
	public void testControlStructureEditorObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID root = this.dataModel.setRoot(new Rectangle(), "");
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		UUID comp = this.dataModel.addComponent(root, new Rectangle(), "", ComponentType.CONTROLLER, -1);
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		this.dataModel.getComponent(comp);
		this.dataModel.getRoot();
		this.dataModel.changeComponentLayout(comp, new Rectangle(), true);
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		this.dataModel.changeComponentLayout(comp, new Rectangle(), false);
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		this.dataModel.synchronizeLayouts();
		this.dataModel.changeComponentText(comp, "");
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		UUID connection = this.dataModel.addConnection(new Anchor(), new Anchor(), ConnectionType.ARROW_DASHED);
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		this.dataModel.changeConnectionSource(connection, new Anchor());
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		this.dataModel.changeConnectionTarget(connection, new Anchor());
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		this.dataModel.changeConnectionType(connection, ConnectionType.ARROW_SIMPLE);
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		this.dataModel.getConnection(connection);
		this.dataModel.getConnections();
		this.dataModel.removeConnection(connection);
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		this.dataModel.removeComponent(comp);
		this.addToNominal(ObserverValue.CONTROL_STRUCTURE);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link ICorrespondingSafetyConstraintDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testCorrespondingSafetyConstraintViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID controlAction = this.dataModel.addControlAction("", "");
		this.addToNominal(ObserverValue.CONTROL_ACTION);
		UUID unsafeControlAction =
			this.dataModel.addUnsafeControlAction(controlAction, "", UnsafeControlActionType.GIVEN_INCORRECTLY);
		this.addToNominal(ObserverValue.UNSAFE_CONTROL_ACTION);
		this.dataModel.setCorrespondingSafetyConstraint(unsafeControlAction, "");
		this.addToNominal(ObserverValue.UNSAFE_CONTROL_ACTION);
		this.dataModel.getAllUnsafeControlActions();
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link IDesignRequirementViewDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testDesignRequirementViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID designRequirement = this.dataModel.addDesignRequirement("", "");
		this.addToNominal(ObserverValue.DESIGN_REQUIREMENT);
		this.dataModel.getDesignRequirement(designRequirement);
		this.dataModel.getAllDesignRequirements();
		this.dataModel.setDesignRequirementTitle(designRequirement, "");
		this.dataModel.setDesignRequirementDescription(designRequirement, "desc");
		this.addToNominal(ObserverValue.DESIGN_REQUIREMENT);
		this.dataModel.removeDesignRequirement(designRequirement);
		this.addToNominal(ObserverValue.DESIGN_REQUIREMENT);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link IHazardViewDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testHazardViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID hazard = this.dataModel.addHazard("", "");
		this.addToNominal(ObserverValue.HAZARD);
		this.dataModel.getHazard(hazard);
		this.dataModel.getAllHazards();
		this.dataModel.setHazardTitle(hazard, "desc");
		this.dataModel.setHazardDescription(hazard, "");
		this.addToNominal(ObserverValue.HAZARD);
		this.dataModel.removeHazard(hazard);
		this.addToNominal(ObserverValue.HAZARD);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link ILinkingViewDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testLinkingViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID accident = this.dataModel.addAccident("", "");
		this.addToNominal(ObserverValue.ACCIDENT);
		UUID hazard = this.dataModel.addHazard("", "");
		this.addToNominal(ObserverValue.HAZARD);
		this.dataModel.addLink(accident, hazard);
		this.addToNominal(ObserverValue.HAZ_ACC_LINK);
		this.dataModel.getAllAccidents();
		this.dataModel.getAllHazards();
		this.dataModel.getLinkedAccidents(hazard);
		this.dataModel.getLinkedHazards(accident);
		this.dataModel.deleteLink(accident, hazard);
		this.addToNominal(ObserverValue.HAZ_ACC_LINK);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link INavigationViewDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testNavigationViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		this.dataModel.getProjectName();
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link ISafetyConstraintViewDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testSafetyConstraintViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID safetyConstraint = this.dataModel.addSafetyConstraint("", "");
		this.addToNominal(ObserverValue.SAFETY_CONSTRAINT);
		this.dataModel.getAllCausalSafetyConstraints();
		this.dataModel.getCausalSafetyConstraint(safetyConstraint);
		this.dataModel.setSafetyConstraintTitle(safetyConstraint, "");
		this.dataModel.setSafetyConstraintDescription(safetyConstraint, "desc");
		this.addToNominal(ObserverValue.SAFETY_CONSTRAINT);
		this.dataModel.removeSafetyConstraint(safetyConstraint);
		this.addToNominal(ObserverValue.SAFETY_CONSTRAINT);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link IStatusLineDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testStatusLineObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		this.dataModel.getProjectName();
		this.dataModel.hasUnsavedChanges();
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link ISystemDescriptionViewDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testSystemDescriptionViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		this.dataModel.setProjectName("");
		this.addToNominal(ObserverValue.PROJECT_NAME);
		this.dataModel.setProjectDescription("desc");
		this.addToNominal(ObserverValue.PROJECT_DESCRIPTION);
		this.dataModel.getProjectDescription();
		this.dataModel.getProjectName();
		this.dataModel.addStyleRange(null);
		this.dataModel.getStyleRanges();
		this.dataModel.getStyleRangesAsArray();
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link ISystemGoalViewDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testSystemGoalViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID systemGoal = this.dataModel.addSystemGoal("", "");
		this.addToNominal(ObserverValue.SYSTEM_GOAL);
		this.dataModel.getAllSystemGoals();
		this.dataModel.getSystemGoal(systemGoal);
		this.dataModel.setSystemGoalTitle(systemGoal, "");
		this.dataModel.setSystemGoalDescription(systemGoal, "desc");
		this.addToNominal(ObserverValue.SYSTEM_GOAL);
		this.dataModel.removeSystemGoal(systemGoal);
		this.addToNominal(ObserverValue.SYSTEM_GOAL);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	/**
	 * Test case for the {@link IUnsafeControlActionDataModel}
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void testUnsafeControlActionViewObserver() {
		this.initialize();
		
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
		UUID controlAction = this.dataModel.addControlAction("", "");
		this.addToNominal(ObserverValue.CONTROL_ACTION);
		this.dataModel.getAllControlActions();
		this.dataModel.getControlActionU(controlAction);
		UUID unsafeControlAction =
			this.dataModel.addUnsafeControlAction(controlAction, "", UnsafeControlActionType.GIVEN_INCORRECTLY);
		this.addToNominal(ObserverValue.UNSAFE_CONTROL_ACTION);
		this.dataModel.setUcaDescription(unsafeControlAction, "");
		this.addToNominal(ObserverValue.UNSAFE_CONTROL_ACTION);
		UUID hazard = this.dataModel.addHazard("", "");
		this.addToNominal(ObserverValue.HAZARD);
		this.dataModel.addUCAHazardLink(unsafeControlAction, hazard);
		this.addToNominal(ObserverValue.UNSAFE_CONTROL_ACTION);
		this.dataModel.getLinkedHazardsOfUCA(unsafeControlAction);
		this.dataModel.removeUCAHazardLink(unsafeControlAction, hazard);
		this.addToNominal(ObserverValue.UNSAFE_CONTROL_ACTION);
		this.dataModel.removeUnsafeControlAction(unsafeControlAction);
		this.addToNominal(ObserverValue.UNSAFE_CONTROL_ACTION);
		Assert.assertEquals(this.nominal, this.actual);
		
		Assert.assertTrue(this.dataModel.hasUnsavedChanges());
		this.dataModel.setStored();
		Assert.assertFalse(this.dataModel.hasUnsavedChanges());
	}
	
	@Override
	public void update(Observable o, Object updatedValue) {
		this.actual.add((ObserverValue) updatedValue);
	}
	
	private void initialize() {
		this.dataModel = new DataModelController();
		this.dataModel.addObserver(this);
		this.nominal = new ArrayList<>();
		this.actual = new ArrayList<>();
	}
	
	private void addToNominal(ObserverValue updatedValue) {
		this.nominal.add(updatedValue);
		this.nominal.add(ObserverValue.UNSAVED_CHANGES);
	}
}
