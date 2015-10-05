/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package acast.model.controlstructure.components;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.draw2d.geometry.Rectangle;

import acast.controlstructure.CSAbstractEditor;
import acast.controlstructure.CSEditor;
import acast.model.causalfactor.ICausalComponent;
import acast.model.controlstructure.ControlStructureController;
import acast.model.controlstructure.Responsibility;
import acast.model.controlstructure.TableView;
import acast.model.controlstructure.interfaces.IRectangleComponent;
import acast.ui.accidentDescription.Recommendation;

/**
 * 
 * CSModel is an abstract class which contains all elementary fields and
 * functions for a CSModel
 * 
 * @version 1.1
 * @author Lukas Balzer, Aliaksei Babkovich, Fabian Toth
 * 
 */
@XmlRootElement(name = "component")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "text", "isSafetyCritical", "comment", "componentType", "controlActionId", "layout",
		"layoutPM", "relative", "children", "unsafeVariables", "recommendations", "responsibilities", "context",
		"flaws", "unsafeActions" })
public class Component implements IRectangleComponent, ICausalComponent {

	private UUID id;
	private String comment;
	private UUID relative;
	private UUID controlActionId;
	private String text;
	private boolean isSafetyCritical;
	private Rectangle layout;
	private Rectangle layoutPM;
	private ComponentType componentType;
	private List<Recommendation> recommendations = new ArrayList<Recommendation>();

	private List<Responsibility> responsibilities = new ArrayList<Responsibility>();

	private List<Responsibility> context = new ArrayList<Responsibility>();

	private List<Responsibility> flaws = new ArrayList<Responsibility>();

	private List<Responsibility> unsafeActions = new ArrayList<Responsibility>();

	@XmlElementWrapper(name = "children")
	@XmlElement(name = "component")
	private List<Component> children;

	@XmlElementWrapper(name = "unsafeVariables")
	@XmlElement(name = "unsafeVariable")
	private List<UUID> unsafeVariables;

	/**
	 * Constructs a new component with the given text and layout
	 * 
	 * @param text
	 *            the text of the new component
	 * @param layout
	 *            the layout of the new component
	 * @param type
	 *            the type of the component
	 * 
	 * @author Fabian Toth
	 */
	public Component(String text, Rectangle layout, ComponentType type) {
		this.id = UUID.randomUUID();
		this.controlActionId = null;
		this.text = text;
		this.layout = layout.getCopy();
		this.layoutPM = layout.getCopy();
		this.componentType = type;
		this.children = new ArrayList<>();
		this.recommendations = new ArrayList<>();
		this.responsibilities = new ArrayList<>();
		this.context = new ArrayList<>();
		this.flaws = new ArrayList<>();
		this.unsafeActions = new ArrayList<>();
		if (type.equals(ComponentType.ACTUATOR) || type.equals(ComponentType.CONTROLLER)
				|| type.equals(ComponentType.CONTROLLED_PROCESS) || type.equals(ComponentType.SENSOR)
				|| type.equals(ComponentType.CONTROLACTION)) {

			CSAbstractEditor.identifiers.put(text, id);
		}
	}

	/**
	 * Constructs a new component if the component is a ControlAction this
	 * constructor will link the given caId as referenced control action
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param caId
	 *            the id of the linked ControlAction
	 * @param text
	 *            the text of the new component
	 * @param layout
	 *            the layout of the new component
	 * @param type
	 *            the type of the component
	 */
	public Component(UUID caId, String text, Rectangle layout, ComponentType type) {
		this(text, layout, type);

		if (type != ComponentType.CONTROLACTION) {
			this.controlActionId = null;
		} else {

			this.controlActionId = caId;
		}
	}

