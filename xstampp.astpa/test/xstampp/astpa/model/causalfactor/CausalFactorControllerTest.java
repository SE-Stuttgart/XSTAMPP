package xstampp.astpa.model.causalfactor;

import java.util.Arrays;
import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.TestObserver;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.model.ObserverValue;

/**
 * Test class for the causal factor controller
 * 
 * @author Fabian Toth
 * 
 */
public class CausalFactorControllerTest extends TestObserver {

  @Test
  public void testAddCausalFactor() {
    DataModelController dataModel = new DataModelController(true);
    dataModel.addObserver(getCleanObserver());
    UUID factor = dataModel.getCausalFactorController().addCausalFactor();
    Assert.assertNotNull(factor);
    Assert.assertNotNull(dataModel.getCausalFactorController().getCausalFactor(factor));
    Assert.assertTrue(hasUpdates(Arrays.asList(ObserverValue.CAUSAL_FACTOR)));
  }

  @Test
  public void testRemoveCausalFactor() {
    DataModelController dataModel = new DataModelController(true);
    dataModel.addObserver(getCleanObserver());
    UUID factor = dataModel.getCausalFactorController().addCausalFactor();
    Assert.assertNotNull(dataModel.getCausalFactorController().getCausalFactor(factor));
    dataModel.addObserver(getCleanObserver());
    Assert.assertTrue(dataModel.getCausalFactorController().removeCausalFactor(factor));
    Assert.assertTrue(hasUpdates(Arrays.asList(ObserverValue.CAUSAL_FACTOR)));
  }

  @Test
  public void testAddCausalFactorComponentUUIDValid() {
    DataModelController dataModel = new DataModelController(true);
    dataModel.addObserver(getCleanObserver());
    UUID componentId = dataModel.addComponent(dataModel.getRoot().getId(), new Rectangle(), "",
        ComponentType.CONTROLLER, 1);
    UUID factor = dataModel.addCausalFactor(componentId);
    Assert.assertNotNull(dataModel.getCausalFactorController().getCausalFactor(factor));
    Assert.assertTrue(hasUpdates(Arrays.asList(ObserverValue.CAUSAL_FACTOR, ObserverValue.UCA_CausalFactor_LINK,
        ObserverValue.UcaCfLink_Component_LINK)));
  }

  @Test
  public void testAddCausalFactorComponentUUIDInvalid() {
    DataModelController dataModel = new DataModelController(true);
    UUID invalidComponentId = dataModel.addComponent(dataModel.getRoot().getId(), new Rectangle(), "",
        ComponentType.TEXTFIELD, 1);
    dataModel.addObserver(getCleanObserver());
    UUID factor = dataModel.addCausalFactor(invalidComponentId);
    Assert.assertNull(dataModel.getCausalFactorController().getCausalFactor(factor));
    Assert.assertTrue(hasNoUpdates());
  }

  @Test
  public void testAddCausalFactorComponentUUIDNull() {
    DataModelController dataModel = new DataModelController(true);
    dataModel.addObserver(getCleanObserver());
    Assert.assertNull(dataModel.addCausalFactor((UUID) null));
    Assert.assertTrue(hasNoUpdates());
  }

  @Test
  public void testSetCausalFactorText() {
    DataModelController dataModel = new DataModelController(true);
    UUID factor = dataModel.getCausalFactorController().addCausalFactor();
    dataModel.addObserver(getCleanObserver());
    Assert.assertFalse(dataModel.getCausalFactorController().setCausalFactorText(null, null));
    Assert.assertFalse(dataModel.getCausalFactorController().setCausalFactorText(UUID.randomUUID(), ""));
    Assert.assertTrue(dataModel.getCausalFactorController().setCausalFactorText(factor, "Test"));
    Assert.assertTrue(hasUpdates(Arrays.asList(ObserverValue.CAUSAL_FACTOR)));
  }

  @Test
  public void testAddSafetyConstraint() {
    DataModelController dataModel = new DataModelController(true);
    dataModel.addObserver(getCleanObserver());
    UUID constraint = dataModel.getCausalFactorController().addSafetyConstraint("Test");
    Assert.assertNotNull(constraint);
    Assert.assertNotNull(dataModel.getCausalFactorController().getSafetyConstraint(constraint));
    Assert.assertTrue(hasUpdates(Arrays.asList(ObserverValue.CAUSAL_FACTOR)));
  }

  @Test
  public void testRemoveSafetyConstraint() {
    DataModelController dataModel = new DataModelController(true);
    dataModel.addObserver(getCleanObserver());
    UUID constraint = dataModel.getCausalFactorController().addSafetyConstraint("Test");
    dataModel.addObserver(getCleanObserver());
    Assert.assertNotNull(constraint);
    Assert.assertNotNull(dataModel.getCausalFactorController().removeSafetyConstraint(constraint));
    Assert.assertTrue(hasUpdates(Arrays.asList(ObserverValue.CAUSAL_FACTOR)));
  }
}
