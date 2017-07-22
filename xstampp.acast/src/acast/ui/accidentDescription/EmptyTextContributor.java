package acast.ui.accidentDescription;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.editors.interfaces.ITextEditContribution;

public class EmptyTextContributor implements ITextEditContribution {

	private static IPreferenceStore store=xstampp.Activator.getDefault().getPreferenceStore();
	@Override
	public boolean getBoldControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getItalicControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getStrikeoutControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getUnderlineControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBulletListControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Color getBackground() {
		return new Color(null,PreferenceConverter.getColor(store, IPreferenceConstants.COMPANY_BACKGROUND_COLOR));
	}

	@Override
	public Color getForeground() {
		return new Color(null,PreferenceConverter.getColor(store, IPreferenceConstants.COMPANY_FONT_COLOR));
	}

	@Override
	public Font getFont() {
		return new Font(null,PreferenceConverter.getDefaultFontData(store, IPreferenceConstants.DEFAULT_FONT));
	}

}
