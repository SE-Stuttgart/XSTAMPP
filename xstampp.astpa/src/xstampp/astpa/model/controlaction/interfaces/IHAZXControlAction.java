package xstampp.astpa.model.controlaction.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.model.AbstractLTLProvider;

public interface IHAZXControlAction extends IControlAction {
	/**
	 * @return the componentLink
	 */
	public UUID getComponentLink();
	public List<AbstractLTLProvider> getAllRefinedRules();
}
