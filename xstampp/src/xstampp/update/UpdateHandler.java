/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import messages.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.ServiceReference;

import xstampp.Activator;

/**
 * Handles the update routine
 * 
 * @author Fabian Toth
 * 
 */
public class UpdateHandler extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    ServiceReference<?> reference = Activator.getContext().getServiceReference(IProvisioningAgent.SERVICE_NAME);
    final IProvisioningAgent agent = (IProvisioningAgent) Activator.getContext().getService(reference);
    Activator.getContext().ungetService(reference);
    UpdateJob j = new UpdateJob(Messages.UpdatingASTPA, agent, Display.getCurrent().getActiveShell(), false);
    j.schedule();
    return null;
  }
}
