package xstampp.astpa.util.jobs.statistics;

import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.interfaces.Severity;

public class SeveritySheet extends AbstractProgressSheetCreator {

  public SeveritySheet(Workbook wb, DataModelController controller, STEP step) {
    super(wb, controller, step);
  }

  @Override
  protected void createWorkSheet(Sheet sheet) {
    Row row = createRow(sheet);
    createCells(row, new String[] { "Severity Definitions", "" }, Styles.HEADER_STYLE, sheet);
    createCell(row, 0, "Severity", Styles.HEADER_STYLE);
    createCell(row, 1, "Description", Styles.HEADER_STYLE);

    List<Severity> severities = Arrays.asList(Severity.S0, Severity.S1, Severity.S2, Severity.S3);
    for (Severity severity : severities) {
      row = createRow(sheet);
      createCell(row, 0, severity.name());
      createCell(row, 1, severity.getDescription());
      triggerDefaultStyle();
    }
    sheet.autoSizeColumn(0);
    sheet.autoSizeColumn(1);
  }

}
