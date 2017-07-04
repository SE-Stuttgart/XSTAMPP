package xstampp.astpa.model.controlstructure.interfaces;

import java.util.UUID;

public interface IAnchor {

  boolean isFlying();

  void setIsFlying(boolean isFlying);

  int getxOrientation();

  int getyOrientation();

  UUID getOwnerId();

}
