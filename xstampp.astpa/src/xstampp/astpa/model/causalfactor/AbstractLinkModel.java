/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.model.causalfactor;

import java.util.UUID;

import xstampp.astpa.haz.causalfactor.ICausalFactorHazardLink;

public abstract class AbstractLinkModel {

  public AbstractLinkModel() {
    super();
  }

  protected abstract UUID getValueId();

  protected abstract UUID getKeyId();

  /**
   * Checks if the given id is in this link
   * 
   * @author Fabian Toth
   * 
   * @param id
   *            the id to check
   * @return id the given id is in this link
   */
  public boolean containsId(UUID id) {
  	return this.getKeyId().equals(id) || this.getValueId().equals(id);
  }

  @Override
  public int hashCode() {
  	final int prime = 31;
  	int result = 1;
  	result = (prime * result)
  			+ ((getKeyId() == null) ? 0 : getKeyId()
  					.hashCode());
  	result = (prime * result)
  			+ ((getValueId() == null) ? 0 : getValueId().hashCode());
  	return result;
  }

  @Override
  public boolean equals(Object obj) {
  	if (this == obj) {
  		return true;
  	}
  	if (obj == null) {
  		return false;
  	}
  	if (this.getClass() != obj.getClass()) {
  		return false;
  	}
  	CausalFactorHazardLink other = (CausalFactorHazardLink) obj;
  	if (getKeyId() == null) {
  		if (other.getKeyId() != null) {
  			return false;
  		}
  	} else if (!getKeyId().equals(other.getKeyId())) {
  		return false;
  	}
  	if (getValueId() == null) {
  		if (other.getValueId() != null) {
  			return false;
  		}
  	} else if (!this.getValueId().equals(other.getValueId())) {
  		return false;
  	}
  	return true;
  }

}