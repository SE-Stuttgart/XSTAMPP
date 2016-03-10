package xstampp.astpa.util.jobs;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;

import org.apache.poi.wp.usermodel.CharacterRun;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFRun.FontCharRange;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.FontData;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import xstampp.astpa.model.DataModelController;
import xstampp.util.XstamppJob;

public class STPAWordJob extends XstamppJob {

	private String path;
	private DataModelController controller;
	private int textSize,titleSize,tableHeadSize;
	private String title;
	private boolean enablePreview;

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
//			
//				while(controller.getExportInfo() == null){
//					if(monitor.isCanceled()){
//						return Status.CANCEL_STATUS;
//					}
//				}
			//Blank Document
			XWPFDocument document= new XWPFDocument(); 
			//Write the Document in file system
			FileOutputStream out;
			out = new FileOutputStream(new File(path));
			//create Paragraph
			XWPFParagraph paragraph = document.createParagraph();
			
			XWPFRun run=paragraph.createRun();
			run.setFontSize(titleSize);
			run.setText(title);
			run.addCarriageReturn();
			run.addCarriageReturn();
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

}
