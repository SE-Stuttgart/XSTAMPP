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

package xstampp.astpa.model.controlstructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.haz.controlstructure.interfaces.IComponent;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.causalfactor.ICausalFactor;
import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.CSConnection;
import xstampp.astpa.model.controlstructure.components.Component;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;

/**
 * Controller-class for working with the control structure diagram
 * 
 * @author Fabian Toth
 * 
 */
public class ControlStructureController {

	@XmlElement(name = "component")
	private Component root;
	
	@XmlElementWrapper(name = "connections")
	@XmlElement(name = "connection")
	private List<CSConnection> connections;

	
	private final Map<UUID, IRectangleComponent> componentTrash;
	private final Map<UUID, Integer> componentIndexTrash;
	private final Map<UUID, IConnection> connectionTrash;
	private final Map<UUID, List<UUID>> removedLinks;
	private boolean initiateStep1;
	private boolean initiateStep2;
	/**
	 * Constructor of the control structure controller
	 * 
	 * @author Fabian Toth
	 */
	public ControlStructureController() {
		this.connections = new ArrayList<>();
		this.componentIndexTrash = new HashMap<>();
		this.componentTrash = new HashMap<>();
		this.connectionTrash = new HashMap<>();
		this.removedLinks = new HashMap<>();
		this.setRoot(new Rectangle(), new String());
		this.initiateStep1=false;
		this.initiateStep2=false;
	}

	/**
	 * Adds a new component to a parent with the given values.
	 * 
	 * @param parentId
	 *            the id of the parent
	 * @param layout
	 *            the layout of the new component
	 * @param text
	 *            the text of the new component
	 * @param type
	 *            the type of the new component
	 * @param index TODO
	 * @return the id of the created component. Null if the component could not
	 *         be added
	 * 
	 * @author Fabian Toth
	 */
	public UUID addComponent(UUID parentId, Rectangle layout, String text,
			ComponentType type, Integer index) {
		return addComponent(null, parentId, layout, text, type, index);
	}

	/**
	 * Adds a new component to a parent with the given values.
	 * @param controlActionId
	 *            an id of a ControlAction
	 * @param parentId
	 *            the id of the parent
	 * @param layout
	 *            the layout of the new component
	 * @param text
	 *            the text of the new component
	 * @param type
	 *            the type of the new component
	 * @param index TODO
	 * 
	 * @return the id of the created component. Null if the component could not
	 *         be added
	 * @author Fabian Toth,Lukas Balzer
	 */
	public UUID addComponent(UUID controlActionId, UUID parentId,
			Rectangle layout, String text, ComponentType type, Integer index) {
		Component newComp = new Component(controlActionId, text, layout, type);
		IComponent parent = this.getInternalComponent(parentId);
		if(parent != null){
			((Component)parent).addChild(newComp,index);
			return newComp.getId();
		}
		return null;
	}

	/**
	 * Creates a new root with the given values.
	 * 
	 * @param layout
	 *            the layout of the new component
	 * @param text
	 *            the text of the new component
	 * @return the id of the created component. Null if the component could not
	 *         be added
	 * 
	 * @author Fabian Toth
	 */
	public UUID setRoot(Rectangle layout, String text) {
		Component newComp = new Component(text, layout, ComponentType.ROOT);
		this.root = newComp;
		return newComp.getId();
	}

	/**
	 * Searches for the component with the given id and changes the layout of it
	 * 
	 * @param componentId
	 *            the id of the component
	 * @param layout
	 *            the new text
	 * @param step1
	 *            if the layout of step 1 should be changed
	 * @return true if the text could be changed
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public boolean changeComponentLayout(UUID componentId, Rectangle layout,
			boolean step1) {
	Component component = this.getInternalComponent(componentId);
		if (component != null) {
			//every time the layout is changed the controller checks if both
			//steps have been initialized and if not synchronizes the two layouts
			if		(step1 && this.initiateStep1){
				this.initiateStep2=true;
				component.setLayout(layout, false);
			}
			else if	(step1 && !this.initiateStep1){
				this.initiateStep2=false;
			}
			else if	(!step1 &&this.initiateStep2){
				this.initiateStep1=false;
			}
			component.setLayout(layout, step1);
			return true;
		}
		return false;
	}

	/**
	 * Searches for the component with the given id and changes the text of it
	 * 
	 * @param componentId
	 *            the id of the component
	 * @param text
	 *            the new text
	 * @return true if the text could be changed
	 * 
	 * @author Fabian Toth
	 */
	public boolean changeComponentText(UUID componentId, String text) {
		Component component = this.getInternalComponent(componentId);
		if (component != null) {
			component.setText(text);
			return true;
		}
		return false;
	}

