/*******************************************************************************
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp.stpapriv.model.controlaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.controlaction.ControlAction;
import xstampp.astpa.model.controlaction.NotProvidedValuesCombi;
import xstampp.astpa.model.controlaction.ProvidedValuesCombi;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlstructure.ControlStructureController;
import xstampp.astpa.model.extendedData.ExtendedDataController;
import xstampp.astpa.model.extendedData.RefinedSafetyRule;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.IValueCombie;

/**
 * Class representing the control action objects
 * 
 * @author Fabian Toth, Lukas Balzer
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "controlaction2")
public class SecControlAction extends ControlAction {

	@XmlElementWrapper(name = "unsecurecontrolactions")
	@XmlElement(name = "unsecurecontrolaction")
	private List<UnsecureControlAction> unsafeControlActions;


	


	/**
	 * Constructor of a control action
	 * 
	 * @param title
	 *            the title of the new accident
	 * @param description
	 *            the description of the new accident
	 * @param number
	 *            the number of the new accident
	 * 
	 * @author Fabian Toth
	 */
	public SecControlAction(String title, String description, int number) {
		super(title, description, number);
		this.unsafeControlActions = new ArrayList<>();
	}

	/**
	 * Empty constructor for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 */
	public SecControlAction() {
		this.unsafeControlActions = new ArrayList<>();
		// empty constructor for JAXB
	}

	@Override
	public List<IUnsafeControlAction>  getUnsafeControlActions() {
		List<IUnsafeControlAction> result = new ArrayList<>();
		for (UnsecureControlAction unsafeControlAction : this.unsafeControlActions) {
			result.add(unsafeControlAction);
		}
		return result;
	}

	@Override
  public IUnsafeControlAction getUnsafeControlAction(UUID ucaId) {
    for (UnsecureControlAction unsafeControlAction : this.unsafeControlActions) {
      if (unsafeControlAction.getId().equals(ucaId)) {
        return unsafeControlAction;
      }
    }
    return null;
  }

  @Override
  public List<IUnsafeControlAction> getUnsafeControlActions(
      UnsafeControlActionType unsafeControlActionType) {
		List<IUnsafeControlAction> result = new ArrayList<>();
		for (UnsecureControlAction unsafeControlAction : this.unsafeControlActions) {
			if (unsafeControlAction.getType().equals(unsafeControlActionType)) {
				result.add(unsafeControlAction);
			}
		}
		return result;
	}

	/**
	 * Adds a unsafe control action to this control action
	 * 
	 * @param description
	 *            the description of the new control action
	 * @param unsafeControlActionType
	 *            the type of the enw control action
	 * @return the id of the new control action
	 * 
	 * @author Fabian Toth
	 */
	public UUID addUnsafeControlAction(int number, String description,
			UnsafeControlActionType unsafeControlActionType) {
    UnsecureControlAction unsafeControlAction = new UnsecureControlAction(description,
        unsafeControlActionType);
    this.unsafeControlActions.add(unsafeControlAction);
    unsafeControlAction.setNumber(number);
    return unsafeControlAction.getId();
  }

  /**
   * Adds a unsafe control action to this control action
   * 
   * @param description
   *          the description of the new control action
   * @param unsafeControlActionType
   *          the type of the enw control action
   * @return the id of the new control action
   * 
   * @author Fabian Toth
   */
  public UUID addUnsafeControlAction(int number, String description,
      UnsafeControlActionType unsafeControlActionType, UUID id) {
	  UnsecureControlAction unsafeControlAction = new UnsecureControlAction(description,
        unsafeControlActionType);
    unsafeControlAction.setId(id);
		this.unsafeControlActions.add(unsafeControlAction);
		unsafeControlAction.setNumber(number);
		return unsafeControlAction.getId();
	}

	/**
	 * Searches the unsafe control action and removes it when it is in the list
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action to delete
	 * @return true if the unsafe control action has been removed
	 * 
	 * @author Fabian Toth
	 */
	public boolean removeUnsafeControlAction(UUID unsafeControlActionId) {
		for (UnsecureControlAction unsafeControlAction : this.unsafeControlActions) {
			if (unsafeControlAction.getId().equals(unsafeControlActionId)) {
				return this.unsafeControlActions.remove(unsafeControlAction);
			}
		}
		return false;
	}

	/**
   * Gets all unsafe control actions of this control action in an internal type
	 * 
	 * @return all unsafe control actions of this control action
	 * 
	 * @author Fabian Toth
	 */
	public List<UnsecureControlAction> getInternalUnsafeControlActions() {
		return this.unsafeControlActions;
	}



}
