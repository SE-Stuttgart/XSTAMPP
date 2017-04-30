/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.ui.navigation.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * An interface providing a Map to create a dynamic step entry in the project explorer.
 * 
 * @author Lukas Balzer
 */
public interface IDynamicStepsProvider {
  
  /**
   * This should produce a list containing all entries that should be
   * available in the step list with a name and a Identifier.
   * The UUID is given to the openStepCommand as argument.
   * @param projectId The id of the project for that the list should be
   * @return a List containing the step property map mapped to the unique title of the step
   */
  List<DynamicDescriptor> getStepMap(UUID projectId);
  
  class DynamicDescriptor {
    String name;
    Map<String, Object> properties;
    
    public DynamicDescriptor(String name, Map<String, Object> properties) {
      this.name = name;
      this.properties = properties;
    }
    
    public Map<String, Object> getProperties() {
      return properties;
    }
    
    public String getName() {
      return name;
    }
  }
}
