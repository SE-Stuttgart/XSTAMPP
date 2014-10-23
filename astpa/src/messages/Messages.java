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

package messages;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author Jarkko Heidenwag
 * 
 */
// CHECKSTYLE:OFF
public final class Messages extends NLS {
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(Messages.BUNDLE_NAME, Messages.class);
	}
	private static final String BUNDLE_NAME = "messages.messages"; //$NON-NLS-1$
	@SuppressWarnings("javadoc")
	public static String Accidents;
	@SuppressWarnings("javadoc")
	public static String AccidentsAvailableForLinking;
	@SuppressWarnings("javadoc")
	public static String Confirm;
	@SuppressWarnings("javadoc")
	public static String CantOverride;
	@SuppressWarnings("javadoc")
	public static String Delete_accidents;
	@SuppressWarnings("javadoc")
	public static String DotASTPA;
	@SuppressWarnings("javadoc")
	public static String DescriptionNotes;
	@SuppressWarnings("javadoc")
	public static String DoubleClickToEditTitle;
	@SuppressWarnings("javadoc")
	public static String DoYouWishToDeleteTheAccident;
	@SuppressWarnings("javadoc")
	public static String DeleteSystemGoals;
	@SuppressWarnings("javadoc")
	public static String DeleteUnsafeControlAction;
	@SuppressWarnings("javadoc")
	public static String DeleteUnsafeControlActions;
	@SuppressWarnings("javadoc")
	public static String DevelopmentTeam;
	@SuppressWarnings("javadoc")
	public static String DoYouWishToDeleteTheSystemGoal;
	@SuppressWarnings("javadoc")
	public static String ConfirmSaveAs;
	@SuppressWarnings("javadoc")
	public static String CurrentlyLinkedAccidents;
	@SuppressWarnings("javadoc")
	public static String CurrentlyLinkedHazards;
	@SuppressWarnings("javadoc")
	public static String DoYouReallyWantToOverwriteTheFile;
	@SuppressWarnings("javadoc")
	public static String ExitASTPA;
	@SuppressWarnings("javadoc")
	public static String ExportImage;
	@SuppressWarnings("javadoc")
	public static String ExportingPdf;
	@SuppressWarnings("javadoc")
	public static String ExportingCS;
	@SuppressWarnings("javadoc")
	public static String ExportingCSV;
	@SuppressWarnings("javadoc")
	public static String ExportingCSwithPM;
	@SuppressWarnings("javadoc")
	public static String ExportingUCATable;
	@SuppressWarnings("javadoc")
	public static String ExportingCFTable;
	@SuppressWarnings("javadoc")
	public static String Export;
	@SuppressWarnings("javadoc")
	public static String ExportPdf;
	@SuppressWarnings("javadoc")
	public static String ExportCS;
	@SuppressWarnings("javadoc")
	public static String ExportCSV;
	@SuppressWarnings("javadoc")
	public static String ExportCSwithPM;
	@SuppressWarnings("javadoc")
	public static String ExportUCATable;
	@SuppressWarnings("javadoc")
	public static String ExportCFTable;
	@SuppressWarnings("javadoc")
	public static String ExportPreferences;
	@SuppressWarnings("javadoc")
	public static String GivenIncorrectly;
	@SuppressWarnings("javadoc")
	public static String GridCellButton_None;
	@SuppressWarnings("javadoc")
	public static String GridWrapper_Column;
	@SuppressWarnings("javadoc")
	public static String NoUpdatesForTheCurrentInstallationHaveBeenFound;
	@SuppressWarnings("javadoc")
	public static String Tabulator;
	@SuppressWarnings("javadoc")
	public static String TheUpdateRoutineDoesNotWorkWhenASTPAIsExecutedFromTheIDE;
	@SuppressWarnings("javadoc")
	public static String RunningUpdateFromWithinEclipseIDEThisWontWork;
	@SuppressWarnings("javadoc")
	public static String UpdatesInstalledRestart;
	@SuppressWarnings("javadoc")
	public static String UpdatesHaveBeenInstalledSuccessfullyDoYouWantToRestart;
	@SuppressWarnings("javadoc")
	public static String NoUpdate;
	@SuppressWarnings("javadoc")
	public static String ReallyInstallUpdates;
	public static String RecentProjects;
	@SuppressWarnings("javadoc")
	public static String UpdateFromIDE;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfTheFollowingAccidents;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfAllSelectedAccidents;
	@SuppressWarnings("javadoc")
	public static String Information;
	@SuppressWarnings("javadoc")
	public static String NoAccidentSelected;
	@SuppressWarnings("javadoc")
	public static String DescriptionOfThisAccident;
	@SuppressWarnings("javadoc")
	public static String Links;
	@SuppressWarnings("javadoc")
	public static String Actuator;
	@SuppressWarnings("javadoc")
	public static String ASTPA;
	@SuppressWarnings("javadoc")
	public static String ThereAreUnsafedChangesDoYouWantToStoreThem;
	@SuppressWarnings("javadoc")
	public static String ThereAreUnsafedChangesDoYouWantToStoreThemAbort;
	@SuppressWarnings("javadoc")
	public static String TheWorkspaceCannotBeChangedWhen;
	@SuppressWarnings("javadoc")
	public static String StoppedTooSoon;
	@SuppressWarnings("javadoc")
	public static String Store;
	@SuppressWarnings("javadoc")
	public static String Discard;
	@SuppressWarnings("javadoc")
	public static String Abort;
	@SuppressWarnings("javadoc")
	public static String About;
	public static String AboutKontakt;
	@SuppressWarnings("javadoc")
	public static String File;
	@SuppressWarnings("javadoc")
	public static String FileFormatNotSupported;
	@SuppressWarnings("javadoc")
	public static String Filter;
	@SuppressWarnings("javadoc")
	public static String FontColor;
	@SuppressWarnings("javadoc")
	public static String Comma;
	@SuppressWarnings("javadoc")
	public static String CommonTable;
	@SuppressWarnings("javadoc")
	public static String ID;
	@SuppressWarnings("javadoc")
	public static String Title;
	@SuppressWarnings("javadoc")
	public static String TitleFont;
	@SuppressWarnings("javadoc")
	public static String ControlActionController_NotHazardous;
	@SuppressWarnings("javadoc")
	public static String ControlAction;
	@SuppressWarnings("javadoc")
	public static String ControlActions;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfTheFollowingControlActions;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfAllSelectedControlActions;
	@SuppressWarnings("javadoc")
	public static String NoControlActionSelected;
	@SuppressWarnings("javadoc")
	public static String DescriptionOfThisControlAction;
	@SuppressWarnings("javadoc")
	public static String DeleteCausalFactor;
	@SuppressWarnings("javadoc")
	public static String DeleteControlActions;
	@SuppressWarnings("javadoc")
	public static String DoYouWishToDeleteTheControlAction;
	@SuppressWarnings("javadoc")
	public static String Controller;
	@SuppressWarnings("javadoc")
	public static String CreateController;
	public static String Preferences;
	public static String PreferencesToolTip;
	@SuppressWarnings("javadoc")
	public static String PrepareDataExport;
	@SuppressWarnings("javadoc")
	public static String Preview;
	@SuppressWarnings("javadoc")
	public static String ProcessModel;
	@SuppressWarnings("javadoc")
	public static String ProcessVariable;
	@SuppressWarnings("javadoc")
	public static String ProcessValue;
	@SuppressWarnings("javadoc")
	public static String ControlledProcess;
	@SuppressWarnings("javadoc")
	public static String CreateControlledProcess;
	@SuppressWarnings("javadoc")
	public static String CreateProcessModel;
	@SuppressWarnings("javadoc")
	public static String CreateProcessVariable;
	@SuppressWarnings("javadoc")
	public static String CreateProcessValue;
	@SuppressWarnings("javadoc")
	public static String ConnectingElements;
	@SuppressWarnings("javadoc")
	public static String Arrow;
	@SuppressWarnings("javadoc")
	public static String CreateConnections;
	@SuppressWarnings("javadoc")
	public static String ManipulationObjects;
	@SuppressWarnings("javadoc")
	public static String DashedArrows;
	@SuppressWarnings("javadoc")
	public static String OffsetValue;
	@SuppressWarnings("javadoc")
	public static String Others;
	@SuppressWarnings("javadoc")
	public static String TextBox;
	@SuppressWarnings("javadoc")
	public static String CreateTextBox;
	@SuppressWarnings("javadoc")
	public static String ControlStructure;
	@SuppressWarnings("javadoc")
	public static String Company;
	@SuppressWarnings("javadoc")
	public static String Component;
	@SuppressWarnings("javadoc")
	public static String ComponentElements;
	@SuppressWarnings("javadoc")
	public static String CreateActuator;
	public static String CreateNew;
	@SuppressWarnings("javadoc")
	public static String CreateNewProject;
	@SuppressWarnings("javadoc")
	public static String SelectColor;
	@SuppressWarnings("javadoc")
	public static String SelectedNavItem;
	@SuppressWarnings("javadoc")
	public static String Semicolon;
	@SuppressWarnings("javadoc")
	public static String Sensor;
	@SuppressWarnings("javadoc")
	public static String SeperatorCharacter;
	@SuppressWarnings("javadoc")
	public static String SetValuesForTheExportFile;
	@SuppressWarnings("javadoc")
	public static String CreateSensor;
	@SuppressWarnings("javadoc")
	public static String DesignRequirements;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfTheFollowingDesignRequirements;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfAllSelectedDesignRequirements;
	@SuppressWarnings("javadoc")
	public static String NoDesignRequirementSelected;
	@SuppressWarnings("javadoc")
	public static String NoExportAvailable;
	@SuppressWarnings("javadoc")
	public static String DescriptionOfThisDesignReq;
	@SuppressWarnings("javadoc")
	public static String DeleteDesignRequirements;
	@SuppressWarnings("javadoc")
	public static String DoYouWishToDeleteTheDesignRequirement;
	@SuppressWarnings("javadoc")
	public static String HazardLinks;
	@SuppressWarnings("javadoc")
	public static String Hazards;
	@SuppressWarnings("javadoc")
	public static String HazardsAvailableForLinking;
	public static String HelpContents;
	public static String HelpToolTip;
	@SuppressWarnings("javadoc")
	public static String Hover;
	@SuppressWarnings("javadoc")
	public static String DeleteHazards;
	@SuppressWarnings("javadoc")
	public static String DoYouWishToDeleteTheHazard;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfTheFollowingHazards;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfAllSelectedHazards;
	@SuppressWarnings("javadoc")
	public static String NoHazardSelected;
	@SuppressWarnings("javadoc")
	public static String DescriptionOfThisHazard;
	@SuppressWarnings("javadoc")
	public static String LinkingView;
	@SuppressWarnings("javadoc")
	public static String RemoveAll;
	@SuppressWarnings("javadoc")
	public static String SwitchToHazards;
	@SuppressWarnings("javadoc")
	public static String SwitchToAccidents;
	@SuppressWarnings("javadoc")
	public static String Add;
	@SuppressWarnings("javadoc")
	public static String AddAll;
	@SuppressWarnings("javadoc")
	public static String AddGivenIncorrectlyUCA;
	@SuppressWarnings("javadoc")
	public static String AddingNewCausalFactor;
	@SuppressWarnings("javadoc")
	public static String AddingNewUCA;
	@SuppressWarnings("javadoc")
	public static String AddNewCausalFactor;
	@SuppressWarnings("javadoc")
	public static String AddNotGivenUCA;
	@SuppressWarnings("javadoc")
	public static String AddStoppedTooSoonUCA;
	@SuppressWarnings("javadoc")
	public static String AddWrongTimingUCA;
	@SuppressWarnings("javadoc")
	public static String All;
	@SuppressWarnings("javadoc")
	public static String Remove;
	@SuppressWarnings("javadoc")
	public static String Navigation;
	@SuppressWarnings("javadoc")
	public static String OneAnalysisFundamentals;
	@SuppressWarnings("javadoc")
	public static String Open;
	@SuppressWarnings("javadoc")
	public static String TwoUnsafeControlActions;
	@SuppressWarnings("javadoc")
	public static String UnsafeControlActions;
	@SuppressWarnings("javadoc")
	public static String UnsafeControlActionsTable;
	@SuppressWarnings("javadoc")
	public static String UnselectedNavItem;
	@SuppressWarnings("javadoc")
	public static String CorrespondingSafetyConstraints;
	@SuppressWarnings("javadoc")
	public static String ThreeCausalAnalysis;
	public static String ControlStructureDeco;
	@SuppressWarnings("javadoc")
	public static String ControlStructureDiagramWithProcessModel;
	@SuppressWarnings("javadoc")
	public static String CausalFactorsTable;
	@SuppressWarnings("javadoc")
	public static String CausalFactors;
	@SuppressWarnings("javadoc")
	public static String CausalFactorsView;
	@SuppressWarnings("javadoc")
	public static String ASTPAminus;
	@SuppressWarnings("javadoc")
	public static String SystemDescription;
	@SuppressWarnings("javadoc")
	public static String LinkingOfAccidentsAndHazards;
	@SuppressWarnings("javadoc")
	public static String SafetyConstraint;
	@SuppressWarnings("javadoc")
	public static String SafetyConstraints;
	@SuppressWarnings("javadoc")
	public static String ShowPageOnStartup;
	@SuppressWarnings("javadoc")
	public static String SnapFeedback;
	@SuppressWarnings("javadoc")
	public static String SpacePlusMouseTo;
	@SuppressWarnings("javadoc")
	public static String SplitterBackgColor;
	@SuppressWarnings("javadoc")
	public static String SplitterFontColor;
	@SuppressWarnings("javadoc")
	public static String SplitterForegColor;
	@SuppressWarnings("javadoc")
	public static String SystemGoals;
	@SuppressWarnings("javadoc")
	public static String NewProject;
	public static String NewProjectPath1;
	@SuppressWarnings("javadoc")
	public static String NewSafetyConstraint;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfTheFollowingSafetyConstraints;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfAllSelectedSafetyConstraints;
	@SuppressWarnings("javadoc")
	public static String NoSafetyConstraintSelected;
	@SuppressWarnings("javadoc")
	public static String DescriptionOfThisSafetyConstr;
	@SuppressWarnings("javadoc")
	public static String DeleteSafetyConstraints;
	@SuppressWarnings("javadoc")
	public static String DoYouWishToDeleteTheSafetyConstraint;
	@SuppressWarnings("javadoc")
	public static String ProjectName;
	@SuppressWarnings("javadoc")
	public static String ChangeColorsAnd;
	@SuppressWarnings("javadoc")
	public static String ChangeExportValues;
	@SuppressWarnings("javadoc")
	public static String CharacterAt;
	@SuppressWarnings("javadoc")
	public static String ChooseTheDestination;
	@SuppressWarnings("javadoc")
	public static String ClickToEdit;
	@SuppressWarnings("javadoc")
	public static String CloseWelcomeView;
	public static String CSDecoToolTip;
	@SuppressWarnings("javadoc")
	public static String DecreaseFontSize;
	@SuppressWarnings("javadoc")
	public static String DefaultFont;
	@SuppressWarnings("javadoc")
	public static String Bold;
	@SuppressWarnings("javadoc")
	public static String Italic;
	@SuppressWarnings("javadoc")
	public static String Underline;
	@SuppressWarnings("javadoc")
	public static String UserDotHome;
	@SuppressWarnings("javadoc")
	public static String UsuallyTheIDEStarts;
	@SuppressWarnings("javadoc")
	public static String Strikeout;
	@SuppressWarnings("javadoc")
	public static String StuPro;
	@SuppressWarnings("javadoc")
	public static String TextForeground;
	@SuppressWarnings("javadoc")
	public static String TextBackground;
	@SuppressWarnings("javadoc")
	public static String BulletList;
	@SuppressWarnings("javadoc")
	public static String BundleVersion;
	@SuppressWarnings("javadoc")
	public static String BackgroundColor;
	@SuppressWarnings("javadoc")
	public static String BackSlashSpaceOffset;
	@SuppressWarnings("javadoc")
	public static String Line;
	public static String LoadExistingProject;
	@SuppressWarnings("javadoc")
	public static String LoadProject;
	@SuppressWarnings("javadoc")
	public static String Logo;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfTheFollowingSystemGoals;
	@SuppressWarnings("javadoc")
	public static String PleaseConfirmTheDeletionOfAllSelectedSystemGoals;
	@SuppressWarnings("javadoc")
	public static String NoSystemGoalSelected;
	@SuppressWarnings("javadoc")
	public static String Note;
	@SuppressWarnings("javadoc")
	public static String NotesSlashRationale;
	@SuppressWarnings("javadoc")
	public static String NotGiven;
	@SuppressWarnings("javadoc")
	public static String NotHazardous;
	@SuppressWarnings("javadoc")
	public static String DescriptionOfThisSystemGoal;
	@SuppressWarnings("javadoc")
	public static String Destination;
	public static String loadHaz;
	public static String loadingHaz;
	public static String saveHaz;
	public static String savingHaz;
	@SuppressWarnings("javadoc")
	public static String ThisHazFileIsInvalid;
	@SuppressWarnings("javadoc")
	public static String TypeHere;
	@SuppressWarnings("javadoc")
	public static String StartTyping;
	@SuppressWarnings("javadoc")
	public static String StatusLine;
	@SuppressWarnings("javadoc")
	public static String ThereAreUnsafedChanges;
	@SuppressWarnings("javadoc")
	public static String UpdatingASTPA;
	@SuppressWarnings("javadoc")
	public static String Version;
	@SuppressWarnings("javadoc")
	public static String WantToDeleteTheCF;
	@SuppressWarnings("javadoc")
	public static String WantToDeleteTheUCA;
	@SuppressWarnings("javadoc")
	public static String Warning;
	@SuppressWarnings("javadoc")
	public static String WelcomeView;
	@SuppressWarnings("javadoc")
	public static String WrongTiming;
	@SuppressWarnings("javadoc")
	public static String ZoomItem;
	@SuppressWarnings("javadoc")
	public static String ZoomLevel;
	static {
		// initialize resource bundle
		NLS.initializeMessages(Messages.BUNDLE_NAME, Messages.class);
	}
	
	
	private Messages() {
	}
}
// CHECKSTYLE:ON
