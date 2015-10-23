package acast.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import acast.ui.accidentDescription.Responsibility;

public class ResponsibilityController {

	public ResponsibilityController() {
		this.responsibilities = new ArrayList<>();
		this.contexts = new ArrayList<>();
		this.flaws = new ArrayList<>();
		this.unsafeActions = new ArrayList<>();
		this.recommendations = new ArrayList<>();
		this.feedback = new ArrayList<>();
		this.coordination = new ArrayList<>();
		this.componentNames = new HashMap<>();
	}

	@XmlElementWrapper(name = "responsibilities")
	@XmlElement(name = "responsibility")
	private List<Responsibility> responsibilities;

	@XmlElementWrapper(name = "contexts")
	@XmlElement(name = "context")
	private List<Responsibility> contexts;

	@XmlElementWrapper(name = "flaws")
	@XmlElement(name = "flaw")
	private List<Responsibility> flaws;

	@XmlElementWrapper(name = "unsafeActions")
	@XmlElement(name = "unsafeAction")
	private List<Responsibility> unsafeActions;

	@XmlElementWrapper(name = "recommendations")
	@XmlElement(name = "recommendation")
	private List<Responsibility> recommendations;

	@XmlElementWrapper(name = "feedbacks")
	@XmlElement(name = "feedback")
	private List<Responsibility> feedback;

	@XmlElementWrapper(name = "coordinations")
	@XmlElement(name = "coordination")
	private List<Responsibility> coordination;

	@XmlElementWrapper(name = "componentNames")
	private java.util.Map<String, UUID> componentNames;

	public void addComponentName(String key, UUID value) {
		this.componentNames.put(key, value);
	}

	public void removeComponentName(String key) {
		this.componentNames.remove(key);
	}

	public Map<String, UUID> getComponentNames() {
		return this.componentNames;
	}

	public int getIndexOfElement(List<Responsibility> list, Responsibility resp) {
		int i = 0;
		for (Responsibility r : list) {
			if (r.getId().equals(resp.getId()) && r.getIdent().equals(resp.getIdent())) {
				return i;
			}
			i++;
		}
		return 0;
	}

	public List<Responsibility> getResponsibilitiesList() {
		return this.responsibilities;
	}

	public Responsibility getResponsibility(UUID ident, String id) {
		for (Responsibility resp : this.responsibilities) {
			if (resp.getIdent().equals(ident) && resp.getId().equals(id)) {
				return resp;
			}
		}
		return null;
	}

	public void addResponsibility(UUID ident, String id, String description, String name) {
		this.responsibilities.add(new Responsibility(ident, id, description, name));
	}

	public void changeResponsibility(UUID ident, String oldId, String newId, String newDescription, String name) {
		this.responsibilities.set(getIndexOfElement(this.responsibilities, getResponsibility(ident, oldId)),
				new Responsibility(ident, newId, newDescription, name));
	}

	public void removeResponsibility(UUID ident, String id) {
		this.responsibilities.remove(getIndexOfElement(this.responsibilities, getResponsibility(ident, id)));
	}

	public List<Responsibility> getContextList() {
		return this.contexts;
	}

	public Responsibility getContext(UUID ident, String id) {
		for (Responsibility resp : this.contexts) {
			if (resp.getIdent().equals(ident) && resp.getId().equals(id)) {
				return resp;
			}
		}
		return null;
	}

	public void addContext(UUID ident, String id, String description, String name) {
		this.contexts.add(new Responsibility(ident, id, description, name));
	}

	public void changeContext(UUID ident, String oldId, String newId, String newDescription, String name) {
		this.contexts.set(getIndexOfElement(contexts, getContext(ident, oldId)),
				new Responsibility(ident, newId, newDescription, name));
	}

	public void removeContext(UUID ident, String id) {
		this.contexts.remove(getIndexOfElement(contexts, getContext(ident, id)));
	}

	public List<Responsibility> getFlawsList() {
		return this.flaws;
	}

	public Responsibility getFlaw(UUID ident, String id) {
		for (Responsibility resp : this.flaws) {
			if (resp.getIdent().equals(ident) && resp.getId().equals(id)) {
				return resp;
			}
		}
		return null;
	}

	public void addFlaw(UUID ident, String id, String description, String name) {
		this.flaws.add(new Responsibility(ident, id, description, name));
	}

	public void changeFlaw(UUID ident, String oldId, String newId, String newDescription, String name) {
		this.flaws.set(getIndexOfElement(flaws, getFlaw(ident, oldId)),
				new Responsibility(ident, newId, newDescription, name));
	}

	public void removeFlaw(UUID ident, String id) {
		this.flaws.remove(getIndexOfElement(flaws, getFlaw(ident, id)));
	}

	public List<Responsibility> getUnsafeActionsList() {
		return this.unsafeActions;
	}

	public Responsibility getUnsafeAction(UUID ident, String id) {
		for (Responsibility resp : this.unsafeActions) {
			if (resp.getIdent().equals(ident) && resp.getId().equals(id)) {
				return resp;
			}
		}
		return null;
	}

	public void addUnsafeAction(UUID ident, String id, String description, String name) {
		this.unsafeActions.add(new Responsibility(ident, id, description, name));
	}

