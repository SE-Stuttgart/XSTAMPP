package xstampp.astpa.model.controlaction.interfaces;

import java.util.UUID;

public interface IUnsafeControlAction {

  UnsafeControlActionType getType();

  UUID getId();

  String getDescription();

}