	/**
	 * Searches recursively for the component with the given id and removes it
	 * 
	 * @param componentId
	 *            the id of the component to delete
	 * @return true if this controller contained the specified element
	 * 
	 * @author Fabian Toth
	 */
	public boolean removeComponent(UUID componentId) {
		if(componentId != null){
			Component component = this.getInternalComponent(componentId);
			this.removeAllLinks(componentId);
			this.componentTrash.put(componentId, component);
			this.componentIndexTrash.put(componentId, this.root.getChildren().indexOf(component));
			return this.root.removeChild(componentId);
		}
		return false;
	}

	/**
	 * This methode recovers a Component which was deleted before, from the
	 * componentTrash
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param parentId
	 *            the id of the parent
	 * @param componentId
	 *            the id of the component to recover
	 * @return whether the component could be recoverd or not
	 */
	public boolean recoverComponent(UUID parentId, UUID componentId) {
		if (this.componentTrash.containsKey(componentId)) {
			Component parent = this.getInternalComponent(parentId);
			boolean success = parent.addChild((Component) this.componentTrash
					.get(componentId),this.componentIndexTrash.get(componentId));
			this.componentTrash.remove(componentId);
			if (this.removedLinks.containsKey(componentId)) {
				for (UUID connectionId : this.removedLinks.get(componentId)) {
					this.recoverConnection(connectionId);
				}
			}
			return success;
		}

		return false;

	}

	/**
	 * Searches recursively for the component with the given id
	 * 
	 * @param componentId
	 *            the id of the child
	 * @return the component with the given id
	 * 
	 * @author Fabian Toth
	 */
	public IRectangleComponent getComponent(UUID componentId) {
		if (this.root == null) {
			return null;
		}
		return this.root.getChild(componentId);
	}

	/**
	 * Gets all components of the root level
	 * 
	 * @return the the components
	 * 
	 * @author Fabian Toth
	 */
	public IRectangleComponent getRoot() {
		return this.root;
	}

	/**
	 * Adds a new connection with the given values
	 * 
	 * @param sourceAnchor
	 *            the anchor at the source component
	 * @param targetAnchor
	 *            the anchor at the target component
	 * @param connectionType
	 *            the type of the connection
	 * @return the id of the new connection
	 * 
	 * @author Fabian Toth
	 */
	public UUID addConnection(Anchor sourceAnchor, Anchor targetAnchor,
			ConnectionType connectionType) {
		CSConnection newConn = new CSConnection(sourceAnchor, targetAnchor,
				connectionType);
		this.connections.add(newConn);
		return newConn.getId();
	}

	/**
	 * Searches for the connection with the given id and changes the connection
	 * type to the new value
	 * 
	 * @param connectionId
	 *            the id of the connection to change
	 * @param connectionType
	 *            the new connection type
	 * @return true if the connection type could be changed
	 * 
	 * @author Fabian Toth
	 */
	public boolean changeConnectionType(UUID connectionId,
			ConnectionType connectionType) {
		IConnection connection = this.getConnection(connectionId);
		if (connection != null) {
			((CSConnection) connection).setConnectionType(connectionType);
			return true;
		}
		return false;
	}

	/**
	 * Searches for the connection with the given id and changes the targetId to
	 * the new value
	 * 
	 * @param connectionId
	 *            the id of the connection to change
	 * @param targetAnchor
	 *            the new source anchor
	 * @return true if the targetId could be changed
	 * 
	 * @author Fabian Toth
	 */
	public boolean changeConnectionTarget(UUID connectionId, Anchor targetAnchor) {
		IConnection connection = this.getConnection(connectionId);
		if (connection != null) {
			((CSConnection) connection).setTargetAnchor(targetAnchor);
			return true;
		}
		return false;
	}

	/**
	 * Searches for the connection with the given id and changes the sourceId to
	 * the new value
	 * 
	 * @param connectionId
	 *            the id of the connection to change
	 * @param sourceAnchor
	 *            the new source anchor
	 * @return true if the sourceId could be changed
	 * 
	 * @author Fabian Toth
	 */
	public boolean changeConnectionSource(UUID connectionId, Anchor sourceAnchor) {
		IConnection connection = this.getConnection(connectionId);
		if (connection != null) {
			((CSConnection) connection).setSourceAnchor(sourceAnchor);
			return true;
		}
		return false;
	}

