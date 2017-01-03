package xstampp.astpa.model.extendedData.interfaces;

import java.util.List;
import java.util.UUID;

import xstampp.astpa.haz.controlaction.interfaces.IControlAction;
import xstampp.astpa.haz.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.model.interfaces.IExtendedDataModel.RuleType;
import xstampp.model.AbstractLtlProvider;
import xstampp.model.AbstractLtlProviderData;
import xstampp.model.IEntryFilter;
import xstampp.model.IValueCombie;

public interface IExtendedDataController {

  /**
   * adds a rule of the given ruleType and type
   * @param ruleType
   *        
   * @param data
   *          {@link AbstractLtlProviderData}
   * @param caID
   *          {@link AbstractLtlProvider#getRelatedControlActionID()}
   * @param type 
   *          {@link AbstractLtlProvider#getType()}
   * 
   * @see IValueCombie
   * @return the id of rule which has been added, or null if any of the parameters is given as null or 
   *          the given type String is none of the defined<ul>
   *          <li> {@link IValueCombie#TYPE_ANYTIME}
   *          <li> {@link IValueCombie#TYPE_NOT_PROVIDED}
   *          <li> {@link IValueCombie#TYPE_TOO_EARLY}
   *          <li> {@link IValueCombie#TYPE_TOO_LATE}
   */
  UUID addRuleEntry(IExtendedDataModel.RuleType ruleType, AbstractLtlProviderData data, UUID caID, String type);


  /**
   * 
   * @param data
   *          {@link AbstractLtlProviderData}
   * @param ruleId
   *          {@link AbstractLtlProvider#getRuleId()}
   *
   * @param linkedControlActionID
   *          {@link AbstractLtlProvider#getRelatedControlActionID()}
   * @return
   *        The UUID of the Refined rule which was updated in the data model or null if no rule could be found
   *         
   * @see IUnsafeControlAction
   * @see IControlAction
   * @see IValueCombie
   */
  boolean updateRefinedRule(UUID ruleId, AbstractLtlProviderData data, UUID linkedControlActionID);

  List<AbstractLtlProvider> getAllRefinedRules(boolean includeRules, boolean includeScenarios, boolean includeLTL);

  List<AbstractLtlProvider> getAllRefinedRules(IEntryFilter<AbstractLtlProvider> filter);

  /**
   * @param removeAll whether all currently stored RefinedSafetyRule objects should be deleted<br>
   *          when this is true than the ruleId will be ignored
   * @param ruleId an id of a RefinedSafetyRule object stored in a controlAction 
   * 
   * @return whether the delete was successful or not, also returns false if the rule could not be found or the 
   *          id was illegal
   */
  boolean removeRefinedSafetyRule(RuleType type, boolean removeAll, UUID ruleId);

}