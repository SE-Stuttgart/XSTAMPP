# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).
## XSTAMPP [3.1.1] - [Unreleased]

### A-STPA Bugfixes:
*  Fix Bug that when adding a new UCA in the Causal Factors Table it automatically adds a new Causal Factor which leads to save problems
*  Fix Bug that exported images of the control structure would sometimes include black vertical lines on the left or right side
*  Fix Bug in Collaboration System that Severity of Table Entries would not be included in user Copy
## XSTAMPP [3.1.0] - 2018-09-12

- Refactors documentation generation (README and CHANGELOG)
- Updates documentation for working on XSTAMPP
- Updated step images to the new STPA steps given by the STPA_Handbook by Leveson

### A-STPA Features:

*  Add option in the project settings to switch causal Analysis to UCA -> Causal Factor
*  Add A-STPA project setting for linking hazards to safety constraints
*  Add ToolTips for control actions in UCA table
*  Add ToolTips for Severity in common Table and Grid
*  Add option to edit UCA text from the Corresponding SC View
*  Add delete buttons to the second column in Causal Factors table in UCA centered view
*  Add Button in CausalFactor Table to add links to existing safety constraints
*  Add causal factor link labels to safety constraint view in step 4
*  Add ability to remove safety constraints in step 4 if they are not linked to a causal factor
*  Add export for pdf and data of Causal Safety Constraints
*  Add csv export for safety constraints of step 4

### A-STPA Bugfixes:

*  Fix Bug in List of Control Actions that resize/rename does not work properly
*  Fix Bug in Causal Factors Table that sometimes not editable Causal Factors are Displayed
*  Fix bug in ASTPA Collaboration that uca severity would not sync
*  Fix Bug that the font size setting for the export was not adopted
*  Fix Bug that caused the causal factor table data export to be always empty
*  Fix Bug in STPA Collaboration system that changes from one user could be overwritten by an other
*  Fix Bug that export of word/pdf sometimes can't be started
*  Fix Bug in control structure copy&paste that pasted components are placed incorrect
*  Fix Bug that connection Anchors are always synchronized between step 2&4
*  Fix NullPointer when using the CorrespondingSafetyConstraints Filter on empty safety constraints
*  Fix Bug when that copied components are not placed correct in control structure
*  Fix NullPointer in Causal Factor safety Constraints View for scenarios
*  Fix names of the Progress sheets exported in the statistics export to new step naming
*  Fix Bug in Control Structure that an existing Control Action can not be added to a List
*  Fix Bug that in UCA Table Severity is not updated when a Hazard link is added
*  Fix wrong placement of the Linking Pop-up of Display

## XSTAMPP [3.0.2] - 2018-04-10

### A-STPA Bugfixes:

*  Fix bug in ControlStructure Export that sometimes connections or components would be drawn in wrong positions

## XSTAMPP [3.0.1] - 2018-02-28

### A-STPA Bugfixes:

*  Fix bug in A-STPA collaboration when creating a new user copy its not visible in the Project Explorer and the Causal Factors are empty
*  Fix bug that the user would not be notified if a StpaPriv/STPASec project would fail to load
*  Fix bug that STPASec/Priv and ACAST files that are manipulated in XSTAMPP 3.0.0

## XSTAMPP [3.0.0] - 2018-02-15

### A-STPA Features:

*  Add Tooltips for Safety Constraint IDs in the Causal Factors table
*  Add MS Word Export for the Final STPA Report
*  Add support for carrying a single UCA constraint in the Causal Factors Table over to one or more single constraints for each hazard

### A-STPA Bugfixes:

