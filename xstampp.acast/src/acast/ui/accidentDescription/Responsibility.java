package acast.ui.accidentDescription;

import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "responsibility")
public class Responsibility {

	public Responsibility() {
		// TODO Auto-generated constructor stub
	}

	public Responsibility(UUID ident, String id, String description, String name) {
		this.id = id;
		this.description = description;
		this.ident = ident;
		this.name = name;
	}

	public UUID getIdent() {
		return ident;
	}

	public void setIdent(UUID ident) {
		this.ident = ident;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
	private String id;
	private String description;
	private UUID ident;

}