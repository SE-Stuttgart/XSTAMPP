package xstampp.ui.wizards;

import messages.Messages;

import org.eclipse.swt.events.ModifyListener;

/**
 * Creates a Page which collects basic informations about formatting the table
 * Export
 * 
 * @author Lukas Balzer
 * 
 */
public class TableExportPage extends PdfExportPage implements
		ModifyListener {

	/**
	 * 
	 * @author Lukas Balzer
	 * @param filters
	 *            the file extensions, which shall be excepted by in the dialog
	 * @param pageName
	 *            the Name of this page, that is displayed in the header of the
	 *            wizard
	 * @param pluginID TODO
	 */
	public TableExportPage(String[] filters, String pageName, String pluginID) {
		super(pageName, pageName, pluginID);
		setFilterExtensions(filters, filters);
		setShowCompanyFields(false);
		this.setDescription(Messages.SetValuesForTheExportFile);
	}

}