*  Fix bug that projects could sometimes not be loaded in XSTAMPP due to special characters in the causal factor notes
*  Fix bug in the calculation of the project progress that UCAs marked as not hazardous lead to no progress in the respective Control Action
*  Fix bug when deleting a list from a Linking Widget, always the first one entry is removed
*  Fix collaboration System always displays a wrong number of changes after a merge
*  Fix multiple User responsibilities not working when more than one user is selected
*  Fix bug in Causal Factos and UCA table the buttons of a cell editor are sometimes outside of the screenspace
*  In the UCA table when filtering for a UCA or Hazard ID only the result entry is shown(the add rows are also omitted)
*  Fix in Causal Factors table the Notes / Rationale is not imported in v2.6
*  Fix in Causal Factors table the design hints for single safety constraint rows overwrite the notes
*  Increase heap space reserved for XSTAMPP to prevent errors that occur  during the pdf export of huge projects
*  Fix bug that XSTAMPP dialogs cannot be resized

## XSTAMPP 2.6.0

### A-STPA Features:

*  Add STPA Progress Indicator Export for creating a status report based on editable measures
*  Add A-STPA User Manual with stepwise guidance through the analysis and glossary with STPA definitions
*  Add Option to change between using one or multiple safety constraint in the Causal Analysis
*  Add ToolTips Support for UCA and Causal Factors Table
*  Add Editor to add a Design Hint for a hazard based safety constraint in the Causal Analysis

### A-STPA Bugfixes:

*  Fix bug that filter in Causal Factors Table is not working for Component Type filters
*  Fix unexpected behaviour in control structure of undo/redo of parent changes
*  Fix scrolling informations are lost when manipulating grid views
*  Fix bug that Design requirements of step1&2 can't be removed/added
*  Fix bug in control structure export that image and text would overlap

### Bugfixes:

*  Fix Nullpointer when creating a new CAST/StpaPriv/STPASec project

### Other Changes:

*  In the Control Structure Undo/Redo is now handled by the global Undo/Redo service which is accessible on the XSTAMPP toolbar and over CTRL+Z/CTRL+Y

## XSTAMPP [2.5.3] - 2017-11-22

### A-STPA Bugfixes:

*  Fix visibility issues on macOS of UI Elements in the user system
*  Fix on macOS the button labels of the text editor in the UCA/Causal Factor Table are not visible
*  Fix bug that adding a new UCA is not poossible in the Unsafe Control Actions Table

## XSTAMPP [2.5.2] - 2017-11-08

### A-STPA Features:

*  Add auto increase of UCA Severity to the highest Severity of itself and the linked Hazards

### A-STPA Bugfixes:

*  Fix that when changing the title of a design requirement in step1/2 the title is not changed in the table
*  Fix bug in the step 1.3 Table filter won't filter by safety constraints and added filter by uca's
*  Fix bug when deleting a hazard/accident all ids are changed
*  Fix bug in user management that would create a deadlock when trying to log in
*  Fix bug in the Linking Shells that the description won't fit the seleted entrie when the list is fitlered
*  Fix bug in Step 1.3 that description of Safety Constraints in the Linking Dialog is empty
*  Fix bug since 2.3.0 that a Table entry description is not Wrapped
*  Fix loading bug when creating UCA without Corresponding Safety Constraint
*  Fix bug in A-STPA 2.5.1 that corresponding safety constraints are not loaded in Step 1

## XSTAMPP [2.5.1] - 2017-10-18

### Bugfixes

*  Fix all UCA-Hazard-Links are removed when one is deleted by the user
*  Fix Null Pointer Exception during storage in Unsafe Control Actions
*  Fix Value '-1' is not facet-valid with respect to minInclusive '0' for type 'nonNegativeInteger'
*  Fix that in old projects a UCA's id is always UCA1.-1 
*  Fix Null Pointer Exception when saving a project without System Goals
*  Fix Null Pointer Exception when category filter is cleared
*  Fix that the toolbar doesn't update correctly when the uca view is active
*  Fix loading error that when creating Causal Safety Constraints in version 2.5 the file can't be loaded anymore
*  Fix issue that previously created UCA-hazard-links aren't present anymore
*  Fix that changing the style of the System Description doesn't enable save
*  Fix that the System descriptions style is not included correctly in the export
*  Fix bad link in A-STPA User Guide for managing projects and styling of pages
*  Fix that Save As command is only enabled if project is changed
*  Fix that XSTAMPP cannot be closed with unfinished jobs
*  Fix several typos in XSTAMPP
*  Fix bug in System Description Export Wizard that the export path cannot be chosen

