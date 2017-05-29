/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.model;

import java.io.File;
import java.util.Observer;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.jobs.Job;

/**
 * Interface for the DataModel which defines the methods to handle the Observer pattern. All other
 * interfaces for the data model should extend this interface
 * 
 * @author Fabian Toth
 * 
 */
public interface IDataModel {

  /**
   * Adds an observer to the set of observers for this object, provided that it is not the same as
   * some observer already in the set.
   * 
   * @author Fabian Toth
   * 
   * @param observer
   *          an observer to be added.
   */
  void addObserver(Observer observer);

  /**
   * Deletes an observer from the set of observers of this object.
   * 
   * @author Fabian Toth
   * 
   * @param observer
   *          the observer to be deleted.
   */
  void deleteObserver(Observer observer);

  /**
   * 
   * 
   * @author Lukas Balzer
   * 
   * @param file
   *          the file in which the job should store the results
   * @param log
   *          the logger in which all messeges are written
   * @param isUIcall
   *          the Data model which should be stored, this must be a JAXBContext
   * @return a Job that handles the saving of the project
   */
  Job doSave(final File file, Logger log, boolean isUIcall);

  /**
   * Returns the number of observers of this Observable object.
   * 
   * @author Fabian Toth
   * 
   * @return the number of observers of this object.
   */
  int countObservers();

  /**
   * Prepares the data model for the export
   * 
   * @author Fabian Toth,Lukas Balzer
   * @return whether successful or not
   * 
   */
  public boolean prepareForExport();

  /**
   * Removes the preparations that were made for the export
   * 
   * @author Fabian Toth
   * 
   */
  void prepareForSave();

  /**
   * Triggers an update of the given value
   * 
   * @author Fabian Toth,Lukas Balzer
   * 
   * @param value
   *          the given value to update
   */
  void updateValue(ObserverValue value);

  /**
   *
   * @author Lukas Balzer
   *
   * @return returns the project Name <b>must not be null</b>
   */
  String getProjectName();

  UUID getProjectId();

  /**
   * sets the state of the dataModel to saved
   * 
   * @author Lukas Balzer
   *
   */
  void setStored();

  /**
   *
   * @author Lukas Balzer
   *
   * @return returns wether the currrent state of the dataModel is stored or unstored
   */
  boolean hasUnsavedChanges();

  /**
   *
   * @author Lukas Balzer
   *
   * @param projectName
   *          The name of the project
   * @return if the name has succesfullly been changed
   */
  boolean setProjectName(String projectName);

  /**
   * This method is only to be called the first time the projekt is started to perform initial
   * actions like "not visited" flags or so
   *
   * @author Lukas Balzer
   *
   */
  void initializeProject();

  /**
   * This method can be called when creating a project with existing data from an existing
   * {@link IDataModel}. The initialized project is independent from its original, but contains the
   * same entries and components with respect to their values and uuids.
   * 
   * @param original
   *          A {@link IDataModel} that serves as original.
   */
  void initializeProject(IDataModel original);

  /**
   * 
   *
   * @author Lukas Balzer
   *
   * @return the extension which is stored in the stepped process for this DataModel <b> must not
   *         return null!</b>
   */
  String getFileExtension();

  /**
   *
   * @author Lukas Balzer
   *
   * @return the id for the Plugin which is used by the DataModel
   */
  String getPluginID();

  /**
   * This method should set up a lock for prevents the DataModel from being updated this can be
   * useful when the DataModel receives a lot of changes. Implementors should see that
   * {@link IDataModel#releaseLockAndUpdate(ObserverValue)} resets the lock and triggers an update
   * of the requested value
   * 
   */
  void lockUpdate();

  /**
   * This method should act as counterpart of {@link IDataModel#lockUpdate()} it should release the
   * lock and trigger an update of the requested value.
   * 
   * @param values
   *          the values that should be updated
   */
  void releaseLockAndUpdate(ObserverValue[] values);

  void setUnsavedAndChanged();

  <T> T getAdapter(Class<T> clazz);

  /**
   * Returns a property of an type <b>T</b> that is stored as property mapped to the given key
   * string
   * 
   * @param key
   *          a key for which the data model can contain a property of the given class
   * @param clazz
   *          the class of the requested property, if a property is stored for the given string but
   *          of a different class this method method should return <code>null</code>
   * @return null if no property was stored in the data model or the property had the wrong type.
   */
  <T> T getProperty(String key, Class<T> clazz);
}
