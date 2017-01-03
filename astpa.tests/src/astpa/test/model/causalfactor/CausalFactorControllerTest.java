package astpa.test.model.causalfactor;

import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.haz.controlaction.UnsafeControlActionType;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.interfaces.CausalFactorEntryData;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.RuleType;
import xstampp.astpa.model.sds.interfaces.ISafetyConstraint;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IEntryFilter;
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
    ucaId1 =((DataModelController)dataModel).addUnsafeControlAction(UUID.randomUUID(), "", UnsafeControlActionType.GIVEN_INCORRECTLY);
    AbstractLtlProviderData data = new AbstractLtlProviderData();
    data.addRelatedUcas(ucaId1);
    scenarioId1 = dataModel.addRuleEntry(RuleType.SCENARIO, new AbstractLtlProviderData()
                                      , null, IValueCombie.TYPE_ANYTIME);
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
	  UUID cf1Id = dataModel.addCausalFactor(compId1);
		Assert.assertNotNull(cf1Id);
		UUID entry1Id = dataModel.addCausalHazardEntry(compId1, cf1Id);
		CausalFactorEntryData data = new CausalFactorEntryData(entry1Id);
    data.addHazardId(hazId1);
    data.addHazardId(UUID.randomUUID());
		Assert.assertTrue(dataModel.changeCausalEntry(compId1, cf1Id, data));

		ICausalComponent comp =dataModel.getCausalComponent(compId1);
    for(ICausalFactor factor: comp.getCausalFactors()){
      if(factor.getId().equals(cf1Id)){
        for(ICausalFactorEntry entry : factor.getAllEntries()){
          Assert.assertEquals(1, dataModel.getHazards(entry.getHazardIds()).size());
          ITableModel model = dataModel.getHazards(entry.getHazardIds()).get(0);
          Assert.assertEquals(entry1Id, entry.getId());
          Assert.assertEquals(hazId1, model.getId());
        }
      }
    }
    
	}
	
	
	@Test
	public void testUCALinking(){

    UUID cf1Id = dataModel.addCausalFactor(compId1);
    UUID ucaEntryId = dataModel.addCausalUCAEntry(compId1, cf1Id, ucaId1);
    Assert.assertNotNull(ucaEntryId);
    ICausalComponent causalComp = dataModel.getCausalComponent(compId1);
    for(ICausalFactor factor: causalComp.getCausalFactors()){
      Assert.assertEquals(cf1Id, factor.getId());
      ICausalFactorEntry entry = factor.getAllEntries().get(0);
      Assert.assertEquals(ucaEntryId, entry.getId());
      Assert.assertEquals(ucaId1, entry.getUcaLink());
      Assert.assertNull(entry.getHazardIds());
    }
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
		Assert.assertNull(dataModel.addCausalFactor((IRectangleComponent)null));
		Assert.assertNull(dataModel.addCausalFactor((UUID)null));
    Assert.assertFalse(dataModel.setCausalFactorText(null, null, null));
    Assert.assertFalse(dataModel.setCausalFactorText(UUID.randomUUID(), UUID.randomUUID(), ""));
    Assert.assertFalse(dataModel.changeCausalEntry(hazId1, compId1, null));
    
    UUID factorId = dataModel.addCausalFactor(compId1);
    UUID entryId = dataModel.addCausalHazardEntry(compId1, factorId);
    Assert.assertNotNull(entryId);
    Assert.assertFalse(dataModel.changeCausalEntry(compId1, factorId, new CausalFactorEntryData(entryId)));
		
		
	}
}
