package xstampp.stpapriv.wizards;

import xstampp.astpa.wizards.AbstractExportWizard;
import xstampp.stpapriv.model.PrivacyController;

public abstract class AbstractPrivacyExportWizard extends AbstractExportWizard {

	public AbstractPrivacyExportWizard() {
		super();
	}

	public AbstractPrivacyExportWizard(String viewId) {
		super(viewId);
	}

	public AbstractPrivacyExportWizard(String[] viewId) {
		super(viewId);
	}

	@Override
	protected Class<?> getExportModel() {
		return PrivacyController.class;
	}

}
