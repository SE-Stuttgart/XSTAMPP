package xstampp.stpapriv.model.results;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.IExtendedDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.AbstractLTLProvider;
import xstampp.model.ObserverValue;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.stpapriv.model.controlaction.ControlAction;
import xstampp.stpapriv.model.controlaction.UnsecureControlAction;


public class ConstraintResultController extends Observable implements Observer {
	private PrivacyController model;
	private Map<UUID, ConstraintResult> constraintResults;

	@XmlElementWrapper(name = "constraintResults")
	@XmlElement(name = "constraintResult")
	private List<ConstraintResult> constraintResultList;

	@XmlTransient
	public Map<UUID, ConstraintResult> getConstraintResults() {
		return constraintResults;
	}

	public void setConstraintResults(Map<UUID, ConstraintResult> constraintResults) {
		this.constraintResults = constraintResults;
	}

	public void setModel(PrivacyController model) {
		this.model = model;
	}

	public ConstraintResultController() {
	}

	public ConstraintResultController(IExtendedDataModel model) {

		super();
		this.model = (PrivacyController) model;
		constraintResults = new HashMap<UUID, ConstraintResult>();
		clear();

	}

	public void clear() {
		// this.linkedCAE = null;
		this.constraintResults.clear();
		if (getModel() != null) {
			this.fetchSystemSecurityConstraints();
			this.fetchCorrespondingSecurityConstraints();
			this.fetchCausalSecurityConstraints();

		}
	}

	/**
	 * @return the valuesList
	 * @see ProcessModelValue
	 */
	@XmlTransient
	public List<ConstraintResult> getValuesList() {
		fetchSystemSecurityConstraints();
		fetchCorrespondingSecurityConstraints();
		fetchCausalSecurityConstraints();
		addRelated();
		boolean set = false;
		boolean set2 = false;
		boolean set3 = false;
		ArrayList<ConstraintResult> returnedValues = new ArrayList<>();

		for (ConstraintResult value : this.constraintResults.values()) {
			returnedValues.add(value);

		}
		Collections.sort(returnedValues);
		for (ConstraintResult results : returnedValues) {
			if (results.getIdCompare()[0] == 2 && set == false && (results.isSafe() || results.isSecure()|| results.isPrivate())) {
				results.setStpastep("STPA-Sec Step 2");
				set = true;
			} else if (results.getIdCompare()[0] == 1 && set2 == false && (results.isSafe() || results.isSecure()|| results.isPrivate())) {
				results.setStpastep("STPA-Sec Step 1");
				set2 = true;
			} else if (results.getIdCompare()[0] == 0 && set3 == false) {
				results.setStpastep("STPA-Sec Step 0");
				set3 = true;
			}
		}
		this.constraintResultList = returnedValues;
		return returnedValues;
	}

	@XmlTransient
	public int getValueCount() {
		return this.constraintResults.size();
	}

	@XmlTransient
	public int getSafeValueCount() {
		List<ConstraintResult> results = getValuesList();
		int i = 0;
		for (int j = 0; j < results.size(); j++) {
			if (results.get(j).getIdCompare()[0] != 0) {
				if (results.get(j).isSafe() && !(results.get(j).isSecure())&& !(results.get(j).isPrivate())) {
					i++;
				}
			}
		}
		return i;
	}

	@XmlTransient
	public int getSecureValueCount() {
		List<ConstraintResult> results = getValuesList();
		int i = 0;
		for (int j = 0; j < results.size(); j++) {
			if (results.get(j).getIdCompare()[0] != 0) {
				if (results.get(j).isSecure() && !(results.get(j).isSafe())&& !(results.get(j).isPrivate())) {
					i++;
				}
			}

		}
		return i;
	}
	
	@XmlTransient
	public int getPrivateValueCount() {
		List<ConstraintResult> results = getValuesList();
		int i = 0;
		for (int j = 0; j < results.size(); j++) {
			if (results.get(j).getIdCompare()[0] != 0) {
				if (results.get(j).isPrivate() && !(results.get(j).isSafe())&& !(results.get(j).isSecure())) {
					i++;
				}
			}

		}
		return i;
	}

	@XmlTransient
	public int getBothValueCount() {
		List<ConstraintResult> results = getValuesList();
		int i = 0;
		for (int j = 0; j < results.size(); j++) {
			if (results.get(j).getIdCompare()[0] != 0) {
				if (results.get(j).isSecure() && (results.get(j).isSafe())) {
					i++;
				}
			}
		}
		return i;
	}

	public void fetchSystemSecurityConstraints() {
		constraintResults = new HashMap<UUID, ConstraintResult>();
		// get the controlActions
		boolean set = false;
		for (ITableModel entry : getModel().getAllSafetyConstraints()) {
			ConstraintResult temp = new ConstraintResult();
			temp.setScId("SC0." + entry.getNumber());
			temp.setId(entry.getId());
			if (set == false) {
				temp.setStpastep("");
				set = true;
			}

			temp.setSecurityConstraint(entry.getTitle());
			this.constraintResults.put(temp.getId(), temp);
		}
		set = false;
	}

