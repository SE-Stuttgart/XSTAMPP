package acast.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import messages.Messages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.model.IDataModel;
import acast.controller.Controller;
import acast.model.ITableModel;
import acast.model.causalfactor.ICausalComponent;
import acast.model.controlstructure.Responsibility;
import acast.ui.accidentDescription.ProximalEvent;
import acast.ui.accidentDescription.Recommendation;
import acast.wizards.BufferedCSVWriter;

public class CastCSVExport extends Job {

	private final char seperator;
	private final String path;
	private final Controller model;
	private final List<String> type;

	public CastCSVExport(String name, String filePath, char seperator, IDataModel controller, List<String> types) {
		super(name);
		this.path = filePath;
		this.seperator = seperator;
		this.model = (Controller) controller;
		this.type = types;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.ExportingCSV, IProgressMonitor.UNKNOWN);
		File tableCSV = new File(this.path);

		try (BufferedCSVWriter csvWriter = new BufferedCSVWriter(new FileWriter(tableCSV), this.seperator);) {

			if (this.type.contains("Accident Description")) {
				this.writeAccidentDescCSV(csvWriter, "Accident Description");
			}

			if (this.type.contains("Safety Constraints")) {
				this.exportAsCSV(this.model.getAllSafetyConstraints(), csvWriter, "Safety Constraints");
			}

			if (this.type.contains("Hazards")) {
				this.exportAsCSV(this.model.getAllHazards(), csvWriter, "Hazards");
			}

			if (this.type.contains("Proximal Events")) {
				this.getProximalEventsAsCSV(this.model.getEventList(), csvWriter);
			}

			if (this.type.contains("Roles and Responsibilities")) {
				this.getResponsibilitiesAsCSV(csvWriter);
			}

			if (this.type.contains("Findings and Recommendations")) {
				this.getFindingsAsCSV(csvWriter);
			}

			csvWriter.close();
//			if (tableCSV.exists() && Desktop.isDesktopSupported()) {
//				Desktop.getDesktop().open(tableCSV);
//			}
		} catch (IOException e) {
			return Status.CANCEL_STATUS;
		}

