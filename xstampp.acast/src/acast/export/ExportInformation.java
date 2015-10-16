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
package acast.export;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import xstampp.Activator;
import xstampp.preferences.IPreferenceConstants;

/**
 * Stores extra information for the export
 *
 * @author Fabian Toth
 *
 */
public class ExportInformation {

	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss"); //$NON-NLS-1$

	private Date creationDate;
	private String csImagePath;
	private String cspmImagePath;
	private String csImageWidth;
	private String csImageHeight;
	private String csPmImageWidth;
	private String csPmImageHeight;
	private String logoPath;
	private String company;
	private String colorBackground;
	private String colorFont;

	/**
	 * Constructor of the export information
	 *
	 *
	 */
	public ExportInformation() {
		this.creationDate = new Date();
		this.logoPath = this.store.getString(IPreferenceConstants.COMPANY_LOGO);
		this.company = this.store.getString(IPreferenceConstants.COMPANY_NAME);
		this.colorBackground = this
				.rgbToHex(PreferenceConverter.getColor(this.store, IPreferenceConstants.COMPANY_BACKGROUND_COLOR));
		this.colorFont = this
				.rgbToHex(PreferenceConverter.getColor(this.store, IPreferenceConstants.COMPANY_FONT_COLOR));
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return this.dateFormat.format(this.creationDate);
	}

	/**
	 * @param date
	 *            the date to set
	 */
	@SuppressWarnings("unused")
	public void setDate(String date) {
		// Nothing to do here. Is needed that JAXB does work
	}

	/**
	 * @return the csImagePath
	 */
	public String getCsImagePath() {
		return this.csImagePath;
	}

	/**
	 * @param csImagePath
	 *            the csImagePath to set
	 * @return true, if the path has been set
	 */
	public boolean setCsImagePath(String csImagePath) {
		this.csImagePath = csImagePath;
		return true;
	}

	/**
	 * @return the cspmImagePath
	 */
	public String getCspmImagePath() {
		return this.cspmImagePath;
	}

	/**
	 * @param cspmImagePath
	 *            the cspmImagePath to set
	 * @return true, if the path has been set
	 */
	public boolean setCspmImagePath(String cspmImagePath) {
		this.cspmImagePath = cspmImagePath;
		return true;
	}

	/**
	 * @return the logoPath
	 */
	public String getLogoPath() {
		if (this.logoPath.isEmpty()) {
			return null;
		}
		return this.logoPath;
	}

	/**
	 * @param logoPath
	 *            the logoPath to set
	 * @return true, if the logoPath has been set
	 */
	public boolean setLogoPath(String logoPath) {
		this.logoPath = logoPath;
		return true;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		if (this.company.isEmpty()) {
			return null;
		}
		return this.company;
	}

	/**
	 * @param company
	 *            the company to set
	 * @return true, if the company has been set
	 */
	public boolean setCompany(String company) {
		this.company = company;
		return true;
	}

	/**
	 * @return the background color
	 */
	public String getBackgroundColor() {
		return this.colorBackground;
	}

	/**
	 * @param colorBackground
	 *            the color to set
	 * @return true, if the color has been set
	 */
	public boolean setBackgroundColor(String colorBackground) {
		this.colorBackground = colorBackground;
		return true;
	}

	/**
	 * @return the font color
	 */
	public String getFontColor() {
		return this.colorFont;
	}

	/**
	 * @param colorFont
	 *            the color to set
	 * @return true, if the color has been set
	 */
	public boolean setFontColor(String colorFont) {
		this.colorFont = colorFont;
		return true;
	}

	private String rgbToHex(RGB rgb) {
		String redHexadecimal = Integer.toHexString(rgb.red);
		String greenHexadecimal = Integer.toHexString(rgb.green);
		String blueHexadecimal = Integer.toHexString(rgb.blue);
		if (redHexadecimal.length() == 1) {
			redHexadecimal = "0" + redHexadecimal; //$NON-NLS-1$
		}
		if (greenHexadecimal.length() == 1) {
			greenHexadecimal = "0" + greenHexadecimal; //$NON-NLS-1$
		}
		if (blueHexadecimal.length() == 1) {
			blueHexadecimal = "0" + blueHexadecimal; //$NON-NLS-1$
		}
		return "#" + redHexadecimal + greenHexadecimal + blueHexadecimal; //$NON-NLS-1$
	}

	/**
	 * @return the csImageWidth
	 */
	public String getCsImageWidth() {
		return this.csImageWidth;
	}

	/**
	 * @param csImageWidth
	 *            the csImageWidth to set
	 */
	public void setCsImageWidth(String csImageWidth) {
		this.csImageWidth = csImageWidth;
	}

	/**
	 * @return the csImageheight
	 */
	public String getCsImageHeight() {
		return this.csImageHeight;
	}

	/**
	 * @param csImageheight
	 *            the csImageheight to set
	 */
	public void setCsImageHeight(String csImageheight) {
		this.csImageHeight = csImageheight;
	}

	/**
	 * @return the csPmImageWidth
	 */
	public String getCsPmImageWidth() {
		return this.csPmImageWidth;
	}

	/**
	 * @param csPmImageWidth
	 *            the csPmImageWidth to set
	 */
	public void setCsPmImageWidth(String csPmImageWidth) {
		this.csPmImageWidth = csPmImageWidth;
	}

	/**
	 * @return the csPmImageheight
	 */
	public String getCsPmImageHeight() {
		return this.csPmImageHeight;
	}

	/**
	 * @param csPmImageheight
	 *            the csPmImageheight to set
	 */
	public void setCsPmImageHeight(String csPmImageheight) {
		this.csPmImageHeight = csPmImageheight;
	}

}
