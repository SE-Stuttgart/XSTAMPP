package xstampp.ui.common.projectsettings;

import java.util.UUID;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

import xstampp.ui.common.shell.ModalShell;

public interface ISettingsPage {
  Composite createControl(CTabFolder control, ModalShell parent, UUID modelId);

  boolean isVisible(UUID projectId);

  boolean validate();

  boolean doAccept();

  String getName();

  void setName(String name);

  /**
   * A with respect to all {@link ISettingsPage}'s unique identification String. 
   * @return a String that must <b>not be null</b>
   */
  String getId();
}
