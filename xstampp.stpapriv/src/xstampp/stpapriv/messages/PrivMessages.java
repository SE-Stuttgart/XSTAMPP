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

package xstampp.stpapriv.messages;

import org.eclipse.osgi.util.NLS;


// CHECKSTYLE:OFF
public final class PrivMessages extends NLS{

	private static final String BUNDLE_NAME = "xstampp.stpapriv.messages.privmessages"; //$NON-NLS-1$


	
	public static String Delete_losses;
	public static String DoYouWishToDeleteTheLoss;
	public static String ConfirmTheDeletionOfTheFollowingLosses;
	public static String ConfirmTheDeletionOfAllSelectedLosses;
	public static String NoLossSelected;
	public static String DescriptionOfThisLoss;
	public static String DeleteVulnerabilities;
	public static String DoYouWishToDeleteTheVulnerability;
	public static String ConfirmTheDeletionOfTheFollowingVulnerabilities;
	public static String ConfirmTheDeletionOfAllSelectedVulnerabilities;
	public static String NoVulnerabilitySelected;
	public static String DescriptionOfThisVulnerability;
	public static String SwitchToVulnerabilities;
	public static String SwitchToLosses;
	public static String TwoUnsecureControlActions;
	public static String ControlActionController_NotVulnerable;
	public static String UnsecureControlActions;
	public static String CorrespondingSecurityConstraints;
	public static String Losses;
	public static String Results;
	public static String LossesAvailableForLinking;
	public static String VulnerabilityLinks;
	public static String Vulnerabilities;
	public static String VulnerabilitiesAvailableForLinking;
	public static String SecDesc;
	public static String LinkingOfLossesAndVulnerabilities;
	public static String SecurityConstraint;
	public static String SecurityConstraints;
	public static String ConfirmTheDeletionOfTheFollowingSecurityConstraints;
	public static String ConfirmTheDeletionOfAllSelectedSecurityConstraints;
	public static String NoSecurityConstraintSelected;
	public static String DescriptionOfThisSecurityConstr;
	public static String DeleteSecurityConstraints;
	public static String DoYouWishToDeleteTheSecurityConstraint;
	public static String NewSecurityConstraint;
	public static String NotGiven2;
	public static String NotVulnerable;
	public static String DeleteUnsecureControlAction;
	public static String DeleteUnsecureControlActions;
	public static String UnsecureControlActionsTable;
	public static String CurrentlyLinkedLosses;
	public static String CurrentlyLinkedVulnerabilities;
	public static String ExportingCSC2;
	public static String ExportingUCATable2;
	public static String ExportingRefinedSecurity;
	public static String ExportUCATable2;
	public static String GivenIncorrectly2;
	public static String UpdatingSTPASEC;
	public static String TheUpdateRoutineDoesNotWorkWhenSTPASECIsExecutedFromTheIDE;
	public static String UpdatesHaveBeenInstalledSuccessfullyDoYouWantToRestart2;
	public static String RefinedSecurityConstraintsTable;
	public static String RefinedUnsecureControlActions;
	public static String UsuallyTheIDEStarts2;
	public static String WantToDeleteTheUCA2;
	public static String WrongTiming2;
	public static String saveSec;
	public static String savingSec;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, PrivMessages.class);
	}

	private PrivMessages() {
	}
}
// CHECKSTYLE:ON
