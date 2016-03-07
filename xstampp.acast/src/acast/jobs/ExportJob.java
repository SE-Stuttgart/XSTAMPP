package acast.jobs;

import java.net.URL;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import acast.controller.Controller;

/**
 * Eclipse job that handles the export
 *
 * @author Fabian Toth,Lukas Balzer
 * @since 2.0
 *
 */
public class ExportJob extends xstampp.astpa.util.jobs.ExportJob {


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
		super(projectId, name, filePath, xslName, asOne, decorate);
	}

	@Override
	protected boolean canExport() {
		return false;
	}
	@Override
	protected Object getModel() {
		return ProjectManager.getContainerInstance().getDataModel(getProjectId());
	}

	@Override
	protected JAXBContext getModelContent() throws JAXBException {
		return JAXBContext.newInstance(Controller.class);
	}
	
	@Override
	protected Transformer getxslTransformer(String resource) {
		URL xslUrl = this.getClass().getResource(resource);

		if (xslUrl == null) {
			return null;
		}
		try {
			StreamSource transformXSLSource = new StreamSource(xslUrl.openStream());
			TransformerFactory transfact = TransformerFactory.newInstance();
			transfact.setURIResolver(new URIResolver() {
				@Override
				public Source resolve(String href, String base) {
					return new StreamSource(this.getClass().getClassLoader()
							.getResourceAsStream("/" + href)); //$NON-NLS-1$
				}
			});
			return transfact.newTransformer(transformXSLSource);
		} catch (Exception e) {
			return null;
		}
	}
}
