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
package xstpapriv;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "xstpapriv.messages"; //$NON-NLS-1$
	public static String AddsNewTableEntry;
	public static String ChecksTableForConflicts;
	public static String ContextTableContext_NotProvided;
	public static String ContextTableContext_Provided;
	public static String ContextTableFilter_All;
	public static String ContextTableFilter_Hazardous;
	public static String ContextTableFilter_NotHazardous;
	public static String DeletesRow;
	public static String GenerateNewTableMsg;
	public static String KeepRules;
	public static String LinkSomeVariables;
	public static String LinkVariablesMessage;
	public static String NoLinkedVariables;
	public static String NoLinkedVariablesMsg;
	public static String NOTestsetsAvailable;
	public static String OpensSettingsACTS;
	public static String Param_ConflictsMsg;
	public static String PreserveOldRules;
	public static String RefinedRulesTable_ConfirmDelete;
	public static String RefinedRulesTable_DeleteAll;
	public static String RefinedRulesTable_EditUCALinks;
	public static String RefinedRulesTable_Export;
	public static String RefinedRulesTable_NoHazardousCombies;
	public static String RefinedRulesTable_ReallyDeleteRefinedSafety;
	public static String RefinedRulesTable_Remove;
	public static String RefinedRulesTable_RemoveAll;
	public static String RefinedRulesTable_Type;
	public static String SelectAControlAction;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
