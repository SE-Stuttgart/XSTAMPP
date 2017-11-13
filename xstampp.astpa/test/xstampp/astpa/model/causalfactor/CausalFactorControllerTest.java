package xstampp.astpa.model.causalfactor;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IValueCombie;

/**
 * Test class for the causal factor controller
 * 
 * @author Fabian Toth
 * 
 */
public class CausalFactorControllerTest {
  ICausalFactorDataModel dataModel;
  private UUID compId1;
  private UUID rootCompId;
  private UUID hazId1;
  private UUID hazId2;
  private UUID hazId3;
  private UUID scenarioId1;
  private UUID ucaId1;

  @Before
  public void setUp() throws Exception {
    DataModelController dataModel = new DataModelController();

    // Set the root for the control structure
    rootCompId = dataModel.setRoot(new Rectangle(0, 0, 0, 0), "");

    compId1 = dataModel.addComponent(rootCompId, new Rectangle(0, 0, 0, 0), "", ComponentType.CONTROLLER, -1);
    hazId1 = dataModel.addHazard("Title 1", "Description 1");
    hazId2 = dataModel.addHazard("Title 2", "Description 2");
    hazId3 = dataModel.addHazard("", "");
    ucaId1 = ((DataModelController) dataModel).addUnsafeControlAction(UUID.randomUUID(), "",
        UnsafeControlActionType.GIVEN_INCORRECTLY);
    AbstractLtlProviderData data = new AbstractLtlProviderData();
    data.addRelatedUcas(ucaId1);
    scenarioId1 = dataModel.addRuleEntry(ScenarioType.CAUSAL_SCENARIO, new AbstractLtlProviderData(), null,
        IValueCombie.TYPE_ANYTIME);
    this.dataModel = dataModel;
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test for the linking mechanism of the causal factor controller
   * 
   * @author Fabian Toth
   * 
   */
  @Test
  public void testLinking() {

    // Try to create a link before the root of the control structure has
    // been set

  }

  /**
   * Tests the validation of the CausalFactorController
   * 
   * @author Fabian Toth
   * 
   */
  @Test
  public void testNotValid() {

    // CausalFactors
    UUID factor = dataModel.addCausalFactor();
    Assert.assertNotNull(factor);
    Assert.assertNull(dataModel.addCausalFactor((UUID) null));
    Assert.assertFalse(dataModel.getCausalFactorController().setCausalFactorText(null, null));
    Assert.assertFalse(dataModel.getCausalFactorController().setCausalFactorText(UUID.randomUUID(), ""));

  }
}
