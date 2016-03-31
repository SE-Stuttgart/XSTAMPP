package xstampp.astpa.model.interfaces;

import java.util.UUID;

import xstampp.model.ObserverValue;

public interface ICommonTables {
	public boolean moveEntry(boolean moveUp,UUID id,ObserverValue value);
}
