package acast.model.controlstructure.interfaces;

public abstract interface IAnchor {
	  
	  public abstract boolean isFlying();
	  
	  public abstract int getxOrientation();
	  
	  public abstract int getyOrientation();
	  
	  public abstract java.util.UUID getOwnerId();
	  
	  public abstract void setIsFlying(boolean arg0);
	}