	public void fetchCorrespondingSecurityConstraints() {

		boolean set = false;

		for (IControlAction entry : getModel().getAllControlActionsU()) {

			ControlAction tempaction = (ControlAction) entry;

			if (!tempaction.getUnsafeControlActions().isEmpty()) {
				for (IUnsafeControlAction action : tempaction.getUnsafeControlActions()) {
					ConstraintResult temp = new ConstraintResult();
					UnsecureControlAction tempUCA = (UnsecureControlAction) action;
					if (!tempUCA.getCorrespondingSafetyConstraint().getText().equals("")) {
						temp.setScId("SC1." + tempUCA.getNumber());
						temp.setId(tempUCA.getId());
						if (set == false) {
							temp.setStpastep("");
							set = true;
						}
						temp.setTemp(tempaction);
						temp.setSecurityConstraint(tempUCA.getCorrespondingSafetyConstraint().getText());
						temp.setSafe(tempUCA.isSafetyCritical);
						temp.setSecure(tempUCA.isSecurityCritical);
						temp.setPrivate(tempUCA.isPrivacyCritical);

						this.constraintResults.put(temp.getId(), temp);
					}

				}
			}
		}
		set = false;

	}

	public void fetchCausalSecurityConstraints() {
		// get the controlActions
		Map<UUID, ICorrespondingUnsafeControlAction> ucaMap = new HashMap<>();
		Map<UUID, AbstractLTLProvider> ltlMap = new HashMap<>();
		List<ControlAction> calist = new ArrayList<>();
		for (IControlAction ca : getModel().getAllControlActions()) {
			calist.add((ControlAction) ca);
		}
		for (ICorrespondingUnsafeControlAction uca : getModel().getUCAList(null)) {
			ucaMap.put(uca.getId(), uca);
		}
		for (AbstractLTLProvider ltl : getModel().getAllScenarios(false, true, false)) {
			ltlMap.put(ltl.getId(), ltl);
		}
		for (ICausalComponent entry : getModel().getCausalComponents()) {

			if (!entry.getCausalFactors().isEmpty()) {
				for (ICausalFactor tempCF : entry.getCausalFactors()) {

					if (!tempCF.getAllEntries().isEmpty()) {
						for (ICausalFactorEntry tempCFE : tempCF.getAllEntries()) {
							if (tempCFE != null && tempCFE.getScenarioLinks() != null) {
								for (UUID templtl : tempCFE.getScenarioLinks()) {
									if (ltlMap.get(templtl).getRefinedSafetyConstraint() != null) {
										if (ltlMap.containsKey(templtl)
												&& !(ltlMap.get(templtl).getRefinedSafetyConstraint().equals(""))) {
											ConstraintResult tempResult = new ConstraintResult();
											tempResult.setId(ltlMap.get(templtl).getRuleId());
											tempResult.setSecurityConstraint(
													ltlMap.get(templtl).getRefinedSafetyConstraint());
											tempResult.setScId("SC2." + ltlMap.get(templtl).getNumber());
											tempResult.setStpastep("");

											if (tempCFE.getUcaLink() != null
													&& ucaMap.containsKey(tempCFE.getUcaLink())) {
												UnsecureControlAction uca = (UnsecureControlAction) ucaMap
														.get(tempCFE.getUcaLink());
												for (ControlAction cas : calist) {
													for (IUnsafeControlAction ucas : cas.getUnsafeControlActions()) {
														if (ucas.getId() == uca.getId()) {
															tempResult.setTemp(cas);
														}
													}
												}
												tempResult.setRelatedId("SC1." + uca.getNumber());
												tempResult.setSafe(uca.isSafetyCritical());
												tempResult.setSecure(uca.isSecurityCritical);
												tempResult.setPrivate(uca.isPrivacyCritical);

												// tempResult.addRelatedConstraints(tempResult.getSecurityConstraint());
												if (this.constraintResults.get(tempResult.getId()) == null) {
													for (ConstraintResult result : constraintResults.values()) {
														if (result.getScId().equals(tempResult.getRelatedId())) {
															result.addRelatedConstraints(
																	tempResult.getSecurityConstraint());
															tempResult.getRelatedConstraints().clear();
															tempResult.addAllRelatedConstraints(
																	result.getRelatedConstraints());
														}

													}
													this.constraintResults.put(tempResult.getId(), tempResult);
												}

											}
										}
									}

								}

							}

						}
					}

				}
			}
		}
		// set = false;
	}

	public void addRelated() {
		for (ConstraintResult result : constraintResults.values()) {
			for (ConstraintResult result2 : constraintResults.values()) {
				if (result.getRelatedId() != null) {
					if (result.getRelatedId().equals(result2.getScId())) {
						result.getRelatedConstraints().clear();
						result.addAllRelatedConstraints(result2.getRelatedConstraints());
					}
				}

			}

		}
	}

	/**
	 * @return the model
	 */
	@XmlTransient
	public PrivacyController getModel() {
		return this.model;
	}

	@Override
	public void update(Observable arg0, Object updatedValue) {
		final ObserverValue value = (ObserverValue) updatedValue;
		switch (value) {
		case CONTROL_STRUCTURE:
			new Runnable() {
				@Override
				public void run() {
					setChanged();
					notifyObservers(value);
				}
			}.run();
		default:
			break;

		}
	}
}
