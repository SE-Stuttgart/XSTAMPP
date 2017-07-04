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

import javax.xml.bind.annotation.XmlElement;

import xstampp.astpa.model.hazacc.ATableModel;

/**
 * A corresponding safety constraint
 * 
 * @author Fabian Toth
 * 
 */
public class CorrespondingSafetyConstraint extends ATableModel {

  @XmlElement
  private String text;
  
	/**
	 * Constructor of a corresponding safety constraint
	 * 
	 * @param text
	 *            the text of the new safety constraint
	 * 
	 * @author Fabian Toth
	 */
	public CorrespondingSafetyConstraint(String text) {
		super(text,null,0);
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
	public String getTitle() {
	  if(text != null) {
	    this.setTitle(text);
	    text = null;
	  }
	  return super.getTitle();
	}

  public void prepareForSave() {
    getTitle();
    setLinks(null);
  }
}
