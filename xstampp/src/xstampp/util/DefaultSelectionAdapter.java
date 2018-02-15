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
package xstampp.util;

import java.util.function.Consumer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DefaultSelectionAdapter extends SelectionAdapter {

  private Consumer<SelectionEvent> consumer;

  public DefaultSelectionAdapter(Consumer<SelectionEvent> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void widgetSelected(SelectionEvent e) {
    consumer.accept(e);
  }

}
