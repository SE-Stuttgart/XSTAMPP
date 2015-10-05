package xstpa;

public class Hazard {

	private int number;
	private String description;
	private String name;
	
	
	public Hazard(int number, String description, String name) {
		this.number = number;
		this.description = description;
		this.name = name;
	}
	
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
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
}
