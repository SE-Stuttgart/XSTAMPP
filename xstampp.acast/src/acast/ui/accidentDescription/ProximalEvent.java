package acast.ui.accidentDescription;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "event")
public class ProximalEvent {

	public ProximalEvent() {
	}

	private String description;
	private String date;
	private String time;
	private int ID;

	public ProximalEvent(String description, String date, int id) {
		this.description = description;
		this.date = date;
		this.ID = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
