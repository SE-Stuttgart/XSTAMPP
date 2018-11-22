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
package xstampp.astpa.model;

import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.ScenarioType;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IValueCombie;

public class ExtendedDataModelTest {
  IExtendedDataModel dataModel;
  private UUID controlAction;
  @Before
  public void setUp() throws Exception {
    DataModelController controller = new DataModelController();
    controlAction = controller.getControlActionController().addControlAction("Control Action 1", "");
    dataModel = controller;
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    //first the method parameter handling is tested 
    Assert.assertNull(dataModel.getExtendedDataController().addRuleEntry(ScenarioType.CAUSAL_SCENARIO, null,controlAction , "", dataModel.getLinkController()));
    Assert.assertNull(dataModel.getExtendedDataController().addRuleEntry(null,  new AbstractLtlProviderData(),controlAction , "", dataModel.getLinkController()));
    Assert.assertNull(dataModel.getExtendedDataController().addRuleEntry(null,  new AbstractLtlProviderData(),null , "", dataModel.getLinkController()));
    UUID scenario1Id = dataModel.getExtendedDataController().addRuleEntry(ScenarioType.CAUSAL_SCENARIO, new AbstractLtlProviderData(),controlAction , IValueCombie.TYPE_ANYTIME, dataModel.getLinkController());
    Assert.assertNotNull(scenario1Id);
    UUID rule1Id1 = dataModel.getExtendedDataController().addRuleEntry(ScenarioType.BASIC_SCENARIO, new AbstractLtlProviderData(),controlAction , IValueCombie.TYPE_ANYTIME, dataModel.getLinkController());
    Assert.assertNotNull(rule1Id1);
    UUID ltl1Id = dataModel.getExtendedDataController().addRuleEntry(ScenarioType.CUSTOM_LTL, new AbstractLtlProviderData(),controlAction , IValueCombie.TYPE_ANYTIME, dataModel.getLinkController());
    Assert.assertNotNull(ltl1Id);
    Assert.assertEquals(1, dataModel.getExtendedDataController().getAllScenarios(true, false, false).size());
    Assert.assertEquals(1, dataModel.getExtendedDataController().getAllScenarios(false, true, false).size());
    Assert.assertEquals(1, dataModel.getExtendedDataController().getAllScenarios(false, false, true).size());
    Assert.assertEquals(3, dataModel.getExtendedDataController().getAllScenarios(true, true, true).size());
    
  }

}
