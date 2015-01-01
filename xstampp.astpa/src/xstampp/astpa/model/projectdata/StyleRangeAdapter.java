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

package xstampp.astpa.model.projectdata;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * JAXB Adapter for the StyleRange class
 * 
 * @author Fabian Toth
 * 
 */
public class StyleRangeAdapter extends XmlAdapter<String, StyleRange> {

	private static final String DELIMITER = ","; //$NON-NLS-1$

	@Override
	public String marshal(StyleRange sr) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(sr.start + StyleRangeAdapter.DELIMITER);
		sb.append(sr.length + StyleRangeAdapter.DELIMITER);
		sb.append(sr.fontStyle + StyleRangeAdapter.DELIMITER);
		sb.append(sr.underline + StyleRangeAdapter.DELIMITER);
		sb.append(sr.underlineStyle + StyleRangeAdapter.DELIMITER);
		sb.append(sr.strikeout + StyleRangeAdapter.DELIMITER);
		// Foreground color
		if (sr.foreground == null) {
			sb.append(sr.foreground + StyleRangeAdapter.DELIMITER
					+ sr.foreground + StyleRangeAdapter.DELIMITER
					+ sr.foreground + StyleRangeAdapter.DELIMITER);
		} else {
			sb.append(sr.foreground.getRed() + StyleRangeAdapter.DELIMITER
					+ sr.foreground.getGreen() + StyleRangeAdapter.DELIMITER
					+ sr.foreground.getBlue() + StyleRangeAdapter.DELIMITER);
		}
		// Background color
		if (sr.background == null) {
			sb.append(sr.background + StyleRangeAdapter.DELIMITER
					+ sr.background + StyleRangeAdapter.DELIMITER
					+ sr.background + StyleRangeAdapter.DELIMITER);
		} else {
			sb.append(sr.background.getRed() + StyleRangeAdapter.DELIMITER
					+ sr.background.getGreen() + StyleRangeAdapter.DELIMITER
					+ sr.background.getBlue() + StyleRangeAdapter.DELIMITER);
		}
		// Font
		sb.append(sr.fontStyle + StyleRangeAdapter.DELIMITER);
		if (sr.font == null) {
			sb.append("null"); //$NON-NLS-1$
		} else {
			for (FontData fontData : sr.font.getFontData()) {
				sb.append(fontData.toString() + StyleRangeAdapter.DELIMITER);
			}
		}
		String temp = sb.toString();
		if (temp.lastIndexOf(StyleRangeAdapter.DELIMITER) == (temp.length() - 1)) {
			temp = temp.substring(0, temp.length() - 1);
		}
		return temp;
	}

	@Override
	public StyleRange unmarshal(String s) throws Exception {
		String[] data = s.split(StyleRangeAdapter.DELIMITER);
		StyleRange sr = new StyleRange();
		sr.start = Integer.parseInt(data[0]);
		sr.length = Integer.parseInt(data[1]);
		sr.fontStyle = Integer.parseInt(data[2]);
		sr.underline = Boolean.valueOf(data[3]);
		sr.underlineStyle = Integer.parseInt(data[4]);
		sr.strikeout = Boolean.valueOf(data[5]);
		if (data[6].equals("null")) { //$NON-NLS-1$
			sr.foreground = null;
		} else {
			sr.foreground = new Color(Display.getCurrent(),
					Integer.parseInt(data[6]), Integer.parseInt(data[7]),
					Integer.parseInt(data[8]));
		}
		if (data[9].equals("null")) { //$NON-NLS-1$
			sr.foreground = null;
		} else {
			sr.background = new Color(Display.getCurrent(),
					Integer.parseInt(data[9]), Integer.parseInt(data[10]),
					Integer.parseInt(data[11]));
		}
		sr.fontStyle = Integer.parseInt(data[12]);
		if (data[13].equals("null")) { //$NON-NLS-1$
			sr.font = null;
		} else {
			FontData[] fontData = new FontData[data.length - 13];
			for (int i = 13; i < data.length; i++) {
				fontData[0] = new FontData(data[i]);
			}
			sr.font = new Font(Display.getCurrent(), fontData);
		}
		return sr;
	}
}
