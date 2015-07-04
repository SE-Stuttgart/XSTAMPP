/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package xstampp.astpa.controlstructure.utilities;

import java.util.List;





import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowBox;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.preferences.IAstpaPreferences;
import xstampp.preferences.IPreferenceConstants;

/**
 * 
 * CSTextLabel
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public class CSTextLabel extends FlowPage implements IPropertyChangeListener{
	private static final int INITIAL_TEXT_SIZE = 10;
	private TextFlow content;
	private FontData currentFont;
	private static final int CENTER_COMPENSATION = 2;
	private boolean extraLine = false;
	private final IPreferenceStore store = Activator.getDefault()
			.getPreferenceStore();
	/**
	 * This constructor initializes the <code>blockFlow</code> and the
	 * <code>content</code> It also sets the Layout Manager to
	 * <code>ParagraphTextLayout</code> which layouts the TextFlow in dependency
	 * to the size
	 * 
	 * @author Lukas Balzer
	 * @param csFigure
	 *            the parent figure
	 * 
	 */
	public CSTextLabel(IControlStructureFigure csFigure) {
		super();
		this.setParent(csFigure);
		this.content = new TextFlow();
		this.content.setBackgroundColor(ColorConstants.white);
		this.store.addPropertyChangeListener( this);
		syncProperty(IAstpaPreferences.CONTROLSTRUCTURE_FONT);
		syncProperty(IAstpaPreferences.CONTROLSTRUCTURE_FONT_COLOR);

		this.content.setLayoutManager(new ParagraphTextLayout(this.content,
				ParagraphTextLayout.WORD_WRAP_TRUNCATE));
		this.content.setLocation(new Point(0, 0));
		this.content.setForegroundColor(ColorConstants.black);
		this.content.setVisible(true);
		this.setOpaque(true);
		this.content.setOpaque(true);
		this.add(this.content);

	}

	
	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param style
	 *            a SWT int constant
	 * @see SWT#NORMAL
	 * @see SWT#BOLD
	 * @see SWT#ITALIC
	 */
	public void setFontStyle(int style) {
		this.content.setFont(new Font(null,
				this.currentFont.getName(), this.currentFont.getHeight(), style)); //$NON-NLS-1$
	}

	@Override
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		return null;
	}

	@Override
	public Font getFont() {
		FontData[] tmpFont = this.content.getFont().getFontData();
		int fontHeight = tmpFont[0].getHeight();
		Dimension p = new Dimension(0, fontHeight);
		this.translateToAbsolute(p);
		tmpFont[0].setHeight(p.height);
		return new Font(null, tmpFont);
	}

	@Override
	public Color getForegroundColor() {
		return this.content.getForegroundColor();
	}

	/**
	 * sets the ForegroundColor of the content
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param newColor
	 *            Color of the text
	 */
	public void setForeground(Color newColor) {
		this.content.setForegroundColor(newColor);
	}

	/**
	 * Formats the text and returns it
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return the text of this Label
	 */
	public String getText() {
		return this.content.getText();
	}

	/**
	 * @author Lukas Balzer
	 * 
	 * @return the Object of this CSTextLabel
	 */
	public CSTextLabel getTextField() {
		return this;
	}

	/**
	 * @author Lukas Balzer
	 * 
	 * @return the bounds of the text after the text has been formatted from the
	 *         LayoutManager
	 */
	public Rectangle getTextBounds() {
		Rectangle rect = this.getParent().getBounds().getCopy();
		List<FlowBox> fragments = this.content.getFragments();
		int minHeight = 0;

		if (fragments.size() != 0) {
			minHeight = fragments.get(fragments.size() - 1).getBaseline();
		}
		rect.setLocation(CSTextLabel.CENTER_COMPENSATION,
				CSTextLabel.CENTER_COMPENSATION);
		rect.setHeight(minHeight);

		return rect;
	}

	@Override
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		rect.setLocation(0, 0);
		this.content.setBounds(rect);
	}

	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param text
	 *            the new Text to be displayed
	 */
	public void setText(String text) {
		this.content.getUpdateManager().performUpdate();
		this.content.setText(text);
		this.content.getLayoutManager().invalidate();
		this.content.getUpdateManager().performUpdate();
	}

	public void setLineVisible(boolean extraLine) {
		this.extraLine = extraLine;
	}
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		if(this.extraLine && Activator.getDefault().getPreferenceStore().
							getBoolean(IAstpaPreferences.CONTROLSTRUCTURE_PROCESS_MODEL_BORDER)){
			graphics.drawLine(0, getBounds().height, getParent().getBounds().width, getBounds().height);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		syncProperty(event.getProperty());
	}
	
	private void syncProperty(String property){
		if(property.equals(IAstpaPreferences.CONTROLSTRUCTURE_FONT)){
			this.currentFont= PreferenceConverter.getFontData(this.store, property);
			this.content.setFont(new Font(null,
					this.currentFont)); //$NON-NLS-1$
		}else if(property.equals(IAstpaPreferences.CONTROLSTRUCTURE_FONT_COLOR)){
			Color fontColor = new Color(Display.getCurrent(), PreferenceConverter
					.getColor(this.store, property));
			setForeground(fontColor);
		}
		
	}
}
