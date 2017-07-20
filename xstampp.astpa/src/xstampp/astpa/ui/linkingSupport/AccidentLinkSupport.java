package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.astpa.model.interfaces.IAccidentModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.ObserverValue;

public class AccidentLinkSupport<M extends IAccidentModel> extends LinkSupport<M> {

	public AccidentLinkSupport(M dataInterface) {
		super(dataInterface);
	}

	@Override
	List<UUID> getAvailable() {
		setModelList(getDataInterface().getAllAccidents());
		List<UUID> result = new ArrayList<>();
		for (ITableModel haz : getModelList()) {
			result.add(haz.getId());
		}
		result.removeAll(fetch());
		return result;
	}

	@Override
	protected String getLiteral() {
		return "A-";
	}

	@Override
	public String getTitle() {
		return "Accident Links";
	}

	@Override
	public ObserverValue getLinkType() {
		return ObserverValue.HAZ_ACC_LINK;
	}

}
