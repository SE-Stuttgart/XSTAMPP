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

package xstampp.astpa.model.controlstructure.components;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.model.causalfactor.CausalFactor;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.causalfactor.ICausalFactor;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;

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
@XmlType(propOrder = { "id", "text", "componentType", "controlActionId",
		"layout", "layoutPM", "children", "causalFactors" })
public class Component implements IRectangleComponent, ICausalComponent {

	private UUID id;
	private UUID controlActionId;
	private String text;
	private Rectangle layout;
	private Rectangle layoutPM;
	private ComponentType componentType;

	@XmlElementWrapper(name = "causalFactors")
	@XmlElement(name = "causalFactor")
	private List<CausalFactor> causalFactors;

	@XmlElementWrapper(name = "children")
	@XmlElement(name = "component")
	private List<Component> children;

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
		this.causalFactors = new ArrayList<>();
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
	public Component(UUID caId, String text, Rectangle layout,
			ComponentType type) {
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
		this.causalFactors = new ArrayList<>();
		this.layout = new Rectangle();
		this.layoutPM = new Rectangle();
		// empty constructor for JAXB
	}

	@Override
	public UUID getId() {
		return this.id;
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
	 * @return true if child could be added
	 * 
	 * @author Fabian Toth
	 */
	public boolean addChild(Component child) {
		return this.children.add(child);
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

	@Override
	public ComponentType getComponentType() {
		return this.componentType;
	}

	/**
	 * @param componentType
	 *            the componentType to set
	 */
	public void setComponentType(ComponentType componentType) {
		this.componentType = componentType;
	}

	@Override
	public List<ICausalFactor> getCausalFactors() {
		List<ICausalFactor> result = new ArrayList<>();
		for (CausalFactor causalFactor : this.causalFactors) {
			result.add(causalFactor);
		}
		return result;
	}

	/**
	 * Gets the causal factors in an internal type. Do not use outside the data
	 * model
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the causal factors
	 */
	public List<CausalFactor> getInternalCausalFactors() {
		return this.causalFactors;
	}

	/**
	 * Adds a causal factor to this component
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalFactorText
	 *            the text of the new causal factor
	 * @return the id of the new causal factor. null if the causal could not be
	 *         added
	 */
	public UUID addCausalFactor(String causalFactorText) {
		CausalFactor causalFactor = new CausalFactor(causalFactorText);
		this.causalFactors.add(causalFactor);
		return causalFactor.getId();
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
}
