package astpa.test.model;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IValueCombie;

public class ExtendedDataModelTest {
  IExtendedDataController dataModel;
  private UUID controlAction;
  @Before
  public void setUp() throws Exception {
    DataModelController controller = new DataModelController();
    controlAction = controller.addControlAction("Control Action 1", "");
    dataModel = controller;
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    //first the method parameter handling is tested 
    Assert.assertNull(dataModel.addRuleEntry(ScenarioType.CAUSAL_SCENARIO, null,controlAction , ""));
    Assert.assertNull(dataModel.addRuleEntry(null,  new AbstractLtlProviderData(),controlAction , ""));
    Assert.assertNull(dataModel.addRuleEntry(null,  new AbstractLtlProviderData(),null , ""));
    UUID scenario1Id = dataModel.addRuleEntry(ScenarioType.CAUSAL_SCENARIO, new AbstractLtlProviderData(),controlAction , IValueCombie.TYPE_ANYTIME);
    Assert.assertNotNull(scenario1Id);
    UUID rule1Id1 = dataModel.addRuleEntry(ScenarioType.BASIC_SCENARIO, new AbstractLtlProviderData(),controlAction , IValueCombie.TYPE_ANYTIME);
    Assert.assertNotNull(rule1Id1);
    UUID ltl1Id = dataModel.addRuleEntry(ScenarioType.CUSTOM_LTL, new AbstractLtlProviderData(),controlAction , IValueCombie.TYPE_ANYTIME);
    Assert.assertNotNull(ltl1Id);
    Assert.assertEquals(1, dataModel.getAllRefinedRules(true, false, false).size());
    Assert.assertEquals(1, dataModel.getAllRefinedRules(false, true, false).size());
    Assert.assertEquals(1, dataModel.getAllRefinedRules(false, false, true).size());
    Assert.assertEquals(3, dataModel.getAllRefinedRules(true, true, true).size());
    
  }

}
