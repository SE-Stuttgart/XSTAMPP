package xstampp.astpa.controlstructure.utilities;

import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.jface.resource.ImageDescriptor;

public class EditableTemplateCreationEntry extends CombinedTemplateCreationEntry {

	public EditableTemplateCreationEntry(String label, String shortDesc, CreationFactory factory,
			ImageDescriptor iconSmall, ImageDescriptor iconLarge) {
		super(label, shortDesc, factory, iconSmall, iconLarge);
	}

	@Override
	public Tool createTool() {
		CreationTool createTool = new CreationTool(factory) {
			
		};
		return createTool;
	}
	
	class DefaultTool extends PaletteEntry {

		public DefaultTool(String label, String shortDescription, ImageDescriptor iconSmall, ImageDescriptor iconLarge,
				Object type) {
			super(label, shortDescription, iconSmall, iconLarge, type);
			// TODO Auto-generated constructor stub
		}
		
	}
}
