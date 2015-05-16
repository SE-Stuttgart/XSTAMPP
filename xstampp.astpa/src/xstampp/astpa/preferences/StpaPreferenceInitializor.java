package xstampp.astpa.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import xstampp.astpa.Activator;


/**
 *
 * @author Lukas Balzer
 * @since 2.0.0
 *
 */
public class StpaPreferenceInitializor extends AbstractPreferenceInitializer {
	
	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	/**
	 * The shared instance.
	 */
	private static StpaPreferenceInitializor initalizer;

	/**
	 * Default font used by labels of each view
	 */
	private static final Font DEFAUL_FONT = new Font(Display.getCurrent(),
			Display.getCurrent().getSystemFont().getFontData().toString(), 9,
			SWT.NORMAL);
	

	public StpaPreferenceInitializor(){
		initalizer=this;
	}
	
	/**
	 * Returns the shared instance.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return initalizer
	 */
	public static StpaPreferenceInitializor getDefault() {
		return initalizer;
	}

	@Override
	public void initializeDefaultPreferences() {
		PreferenceConverter.setDefault(this.store, IAstpaPreferences.CONTROLSTRUCTURE_FONT,
									   DEFAUL_FONT.getFontData());
		PreferenceConverter.setDefault(this.store, IAstpaPreferences.CONTROLSTRUCTURE_FONT_COLOR,
									   ColorConstants.black.getRGB());
		PreferenceConverter.setDefault(this.store, IAstpaPreferences.CONTROLSTRUCTURE_CONTROLLER_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_BLUE).getRGB());
		PreferenceConverter.setDefault(this.store, IAstpaPreferences.CONTROLSTRUCTURE_ACTUATOR_COLOR,
				   ColorConstants.orange.getRGB());
		PreferenceConverter.setDefault(this.store, IAstpaPreferences.CONTROLSTRUCTURE_PROCESS_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA).getRGB());
		PreferenceConverter.setDefault(this.store, IAstpaPreferences.CONTROLSTRUCTURE_SENSOR_COLOR,
				   ColorConstants.green.getRGB());
		this.store.setDefault(IAstpaPreferences.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS, true);

	}
	


}
