package acast.model.controlstructure.interfaces;

public abstract interface IRectangleComponent2 extends IComponent2 {
	  
	  public abstract org.eclipse.draw2d.geometry.Rectangle getLayout(boolean arg0);
	  
	  public abstract java.lang.String getText();
	  
	  public abstract java.util.UUID getControlActionLink();
	  
	  public abstract boolean linktoControlAction(java.util.UUID arg0);
	 
	  public abstract java.util.List getChildren();
	}
