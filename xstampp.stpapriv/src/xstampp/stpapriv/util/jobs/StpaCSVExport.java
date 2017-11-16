/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.stpapriv.util.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import messages.Messages;
import xstampp.astpa.model.causalfactor.interfaces.ICausalComponent;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactor;
import xstampp.astpa.model.causalfactor.interfaces.ICausalFactorEntry;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.stpapriv.messages.PrivMessages;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.stpapriv.model.results.ConstraintResult;
import xstampp.util.BufferedCSVWriter;

/**
 * This job exports all data from the STPA PrivacyController a CSV
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 * 
 */
public class StpaCSVExport extends Job {

  private final char seperator;
  private final String path;
  private final PrivacyController model;
  private final int type;
  private boolean enablePreview;

  /**
   * calls the Export function in the given view.
   * 
   * @author Lukas Balzer
   * @param name
   *          the name of the job
   * @param filePath
   *          String
   * @param seperator
   *          the separator with which the cells in the CSV are separated by
   * @param controller
   *          the data model which provides the necessary data
   * @param types
   *          the type of the export
   */
  public StpaCSVExport(String name, String filePath, char seperator,
      IDataModel controller, int types) {
    super(name);
    this.path = filePath;
    this.seperator = seperator;
    this.model = (PrivacyController) controller;
    this.type = types;
    this.enablePreview = true;
  }

  public void showPreview(boolean preview) {
    this.enablePreview = preview;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
    File tableCSV = new File(this.path);

    try (BufferedCSVWriter csvWriter = new BufferedCSVWriter(
        new FileWriter(tableCSV), this.seperator);) {

      if ((type & ICSVExportConstants.PROJECT_DESCRIPTION) != 0) {
        this.writeSystemDescCSV(csvWriter, Messages.SystemDescription);
      }
      if ((type & ICSVExportConstants.ACCIDENT) != 0) {
        this.exportAsCSV(this.model.getAllAccidents(), csvWriter,
            PrivMessages.Losses);
      }
      if ((type & ICSVExportConstants.HAZARD) != 0) {
        this.exportAsCSV(this.model.getAllHazards(), csvWriter,
            PrivMessages.Vulnerabilities);
      }
      if ((type & ICSVExportConstants.SAFETY_CONSTRAINT) != 0) {
        this.exportAsCSV(this.model.getAllSafetyConstraints(), "SR0.",
            csvWriter, PrivMessages.SecurityConstraints);
      }
      if ((type & ICSVExportConstants.SYSTEM_GOAL) != 0) {
        this.exportAsCSV(this.model.getAllSystemGoals(), csvWriter,
            Messages.SystemGoals);
      }
      if ((type & ICSVExportConstants.DESIGN_REQUIREMENT) != 0) {
        this.exportAsCSV(this.model.getAllDesignRequirements(),
            csvWriter, Messages.DesignRequirements);
      }
      if ((type & ICSVExportConstants.CONTROL_ACTION) != 0) {
        this.exportCAcAsCSV(this.model.getAllControlActions(), csvWriter,
            Messages.ControlActions);
      }
      if ((type & ICSVExportConstants.CORRESPONDING_SAFETY_CONSTRAINTS) != 0) {
        this.writeCscCSV(csvWriter, PrivMessages.CorrespondingSecurityConstraints);
      }
      if ((type & ICSVExportConstants.UNSAFE_CONTROL_ACTION) != 0) {
        this.writeUCACSV(csvWriter, PrivMessages.UnsecureControlActionsTable);
      }
      if ((type & ICSVExportConstants.CAUSAL_FACTOR) != 0) {
        this.writeCausalFactorsCSV(csvWriter,
            Messages.CausalFactorsTable);
      }
      if ((type & ICSVExportConstants.RESULT) != 0) {
        this.exportResAsCSV(this.model.getAllConstraintResults(), csvWriter,
            "Results Table");
      }
      csvWriter.close();
      if (this.enablePreview && tableCSV.exists() && Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(tableCSV);
      }
    } catch (IOException e) {
      return Status.CANCEL_STATUS;
    }

    return Status.OK_STATUS;

  }

