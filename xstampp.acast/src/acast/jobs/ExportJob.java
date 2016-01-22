package acast.jobs;

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
import java.util.regex.Pattern;

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
import org.eclipse.core.runtime.Status;
import org.xml.sax.SAXException;

import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.util.XstamppJob;
import acast.controller.Controller;

/**
 * Eclipse job that handles the export
 *
 * @author Fabian Toth,Lukas Balzer
 * @since 2.0
 *
 */
public class ExportJob extends XstamppJob {

	private boolean enablePreview = true;
	private static final float MP_TO_INCH = 72270f;
	private static final Logger LOGGER = Logger.getRootLogger();
	private String filePath;
	private ByteArrayOutputStream outStream;
	private final String fileType;
	private final String xslName;
	private final boolean asOne;
	private final UUID id;
	private float textSize, titleSize, tableHeadSize;

	private final boolean decorate;
	private String imgPath;
	private boolean isCsDirty;

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
	public ExportJob(UUID projectId, String name, String filePath, String xslName, boolean asOne, boolean decorate) {
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
	}

	public void setCSImagePath(String path) {
		File pathFile = new File(path);
		if (pathFile.exists()) {
			this.imgPath = path;
		}
	}

	public void setCSDirty() {
		this.isCsDirty = true;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Messages.ExportPdf, 5);
		if (this.imgPath == null) {
			this.imgPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		}
		File csPath = new File(this.imgPath + File.separator + Messages.ControlStructure + ".png");

		if (this.isCsDirty || !csPath.exists()) {
			if (this.filePath.contains("images")) {
				String pattern = Pattern.quote(System.getProperty("file.separator"));
				String[] tmp = this.filePath.split(pattern);
				String path = tmp[0];
				for (int i = 1; i < tmp.length - 1; i++) {
					path = path + File.separator + tmp[i];
				}
				path = path + File.separator + Messages.ControlStructure + ".png";
				csPath = new File(path);
				CSExportJob csExport = new CSExportJob(path, "acast.steps.step2_1", this.id, 10, this.decorate);
				csExport.getPrintableRoot();
			} else {
				CSExportJob csExport = new CSExportJob(csPath.getPath(), "acast.steps.step2_1", this.id, 10,
						this.decorate);

				csExport.getPrintableRoot();
			}
		}
		monitor.worked(1);

		IDataModel model = ProjectManager.getContainerInstance().getDataModel(this.id);
		((Controller) model).setCSImagePath(csPath.getPath());
		// put the xml jaxb content into an output stream
		this.outStream = new ByteArrayOutputStream();
		if (this.filePath != null)

		{
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(Controller.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

				m.marshal(model, this.outStream);
			} catch (JAXBException e) {
				ExportJob.LOGGER.error(e.getMessage(), e);
				return Status.OK_STATUS;
			}
		} else

		{
			ExportJob.LOGGER.error("Report cannot be exported: Invalid file path"); //$NON-NLS-1$
			return Status.CANCEL_STATUS;
		}
		monitor.worked(1);

		FopFactory fopFactory = FopFactory.newInstance();
		ByteArrayOutputStream pdfoutStream = new ByteArrayOutputStream();

		StreamSource informationSource = new StreamSource(new ByteArrayInputStream(this.outStream.toByteArray()));

		/**
		 * the xslfoTransormer is beeing related to the xsl which describes the
		 * pdf export
		 */
		Transformer xslfoTransformer;
		try

