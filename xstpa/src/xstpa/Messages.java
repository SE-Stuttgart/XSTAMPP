package xstpa;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "xstpa.messages"; //$NON-NLS-1$
	public static String RefinedRulesTable_ConfirmDelete;
	public static String RefinedRulesTable_DeleteAll;
	public static String RefinedRulesTable_EditUCALinks;
	public static String RefinedRulesTable_Export;
	public static String RefinedRulesTable_NoHazardousCombies;
	public static String RefinedRulesTable_ReallyDeleteRefinedSafety;
	public static String RefinedRulesTable_Remove;
	public static String RefinedRulesTable_RemoveAll;
	public static String RefinedRulesTable_Type;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
