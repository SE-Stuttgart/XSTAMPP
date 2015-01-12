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

import messages.Messages;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.area.AreaTreeModel;
import org.apache.fop.area.AreaTreeParser;
import org.apache.fop.area.Span;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.xml.sax.SAXException;

import xstampp.Activator;
import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.CSEditorWithPM;
import xstampp.astpa.model.DataModelController;
import xstampp.model.IDataModel;
import xstampp.ui.common.ViewContainer;

/**
 * Eclipse job that handles the export
 * 
 * @author Fabian Toth,Lukas Balzer
 * @since 2.0
 * 
 */
public class ExportJob extends Job {

	private static final float MP_TO_INCH = 72270f;
	private static final Logger LOGGER = Logger.getRootLogger();
	private String filePath;
	private ByteArrayOutputStream outStream;
	private final String fileType;
	private final String xslName;
	private final boolean asOne;
	public final UUID id;
	private final boolean decorate;

	/**
	 * Constructor of the export job
	 * 
	 * @author Fabian Toth, Lukas Balzer
	 * @param projectId
	 *            the project which
	 * 
	 * @param name
	 *            the name of the job
	 * @param filePath
	 *            the path to the pdf file
	 * @param xslName
	 *            the name of the file in which the xsl file is stored which
	 *            should be used
	 * @param type
	 *            the file type which shall be exported
	 * @param asOne
	 *            true if all content shall be exported on a single page
	 * @param decorate
	 *            if the control structure components should be pictured with
	 *            colored borders and image labels
	 */
	public ExportJob(UUID projectId, String name, String filePath, String type,
			String xslName, boolean asOne, boolean decorate) {
		super(name);
		this.decorate = decorate;
		this.id = projectId;
		this.filePath = filePath;
		this.fileType = type;
		this.xslName = xslName;
		this.asOne = asOne;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.ExportPdf, IProgressMonitor.UNKNOWN);
		String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		String csPath = workspacePath + File.separator
				+ Messages.ControlStructure + ".png";
		String csPmPath = workspacePath + File.separator
				+ Messages.ControlStructureDiagramWithProcessModel + ".png";

		CSExportJob csExport = new CSExportJob(csPath, CSEditor.ID, this.id,
				10, this.decorate);
		CSExportJob csPmExport = new CSExportJob(csPmPath, CSEditorWithPM.ID,
				this.id, 10, this.decorate);

		csExport.getPrintableRoot(monitor);
		csPmExport.getPrintableRoot(monitor);

		IDataModel model = ViewContainer.getContainerInstance().getDataModel(
				this.id);
		((DataModelController) model).setCSImagePath(csPath);
		((DataModelController) model).setCSPMImagePath(csPmPath);
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

		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

		ByteArrayOutputStream pdfoutStream = new ByteArrayOutputStream();

		StreamSource informationSource = new StreamSource(
				new ByteArrayInputStream(this.outStream.toByteArray()));

		/**
		 * the xslfoTransormer is beeing related to the xsl which describes the
		 * pdf export
		 */
		Transformer xslfoTransformer;
		try {
			URL xslUrl = this.getClass().getResource(this.xslName);

			if (xslUrl == null) {
				ExportJob.LOGGER.error("Fop xsl file not found"); //$NON-NLS-1$
				return Status.CANCEL_STATUS;
			}

			StreamSource transformXSLSource = new StreamSource(
					xslUrl.openStream());

			File pdfFile = new File(this.filePath);
			if (!pdfFile.exists()) {
				pdfFile.createNewFile();
			}

			TransformerFactory transfact = TransformerFactory.newInstance();
			transfact.setURIResolver(new URIResolver() {
				@Override
				public Source resolve(String href, String base) {
					return new StreamSource(this.getClass().getClassLoader()
							.getResourceAsStream("/" + href)); //$NON-NLS-1$
				}
			});
			xslfoTransformer = transfact.newTransformer(transformXSLSource);

			try (OutputStream out = new BufferedOutputStream(
					new FileOutputStream(pdfFile));
					FileOutputStream str = new FileOutputStream(pdfFile);) {
				if (this.asOne) {
					fopFactory.setPageHeight(this
							.getFirstDocumentSpan(xslfoTransformer));
				}

				Fop fop;
				fop = fopFactory.newFop(this.fileType, foUserAgent,
						pdfoutStream);
				Result res = new SAXResult(fop.getDefaultHandler());

				// transform the informationSource with the transformXSLSource
				xslfoTransformer.transform(informationSource, res);

				str.write(pdfoutStream.toByteArray());
				str.close();
				if (pdfFile.exists()) {
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

	private String getFirstDocumentSpan(Transformer xslTransformer)
			throws TransformerException, FOPException {
		FopFactory fopFactory = FopFactory.newInstance();
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
		Span span = (Span) treeModel.getCurrentPageSequence().getPage(0)
				.getBodyRegion().getMainReference().getSpans().get(0);
		float pageHeight = span.getBPD() / ExportJob.MP_TO_INCH;
		return Float.toString(pageHeight) + "in";//$NON-NLS-1$

	}

}
