package xstampp.ui.editors.interfaces;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public interface ITextEditContribution {

	/**
	 * @return the boldControl selection
	 */
	public abstract boolean getBoldControl();

	/**
	 * @return the italicControl
	 */
	public abstract boolean getItalicControl();

	/**
	 * @return the strikeoutControl
	 */
	public abstract boolean getStrikeoutControl();

	/**
	 * @return the underlineControl
	 */
	public abstract boolean getUnderlineControl();

	/**
	 * @return the bulletListControl
	 */
	public abstract boolean getBulletListControl();

	Color getBackground();

	Color getForeground();

	Font getFont();

}