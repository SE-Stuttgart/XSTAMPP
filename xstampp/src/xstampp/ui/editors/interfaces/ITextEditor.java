package xstampp.ui.editors.interfaces;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.graphics.RGB;

/**
 * defines a set of functions and constants to communcate with a TextEditor
 * 
 * @author Lukas Balzer
 *
 */
public interface ITextEditor extends ISelectionProvider {

  /**
   * a constant for Incresing e.g. the font size
   * 
   * @author Lukas Balzer
   */
  String INCREASE = "INCREASE"; //$NON-NLS-1$
  /**
   * a constant for decreasing sth.
   * 
   * @author Lukas Balzer
   */
  String DECREASE = "DECREASE"; //$NON-NLS-1$
  /**
   * constant for applying an italic style to some text
   */
  String ITALIC = "ITALIC"; //$NON-NLS-1$
  /**
   * constant for underlining text
   */
  String UNDERLINE = "UNDERLINE"; //$NON-NLS-1$
  /**
   * constant for for adding a strike out
   */
  String STRIKEOUT = "STRIKEOUT"; //$NON-NLS-1$
  /**
   * constant for addressing the foreground color
   */
  String FOREGROUND = "FOREGROUND"; //$NON-NLS-1$
  /**
   * constant for addressing the background color
   */
  String BACKGROUND = "BACKGROUND"; //$NON-NLS-1$
  /**
   * constant for applying a bold style to the text
   */
  String BOLD = "BOLD"; //$NON-NLS-1$
  /**
   * constant for adding an itemization
   */
  String DOT_LIST = "DOT_LIST"; //$NON-NLS-1$
  /**
   * constant for adding an enumeration
   */
  String NUM_LIST = "NUM_LIST"; //$NON-NLS-1$

  @SuppressWarnings("javadoc")
  String FONT_SIZE = "FONT_SIZE"; //$NON-NLS-1$

  @SuppressWarnings("javadoc")
  String FONT_FAMILY = "FONT_FAMILY"; //$NON-NLS-1$

  @SuppressWarnings("javadoc")
  String FONT_SIZE_UP = "FONT_SIZE_UP"; //$NON-NLS-1$

  @SuppressWarnings("javadoc")
  String FONT_SIZE_DOWN = "FONT_SIZE_DOWN"; //$NON-NLS-1$

  @SuppressWarnings("javadoc")
  String DESCRIPTION = "DESCRIPTION"; //$NON-NLS-1$

  /**
   * Set style to chosen format if text gets modified or toolBar item pressed.
   * 
   * @author Sebastian Sieber,Lukas Balzer
   * @param style
   *          one of the constants defined in ITextEditor
   */
  void setStyle(String style);

  /**
   * @author Lukas Balzer
   *
   * @param color
   *          one of {@link #FOREGROUND} and {@link #BACKGROUND}
   * @param rgb
   *          the new rgbColor
   */
  void setStyleColor(String color, RGB rgb);

  /**
   * changes the font
   *
   * @author Lukas Balzer
   *
   * @param fontString
   *          the name of the new Font
   * @param fontSize
   *          the Size in points
   */
  void setFont(String fontString, int fontSize);

  /**
   * changes the font size
   *
   * @author Lukas Balzer
   * @param style
   *          one of {@link #FONT_SIZE_DOWN},{@link #FONT_SIZE_UP},
   *          {@link #FONT_SIZE}
   *
   * @param fontSize
   *          the font size of the text in points
   */
  void setFontSize(String style, int fontSize);

  /**
   * Set a bullet to TextField.
   * 
   * @author Sebastian Sieber
   * @param type
   *          one of {@link #DOT_LIST},{@value #NUM_LIST}
   */
  void setBullet(String type);

  /**
   * registers the contributor by the editor so that the editor can react to its
   * calls
   * 
   * @author Lukas Balzer
   *
   * @param contributor
   *          the contributor, with which the user can manipulate the Text
   */
  void setEditToolContributor(ITextEditContribution contributor);

}