	/**
	 * Empty constructor used for JAXB. Do not use it!
	 * 
	 * @author Fabian Toth
	 */
	public Component() {
		this.children = new ArrayList<>();
		this.layout = new Rectangle();
		this.layoutPM = new Rectangle();
		// empty constructor for JAXB
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public ComponentType getComponentType() {
		return this.componentType;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String getText() {
		return this.text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public Rectangle getLayout(boolean step1) {
		if (step1) {
			return this.layout;
		}
		return this.layoutPM;

	}

	/**
	 * @param layout
	 *            the layout to set
	 * @param step1
	 *            if the layout of step 1 should be changed
	 * 
	 * @see CSEditor#ID
	 * @see CSEditorWithPM#ID
	 */
	public void setLayout(Rectangle layout, boolean step1) {
		if (step1) {
			this.layout = layout;
		} else {
			this.layoutPM = layout;

		}
	}

	@Override
	public List<IRectangleComponent> getChildren() {
		List<IRectangleComponent> result = new ArrayList<>();
		for (Component component : this.children) {
			result.add(component);
		}
		return result;
	}

	/**
	 * Gets the children in an internal type. Do not use outside the data model
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the children
	 */
	public List<Component> getInternalChildren() {
		return this.children;
	}

	/**
	 * Gets the child with the given id
	 * 
	 * @param childId
	 *            the id of the child
	 * @return the child with the given id
	 * 
	 * @author Fabian Toth
	 */
	public Component getChild(UUID childId) {
		if (this.id.equals(childId)) {
			return this;
		}
		for (Component component : this.children) {
			Component result = component.getChild(childId);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Adds a chid
	 * 
	 * @param child
	 *            the new child
	 * @param index
	 *            TODO
	 * @return true if child could be added
	 * 
	 * @author Fabian Toth
	 */
	public boolean addChild(Component child, Integer index) {
		if (index < 0 || index > this.children.size()) {
			return this.children.add(child);
		}
		this.children.add(index, child);
		return true;
	}

	/**
	 * Removes a child
	 * 
	 * @param childId
	 *            the id of the child to remove
	 * @return true if the child could be removed
	 * 
	 * @author Fabian Toth
	 */
	public boolean removeChild(UUID childId) {
		for (Component component : this.children) {
			if (component.getId().equals(childId)) {
				return this.children.remove(component);
			}
			if (component.removeChild(childId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param componentType
	 *            the componentType to set
	 */
	public void setComponentType(ComponentType componentType) {
		this.componentType = componentType;
	}

	/**
	 * Sets the layout data of both Steps to the value of step 1.8
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return true, if the layouts have been synchronized
	 */
	public boolean sychronizeLayout() {
		this.layoutPM = this.layout;
		return true;
	}

	@Override
	public UUID getControlActionLink() {
		return this.controlActionId;

	}

	@Override
	public boolean linktoControlAction(UUID controlActionid) {
		this.controlActionId = controlActionid;
		return true;
	}

	public void addResponsibility(String id, String description) {
		responsibilities.add(new Responsibility(id, description));
	}

	public void removeResponsibility(String id) {
		for (Responsibility resp : responsibilities) {
			if (resp.getId().equals(id)) {
				responsibilities.remove(resp);
				return;
			}
		}
	}

	public void updateResponsibility(String id, String newId) {
		for (Responsibility resp : responsibilities) {
			if (resp.getId().equals(id)) {
				resp.setId(newId);
				return;
			}
		}
	}

	public void changeResponsibility(String id, String description) {
		for (Responsibility responsibility : responsibilities) {
			if (responsibility.getId().equals(id)) {
				responsibility.setDescription(description);
				return;
			}
		}
	}

	@XmlTransient
	public List<Responsibility> getResponsibilitiesList() {
		return responsibilities;
	}

	public Responsibility getResponsibility(String id) {
		for (Responsibility responsibility : responsibilities) {
			if (responsibility.getId().equals(id)) {
				return responsibility;
			}
		}
		return null;
	}

	public void addFlaw(String id, String description) {
		flaws.add(new Responsibility(id, description));
	}

	public void removeFlaw(String id) {
		for (Responsibility resp : flaws) {
			if (resp.getId().equals(id)) {
				flaws.remove(resp);
				return;
			}
		}
	}

	public void updateFlaw(String id, String newId) {
		for (Responsibility resp : flaws) {
			if (resp.getId().equals(id)) {
				resp.setId(newId);
				return;
			}
		}
	}

	public void changeFlaw(String id, String description) {
		for (Responsibility responsibility : flaws) {
			if (responsibility.getId().equals(id)) {
				responsibility.setDescription(description);
				return;
			}
		}
	}

	public List<Responsibility> getFlawsList() {
		return flaws;
	}

	public Responsibility getFlaw(String id) {
		for (Responsibility responsibility : flaws) {
			if (responsibility.getId().equals(id)) {
				return responsibility;
			}
		}
		return null;
	}

	public void addContext(String id, String description) {
		context.add(new Responsibility(id, description));
	}

	public void updateContext(String id, String newId) {
		for (Responsibility resp : context) {
			if (resp.getId().equals(id)) {
				resp.setId(newId);
				return;
			}
		}
	}

	public void removeContext(String id) {
		for (Responsibility resp : context) {
			if (resp.getId().equals(id)) {
				context.remove(resp);
				return;
			}
		}
	}

	public void changeContext(String id, String description) {
		for (Responsibility responsibility : context) {
			if (responsibility.getId().equals(id)) {
				responsibility.setDescription(description);
				return;
			}
		}
	}

	public List<Responsibility> getContextList() {
		return context;
	}

	public Responsibility getContext(String id) {
		for (Responsibility responsibility : context) {
			if (responsibility.getId().equals(id)) {
				return responsibility;
			}
		}
		return null;
	}

	public void addUnsafeAction(String id, String description) {
		unsafeActions.add(new Responsibility(id, description));
	}

	public void updateUnsafeAction(String id, String newId) {
		for (Responsibility resp : unsafeActions) {
			if (resp.getId().equals(id)) {
				resp.setId(newId);
				return;
			}
		}
	}

	public void removeUnsafeAction(String id) {
		for (Responsibility resp : unsafeActions) {
			if (resp.getId().equals(id)) {
				unsafeActions.remove(resp);
				return;
			}
		}
	}

	public void changeUnsafeAction(String id, String description) {
		for (Responsibility responsibility : unsafeActions) {
			if (responsibility.getId().equals(id)) {
				responsibility.setDescription(description);
				return;
			}
		}
	}

	public List<Responsibility> getUnsafeActionsList() {
		return unsafeActions;
	}

	public Responsibility getUnsafeActions(String id) {
		for (Responsibility responsibility : unsafeActions) {
			if (responsibility.getId().equals(id)) {
				return responsibility;
			}
		}
		return null;
	}

	public void addRecommendation(String id, String description) {
		recommendations.add(new Recommendation(id, description));
	}

	public void removeRecommendation(String id) {
		for (Recommendation resp : recommendations) {
			if (resp.getId().equals(id)) {
				recommendations.remove(resp);
				return;
			}
		}
	}

	public void updateRecommendation(String id, String newId) {
		for (Recommendation resp : recommendations) {
			if (resp.getId().equals(id)) {
				resp.setId(newId);
				return;
			}
		}
	}

	public void changeRecommendation(String id, String description) {
		for (Recommendation recommendation : recommendations) {
			if (recommendation.getId().equals(id)) {
				recommendation.setDescription(description);
				return;
			}
		}
	}

	public List<Recommendation> getRecommendationList() {
		return recommendations;
	}

	public Recommendation getRecommendation(String id) {
		for (Recommendation recommendation : recommendations) {
			if (recommendation.getId().equals(id)) {
				return recommendation;
			}
		}
		return null;
	}

	@Override
	public UUID getRelative() {
		return this.relative;
	}

	/**
	 * @param relative
	 *            the relative to set
	 */
	public void setRelative(UUID relative) {
		this.relative = relative;
	}

	@Override
	public boolean isSafetyCritical() {
		return this.isSafetyCritical;
	}

	/**
	 * @param isSafetyCritical
	 *            the isSafetyCritical to set
	 */
	public void setSafetyCritical(boolean isSafetyCritical) {
		this.isSafetyCritical = isSafetyCritical;
	}

	@Override
	public String getComment() {
		if (this.comment == null) {
			this.comment = new String();
		}
		return new String(this.comment);
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 
	 *
	 * @author Lukas
	 *
	 * @param variableID
	 *            the variable which should be rmoved
	 * @see ControlStructureController#addUnsafeProcessVariable(UUID, UUID)
	 * 
	 * @return whether or not the add was successful, it also returns false if
	 *         the given uuid belongs to no component
	 */
	public boolean addUnsafeProcessVariable(UUID variableID) {
		if (this.componentType == ComponentType.CONTROLACTION) {
			return false;
		}
		if (this.unsafeVariables == null) {
			this.unsafeVariables = new ArrayList<>();
		}
		return this.unsafeVariables.add(variableID);
	}

	/**
	 * 
	 *
	 * @author Lukas
	 *
	 * @param variableID
	 *            the variable which should be rmoved
	 * 
	 * @see ControlStructureController#removeUnsafeProcessVariable(UUID, UUID)
	 * @return whether or not the remove was successful, it also returns false
	 *         if the given uuid belongs to no component
	 */
	public boolean removeUnsafeProcessVariable(UUID variableID) {
		if (this.unsafeVariables == null) {
			return false;
		}
		return this.unsafeVariables.remove(variableID);
	}

	/**
	 * getter for the unsafe process model variables
	 *
	 * @author Lukas
	 *
	 * @return a list containing all process variables registered as unsafe
	 */
	public List<UUID> getUnsafeProcessVariables() {
		if (this.unsafeVariables == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(this.unsafeVariables);
	}

}
