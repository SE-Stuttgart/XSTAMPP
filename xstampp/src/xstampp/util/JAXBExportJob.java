package xstampp.util;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.area.AreaTreeModel;
import org.apache.fop.area.AreaTreeParser;
import org.apache.fop.area.Span;
import org.apache.fop.fo.FOTreeBuilder;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.xml.sax.SAXException;

import xstampp.ui.common.ProjectManager;
import xstampp.ui.wizards.AbstractExportPage;

/**
 * Eclipse job that handles the export
 * 
 * @author Fabian Toth,Lukas Balzer
 * @since 2.0
 * 
 */
public abstract class JAXBExportJob extends XstamppJob implements IJobChangeListener {

	private boolean enablePreview = true;
	private static final float MP_TO_INCH = 72270f;
	private static final Logger LOGGER = Logger.getRootLogger();
	private String filePath;
	private ByteArrayOutputStream outStream;
	private final String fileType;
	private final String xslName;
	private float textSize,titleSize,tableHeadSize;
	private String pageFormat = AbstractExportPage.A4_PORTRAIT;
	private String  pdfTitle="";
	/**
	 * the xslfoTransormer is beeing related to the xsl which describes the
	 * pdf export
	 */
	private Transformer xslfoTransformer;

	/**
	 * Constructor of the export job
	 * 
	 * @author Fabian Toth, Lukas Balzer
	 * @param projectId
	 *            the project which
	 * @param name
	 *            the name of the job
	 * @param filePath
	 *            the path to the pdf file
	 * @param xslName
	 *            the name of the file in which the xsl file is stored which
	 *            should be used
	 * @param asOne
	 *            true if all content shall be exported on a single page
	 * @param decorate
	 *            if the control structure components should be pictured with
	 *            colored borders and image labels
	 */
	public JAXBExportJob(String name, String filePath, String xslName) {
		super(name);
		this.filePath = filePath;
		this.fileType = ProjectManager.getContainerInstance().getMimeConstant(filePath);
		this.xslName = xslName;
		this.tableHeadSize = 14;
		this.titleSize = 24;
		this.textSize = 12;
		addJobChangeListener(this);
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
//		monitor.worked(1);
		// put the xml jaxb content into an output stream
		this.outStream = new ByteArrayOutputStream();
		if (this.filePath != null) {
			JAXBContext context;
			try {
				context = getModelContent();
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				m.marshal(getModel(), this.outStream);
			} catch (JAXBException e) {
				JAXBExportJob.LOGGER.error(e.getMessage(), e);
				return Status.OK_STATUS;
			}
		} else {
			JAXBExportJob.LOGGER
					.error("Report cannot be exported: Invalid file path"); //$NON-NLS-1$
			return Status.CANCEL_STATUS;
		}
//		monitor.worked(2);

		FopFactory fopFactory = FopFactory.newInstance();
		ByteArrayOutputStream pdfoutStream = new ByteArrayOutputStream();

		StreamSource informationSource = new StreamSource(
				new ByteArrayInputStream(this.outStream.toByteArray()));

		
		try {

			this.xslfoTransformer = getxslTransformer(xslName);
			if (xslfoTransformer == null) {
				JAXBExportJob.LOGGER.error("Fop xsl: " + this.xslName + " not found"); //$NON-NLS-1$
				return Status.CANCEL_STATUS;
			}

			File pdfFile = new File(this.filePath);
			if (!pdfFile.exists()) {
				pdfFile.createNewFile();
			}
			if(monitor.isCanceled()){
				return Status.CANCEL_STATUS;
			}
//			this.xslfoTransformer.setParameter("page.layout", pageFormat);
			this.xslfoTransformer.setParameter("page.title", pdfTitle);
			try (OutputStream out = new BufferedOutputStream(
					new FileOutputStream(pdfFile));
					FileOutputStream str = new FileOutputStream(pdfFile);
					) {
				if (this.fileType.equals(org.apache.xmlgraphics.util.MimeConstants.MIME_PNG)) {	
					this.titleSize *=2;
					this.textSize *= 2;
					this.tableHeadSize *=2;
					
					float width = 2 *Float.parseFloat(fopFactory.getPageWidth().replace("in", ""));
					float height = 2 * Float.parseFloat(fopFactory.getPageHeight().replace("in", ""));
					if(pageFormat.equals(AbstractExportPage.A4_LANDSCAPE)){
						fopFactory.setPageWidth(height + "in");
						fopFactory.setPageHeight(width + "in");
					}else{
						fopFactory.setPageWidth(width + "in");
						fopFactory.setPageHeight(height + "in");
					}

					this.xslfoTransformer.setParameter("page.layout", "auto");
					this.xslfoTransformer.setParameter("title.size", this.titleSize);
					this.xslfoTransformer.setParameter("table.head.size", this.tableHeadSize);
					this.xslfoTransformer.setParameter("text.size", this.textSize);
					this.xslfoTransformer.setParameter("header.omit", "true"); //$NON-NLS-1$
//					this.getFirstDocumentSpan(this.xslfoTransformer,fopFactory);
				}else{
					this.xslfoTransformer.setParameter("page.layout", pageFormat);
					this.xslfoTransformer.setParameter("title.size", this.titleSize);
					this.xslfoTransformer.setParameter("table.head.size", this.tableHeadSize);
					this.xslfoTransformer.setParameter("text.size", this.textSize);
					this.xslfoTransformer.setParameter("header.omit", "false"); //$NON-NLS-1$
				}
//				
//				monitor.worked(4);
				Fop fop;
				FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
				foUserAgent.setOutputFile(pdfFile);
				fop = fopFactory.newFop(this.fileType, foUserAgent,
						pdfoutStream);
				
				SAXResult res = new SAXResult(fop.getDefaultHandler());

				// transform the informationSource with the transformXSLSource
				
				this.xslfoTransformer.transform(informationSource, res);
				
				str.write(pdfoutStream.toByteArray());
				str.close();
				
//				monitor.worked(5);
				if (pdfFile.exists() && this.enablePreview) {
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().open(pdfFile);
					}
				}
			}
		} catch (SAXException | IOException | TransformerException e) {
				ProjectManager.getLOGGER().error(e.getMessage());
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	private void getFirstDocumentSpan(Transformer xslTransformer, FopFactory fopFactory)
			throws TransformerException, FOPException {
		FOUserAgent userAgent = fopFactory.newFOUserAgent();
		StreamSource informationSource = new StreamSource(
				new ByteArrayInputStream(this.outStream.toByteArray()));
		ByteArrayOutputStream areaTreeStream = new ByteArrayOutputStream();
		Fop fop;

		// creae a new fop as areaTree
		fop = fopFactory.newFop(MimeConstants.MIME_FOP_AREA_TREE, userAgent,
				areaTreeStream);
		
		Result areaTreeResult = new SAXResult(fop.getDefaultHandler());
		// transform the informationSource with the transformXSLSource
		// the areaTreeResult is a complete description of the export dokument
		xslTransformer.transform(informationSource, areaTreeResult);

		StreamSource treeSource = new StreamSource(new ByteArrayInputStream(
				areaTreeStream.toByteArray()));

		AreaTreeModel treeModel = new AreaTreeModel();
		AreaTreeParser areaTreeParser = new AreaTreeParser();
		areaTreeParser.parse(treeSource, treeModel, userAgent);
		
		float pageHeight = 0;
		float addition;
		
		for(int i= 0;i < treeModel.getCurrentPageSequence().getPageCount();i++){
			Span span = (Span) treeModel.getCurrentPageSequence().getPage(i)
					.getBodyRegion().getMainReference().getSpans().get(0);
			addition = span.getBPD()/ JAXBExportJob.MP_TO_INCH;

			treeModel.getCurrentPageSequence().getPage(1).toString();
			pageHeight += addition;
		}
		fopFactory.setPageHeight(Float.toString(pageHeight + 1) + "in"); //$NON-NLS-1$
		

	}
	
	@Override
	protected void canceling() {
		if(this.xslfoTransformer != null){
			this.xslfoTransformer.reset();
		}
		super.canceling();
	}
	
	public void showPreview(boolean preview){
		this.enablePreview = preview;
	}

	/**
	 * one of the two constants {@link AbstractExportPage#A4_PORTRAIT} or {@link AbstractExportPage#A4_LANDSCAPE}
	 *  defined in AbstractExportPage
	 * 
	 * @param pageFormat the pageFormat to set
	 */
	public void setPageFormat(String pageFormat) {
		this.pageFormat = pageFormat;
	}

	/**
	 * @param pdfTitle the pdfTitle to set
	 */
	public void setPdfTitle(String pdfTitle) {
		this.pdfTitle = pdfTitle;
	}

	/**
	 * @return the model file from which the export informations are created,, implementers must
	 * 			take care that the returned object can be parsed to an xml file
	 * @see Marshaller#marshal(Object, OutputStream)
	 */
	protected abstract Object getModel();
	
	
	
	/**
	 * @return The class with which the jaxb instance is created 
	 */
	protected abstract JAXBContext getModelContent() throws JAXBException;

	protected abstract Transformer getxslTransformer(String resource) ;
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @param textSize the textSize to set
	 */
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	/**
	 * @param titleSize the titleSize to set
	 */
	public void setTitleSize(float titleSize) {
		this.titleSize = titleSize;
	}

	/**
	 * @param tableHeadSize the tableHeadSize to set
	 */
	public void setTableHeadSize(float tableHeadSize) {
		this.tableHeadSize = tableHeadSize;
	}
	
}