	/**
	 * Deletes the connection with the given id
	 * 
	 * @param connectionId
	 *            the id of the connection
	 * @return true if this component contained the specified element
	 * 
	 * @author Fabian Toth
	 */
	public boolean removeConnection(UUID connectionId) {
		IConnection connection = this.getConnection(connectionId);
		if (this.connections.remove(connection)) {
			this.connectionTrash.put(connectionId, connection);
			return true;
		}
		return false;
	}

	
	/**
	 * This methode recovers a Connection which was deleted before, from the
	 * connectionTrash
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param connectionId
	 *            the id of the component to recover
	 * @return whether the Connection could be recovered or not
	 */
	public boolean recoverConnection(UUID connectionId) {
		if (this.connectionTrash.containsKey(connectionId)) {
			boolean success = this.connections
					.add((CSConnection) this.connectionTrash.get(connectionId));
			this.connectionTrash.remove(connectionId);
			return success;
		}
		return false;

	}

	/**
	 * Gets the connection with the given id
	 * 
	 * @param connectionId
	 *            the id of the connection
	 * @return the connection with the given id
	 * 
	 * @author Fabian Toth
	 */
	public IConnection getConnection(UUID connectionId) {
		for (IConnection connection : this.connections) {
			if (connection.getId().equals(connectionId)) {
				return connection;
			}
		}
		return null;
	}

	/**
	 * Searches recursively for the internal component with the given id
	 * 
	 * @param componentId
	 *            the id of the child
	 * @return the component with the given id
	 * 
	 * @author Fabian Toth
	 */
	private Component getInternalComponent(UUID componentId) {
		if (this.root == null) {
			return null;
		}
		return this.root.getChild(componentId);
		
	}

	/**
	 * Removes all links that are connected to the component with the given id
	 * 
	 * @author Fabian Toth,Lukas Balzer
	 * 
	 * @param componentId
	 *            the id of the component
	 * @return true if the connections have been deleted
	 */
	private boolean removeAllLinks(UUID componentId) {
		List<IConnection> connectionList = new ArrayList<>();
		this.removedLinks.put(componentId, new ArrayList<UUID>());
		for (CSConnection connection : this.connections) {
			if (connection.connectsComponent(componentId)) {
				UUID tmpID = connection.getId();
				connectionList.add(connection);
				this.connectionTrash.put(tmpID, connection);
				this.removedLinks.get(componentId).add(tmpID);
			}
		}
		return this.connections.removeAll(connectionList);
	}

	/**
	 * Gets all connections of the control structure diagram
	 * 
	 * @author Fabian Toth
	 * 
	 * @return all connections
	 */
	public List<IConnection> getConnections() {
		List<IConnection> result = new ArrayList<>();
		for (CSConnection connection : this.connections) {
			result.add(connection);
		}
		return result;
	}

	/**
	 * Get all causal components
	 * 
	 * @author Fabian Toth
	 * 
	 * @return all causal components
	 */
	public List<ICausalComponent> getCausalComponents() {
		List<ICausalComponent> result = new ArrayList<>();
		if (this.root == null) {
			return result;
		}

		for (Component component : this.root.getInternalChildren()) {
			switch(component.getComponentType()){
			case ACTUATOR:
			case CONTROLLED_PROCESS:
			case CONTROLLER:
			case SENSOR:
				result.add(component);
				//$FALL-THROUGH$
			default:
				break;
			}
		}
		return result;
	}

	/**
	 * Gets all components of an internal type. Do not use outside the data
	 * model.
	 * 
	 * @author Fabian Toth
	 * 
	 * @return all components
	 */
	public List<Component> getInternalComponents() {
		if (this.root == null) {
			return new ArrayList<Component>();
		}
		return this.root.getInternalChildren();
	}

	/**
	 * Removes a causal factor
	 * 
	 * @author Fabian Toth
	 * 
	 * @param causalFactorId
	 *            the id of the causal factor to remove
	 * @return true, if the causal factor has been removed
	 */
	public boolean removeCausalFactor(UUID causalFactorId) {
		for (ICausalComponent comp : this.getCausalComponents()) {
			for (ICausalFactor causalFactor : comp.getCausalFactors()) {
				if (causalFactor.getId().equals(causalFactorId)) {
					return this.getInternalComponent(comp.getId())
							.getInternalCausalFactors().remove(causalFactor);
				}
			}
		}
		return false;
	}

