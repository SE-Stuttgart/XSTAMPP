/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.astpa.model.controlaction.safetyconstraint;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.sds.ASafetyConstraint;

/**
 * A corresponding safety constraint
 * 
 * @author Fabian Toth
 * 
 */
public class CorrespondingSafetyConstraint extends ASafetyConstraint implements ITableModel {

	/**
	 * Constructor of a corresponding safety constraint
	 * 
	 * @param text
	 *            the text of the new safety constraint
	 * 
	 * @author Fabian Toth
	 */
	public CorrespondingSafetyConstraint(String text) {
		super(text);
	}

	/**
	 * Empty constructor used for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 */
	public CorrespondingSafetyConstraint() {
		// empty constructor for JAXB
	}

  @Override
  public int compareTo(ITableModel o) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getDescription() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getNumber() {
    return 0;
  }

  @Override
  public String getTitle() {
    // TODO Auto-generated method stub
    return null;
  }

}
