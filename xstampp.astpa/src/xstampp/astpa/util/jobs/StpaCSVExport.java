package xstampp.astpa.util.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.ITableModel;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.causalfactor.ICausalFactor;
import xstampp.astpa.model.controlaction.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.wizards.BufferedCSVWriter;
import xstampp.model.IDataModel;

/**
 * This job exports all data from the STPA DataModelController a CSV
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 * 
 */
public class StpaCSVExport extends Job {

	private final char seperator;
	private final String path;
	private final DataModelController model;
	private final List<String> type;

	/**
	 * calls the Export function in the given view.
	 * 
	 * @author Lukas Balzer
	 * @param name
	 *            the name of the job
	 * @param filePath
	 *            String
	 * @param seperator
	 *            the separator with which the cells in the CSV are separated by
	 * @param controller
	 *            the data model which provides the necessary data
	 * @param types
	 *            the type of the export
	 */
	public StpaCSVExport(String name, String filePath, char seperator,
			IDataModel controller, List<String> types) {
		super(name);
		this.path = filePath;
		this.seperator = seperator;
		this.model = (DataModelController) controller;
		this.type = types;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.ExportingCSV, IProgressMonitor.UNKNOWN);
		File tableCSV = new File(this.path);

		try (BufferedCSVWriter csvWriter = new BufferedCSVWriter(
				new FileWriter(tableCSV), this.seperator);) {

			if (this.type.contains(ICSVExportConstants.PROJECT_DESCRIPTION)) {
				this.writeSystemDescCSV(csvWriter, Messages.SystemDescription);
			}
			if (this.type.contains(ICSVExportConstants.ACCIDENT)) {
				this.exportAsCSV(this.model.getAllAccidents(), csvWriter,
						Messages.Accidents);
			}
			if (this.type.contains(ICSVExportConstants.HAZARD)) {
				this.exportAsCSV(this.model.getAllHazards(), csvWriter,
						Messages.Hazards);
			}
			if (this.type.contains(ICSVExportConstants.SAFETY_CONSTRAINT)) {
				this.exportAsCSV(this.model.getAllSafetyConstraints(),
						csvWriter, Messages.SafetyConstraints);
			}
			if (this.type.contains(ICSVExportConstants.SYSTEM_GOAL)) {
				this.exportAsCSV(this.model.getAllSystemGoals(), csvWriter,
						Messages.SystemGoals);
			}
			if (this.type.contains(ICSVExportConstants.DESIGN_REQUIREMENT)) {
				this.exportAsCSV(this.model.getAllDesignRequirements(),
						csvWriter, Messages.DesignRequirements);
			}
			if (this.type.contains(ICSVExportConstants.CONTROL_ACTION)) {
				this.exportAsCSV(this.model.getAllControlActions(), csvWriter,
						Messages.ControlActions);
			}
			if (this.type
					.contains(ICSVExportConstants.CORRESPONDING_SAFETY_CONSTRAINTS)) {
				this.writeCscCSV(csvWriter);
			}
			if (this.type.contains(ICSVExportConstants.UNSAFE_CONTROL_ACTION)) {
				this.writeUCACSV(csvWriter, Messages.UnsafeControlActionsTable);
			}
			if (this.type.contains(ICSVExportConstants.CAUSAL_FACTOR)) {
				this.writeCausalFactorsCSV(csvWriter,
						Messages.CausalFactorsTable);
			}
			csvWriter.close();
			if (tableCSV.exists() && Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(tableCSV);
			}
		} catch (IOException e) {
			return Status.CANCEL_STATUS;
		}

		return Status.OK_STATUS;

	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param models
	 *            the data which shall be exported as CSV
	 * @throws IOException
	 */
	protected void exportAsCSV(List<ITableModel> models,
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

		// the First two Rows are filled with the view- and the Column-titles
		writer.newLine();
		writer.write(title + " - "); ////$NON-NLS-1$
		writer.write(this.model.getProjectName());
		writer.newLine();
		writer.writeCell(Messages.Component);
		writer.writeCell(Messages.CausalFactors);
		writer.writeCell(Messages.HazardLinks);
		writer.writeCell(Messages.SafetyConstraints);
		writer.write(Messages.NotesSlashRationale);
		writer.newLine();
		writer.newLine();
		for (ICausalComponent action : this.model.getCausalComponents()) {
			ICausalFactor factor;

			// this loop writes two lines
			for (int index = 0; index < action.getCausalFactors().size(); index++) {
				factor = action.getCausalFactors().get(index);
				if (index == 0) {
					writer.writeCell(action.getText());
				} else {
					writer.writeCell();
				}

				// write the Descriptions in one line
				writer.writeCell(factor.getText());
				for (ITableModel haz : this.model.getLinkedHazardsOfCf(factor
						.getId())) {
					writer.write("[H-" + haz.getNumber() + "]");
				}
				writer.writeCell();
				writer.writeCell(factor.getSafetyConstraint().getText());
				writer.writeCell(factor.getNote());
				writer.newLine();
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
		for (int length = 200; length <= description.length(); length += 200) {
			writer.writeCell();
			writer.writeCell(description.substring(length - 200, length - 1));
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
		writer.writeCell(Messages.NotGiven);
		writer.writeCell(Messages.GivenIncorrectly);
		writer.writeCell(Messages.WrongTiming);
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
					writer.write("[H-" + haz.getNumber() + "]");
				}
				writer.writeCell();
				for (ITableModel haz : this.model
						.getLinkedHazardsOfUCA(givenInc.getUCAId(i))) {
					writer.write("[H-" + haz.getNumber() + "]");
				}
				writer.writeCell();
				for (ITableModel haz : this.model
						.getLinkedHazardsOfUCA(wrongTiming.getUCAId(i))) {
					writer.write("[H-" + haz.getNumber() + "]");
				}
				writer.writeCell();
				for (ITableModel haz : this.model
						.getLinkedHazardsOfUCA(stoppedTooSoon.getUCAId(i))) {
					writer.write("[H-" + haz.getNumber() + "]");
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

	private void writeCscCSV(BufferedCSVWriter csvWriter) throws IOException {

		csvWriter.writeCell(Messages.CorrespondingSafetyConstraints);
		csvWriter.newLine();
		for (ICorrespondingUnsafeControlAction data : this.model
				.getAllUnsafeControlActions()) {
			csvWriter.writeCell();
			csvWriter.writeCell(data.getDescription() );
			csvWriter.writeCell(data.getCorrespondingSafetyConstraint().getText());
			csvWriter.newLine();
		}
	}
}
