/**
 * 
 * @author Lukas Balzer
 */
package astpa.wizards.stepData;

import java.io.IOException;

/**
 *
 * @author Lukas Balzer
 *
 */
public interface IDataExport extends IExportView{
	
	/**
	 * writes all data provided by this view in the given writer object
	 * @author Lukas Balzer
	 *
	 * @param writer 
	 * 			the Writer where the informations are passed to
	 * @return
	 * 			whether the process was successful or not
	 * @throws IOException 
	 */
	boolean writeCSVData(astpa.wizards.BufferedCSVWriter writer) throws IOException;
	
}
