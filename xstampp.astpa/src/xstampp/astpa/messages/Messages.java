/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
  private static final String BUNDLE_NAME = "xstampp.astpa.messages.messages"; //$NON-NLS-1$
  public static String AddGivenIncorrectlyUCA;
  public static String AddingNewUCA;
  public static String AddNotGivenUCA;
  public static String AddStoppedTooSoonUCA;
  public static String AddWrongTimingUCA;
  public static String ControlAction;
  public static String ControlStructure_NameMustBeUnique;
  public static String ControlStructure_New;
  public static String CSContextMenuProvider_MoveDOWN;
  public static String CSContextMenuProvider_MoveUP;
  public static String CSContextMenuProvider_MoveUP_DOWNAllWay;
  public static String CSContextMenuProvider_MoveUP_DOWNOneStep;
  public static String DeleteControlStructureQuestion;
  public static String DeleteControlStructureTitle;
  public static String GivenIncorrectly;
  public static String ListOfCAEditPart_ToolTip0;
  public static String NotGiven;
  public static String ProjectSpecifics_GeneralSettings;
  public static String ProjectSpecifics_ReopenDesignRequirements;
  public static String ProjectSpecifics_ReopenHazardView;
  public static String ProjectSpecifics_UseCausalScenarios;
  public static String ProjectSpecifics_UseCausalScenariosTip;
  public static String ProjectSpecifics_UseSeverity;
  public static String ProjectSpecifics_UseSeverityTip;
  public static String Severity_S0;
  public static String Severity_S1;
  public static String Severity_S2;
  public static String Severity_S3;
  public static String StoppedTooSoon;
  public static String UCAHeaderSettings_Column;
  public static String UCAHeaderSettings_CustomHeadersGroupTitle;
  public static String UCAHeaderSettings_CustomHeadersGroupToolTip;
  public static String UCAHeaderSettings_Default;
  public static String UnsafeControlActions;
  public static String UnsafeControlActionsTable;
  public static String UnsafeControlActionsView_HeaderToolTip;
  public static String UnsafeControlActionsView_InvalidSeverityMsg;
  public static String UnsafeControlActionsView_InvalidSeverityTitle;
  public static String WrongTiming;
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
  }
}
