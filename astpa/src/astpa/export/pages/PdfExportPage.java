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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import astpa.Activator;
import astpa.export.AbstractExportPage;
import astpa.preferences.IPreferenceConstants;

/**
 * Create export page.
 * 
 * @author Sebastian Sieber, Lukas Balzer
 * 
 */
public class PdfExportPage extends AbstractExportPage implements ModifyListener{
	
	private Composite container;
	private Text textCompany, textLogo,textExportPath;
	private ColorChooser bgChooser,fontChooser;
	private Composite sampleComp;
	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	
	
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
		FormData data;
		this.container = new Composite(parent, SWT.NONE);
		this.container.setLayout(new FormLayout());
		
		
		Composite labelComposite= new Composite(this.container, SWT.NONE);
		data=new FormData();
		data.top = new FormAttachment(COMPONENT_OFFSET);
		data.height = 25;
		labelComposite.setLayoutData(data);
		labelComposite.setLayout(null);
		Label labelCompany = new Label(labelComposite, SWT.SHADOW_IN);
		labelCompany.setBounds(LABEL_COLUMN, 0, 55, 15);
		labelCompany.setText(Messages.Company);
		
		this.textCompany = new Text(labelComposite,SWT.BORDER | SWT.SINGLE);
		this.textCompany.setBounds(TEXT_COLUMN, 0, 345, 21);
		
		String companyName = this.store.getString(IPreferenceConstants.COMPANY_NAME);

		if (companyName != null) {
			this.textCompany.setText(companyName);
			this.textCompany.setSelection(companyName.length());
		} else {
			this.textCompany.setText(""); //$NON-NLS-1$
		}
		
		PathComposite logoComposite= new PathComposite(this.container, SWT.NONE, Messages.Logo);
		data=new FormData();
		data.top = new FormAttachment(labelComposite,COMPONENT_OFFSET);
		data.height = 25;
		logoComposite.setLayoutData(data);
		this.textLogo = logoComposite.getText();
		logoComposite.setVisible(true);
		
		logoComposite.addButtonListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				PdfExportPage.this.openLogoDialog();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// no-op
			}
		});
		
		this.bgChooser=new ColorChooser(this.container, SWT.NONE, Messages.BackgroundColor,
											IPreferenceConstants.COMPANY_BACKGROUND_COLOR);
		data=new FormData();
		data.top = new FormAttachment(logoComposite,COMPONENT_OFFSET);
		data.height = 25;
		this.bgChooser.setLayoutData(data);
		this.bgChooser.setVisible(true);
		this.bgChooser.addColorChangeListener(this);
		
		this.fontChooser=new ColorChooser(this.container, SWT.NONE,Messages.FontColor,
				IPreferenceConstants.COMPANY_FONT_COLOR);
		data=new FormData();
		data.top = new FormAttachment(this.bgChooser,COMPONENT_OFFSET);
		data.height = 25;
		this.fontChooser.setLayoutData(data);
		this.fontChooser.setVisible(true);
		this.fontChooser.addColorChangeListener(this);
		
		PathComposite path= new PathComposite(this.container, SWT.NONE, Messages.Destination);
		data=new FormData();
		data.top = new FormAttachment(this.fontChooser,COMPONENT_OFFSET);
		data.height = 25;
		path.setLayoutData(data);
		path.setVisible(true);
		path.addButtonListener(new SelectionListener(){
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				PdfExportPage.this.openExportDialog(new String[]{"*.pdf"},new String[]{"A-STPA Report *.pdf"});
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// no-op
			}
		});
		this.textExportPath= path.getText();
		
		this.sampleComp= new DemoCanvas(this.container, SWT.NONE);
		data=new FormData();
		data.top= new FormAttachment(path, COMPONENT_OFFSET);
		data.height = DEMOCANVAS_HEIGHT;
		data.width = parent.getBounds().width;
		this.sampleComp.setLayoutData(data);
		
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
		FileDialog dlg = new FileDialog(PdfExportPage.this.getShell(), SWT.OPEN);
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
		
	
	
	/**
	 * @return the textCompany
	 */
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
	
	
	/**
	 * @return path of the Logo img
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
	 * @return the backgroundColor as an rgb string
	 */
	public String getTextBackgroundColor() {
		return this.bgChooser.getColorString();
	}
	
	
	/**
	 * @return fontColor as an rgb string
	 */
	public String getTextFontColor() {
		return this.fontChooser.getColorString();
	}


	@Override
	public String getExportPath() {
		return this.textExportPath.getText();
	}
	
	@Override
	public void setExportPath(String path) {
		this.textExportPath.setText(path);
	}

	@Override
	public boolean asOne() {
		return false;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		this.sampleComp.redraw();
	}
}
