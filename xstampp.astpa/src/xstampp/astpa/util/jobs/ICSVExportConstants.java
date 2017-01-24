/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.util.jobs;

import java.util.ArrayList;

import messages.Messages;

/**
 * @since 2.0
 */
public interface ICSVExportConstants {
	
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final ArrayList<String> STEPS = new ArrayList<String>(){

		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			this.add(Messages.SystemDescription);
			this.add(Messages.Accidents);
			this.add(Messages.Hazards);
			this.add(Messages.SafetyConstraints);
			this.add(Messages.SystemGoals);
			this.add(Messages.DesignRequirements);
			this.add(Messages.ControlActions);
			this.add(Messages.CorrespondingSafetyConstraints);
			this.add(Messages.UnsafeControlActionsTable);
			this.add(Messages.CausalFactorsTable);
		}
		
	};
	
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int PROJECT_DESCRIPTION = 1 << 0;
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int ACCIDENT = 1 << 1;
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int HAZARD = 1 << 2;
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int SAFETY_CONSTRAINT = 1 << 3;
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int SYSTEM_GOAL = 1 << 4;
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int DESIGN_REQUIREMENT = 1 << 5;
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int CONTROL_ACTION = 1 << 6;
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int CORRESPONDING_SAFETY_CONSTRAINTS = 1 << 7;
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int UNSAFE_CONTROL_ACTION = 1 << 8;
	/**
	 * 
	 * @author Lukas Balzer
	 */
	public static final int CAUSAL_FACTOR = 1 << 9;
}
