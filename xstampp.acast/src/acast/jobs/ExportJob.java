package acast.jobs;

import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import acast.controller.Controller;
import xstampp.ui.common.ProjectManager;
import xstampp.util.ExportPackage;

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
   *          the project which
   * @param name
   *          the name of the job
   * @param filePath
   *          the path to the pdf file
   * @param xslName
   *          the name of the file in which the xsl file is stored which should
   *          be used
   * @param asOne
   *          true if all content shall be exported on a single page
   * @param decorate
   *          if the control structure components should be pictured with
   *          colored borders and image labels
   */
  public ExportJob(UUID projectId, ExportPackage data, boolean asOne, boolean decorate) {
    super(projectId, data, asOne, decorate);
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
  protected Class<?> getResourceLoader() {
    return getClass();
  }
}