  protected void exportAsCSV(List<ITableModel> models,
      BufferedCSVWriter csvWriter, String title) throws IOException {
    exportAsCSV(models, "", csvWriter, title);
  }

  protected void exportResAsCSV(List<ConstraintResult> models,
      BufferedCSVWriter csvWriter, String title) throws IOException {
    csvWriter.newLine();
    csvWriter.write(title);
    csvWriter.newLine();
    csvWriter.writeCell("STPA-Priv Step");
    csvWriter.writeCell("ID");
    csvWriter.writeCell("Privacy Constraint");
    csvWriter.writeCell("Corresponding Constraint");
    csvWriter.writeCell("Privacy Related");
    csvWriter.writeCell("Security Related");
    csvWriter.writeCell("Safety Related");
    csvWriter.newLine();
    int i = 0;
    for (ConstraintResult data : models) {
      i++;
      csvWriter.writeCell("" + Integer.toString(i));

      csvWriter.writeCell(data.getStpastep());
      csvWriter.writeCell(data.getScId());
      csvWriter.writeCell(data.getSecurityConstraint());
      if (data.getRelatedId() != null) {
        csvWriter.writeCell(data.getRelatedId());
      } else {
        csvWriter.writeCell("");
      }
      if (data.isPrivate() != null) {
        csvWriter.writeCell(data.isPrivate().toString());
      } else {
        csvWriter.writeCell("");
      }
      if (data.isSecure() != null) {
        csvWriter.writeCell(data.isSecure().toString());
      } else {
        csvWriter.writeCell("");
      }
      if (data.isSafe() != null) {
        csvWriter.writeCell(data.isSafe().toString());
      } else {
        csvWriter.writeCell("");
      }

      csvWriter.newLine();
    }
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param models
   *          the data which shall be exported as CSV
   * @throws IOException
   */
  protected void exportAsCSV(List<ITableModel> models, String literals,
      BufferedCSVWriter csvWriter, String title) throws IOException {
    csvWriter.newLine();
    csvWriter.write(title);
    csvWriter.newLine();
    csvWriter.writeCell("ID");
    csvWriter.writeCell("Name");
    csvWriter.write("Description");
    csvWriter.newLine();
    int i = 0;
    for (ITableModel data : models) {
      i++;
      csvWriter.writeCell(literals + Integer.toString(i));

      csvWriter.writeCell(data.getTitle());
      csvWriter.write(data.getDescription());
      csvWriter.newLine();
    }
  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param models
   *          the data which shall be exported as CSV
   * @throws IOException
   */
  protected void exportCAcAsCSV(List<IControlAction> models,
      BufferedCSVWriter csvWriter, String title) throws IOException {
    csvWriter.newLine();
    csvWriter.write(title);
    csvWriter.newLine();
    csvWriter.writeCell("ID");
    csvWriter.writeCell("Name");
    csvWriter.write("Description");
    csvWriter.newLine();
    int i = 0;
    for (ITableModel data : models) {
      i++;
      csvWriter.writeCell(Integer.toString(i));

      csvWriter.writeCell(data.getTitle());
      csvWriter.write(data.getDescription());
      csvWriter.newLine();
    }
  }

  private void writeCausalFactorsCSV(BufferedCSVWriter writer, String title)
      throws IOException {
    PrivacyController model = this.model;
    // the First two Rows are filled with the view- and the Column-titles
    writer.newLine();
    writer.write(title + " - "); ////$NON-NLS-1$
    writer.write(this.model.getProjectName());
    writer.newLine();
    writer.writeCell(Messages.Component);
    writer.writeCell(Messages.CausalFactors);
    writer.writeCell(PrivMessages.UnsecureControlActions);
    writer.writeCell(PrivMessages.VulnerabilityLinks);
    writer.writeCell("Scenario");
    writer.writeCell(PrivMessages.SecurityConstraints);
    writer.write(Messages.NotesSlashRationale);
    writer.newLine();
    boolean writeNote = true;
    Map<UUID, String> ucaDescMap = new HashMap<>();
    for (ICorrespondingUnsafeControlAction uca : model.getAllUnsafeControlActions()) {
      ucaDescMap.put(uca.getId(), "PCA1." + uca.getNumber() + ": " + uca.getDescription());
    }
    for (ICausalComponent component : this.model.getCausalComponents()) {
      SortedMap<ICausalFactor, List<Link>> factorBasedMap = model.getCausalFactorController()
          .getCausalFactorBasedMap(component, model.getLinkController());
      // this loop writes two lines
      for (ICausalFactor factor : factorBasedMap.keySet()) {

        for (Link entryLink : factorBasedMap.get(factor)) {
          String ucaCell = ""; //$NON-NLS-1$
          String hazCell = ""; //$NON-NLS-1$
          String scenarioCell = ""; //$NON-NLS-1$
          String safetyCell = ""; //$NON-NLS-1$

          Link ucaCfLink = model.getLinkController().getLinkObjectFor(ObserverValue.UCA_CausalFactor_LINK,
              entryLink.getLinkA());

          if (ucaDescMap.containsKey(ucaCfLink.getLinkA())) {
            ucaCell = ucaDescMap.get(ucaCfLink.getLinkA());
            for (ITableModel haz : model.getLinkedHazardsOfUCA(ucaCfLink.getLinkA())) {
              hazCell += "V-" + haz.getNumber() + ",";
            }
          }
          hazCell = hazCell.substring(0, hazCell.length() - 1);

          // writer.writeCell(component.getText());
          // writer.writeCell(factor.getText());
          // writer.writeCell(ucaCell);
          // writer.writeCell(hazCell);
          // writer.writeCell();
          // writer.writeCell(
          // model.getCausalFactorController().getConstraintTextFor(entry.getId()));
          // writer.writeCell(factor.getNote());
          // writer.newLine();
          //
          // for(UUID provider : entry.getScenarioLinks()){
          //
          // writer.writeCell(component.getText());
          // writer.writeCell(factor.getText());
          // writer.writeCell(ucaCell);
          // writer.writeCell(hazCell);
          // writer.writeCell(model.getRefinedScenario(provider).getSafetyRule());
          // writer.writeCell(model.getRefinedScenario(provider).getRefinedSafetyConstraint());
          // writer.writeCell(factor.getNote());
          // writer.newLine();
          // }
        }
      }
    }
  }

  private void writeSystemDescCSV(BufferedCSVWriter writer, String title)
      throws IOException {

    writer.newLine();
    writer.write(title + " - "); ////$NON-NLS-1$
    writer.write(this.model.getProjectName());
    writer.newLine();
    String description = this.model.getProjectDescription();
    for (int length = 0; length < description.length(); length += 200) {
      writer.writeCell();
      writer.writeCell(description.substring(length,
          Math.min(description.length(), length + 199)));
      writer.newLine();
    }
  }

  private void writeUCACSV(BufferedCSVWriter writer, String title)
      throws IOException {
    int length;

    WriterList notGiven = new WriterList();
    WriterList givenInc = new WriterList();
    WriterList wrongTiming = new WriterList();
    WriterList stoppedTooSoon = new WriterList();

    // the First two Rows are filled with the view- and the Column-titles
    writer.newLine();
    writer.write(title + " - "); ////$NON-NLS-1$
    writer.write(this.model.getProjectName());
    writer.newLine();
    writer.writeCell(Messages.ControlAction);
    writer.writeCell(PrivMessages.NotGiven2);
    writer.writeCell(PrivMessages.GivenIncorrectly2);
    writer.writeCell(PrivMessages.WrongTiming2);
    writer.write(Messages.StoppedTooSoon);
    writer.newLine();
    writer.newLine();
    for (IControlAction action : this.model.getAllControlActionsU()) {
      // for each controlAction the lists are filled with its uca's
      notGiven.setList(action
          .getUnsafeControlActions(UnsafeControlActionType.NOT_GIVEN));
      givenInc.setList(action
          .getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY));
      wrongTiming
          .setList(action
              .getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING));
      stoppedTooSoon
          .setList(action
              .getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON));

      length = Math.max(notGiven.size(), givenInc.size());
      length = Math.max(length, wrongTiming.size());
      length = Math.max(length, stoppedTooSoon.size());

      // this loop writes two lines
      for (int i = 0; i < length; i++) {
        if (i == 0) {
          writer.writeCell(action.getTitle());
        } else {
          writer.writeCell();
        }
        if (!this.model.getLinkedHazardsOfUCA(notGiven.getUCAId(i)).isEmpty()) {
          writer.writeCell("PCA1." + model.getUCANumber(notGiven.getUCAId(i)));
        }
        if (!this.model.getLinkedHazardsOfUCA(givenInc.getUCAId(i)).isEmpty()) {
          writer.writeCell("PCA1." + model.getUCANumber(givenInc.getUCAId(i)));
        }
        if (!this.model.getLinkedHazardsOfUCA(wrongTiming.getUCAId(i)).isEmpty()) {
          writer.writeCell("PCA1." + model.getUCANumber(wrongTiming.getUCAId(i)));
        }
        if (!this.model.getLinkedHazardsOfUCA(stoppedTooSoon.getUCAId(i)).isEmpty()) {
          writer.writeCell("PCA1." + model.getUCANumber(stoppedTooSoon.getUCAId(i)));
        }
        writer.newLine();
        // the description line starting with an empty cell
        writer.writeCell();
        // write the Descriptions in one line
        writer.writeCell(notGiven.getUCADescription(i));
        writer.writeCell(givenInc.getUCADescription(i));
        writer.writeCell(wrongTiming.getUCADescription(i));
        writer.write(stoppedTooSoon.getUCADescription(i));
        writer.newLine();

        // the hazard line starting with an empty cell
        writer.writeCell();
        for (ITableModel haz : this.model
            .getLinkedHazardsOfUCA(notGiven.getUCAId(i))) {
          writer.write("[V-" + haz.getNumber() + "]");
        }
        writer.writeCell();
        for (ITableModel haz : this.model
            .getLinkedHazardsOfUCA(givenInc.getUCAId(i))) {
          writer.write("[V-" + haz.getNumber() + "]");
        }
        writer.writeCell();
        for (ITableModel haz : this.model
            .getLinkedHazardsOfUCA(wrongTiming.getUCAId(i))) {
          writer.write("[V-" + haz.getNumber() + "]");
        }
        writer.writeCell();
        for (ITableModel haz : this.model
            .getLinkedHazardsOfUCA(stoppedTooSoon.getUCAId(i))) {
          writer.write("[V-" + haz.getNumber() + "]");
        }
        writer.newLine();
      }
    }
  }

  private class WriterList {
    List<IUnsafeControlAction> list;

    public WriterList() {
      this.list = new ArrayList<>();
    }

    public void setList(List<IUnsafeControlAction> list) {
      this.list = list;
    }

    public int size() {
      return this.list.size();
    }

    public String getUCADescription(int i) {
      if (this.list.size() > i) {
        return this.list.get(i).getDescription();
      }
      return "";
    }

    public UUID getUCAId(int i) {
      if (this.list.size() > i) {
        return this.list.get(i).getId();
      }
      return UUID.randomUUID();
    }

  }

  private void writeCscCSV(BufferedCSVWriter csvWriter, String title) throws IOException {
    // the First two Rows are filled with the view- and the Column-titles
    csvWriter.newLine();
    csvWriter.write(title + " - "); ////$NON-NLS-1$
    csvWriter.write(this.model.getProjectName());
    csvWriter.newLine();
    csvWriter.writeCell(Messages.ID);
    csvWriter.writeCell(PrivMessages.UnsecureControlActions);
    csvWriter.writeCell(Messages.ID);
    csvWriter.writeCell(PrivMessages.CorrespondingSecurityConstraints);
    csvWriter.newLine();
    for (ICorrespondingUnsafeControlAction data : this.model
        .getAllUnsafeControlActions()) {

      csvWriter.writeCell("PCA1." + this.model.getUCANumber(data.getId()));
      csvWriter.writeCell(data.getDescription());
      csvWriter.writeCell("SR1." + this.model.getUCANumber(data.getId()));
      csvWriter.writeCell(data.getCorrespondingSafetyConstraint().getText());
      csvWriter.newLine();
    }
  }
}
