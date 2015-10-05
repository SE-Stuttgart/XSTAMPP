package acast.model.interfaces;

import java.util.List;

import acast.ui.accidentDescription.ProximalEvent;
import xstampp.model.IDataModel;

public interface IProximalEventsViewDataModel extends IDataModel {
	
	List<ProximalEvent> getEventList();
	
	ProximalEvent getEvent(int id);
	
	boolean setEventList(List<ProximalEvent> list);

}
