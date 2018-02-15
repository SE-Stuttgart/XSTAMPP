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
package xstampp.astpa.model.linking;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import xstampp.astpa.model.TestObserver;
import xstampp.model.ObserverValue;

public class TestLinkController extends TestObserver {
  private List<UUID> ids;

  @Before
  public void setUp() throws Exception {
    this.ids = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      this.ids.add(UUID.randomUUID());
    }
  }

  @Test
  public final void testDeleteLinksForUUID() {
    Link ucaCfLink = new Link(this.ids.get(0), this.ids.get(1), LinkingType.UCA_CausalFactor_LINK);
    Link ucaEntryLink = new Link(ucaCfLink.getId(), this.ids.get(3), LinkingType.UcaCfLink_Component_LINK);
    LinkController linkController = new LinkController(Arrays.asList(ucaCfLink, ucaEntryLink));
    assertTrue(linkController.getLinkMapSize() == 2);
    linkController.deleteLinksFor(this.ids.get(0), 2);
    assertTrue(linkController.getLinkMapSize() == 0);
  }

  @Test
  public final void testAddLinks() {
    LinkController linkController = new LinkController();
    linkController.addObserver(getCleanObserver());
    linkController.addLink(LinkingType.UCA_CausalFactor_LINK, this.ids.get(0), null);
    linkController.addLink(LinkingType.UCA_CausalFactor_LINK, this.ids.get(0), this.ids.get(2));
    linkController.addLink(LinkingType.UCA_CausalFactor_LINK, this.ids.get(1), this.ids.get(2));
    assertTrue(hasUpdates(Arrays.asList(ObserverValue.LINKING, ObserverValue.LINKING, ObserverValue.LINKING)));
    assertTrue(linkController.getLinkMapSize() == 1);
  }
}
