package acast.model.controlstructure;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "responsibility")
public class Responsibility {

	public Responsibility() {
		// TODO Auto-generated constructor stub
	}
	
	public Responsibility(String id, String description) {
		this.id= id;
		this.description = description;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private String id;
	private String description;
	

	
}
