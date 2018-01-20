package xstampp.astpa.util.jobs.word;

import java.util.EnumSet;

import xstampp.ui.wizards.AbstractExportPage;

public class ReportConfiguration {

  public enum ReportType {
    FINAL, UCA_TABLE, CAUSAL_FACTORS, SAFETY_CONSTRAINTS, SYSTEM_DESCRIPTION, ACCIDENTS, HAZARDS, DESIGN_REQUIREMENTS, CONTROL_ACTIONS;
  }

  private EnumSet<ReportType> typeSet;
  private String reportName;
  private String path;
  private int contentSize;
  private int headSize;
  private boolean decoChoice;
  private boolean showPreview;
  private int titleSize;
  private String pageFormat;

  /**
   * calls {@link ReportConfiguration#ReportConfiguration(String, ReportType, String)}
   */
  public ReportConfiguration(String reportName, ReportType type, String path) {
    this(reportName, EnumSet.of(type), path);
  }

  /**
   * Creates a default configuration with the values
   * <ul>
   * <li>decorate control structure is <b>true</b>
   * <li>show preview after export is <b>true</b>
   * <li>Table Header Size is <b>10</b>
   * <li>Text Size is <b>8</b>
   * <li>Title Size is <b>12</b>
   * </ul>
   * 
   * @param reportName
   * @param typeSet
   */
  public ReportConfiguration(String reportName, EnumSet<ReportType> typeSet, String path) {
    this.reportName = reportName;
    this.typeSet = typeSet;
    this.path = path;
    setDecorate(true);
    setShowPreview(true);
    setPageFormat(AbstractExportPage.A4_PORTRAIT);
    setTableHeadSize(10);
    setTextSize(8);
    setTitleSize(12);
  }

  public String getReportName() {
    return reportName;
  }

  public boolean exports(ReportType type) {
    if (typeSet.contains(ReportType.FINAL)) {
      return true;
    }
    return typeSet.contains(type);
  }

  public String getPath() {
    return path;
  }

  public void setTextSize(int contentSize) {
    this.contentSize = contentSize;
  }

  public int getContentSize() {
    return contentSize;
  }

  public int getHeadSize() {
    return headSize;
  }

  public void setTableHeadSize(int headSize) {
    this.headSize = headSize;
  }

  public void setDecorate(boolean decoChoice) {
    this.decoChoice = decoChoice;
  }

  public boolean getDecoChoice() {
    return decoChoice;
  }

  public void setTitleSize(int titleSize) {
    this.titleSize = titleSize;
  }

  public int getTitleSize() {
    return titleSize;
  }

  public boolean isShowPreview() {
    return showPreview;
  }

  public void setShowPreview(boolean showPreview) {
    this.showPreview = showPreview;
  }

  /**
   * Sets the either the {@link AbstractExportPage#A4_LANDSCAPE} or
   * {@link AbstractExportPage#A4_PORTRAIT} format for the document
   * 
   * @param pageFormat
   *          should be one of {@link AbstractExportPage#A4_LANDSCAPE} or
   *          {@link AbstractExportPage#A4_PORTRAIT}
   */
  public void setPageFormat(String pageFormat) {
    this.pageFormat = pageFormat;
  }

  public String getPageFormat() {
    return pageFormat;
  }
}
