package xstampp.ui.editors.interfaces;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

public class StyledTextSelection implements ISelection {

	private int fontSize;
	private Point selectionRange;
	private String fontName;

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
		return fontName;
	}
	/**
	 * @param fontName the fontName to set
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
}
