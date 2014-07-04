package astpa.export.pages;

import org.eclipse.jface.wizard.IWizardPage;

/**
 *
 * @author Lukas Balzer
 *
 */
public interface IExportPage extends IWizardPage{

	/**
	 * @return The path as uri
	 */
	String getExportPath();

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @return
	 * 		whether the table shall be exported as a single image or in multiple images
	 */
	public boolean asOne();
}
