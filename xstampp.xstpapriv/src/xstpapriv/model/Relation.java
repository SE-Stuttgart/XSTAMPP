/*******************************************************************************
 * Copyright (c) 2013, 2015 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstpapriv.model;

import java.util.ArrayList;
import java.util.List;

public class Relation {
	
	// ==================== 2. Instance Fields ============================
	
	private int strength;
	private List<String> variables = new ArrayList<String>();

	// ==================== 4. Constructors ===============================
	
	public Relation(int strength, List<String> variables) {
		this.setStrength(strength);
		this.setVariables(variables);
	}
	
	// ==================== 5. Creators =============================

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> controlActions) {
		this.variables = controlActions;
	}
}
