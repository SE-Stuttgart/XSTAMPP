/*******************************************************************************
 * Copyright (c) 2013-2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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
  // *-------A-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String Accidents;
  @SuppressWarnings("javadoc")
  public static String Actuator;
  @SuppressWarnings("javadoc")
  public static String AccidentsAvailableForLinking;
  @SuppressWarnings("javadoc")
  public static String Abort;
  @SuppressWarnings("javadoc")
  public static String About;
  @SuppressWarnings("javadoc")
  public static String AboutKontakt;
  public static String AbstractExportPage_4;
  public static String AbstractExportPage_5;
  public static String AbstractExportPage_excludeInExport;
  public static String AbstractExportPage_ExportInA4LandscapeFormat;
  public static String AbstractExportPage_ExportInA4PortraitFormat;
  public static String AbstractExportPage_ExportTheData;
  public static String AbstractExportPage_includeInExport;
  public static String AbstractExportPage_SampleText;
  public static String AbstractFilteredEditor_CategoryToolTip;
  public static String AbstractFilteredEditor_ClearFilter;
  public static String AbstractFilteredEditor_FilterDropDowmToolTip;
  public static String AbstractFilteredEditor_FilterLabel;
  public static String AbstractFilteredEditor_FilterTextToolTip;
  @SuppressWarnings("javadoc")
  public static String Arrow;
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
  public static String ApplicationWorkbenchAdvisor_Load_Projects;
  public static String ApplicationWorkbenchWindowAdvisor_Unfinished_Jobs_Message;
  public static String ApplicationWorkbenchWindowAdvisor_Unfinished_Jobs_Short;
  public static String ApplicationWorkbenchWindowAdvisor_Unfinished_Jobs_Title;
  public static String AsDataSet;
  public static String AsImage;
  public static String AsPDF;
  @SuppressWarnings("javadoc")
  public static String ASTPAminus;
  // *-------B-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String Bold;
  @SuppressWarnings("javadoc")
  public static String BulletList;
  @SuppressWarnings("javadoc")
  public static String BundleVersion;
  @SuppressWarnings("javadoc")
  public static String BackgroundColor;
  @SuppressWarnings("javadoc")
  public static String BackSlashSpaceOffset;
  /*
   * -------C-------------------------------------------------------------------
   * *
   */
  @SuppressWarnings("javadoc")
  public static String CantOverride;
  @SuppressWarnings("javadoc")
  public static String CannotCreateDir;
  @SuppressWarnings("javadoc")
  public static String CausalFactorsTable;
  @SuppressWarnings("javadoc")
  public static String CausalFactors;
  @SuppressWarnings("javadoc")
  public static String CausalFactorsView;
  @SuppressWarnings("javadoc")
  public static String ChangeColorsAnd;
  @SuppressWarnings("javadoc")
  public static String ChangeExportValues;
  @SuppressWarnings("javadoc")
  public static String CharacterAt;
  @SuppressWarnings("javadoc")
  public static String ChooseProjectForExport;
  @SuppressWarnings("javadoc")
  public static String ChooseTheDestination;
  @SuppressWarnings("javadoc")
  public static String Comma;
  @SuppressWarnings("javadoc")
  public static String CommonTable;
  public static String ContextTables;
  public static String Exporting_Format;
  public static String ExportingContextTables;
  @SuppressWarnings("javadoc")
  public static String Controller;
  @SuppressWarnings("javadoc")
  public static String CreateControlAction;
  @SuppressWarnings("javadoc")
  public static String CreateController;
  @SuppressWarnings("javadoc")
  public static String ControlActionController_NotHazardous;
  @SuppressWarnings("javadoc")
  public static String ControlAction;
  @SuppressWarnings("javadoc")
  public static String ControlActions;
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
  public static String CreateConnections;
  @SuppressWarnings("javadoc")
  public static String Confirm;
  @SuppressWarnings("javadoc")
  public static String ConfirmSaveAs;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfTheFollowingAccidents;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfAllSelectedAccidents;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfTheFollowingControlActions;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfAllSelectedControlActions;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfTheFollowingDesignRequirements;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfAllSelectedDesignRequirements;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfTheFollowingHazards;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfAllSelectedHazards;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfTheFollowingSafetyConstraints;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfAllSelectedSafetyConstraints;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfTheFollowingSystemGoals;
  @SuppressWarnings("javadoc")
  public static String ConfirmTheDeletionOfAllSelectedSystemGoals;
  @SuppressWarnings("javadoc")
  public static String CurrentlyLinkedAccidents;
  @SuppressWarnings("javadoc")
  public static String CurrentlyLinkedHazards;
  @SuppressWarnings("javadoc")
  public static String CreateTextBox;
  @SuppressWarnings("javadoc")
  public static String ControlStructure;
  @SuppressWarnings("javadoc")
  public static String ControlStructure_DecoOff;
  @SuppressWarnings("javadoc")
  public static String ControlStructure_DecoOn;
  @SuppressWarnings("javadoc")
  public static String Company;
  @SuppressWarnings("javadoc")
  public static String Component;
  @SuppressWarnings("javadoc")
  public static String ComponentElements;
  @SuppressWarnings("javadoc")
  public static String CreateActuator;
  @SuppressWarnings("javadoc")
  public static String CreateDashedBox;
  @SuppressWarnings("javadoc")
  public static String CreateNew;
  @SuppressWarnings("javadoc")
  public static String CreateNewProject;
  @SuppressWarnings("javadoc")
  public static String CreateSensor;
  @SuppressWarnings("javadoc")
  public static String CorrespondingSafetyConstraints;
  @SuppressWarnings("javadoc")
  public static String ControlStructureDeco;
  @SuppressWarnings("javadoc")
  public static String ControlStructureDiagramWithProcessModel;
  @SuppressWarnings("javadoc")
  public static String ControlStructureFontColor;
  @SuppressWarnings("javadoc")
  public static String ClickToEdit;
  @SuppressWarnings("javadoc")
  public static String CSDecoToolTip;
  @SuppressWarnings("javadoc")
  public static String CloseWelcomeView;
  /*
   * -------D-------------------------------------------------------------------
   * *
   */
  @SuppressWarnings("javadoc")
  public static String Delete_accidents;
  @SuppressWarnings("javadoc")
  public static String DescriptionOfThisDesignReq;
  @SuppressWarnings("javadoc")
  public static String DeleteDesignRequirements;
  @SuppressWarnings("javadoc")
  public static String DeleteFailedMsg;
  @SuppressWarnings("javadoc")
  public static String DirDoesNotExist;
  @SuppressWarnings("javadoc")
  public static String DirIsNotReadable;
  @SuppressWarnings("javadoc")
  public static String Discard;
  public static String DontPromtThisMsgAgain;
  @SuppressWarnings("javadoc")
  public static String DotASTPA;
  @SuppressWarnings("javadoc")
  public static String DescriptionOfThisAccident;
  @SuppressWarnings("javadoc")
  public static String DescriptionOfThisSystemGoal;
  @SuppressWarnings("javadoc")
  public static String Destination;
  @SuppressWarnings("javadoc")
  public static String DescriptionNotes;
  @SuppressWarnings("javadoc")
  public static String DoubleClickToEditTitle;
  @SuppressWarnings("javadoc")
  public static String DoYouWantToCreateIt;
  @SuppressWarnings("javadoc")
  public static String DoYouWishToDeleteTheAccident;
  @SuppressWarnings("javadoc")
  public static String DescriptionOfThisControlAction;
  @SuppressWarnings("javadoc")
  public static String DeleteCausalFactor;
  @SuppressWarnings("javadoc")
  public static String DeleteControlActions;
  /**
   * A string that asks the user to confirm the deletion of one or more
   * entries inserted as
   * <br/>
   * <b><i>%s</i></b>: (e.g. a 'control action', 'design requirement')
   * <p/>
   * <code>
   * Do you really want to delete the <i>Control Action</i>:<br>
   * <span style="margin-left:50px">CA 1
   * </ul></code>
   */
  public static String DeleteQuestionMask;

  /**
   * A string that asks the user to confirm the deletion of one or more
   * entries inserted as
   * <br/>
   * <b><i>%s</i></b>: (e.g. a 'control action', 'design requirement')
   * <p/>
   * <code>Delete <i>Control Action</i></code>
   */
  public static String DeleteMask;
  @SuppressWarnings("javadoc")
  public static String DoYouWishToDeleteTheControlAction;
  @SuppressWarnings("javadoc")
  public static String DeleteSystemGoals;
  @SuppressWarnings("javadoc")
  public static String DeleteUnsafeControlAction;
  @SuppressWarnings("javadoc")
  public static String DeleteUnsafeControlActions;
  @SuppressWarnings("javadoc")
  public static String DevelopmentTeam;
  @SuppressWarnings("javadoc")
  public static String DoYouWishToDeleteTheDesignRequirement;
  @SuppressWarnings("javadoc")
  public static String DoYouWishToDeleteTheSystemGoal;
  @SuppressWarnings("javadoc")
  public static String DoYouReallyWantToOverwriteTheContentAt;
  @SuppressWarnings("javadoc")
  public static String DeleteHazards;
  @SuppressWarnings("javadoc")
  public static String DeleteProject;
  @SuppressWarnings("javadoc")
  public static String DeleteProjectConfirmMsg;
  @SuppressWarnings("javadoc")
  public static String DoYouWishToDeleteTheHazard;
  @SuppressWarnings("javadoc")
  public static String DescriptionOfThisHazard;
  @SuppressWarnings("javadoc")
  public static String DescriptionOfThisSafetyConstr;
  @SuppressWarnings("javadoc")
  public static String DeleteSafetyConstraints;
  @SuppressWarnings("javadoc")
  public static String DoYouWishToDeleteTheSafetyConstraint;
  @SuppressWarnings("javadoc")
  public static String DecorationToolTip;
  @SuppressWarnings("javadoc")
  public static String DecreaseFontSize;
  @SuppressWarnings("javadoc")
  public static String DefaultNavigationFont;
  @SuppressWarnings("javadoc")
  public static String DefaultControlStructureFont;
  @SuppressWarnings("javadoc")
  public static String DesignRequirements;
  @SuppressWarnings("javadoc")
  public static String DashedArrows;
  @SuppressWarnings("javadoc")
  public static String DashedBox;
  /*
   * ------E-------------------------------------------------------------------*
   */
  @SuppressWarnings("javadoc")
  public static String Error;
  @SuppressWarnings("javadoc")
  public static String ExportImage;
  @SuppressWarnings("javadoc")
  public static String ExportingPdf;
  @SuppressWarnings("javadoc")
  public static String ExportingCS;
  public static String ExportingCSC;
  @SuppressWarnings("javadoc")
  public static String ExportingCSV;
  @SuppressWarnings("javadoc")
  public static String ExportingCSwithPM;
  @SuppressWarnings("javadoc")
  public static String ExportingUCATable;
  @SuppressWarnings("javadoc")
  public static String ExportingCFTable;
  public static String ExportingRefinedSafety;
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
  // *-------F-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String File;
  @SuppressWarnings("javadoc")
  public static String FileExists;
  @SuppressWarnings("javadoc")
  public static String FileFormatNotSupported;
  @SuppressWarnings("javadoc")
  public static String Filter;
  @SuppressWarnings("javadoc")
  public static String FontColor;
  // *-------G-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String GoToWorkbench;
  @SuppressWarnings("javadoc")
  public static String GivenIncorrectly;
  @SuppressWarnings("javadoc")
  public static String GridCellButton_None;
  @SuppressWarnings("javadoc")
  public static String GridWrapper_Column;
  // *-------H-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String HazardLinks;
  public static String Hazard;
  @SuppressWarnings("javadoc")
  public static String Hazards;
  @SuppressWarnings("javadoc")
  public static String HazardsAvailableForLinking;
  public static String HazDesc;
  public static String HazXDesc;
  @SuppressWarnings("javadoc")
  public static String HelpContents;
  @SuppressWarnings("javadoc")
  public static String HelpToolTip;
  @SuppressWarnings("javadoc")
  public static String Hover;
  // *-------I-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String Information;
  public static String InformationCannotBeStored;
  @SuppressWarnings("javadoc")
  public static String InvalidPluginCommand;
  @SuppressWarnings("javadoc")
  public static String InvalidSchemaFile;
  @SuppressWarnings("javadoc")
  public static String ID;
  @SuppressWarnings("javadoc")
  public static String IlegalPath;
  @SuppressWarnings("javadoc")
  public static String Italic;
  // *-------J-------------------------------------------------------------------*
  // *-------K-------------------------------------------------------------------*
  // *-------L-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String Links;
  @SuppressWarnings("javadoc")
  public static String LinkingView;
  @SuppressWarnings("javadoc")
  public static String LinkingOfAccidentsAndHazards;
  @SuppressWarnings("javadoc")
  public static String Line;
  @SuppressWarnings("javadoc")
  public static String LoadExistingProject;
  @SuppressWarnings("javadoc")
  public static String LoadFailed;
  @SuppressWarnings("javadoc")
  public static String LoadProject;
  @SuppressWarnings("javadoc")
  public static String Logo;
  public static String LossOfData;
  public static String LTLFormulasTable;
  @SuppressWarnings("javadoc")
  public static String loadHaz;
  @SuppressWarnings("javadoc")
  public static String loadingHaz;

  // *-------M-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String ManipulationObjects;
  // *-------N-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String NameInputLabel;
  @SuppressWarnings("javadoc")
  public static String NoUpdatesForTheCurrentInstallationHaveBeenFound;
  @SuppressWarnings("javadoc")
  public static String NoUpdate;
  @SuppressWarnings("javadoc")
  public static String NoAccidentSelected;
  @SuppressWarnings("javadoc")
  public static String NoControlActionSelected;
  @SuppressWarnings("javadoc")
  public static String NoDataSelected;
  @SuppressWarnings("javadoc")
  public static String NoDesignRequirementSelected;
  @SuppressWarnings("javadoc")
  public static String NoExportAvailable;
  @SuppressWarnings("javadoc")
  public static String NoHazardSelected;
  @SuppressWarnings("javadoc")
  public static String NoProjectSelected;
  @SuppressWarnings("javadoc")
  public static String Navigation;
  @SuppressWarnings("javadoc")
  public static String NewDir;
  @SuppressWarnings("javadoc")
  public static String NewProject;
  @SuppressWarnings("javadoc")
  public static String NewProjectPath1;
  @SuppressWarnings("javadoc")
  public static String NewSafetyConstraint;
  @SuppressWarnings("javadoc")
  public static String NoSafetyConstraintSelected;
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
  // *-------O-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String OffsetValue;
  @SuppressWarnings("javadoc")
  public static String Others;
  @SuppressWarnings("javadoc")
  public static String OneAnalysisFundamentals;
  @SuppressWarnings("javadoc")
  public static String Open;
  // *-------P-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String PathIsNoDir;
  @SuppressWarnings("javadoc")
  public static String PlatformName;
  @SuppressWarnings("javadoc")
  public static String PreferedWSDoNotExist;
  public static String PreferencePageColorAndFont_DontSortExplorer;
  public static String PreferencePageColorAndFont_DoYouWantToHighlight_selected_step_items;
  public static String PreferencePageColorAndFont_From_a_to_z;
  public static String PreferencePageColorAndFont_From_z_to_a;
  public static String PreferencePageColorAndFont_SortExplorerByExtension;
  public static String PreferencePageColorAndFont_SortExplorerByFileName;
  @SuppressWarnings("javadoc")
  public static String Preferences;
  @SuppressWarnings("javadoc")
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
  public static String Project;
  @SuppressWarnings("javadoc")
  public static String ProjectExists;
  public static String ProjectExplorer_OpenWith;
  public static String ProjectManager_3;
  public static String ProjectManager_ProjectAlreadyExistsInWorkspace;
  public static String ProjectManager_ProjectIsAlreadyOpen;
  @SuppressWarnings("javadoc")
  public static String ProjectName;
  public static String RulesTable;
  public static String RunExport;
  // *-------Q-------------------------------------------------------------------*
  // *-------R-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String RunningUpdateFromWithinEclipseIDEThisWontWork;
  @SuppressWarnings("javadoc")
  public static String ReallyInstallUpdates;
  @SuppressWarnings("javadoc")
  public static String RecentProjects;
  public static String RefinedSafetyConstraintsTable;
  public static String RefinedUnsafeControlActions;
  @SuppressWarnings("javadoc")
  public static String RememberWorkspace;
  @SuppressWarnings("javadoc")
  public static String RemoveAll;
  @SuppressWarnings("javadoc")
  public static String Remove;
  public static String RenameCommand_alreadyExists;
  public static String RenameCommand_CannotRenameProject;
  public static String RenameCommand_NewProjectName;
  public static String RenameCommand_RenameProject;
  public static String RenameCommand_TheProject;
  // *-------S-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String StoppedTooSoon;
  @SuppressWarnings("javadoc")
  public static String Store;
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
  public static String SystemDescription;
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
  public static String SplitterForegColor;
  @SuppressWarnings("javadoc")
  public static String SystemGoals;
  @SuppressWarnings("javadoc")
  public static String Strikeout;
  @SuppressWarnings("javadoc")
  public static String StuPro;
  @SuppressWarnings("javadoc")
  public static String StartTyping;
  @SuppressWarnings("javadoc")
  public static String StatusLine;
  public static String STPAPDFReport;
  @SuppressWarnings("javadoc")
  public static String SwitchToHazards;
  @SuppressWarnings("javadoc")
  public static String SwitchToAccidents;
  @SuppressWarnings("javadoc")
  public static String saveHaz;
  @SuppressWarnings("javadoc")
  public static String savingHaz;
  // *-------T-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String Tabulator;
  @SuppressWarnings("javadoc")
  public static String TheUpdateRoutineDoesNotWorkWhenASTPAIsExecutedFromTheIDE;
  @SuppressWarnings("javadoc")
  public static String ThereAreUnsafedChangesDoYouWantToStoreThem;
  @SuppressWarnings("javadoc")
  public static String ThereAreUnsafedChangesDoYouWantToStoreThemAbort;
  @SuppressWarnings("javadoc")
  public static String TheWorkspaceCannotBeChangedWhen;
  @SuppressWarnings("javadoc")
  public static String Title;
  @SuppressWarnings("javadoc")
  public static String TitleFont;
  @SuppressWarnings("javadoc")
  public static String TextBox;
  @SuppressWarnings("javadoc")
  public static String ThreeCausalAnalysis;
  @SuppressWarnings("javadoc")
  public static String TextForeground;
  @SuppressWarnings("javadoc")
  public static String TextBackground;
  @SuppressWarnings("javadoc")
  public static String ThisHazFileIsInvalid;
  @SuppressWarnings("javadoc")
  public static String TypeHere;
  @SuppressWarnings("javadoc")
  public static String ThereAreUnsafedChanges;
  @SuppressWarnings("javadoc")
  public static String TwoUnsafeControlActions;
  // *-------U-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String UpdatesInstalledRestart;
  @SuppressWarnings("javadoc")
  public static String UpdatesHaveBeenInstalledSuccessfullyDoYouWantToRestart;
  @SuppressWarnings("javadoc")
  public static String UpdateFromIDE;
  @SuppressWarnings("javadoc")
  public static String UnsafeControlActions;
  @SuppressWarnings("javadoc")
  public static String UnsafeControlActionsTable;
  @SuppressWarnings("javadoc")
  public static String UnselectedNavItem;
  @SuppressWarnings("javadoc")
  public static String Underline;
  @SuppressWarnings("javadoc")
  public static String UserDotHome;
  @SuppressWarnings("javadoc")
  public static String UseWorkspace;
  @SuppressWarnings("javadoc")
  public static String UsuallyTheIDEStarts;
  @SuppressWarnings("javadoc")
  public static String UpdatingASTPA;
  // *-------V-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String Version;
  // *-------W-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String WantToDeleteTheCF;
  @SuppressWarnings("javadoc")
  public static String WantToDeleteTheUCA;
  @SuppressWarnings("javadoc")
  public static String Warning;
  @SuppressWarnings("javadoc")
  public static String WelcomeView;
  public static String WelcomeView_0;
  public static String WelcomeView_1;
  public static String WelcomeView_2;
  public static String WelcomeView_3;
  public static String WelcomeView_4;
  public static String WelcomeView_ToWorkbench;
  @SuppressWarnings("javadoc")
  public static String WorkspaceIsNoWorkspaceYet;
  @SuppressWarnings("javadoc")
  public static String WorkspaceSetDesc;
  @SuppressWarnings("javadoc")
  public static String WorkspaceNew;
  @SuppressWarnings("javadoc")
  public static String WorkspaceRootPath;
  @SuppressWarnings("javadoc")
  public static String WorkspaceSet;
  @SuppressWarnings("javadoc")
  public static String WrongTiming;
  // *-------X-------------------------------------------------------------------*
  // *-------X-------------------------------------------------------------------*
  // *-------Z-------------------------------------------------------------------*
  @SuppressWarnings("javadoc")
  public static String ZoomItem;
  @SuppressWarnings("javadoc")
  public static String ZoomLevel;

}
// CHECKSTYLE:ON
