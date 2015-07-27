package xstampp.astpa.model.controlaction.interfaces;

import java.util.UUID;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;

public interface IHAZXControlAction extends IControlAction {
	/**
	 * @return the componentLink
	 */
	public UUID getComponentLink();
}
