package xstampp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class SerialationJob extends Job {

  private UUID projectID;
  private File saveFile;

  public SerialationJob(String name, File saveFile, UUID projectID) {
    super(name);
    this.saveFile = saveFile;
    this.projectID = projectID;
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    try {

      File serialContainer = new File(Platform.getInstanceLocation().getURL().toURI());
      serialContainer = new File(serialContainer, ".metadata");
      serialContainer = new File(serialContainer, ".plugins");
      serialContainer = new File(serialContainer, "xstampp");
      if (!serialContainer.exists()) {
        serialContainer.mkdirs();
      }
      serialContainer = new File(serialContainer, "." + projectID);
      if (!serialContainer.exists()) {
        serialContainer.createNewFile();
      }
      BufferedReader reader = new BufferedReader(new FileReader(saveFile));
      String buffer = new String();

      String line = reader.readLine();
      while (line != null) {
        buffer = buffer.concat(line);
        line = reader.readLine();
      }
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serialContainer));
      out.writeObject(buffer);
      out.close();
      reader.close();
    } catch (Exception e) {
      return Status.CANCEL_STATUS;
    }

    return Status.OK_STATUS;
  }

}
