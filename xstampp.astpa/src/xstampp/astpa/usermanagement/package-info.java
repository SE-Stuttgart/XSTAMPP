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

/**
 *  ADMIN: <ul>
 *  <li>legt project an 
 *  <li>erstellt user einträge (name/passwort) mit standart rechten - Erklärung: Einlegen User und User Rechten 
 *  <li>darf Einträge(Accidents& Hazards, usw.) erstellen/bearbeiten 
 *  <li>darf Process Model anlegen und modifizieren
 *  <li>darf Safety Constraints anlegen 
 *  <li>darf/muss Control Structure erstellen, kann Modifikation sperren  <b>TODO</b>
 *  <li>darf/muss ControlActions erstellen 
 *  <li>darf UnsafeControlActions für ControlActions erstellen/bearbeiten 
 *  <li>darf Hazard Links (verlinken) hinzufügen/entfernen
 *  <li>darf Corresponding Safety Constraints schreiben
 *  <li>darf CausalFactors anlegen
 *  </ul>
 *  USER:
 *  <ul> 
 *  <li>darf Einträge bearbeiten (je nach vom admin gegebenen rechten*)
 *    <ol>
 *      <li>Accidents
 *      <li>Hazards
 *      <li>Control Actions
 *    </ol>
 *  <li>darf Safety Constraints anlegen/bearbeiten <b>TODO</b>
 *  <li>darf Control Structure einsehen und editieren <b>TODO</b>
 *  <li>darf ControlActions bearbeiten *²
 *  <li>darf UnsafeControlActions für ControlActions erstellen/bearbeiten *²
 *  <li>darf Hazard Links hinzufügen/entfernen <b>TODO</b>
 *  <li>darf Corresponding Safety Constraints schreiben *³ <b>TODO</b>
 *  <li>darf CausalFactors für UnsafeControlActions erstellen und bearbeiten *³ <b>TODO</b>
 *  </ul>
 *  
 *  <i>
 *  * einträge können für alle/bestimmte user gesperrt werden<br>
 *  *² nur für ControlActions die für ihn vom admin freigegeben wurden  <br>
 *  *³ nur für zugriffsberechtigte UnsafeControlActions <p> 
 *  </i>
 *  nur ein Admin Mode, aber es kann mehrere Admins geben 
 *  -> es kann immer nur eine Person zu einem Zeitpunkt als Admin zugreifen<br>
 *  User Profile: Standard User, Restrict User (read only)
 *  
 * @author Lukas Balzer
 *
 */
package xstampp.astpa.usermanagement;