### Others

*  Change that a UCA's severity increases when linked to more severe hazard

## XSTAMPP [2.5.0] - 2017-09-24

### Features:

*   Add STPA Glossary for clearification of STPA terms/terminology
*   Add posibillity to add Control Actions to the Control Structure from the Control Action Table (Step 1)

## XSTAMPP [2.4.1] - 2017-08-18

### Bugfixes:

*   Fixed NullPointerException when selecting hazard or accident entries
*   Fixed NullPointerException when opening the Results View by null check #524
*   Fixed In STPASec/Priv the Unsafe Unsecure Editor won't show up when opening from the Project Explorer
*   Changed Context Table/Unsafe Unsecure Editor/Privacy Relation Editor views in xstpa/stpasec and stpaprov to inline views
*   Fixed a bug that enabled a user to drag a component in the Control Structure in a Prozess Variable and thereby deleting all connections without the abillity to undo

## XSTAMPP [2.4.0] - 2017-08-11

### Features:

*   Move Components in the control structure Up and Down from the context menu (right clicking on them)
*   Added a Feedback component to the control structure that is linked (via a visible connection) to a component connection#497
*   Added new Typeless component for definition of a unkown component, clarified Dached box Tooltip #517
*   Added Severity analysis for Hazards, Accidents and Unsafe Control Actions which can be added/removed from a project in the Project Settings
*   Added new Editors for Creating Design Requirements in Step 1 and 2
*   Introduced new Linking Widgets for the linking between Accidents-Hazards, Accidents-SafetyConstraints, SafetyConstraints-DesignRequirements(for each step)
*   Created new Grid Editor for the Unsafe Control Action and Causal Factors Table with Save/Cancel Button
*   Added settings dialog to customize the table headers in the unsafe control actions table per project #521

### Bugfixes:

*   Fixed a bug that would produce a null pointer when drag&dropping a component in the control structure #506

### Others:

*   Control Structure Components are now added in the front level/ at the bottom of a list
*   Added checkbox to login dialog to enable read only access without login credentials #520
*   Included new icons for the Control Structure (
*   with Process Modell)
*   Included STPAPriv and STPASec

## XSTAMPP 2.3.1

### Features:

*   Move Components in the control structure Up and Down by right clicking on them

### Others:

*   Control Structure Components are now added in the front level/ at the bottom of a list

## XSTAMPP [2.3.0] - 2017-06-07

### Features:

*   Added Project Settings Dialog in 'Project->Project Settings' and the context menu of the project explorer
*   Added Project Menu with Project Settings in the main menu bar of XSTAMPP
*   Added User System for creating multiple users/admins/'read only' users
*   Added login Dialog to prevent unregistered access
*   Added 'User' page in the Project Settings Dialog
*   Added 'Collaboration' page in the Project Settings Dialog for creating working copies per user
*   Added a '[READ_ONLY]' indicator in the project explorer and the title bar of the program window
*   Added a '*' indicator in the project explorer and the title bar of the program window to mark projects that need saving
*   Added Undo/Redo Buttons in the main toolbar of XSTAMPP to undo/redo changes in:
    *   Accidents Table
    *   Hazards Table
    *   Safety Constraints Table
    *   System Goals Table
    *   Design Requirements Table
    *   Unsafe Control Action Description
    *   Control Actions Table
    *   Corresponding Safety Constraints
    *   Causal Factors Description and Notes
*   **A-STPA 2.3.0**
    *   Added project specific option to safe preferences to hide/show scenarios
    *   Added project specific option to safe preferences to hide/show hazard severity analysis

### Bugfixes:

*   Fixed bug where XSTAMPP would sometimes create 'null.ext' files
*   Fixed Bug that caused unsafed changes to be dismissed without promt when the user clicked on 'File->Switch Workspace'
*   Fixed Bug that caused the project to crash when clicking the save button very often and very quickly
*   **A-STPA 2.3.0**
    *   Fixed Bug where the connection anchor sometimes diappeared when trying to connect two components
    *   Fixed Bug that disabled the snap-to feature for connecting a component when inside a dashed box
    *   Fixed Bug that caused source/target components(in the control action table) of a control action to stay the same even if the control action had been moved to a new connection in the control structure
    *   Fixed Bug in the control structure diagram that caused the arrow head of a connection to point in the wrong direction
    *   Fixed Bug that would prevent entries in the Table Views to be moved up/down
*   **A-CAST 1.0.5**
    *   Fixed Bug that would prevent storage of the responsibilities

### Others:

*   Changed second column of Unsafe Control Actions Table from 'Providing causes hazard' to 'Providing incorrect causes hazard'
*   Removed 'unused' toolbar items form the main toolbar

## XSTAMPP 2.0.1

### Features:

*   Added 'Install New Software' menu to manually install/deinstall features and plugins for xstampp
*   Updates are now available over the Edit->Preferences->Install/Update preferences please see adapt these preferences to build your update schedule
*   **A-STPA 2.0.4**
    *   enhanced id mapping of safety Constraints/Control Actions and UCAs
    *   Show/Hide the border of the "List of Control Actions" Component
    *   Show/Hide of the border of the process variable in the control structure
