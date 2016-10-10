package xstampp.astpa.controlstructure.controller.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.model.controlstructure.components.Anchor;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.model.ObserverValue;

public class CopyComponentCommand extends ControlStructureAbstractCommand {

	private List<IRectangleComponent> copyList;
	private List<IConnection> connectionCopyList;
	private List<UUID> undoList;
	private ArrayList<UUID> undoConnectionList;
	
	private Point position;
	private UUID parentID;
	private Point pastePosition;
	private Map<UUID,UUID> pasteIdToCopyId;
	private Map<IRectangleComponent,UUID> parentIdsToComponents;
	
	/**
	 * A comparator that makes sure that components which are in a child relation which an other component
	 * in the copy scope are added after their parent, because otherwise they wouldn't be drawn at all
	 *  
	 * @author Lukas Balzer
	 *
	 */
	private class CopySorter implements Comparator<IRectangleComponent>{

		@Override
		public int compare(IRectangleComponent partA, IRectangleComponent partB) {
			if(partB.getComponentType().equals(ComponentType.PROCESS_VALUE)){
				return -1;
			}if(partB.getComponentType().equals(ComponentType.PROCESS_VARIABLE) && !partA.getComponentType().equals(ComponentType.PROCESS_VALUE)){
				return -1;
			}
			if(partA.getChildren().contains(partB)){
				return -1;
			}if(partB.getChildren().contains(partA)){
				return 1;
			}
			return 0;
		}
		
	}
	public CopyComponentCommand(IControlStructureEditorDataModel model,
			String stepID) {
		super(model, stepID);
		this.copyList = new ArrayList<>();
		this.connectionCopyList =  new ArrayList<>(); 
		this.undoList = new ArrayList<>(); 
		this.undoConnectionList = new ArrayList<>();
		this.pasteIdToCopyId = new HashMap<>();
		this.parentIdsToComponents = new HashMap<>();
	}
	
	public void addComponent(Object selection, UUID parentID){
		if(selection != null && selection instanceof IRectangleComponent){
			this.copyList.add((IRectangleComponent) selection);
			this.parentIdsToComponents.put((IRectangleComponent) selection,parentID);
			
		}
		if(selection != null && selection instanceof IConnection){
			this.connectionCopyList.add((IConnection) selection);
		}
	}
	
	public void setPastePosition(UUID uuid, int x, int y){
		this.pastePosition = new Point(x, y);
		this.parentID = uuid;
	}
	
	public void setPosition(Point p){
		this.position = p;
	}
	
	@Override
	public boolean canExecute() {
		Collections.sort(this.copyList,new CopySorter());
		ComponentType parentType = this.getDataModel().getComponent(parentID).getComponentType();
		ComponentType childType =this.copyList.get(0).getComponentType();
		
		if(childType.equals(parentType)){
			return false;
		}if(childType.equals(ComponentType.PROCESS_MODEL) && !parentType.equals(ComponentType.CONTROLLER)){
			return false;
		}if(childType.equals(ComponentType.PROCESS_VALUE) && !parentType.equals(ComponentType.PROCESS_VARIABLE)){
			return false;
		}if(childType.equals(ComponentType.PROCESS_VARIABLE) && !parentType.equals(ComponentType.PROCESS_MODEL)){
			return false;
		}
		return this.copyList.size() > 0 && this.position != null && this.parentID != null && this.pastePosition != null;
	}
	
	@Override
	public void execute() {

		this.undoList = new ArrayList<>();
		
		for(IRectangleComponent comp : this.copyList){
			UUID newParentId;
			Rectangle rect = comp.getLayout(this.getStepID().equals(CSEditor.ID));
			int x = rect.x;
			int y = rect.y;
			if(this.pasteIdToCopyId.containsKey(this.parentIdsToComponents.get(comp))){
				newParentId = this.pasteIdToCopyId.get(this.parentIdsToComponents.get(comp));
				
			}else{
				newParentId = this.parentID;
				x -= (this.position.x - this.pastePosition.x);
				y -= this.position.y - this.pastePosition.y;
			}
			
			x= Math.max(x, 0);
			y = Math.max(y, 0);
			
			UUID compId = this.getDataModel().addComponent(newParentId,
														   new Rectangle(x, y, rect.width, rect.height),
														   comp.getText(),
														   comp.getComponentType(), 1);
			this.pasteIdToCopyId.put(comp.getId(), compId);
			this.undoList.add(compId);
		}
		for(IConnection conn: this.connectionCopyList){
			if(this.pasteIdToCopyId.containsKey(conn.getSourceAnchor().getOwnerId()) &&
			   this.pasteIdToCopyId.containsKey(conn.getTargetAnchor().getOwnerId()))
			{
				ConnectionType type = conn.getConnectionType();
				int xOrientation = conn.getSourceAnchor().getxOrientation();
				int yOrientation = conn.getSourceAnchor().getyOrientation();
				boolean isFlying = conn.getSourceAnchor().isFlying();
				UUID ownerId = this.pasteIdToCopyId.get(conn.getSourceAnchor().getOwnerId());
				Anchor source = new Anchor(isFlying, xOrientation, yOrientation, ownerId);
				
				xOrientation = conn.getTargetAnchor().getxOrientation();
				yOrientation = conn.getTargetAnchor().getyOrientation();
				isFlying = conn.getTargetAnchor().isFlying();
				ownerId = this.pasteIdToCopyId.get(conn.getTargetAnchor().getOwnerId());
				Anchor target = new Anchor(isFlying, xOrientation, yOrientation, ownerId);
				
				this.undoConnectionList.add(this.getDataModel().addConnection(source, target, type));
			}
		}
	}
	
	@Override
	public boolean canUndo() {
		return super.canUndo() && this.undoList.size() > 0;
	}
	
	@Override
	public void undo() {
		this.getDataModel().lockUpdate();
		for(UUID compId:this.undoList){
			this.getDataModel().removeComponent(compId);
		}
		for(UUID connId:this.undoConnectionList){
			this.getDataModel().removeConnection(connId);
		}
		this.getDataModel().releaseLockAndUpdate(new ObserverValue[]{ObserverValue.CONTROL_STRUCTURE});
	}
}
