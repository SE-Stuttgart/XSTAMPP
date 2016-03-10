package xstampp.astpa.util.jobs;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.FontData;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.DataModelController;
import xstampp.ui.common.ProjectManager;
import xstampp.util.XstamppJob;

public class STPAWordJob extends XstamppJob {

	private String path;
	private DataModelController controller;
	private int textSize,titleSize,tableHeadSize;
	private String title;
	private boolean enablePreview;
	private String backgoundColorStr;
	private String textColorStr;
	private boolean decorate;
	private float csRatio = 1;
	private float csPmRatio = 1;

	public STPAWordJob(String name,String path,DataModelController controller,boolean preview) {
		super(name);
		this.path = path;
		this.controller = controller;
		this.enablePreview = preview;
		
	}

	@Override
	protected Observable getModelObserver() {
		return controller;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		try {
			while(controller.getExportInfo() == null){
				if(monitor.isCanceled()){
					return Status.CANCEL_STATUS;
				}
			}
			backgoundColorStr = "ffffff";
			if(controller.getExportInfo().getBackgroundColor() != null){
				backgoundColorStr=controller.getExportInfo().getBackgroundColor().replace("#", "");
			}
			textColorStr = "000000";
			if(controller.getExportInfo().getBackgroundColor() != null){
				textColorStr=controller.getExportInfo().getFontColor().replace("#", "");
			}
			UUID projectID = ProjectManager.getContainerInstance().getProjectID(controller);
			ByteArrayOutputStream controlStructureStream = new ByteArrayOutputStream();
			CSExportJob csExport = new CSExportJob(controlStructureStream, 10,CSEditor.ID, projectID, this.decorate);
			
			csExport.getPrintableRoot();
			byte[] normalCSArray = controlStructureStream.toByteArray();
			controlStructureStream.close();
			csRatio = csExport.getRatio();
			
			controlStructureStream = new ByteArrayOutputStream();
			csExport = new CSExportJob(controlStructureStream, 10,CSEditorWithPM.ID, projectID, this.decorate);
			
			csExport.getPrintableRoot();
			byte[] pmCSArray = controlStructureStream.toByteArray();
			csPmRatio = csExport.getRatio();
			controlStructureStream.close();
			
			//Blank Document
			XWPFDocument document= new XWPFDocument(); 
			//Write the Document in file system
			FileOutputStream out;
			out = new FileOutputStream(new File(path));
			//The first run object draws the Title banner on the Word Doc
			addNewTitle(title, document);
			
			/*
			 * ******************************************************************
			 * form here on the run funktion will call all the neccessary functions to create the 
			 * desired output document
			 */
			addDescription(document);

			addTableModel(controller.getAllSystemGoals(), "",Messages.SystemGoals, document);
			addTableModel(controller.getAllAccidents(), "",Messages.Accidents, document);
			addTableModel(controller.getAllHazards(), "",Messages.Hazards, document);
			addTableModel(controller.getAllDesignRequirements(), "",Messages.DesignRequirements, document);
			addPicture(document, Messages.ControlStructure, normalCSArray,csRatio);
			addPicture(document, Messages.ControlStructureDiagramWithProcessModel, pmCSArray,csPmRatio);
			document.write(out);
			out.close();
			 
			File docFile = new File(path);
			if (docFile.exists() && this.enablePreview) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(docFile);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Status.CANCEL_STATUS;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Status.CANCEL_STATUS;
		}

		return Status.OK_STATUS;
	}

	/**
	 * @param textSize the textSize to set
	 */
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	/**
	 * @param titleSize the titleSize to set
	 */
	public void setTitleSize(int titleSize) {
		this.titleSize = titleSize;
	}

	/**
	 * @param tableHeadSize the tableHeadSize to set
	 */
	public void setTableHeadSize(int tableHeadSize) {
		this.tableHeadSize = tableHeadSize;
	}

	public void setPdfTitle(String title) {
		this.title = title;
		
	}

	private void addNewTitle(String text, XWPFDocument document){
		XWPFParagraph paragraph = document.createParagraph();
		
		XWPFRun run=paragraph.createRun();
		CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
		cTShd.setVal(STShd.CLEAR);
		cTShd.setColor("auto");
		cTShd.setFill(backgoundColorStr);
		
		run.setFontSize(titleSize);
		run.setEmbossed(true);
		run.setColor(textColorStr);
		run.addCarriageReturn();
		run.setText(text);
		run.addCarriageReturn();
		run.addCarriageReturn();
	}
	
