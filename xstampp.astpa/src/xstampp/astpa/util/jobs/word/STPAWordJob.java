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
package xstampp.astpa.util.jobs.word;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFHeaderFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.FontData;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import messages.Messages;
import xstampp.Activator;
import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.model.ATableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.astpa.model.causalfactor.CausalCSComponent;
import xstampp.astpa.model.causalfactor.CausalFactor;
import xstampp.astpa.model.causalfactor.CausalFactorController;
import xstampp.astpa.model.causalfactor.CausalFactorEntry;
import xstampp.astpa.model.causalfactor.CausalHazardEntry;
import xstampp.astpa.model.causalfactor.CausalScenarioEntry;
import xstampp.astpa.model.controlaction.UnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.IControlAction;
import xstampp.astpa.model.controlaction.interfaces.IUnsafeControlAction;
import xstampp.astpa.model.controlaction.interfaces.UnsafeControlActionType;
import xstampp.astpa.model.controlaction.safetyconstraint.ICorrespondingUnsafeControlAction;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.astpa.util.jobs.CSExportJob;
import xstampp.astpa.util.jobs.word.ReportConfiguration.ReportType;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.AbstractExportPage;
import xstampp.util.XstamppJob;

/**
 * This Job creates a word document in the docx format using the <code>Apache Poi</code> Framework
 *
 * @author Lukas Balzer
 * @since 2.0.2
 * @see org.apache.poi.xwpf.usermodel
 * @see org.openxmlformats.schemas
 */
public class STPAWordJob extends XstamppJob {

  private BigInteger shortEdge = BigInteger.valueOf(11900);
  private BigInteger longEdge = BigInteger.valueOf(16840);
  private int contentWidth;
  private DataModelController controller;
  private String backgoundColorStr;
  private String textColorStr;
  private float csRatio = 1;
  private float csPmRatio = 1;
  private CTPageSz pageSize;
  private ReportConfiguration config;

  public STPAWordJob(ReportConfiguration config, DataModelController controller, boolean preview) {
    super(config.getReportName());
    this.config = config;
    this.controller = controller;
  }

