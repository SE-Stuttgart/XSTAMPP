package acast.model.interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import acast.ui.accidentDescription.Responsibility;
import xstampp.model.IDataModel;

/**
 * @author Martin
 *
 */
public interface IResponsibilityDataModel extends IDataModel {

	/**
	 * add Name of the Component to to map
	 *
	 * @param key
	 * @param value
	 */
	public void addComponentName(String key, UUID value);

	/**
	 * remove component name from the map
	 *
	 * @param key
	 */
	public void removeComponentName(String key);

	/**
	 * get the map with all component names
	 *
	 * @return
	 */
	public Map<String, UUID> getComponentNames();

	/**
	 * get a list of reponsibilities for a component with the given uuid
	 *
	 * @param id
	 * @return
	 */
	public List<Responsibility> getResponsibilitiesListforComponent(UUID id);

	/**
	 * get a list of contexts for a component with the given uuid
	 *
	 * @param id
	 * @return
	 */
	public List<Responsibility> getContextListforComponent(UUID id);

	/**
	 * get a list of flaws for a component with the given uuid
	 *
	 * @param id
	 * @return
	 */
	public List<Responsibility> getFlawListforComponent(UUID id);

	/**
	 * get a list of unsafeActions for a component with the given uuid
	 *
	 * @param id
	 * @return
	 */
	public List<Responsibility> getUnsafeActionListforComponent(UUID id);

	/**
	 * get a list of Feedback for a component with the given uuid
	 *
	 * @param id
	 * @return
	 */
	public List<Responsibility> getFeedbackListforComponent(UUID id);

	/**
	 * get a list of coordination for a component with the given uuid
	 *
	 * @param id
	 * @return
	 */
	public List<Responsibility> getCoordinationListforComponent(UUID id);

	/**
	 * get a list of recommendations for a component with the given uuid
	 *
	 * @param id
	 * @return
	 */
	public List<Responsibility> getRecommendationListforComponent(UUID id);

	/**
	 * get a list of all responsibilities
	 *
	 * @return
	 */
	public List<Responsibility> getResponsibilitiesList();

	/**
	 * get a responsibility with the given uuid an the id
	 *
	 * @param ident
	 * @param id
	 * @return
	 */
	public Responsibility getResponsibility(UUID ident, String id);

	/**
	 * add ad Responsibility to the list
	 *
	 * @param ident
	 * @param id
	 * @param description
	 * @param name
	 */
	public void addResponsibility(UUID ident, String id, String description, String name);

	/**
	 * change values of the responsibility for the given id
	 *
	 * @param ident
	 * @param oldId
	 * @param newId
	 * @param newDescription
	 * @param name
	 */
	public void changeResponsibility(UUID ident, String oldId, String newId, String newDescription, String name);

	/**
	 * removes responsiblity from the Responsibility list
	 *
	 * @param ident
	 * @param id
	 */
	public void removeResponsibility(UUID ident, String id);

	/**
	 * get the list with all contexts
	 *
	 * @return
	 */
	public List<Responsibility> getContextList();

	/**
	 * get a context with the given uuid and id
	 *
	 * @param ident
	 * @param id
	 * @return
	 */
	public Responsibility getContext(UUID ident, String id);

	/**
	 * adds an context to the contextList
	 *
	 * @param ident
	 * @param id
	 * @param description
	 * @param name
	 */
	public void addContext(UUID ident, String id, String description, String name);

	/**
	 * changes values of the context with the given id and uuid
	 *
	 * @param ident
	 * @param oldId
	 * @param newId
	 * @param newDescription
	 * @param name
	 */
	public void changeContext(UUID ident, String oldId, String newId, String newDescription, String name);

	/**
	 * removes a context with the given uuid and id from the contextList
	 *
	 * @param ident
	 * @param id
	 */
	public void removeContext(UUID ident, String id);

	/**
	 * get a list with all flaws
	 *
	 * @return
	 */
	public List<Responsibility> getFlawsList();

	/**
	 * get a flaw with the given uuid and id
	 *
	 * @param ident
	 * @param id
	 * @return
	 */
	public Responsibility getFlaw(UUID ident, String id);

	/**
	 * adds a flaw to the flawList
	 *
	 * @param ident
	 * @param id
	 * @param description
	 * @param name
	 */
	public void addFlaw(UUID ident, String id, String description, String name);

