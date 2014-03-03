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

package astpa.export;

import java.io.File;
import java.util.ArrayList;

import messages.Messages;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import astpa.Activator;
import astpa.preferences.IPreferenceConstants;

/**
 * Create export page.
 * 
 * @author Sebastian
 * 
 */
public class ExportPage extends WizardPage {
	
	private String projectName;
	private Composite container;
	private Text textCompany, textLogo, textBackgroundColor, textFontColor, textExportPath;
	private Button buttonLogo, buttonBackgroundColor, buttonFontColor, buttonExportPath;
	private static final String IMAGE_PATH = "/icons/buttons/export"; //$NON-NLS-1$
	
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	
	private Label lbBackgroundIcon, lbFontIcon;
	
	private int fontState = 0;
	private int backgroundState = 1;
	
	
	/**
	 * Constructor.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param pageName the name of the page
	 * @param projectName the name of the project
	 */
	public ExportPage(String pageName, String projectName) {
		super(pageName);
		this.setTitle(Messages.Export);
		this.setDescription(Messages.SetValuesForTheExportFile);
		this.projectName = projectName;
		
	}
	
	@Override
	public void createControl(Composite parent) {
		this.container = new Composite(parent, SWT.NONE);
		this.container.setLayout(null);
		Label labelCompany = new Label(this.container, SWT.NONE);
		labelCompany.setBounds(5, 8, 55, 15);
		labelCompany.setText(Messages.Company);
		
		this.textCompany = new Text(this.container, SWT.BORDER | SWT.SINGLE);
		this.textCompany.setBounds(127, 5, 345, 21);
		
		String companyName = this.store.getString(IPreferenceConstants.COMPANY_NAME);
		if (companyName != null) {
			this.textCompany.setText(companyName);
			this.textCompany.setSelection(companyName.length());
		} else {
			this.textCompany.setText(""); //$NON-NLS-1$
		}
		
		Label labelLogo = new Label(this.container, SWT.NONE);
		labelLogo.setBounds(5, 34, 107, 15);
		labelLogo.setText(Messages.Logo);
		
		this.textLogo = new Text(this.container, SWT.BORDER | SWT.SINGLE);
		this.textLogo.setBounds(127, 31, 297, 21);
		this.textLogo.setEditable(false);
		
		String companyLogo = this.store.getString(IPreferenceConstants.COMPANY_LOGO);
		if (companyLogo != null) {
			this.textLogo.setText(companyLogo);
			this.textLogo.setSelection(companyLogo.length());
		} else {
			this.textLogo.setText(""); //$NON-NLS-1$
		}
		
		this.buttonLogo = new Button(this.container, SWT.NONE);
		this.buttonLogo.setBounds(430, 29, 42, 25);
		this.buttonLogo.setText("..."); //$NON-NLS-1$
		this.buttonLogo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ExportPage.this.openLogoDialog();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// no-op
			}
		});
		
		Label labelBackgroundColor = new Label(this.container, SWT.NONE);
		labelBackgroundColor.setText(Messages.BackgroundColor);
		labelBackgroundColor.setBounds(5, 61, 107, 15);
		
		this.textBackgroundColor = new Text(this.container, SWT.BORDER);
		this.textBackgroundColor.setBounds(156, 58, 268, 21);
		this.textBackgroundColor.setEditable(false);
		
		String companyBackgroundColor =
			PreferenceConverter.getColor(this.store, IPreferenceConstants.COMPANY_BACKGROUND_COLOR).toString();
		
		if (companyBackgroundColor != null) {
			this.textBackgroundColor.setText(companyBackgroundColor);
			this.textBackgroundColor.setSelection(companyBackgroundColor.length());
		} else {
			this.textBackgroundColor.setText(""); //$NON-NLS-1$
		}
		
		this.lbBackgroundIcon = new Label(this.container, SWT.NONE);
		this.lbBackgroundIcon.setBounds(127, 58, 23, 23);
		this.setLabelIcon(this.lbBackgroundIcon,
			PreferenceConverter.getColor(this.store, IPreferenceConstants.COMPANY_BACKGROUND_COLOR),
			this.backgroundState);
		
		this.buttonBackgroundColor = new Button(this.container, SWT.NONE);
		this.buttonBackgroundColor.setText("..."); //$NON-NLS-1$
		this.buttonBackgroundColor.setBounds(430, 56, 42, 25);
		this.buttonBackgroundColor.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ExportPage.this.openColorDialog(ExportPage.this.buttonBackgroundColor,
					ExportPage.this.textBackgroundColor, ExportPage.this.lbBackgroundIcon,
					ExportPage.this.backgroundState);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// no-op
			}
		});
		
		Label labelFontColor = new Label(this.container, SWT.NONE);
		labelFontColor.setText(Messages.FontColor);
		labelFontColor.setBounds(5, 88, 107, 15);
		
		this.textFontColor = new Text(this.container, SWT.BORDER);
		this.textFontColor.setBounds(156, 85, 268, 21);
		this.textFontColor.setEditable(false);
		
		String companyFontColor =
			PreferenceConverter.getColor(this.store, IPreferenceConstants.COMPANY_FONT_COLOR).toString();
		if (companyFontColor != null) {
			this.textFontColor.setText(companyFontColor);
			this.textFontColor.setSelection(companyFontColor.length());
		} else {
			this.textFontColor.setText(""); //$NON-NLS-1$
		}
		
		this.lbFontIcon = new Label(this.container, SWT.NONE);
		this.lbFontIcon.setBounds(127, 85, 23, 23);
		this.setLabelIcon(this.lbFontIcon,
			PreferenceConverter.getColor(this.store, IPreferenceConstants.COMPANY_FONT_COLOR), this.fontState);
		
		this.buttonFontColor = new Button(this.container, SWT.NONE);
		this.buttonFontColor.setText("..."); //$NON-NLS-1$
		this.buttonFontColor.setBounds(430, 83, 42, 25);
		this.buttonFontColor.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ExportPage.this.openColorDialog(ExportPage.this.buttonFontColor, ExportPage.this.textFontColor,
					ExportPage.this.lbFontIcon, ExportPage.this.fontState);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// no-op
			}
		});
		
		Label labelExport = new Label(this.container, SWT.NONE);
		labelExport.setText(Messages.Destination);
		labelExport.setBounds(5, 115, 107, 15);
		
		this.textExportPath = new Text(this.container, SWT.BORDER);
		this.textExportPath.setBounds(127, 112, 297, 21);
		this.textExportPath.setText(""); //$NON-NLS-1$
		this.textExportPath.setEditable(false);
		
		this.buttonExportPath = new Button(this.container, SWT.NONE);
		this.buttonExportPath.setBounds(430, 110, 42, 25);
		this.buttonExportPath.setText("..."); //$NON-NLS-1$
		this.buttonExportPath.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ExportPage.this.openExportDialog();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// no-op
			}
		});
		
		// Required to avoid an error in the system
		this.setControl(this.container);
		this.setPageComplete(true);
		
	}
	
	/**
	 * Opens color dialog.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param srcBtn Button
	 * @param srcText Text
	 * @param srcLbl Label
	 * @param state Integer
	 */
	private void openColorDialog(Button srcBtn, Text srcText, Label srcLbl, int state) {
		ColorDialog colorDialog = new ColorDialog(srcBtn.getShell());
		colorDialog.setText(Messages.SelectColor);
		RGB selectedColor = colorDialog.open();
		if (selectedColor == null) {
			return;
		}
		srcText.setText(selectedColor.toString());
		this.setLabelIcon(srcLbl, selectedColor, state);
	}
	
	/**
	 * Opens export picture dialog
	 * 
	 * @author Sebastian Sieber
	 * 
	 */
	private void openLogoDialog() {
		final String[] filterNames = {"JPEG (*.jpg;*.jpeg;*.jpe;*.jfif)", //$NON-NLS-1$
			"PNG (*.png)", "TIFF (*.tif;*.tiff)", "GIF (*.gif)", "Bitmap (*.bmp;*.dib)", "SVG (*.svg;*.svgz)"};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		final String[] filterExtensions = {"*.jpg", "*.png", "*.jpe", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"*.jfif", "*.jpeg", "*.tif", "*.tiff", "*.gif", "*.bmp", "*.dib", "*.svg", "*.svgz"};//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
		FileDialog dlg = new FileDialog(ExportPage.this.buttonLogo.getShell(), SWT.OPEN);
		dlg.setFilterExtensions(filterExtensions);
		dlg.setFilterNames(filterNames);
		dlg.setText(Messages.Open);
		String path = dlg.open();
		if (path == null) {
			return;
		}
		if (path.contains(".")) { //$NON-NLS-1$
			String ext = "*" + path.substring(path.lastIndexOf('.')); //$NON-NLS-1$
			boolean supported = false;
			for (int i = 0; i < filterExtensions.length; i++) {
				if (filterExtensions[i].equalsIgnoreCase(ext)) {
					supported = true;
				}
			}
			if (supported) {
				File tmp = new File(path);
				path = tmp.toURI().toString();
				ExportPage.this.getTextLogo().setText(path);
			} else {
				MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.FileFormatNotSupported);
			}
		}
	}
	
	/**
	 * Open the export dialog
	 * 
	 * @author Sebastian
	 * 
	 */
	private void openExportDialog() {
		FileDialog fileDialog = new FileDialog(ExportPage.this.buttonBackgroundColor.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(new String[] {"*.pdf"}); //$NON-NLS-1$
		fileDialog.setFilterNames(new String[] {"A-STPA report (*.pdf)"}); //$NON-NLS-1$
		fileDialog.setFileName(ExportPage.this.projectName);
		String filePath = fileDialog.open();
		if (filePath != null) {
			ExportPage.this.textExportPath.setText(filePath);
		}
	}
	
	/**
	 * Set different color icons for different color shades.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param rgb RGB
	 */
	private void setLabelIcon(Label lbColorIcon, RGB rgb, int state) {
		String folder = ""; //$NON-NLS-1$
		// Shade lists
		ArrayList<RGB> redShades = new ArrayList<>();
		ArrayList<RGB> blackShades = new ArrayList<>();
		ArrayList<RGB> yellowShades = new ArrayList<>();
		ArrayList<RGB> greenShades = new ArrayList<>();
		ArrayList<RGB> purpleShades = new ArrayList<>();
		ArrayList<RGB> whiteShades = new ArrayList<>();
		ArrayList<RGB> blueShades = new ArrayList<>();
		ArrayList<RGB> grayShades = new ArrayList<>();
		
		this.initShadeLists(redShades, blackShades, yellowShades, greenShades, purpleShades, whiteShades, blueShades,
			grayShades);
		
		if (state == this.fontState) {
			folder = "/font"; //$NON-NLS-1$
		} else {
			folder = "/background"; //$NON-NLS-1$
		}
		
		// set icon
		if (redShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(ExportPage.IMAGE_PATH + folder + "/textRed.ico") //$NON-NLS-1$
				.createImage());
		} else if (blackShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(ExportPage.IMAGE_PATH + folder + "/textBlack.ico") //$NON-NLS-1$
				.createImage());
		} else if (yellowShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(ExportPage.IMAGE_PATH + folder + "/textYellow.ico") //$NON-NLS-1$
				.createImage());
		} else if (greenShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(ExportPage.IMAGE_PATH + folder + "/textGreen.ico") //$NON-NLS-1$
				.createImage());
		} else if (purpleShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(ExportPage.IMAGE_PATH + folder + "/textPurple.ico") //$NON-NLS-1$
				.createImage());
		} else if (whiteShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(ExportPage.IMAGE_PATH + folder + "/textWhite.ico") //$NON-NLS-1$
				.createImage());
		} else if (blueShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(ExportPage.IMAGE_PATH + folder + "/textBlue.ico") //$NON-NLS-1$
				.createImage());
		} else if (grayShades.contains(rgb)) {
			lbColorIcon.setImage(Activator.getImageDescriptor(ExportPage.IMAGE_PATH + folder + "/textGrey.ico") //$NON-NLS-1$
				.createImage());
		}
		
	}
	
	/**
	 * Initialize ArrayList with RGB values of color shades.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param redShades ArrayList<RGB>
	 * @param blackShades ArrayList<RGB>
	 * @param yellowShades ArrayList<RGB>
	 * @param greenShades ArrayList<RGB>
	 * @param purpleShades ArrayList<RGB>
	 * @param whiteShades ArrayList<RGB>
	 * @param blueShades ArrayList<RGB>
	 * @param grayShades ArrayList<RGB>
	 */
	private void initShadeLists(ArrayList<RGB> redShades, ArrayList<RGB> blackShades, ArrayList<RGB> yellowShades,
		ArrayList<RGB> greenShades, ArrayList<RGB> purpleShades, ArrayList<RGB> whiteShades, ArrayList<RGB> blueShades,
		ArrayList<RGB> grayShades) {
		
		// RGB values
		final int twoHundredAndFiftyFive = 255;
		final int zero = 0;
		final int sixtyfour = 64;
		final int oneHundredAndTwentyEight = 128;
		// initialize
		if (redShades.size() == 0) {
			redShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			redShades.add(new RGB(twoHundredAndFiftyFive, zero, zero));
			redShades.add(new RGB(oneHundredAndTwentyEight, sixtyfour, sixtyfour));
			redShades.add(new RGB(oneHundredAndTwentyEight, zero, zero));
			redShades.add(new RGB(sixtyfour, zero, zero));
		}
		
		if (blackShades.size() == 0) {
			blackShades.add(new RGB(zero, zero, zero));
		}
		
		if (yellowShades.size() == 0) {
			yellowShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, zero));
			yellowShades.add(new RGB(oneHundredAndTwentyEight, sixtyfour, zero));
			yellowShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, zero));
			yellowShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, sixtyfour));
			yellowShades.add(new RGB(twoHundredAndFiftyFive, twoHundredAndFiftyFive, zero));
			yellowShades.add(new RGB(twoHundredAndFiftyFive, twoHundredAndFiftyFive, oneHundredAndTwentyEight));
		}
		
		if (greenShades.size() == 0) {
			greenShades.add(new RGB(oneHundredAndTwentyEight, twoHundredAndFiftyFive, oneHundredAndTwentyEight));
			greenShades.add(new RGB(oneHundredAndTwentyEight, twoHundredAndFiftyFive, zero));
			greenShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, sixtyfour));
			greenShades.add(new RGB(sixtyfour, oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			greenShades.add(new RGB(zero, twoHundredAndFiftyFive, zero));
			greenShades.add(new RGB(zero, oneHundredAndTwentyEight, zero));
			greenShades.add(new RGB(zero, sixtyfour, zero));
			greenShades.add(new RGB(zero, twoHundredAndFiftyFive, oneHundredAndTwentyEight));
			greenShades.add(new RGB(zero, twoHundredAndFiftyFive, sixtyfour));
			greenShades.add(new RGB(zero, oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			greenShades.add(new RGB(zero, oneHundredAndTwentyEight, sixtyfour));
			greenShades.add(new RGB(zero, sixtyfour, sixtyfour));
		}
		
		final int onehundredAndNinetyTwo = 192;
		if (purpleShades.size() == 0) {
			purpleShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, onehundredAndNinetyTwo));
			purpleShades.add(new RGB(twoHundredAndFiftyFive, oneHundredAndTwentyEight, twoHundredAndFiftyFive));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, onehundredAndNinetyTwo));
			purpleShades.add(new RGB(twoHundredAndFiftyFive, zero, twoHundredAndFiftyFive));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, zero, sixtyfour));
			purpleShades.add(new RGB(twoHundredAndFiftyFive, zero, oneHundredAndTwentyEight));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, zero, oneHundredAndTwentyEight));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, zero, twoHundredAndFiftyFive));
			purpleShades.add(new RGB(sixtyfour, zero, sixtyfour));
			purpleShades.add(new RGB(sixtyfour, zero, oneHundredAndTwentyEight));
			purpleShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, twoHundredAndFiftyFive));
		}
		
		if (whiteShades.size() == 0) {
			whiteShades.add(new RGB(twoHundredAndFiftyFive, twoHundredAndFiftyFive, twoHundredAndFiftyFive));
		}
		
		final int onehundredAndSixty = 160;
		if (blueShades.size() == 0) {
			blueShades.add(new RGB(oneHundredAndTwentyEight, twoHundredAndFiftyFive, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, twoHundredAndFiftyFive, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, sixtyfour, oneHundredAndTwentyEight));
			blueShades.add(new RGB(zero, zero, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, zero, oneHundredAndTwentyEight));
			blueShades.add(new RGB(zero, oneHundredAndTwentyEight, twoHundredAndFiftyFive));
			blueShades.add(new RGB(zero, oneHundredAndTwentyEight, onehundredAndNinetyTwo));
			blueShades.add(new RGB(zero, zero, onehundredAndSixty));
			blueShades.add(new RGB(zero, zero, sixtyfour));
		}
		
		if (grayShades.size() == 0) {
			grayShades.add(new RGB(oneHundredAndTwentyEight, oneHundredAndTwentyEight, oneHundredAndTwentyEight));
			grayShades.add(new RGB(onehundredAndNinetyTwo, onehundredAndNinetyTwo, onehundredAndNinetyTwo));
		}
		
	}
	
	/**
	 * @return the textCompany
	 */
	public Text getTextCompany() {
		return this.textCompany;
	}
	
	/**
	 * @param textCompany the textCompany to set
	 */
	public void setTextCompany(Text textCompany) {
		this.textCompany = textCompany;
	}
	
	/**
	 * @return the textLogo
	 */
	public Text getTextLogo() {
		return this.textLogo;
	}
	
	/**
	 * @param textLogo the textLogo to set
	 */
	public void setTextLogo(Text textLogo) {
		this.textLogo = textLogo;
	}
	
	/**
	 * @return the textBackgroundColor
	 */
	public Text getTextBackgroundColor() {
		return this.textBackgroundColor;
	}
	
	/**
	 * @param textBackgroundColor the textColor to set
	 */
	public void setTextColor(Text textBackgroundColor) {
		this.textBackgroundColor = textBackgroundColor;
	}
	
	/**
	 * @return the textFontColor
	 */
	public Text getTextFontColor() {
		return this.textFontColor;
	}
	
	/**
	 * @param textFontColor the textColor to set
	 */
	public void setTextFontColor(Text textFontColor) {
		this.textFontColor = textFontColor;
	}
	
	/**
	 * @return export path
	 */
	public String getExportPath() {
		return this.textExportPath.getText();
	}
}
