/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.update;

import java.security.cert.Certificate;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.UIServices;

/**
 * Class that overrides the UIServices so that the security warning dialog won't
 * show
 * 
 * @author Fabian Toth
 * 
 */
public class ValidationDialogService extends UIServices {

  @Override
  public AuthenticationInfo getUsernamePassword(String location) {
    // nothing yet
    return null;
  }

  @Override
  public AuthenticationInfo getUsernamePassword(String location, AuthenticationInfo previousInfo) {
    // nothing yet
    return null;
  }

  @Override
  public TrustInfo getTrustInfo(Certificate[][] untrustedChain, String[] unsignedDetail) {
    boolean trustUnsigned = true;
    boolean persistTrust = true;

    Certificate[] trusted = new Certificate[0];
    TrustInfo trustInfo = new TrustInfo(trusted, trustUnsigned, persistTrust);
    return trustInfo;
  }

  /**
   * This method will override the default UIServices instance within the
   * provisioning agent. This will prevent the blocking "...you're installing
   * untrusted content..." dialog to appear.
   * 
   * @param agent
   *          The P2 provisioning agent.
   */
  public void bindProvisioningAgent(IProvisioningAgent agent) {
    agent.registerService(UIServices.SERVICE_NAME, this);
  }

}
