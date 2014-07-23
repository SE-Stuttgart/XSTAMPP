package astpa.export.stepData;

import org.eclipse.ui.PlatformUI;

import astpa.export.AbstractExportWizard;
import astpa.export.pages.STPADataPage;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.acchaz.AccidentsView;
import astpa.ui.acchaz.HazardsView;
import astpa.ui.common.ViewContainer;

/**
 *
 * @author Lukas Balzer
 *
 */
public class STPAdataWizard extends AbstractExportWizard{
	
	STPADataPage site;
	/**
	 *
	 * @author Lukas Balzer
	 *
	 */
	public STPAdataWizard() {
		super(new String[]{AccidentsView.ID,HazardsView.ID});
		String[] filters= new String[] {"*.csv"}; //$NON-NLS-1$
		ViewContainer viewContainer =
				(ViewContainer) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView(ViewContainer.ID);
		this.site=new STPADataPage(filters,viewContainer.getInitializedViews(),"Custom Data",
				this.getStore().getString(IPreferenceConstants.PROJECT_NAME));
		setExportPage(this.site);
	}
	
	@Override
	public boolean performFinish() {
		this.setExportedViews(this.site.getSteps());
		return performCSVExport();
	}

}