	/**
	 * changes the values of a flaw in the flawList
	 *
	 * @param ident
	 * @param oldId
	 * @param newId
	 * @param newDescription
	 * @param name
	 */
	public void changeFlaw(UUID ident, String oldId, String newId, String newDescription, String name);

	/**
	 * removes a flaw from the list
	 *
	 * @param ident
	 * @param id
	 */
	public void removeFlaw(UUID ident, String id);

	/**
	 * get all unsafeActions in a list
	 *
	 * @return
	 */
	public List<Responsibility> getUnsafeActionsList();

	/**
	 * get a unsafeAction with the given uuid and id
	 *
	 * @param ident
	 * @param id
	 * @return
	 */
	public Responsibility getUnsafeAction(UUID ident, String id);

	/**
	 * add a unsafeAction to the unsafeActionList
	 *
	 * @param ident
	 * @param id
	 * @param description
	 * @param name
	 */
	public void addUnsafeAction(UUID ident, String id, String description, String name);

	/**
	 * changes values of the unsafeAction in the unsafeActionList
	 *
	 * @param ident
	 * @param oldId
	 * @param newId
	 * @param newDescription
	 * @param name
	 */
	public void changeUnsafeAction(UUID ident, String oldId, String newId, String newDescription, String name);

	/**
	 * removes a unsafeActions with the given uuid an id
	 *
	 * @param ident
	 * @param id
	 */
	public void removeunsafeAction(UUID ident, String id);

	/**
	 * get a list with all recommendations
	 *
	 * @return
	 */
	public List<Responsibility> getRecommendationList();

	/**
	 * get the recommendation with the given uuid and id
	 *
	 * @param ident
	 * @param id
	 * @return
	 */
	public Responsibility getRecommendation(UUID ident, String id);

	/**
	 * adds a recommendation to the recommandationList
	 *
	 * @param ident
	 * @param id
	 * @param description
	 * @param name
	 */
	public void addRecommendation(UUID ident, String id, String description, String name);

	/**
	 * change the values of a recommendation in the recommendationList
	 *
	 * @param ident
	 * @param oldId
	 * @param newId
	 * @param newDescription
	 * @param name
	 */
	public void changeRecommendation(UUID ident, String oldId, String newId, String newDescription, String name);

	/**
	 * removes the recommendation with the given uuid and id from the
	 * recommendationList
	 *
	 * @param ident
	 * @param id
	 */
	public void removeRecommendation(UUID ident, String id);

	/**
	 * get a list with all feedback
	 *
	 * @return
	 */
	public List<Responsibility> getFeedbackList();

	/**
	 * get the feedback with the given uuid and id
	 *
	 * @param ident
	 * @param id
	 * @return
	 */
	public Responsibility getFeedback(UUID ident, String id);

	/**
	 * adds a recommendation to the feedbackList
	 *
	 * @param ident
	 * @param id
	 * @param description
	 * @param name
	 */
	public void addFeedback(UUID ident, String id, String description, String name);

	/**
	 * change the values of a feedback in the feedbackList
	 *
	 * @param ident
	 * @param oldId
	 * @param newId
	 * @param newDescription
	 * @param name
	 */
	public void changeFeedback(UUID ident, String oldId, String newId, String newDescription, String name);

	/**
	 * removes the feedback with the given uuid and id from the feedbackList
	 *
	 * @param ident
	 * @param id
	 */
	public void removeFeedback(UUID ident, String id);

	/**
	 * get a list with all Coordination
	 *
	 * @return
	 */
	public List<Responsibility> getCoordinationList();

	/**
	 * get the Coordination with the given uuid and id
	 *
	 * @param ident
	 * @param id
	 * @return
	 */
	public Responsibility getCoordination(UUID ident, String id);

	/**
	 * adds a Coordination to the CoordinationList
	 *
	 * @param ident
	 * @param id
	 * @param description
	 * @param name
	 */
	public void addCoordination(UUID ident, String id, String description, String name);

	/**
	 * change the values of a Coordination in the CoordinationList
	 *
	 * @param ident
	 * @param oldId
	 * @param newId
	 * @param newDescription
	 * @param name
	 */
	public void changeCoordination(UUID ident, String oldId, String newId, String newDescription, String name);

	/**
	 * removes the Coordination with the given uuid and id from the
	 * recommendationList
	 *
	 * @param ident
	 * @param id
	 */
	public void removeCoordination(UUID ident, String id);

}