	public void changeUnsafeAction(UUID ident, String oldId, String newId, String newDescription, String name) {
		this.unsafeActions.set(getIndexOfElement(unsafeActions, getUnsafeAction(ident, oldId)),
				new Responsibility(ident, newId, newDescription, name));
	}

	public void removeunsafeAction(UUID ident, String id) {
		this.unsafeActions.remove(getIndexOfElement(unsafeActions, getUnsafeAction(ident, id)));
	}

	public List<Responsibility> getRecommendationList() {
		return this.recommendations;
	}

	public Responsibility getRecommendation(UUID ident, String id) {
		for (Responsibility resp : this.recommendations) {
			if (resp.getIdent().equals(ident) && resp.getId().equals(id)) {
				return resp;
			}
		}
		return null;
	}

	public void addRecommendation(UUID ident, String id, String description, String name) {
		this.recommendations.add(new Responsibility(ident, id, description, name));
	}

	public void changeRecommendation(UUID ident, String oldId, String newId, String newDescription, String name) {
		this.recommendations.set(getIndexOfElement(recommendations, getRecommendation(ident, oldId)),
				new Responsibility(ident, newId, newDescription, name));
	}

	public void removeRecommendation(UUID ident, String id) {
		this.recommendations.remove(getIndexOfElement(recommendations, getRecommendation(ident, id)));
	}

	public List<Responsibility> getFeedbackList() {
		return this.feedback;
	}

	public Responsibility getFeedback(UUID ident, String id) {
		for (Responsibility resp : this.feedback) {
			if (resp.getIdent().equals(ident) && resp.getId().equals(id)) {
				return resp;
			}
		}
		return null;
	}

	public void addFeedback(UUID ident, String id, String description, String name) {
		this.feedback.add(new Responsibility(ident, id, description, name));
	}

	public void changeFeedback(UUID ident, String oldId, String newId, String newDescription, String name) {
		this.feedback.set(getIndexOfElement(feedback, getFeedback(ident, oldId)),
				new Responsibility(ident, newId, newDescription, name));
	}

	public void removeFeedback(UUID ident, String id) {
		this.feedback.remove(getIndexOfElement(feedback, getFeedback(ident, id)));
	}

	public List<Responsibility> getCoordinationList() {
		return this.coordination;
	}

	public Responsibility getCoordination(UUID ident, String id) {
		for (Responsibility resp : this.coordination) {
			if (resp.getIdent().equals(ident) && resp.getId().equals(id)) {
				return resp;
			}
		}
		return null;
	}

	public void addCoordination(UUID ident, String id, String description, String name) {
		this.coordination.add(new Responsibility(ident, id, description, name));
	}

	public void changeCoordination(UUID ident, String oldId, String newId, String newDescription, String name) {
		this.coordination.set(getIndexOfElement(coordination, getCoordination(ident, oldId)),
				new Responsibility(ident, newId, newDescription, name));
	}

	public void removeCoordination(UUID ident, String id) {
		this.coordination.remove(getIndexOfElement(coordination, getCoordination(ident, id)));
	}

	public List<Responsibility> getResponsibilitiesListforComponent(UUID id) {
		List<Responsibility> list = new ArrayList<>();
		if (!responsibilities.isEmpty()) {
			for (Responsibility resp : this.responsibilities) {
				if (resp.getIdent().equals(id)) {
					list.add(resp);
				}
			}
		}
		return list;
	}

	public List<Responsibility> getContextListforComponent(UUID id) {
		List<Responsibility> list = new ArrayList<>();
		if (!contexts.isEmpty()) {
			for (Responsibility resp : this.contexts) {
				if (resp.getIdent().equals(id)) {
					list.add(resp);
				}
			}
		}
		return list;
	}

	public List<Responsibility> getFlawListforComponent(UUID id) {
		List<Responsibility> list = new ArrayList<>();
		if (!flaws.isEmpty()) {
			for (Responsibility resp : this.flaws) {
				if (resp.getIdent().equals(id)) {
					list.add(resp);
				}
			}
		}
		return list;
	}

	public List<Responsibility> getUnsafeActionListforComponent(UUID id) {
		List<Responsibility> list = new ArrayList<>();
		if (!unsafeActions.isEmpty()) {
			for (Responsibility resp : this.unsafeActions) {
				if (resp.getIdent().equals(id)) {
					list.add(resp);
				}
			}
		}
		return list;
	}

	public List<Responsibility> getRecommendationListforComponent(UUID id) {
		List<Responsibility> list = new ArrayList<>();
		if (!recommendations.isEmpty()) {
			for (Responsibility resp : this.recommendations) {
				if (resp.getIdent().equals(id)) {
					list.add(resp);
				}
			}
		}
		return list;
	}

	public List<Responsibility> getFeedbackListforComponent(UUID id) {
		List<Responsibility> list = new ArrayList<>();
		if (!feedback.isEmpty()) {
			for (Responsibility resp : this.feedback) {
				if (resp.getIdent().equals(id)) {
					list.add(resp);
				}
			}
		}
		return list;
	}

	public List<Responsibility> getCoordinationListforComponent(UUID id) {
		List<Responsibility> list = new ArrayList<>();
		if (!coordination.isEmpty()) {
			for (Responsibility resp : this.coordination) {
				if (resp.getIdent().equals(id)) {
					list.add(resp);
				}
			}
		}
		return list;
	}

}
