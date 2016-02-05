package xstampp.astpa.util.jobs;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
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
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.xml.sax.SAXException;

import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.model.DataModelController;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.util.AbstractExportPage;
import xstampp.util.XstamppJob;

/**
 * Eclipse job that handles the export
 * 
 * @author Fabian Toth,Lukas Balzer
 * @since 2.0
 * 
 */
public class ExportJob extends XstamppJob implements IJobChangeListener {

	private boolean enablePreview = true;
	private static final float MP_TO_INCH = 72270f;
	private static final Logger LOGGER = Logger.getRootLogger();
	private String filePath;
	private ByteArrayOutputStream outStream;
	private final String fileType;
	private final String xslName;
	private final boolean asOne;
	private final UUID id;
	private float textSize,titleSize,tableHeadSize;
	private String pageFormat = AbstractExportPage.A4_PORTRAIT;

	private final boolean decorate;
	private String imgPath;
	private boolean isCsDirty;
	private File csPath;
	private File csPmPath;
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
	public ExportJob(UUID projectId, String name, String filePath, String xslName,
			boolean asOne, boolean decorate) {
		super(name,projectId);
		this.decorate = decorate;
		this.id = projectId;
		this.filePath = filePath;
		this.fileType = ProjectManager.getContainerInstance().getMimeConstant(filePath);
		this.xslName = xslName;
		this.asOne = asOne;
		this.tableHeadSize = 14;
		this.titleSize = 24;
		this.textSize = 12;
		this.isCsDirty = false;
		addJobChangeListener(this);
	}

	public void setCSImagePath(String path){
		File pathFile= new File(path);
		if(pathFile.exists()){
			this.imgPath = path;
		}
	}
	public void setCSDirty(){
		this.isCsDirty= true;
	}
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
			File tmp = new File(this.filePath);
			this.imgPath = tmp.getParent(); 
			
		this.csPath = new File(this.imgPath + File.separator
				+ getThread().getId() + "cs.png");
		this.csPmPath = new File(this.imgPath + File.separator
				+ getThread().getId() + "cspm.png");
		if(this.isCsDirty || !this.csPath.exists() || !this.csPmPath.exists()){
			CSExportJob csExport = new CSExportJob(this.csPath.getPath(), CSEditor.ID, this.id,
					10, this.decorate);
			CSExportJob csPmExport = new CSExportJob(this.csPmPath.getPath(), CSEditorWithPM.ID,
					this.id, 10, this.decorate);
	
			csExport.getPrintableRoot();
			csPmExport.getPrintableRoot();
		}
//		monitor.worked(1);

		IDataModel model = ProjectManager.getContainerInstance().getDataModel(
				this.id);
		((DataModelController) model).setCSImagePath(this.csPath.getPath());
		((DataModelController) model).setCSPMImagePath(this.csPmPath.getPath());
		// put the xml jaxb content into an output stream
		this.outStream = new ByteArrayOutputStream();
		if (this.filePath != null) {
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(DataModelController.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

				m.marshal(model, this.outStream);
			} catch (JAXBException e) {
				ExportJob.LOGGER.error(e.getMessage(), e);
				return Status.OK_STATUS;
			}
		} else {
			ExportJob.LOGGER
					.error("Report cannot be exported: Invalid file path"); //$NON-NLS-1$
			return Status.CANCEL_STATUS;
		}
//		monitor.worked(2);

		FopFactory fopFactory = FopFactory.newInstance();
		ByteArrayOutputStream pdfoutStream = new ByteArrayOutputStream();

		StreamSource informationSource = new StreamSource(
				new ByteArrayInputStream(this.outStream.toByteArray()));

		
		try {
			URL xslUrl = this.getClass().getResource(this.xslName);

			if (xslUrl == null) {
				ExportJob.LOGGER.error("Fop xsl: " + this.xslName + " not found"); //$NON-NLS-1$
				return Status.CANCEL_STATUS;
			}

			StreamSource transformXSLSource = new StreamSource(
					xslUrl.openStream());

			File pdfFile = new File(this.filePath);
			if (!pdfFile.exists()) {
				pdfFile.createNewFile();
			}

//			monitor.worked(3);
			TransformerFactory transfact = TransformerFactory.newInstance();
			transfact.setURIResolver(new URIResolver() {
				@Override
				public Source resolve(String href, String base) {
					return new StreamSource(this.getClass().getClassLoader()
							.getResourceAsStream("/" + href)); //$NON-NLS-1$
				}
			});
			if(monitor.isCanceled()){
				return Status.CANCEL_STATUS;
			}
			this.xslfoTransformer = transfact.newTransformer(transformXSLSource);
			this.xslfoTransformer.setParameter("page.layout", pageFormat);
			try (OutputStream out = new BufferedOutputStream(
					new FileOutputStream(pdfFile));
					FileOutputStream str = new FileOutputStream(pdfFile);) {
				if (this.fileType.equals(org.apache.xmlgraphics.util.MimeConstants.MIME_PNG)) {	
					this.titleSize *=2;
					this.textSize *= 2;
					this.tableHeadSize *=2;
					float width = Float.parseFloat(fopFactory.getPageWidth().replace("in", ""));
					fopFactory.setPageWidth(2 * width + "in");
					this.xslfoTransformer.setParameter("title.size", this.titleSize);
					this.xslfoTransformer.setParameter("table.head.size", this.tableHeadSize);
					this.xslfoTransformer.setParameter("text.size", this.textSize);
					this.xslfoTransformer.setParameter("header.omit", "true"); //$NON-NLS-1$
					this.getFirstDocumentSpan(this.xslfoTransformer,fopFactory);
//					float height = Float.parseFloat(fopFactory.getPageHeight().replace("in", ""));
//					fopFactory.setPageHeight(2 * height + "in");
				}else{
					this.xslfoTransformer.setParameter("title.size", this.titleSize);
					this.xslfoTransformer.setParameter("table.head.size", this.tableHeadSize);
					this.xslfoTransformer.setParameter("text.size", this.textSize);
					this.xslfoTransformer.setParameter("header.omit", "false"); //$NON-NLS-1$
				}
//				monitor.worked(4);
				Fop fop;
				FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
				fop = fopFactory.newFop(this.fileType, foUserAgent,
						pdfoutStream);
				
				Result res = new SAXResult(fop.getDefaultHandler());

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
			addition = span.getBPD()/ ExportJob.MP_TO_INCH;
			
			pageHeight += addition;
		}
		fopFactory.setPageHeight(Float.toString(pageHeight + 1) + "in"); //$NON-NLS-1$
		

	}
	
	@Override
	protected void canceling() {
		if(this.xslfoTransformer != null){
			this.xslfoTransformer.reset();
		}
		if(this.csPath != null){
			ExportJob.this.csPath.deleteOnExit();
		}
		if(this.csPmPath != null){
			ExportJob.this.csPmPath.deleteOnExit();
		}
		super.canceling();
	}
	/**
	 * @return the id
	 */
	public UUID getId() {
		return this.id;
	}
	
	public void showPreview(boolean preview){
		this.enablePreview = preview;
	}

	@Override
	public void done(IJobChangeEvent event) {
		ExportJob.this.csPath.delete();
		ExportJob.this.csPmPath.delete();
		super.done(event);
	}

	/**
	 * @param pageFormat the pageFormat to set
	 */
	public void setPageFormat(String pageFormat) {
		this.pageFormat = pageFormat;
	}

}
