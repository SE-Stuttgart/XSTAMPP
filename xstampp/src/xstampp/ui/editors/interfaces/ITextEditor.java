package xstampp.ui.editors.interfaces;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.graphics.RGB;

public interface ITextEditor extends ISelectionProvider{

	String INCREASE="INCREASE";
	String DECREASE="DECREASE";
	/**
	 * constant for applying an italic style to some text 
	 */
	String ITALIC="ITALIC";
	/**
	 * constant for underlining text
	 */
	String  UNDERLINE="UNDERLINE";
	/**
	 * constant for for adding a strike out
	 */
	String  STRIKEOUT="STRIKEOUT";
	/**
	 * constant for addressing the foreground color
	 */
	String FOREGROUND="FOREGROUND";
	/**
	 * constant for addressing the background color 
	 */
	String BACKGROUND="BACKGROUND";
	/**
	 * constant for applying a bold style to the text
	 */
	String BOLD="BOLD";
	/**
	 * constant for adding an itemization
	 */
	String DOT_LIST="DOT_LIST";
	/**
	 * constant for adding an enumeration
	 */
	String NUM_LIST="NUM_LIST";
	
	String FONT_SIZE="FONT_SIZE";
	
	String FONT_FAMILY = "FONT_FAMILY";
	
	String FONT_SIZE_UP ="FONT_SIZE_UP";
	
	String FONT_SIZE_DOWN= "FONT_SIZE_DOWN";
	
	String DESCRIPTION ="DESCRIPTION";
	/**
	 * Set style to chosen format if text gets modified or toolBar item pressed.
	 * 
	 * @author Sebastian Sieber,Lukas Balzer
	 * @param style
	 *            one of the constants defined in ITextEditor
	 */
	void setStyle(String style);
	/**
	 * @author Lukas Balzer
	 *
	 * @param color one of {@link #FOREGROUND} and {@link #BACKGROUND}
	 * @param rgb the new rgbColor
	 */
	void setStyleColor(String color,RGB rgb);
	
	/**
	 * changes the font 
	 *
	 * @author Lukas Balzer
	 *
	 * @param fontString the name of the new Font
	 */
	void setFont(String fontString,int fontSize);
	
	/**
	 * changes the font size
	 *
	 * @author Lukas Balzer
	 * @param style one of {@link #FONT_SIZE_DOWN},{@link #FONT_SIZE_UP},{@link #FONT_SIZE}
	 *
	 * @param fontSize
	 */
	void setFontSize(String style,int fontSize);
	/**
	 * Set a bullet to TextField.
	 * 
	 * @author Sebastian Sieber
	 * @param type
	 *            one of {@link #DOT_LIST},{@value #NUM_LIST}
	 */
	void setBullet(String type);
	
	void setEditToolContributor(ITextEditContribution contributor);

}
