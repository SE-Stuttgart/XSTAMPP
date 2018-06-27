/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software
 * Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.wizards.pages;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.Severity;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.AbstractExportPage;
import xstampp.util.DefaultSelectionAdapter;

public class StatisticsExportPage extends AbstractExportPage {

  private boolean isUseExport;
  private Map<Severity, Integer> sc_per_acc;
  private Map<Severity, Integer> cf_per_uca;
  private int uca_per_ca;

  public StatisticsExportPage(String pageName, String pluginID) {
    super(pageName, pluginID);
    this.cf_per_uca = new HashMap<>();
    this.cf_per_uca.put(Severity.S0, 21);
    this.cf_per_uca.put(Severity.S1, 21);
    this.cf_per_uca.put(Severity.S2, 21);
    this.cf_per_uca.put(Severity.S3, 21);

    this.sc_per_acc = new HashMap<>();
    this.sc_per_acc.put(Severity.S0, 1);
    this.sc_per_acc.put(Severity.S1, 1);
    this.sc_per_acc.put(Severity.S2, 1);
    this.sc_per_acc.put(Severity.S3, 1);

    this.uca_per_ca = 4;
  }

  @Override
  public boolean asOne() {
    return true;
  }

  public boolean isUseExport() {
    return isUseExport;
  }

  public Map<Severity, Integer> getSc_per_acc() {
    return sc_per_acc;
  }

  public Map<Severity, Integer> getCf_per_uca() {
    return cf_per_uca;
  }

  public int getUca_per_ca() {
    return uca_per_ca;
  }

  @Override
  public void createControl(Composite parent) {
    Composite control = new Composite(parent, SWT.None);
    control.setLayout(new FormLayout());
    addProjectChooser(control, new FormAttachment());
    Group exportGroup = new Group(control, SWT.None);
    exportGroup.setText("Export Method");
    exportGroup.setLayoutData(getDefaultFormData(null, 4));
    exportGroup.setLayout(new GridLayout());
    Button useView = new Button(exportGroup, SWT.RADIO);
    useView.setText("Use XSTAMPP View");
    useView.setSelection(true);
    final Button useExport = new Button(exportGroup, SWT.RADIO);
    useExport.setText("Export to Excel");
    this.pathChooser = new PathComposite(new String[] { "*.xlsx" }, control, PathComposite.PATH_DIALOG);
    useExport.addSelectionListener(new DefaultSelectionAdapter((obj) -> {
      pathChooser.setEnabled(useExport.getSelection());
      isUseExport = useExport.getSelection();
      setNeedsPath(useExport.getSelection());
    }));
    pathChooser.setEnabled(false);
    setNeedsPath(false);
    pathChooser.setLayoutData(getDefaultFormData(exportGroup, 3));

    Composite parameterPanel = new Composite(control, SWT.None);
    parameterPanel.setLayoutData(getDefaultFormData(pathChooser, 3));
    parameterPanel.setLayout(new GridLayout(4, false));
    new Label(parameterPanel, SWT.NONE);
    IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(this.getProjectID());
    if (((DataModelController) dataModel).getHazAccController().isUseHazardConstraints()) {
      new Label(parameterPanel, SWT.BORDER).setText("Safety Constraints per Hazard");
    } else {
      new Label(parameterPanel, SWT.BORDER).setText("Safety Constraints per Accident");
    }
    new Label(parameterPanel, SWT.BORDER).setText("UCAs per Control Action");
    new Label(parameterPanel, SWT.BORDER).setText("Causal Factors per UCA");
    new Label(parameterPanel, SWT.BORDER).setText("S0");
    createSpinner(sc_per_acc, Severity.S0, parameterPanel);
    Spinner spinner = new Spinner(parameterPanel, SWT.BORDER);
    spinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
    spinner.setSelection(uca_per_ca);
    spinner.addModifyListener(arg0 -> uca_per_ca = spinner.getSelection());
    createSpinner(cf_per_uca, Severity.S0, parameterPanel);
    new Label(parameterPanel, SWT.BORDER).setText("S1");
    createSpinner(sc_per_acc, Severity.S1, parameterPanel);
    createSpinner(cf_per_uca, Severity.S1, parameterPanel);
    new Label(parameterPanel, SWT.BORDER).setText("S2");
    createSpinner(sc_per_acc, Severity.S2, parameterPanel);
    createSpinner(cf_per_uca, Severity.S2, parameterPanel);
    new Label(parameterPanel, SWT.BORDER).setText("S3");
    createSpinner(sc_per_acc, Severity.S3, parameterPanel);
    createSpinner(cf_per_uca, Severity.S3, parameterPanel);
    setControl(control);
  }

  private void createSpinner(Map<Severity, Integer> map, Severity key, Composite parent) {
    Spinner spinner = new Spinner(parent, SWT.BORDER);
    spinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    spinner.setSelection(map.get(key));
    spinner.addModifyListener(arg0 -> map.put(key, spinner.getSelection()));
  }
}