		return Status.OK_STATUS;
	}

	private void writeAccidentDescCSV(BufferedCSVWriter writer, String title) throws IOException {
		writer.newLine();
		writer.write("Project Name- ");
		writer.write(this.model.getProjectName());
		writer.newLine();
		writer.write(title);
		writer.newLine();
		String description = this.model.getAccidentDescription();
		for (int length = 0; length < description.length(); length += 200) {
			writer.writeCell();
			writer.writeCell(description.substring(length, Math.min(description.length(), length + 199)));
			writer.newLine();
		}
		writer.newLine();
		writer.write("Accident Company");
		writer.newLine();
		description = this.model.getAccidentCompany();
		for (int length = 0; length < description.length(); length += 200) {
			writer.writeCell();
			writer.writeCell(description.substring(length, Math.min(description.length(), length + 199)));
			writer.newLine();
		}
		writer.newLine();
		writer.write("Accident Date");
		writer.newLine();
		if (!this.model.getAccidentDate().isEmpty()) {
			writer.writeCell();
			String date = this.model.getAccidentDate();
			writer.writeCell("'" + date + "'");

		}
		writer.newLine();
		writer.write("Accident Location");
		writer.newLine();
		description = this.model.getAccidentLocation();
		for (int length = 0; length < description.length(); length += 200) {
			writer.writeCell();
			writer.writeCell(description.substring(length, Math.min(description.length(), length + 199)));
			writer.newLine();
		}
	}

	protected void exportAsCSV(List<ITableModel> models, BufferedCSVWriter csvWriter, String title) throws IOException {
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

	private void getProximalEventsAsCSV(List<ProximalEvent> models, BufferedCSVWriter csvWriter) throws IOException {
		csvWriter.newLine();
		csvWriter.write("Proximal Events");
		csvWriter.newLine();
		csvWriter.writeCell("ID");
		csvWriter.writeCell("Date");
		csvWriter.writeCell("Time");
		csvWriter.write("Description");
		csvWriter.newLine();
		int i = 0;
		if (!models.isEmpty()) {
			for (ProximalEvent data : models) {
				i++;
				csvWriter.writeCell(Integer.toString(i));
				csvWriter.writeCell(data.getDate());
				csvWriter.writeCell(data.getTime());
				csvWriter.write(data.getDescription());
				csvWriter.newLine();
			}
		}
	}

	private void getResponsibilitiesAsCSV(BufferedCSVWriter csvWriter) throws IOException {
		int i;
		csvWriter.newLine();
		csvWriter.write("Roles and Responsibilities");
		csvWriter.newLine();
		for (ICausalComponent name : this.model.getCasualComponents()) {
			csvWriter.write("Role - ");
			csvWriter.write(name.getText());
			csvWriter.newLine();
			if (!this.model.getResponsibilitiesList(name.getId()).isEmpty()) {

				csvWriter.write("Safety Related Responsibilities");
				csvWriter.newLine();
				csvWriter.writeCell("ID");
				csvWriter.write("Description");
				csvWriter.newLine();
				i = 0;
				for (Responsibility resp : this.model.getResponsibilitiesList(name.getId())) {
					i++;
					csvWriter.writeCell(Integer.toString(i));
					csvWriter.write(resp.getDescription());
					csvWriter.newLine();
				}
			}
			if (!this.model.getContextList(name.getId()).isEmpty()) {
				csvWriter.newLine();
				csvWriter.write("Context in which Decision made");
				csvWriter.newLine();
				csvWriter.writeCell("ID");
				csvWriter.write("Description");
				csvWriter.newLine();
				i = 0;
				for (Responsibility resp : this.model.getContextList(name.getId())) {
					i++;
					csvWriter.writeCell(Integer.toString(i));
					csvWriter.write(resp.getDescription());
					csvWriter.newLine();
				}
			}
			if (!this.model.getUnsafeActionsList(name.getId()).isEmpty()) {
				csvWriter.newLine();
				csvWriter.write("Unsafe Decisions and Control Actions");
				csvWriter.newLine();
				csvWriter.writeCell("ID");
				csvWriter.write("Description");
				csvWriter.newLine();
				i = 0;
				for (Responsibility resp : this.model.getUnsafeActionsList(name.getId())) {
					i++;
					csvWriter.writeCell(Integer.toString(i));
					csvWriter.write(resp.getDescription());
					csvWriter.newLine();
				}
			}
			if (!this.model.getFlawsList(name.getId()).isEmpty()) {
				csvWriter.newLine();
				csvWriter.write("Process Model Flaws");
				csvWriter.newLine();
				csvWriter.writeCell("ID");
				csvWriter.write("Description");
				csvWriter.newLine();
				i = 0;
				for (Responsibility resp : this.model.getFlawsList(name.getId())) {
					i++;
					csvWriter.writeCell(Integer.toString(i));
					csvWriter.write(resp.getDescription());
					csvWriter.newLine();
				}
			}
			csvWriter.newLine();
		}

	}

	private void getFindingsAsCSV(BufferedCSVWriter csvWriter) throws IOException {
		csvWriter.newLine();
		csvWriter.write("Findings and Recommendations");
		csvWriter.newLine();
		for (ICausalComponent name : this.model.getCasualComponents()) {
			csvWriter.write("Role - ");
			csvWriter.write(name.getText());
			csvWriter.newLine();
			if (!this.model.getRecommendationList(name.getId()).isEmpty()) {
				csvWriter.writeCell("ID");
				csvWriter.write("Description");
				csvWriter.newLine();
				int i = 0;
				for (Recommendation resp : this.model.getRecommendationList(name.getId())) {
					i++;
					csvWriter.writeCell(Integer.toString(i));
					csvWriter.write(resp.getDescription());
					csvWriter.newLine();
				}
				csvWriter.newLine();
			}
		}
	}
}