  @Override
  protected Observable getModelObserver() {
    return controller;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {

    try {
      while (controller.getExportInfo() == null) {
        if (monitor.isCanceled()) {
          return Status.CANCEL_STATUS;
        }
      }
      backgoundColorStr = "ffffff";
      if (controller.getExportInfo().getBackgroundColor() != null) {
        backgoundColorStr = controller.getExportInfo().getBackgroundColor().replace("#", "");
      }
      textColorStr = "000000";
      if (controller.getExportInfo().getBackgroundColor() != null) {
        textColorStr = controller.getExportInfo().getFontColor().replace("#", "");
      }
      UUID projectID = ProjectManager.getContainerInstance().getProjectID(controller);

      // prepare the control structure images as Byte streams rather than as image files so there
      // are no temporary files stored
      ByteArrayOutputStream controlStructureStream = new ByteArrayOutputStream();
      CSExportJob csExport = new CSExportJob(controlStructureStream, 10, CSEditor.ID, projectID,
          config.getDecoChoice());
      csExport.getPrintableRoot();
      byte[] normalCSArray = controlStructureStream.toByteArray();
      controlStructureStream.close();
      csRatio = csExport.getRatio();

      controlStructureStream = new ByteArrayOutputStream();
      csExport = new CSExportJob(controlStructureStream, 10, CSEditorWithPM.ID, projectID,
          config.getDecoChoice());
      csExport.getPrintableRoot();
      byte[] pmCSArray = controlStructureStream.toByteArray();
      csPmRatio = csExport.getRatio();
      controlStructureStream.close();
      // Blank Document
      XWPFDocument document = new XWPFDocument();
      CTBody body = document.getDocument().getBody();
      if (!body.isSetSectPr()) {
        body.addNewSectPr();
      }

      CTSectPr section = body.getSectPr();
      if (!section.isSetPgSz()) {
        section.addNewPgSz();
      }
      CTSectPr sect = document.getDocument().getBody().getSectPr();
      sect.addNewTitlePg();
      pageSize = section.getPgSz();

      if (config.getPageFormat().equals(AbstractExportPage.A4_LANDSCAPE)) {
        pageSize.setOrient(STPageOrientation.LANDSCAPE);
        pageSize.setW(longEdge);
        pageSize.setH(shortEdge);
        contentWidth = 5000;
      } else {
        pageSize.setOrient(STPageOrientation.PORTRAIT);
        pageSize.setW(shortEdge);
        pageSize.setH(longEdge);
        contentWidth = 5000;
      }
      // Write the Document in file system
      FileOutputStream out;
      File file = new File(config.getPath());
      if (!file.exists()) {
        file.createNewFile();
      }
      out = new FileOutputStream(file);
      if (config.exports(ReportType.FINAL)) {
        addNewTitle(config.getReportName(), document, false, ParagraphAlignment.CENTER, false);
        addNewTitle(controller.getProjectName(), document, false, ParagraphAlignment.CENTER, true);
        if (controller.getExportInfo().getLogoPath() != null) {
          URL url = new URL(controller.getExportInfo().getLogoPath());
          File logo = new File(url.getFile());
          try (FileInputStream stream = new FileInputStream(url.getFile());) {
            BufferedImage bufferedImage = ImageIO.read(logo);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            run.addPicture(stream, Document.PICTURE_TYPE_PNG, "my pic",
                Units.toEMU(width), Units.toEMU(height));
          } catch (InvalidFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        XWPFTable table = addTable(document, 2, 2);
        setCellText(table.getRow(1).getCell(0), "Date", true);
        setCellText(table.getRow(1).getCell(1), java.time.LocalDateTime.now().toString(), true);
        setCellText(table.getRow(0).getCell(0), "Company", true);
        setCellText(table.getRow(0).getCell(1), controller.getExportInfo().getCompany(), true);

      }

      /*
       * form here on the run function will call all the necessary functions to create the
       * desired output document
       */
      if (config.exports(ReportType.SYSTEM_DESCRIPTION)) {
        addDescription(document);
      }

      if (config.exports(ReportType.FINAL)) {
        addTableModel(controller.getAllSystemGoals(), Messages.SystemGoals, document, false, false);
      }
      if (config.exports(ReportType.ACCIDENTS)) {
        addTableModel(controller.getAllAccidents(), Messages.Accidents, document, true, true);
      }
      if (config.exports(ReportType.HAZARDS)) {
        addTableModel(controller.getAllHazards(), Messages.Hazards, document, true, true);
      }
      if (config.exports(ReportType.SAFETY_CONSTRAINTS)) {
        addTableModel(controller.getAllSafetyConstraints(), Messages.SafetyConstraints, document, false, true);
      }
      if (config.exports(ReportType.DESIGN_REQUIREMENTS)) {
        addTableModel(controller.getAllDesignRequirements(), Messages.DesignRequirements, document, false, true);
      }
      if (config.exports(ReportType.FINAL)) {
        addPicture(document, Messages.ControlStructure, normalCSArray, csRatio);
      }

      if (config.exports(ReportType.CONTROL_ACTIONS)) {
        addTableModel(controller.getAllControlActions(), Messages.ControlActions, document, false, false);
      }
      if (config.exports(ReportType.UCA_TABLE)) {
        addUCATable(document, controller.getAllControlActionsU());
      }
      if (config.exports(ReportType.SAFETY_CONSTRAINTS)) {
        addCSCModel(document);
      }
      if (config.exports(ReportType.DESIGN_REQUIREMENTS)) {
        addTableModel(controller.getSdsController().getAllDesignRequirements(ObserverValue.DESIGN_REQUIREMENT_STEP1),
            Messages.DesignRequirements + " of Step 1", document, false, true);
      }
      if (config.exports(ReportType.FINAL)) {
        addPicture(document, Messages.ControlStructureDiagramWithProcessModel, pmCSArray, csPmRatio);
      }
      if (config.exports(ReportType.CAUSAL_FACTORS)) {
        addCausalFactorTable(document);
      }

      if (config.exports(ReportType.SAFETY_CONSTRAINTS)) {
        addTableModel(controller.getCausalFactorController().getSafetyConstraints(),
            Messages.CausalSafetyConstraints, document, false, true);
      }
      if (config.exports(ReportType.DESIGN_REQUIREMENTS)) {
        addTableModel(controller.getSdsController().getAllDesignRequirements(ObserverValue.DESIGN_REQUIREMENT_STEP2),
            Messages.DesignRequirements + " of Step 4",
            document, false, true);
      }

      if (config.exports(ReportType.FINAL)) {
        addGlossary(document);
      }
      addHeaderFooter(document);
      document.write(out);
      out.close();
      /*
       ****************************************************************************************************************
       */
      File docFile = file;
      if (docFile.exists() && config.isShowPreview()) {
        if (Desktop.isDesktopSupported()) {
          Desktop.getDesktop().open(docFile);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return Status.CANCEL_STATUS;
    } catch (IOException e) {
      e.printStackTrace();
      return Status.CANCEL_STATUS;
    }

    return Status.OK_STATUS;
  }

  private void addHeaderFooter(XWPFDocument docx) {

    CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
    XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(docx, sectPr);
    // write footer content
    XWPFFooter footer = policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
    XWPFTable footerTable = addTable(footer, 1, 3);
    String rgbStr = "eeeeee";
    footerTable.getRow(0).getCell(0).setColor(rgbStr);
    footerTable.getRow(0).getCell(0).getCTTc().addNewTcPr().addNewTcW()
        .setW(BigInteger.valueOf(contentWidth / 3));
    footerTable.getRow(0).getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(contentWidth / 3));
    footerTable.getRow(0).getCell(2).getCTTc().addNewTcPr().addNewTcW()
        .setW(BigInteger.valueOf(contentWidth / 3));
    XWPFParagraph paragraph = footerTable.getRow(0).getCell(0).getParagraphArray(0);
    paragraph.setAlignment(ParagraphAlignment.LEFT);
    paragraph.setVerticalAlignment(TextAlignment.CENTER);
    XWPFRun run = paragraph.createRun();
    run.setText("Created with XSTAMPP " + Activator.getContext().getBundle().getVersion());

    footerTable.getRow(0).getCell(1).setColor(rgbStr);
    paragraph = footerTable.getRow(0).getCell(1).getParagraphArray(0);
    paragraph.setAlignment(ParagraphAlignment.CENTER);
    paragraph.setVerticalAlignment(TextAlignment.CENTER);
    paragraph.createRun().setText("Page ");
    paragraph.getCTP().addNewFldSimple().setInstr("PAGE \\* MERGEFORMAT");
    paragraph.createRun().setText(" of ");
    paragraph.getCTP().addNewFldSimple().setInstr("NUMPAGES \\* MERGEFORMAT");
    footerTable.getRow(0).getCell(2).setColor(rgbStr);

    policy.createFooter(XWPFHeaderFooterPolicy.FIRST);

    // write footer content
    XWPFHeader header = policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
    XWPFTable headerTable = addTable(header, 1, 3);
    headerTable.getRow(0).getCell(0).getCTTc().addNewTcPr().addNewTcW()
        .setW(BigInteger.valueOf(contentWidth / 3));
    headerTable.getRow(0).getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(contentWidth / 3));
    headerTable.getRow(0).getCell(2).getCTTc().addNewTcPr().addNewTcW()
        .setW(BigInteger.valueOf(contentWidth / 3));
    headerTable.getRow(0).getCell(0).setColor(rgbStr);
    paragraph = headerTable.getRow(0).getCell(0).getParagraphArray(0);
    paragraph.setAlignment(ParagraphAlignment.LEFT);
    paragraph.setVerticalAlignment(TextAlignment.CENTER);
    paragraph.createRun().setText(config.getReportName());

    headerTable.getRow(0).getCell(1).setColor(rgbStr);
    paragraph = headerTable.getRow(0).getCell(1).getParagraphArray(0);
    paragraph.setAlignment(ParagraphAlignment.CENTER);
    paragraph.setVerticalAlignment(TextAlignment.CENTER);
    paragraph.createRun().setText(controller.getProjectName());
    headerTable.getRow(0).getCell(2).setColor(rgbStr);
    paragraph = headerTable.getRow(0).getCell(2).getParagraphArray(0);
    paragraph.setAlignment(ParagraphAlignment.RIGHT);
    paragraph.setVerticalAlignment(TextAlignment.CENTER);
    paragraph.createRun().setText(java.time.LocalDateTime.now().toString());

    policy.createHeader(XWPFHeaderFooterPolicy.FIRST);
  }

  private void addNewTitle(String text, XWPFDocument document, boolean pageBreak) {
    addNewTitle(text, document, pageBreak, ParagraphAlignment.LEFT, false);
  }

  private void addNewTitle(String text, XWPFDocument document, boolean pageBreak, ParagraphAlignment align,
      boolean colored) {
    XWPFParagraph paragraph = document.createParagraph();
    paragraph.setAlignment(align);
    XWPFRun run = paragraph.createRun();
    if (colored) {
      CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
      cTShd.setVal(STShd.CLEAR);
      cTShd.setColor("auto");
      cTShd.setFill(backgoundColorStr);
    }
    if (pageBreak) {
      run.addBreak(BreakType.PAGE);
    }
    run.setFontSize(config.getTitleSize());
    if (colored) {
      run.setColor(textColorStr);
    }
    run.addCarriageReturn();
    run.setText(text);
    run.addCarriageReturn();
  }

  private void addPicture(XWPFDocument document, String controlStructure, byte[] array,
      float ratio) {

    try {
      addNewTitle(controlStructure, document, true);
      ByteArrayInputStream pic = new ByteArrayInputStream(array);
      float width;
      float height;
      if (config.getPageFormat().equals(AbstractExportPage.A4_LANDSCAPE)) {
        height = 400;
        while (700f - height * ratio < 0) {
          height--;
        }
        width = height * ratio;
      } else {
        width = 450;
        while (700f - width / ratio < 0) {
          width--;
        }
        height = width / ratio;
      }
      document.createParagraph().createRun().addPicture(pic, Document.PICTURE_TYPE_PNG, "my pic",
          Units.toEMU(width), Units.toEMU(height));
    } catch (InvalidFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This internal method adds the Table of unsafe Control Actions
   * 
   * @param paragraph
   */
  private void addUCATable(XWPFDocument document, List<IControlAction> list) {
    addNewTitle(Messages.UnsafeControlActionsTable, document, true);
    List<String> heads = Arrays.asList(
        Messages.ControlAction, Messages.NotGiven,
        Messages.GivenIncorrectly, Messages.WrongTiming,
        Messages.StoppedTooSoon);
    XWPFTable ucaTable = addTable(document, heads);
    XWPFTableCell caCell;
    for (IControlAction cAction : list) {
      XWPFTableRow row = ucaTable.createRow();
      caCell = row.getCell(0);
      setCellText(caCell, cAction.getTitle(), true);
      // The first merged cell is set with RESTART merge value
      startMergeRegion(caCell);

      // all related unsafe control actions are fetched and stored in multiple list one for each
      // column
      List<IUnsafeControlAction> allNotGiven = cAction
          .getUnsafeControlActions(UnsafeControlActionType.NOT_GIVEN);
      List<IUnsafeControlAction> allIncorrect = cAction
          .getUnsafeControlActions(UnsafeControlActionType.GIVEN_INCORRECTLY);
      List<IUnsafeControlAction> allWrongTiming = cAction
          .getUnsafeControlActions(UnsafeControlActionType.WRONG_TIMING);
      List<IUnsafeControlAction> allTooSoon = cAction
          .getUnsafeControlActions(UnsafeControlActionType.STOPPED_TOO_SOON);
      int maxNr = Math.max(allNotGiven.size(), allIncorrect.size());
      maxNr = Math.max(maxNr, allTooSoon.size());
      maxNr = Math.max(maxNr, allWrongTiming.size());
      // This loop runs form 0 to the size of the largest ucaList
      for (int i = 0; i < maxNr; i++) {
        row = row == null ? ucaTable.createRow() : row;
        row.setCantSplitRow(true);

        // Cells which join (merge) the first one, are set with CONTINUE
        addToMergeRegion(row.getCell(0));
        addUCAEntry(row, allNotGiven, i, 1, document);
        addUCAEntry(row, allIncorrect, i, 2, document);
        addUCAEntry(row, allWrongTiming, i, 3, document);
        addUCAEntry(row, allTooSoon, i, 4, document);
        row = null;
      }
    }
  }

  private void startMergeRegion(XWPFTableCell caCell) {
    caCell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
  }

  private void addToMergeRegion(XWPFTableCell caCell) {
    caCell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
  }

  private void addUCAEntry(XWPFTableRow ucaRow,
      List<IUnsafeControlAction> list, int index, int colNr, XWPFDocument document) {
    setCellColor(ucaRow.getCell(colNr), index);
    XWPFParagraph paragraph = ucaRow.getCell(colNr).addParagraph();
    XWPFTable ucaTable = ucaRow.getCell(colNr).insertNewTbl(paragraph.getCTP().newCursor());
    XWPFTableRow idRow = ucaTable.createRow();
    XWPFTableRow descRow = ucaTable.createRow();
    XWPFTableRow linkRow = ucaTable.createRow();
    if (index < list.size()) {
      String identifier = String.valueOf(((UnsafeControlAction) list.get(index)).getNumber());
      if (identifier != null && !identifier.isEmpty()) {
        identifier = "UCA1." + identifier;
      }
      addCell(idRow.createCell(), index, identifier, ParagraphAlignment.CENTER, true);
      addCell(descRow.createCell(), index,
          ((UnsafeControlAction) list.get(index)).getDescription(), true);
      addCell(linkRow.createCell(), index, ((UnsafeControlAction) list.get(index)).getLinks(), true);
    } else {
      addCell(idRow.createCell(), index, "", true);
      addCell(descRow.createCell(), index, "", true);
      addCell(linkRow.createCell(), index, "", true);
    }
  }

  private void addCausalFactorTable(XWPFDocument document) {
    addNewTitle(Messages.CausalFactorsTable, document, true);

    List<String> heads;
    if (controller.isUseScenarios()) {
      heads = Arrays.asList(Messages.Component,
          Messages.CausalFactors, "Unsafe Control Action", Messages.HazardLinks, "Causal Scenarios",
          Messages.SafetyConstraint, "Links", Messages.NotesSlashRationale);
    } else {
      heads = Arrays.asList(Messages.Component,
          Messages.CausalFactors, "Unsafe Control Action", Messages.HazardLinks,
          Messages.SafetyConstraint, "Design Hint", "Links", Messages.NotesSlashRationale);
    }
    XWPFTable table = addTable(document, heads);
    for (CausalCSComponent component : ((CausalFactorController) controller.getCausalFactorController())
        .getCausalComponents()) {
      createCausalComponent(table, component);
    }
  }

  private void createCausalComponent(XWPFTable table, Object component) {
    XWPFTableRow row = table.createRow();
    row.setCantSplitRow(true);
    XWPFTableCell componentCell = row.getCell(0);
    startMergeRegion(componentCell);
    setCellText(componentCell, ((CausalCSComponent) component).getText(), true);
    boolean isEven = false;
    for (CausalFactor factor : ((CausalCSComponent) component).getFactors()) {
      row = row == null ? createColoredRow(table, isEven, Arrays.asList(0)) : row;
      setCellText(row.getCell(1), factor.getText(), true);
      startMergeRegion(row.getCell(1));
      for (CausalFactorEntry entry : factor.getEntries()) {
        row = row == null ? table.createRow() : row;
        setCellText(row.getCell(2), entry.getUcaDescription(), true);
        addToMergeRegion(row.getCell(1));
        startMergeRegion(row.getCell(2));
        if (controller.isUseScenarios() && entry.getScenarioEntries() != null) {
          setCellText(row.getCell(3), entry.getHazardLinks(), true);
          createScenarioRows(table, row, entry.getScenarioEntries(), isEven);
        } else if (entry.getHazardEntries() != null) {
          createHazardRows(table, row, entry.getHazardEntries(), isEven);
        }
        row = null;
        isEven = !isEven;
      }
    }
  }

  private void createHazardRows(XWPFTable table, XWPFTableRow row, List<CausalHazardEntry> hazardEntries,
      boolean isEven) {
    for (CausalHazardEntry hazEntry : hazardEntries) {
      row = row == null ? createColoredRow(table, isEven, Arrays.asList(0, 1, 2)) : row;
      setCellText(row.getCell(3), hazEntry.getText(), true);
      setCellText(row.getCell(4), hazEntry.getConstraint(), true);
      setCellText(row.getCell(5), hazEntry.getDesignHint(), true);
      setCellText(row.getCell(6), hazEntry.getLinks(), true);
      setCellText(row.getCell(7), hazEntry.getNote(), true);
      row = null;
    }
  }

  private void createScenarioRows(XWPFTable table, XWPFTableRow row, List<CausalScenarioEntry> list,
      boolean isEven) {
    for (CausalScenarioEntry scenarioEntry : list) {
      row = row == null ? createColoredRow(table, isEven, Arrays.asList(0, 2)) : row;
      setCellText(row.getCell(4), scenarioEntry.getDescription(), true);
      setCellText(row.getCell(5), scenarioEntry.getConstraint(), true);
      setCellText(row.getCell(7), scenarioEntry.getNote(), true);
    }
  }

  private XWPFTableRow createColoredRow(XWPFTable table, boolean isEven, List<Integer> mergeCells) {
    XWPFTableRow row = table.createRow();
    row.setCantSplitRow(true);
    mergeCells.stream().forEach(cellNr -> addToMergeRegion(row.getCell(cellNr)));
    row.getTableCells().stream().forEach(cell -> setCellColor(cell, isEven));
    return row;
  }

  private void setCellColor(XWPFTableCell cell, int index) {
    setCellColor(cell, index % 2 == 0);
  }

  private void setCellColor(XWPFTableCell cell, boolean even) {
    String color;
    if (even) {
      color = "dddddd";
    } else {
      color = "ededed";
    }
    cell.setColor(color);
  }

  private void addCell(XWPFTableCell cell, int index, String text, boolean wrap) {
    addCell(cell, index, text, ParagraphAlignment.LEFT, wrap);
  }

  private void addCell(XWPFTableCell cell, int index, String text, ParagraphAlignment alignment, boolean wrap) {
    XWPFParagraph paragraph = cell.addParagraph();
    paragraph.setAlignment(alignment);

    XWPFRun run = paragraph.createRun();
    setCellColor(cell, index);
    run.setFontSize(config.getContentSize());
    run.setText(text != null ? text.replace('-', '.') : "");
  }

  private void setCellText(XWPFTableCell cell, String text, boolean wrap) {
    addCell(cell, 0, text, wrap);
  }

  /**
   * This internal method adds and formats the system description as defined in the
   * system description editor
   * 
   * @param paragraph
   */
  private void addDescription(XWPFDocument document) {

    XWPFParagraph paragraph = document.createParagraph();
    XWPFRun run = paragraph.createRun();
    char[] description = controller.getProjectDescription().toCharArray();
    StyleRange[] rangeArray = controller.getStyleRangesAsArray();
    int rangeNr = 0;
    StyleRange defaultStyle = new StyleRange();

    StyleRange range = defaultStyle;
    boolean styleChanged = true;
    for (int index = 0; index < description.length; index++) {
      /*
       * if the array of styleranges does contain a next style range
       * which starts at the current index than this range is set active
       */
      if (rangeArray.length > rangeNr && rangeArray[rangeNr].start == index) {
        range = rangeArray[rangeNr];
        styleChanged = true;
        rangeNr++;
      }
      /*
       * if no starting range has been found but the currently active range has reached its
       * extent than the active range is set to default and the styleChanged boolean is set
       */
      else if (range != null && range.start + range.length <= index) {
        range = defaultStyle;
        styleChanged = true;
      }
      // if the style has changed than a new XWPFRun object is created containing the new style
      if (styleChanged) {
        run = paragraph.createRun();

        if (range.underline) {
          run.setUnderline(UnderlinePatterns.SINGLE);
        } else {
          run.setUnderline(UnderlinePatterns.NONE);
        }
        if ((range.fontStyle & SWT.BOLD) != 0) {
          run.setBold(true);
        }
        if ((range.fontStyle & SWT.ITALIC) != 0) {
          run.setItalic(true);
        }
        try {

          FontData data = range.font.getFontData()[0];
          run.setFontSize(data.getHeight());
          run.setFontFamily(data.getName());
          String red, green, blue, hexColor;
          if (range.background != null) {
            red = Integer.toHexString(range.background.getRed());
            if (red.length() == 1) {
              red = "0" + red;
            }
            green = Integer.toHexString(range.background.getGreen());
            if (green.length() == 1) {
              green = "0" + green;
            }
            blue = Integer.toHexString(range.background.getBlue());
            if (blue.length() == 1) {
              blue = "0" + blue;
            }

            hexColor = red + green + blue;

            if (hexColor.length() == 6) {
              // The background color is added using these mysterious classes which address the
              // character shading
              CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
              cTShd.setVal(STShd.CLEAR);
              cTShd.setColor("auto");
              cTShd.setFill(hexColor);
            }

          }
          if (range.foreground != null) {
            red = Integer.toHexString(range.foreground.getRed());
            if (red.length() == 1) {
              red = "0" + red;
            }
            green = Integer.toHexString(range.foreground.getGreen());
            if (green.length() == 1) {
              green = "0" + green;
            }
            blue = Integer.toHexString(range.foreground.getBlue());
            if (blue.length() == 1) {
              blue = "0" + blue;
            }

            hexColor = red + green + blue;

            if (hexColor.length() == 6) {
              run.setColor(hexColor);
            }
          }
        } catch (NullPointerException e) {
        }
        styleChanged = false;
      }

      if (description[index] == '\n') {
        run.addCarriageReturn();
      } else {
        run.setText(String.valueOf(description[index]));
      }

    }
  }

  private void addTableModel(List<? extends ITableModel> models, String text, XWPFDocument document,
      boolean hasSeverity, boolean hasLinks) {

    List<String> heads = new ArrayList<>();
    heads.add("ID");
    heads.add("Name");
    heads.add("Description");
    if (hasSeverity) {
      heads.add("Severity");
    }
    if (hasLinks) {
      heads.add("Links");
    }
    addNewTitle(text, document, true);
    XWPFTable table = addTable(document, heads);

    XWPFTableRow tableRowTwo;
    for (ITableModel data : models) {
      tableRowTwo = table.createRow();
      XWPFTableCell cell = tableRowTwo.getCell(0);
      setCellText(cell, data.getIdString(), false);
      cell = tableRowTwo.getCell(1);
      setCellText(cell, data.getTitle(), true);
      cell = tableRowTwo.getCell(2);
      setCellText(cell, data.getDescription(), true);
      cell = tableRowTwo.getCell(3);
      if (hasSeverity) {
        setCellText(cell, ((ATableModel) data).getSeverity().name(), true);
        cell = tableRowTwo.getCell(4);
      }
      if (hasLinks) {
        setCellText(cell, ((ATableModel) data).getLinks(), true);
      }
    }
  }

  private void addCSCModel(XWPFDocument document) {

    List<String> heads = Arrays.asList("ID", "UnsafeControlActions", "ID", "Corresponding Safety Constraints", "Links");
    addNewTitle("Corresponding Safety Constraints", document, true);
    XWPFTable table = addTable(document, heads);

    XWPFTableRow tableRowTwo;
    for (ICorrespondingUnsafeControlAction data : controller.getAllUnsafeControlActions()) {
      if (controller.getLinkController().isLinked(LinkingType.UCA_HAZ_LINK, data.getId())) {
        tableRowTwo = table.createRow();
        setCellText(tableRowTwo.getCell(0), data.getIdString(), true);
        setCellText(tableRowTwo.getCell(1), data.getDescription(), true);
        if (data.getCorrespondingSafetyConstraint() != null) {
          setCellText(tableRowTwo.getCell(2), data.getCorrespondingSafetyConstraint().getIdString(), true);
          setCellText(tableRowTwo.getCell(3), data.getCorrespondingSafetyConstraint().getDescription(), true);
          setCellText(tableRowTwo.getCell(4), ((ATableModel) data.getCorrespondingSafetyConstraint()).getLinks(), true);
        }
      }

    }
  }

  private void addGlossary(XWPFDocument document) {
    Map<String, String> glossary = new HashMap<>();
    glossary.put("ACCIDENT",
        "undesired or unplanned event that results in a loss, including loss or injury to human life, property damage, environmental pollution, mission loss etc.");
    glossary.put("ACTUATOR",
        "a human operator or mechanical device tasked with directly acting upon a process and changing its physical state. Valve systems (valve + the motor associated to it), doors,"
            + " magnets (their electronic controller and power source included) or a nurse are actuators that"
            + " respectively implement control on the following processes: \"fluid flow\", \"egress availability\","
            + " \"beam position\", \"patient position\". Actuators, like sensors, can be smart in that they can be programmable;"
            + " they may therefore need to be studied with the same concepts as the controllers are.");
    glossary.put("CAUSAL FACTOR", "cause of a (hazardous) scenario (STPA Step 2).");
    glossary.put("COMMAND", "a signal providing a set of instructions (goals, set points, order) issued by a"
        + " controller with the intent of acting upon a process by activation of a device or"
        + " implementation of"
        + " a procedure. Communication and Control, along with Hierarchy and Emergence, are"
        + " fundamental systems theory concepts at the foundation of STAMP. Commands are issued by"
        + " Controllers, with the intent that they be implemented by Actuators to act on the Controlled"
        + " Process");
    glossary.put("CONTROL ACTION", "the bringing about of an alteration in the system's state through activation"
        + " of a device or implementation of a procedure with the intent of regulating or guiding the"
        + " operation of a human being, machine, apparatus, or system. They are the result of an Actuator"
        + " implementing a control Command issued by a Controller, and aim at controlling the state of"
        + " the Controlled Process");
    glossary.put("CONTROL STRUCTURE", "hierarchy of process loops created to steer a system's operations and"
        + " control its states. In the context of a hazard analysis, we are most concerned with the"
        + " control of"
        + " hazardous states aimed at eliminating, reducing or mitigating them.");
    glossary.put("CONTROLLED PROCESS", "although at times reducible to the state of a physical element (e.g."
        + " framing a \"door\" as a controlled process whose values can be \"open\" or \"shut\"), it appears"
        + " fruitful to rather consider the controlled process identified in STAMP process loops to be"
        + " the"
        + " system's attribute or state variable that the controller aims to control (e.g. thinking of"
        + " the door"
        + " not as the controlled process but, together with its motor, as an actuator that implements"
        + " control"
        + " on the possibility of egress).");
    glossary.put("CONTROLLER", "a human or automated system that is responsible for controlling the system's"
        + " processes by issuing commands to be implemented by system actuators."
        + " FEEDBACK"
        + " evaluative or corrective information about an action, event, or process that is"
        + " transmitted to the original or controlling source.");
    glossary.put("HAZARD", "system state of set or conditions that, together with a particular set of worst-case"
        + " environmental conditions, will lead to an accident.");
    glossary.put("LOSS", "decrease in amount, magnitude or degree including destruction or ruin.");
    glossary.put("SAFETY", "freedom from loss.");
    glossary.put("SAFETY CONSTRAINT", "bound set on system design options and operations to restrict, compel"
        + " to avoid or forbid the performance of actions that would lead to a hazard.");
    glossary.put("SAFETY/DESIGN REQUIREMENT", "design requirement formulated to include the enforcement of safety"
        + " constraints as a design objective.");
    glossary.put("(HAZARDOUS) SCENARIO", "an account or synopsis of a possible course of action or events"
        + " resulting in a hazard. See Causal Factor.");
    glossary.put("SENSOR", "human or mechanical device tasked with measuring a process variable by responding"
        + " to a physical stimulus (as heat, light, sound, pressure, magnetism, or a particular motion)"
        + " and"
        + " transmit a resulting impulse (as for measurement or operating a control).");
    glossary.put("UNSAFE CONTROL ACTION", "control action that leads to a hazard (STPA Step 1).");

    addNewTitle("Glossary", document, true);
    XWPFTable table = document.createTable(1, 2);
    XWPFTableRow row = table.getRow(0);
    for (Entry<String, String> entry : glossary.entrySet()) {

      row.setCantSplitRow(true);
      row.getCell(0).setText(entry.getKey());
      row.getCell(1).setText(entry.getValue());
      row = table.createRow();
    }

    row.setCantSplitRow(true);
    row.getCell(0).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
    row.getCell(0).setText(
        "Definitions from: Antoine, B. (2013). Systems Theoretic Hazard Analysis (STPA) applied to the risk review of"
            + " complex systems: an example from the medical device industry (Doctoral dissertation,"
            + " Massachusetts Institute of Technology).");
    row.getCell(1).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
  }

  private XWPFTable addTable(XWPFTable table) {
    CTTblPr pr = table.getCTTbl().getTblPr();
    CTTblWidth tblW = pr.getTblW();
    tblW.setW(BigInteger.valueOf(contentWidth));
    tblW.setType(STTblWidth.PCT);
    pr.setTblW(tblW);
    table.getCTTbl().setTblPr(pr);
    return table;
  }

  private XWPFTable addTable(XWPFDocument document, List<String> heads) {
    XWPFTable table = addTable(document, 1, heads.size());
    XWPFTableRow headRow = table.getRow(0);
    XWPFRun run;
    XWPFParagraph paragraph;
    for (int i = 0; i < heads.size(); i++) {
      headRow.getCell(i).setColor(backgoundColorStr);
      paragraph = headRow.getCell(i).addParagraph();
      run = paragraph.createRun();
      run.setColor(textColorStr);
      run.setBold(true);
      run.setFontSize(config.getHeadSize());
      run.setText(heads.get(i));
    }
    return table;
  }

  private XWPFTable addTable(XWPFDocument document, int rows, int columns) {
    XWPFTable table = document.createTable(rows, columns);
    return addTable(table);
  }

  private XWPFTable addTable(XWPFHeaderFooter part, int rows, int columns) {
    XWPFTable table = part.createTable(rows, columns);
    return addTable(table);
  }

}
