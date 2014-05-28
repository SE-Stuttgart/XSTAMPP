@echo off
echo Auf welche Version soll das Projekt gesetzt werden? 
set /P Version=
mvn -Dtycho.mode=maven org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=%Version%

pause