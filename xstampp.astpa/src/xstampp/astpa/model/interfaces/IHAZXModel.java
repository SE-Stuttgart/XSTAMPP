package xstampp.astpa.model.interfaces;

import java.util.List;

import xstampp.astpa.haz.IHAZModel;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;

public interface IHAZXModel extends IHAZModel {
	
	@Override
	public List<IConnection> getConnections();
}