	private void addPicture(XWPFDocument document,String controlStructure, byte[] array,float ratio){
		
		try {
			addNewTitle(controlStructure, document);
			ByteArrayInputStream pic = new ByteArrayInputStream(array);
			document.createParagraph().createRun().
						addPicture(pic, Document.PICTURE_TYPE_PNG, "my pic", Units.toEMU(400), Units.toEMU(400/ratio));
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This internal method adds and formats the system description as defined in the 
	 * system description editor
	 * 
	 * @param paragraph 
	 */
	private void addDescription(XWPFDocument document){

		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run=paragraph.createRun();
		
		char[] description = controller.getProjectDescription().toCharArray();
		StyleRange[] rangeArray= controller.getStyleRangesAsArray();
		int rangeNr = 0;
		StyleRange defaultStyle = new StyleRange();

		StyleRange range = defaultStyle;
		boolean styleChanged = true;
		for (int index = 0;index<description.length;index++) {
			/*
			 * if the array of styleranges does contain a next style range
			 * which starts at the current index than this range is set active
			 */
			if(rangeArray.length > rangeNr && rangeArray[rangeNr].start == index){
				range = rangeArray[rangeNr];
				styleChanged = true;
				rangeNr++;
			}
			/*
			 * if no starting range has been found but the currently active range has reached its
			 * extent than the active range is set to default and the styleChanged boolean is set
			 */
			else if(range != null && range.start + range.length <= index){
				range = defaultStyle;
				styleChanged = true;
			}
			//if the style has changed than a new XWPFRun object is created containing the new style
			if(styleChanged){
				run=paragraph.createRun();
				 
				
				
				if(range.underline){
					run.setUnderline(UnderlinePatterns.SINGLE);
				}else{
					run.setUnderline(UnderlinePatterns.NONE);
				}
				if((range.fontStyle & SWT.BOLD) != 0){
					run.setBold(true);
				}
				if((range.fontStyle & SWT.ITALIC) != 0){
					run.setItalic(true);
				}
				try{

					FontData data = range.font.getFontData()[0];
					run.setFontSize(data.getHeight());
					run.setFontFamily(data.getName());
					String red,green,blue,hexColor;
					if(range.background != null){
						red = Integer.toHexString(range.background.getRed());
						if(red.length() == 1){
							red = "0"+red;
						}
						green = Integer.toHexString(range.background.getGreen());
						if(green.length() == 1){
							green = "0"+green;
						}
						blue = Integer.toHexString(range.background.getBlue());
						if(blue.length() == 1){
							blue = "0"+blue;
						}
						
						hexColor= red + green + blue;
						
						if(hexColor.length() == 6){
							//The background color is added using these mysterious classes which address the character shading 
							CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
							cTShd.setVal(STShd.CLEAR);
							cTShd.setColor("auto");
							cTShd.setFill(hexColor);
						}
						
					}
					if(range.foreground != null){
						red = Integer.toHexString(range.foreground.getRed());
						if(red.length() == 1){
							red = "0"+red;
						}
						green = Integer.toHexString(range.foreground.getGreen());
						if(green.length() == 1){
							green = "0"+green;
						}
						blue = Integer.toHexString(range.foreground.getBlue());
						if(blue.length() == 1){
							blue = "0"+blue;
						}
						
						hexColor= red + green + blue;

						if(hexColor.length() == 6){
							run.setColor(hexColor);
						}
					}
				}catch(NullPointerException e){
				}
				styleChanged = false;
			}
			
			if(description[index] == '\n'){
				run.addCarriageReturn();
			}else{
				run.setText(String.valueOf(description[index]));
			}
			
		}
	}
	
	private void addTableModel(List<ITableModel> models,String literals,String text,XWPFDocument document){
		

		addNewTitle(text, document);
		
		XWPFTable table=document.createTable(1,3);
		XWPFTableRow headRow = table.getRow(0);
		
		String[] heads = new String[]{"ID","Name","Description"};
		XWPFRun run;
		XWPFParagraph paragraph;
		for (int i = 0; i < heads.length; i++) {
			headRow.getCell(i).setColor(backgoundColorStr);
			paragraph = headRow.getCell(i).addParagraph();
			run = paragraph.createRun();
			run.setColor(textColorStr);
			run.setBold(true);
			run.setText(heads[i]);
		}
		
		XWPFTableRow tableRowTwo;
		int i = 0;
		for (ITableModel data : models) {
			tableRowTwo = table.createRow();
			tableRowTwo.getCell(0).setText(literals + Integer.toString(i));
			tableRowTwo.getCell(1).setText(data.getTitle());
			tableRowTwo.getCell(2).setText(data.getDescription());
			i++;
			
		}
	}

	/**
	 * @return the decorate
	 */
	public boolean isDecorate() {
		return this.decorate;
	}

	/**
	 * @param decorate the decorate to set
	 */
	public void setDecorate(boolean decorate) {
		this.decorate = decorate;
	}
	
	
}
