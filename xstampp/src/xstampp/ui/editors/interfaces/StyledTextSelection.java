package xstampp.ui.editors.interfaces;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Point;


/**
 * THis type stores a text selection of a styled text which alos contains informations about it's font
 * @author Lukas Balzer
 * @since 1.0
 */
public class StyledTextSelection implements ISelection {

	private int fontSize;
	private Point selectionRange;
	private String fontName;

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param range the range of the selection
	 */
	public StyledTextSelection(Point range) {
		this.setSelectionRange(range);
	}
	@Override
	public boolean isEmpty() {
		return this.selectionRange.y >=0;
	}

	
	
	/**
	 * @return the selectionRange
	 */
	public Point getSelectionRange() {
		return this.selectionRange;
	}
	/**
	 * @param selectionRange the selectionRange to set
	 */
	public void setSelectionRange(Point selectionRange) {
		this.selectionRange = selectionRange;
	}
	/**
	 * @return the fontSize
	 */
	public int getFontSize() {
		return this.fontSize;
	}
	/**
	 * @param fontSize the fontSize to set
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	/**
	 * @return the fontName
	 */
	public String getFontName() {
		return this.fontName;
	}
	/**
	 * @param fontName the fontName to set
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
}
