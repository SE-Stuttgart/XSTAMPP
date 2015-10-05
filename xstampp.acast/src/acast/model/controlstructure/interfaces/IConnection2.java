package acast.model.controlstructure.interfaces;

public abstract interface IConnection2 extends IComponent2 {
	  
	  public abstract IAnchor getSourceAnchor();
	  
	  public abstract IAnchor getTargetAnchor();
	  
	  public abstract java.lang.Object getConnectionType();
	}
