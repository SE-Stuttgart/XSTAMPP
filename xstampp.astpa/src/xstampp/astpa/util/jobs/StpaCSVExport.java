/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.util.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import messages.Messages;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.CausalCSComponent;
import xstampp.astpa.model.causalfactor.CausalFactor;
import xstampp.astpa.model.causalfactor.CausalFactorEntry;
import xstampp.astpa.model.causalfactor.CausalHazardEntry;
import xstampp.astpa.model.causalfactor.CausalScenarioEntry;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.model.IDataModel;
import xstampp.util.BufferedCSVWriter;

/**
 * This job exports all data from the STPA DataModelController a CSV
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 * 
 */
public class StpaCSVExport extends Job implements IProjectExport{

  private final char seperator;
  private final String path;
  private final DataModelController model;
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
  public StpaCSVExport(String name, String filePath, char seperator, IDataModel controller,
      int types) {
    super(name);
    this.path = filePath;
    this.seperator = seperator;
    this.model = (DataModelController) controller;
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

    try (BufferedCSVWriter csvWriter = new BufferedCSVWriter(new FileWriter(tableCSV),
        this.seperator);) {

      if ((type & ICSVExportConstants.PROJECT_DESCRIPTION) != 0) {
        this.writeSystemDescCSV(csvWriter, Messages.SystemDescription);
      }
      if ((type & ICSVExportConstants.ACCIDENT) != 0) {
        this.exportAsCSV(this.model.getAllAccidents(), csvWriter, Messages.Accidents);
      }
      if ((type & ICSVExportConstants.HAZARD) != 0) {
        this.exportAsCSV(this.model.getAllHazards(), csvWriter, Messages.Hazards);
      }
      if ((type & ICSVExportConstants.SAFETY_CONSTRAINT) != 0) {
        this.exportAsCSV(this.model.getAllSafetyConstraints(), csvWriter,
            Messages.SafetyConstraints);
      }
      if ((type & ICSVExportConstants.SYSTEM_GOAL) != 0) {
        this.exportAsCSV(this.model.getAllSystemGoals(), csvWriter, Messages.SystemGoals);
      }
      if ((type & ICSVExportConstants.DESIGN_REQUIREMENT) != 0) {
        this.exportAsCSV(this.model.getAllDesignRequirements(), csvWriter,
            Messages.DesignRequirements);
      }
      if ((type & ICSVExportConstants.CONTROL_ACTION) != 0) {
        this.exportAsCSV(this.model.getAllControlActions(), csvWriter, Messages.ControlActions);
      }
      if ((type & ICSVExportConstants.CORRESPONDING_SAFETY_CONSTRAINTS) != 0) {
        this.writeCscCSV(csvWriter, Messages.CorrespondingSafetyConstraints);
      }
      if ((type & ICSVExportConstants.UNSAFE_CONTROL_ACTION) != 0) {
        this.writeUCACSV(csvWriter, Messages.UnsafeControlActionsTable);
      }
      if ((type & ICSVExportConstants.CAUSAL_FACTOR) != 0) {
        this.writeCausalFactorsCSV(csvWriter, Messages.CausalFactorsTable);
      }
      if ((type & ICSVExportConstants.CAUSAL_FACTOR_SafetyConstraints) != 0) {
        this.exportAsCSV(this.model.getCausalFactorController().getSafetyConstraints(), csvWriter,
            Messages.SafetyConstraints + "Step 4");
      }
      csvWriter.close();
      if (this.enablePreview && tableCSV.exists() && Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(tableCSV);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return Status.CANCEL_STATUS;
    }

    return Status.OK_STATUS;

  }

  /**
   * 
   * @author Lukas Balzer
   * 
   * @param models
   *          the data which shall be exported as CSV
   * @throws IOException
   */
  protected void exportAsCSV(List<? extends ITableModel> models, BufferedCSVWriter csvWriter,
      String title) throws IOException {
    csvWriter.newLine();
    csvWriter.writeCell(title);
    csvWriter.newLine();
    csvWriter.writeCell("ID");
    csvWriter.writeCell("Name");
    csvWriter.writeCell("Description");
    csvWriter.newLine();
    for (ITableModel data : models) {
      csvWriter.writeCell(data.getIdString());
      csvWriter.writeCell(data.getTitle());
      csvWriter.writeCell(data.getDescription());
      csvWriter.newLine();
    }
  }

  private void writeCausalFactorsCSV(BufferedCSVWriter writer, String title) throws IOException {

    // the First two Rows are filled with the view- and the Column-titles
    writer.newLine();
    writer.write(title + " - "); ////$NON-NLS-1$
    writer.write(this.model.getProjectName());
    writer.newLine();
    writer.writeCell(Messages.Component);
    writer.writeCell(Messages.CausalFactors);
    writer.writeCell(Messages.UnsafeControlActions);
    writer.writeCell(Messages.HazardLinks);
    if (this.model.getCausalFactorController().isUseScenarios()) {
      writer.writeCell("Scenario");
    }
    writer.writeCell(Messages.SafetyConstraints);
    if (!this.model.getCausalFactorController().isUseScenarios()) {
      writer.writeCell("DesignHint");
    }
    writer.write(Messages.NotesSlashRationale);
    writer.newLine();

    for (CausalCSComponent component : this.model.getCausalFactorController()
        .getCausalComponents()) {
      // this loop writes two lines
      for (CausalFactor factor : component.getFactors()) {
        Map<UUID, String> ucaDescMap = new HashMap<>();
        for (ICorrespondingUnsafeControlAction uca : model.getAllUnsafeControlActions()) {
          ucaDescMap.put(uca.getId(), uca.getIdString() + ": " + uca.getDescription()); ////$NON-NLS-1$
        }
        for (CausalFactorEntry entry : factor.getEntries()) {
          writer.writeCell(component.getText());
          writer.writeCell(factor.getText());
          writer.writeCell(entry.getUcaDescription());

          writer.newLine();
          if (this.model.getCausalFactorController().isUseScenarios()) {
            
            for (CausalScenarioEntry scenarioEntry : entry.getScenarioEntries()) {
              writer.writeCell();
              writer.writeCell();
              writer.writeCell();
              writer.writeCell(entry.getHazardLinks());
              writer.writeCell(scenarioEntry.getDescription());
              writer.writeCell(scenarioEntry.getConstraint());
              writer.writeCell(scenarioEntry.getNote());
              writer.newLine();
            }
          } else {
            // if Scenarios analysis is not used than all safety constraints are stored as entries
            // for the respective hazards
            for (CausalHazardEntry hazardEntry : entry.getHazardEntries()) {
              writer.writeCell();
              writer.writeCell();
              writer.writeCell();
              writer.writeCell(hazardEntry.getText());
              writer.writeCell(hazardEntry.getConstraint());
              writer.writeCell(hazardEntry.getDesignHint());
              writer.writeCell(hazardEntry.getNote());
              writer.newLine();
            }
          }
        }
      }
    }
  }

  private void writeSystemDescCSV(BufferedCSVWriter writer, String title) throws IOException {

    writer.newLine();
    writer.write(title + " - "); ////$NON-NLS-1$
    writer.write(this.model.getProjectName());
    writer.newLine();
    String description = this.model.getProjectDescription();
    for (int length = 0; length < description.length(); length += 200) {
      writer.writeCell();
      writer.writeCell(description.substring(length, Math.min(description.length(), length + 199)));
      writer.newLine();
    }
  }

  private void writeUCACSV(BufferedCSVWriter writer, String title) throws IOException {
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
    writer.writeCell(Messages.NotGiven);
    writer.writeCell(Messages.GivenIncorrectly);
    writer.writeCell(Messages.WrongTiming);
    writer.write(Messages.StoppedTooSoon);
    writer.newLine();
    writer.newLine();
    for (IControlAction action : this.model.getAllControlActionsU()) {
      // for each controlAction the lists are filled with its uca's
      notGiven.setList(action.getUnsafeControlActions(UnsafeControlActionType.NOT_GIVEN));
      givenInc.setList(action.getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY));
      wrongTiming.setList(action.getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING));
      stoppedTooSoon
          .setList(action.getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON));

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
          writer.writeCell("UCA1." + model.getUCANumber(notGiven.getUCAId(i)));
        }
        if (!this.model.getLinkedHazardsOfUCA(givenInc.getUCAId(i)).isEmpty()) {
          writer.writeCell("UCA1." + model.getUCANumber(givenInc.getUCAId(i)));
        }
        if (!this.model.getLinkedHazardsOfUCA(wrongTiming.getUCAId(i)).isEmpty()) {
          writer.writeCell("UCA1." + model.getUCANumber(wrongTiming.getUCAId(i)));
        }
        if (!this.model.getLinkedHazardsOfUCA(stoppedTooSoon.getUCAId(i)).isEmpty()) {
          writer.writeCell("UCA1." + model.getUCANumber(stoppedTooSoon.getUCAId(i)));
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
        for (ITableModel haz : this.model.getLinkedHazardsOfUCA(notGiven.getUCAId(i))) {
          writer.write("[" + haz.getIdString() + "]");
        }
        writer.writeCell();
        for (ITableModel haz : this.model.getLinkedHazardsOfUCA(givenInc.getUCAId(i))) {
          writer.write("[" + haz.getIdString() + "]");
        }
        writer.writeCell();
        for (ITableModel haz : this.model.getLinkedHazardsOfUCA(wrongTiming.getUCAId(i))) {
          writer.write("[" + haz.getIdString() + "]");
        }
        writer.writeCell();
        for (ITableModel haz : this.model.getLinkedHazardsOfUCA(stoppedTooSoon.getUCAId(i))) {
          writer.write("[" + haz.getIdString() + "]");
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
    csvWriter.writeCell(Messages.CorrespondingSafetyConstraints);
    csvWriter.newLine();
    for (ICorrespondingUnsafeControlAction data : this.model.getAllUnsafeControlActions()) {
      csvWriter.writeCell(data.getCorrespondingSafetyConstraint().getIdString());
      csvWriter.writeCell(data.getCorrespondingSafetyConstraint().getText());
      csvWriter.newLine();
    }
  }

  @Override
  public UUID getProjectId() {
    return this.model.getProjectId();
  }
}
