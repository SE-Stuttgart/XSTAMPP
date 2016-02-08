package xstampp.astpa.model.controlaction.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.model.ILTLProvider;

public interface IHAZXControlAction extends IControlAction {
	/**
	 * @return the componentLink
	 */
	public UUID getComponentLink();
	public List<ILTLProvider> getAllRefinedRules();
}