		{
			URL xslUrl = this.getClass().getResource("/" + xslName);

			if (xslUrl == null) {
				ExportJob.LOGGER.error("Fop xsl file not found"); //$NON-NLS-1$
				return Status.CANCEL_STATUS;
			}

			StreamSource transformXSLSource = new StreamSource(xslUrl.openStream());

			File pdfFile = new File(this.filePath);
			if (!pdfFile.exists()) {
				pdfFile.createNewFile();
			}

			monitor.worked(1);
			TransformerFactory transfact = TransformerFactory.newInstance();
			transfact.setURIResolver(new URIResolver() {
				@Override
				public Source resolve(String href, String base) {
					return new StreamSource(this.getClass().getClassLoader().getResourceAsStream("/" + href)); //$NON-NLS-1$
				}
			});
			xslfoTransformer = transfact.newTransformer(transformXSLSource);

			try (OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
					FileOutputStream str = new FileOutputStream(pdfFile);) {
				if (this.fileType.equals(org.apache.xmlgraphics.util.MimeConstants.MIME_PNG)) {
					this.titleSize *= 2;
					this.textSize *= 2;
					this.tableHeadSize *= 2;
					float width = Float.parseFloat(fopFactory.getPageWidth().replace("in", ""));
					fopFactory.setPageWidth(2 * width + "in");
					xslfoTransformer.setParameter("title.size", this.titleSize);
					xslfoTransformer.setParameter("table.head.size", this.tableHeadSize);
					xslfoTransformer.setParameter("text.size", this.textSize);
					xslfoTransformer.setParameter("header.omit", "true"); //$NON-NLS-1$
					this.getFirstDocumentSpan(xslfoTransformer, fopFactory);
					// float height =
					// Float.parseFloat(fopFactory.getPageHeight().replace("in",
					// ""));
					// fopFactory.setPageHeight(2 * height + "in");
				} else {
					xslfoTransformer.setParameter("title.size", this.titleSize);
					xslfoTransformer.setParameter("table.head.size", this.tableHeadSize);
					xslfoTransformer.setParameter("text.size", this.textSize);
					xslfoTransformer.setParameter("header.omit", "false"); //$NON-NLS-1$
				}
				monitor.worked(1);
				Fop fop;
				FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
				fop = fopFactory.newFop(this.fileType, foUserAgent, pdfoutStream);

				Result res = new SAXResult(fop.getDefaultHandler());

				// transform the informationSource with the transformXSLSource

				xslfoTransformer.transform(informationSource, res);

				str.write(pdfoutStream.toByteArray());
				str.close();

				monitor.worked(1);
				if (pdfFile.exists() && this.enablePreview) {
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().open(pdfFile);
					}
				}
			}
		} catch (SAXException | IOException |

		TransformerException e)

		{
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;

	}

	private void getFirstDocumentSpan(Transformer xslTransformer, FopFactory fopFactory)
			throws TransformerException, FOPException {
		FOUserAgent userAgent = fopFactory.newFOUserAgent();
		StreamSource informationSource = new StreamSource(new ByteArrayInputStream(this.outStream.toByteArray()));
		ByteArrayOutputStream areaTreeStream = new ByteArrayOutputStream();
		Fop fop;

		// creae a new fop as areaTree
		fop = fopFactory.newFop(MimeConstants.MIME_FOP_AREA_TREE, userAgent, areaTreeStream);

		Result areaTreeResult = new SAXResult(fop.getDefaultHandler());
		// transform the informationSource with the transformXSLSource
		// the areaTreeResult is a complete description of the export dokument
		xslTransformer.transform(informationSource, areaTreeResult);

		StreamSource treeSource = new StreamSource(new ByteArrayInputStream(areaTreeStream.toByteArray()));

		AreaTreeModel treeModel = new AreaTreeModel();
		AreaTreeParser areaTreeParser = new AreaTreeParser();
		areaTreeParser.parse(treeSource, treeModel, userAgent);

		float pageHeight = 0;
		float addition;

		for (int i = 0; i < treeModel.getCurrentPageSequence().getPageCount(); i++) {
			Span span = (Span) treeModel.getCurrentPageSequence().getPage(i).getBodyRegion().getMainReference()
					.getSpans().get(0);
			addition = span.getBPD() / ExportJob.MP_TO_INCH;

			pageHeight += addition;
		}
		fopFactory.setPageHeight(Float.toString(pageHeight + 1) + "in"); //$NON-NLS-1$

	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return this.id;
	}

	public void showPreview(boolean preview) {
		this.enablePreview = preview;
	}
}
