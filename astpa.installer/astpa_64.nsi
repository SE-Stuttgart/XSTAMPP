;NSIS Modern User Interface
;Welcome/Finish Page Example Script
;Written by Joost Verburg

;--------------------------------
;Include Modern UI

	!include "MUI2.nsh"

;--------------------------------
;Include File Functions for getSize

	!include "FileFunc.nsh"
	
;--------------------------------
;Include file association
!include "FileAssociation.nsh"

;--------------------------------
;General

	;Name and file
	Name "A-STPA"
	OutFile "A-STPA_Setup_64bit.exe"
	Icon "logo.ico"

;--------------------------------
; Define Values
	!define EXEC "A-STPA.exe"
	!define APPNAME "A-STPA"
	!define COMPANYNAME "Stupro Team"
	!define DESCRIPTION "A simple tool to create a STPA analysis"
	; These three must be integers
	!define VERSIONMAJOR 0
	!define VERSIONMINOR 12
	!define VERSIONBUILD 0
	; These will be displayed by the "Click here for support information" link in "Add/Remove Programs"
	; It is possible to use "mailto:" links in here to open the email client
	!define HELPURL "http://..." # "Support Information" link
	!define UPDATEURL "http://..." # "Product Updates" link
	!define ABOUTURL "http://..." # "Publisher" link
	
	;Default installation folder
	InstallDir "$PROGRAMFILES\${COMPANYNAME}\${APPNAME}\"

	;Request application privileges for Windows Vista
	RequestExecutionLevel admin

;--------------------------------
;Interface Settings

	!define MUI_ABORTWARNING

;--------------------------------
;Pages

	!insertmacro MUI_PAGE_WELCOME
	!insertmacro MUI_PAGE_LICENSE "license.rtf"
	!insertmacro MUI_PAGE_COMPONENTS
	!insertmacro MUI_PAGE_DIRECTORY
	!insertmacro MUI_PAGE_INSTFILES
	!insertmacro MUI_PAGE_FINISH

	!insertmacro MUI_UNPAGE_WELCOME
	!insertmacro MUI_UNPAGE_CONFIRM
	!insertmacro MUI_UNPAGE_INSTFILES
	!insertmacro MUI_UNPAGE_FINISH

;--------------------------------
;Languages

	!insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "A-STPA" SecDummy
	;set install path
	SetOutPath "$INSTDIR\"
	
	;copy the icon file
	file "logo.ico"
	
	;Generate a list of files to be installed
	!system 'java -jar astpa.listfiles.jar "./../astpa.repository/target/products/A-STPA/win32/win32/x86_64" "list.txt" "unlist.txt"'
	!include list.txt
	!system 'del list.txt'

	;Create uninstaller
	WriteUninstaller "$INSTDIR\Uninstall.exe"
	
	;Create shortcuts
	;Desktop
	CreateShortCut "$DESKTOP\${APPNAME}.lnk" "$INSTDIR\${EXEC}" "$INSTDIR" "$INSTDIR\logo.ico"
	;StartMenu
	createDirectory "$SMPROGRAMS\${COMPANYNAME}"
	createShortCut "$SMPROGRAMS\${COMPANYNAME}\${APPNAME}.lnk" "$INSTDIR\${EXEC}" "$INSTDIR" "$INSTDIR\logo.ico"
	
	;Write Registry keys with uninstall info
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "DisplayName" "${APPNAME}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "UninstallString" "$\"$INSTDIR\uninstall.exe$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "QuietUninstallString" "$\"$INSTDIR\uninstall.exe$\" /S"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "InstallLocation" "$\"$INSTDIR$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "DisplayIcon" "$\"$INSTDIR\logo.ico$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "Publisher" "$\"${COMPANYNAME}$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "HelpLink" "$\"${HELPURL}$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "URLUpdateInfo" "$\"${UPDATEURL}$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "URLInfoAbout" "$\"${ABOUTURL}$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "DisplayVersion" "$\"${VERSIONMAJOR}.${VERSIONMINOR}.${VERSIONBUILD}$\""
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "VersionMajor" ${VERSIONMAJOR}
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "VersionMinor" ${VERSIONMINOR}
	; There is no option for modifying or repairing the install
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "NoModify" 1
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "NoRepair" 1
	; Set the INSTALLSIZE constant (!defined at the top of this script) so Add/Remove Programs can accurately report the size
	${GetSize} "$INSTDIR" "/S=0K" $0 $1 $2
	IntFmt $0 "0x%08X" $0
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "EstimatedSize" "$0"
	
	${registerExtension} "$INSTDIR\${EXEC}" ".haz" "HAZ_File"

SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecDummy ${LANG_ENGLISH} "A test section."

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SecDummy} $(DESC_SecDummy)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "uninstall"
	SetShellVarContext all
	
	;Remove Desktop Shortcut
	Delete "$DESKTOP\${APPNAME}.lnk"
	
	;Remove Start Menu launcher
	Delete "$SMPROGRAMS\${COMPANYNAME}\${APPNAME}.lnk"
	;Try to remove the Start Menu folder - this will only happen if it is empty
	RMDir "$SMPROGRAMS\${COMPANYNAME}"
 
	;Remove files
	!include "unlist.txt"
	!system 'del unlist.txt'
	
	Delete $INSTDIR\logo.ico
 
	;Always delete uninstaller as the last action
	Delete $INSTDIR\uninstall.exe
 
	;Try to remove the install directory - this will only happen if it is empty
	RMDir $INSTDIR
 
	;Remove uninstaller information from the registry
	${unregisterExtension} ".haz" "HAZ_File"
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}"

SectionEnd
