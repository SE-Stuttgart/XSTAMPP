package xstampp.astpa.preferences;

import messages.Messages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import xstampp.preferences.IControlStructureConstants;



/**
*
* @author Lukas Balzer
* @since 2.0.0
*
*/
public class ControlStructurePreferences extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage {

	private BooleanFieldEditor ConnectorSwitch;
	private BooleanFieldEditor ProcessModelBorderSwitch;
	private ColorFieldEditor CSFontColor;
	private FontFieldEditor csLabelFont;
	private ColorFieldEditor CSSensorDeco;
	private ColorFieldEditor CSProcessDeco;
	private ColorFieldEditor CSActuatorDeco;
	private ColorFieldEditor CSControllerDeco;

	public ControlStructurePreferences() {
		super(FieldEditorPreferencePage.GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(xstampp.astpa.Activator.getDefault().getPreferenceStore());
		setDescription("Preferences for both the control structure and the control structure with process model editor");
		
	}

	@Override
	protected void createFieldEditors() {
		this.CSFontColor = new ColorFieldEditor(
				IControlStructureConstants.CONTROLSTRUCTURE_FONT_COLOR, Messages.ControlStructureFontColor,
				this.getFieldEditorParent());
		this.addField(this.CSFontColor);
		this.csLabelFont = new FontFieldEditor(
				IControlStructureConstants.CONTROLSTRUCTURE_FONT, Messages.DefaultControlStructureFont,
				this.getFieldEditorParent());
		
		this.addField(this.csLabelFont);

		this.CSControllerDeco = new ColorFieldEditor(
				IControlStructureConstants.CONTROLSTRUCTURE_CONTROLLER_COLOR, Messages.Controller,
				this.getFieldEditorParent());
		this.addField(this.CSControllerDeco);
		
		this.CSActuatorDeco = new ColorFieldEditor(
				IControlStructureConstants.CONTROLSTRUCTURE_ACTUATOR_COLOR, Messages.Actuator,
				this.getFieldEditorParent());
		this.addField(this.CSActuatorDeco);
		
		this.CSProcessDeco = new ColorFieldEditor(
				IControlStructureConstants.CONTROLSTRUCTURE_PROCESS_COLOR, Messages.ControlledProcess,
				this.getFieldEditorParent());
		this.addField(this.CSProcessDeco);
		
		this.CSSensorDeco = new ColorFieldEditor(
				IControlStructureConstants.CONTROLSTRUCTURE_SENSOR_COLOR, Messages.Sensor,
				this.getFieldEditorParent());
		this.addField(this.CSSensorDeco);
		
		this.ConnectorSwitch = new BooleanFieldEditor(IControlStructureConstants.CONTROLSTRUCTURE_INDIVIDUAL_CONNECTIONS, "Activate manual connection mode",
						this.getFieldEditorParent());
		this.addField(this.ConnectorSwitch);
		
		this.ProcessModelBorderSwitch = new BooleanFieldEditor(IControlStructureConstants.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER, "Show the border of the process Model Variables",
						this.getFieldEditorParent());
		this.addField(this.ProcessModelBorderSwitch);
	}

	@Override
	public boolean performOk() {
		this.CSFontColor.store();
		this.csLabelFont.store();
		this.CSProcessDeco.store();
		this.CSControllerDeco.store();
		this.CSSensorDeco.store();
		this.CSActuatorDeco.store();
		this.ConnectorSwitch.store();
		this.ProcessModelBorderSwitch.store();
		return super.performOk();
	}
	
	@Override
	protected void performDefaults() {
		this.CSFontColor.loadDefault();
		this.csLabelFont.loadDefault();
		this.CSProcessDeco.loadDefault();
		this.CSControllerDeco.loadDefault();
		this.CSSensorDeco.loadDefault();
		this.CSActuatorDeco.loadDefault();
		this.ConnectorSwitch.loadDefault();
	}
	@Override
	protected void performApply() {
		this.CSFontColor.store();
		this.csLabelFont.store();
		this.CSProcessDeco.store();
		this.CSControllerDeco.store();
		this.ConnectorSwitch.store();
		this.CSSensorDeco.store();
		this.CSActuatorDeco.store();
	}
}
