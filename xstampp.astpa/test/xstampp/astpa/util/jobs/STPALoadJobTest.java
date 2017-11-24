package xstampp.astpa.util.jobs;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;

public class STPALoadJobTest {
  
  @Test
  public void loadTest() {
    STPALoadJob loadJob = new STPALoadJob();
    loadJob.setFile("Adaptive Cruise Control System.haz");
    loadJob.setSaveFile("Adaptive Cruise Control System.haz");
    loadJob.run(new NullProgressMonitor());
  }
  
}
