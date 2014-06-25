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

package astpa.export.pages;

import java.io.File;

import messages.Messages;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import astpa.Activator;
import astpa.preferences.IPreferenceConstants;

/**
 * Create export page.
 * 
 * @author Sebastian Sieber, Lukas Balzer
 * 
 */
public class PdfExportPage extends AbstractExportPage {
	

	private Composite container;
	private Text textCompany, textLogo, textBackgroundColor, textFontColor,textExportPath;
	private Button buttonLogo, buttonBackgroundColor, buttonFontColor, buttonExportPath;
	
	
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	
	private Label lbBackgroundIcon, lbFontIcon;
	
	
	
	
	/**
	 * Constructor.
	 * 
	 * @author Sebastian Sieber
	 * 
	 * @param pageName the name of the page
	 * @param projectName the name of the project
	 */
	public PdfExportPage(String pageName, String projectName) {
		super(pageName,projectName);
		this.setTitle(Messages.Export);
		this.setDescription(Messages.SetValuesForTheExportFile);
		
		
	}
	
	@Override
	public void createControl(Composite parent) {
		this.container = new Composite(parent, SWT.NONE);
		this.container.setLayout(null);
		Label labelCompany = new Label(this.container, SWT.NONE);
		labelCompany.setBounds(LABEL_COLUMN, 8, 55, 15);
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
		labelLogo.setBounds(LABEL_COLUMN, 34, 107, 15);
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
		this.buttonLogo.setBounds(BUTTON_COLUMN, 29, 42, 25);
		this.buttonLogo.setText("..."); //$NON-NLS-1$
		this.buttonLogo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				PdfExportPage.this.openLogoDialog();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// no-op
			}
		});
		
		Label labelBackgroundColor = new Label(this.container, SWT.NONE);
		labelBackgroundColor.setText(Messages.BackgroundColor);
		labelBackgroundColor.setBounds(LABEL_COLUMN, 61, 107, 15);
		
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
			this.getBackgroundState());
		
		this.buttonBackgroundColor = new Button(this.container, SWT.NONE);
		this.buttonBackgroundColor.setText("..."); //$NON-NLS-1$
		this.buttonBackgroundColor.setBounds(BUTTON_COLUMN, 56, 42, 25);
		this.buttonBackgroundColor.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				PdfExportPage.this.openColorDialog(PdfExportPage.this.buttonBackgroundColor,
					PdfExportPage.this.textBackgroundColor, PdfExportPage.this.lbBackgroundIcon,
					PdfExportPage.this.getBackgroundState());
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
			PreferenceConverter.getColor(this.store, IPreferenceConstants.COMPANY_FONT_COLOR), this.getFontState());
		
		this.buttonFontColor = new Button(this.container, SWT.NONE);
		this.buttonFontColor.setText("..."); //$NON-NLS-1$
		this.buttonFontColor.setBounds(BUTTON_COLUMN, 83, 42, 25);
		this.buttonFontColor.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				PdfExportPage.this.openColorDialog(PdfExportPage.this.buttonFontColor, PdfExportPage.this.textFontColor,
					PdfExportPage.this.lbFontIcon, PdfExportPage.this.getFontState());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// no-op
			}
		});
		
		PathComposite path= new PathComposite(null,this.container, SWT.NONE);
		path.setBounds(LABEL_COLUMN, 115, 480, 25);
		path.setVisible(true);
		this.textExportPath= path.getText();
		
		// Required to avoid an error in the system
		this.setControl(this.container);
		this.setPageComplete(true);
		
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
		FileDialog dlg = new FileDialog(PdfExportPage.this.buttonLogo.getShell(), SWT.OPEN);
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
				PdfExportPage.this.getTextLogo().setText(path);
			} else {
				MessageDialog.openWarning(this.getShell(), Messages.Warning, Messages.FileFormatNotSupported);
			}
		}
	}
		
	
	
	@Override
	public Text getTextCompany() {
		return this.textCompany;
	}
	
	
	/**
	 * @param textCompany
	 * 			sets the given value
	 */
	public void setTextCompany(Text textCompany) {
		this.textCompany = textCompany;
	}
	
	
	@Override
	public Text getTextLogo() {
		return this.textLogo;
	}
	
	/**
	 * @param textLogo the textLogo to set
	 */
	public void setTextLogo(Text textLogo) {
		this.textLogo = textLogo;
	}
	

	@Override
	public Text getTextBackgroundColor() {
		return this.textBackgroundColor;
	}
	
	/**
	 * @param textBackgroundColor the textColor to set
	 */
	public void setTextColor(Text textBackgroundColor) {
		this.textBackgroundColor = textBackgroundColor;
	}
	
	
	@Override
	public Text getTextFontColor() {
		return this.textFontColor;
	}
	
	/**
	 * @param textFontColor the textColor to set
	 */
	public void setTextFontColor(Text textFontColor) {
		this.textFontColor = textFontColor;
	}



	@Override
	protected void openExportDialog(String[] filters) {
	
		FileDialog fileDialog = new FileDialog(this.getShell(), SWT.SAVE);
		fileDialog.setFilterExtensions(new String[] {"*.pdf"}); //$NON-NLS-1$
		fileDialog.setFilterNames(new String[] {"A-STPA report (*.pdf)"}); //$NON-NLS-1$
		fileDialog.setFileName(this.getProjectName());
		String filePath = fileDialog.open();
		if (filePath != null) {
			this.setExportPath(filePath);
		}
	
	}

	@Override
	public String getExportPath() {
		return this.textExportPath.getText();
	}
	
	@Override
	public void setExportPath(String path) {
		this.textExportPath.setText(path);
	}
}
