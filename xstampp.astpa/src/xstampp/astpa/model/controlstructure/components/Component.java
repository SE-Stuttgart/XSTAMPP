/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.model.causalfactor.CausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.controlstructure.ControlStructureController;
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
@XmlAccessorType(XmlAccessType.NONE)
public class Component implements IRectangleComponent, ICausalComponent,Comparable {

  @XmlElement(name="id")
	private UUID id;

  @XmlElement(name="controlActionId")
	private UUID controlActionId;

  @XmlElement(name="text")
	private String text;

  @XmlElement(name="isSafetyCritical")
	private boolean isSafetyCritical;

  @XmlElement(name="comment")
	private String comment;

  @XmlElement(name="layout")
	private Rectangle layout;

  @XmlElement(name="layoutPM")
	private Rectangle layoutPM;

  @XmlElement(name="componentType")
	private ComponentType componentType;

  @XmlElement(name="relative")
	private UUID relative;

	@XmlElementWrapper(name = "causalFactors")
	@XmlElement(name = "causalFactor")
	private List<CausalFactor> causalFactors;

	@XmlElementWrapper(name = "children")
	@XmlElement(name = "component")
	private List<Component> children;

	@XmlElementWrapper(name = "unsafeVariables")
	@XmlElement(name = "unsafeVariable")
	private List<UUID> unsafeVariables;
	
	
	
	/** 
	 * this comparator compares two Objects from the type IRectangleComponent.
	 * a component of the ComponentType DashedBox is always considered smaller and is therefor 
	 * set below any other component(in any paint job)
	 *
	 * @author Lukas Balzer
	 *
	 */
	private class CSComparator implements  Comparator<xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent>{
		@Override
		public int compare(
				xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent arg0,
				xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent arg1) {
			if(arg0.getComponentType() == ComponentType.DASHEDBOX){
				if(arg1.getComponentType() == ComponentType.DASHEDBOX){
					return 0;
				}
				return -1;
			}else if(arg1.getComponentType() == ComponentType.DASHEDBOX){
				return 1;
			}
			return 0;
		}
	}
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
		return new String(this.text);
	}

	/**
	 * @param text
	 *            the text to set
	 * @return whether the text was changed, or wasn't if the given text equals the current text  
	 */
	public boolean setText(String text) {
		if(this.text.equals(text)){
			return false;
		}
		this.text = text;
		return true;
	}

	@Override
	public Rectangle getLayout(boolean step0) {
		if (step0) {
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
	public void setLayout(Rectangle layout, boolean step0) {
		if (step0) {
			this.layout = layout;
		} else {
			this.layoutPM = layout;
		}
	}

	@Override
	public List<IRectangleComponent> getChildren() {
		return getChildren(false);
	}
	@Override
	public List<IRectangleComponent> getChildren(boolean step0) {
		Collections.sort(this.children);
		List<IRectangleComponent> result = new ArrayList<>();
		for (Component component : this.children) {
			if(!step0 || !component.getComponentType().equals(ComponentType.PROCESS_MODEL)){
				result.add(component);
			}
		}
		return result;
	}
	

	@Override
	public int getChildCount() {
		return children.size();
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
	 * @param index TODO
	 * @return true if child could be added
	 * 
	 * @author Fabian Toth
	 */
	public boolean addChild(Component child, Integer index) {
		if(index < 0 || index >this.children.size()){
			this.children.add(child);
		}else{
			this.children.add(index,child);
		}
		
		Collections.sort(this.children);
		return this.children.contains(child);
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
		if(this.causalFactors != null){
  		for (CausalFactor causalFactor : this.causalFactors) {
  			result.add(causalFactor);
  		}
      this.causalFactors = null;
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
		if(layout.equals(layoutPM)){
			return false;
		}
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
	
	
	@Override
	public UUID getRelative() {
		return this.relative;
	}

	/**
	 * @param relative the relative to set
	 */
	public void setRelative(UUID relative) {
		this.relative = relative;
	}

	@Override
	public boolean isSafetyCritical() {
		return this.isSafetyCritical;
	}

	/**
	 * @param isSafetyCritical the isSafetyCritical to set
	 * @return 
	 */
	public boolean setSafetyCritical(boolean isSafetyCritical) {
	  if(this.isSafetyCritical != isSafetyCritical){
	    this.isSafetyCritical = isSafetyCritical;
	    return true;
	  }
	  return false;
	}

	
	@Override
	public String getComment() {
		if(this.comment == null){
			this.comment = new String();
		}
		return new String(this.comment);
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * 
	 *
	 * @author Lukas
	 *
	 * @param variableID the variable which should be rmoved
	 * @see ControlStructureController#addUnsafeProcessVariable(UUID, UUID)
	 * 
	 * @return whether or not the add was successful, it also returns false if the given uuid belongs to no component
	 */
	public boolean addUnsafeProcessVariable(UUID variableID){
		if(this.componentType == ComponentType.CONTROLACTION){
			return false;
		}
		if(this.unsafeVariables == null){
			this.unsafeVariables = new ArrayList<>();
		}
		return this.unsafeVariables.add(variableID);
	}
	
	/**
	 * 
	 *
	 * @author Lukas
	 *
	 * @param variableID  the variable which should be rmoved  
	 * 
	 * @see ControlStructureController#removeUnsafeProcessVariable(UUID, UUID)
	 * @return whether or not the remove was successful, it also returns false if the given uuid belongs to no component 
	 */
	public boolean removeUnsafeProcessVariable(UUID variableID){
		if(this.unsafeVariables == null){
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
	public List<UUID> getUnsafeProcessVariables(){
		if(this.unsafeVariables == null){
			return new ArrayList<>();
		}
		return new ArrayList<>(this.unsafeVariables);
	}

	@Override
	public int compareTo(Object arg0) {
		Component other= (Component) arg0;
		if(other.getComponentType() == ComponentType.DASHEDBOX){
			if(this.getComponentType() == ComponentType.DASHEDBOX){
				return 0;
			}
			return 1;
		}else if(this.getComponentType() == ComponentType.DASHEDBOX){
			return -1;
		}
		return 0;
	}
}
