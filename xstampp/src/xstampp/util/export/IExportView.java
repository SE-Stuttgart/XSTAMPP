package xstampp.util.export;

/**
 * an interface for all views which provide an export function
 * 
 * @author Lukas Balzer
 * 
 */
public interface IExportView {

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return returns the Wizard class with which the export can be performed
	 */
	Class<?> getExportWizard();
}