*   **XSTPA 1.0.1**
    *   New delete/delete all function in refined rules table
    *   LTL Formulas in context 'control action provided' are now derived in timed ltl
    *   Integration of the Refined UCA, Refined Safety Constraints and LTL Formulas table in the xstpa subtree
    *   Support for automatically generated the rules of the refined unsafe control action based on the critical combinations and add them in column rules in rules table.
    *   Enhanced adding and editing of value/(don't care) entries in the context table
    *   Export function for context tables, refined unsafe control actions, rules,safety constraints and ltl properties
    *   Added Acts algorithm in the installation folder so that it not longer has to be added manually

### Bugfixes:

*   Fixed errors when shutting down XSTAMPP with jobs still running
*   enhanced error dialogs of load/save
*   **A-STPA 2.0.4**
    *   fixed a bug that sometimes xstampp breaks down when performing any changes
    *   fixed the bug that sometimes the text entered text in the UCA and Causal Factors table deleted when closing the editor
*   **XSTPA 1.0.1**
    *   ACTS algorithm can now be executed for more than 9 variables
    *   Values that are marked as (don't care) can now be restored to any available value
    *   Enhanced 'check for conflicts' function in the context table
    *   Fixed a bug that led to loss of changes in the Context Tables configuration dialog
    *   enhanced the performance of the xstpa view
    *   fixed the bug that sometimes the Scrollbars are not displayed when needed

### Others:

*   Enhanced Load and Save Funktion to prevent data loss
*   Enhanced preference function of the project explorer to sort projects and change fonts

## XSTAMPP 2.0.0

### Features:

*   Create CAST Project and STPA project in the project explorer.
*   “One-Click” generate all reports of your STPA or CAST work in different formats e.g. PDF, image and CSV by clicking one button.
*   Edit a large amount of data in the unsafe control actions table, by filtering the entries.
*   In the control structure you can merge multiple control action components into one “List of Control Actions” component
*   In the control structure you can link between the control action and arrows, to attach source and destination to the control action.
*   Automatically generate the context tables and LTL formal specification and apply different coverages criteria on the context table to reduce the number of combinations.
*   Choose between “fixed” or flexible connection points on each component in the control structure diagram.
*   Copy & Paste feature in the control structure diagram.

### Bugfixes:

*   The toolbar does not appear correctly with large screen resolutions
*   In the control structure Dashed Boxes should always be drawn behind other objects
*   I cannot create a new project with STPA wizard
*   change the font in system description by mouse is very hard
*   Null Pointer when trying to delete a List of Control Actions
*   the table headers and column names in the cf and ucs table are not displayed correctly
    *   On mac in the UCA Table the dialoge for linking Hazards does not appear
    *   Accidents/hazzards without a long description doesn't show up in the linking view
    *   The toolbar does not appear correctly with large resolutions of large laptop screen.
    *   Components in the control structure should have distinguishable names
*   The Export function sometimes fails with an error
*   It is diffcult to move and select the control action list
*   I cannot generate the reports from the run button with a project which has a file with .haz

## A-STPA 2.0.1

### Features:

*   Font in Control Structure (Step 1.8/3.1) now changable in Preferences

### Bugfixes:

*   fixed bug that caused special characters to be stored in a wrong format
*   added compability mode for disabeling the above bugfix to guaranty compabillity with older versions (in this mode the bug still exists so its not recommended to store special characters with it!!)
*   disabled connection constraints to enable the users to set connections in the control structure at will
*   fixed bug with dublicated toolbars

## XSTAMPP 1.0.0/A-STPA 2.0.0

### Features:

*   Possibility of managing multiple projects at the same time thanks to the XSTAMPP project explorer
*   Font in Control Structure (Step 1.8/3.1) now changable in Preferences
*   toolbars now provided in the main toolbar of the workbench
*   Renaming Projects directly in the project explorer
*   Preference Option to highlight selected step in Edit/Preferences/Color and Fonts

### Bugfixes:

*   controlstructure images are now exported in higher quality
*   Fixed Bug that links between causal factors and hazards were not stored

## 1.0.5

### Features:

*   added Help Content with contact link
*   added Version History and quickstartguide as in Help->Help Content

### Bugfixes:

*   final bugfix of the control structure image resolution bug in the PDF Export #64
*   added save and load status monitor #38
*   fixed bug #80 CS-Editor: Control Action not working correctly

## 1.0.4

### Features:
*   added decoration mode in the Control Structure, which adds icons and Colored borders for a better distinguishability for the components

### Bugfixes:

*   fixed wrong scaling of the control structure image in the pdf Export
*   Components which are created in the Control Structure are not longer removeable in the Control Structure with process model

## 1.0.3

### Features:

*   added Export function of all Data as Data Sheet, Image and PDF Export
*   added Contact Data in the About Dialog
*   added ControlAction Component in the ControlStructure,linked with the ControlActionsView

## 1.0.2

### Features:

*   enhanced the handling of the hazard linking Dialog in the 'Unsafe Control Actions Table'
*   prevent components from beeing dragged out of sight
*   adapt the Control Structure Export image
*   it's now possible to change the height and width of the Values and Variables inside the Proicess Modell
*   enhanced undo/Redo
*   improved popup for safety Constraint linking in the Causal analysis
*   added Version History and quickstartguide as in Help->Help Content

### Bugfixes:

*   final bugfix of the control structure image resolution bug in the PDF Export #64
*   added save and load status monitor #38
*   fixed bug #80 CS-Editor: Control Action not working correctly

[Unreleased]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v3.0.2...HEAD
[3.0.2]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v3.0.1...v3.0.2
[3.0.1]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v3.0.0...v3.0.1
[3.0.0]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v2.5.3...v3.0.0
[2.5.3]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v2.5.2...v2.5.3
[2.5.2]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v2.5.1...v2.5.2
[2.5.1]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v2.5.0...v2.5.1
[2.5.0]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v2.4.1...v2.5.0
[2.4.1]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v2.4.0...v2.4.1
[2.4.0]: https://github.com/SE-Stuttgart/XSTAMPP/compare/v2.3.0...v2.4.0
[2.3.0]: https://github.com/SE-Stuttgart/XSTAMPP/releases/tag/v2.3.0
