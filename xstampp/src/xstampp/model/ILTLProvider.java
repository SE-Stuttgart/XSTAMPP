package xstampp.model;

import java.util.List;
import java.util.UUID;

public interface ILTLProvider extends Comparable<ILTLProvider>{
	/**
	 * @return the ltlProperty
	 */
	public String getLtlProperty();
	
	/**
	 * @return the refinedUCA
	 */
	public String getRefinedUCA();
	
	/**
	 * @return the rule
	 */
	public String getSafetyRule();
	
	public int getNumber();
	
	/**
	 * @return the controlActionID
	 */
	public UUID getRelatedControlActionID();

	public List<UUID> getUCALinks();
	
	/**
	 * @return the refinedSafetyConstraint
	 */
	public String getRefinedSafetyConstraint();
	
	/**
	 * @return the id
	 */
	public UUID getRuleId();
	

	public String getType();
}
