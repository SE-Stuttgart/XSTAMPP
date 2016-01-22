package acast.model.interfaces;

import java.util.List;

import xstampp.model.IDataModel;
import acast.ui.accidentDescription.ProximalEvent;

public interface IProximalEventsViewDataModel extends IDataModel {

	/**
	 * get the proximal events in a list
	 * 
	 * @return
	 */
	List<ProximalEvent> getEventList();

	/**
	 * get a single event with the given id
	 * 
	 * @param id
	 * @return
	 */
	ProximalEvent getEvent(int id);

	/**
	 * sets the List with all Events
	 * 
	 * @param list
	 * @return
	 */
	boolean setEventList(List<ProximalEvent> list);

}
