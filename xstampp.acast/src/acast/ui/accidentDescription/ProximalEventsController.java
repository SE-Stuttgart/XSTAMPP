package acast.ui.accidentDescription;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

public class ProximalEventsController {

	public ProximalEventsController() {
	}

	@XmlElementWrapper(name = "eventList")
	@XmlElement(name = "event")
	private List<ProximalEvent> events = new ArrayList<ProximalEvent>();

	public ProximalEvent getEvent(int id) {
		for (ProximalEvent event : this.events) {
			if (event.getID() == id) {
				return event;
			}
		}
		return null;
	}

	@XmlTransient
	public List<ProximalEvent> getEventList() {
		return this.events;
	}

	public boolean setEventList(List<ProximalEvent> events) {
		this.events = new ArrayList<ProximalEvent>();
		for (ProximalEvent event : events) {
			this.events.add(event);
		}
		return true;
	}

}
