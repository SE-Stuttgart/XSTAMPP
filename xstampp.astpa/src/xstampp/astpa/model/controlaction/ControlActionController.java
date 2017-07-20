/*******************************************************************************
 * 
 * Copyright (c) 2013-2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam Grahovac, Jarkko
 * Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian Sieber, Fabian Toth, Patrick Wickenhäuser,
 * Aliaksei Babkovich, Aleksander Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.model.controlaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.core.runtime.Assert;

import messages.Messages;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.controlstructure.ControlStructureController;
import xstampp.astpa.model.extendedData.ExtendedDataController;
import xstampp.astpa.model.extendedData.RefinedSafetyRule;
import xstampp.astpa.model.extendedData.interfaces.IExtendedDataController;
import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.hazacc.IHazAccController;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkController;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.IEntryFilter;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;

/**
 * Manager class for control actions.
 * 
 * @author Jaqueline Patzek, Fabian Toth
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ControlActionController extends Observable implements IControlActionController {

	@XmlElementWrapper(name = "controlactions")
	@XmlElement(name = "controlaction")
	private List<ControlAction> controlActions;

	@XmlElementWrapper(name = "links")
	@XmlElement(name = "link")
	private List<UCAHazLink> links;

	@XmlElementWrapper(name = "rules")
	@XmlElement(name = "rule")
	private List<RefinedSafetyRule> rules;

	@XmlAttribute(name = "nextUcaIndex")
	private Integer nextUcaIndex;

	@XmlAttribute(name = "nextCAIndex")
	private Integer nextCAIndex;

	@XmlElementWrapper(name = "ucaCustomHeaders")
	@XmlElement(name = "ucaHeader")
	private List<String> ucaCustomHeaders;

	private Map<UUID, ControlAction> controlActionsToUcaIds;

	private final Map<UUID, ControlAction> trash;

	/**
	 * Constructor for the controller
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public ControlActionController() {
		this.trash = new HashMap<>();
		this.links = new ArrayList<>();
		this.nextCAIndex = null;
		this.nextUcaIndex = null;
		this.ucaCustomHeaders = null;

	}

	@Override
	public UUID addControlAction(String title, String description) {
		ControlAction controlAction = new ControlAction(title, description, getCANumber());
		this.getControlActions().add(controlAction);
		return controlAction.getId();
	}

	@Override
	public boolean removeControlAction(UUID controlActionId) {
		ControlAction controlAction = this.getInternalControlAction(controlActionId);
		for (IUnsafeControlAction unsafeControlAction : controlAction.getUnsafeControlActions()) {
			this.removeAllLinks(unsafeControlAction.getId());
		}
		int index = this.getControlActions().indexOf(controlAction);
		this.getControlActions().remove(index);
		this.trash.put(controlActionId, controlAction);
		for (; index < this.getControlActions().size(); index++) {
			this.getControlActions().get(index).setNumber(index + 1);
		}
		return true;
	}

	@Override
	public boolean recoverControlAction(UUID id) {
		if ((this.trash.size() > 0) && this.trash.containsKey(id)) {
			return this.getControlActions().add(this.trash.get(id));
		}
		return false;
	}

	@Override
	public ITableModel getControlAction(UUID controlActionId) {
		for (ITableModel controlAction : this.getControlActions()) {
			if (controlAction.getId().equals(controlActionId)) {
				return controlAction;
			}
		}
		return null;
	}

	@Override
	public List<ITableModel> getAllControlActions() {
		List<ITableModel> result = new ArrayList<>();
		for (ControlAction controlAction : this.getControlActions()) {
			result.add(controlAction);
		}
		return result;
	}

	@Override
	public List<IControlAction> getAllControlActionsU() {
		List<IControlAction> result = new ArrayList<>();
		for (IControlAction controlAction : this.getControlActions()) {
			result.add(controlAction);
		}
		return result;
	}

	@Override
	public IControlAction getControlActionU(UUID controlActionId) {
		for (IControlAction controlAction : this.getControlActions()) {
			if (controlAction.getId().equals(controlActionId)) {
				return controlAction;
			}
		}
		return null;
	}

	@Override
	public UUID addUnsafeControlAction(UUID controlActionId, String description,
			UnsafeControlActionType unsafeControlActionType) {
		ControlAction controlAction = this.getInternalControlAction(controlActionId);
		if (controlAction == null) {
			return null;
		}
		UUID ucaId = controlAction.addUnsafeControlAction(getNextUCACount(), description, unsafeControlActionType);
		if (ucaId != null) {
			getControlActionMap().put(ucaId, controlAction);
		}
		return ucaId;
	}

	@Override
	public UUID addUnsafeControlAction(UUID controlActionId, String description,
			UnsafeControlActionType unsafeControlActionType, UUID ucaId) {
		ControlAction controlAction = this.getInternalControlAction(controlActionId);
		if (controlAction == null) {
			return null;
		}
		controlAction.addUnsafeControlAction(getNextUCACount(), description, unsafeControlActionType, ucaId);
		if (ucaId != null) {
			getControlActionMap().put(ucaId, controlAction);
		}
		return ucaId;
	}

	@Override
	public boolean removeUnsafeControlAction(UUID unsafeControlActionId) {
		for (ControlAction controlAction : this.getControlActions()) {
			for (IUnsafeControlAction uca : controlAction.getUnsafeControlActions()) {
				if (uca.getId().equals(unsafeControlActionId)) {
					this.removeAllLinks(unsafeControlActionId);
					if (controlAction.removeUnsafeControlAction(unsafeControlActionId)) {
						getControlActionMap().remove(unsafeControlActionId);
						return true;
					}
					break;
				}
			}
		}
		return false;
	}

	private ControlAction getInternalControlAction(UUID controlActionId) {
		for (ControlAction controlAction : this.getControlActions()) {
			if (controlAction.getId().equals(controlActionId)) {
				return controlAction;
			}
		}
		return null;
	}

	@Override
	public List<UUID> getLinksOfUCA(UUID unsafeControlActionId) {
		List<UUID> result = new ArrayList<>();
		if (links != null) {
			for (UCAHazLink link : this.links) {
				if (link.containsId(unsafeControlActionId)) {
					result.add(link.getHazardId());
				}
			}
		}
		return result;
	}

	@Override
	public boolean addUCAHazardLink(UUID unsafeControlActionId, UUID hazardId) {
		return this.links.add(new UCAHazLink(unsafeControlActionId, hazardId));
	}

	@Override
	public boolean removeUCAHazardLink(UUID unsafeControlActionId, UUID hazardId) {
		return this.links.remove(new UCAHazLink(unsafeControlActionId, hazardId));
	}

	@Override
	public boolean removeAllLinks(UUID id) {
		List<UCAHazLink> toDelete = new ArrayList<>();
		for (UCAHazLink link : this.links) {
			if (link.containsId(id)) {
				toDelete.add(link);
			}
		}
		return this.links.removeAll(toDelete);
	}

	@Override
	public String setUcaDescription(UUID unsafeControlActionId, String description) {
		UnsafeControlAction unsafeControlAction = this.getInternalUnsafeControlAction(unsafeControlActionId);
		if (unsafeControlAction != null) {
			return unsafeControlAction.setDescription(description);
		}
		return null;
	}

	@Override
	public List<ICorrespondingUnsafeControlAction> getAllUnsafeControlActions() {

		return getUCAList(new IEntryFilter<IUnsafeControlAction>() {

			@Override
			public boolean check(IUnsafeControlAction model) {
				return !getLinksOfUCA(model.getId()).isEmpty();
			}
		});
	}

	@Override
	public List<ICorrespondingUnsafeControlAction> getUCAList(IEntryFilter<IUnsafeControlAction> filter) {
		List<ICorrespondingUnsafeControlAction> result = new ArrayList<>();
		for (ControlAction controlAction : this.getControlActions()) {
			for (IUnsafeControlAction unsafeControlAction : controlAction.getUnsafeControlActions()) {
				// an unsafe controlaction is only to be conssidered if it leads to a hazard
				if (filter == null || filter.check(unsafeControlAction)) {
					result.add((ICorrespondingUnsafeControlAction) unsafeControlAction);
				}
			}
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public int getUCANumber(UUID ucaID) {
		for (ControlAction controlAction : this.getControlActions()) {
			for (UnsafeControlAction unsafeControlAction : controlAction.getInternalUnsafeControlActions()) {
				boolean isSearched = unsafeControlAction.getId().equals(ucaID);
				if (isSearched) {
					if (unsafeControlAction.getNumber() == 0) {
						assignUCANumbers();
					}
					return unsafeControlAction.getNumber();
				}
			}
		}
		return -1;
	}

	private void assignUCANumbers() {
		nextUcaIndex = 1;
		for (ControlAction controlAction : this.getControlActions()) {
			for (UnsafeControlAction unsafeControlAction : controlAction.getInternalUnsafeControlActions()) {
				unsafeControlAction.setNumber(nextUcaIndex);
				nextUcaIndex++;
			}
		}
	}

	private int getNextUCACount() {
		if (nextUcaIndex == null) {
			nextUcaIndex = getAllUnsafeControlActions().size() + 1;
		}
		return nextUcaIndex++;
	}

	@Override
	public String setCorrespondingSafetyConstraint(UUID unsafeControlActionId, String safetyConstraintDescription) {
		UnsafeControlAction unsafeControlAction = this.getInternalUnsafeControlAction(unsafeControlActionId);
		if (unsafeControlAction == null) {
			return null;
		}
		String oldTitle = unsafeControlAction.getCorrespondingSafetyConstraint().setTitle(safetyConstraintDescription);
		return oldTitle;
	}

	/**
	 * Gets the unsafe control action in an internal type. Do not use outside the
	 * data model
	 * 
	 * @author Fabian Toth
	 * 
	 * @param unsafeControlActionId
	 *            the id of the unsafe control action
	 * @return the unsafe control action with the given id
	 */
	public UnsafeControlAction getInternalUnsafeControlAction(UUID unsafeControlActionId) {
		for (ControlAction controlAction : this.getControlActions()) {
			for (UnsafeControlAction unsafeControlAction : controlAction.getInternalUnsafeControlActions()) {
				if (unsafeControlAction.getId().equals(unsafeControlActionId)) {
					return unsafeControlAction;
				}
			}
		}
		return null;
	}

	/**
	 * @deprecated Use {@link #getCorrespondingSafetyConstraints(IEntryFilter)}
	 *             instead
	 */
	@Override
	public List<ITableModel> getCorrespondingSafetyConstraints() {
		return getCorrespondingSafetyConstraints(null);
	}
 
	@Override
	public List<ITableModel> getCorrespondingSafetyConstraints(IEntryFilter<IUnsafeControlAction> filter) {
		List<ITableModel> result = new ArrayList<>();
		for (ICorrespondingUnsafeControlAction unsafeControlAction : this.getUCAList(filter)) {
			result.add(unsafeControlAction.getCorrespondingSafetyConstraint());
		}
		return result;
	}

	private void moveRulesInCA() {
		if (rules != null) {
			for (ControlAction controlAction : this.getControlActions()) {
				for (int i = this.rules.size() - 1; i >= 0; i--) {
					if (controlAction.intern_addRefinedRule(rules.get(i))) {
						rules.remove(rules.get(i));
					}
				}
			}
			rules = null;
		}

	}

	@Override
	public void prepareForExport(LinkController linkController, IHazAccController hazAccController,
			ControlStructureController csController, String defaultLabel, IExtendedDataController extendedData) {
		moveRulesInCA();
		for (ControlAction controlAction : this.getControlActions()) {
			controlAction.prepareForExport(extendedData, csController, defaultLabel);
			for (UnsafeControlAction unsafeControlAction : controlAction.getInternalUnsafeControlActions()) {
				List<ITableModel> linkedHazards = new ArrayList<>();
				for (UUID link : linkController.getLinksFor(ObserverValue.UNSAFE_CONTROL_ACTION,
						unsafeControlAction.getId())) {
					linkedHazards.add(hazAccController.getHazard(link));
				}
				Collections.sort(linkedHazards);
				StringBuffer linkString = new StringBuffer();
				String id = "";
				if (linkedHazards.size() == 0) {
					linkString.append(Messages.ControlActionController_NotHazardous);
				} else {

					id = Integer.toString(getUCANumber(unsafeControlAction.getId()));
					for (int i = 0; i < linkedHazards.size(); i++) {
						if (i != 0) {
							linkString.append(", "); //$NON-NLS-1$
						}
						linkString.append("H-" + linkedHazards.get(i).getNumber());
					}
				}
				unsafeControlAction.identifier = id;
				unsafeControlAction.setLinks(linkString.toString());
			}

			for (AbstractLTLProvider rule : controlAction.getAllRefinedRules()) {

				if (rule.getUCALinks() != null) {
					StringBuffer linkString = new StringBuffer();
					for (UUID id : rule.getUCALinks()) {
						List<ITableModel> linkedHazards = new ArrayList<>();
						for (UUID link : this.getLinksOfUCA(id)) {
							linkedHazards.add(hazAccController.getHazard(link));
						}
						Collections.sort(linkedHazards);
						if (linkedHazards.size() == 0) {
							linkString.append(Messages.ControlActionController_NotHazardous);
						} else {

							for (int i = 0; i < linkedHazards.size(); i++) {
								linkString.append("H-" + linkedHazards.get(i).getNumber() + ",");
							}
						}
					}
					if (linkString.length() > 0) {
						((RefinedSafetyRule) rule)
								.setLinks(linkString.toString().substring(0, linkString.length() - 1));
					}
				}
			}
		}
	}

	@Override
	public boolean prepareForSave(ExtendedDataController extendedData, LinkController linkController) {
		moveRulesInCA();
		for (ControlAction controlAction : this.getControlActions()) {
			controlAction.prepareForSave(extendedData);
			for (UnsafeControlAction unsafeControlAction : controlAction.getInternalUnsafeControlActions()) {

				unsafeControlAction.prepareForSave();
			}
		}

		for (UCAHazLink ucaHazLink : getAllUCALinks()) {
			linkController.addLink(ObserverValue.UNSAFE_CONTROL_ACTION, ucaHazLink.getHazardId(),
					ucaHazLink.getUnsafeControlActionId());
		}
		this.links = null;
		boolean isUsed = nextCAIndex != null || nextUcaIndex != null;

		if (this.ucaCustomHeaders != null && this.ucaCustomHeaders.size() == 0) {
			this.ucaCustomHeaders = null;
		}
		isUsed |= this.ucaCustomHeaders != null;

		if (this.controlActions != null && controlActions.isEmpty()) {
			controlActions = null;
		}
		return isUsed || this.controlActions != null;
	}

	@Override
	public List<UCAHazLink> getAllUCALinks() {

		ArrayList<UCAHazLink> list = new ArrayList<UCAHazLink>();
		if (this.links != null) {
			list.addAll(this.links);
		}
		return list;
	}

	@Override
	public boolean setComponentLink(UUID componentLink, UUID caId) {
		ControlAction action = getInternalControlAction(caId);
		if (action != null) {
			return action.setComponentLink(componentLink);
		}
		return false;
	}

	@Override
	public boolean isSafetyCritical(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		return action.isCASafetyCritical();
	}

	@Override
	public boolean setSafetyCritical(UUID caID, boolean isSafetyCritical) {

		ControlAction action = getInternalControlAction(caID);
		if (action == null) {
			return false;
		}
		return action.setSafetyCritical(isSafetyCritical);
	}

	@Override
	public List<NotProvidedValuesCombi> getValuesWhenNotProvided(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		if (action == null) {
			return new ArrayList<>();
		}
		return action.getValuesAffectedWhenNotProvided();
	}

	@Override
	public void setValuesWhenNotProvided(UUID caID, List<NotProvidedValuesCombi> valuesWhenNotProvided) {
		ControlAction action = getInternalControlAction(caID);
		if (action == null) {
			return;
		}
		action.setValuesWhenNotProvided(valuesWhenNotProvided);
	}

	@Override
	public boolean addValueWhenNotProvided(UUID caID, NotProvidedValuesCombi valueWhenNotProvided) {
		ControlAction action = getInternalControlAction(caID);
		if (action == null) {
			return false;
		}
		return action.addValuesWhenNotProvided(valueWhenNotProvided);
	}

	@Override
	public boolean removeValueWhenNotProvided(UUID caID, UUID combieId) {
		ControlAction action = getInternalControlAction(caID);
		if (action == null) {
			return false;
		}
		return action.removeValuesWhenNotProvided(combieId);
	}

	@Override
	public List<ProvidedValuesCombi> getValuesWhenProvided(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		return action.getValuesAffectedWhenProvided();
	}

	@Override
	public void setValuesWhenProvided(UUID caID, List<ProvidedValuesCombi> valuesWhenProvided) {
		ControlAction action = getInternalControlAction(caID);
		action.setValuesWhenProvided(valuesWhenProvided);
	}

	@Override
	public boolean addValueWhenProvided(UUID caID, ProvidedValuesCombi valueWhenProvided) {
		ControlAction action = getInternalControlAction(caID);
		if (action == null) {
			return false;
		}
		return action.addValueWhenProvided(valueWhenProvided);
	}

	@Override
	public boolean removeValueWhenProvided(UUID caID, UUID combieId) {
		ControlAction action = getInternalControlAction(caID);
		if (action == null) {
			return false;
		}
		return action.removeValueWhenProvided(combieId);
	}

	@Override
	public List<UUID> getNotProvidedVariables(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		return action.getNotProvidedVariables();
	}

	@Override
	public void addNotProvidedVariable(UUID caID, UUID notProvidedVariable) {
		ControlAction action = getInternalControlAction(caID);
		action.addNotProvidedVariable(notProvidedVariable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see xstampp.astpa.model.controlaction.IControlActionController#
	 * getProvidedVariables(java.util.UUID)
	 */
	@Override
	public List<UUID> getProvidedVariables(UUID caID) {
		ControlAction action = getInternalControlAction(caID);
		return action.getProvidedVariables();
	}

	@Override
	public void addProvidedVariable(UUID caID, UUID providedVariable) {
		ControlAction action = getInternalControlAction(caID);
		action.addProvidedVariable(providedVariable);
	}

	@Override
	public boolean removeNotProvidedVariable(UUID caID, UUID notProvidedVariable) {
		ControlAction action = getInternalControlAction(caID);
		if (action == null) {
			return false;
		}

		return action.removeNotProvidedVariable(notProvidedVariable);
	}

	@Override
	public boolean removeProvidedVariable(UUID caID, UUID providedVariable) {
		ControlAction action = getInternalControlAction(caID);
		if (action == null) {
			return false;
		}

		return action.removeProvidedVariable(providedVariable);
	}

	@Override
	public List<AbstractLTLProvider> getAllRefinedRules(boolean onlyFormal) {
		// moveRulesInCA();
		List<AbstractLTLProvider> list = new ArrayList<>();
		if (!onlyFormal && rules != null) {
			list.addAll(rules);
		}
		for (ControlAction controlAction : getControlActions()) {
			if (controlAction.getAllRefinedRules() != null) {
				list.addAll(controlAction.getAllRefinedRules());
			}
		}
		Collections.sort(list);
		return list;
	}

	@Override
	public boolean addRefinedRuleLink(UUID ruleID, UUID caID) {
		for (ControlAction controlAction : getControlActions()) {
			if (controlAction.getId().equals(caID)) {
				return controlAction.addRefinedRuleLink(ruleID);
			}
		}
		return false;
	}

	@Override
	public boolean removeSafetyRule(boolean removeAll, UUID id) {
		if (removeAll) {
			this.rules = new ArrayList<>();
		} else if (rules != null) {
			// the rule which should be removed is searched for in both the
			// general rules list and in the control actions
			for (RefinedSafetyRule refinedSafetyRule : rules) {
				if (refinedSafetyRule.getRuleId().equals(id)) {
					return rules.remove(refinedSafetyRule);
				}
			}
		}

		for (ControlAction controlAction : getControlActions()) {
			if (controlAction.removeSafetyRule(removeAll, id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean usesHAZXData() {
		for (ControlAction action : this.getControlActions()) {
			if (action.isCASafetyCritical())
				return true;
			if (!action.getNotProvidedVariables().isEmpty())
				return true;
			if (!action.getProvidedVariables().isEmpty())
				return true;
		}
		return false;
	}

	@Override
	public IControlAction getControlActionFor(UUID ucaId) {
		return getControlActionMap().get(ucaId);
	}

	@Override
	public boolean moveEntry(boolean moveUp, UUID id, ObserverValue value) {
		if (value.equals(ObserverValue.CONTROL_ACTION)) {
			return ATableModel.move(moveUp, id, getControlActions());
		}
		return true;
	}

	@Override
	public void setNextUcaIndext(int nextUcaIndext) {
		this.nextUcaIndex = nextUcaIndext;
	}

	private int getCANumber() {
		if (this.nextCAIndex == null) {
			this.nextCAIndex = this.getControlActions().size() + 1;
		}
		return this.nextCAIndex++;
	}

	private Map<UUID, ControlAction> getControlActionMap() {
		if (this.controlActionsToUcaIds == null) {
			this.controlActionsToUcaIds = new HashMap<>();
			for (ControlAction controlAction : this.getControlActions()) {
				for (IUnsafeControlAction uca : controlAction.getUnsafeControlActions()) {
					this.controlActionsToUcaIds.put(uca.getId(), controlAction);
				}
			}
		}
		return this.controlActionsToUcaIds;
	}

	@Override
	public void setUCACustomHeaders(String[] ucaHeaders) {
		Assert.isTrue(ucaHeaders.length == 4, "The uca label array must always be of size 4");
		boolean isRedundant = true;
		if (this.ucaCustomHeaders == null) {
			this.ucaCustomHeaders = new ArrayList<>(4);
		}
		for (int i = 0; i < ucaHeaders.length; i++) {
			this.ucaCustomHeaders.set(i, ucaHeaders[i]);
			isRedundant = false;
		}
		if (isRedundant) {
			this.ucaCustomHeaders = null;
		} else {
			setChanged();
			notifyObservers(ObserverValue.UNSAFE_CONTROL_ACTION);
		}
	}

	@Override
	public String[] getUCAHeaders() {
		String[] headers = new String[4];
		if (this.ucaCustomHeaders == null) {

			this.ucaCustomHeaders = new ArrayList<>();
			this.ucaCustomHeaders.add(Messages.NotGiven);
			this.ucaCustomHeaders.add(Messages.GivenIncorrectly);
			this.ucaCustomHeaders.add(Messages.WrongTiming);
			this.ucaCustomHeaders.add(Messages.StoppedTooSoon);
		}

		try {
			headers[0] = this.ucaCustomHeaders.get(0);
			headers[1] = this.ucaCustomHeaders.get(1);
			headers[2] = this.ucaCustomHeaders.get(2);
			headers[3] = this.ucaCustomHeaders.get(3);
		} catch (IndexOutOfBoundsException exc) {
			ProjectManager.getLOGGER().error(
					"The array with Unsafe Control Actions types was expected to contain 4 labels but it contained " //$NON-NLS-1$
							+ this.ucaCustomHeaders.size());
			headers = null;
		}

		return headers;
	}

	private List<ControlAction> getControlActions() {
		if (this.controlActions == null) {
			this.controlActions = new ArrayList<>();
		}
		return controlActions;
	}
}
