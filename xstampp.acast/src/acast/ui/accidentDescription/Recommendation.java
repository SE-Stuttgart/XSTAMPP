package acast.ui.accidentDescription;

public class Recommendation {
	
	private String id;
	private String description;
	
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

	public Recommendation() {
		// TODO Auto-generated constructor stub
	}
	
	public Recommendation(String id ,String description) {
		this.description =description;
		this.id = id;
	}

}
