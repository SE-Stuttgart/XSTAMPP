package acast.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import acast.Activator;



/**
 *
 * @author Lukas Balzer
 * @since 2.0.0
 *
 */
public class CastPreferenceInitializor extends AbstractPreferenceInitializer {
	
	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();

	/**
	 * The shared instance.
	 */
	private static CastPreferenceInitializor initalizer;

	/**
	 * Default font used by labels of each view
	 */
	private static final Font DEFAUL_FONT = new Font(Display.getCurrent(),
			Display.getCurrent().getSystemFont().getFontData().toString(), 9,
			SWT.NORMAL);
	

	public CastPreferenceInitializor(){
		initalizer=this;
	}
	
	/**
	 * Returns the shared instance.
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return initalizer
	 */
	public static CastPreferenceInitializor getDefault() {
		return initalizer;
	}

	@Override
	public void initializeDefaultPreferences() {
		PreferenceConverter.setDefault(this.store, IACASTPreferences.CONTROLSTRUCTURE_FONT,
									   DEFAUL_FONT.getFontData());
		PreferenceConverter.setDefault(this.store, IACASTPreferences.CONTROLSTRUCTURE_FONT_COLOR,
									   ColorConstants.black.getRGB());
		PreferenceConverter.setDefault(this.store, IACASTPreferences.CONTROLSTRUCTURE_CONTROLLER_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_BLUE).getRGB());
		PreferenceConverter.setDefault(this.store, IACASTPreferences.CONTROLSTRUCTURE_ACTUATOR_COLOR,
				   ColorConstants.orange.getRGB());
		PreferenceConverter.setDefault(this.store, IACASTPreferences.CONTROLSTRUCTURE_PROCESS_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA).getRGB());
		PreferenceConverter.setDefault(this.store, IACASTPreferences.CONTROLSTRUCTURE_SENSOR_COLOR,
				   ColorConstants.green.getRGB());
		this.store.setDefault(IACASTPreferences.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS, true);
		this.store.setDefault(IACASTPreferences.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER, true);

	}
	


}
