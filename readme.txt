Change Log:

A-STPA 2.0.1

	Features:
	- Font in Control Structure (Step 1.8/3.1) now changable in Preferences

	Bugfixes:
	- fixed bug that caused special characters to be stored in a wrong format
	- added compability mode for disabeling the above bugfix to guaranty compabillity with older versions
	  (in this mode the bug still exists so its not recommended to store special characters with it!!)
	- disabled connection constraints to enable the users to set connections in the control structure
	  at will
	- fixed bug with dublicated toolbars
	


XSTAMPP 1.0.0/A-STPA 2.0.0
	Since this version A-STPA is now part of the eXtensible STAMP Platform (XSTAMPP)
	it is now provided as fully integrated plug-in.
 	Due to this A-STPA has now got a whole new appearance which should enable the user
	to use it in a far more flexible way. Features: •Possibility of managing multiple projects
	at the same time thanks to the XSTAMPP project explorer 

	Features:
	- Font in Control Structure (Step 1.8/3.1) now changable in Preferences
	- toolbars now provided in the main toolbar of the workbench 
	- Renaming Projects directly in the project explorer 
	- Preference Option to highlight selected step in Edit/Preferences/Color and Fonts

	Bugfixes:
	- controlstructure images are now exported in higher quality
	- Fixed Bug that links between causal factors and hazards were not stored
	- 
	


1.0.5:

	Features:
	- added Help Content with contact link
	- added Version History and quickstartguide as in Help->Help Content
	

	Bugfixes:
	- final bugfix of the control structure image resolution bug in the PDF Export #64
	- added save and load status monitor #38
	- fixed bug #80 CS-Editor: Control Action not working correctly 
		
1.0.4:
	
	Features:
	- added decoration mode in the Control Structure, which adds icons and Colored borders for a better 	  	  distinguishability for the components

	Bugfixes:
	- fixed wrong scaling of the control structure image in the pdf Export
	- Components which are created in the Control Structure are not longer removeable in the Control 	  	  Structure with process model

1.0.3:
	Features:
	- added Export function of all Data as Data Sheet, Image and PDF Export 
	- added Contact Data in the About Dialog
	- added ControlAction Component in the ControlStructure,linked with the ControlActionsView 

1.0.2:

    	Bugfixes:
	- enhanced the handling of the hazard linking Dialog in the 'Unsafe Control Actions  Table'
    	- prevent components from beeing dragged out of sight
	- adapt the Control Structure Export image
	- it's now possible to change the height and width of the Values and Variables inside the Proicess Modell
	- enhanced undo/Redo
	- improved popup for safety Constraint linking in the Causal analysis

  