package xstampp.astpa.model.controlaction;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IValueCombie {
	static final String CONTEXT_PROVIDED = "provided";
	static final String CONTEXT_NOT_PROVIDED ="not provided";
	
	static final String HAZ_IF_PROVIDED = "provided";
	static final String HAZ_IF_NOT_PROVIDED ="not provided";
	static final String HAZ_IF_WRONG_PROVIDED ="provided with wrong timing";
	
	/**
	 * a string constant for a value combination that always leads to a hazardous situation 
	 * when the parent control action is provided
	 */
	static final String TYPE_ANYTIME ="anytime";
	
	/**
	 * a string constant for a value combination that leads to a hazardous situation 
	 * when the parent control action is provided to late
	 */
	static final String TYPE_TOO_LATE ="provided too late";
	
	/**
	 * a string constant for a value combination that leads to a hazardous situation 
	 * when the parent control action is provided to early
	 */
	static final String TYPE_TOO_EARLY ="provided too early";
	
	/**
	 * a string constant for a value combination that leads to a hazardous situation 
	 * when the parent control action is not provided
	 */
	static final String TYPE_NOT_PROVIDED ="not provided";
	
	/**
	 * @return a map containing the ids of the combined values mapped to their parent
	 * 				variable ids
	 */
	public abstract Map<UUID, UUID> getPMValues();

	public List<UUID> getValueList();
	/**
	 * @return the refinedSC
	 */
	public abstract List<UUID> getRefinedSafetyConstraints();


	/**
	 * @return the constraint
	 */
	public abstract String getSafetyConstraint();

	/**
	 * @param valuesIdsTOvariableIDs a map containing the ids of the combined values mapped to their parent
	 * 				variable ids
	 */
	public abstract void setValues(Map<UUID, UUID> valuesIdsTOvariableIDs);

	/**
	 * @param refinedSC the refinedSC to set
	 */
	public abstract void setRefinedSC(List<UUID> refinedSC);

	
	/**
	 * @param constraint the constraint to set
	 */
	public abstract void setConstraint(String constraint);

	public abstract UUID getCombieId();

	public abstract void setId(UUID id);
	
	void setArchived(boolean archive);
	
	/**
	 * @param type one of the type constants defined in IValueCombie
	 * 
	 * @see #TYPE_ANYTIME
	 * @see #TYPE_TOO_EARLY
	 * @see #TYPE_TOO_LATE
	 * @see #TYPE_NOT_PROVIDED
	 * 
	 * @return the hazardous
	 */
	public abstract boolean isCombiHazardous(String type);
	
	/**
	 * @param type one of the type constants defined in IValueCombie
	 * 
	 * @see #TYPE_ANYTIME
	 * @see #TYPE_TOO_EARLY
	 * @see #TYPE_TOO_LATE
	 * @see #TYPE_NOT_PROVIDED
	 * 
	 * @return the relatedUCAs
	 */
	public List<UUID> getUCALinks(String type);

	/**
	 * @param relatedUCAs the relatedUCAs to set
	 * 
	 * @param type one of the type constants defined in IValueCombie
	 * 
	 * @see #TYPE_ANYTIME
	 * @see #TYPE_TOO_EARLY
	 * @see #TYPE_TOO_LATE
	 * @see #TYPE_NOT_PROVIDED
	 */
	public void setUCALinks(List<UUID> relatedUCAs, String type);
}