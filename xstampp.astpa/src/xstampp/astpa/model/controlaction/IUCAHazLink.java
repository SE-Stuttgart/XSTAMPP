package xstampp.astpa.model.controlaction;

import java.util.UUID;

public interface IUCAHazLink {

  UUID getUnsafeControlActionId();

  UUID getHazardId();

  boolean containsId(UUID id);

}
