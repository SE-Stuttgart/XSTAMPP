package xstpa;

import java.util.ArrayList;
import java.util.List;

public class Relation {
	
	// ==================== 2. Instance Fields ============================
	
	private int strength;
	private List<String> variables = new ArrayList<String>();

	// ==================== 4. Constructors ===============================
	
	public Relation(int strength, List<String> variables) {
		this.setStrength(strength);
		this.setVariables(variables);
	}
	
	// ==================== 5. Creators =============================

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> controlActions) {
		this.variables = controlActions;
	}
}