	/**
	 * Overwrites the layout of step3 with the layout of step1
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param id
	 *            the id of the component
	 * @return true, if the layout has been synchronized
	 */
	public boolean sychronizeLayout(UUID id) {
		return this.root.getChild(id).sychronizeLayout();
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the amount of components currently in the trash
	 */
	public int getComponentTrashSize() {
		return this.componentTrash.size();
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the amount of components currently in the trash
	 */
	public int getConnectionTrashSize() {
		return this.connectionTrash.size();
	}
	
	/**
	 * is called the first time the cs is opened sets a boolean which indicates 
	 * that the 1. step must be initialized 	
	 *
	 * @author Lukas Balzer
	 *
	 */
	 public void initializeCSS(){
			this.initiateStep1=true;
	 }
	 

	/**
	 * this funktion 
	 * @param componentId 
	 *            the id of the component
	 * @return the relative of the component which belongs to the given id
	 */
	public UUID getRelativeOfComponent(UUID componentId) {
		return getInternalComponent(componentId).getRelative();
	}

	/**
	 * @param componentId 
	 *            the id of the component
	 * @param relativeId the relative to set
	 */
	public void setRelativeOfComponent(UUID componentId, UUID relativeId) {
		Component comp = getInternalComponent(componentId);
		if(comp != null){
			comp.setRelative(relativeId);
		}
	}
	
	/**
	 * @param componentId  
	 *            the id of the component
	 * @param isSafetyCritical the isSafetyCritical to set
	 */
	public void setSafetyCritical(UUID componentId, boolean isSafetyCritical) {
		Component comp = getInternalComponent(componentId);
		if(comp != null){
			comp.setSafetyCritical(isSafetyCritical);
		}
	}
	
	/**
	 *returns whether a component is safety critical or not
	 * @author Lukas Balzer
	 *
	 * @param componentId 
	 *            the id of the component
	 * @return 
	 * 			if the component is safety critical, also false if the uuid fits
	 * 			no component
	 */
	public boolean isSafetyCritical(UUID componentId) {
		Component comp = getInternalComponent(componentId);
		if(comp != null){
			return comp.isSafetyCritical();
		}
		return false;
	}
	/**
	 * @param componentId 
	 *            the id of the component
	 * @param comment the comment to set
	 */
	public void setComment(UUID componentId, String comment) {
		Component comp = getInternalComponent(componentId);
		if(comp != null){
			comp.setComment(comment);
		}
	}
	
	/**
	 *
	 * @author Lukas
	 *
	 * @param componentId
	 *            the id of the component
	 * @param variableID the variable which should be rmoved
	 * @return whether or not the add was successful, it also returns false if
	 * 			the given uuid belongs to no component
	 */
	public boolean addUnsafeProcessVariable(UUID componentId,UUID variableID){
		Component comp = getInternalComponent(componentId);
		if(comp != null){
			return comp.addUnsafeProcessVariable(variableID);
		}
		return false;
	}
	
	
	/**
	 *
	 * @author Lukas
	 *
	 * @param componentId
	 *            the id of the component
	 * @param variableID the variable which should be rmoved
	 * @return whether or not the remove was successful, it also returns false if
	 * 			the given uuid belongs to no component 
	 */
	public boolean removeUnsafeProcessVariable(UUID componentId,UUID variableID){
		Component comp = getInternalComponent(componentId);
		if(comp != null){
			comp.removeUnsafeProcessVariable(variableID);
		}
		return false;
	}
	
	/**
	 *
	 * @author Lukas
	 *
	 * @param componentId
	 *            the id of the component
	 * @return 
	 * 			a map cointaining all process variables provided as keys to a safe/unsafe boolean  
	 */
	public Map<IRectangleComponent,Boolean> getRelatedProcessVariables(UUID componentId){
		Component comp = getInternalComponent(componentId);
		Map<IRectangleComponent,Boolean> values =new HashMap<>();
		if(comp != null){
			List<UUID> upv= comp.getUnsafeProcessVariables();
			IConnection conn = getConnection(comp.getRelative());
			Component target = getInternalComponent(conn.getTargetAnchor().getOwnerId());
			if(target == null || target.getComponentType() != ComponentType.CONTROLLER){
				return values;
			}
			for(IRectangleComponent child: target.getChildren()){
				if(child.getComponentType() == ComponentType.PROCESS_MODEL){
					for(IRectangleComponent variable: child.getChildren()){
						if(variable.getComponentType() == ComponentType.PROCESS_VARIABLE){
							values.put(variable, upv.contains(variable.getId()));
						}
					}
				}
			}
		}
		return values;
	}
	
	public boolean usesHAZXData(){
		for(Component comp: this.getInternalComponents()){
			if(comp.getComponentType().equals(ComponentType.CONTAINER)) return true;
			if(comp.getComponentType().equals(ComponentType.DASHEDBOX)) return true;
			if(!comp.getComment().isEmpty()) return true;
			if(!comp.getUnsafeProcessVariables().isEmpty()) return true;
		}
		return false;
	}
	
	
}
