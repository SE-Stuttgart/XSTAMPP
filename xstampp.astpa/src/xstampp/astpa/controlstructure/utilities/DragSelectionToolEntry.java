package xstampp.astpa.controlstructure.utilities;

import org.eclipse.gef.palette.SelectionToolEntry;

public class DragSelectionToolEntry extends SelectionToolEntry {	
	
	/**
	 * Creates a new PanningSelectionToolEntry.
	 */
	public DragSelectionToolEntry() {
		this(null);
	}

	/**
	 * Constructor for PanningSelectionToolEntry.
	 * 
	 * @param label
	 *            the label
	 */
	public DragSelectionToolEntry(String label) {
		this(label, null);
	}

	/**
	 * Constructor for PanningSelectionToolEntry.
	 * 
	 * @param label
	 *            the label
	 * @param shortDesc
	 *            the description
	 */
	public DragSelectionToolEntry(String label, String shortDesc) {
		super(label, shortDesc);
		setToolClass(DragSelectionTool.class);
	}
	
	
}
