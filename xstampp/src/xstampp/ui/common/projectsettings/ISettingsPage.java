package xstampp.ui.common.projectsettings;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

import xstampp.ui.common.shell.ModalShell;

import java.util.UUID;

public interface ISettingsPage {
  Composite createControl(CTabFolder control, ModalShell parent, UUID modelId);

  boolean isVisible(UUID projectId);
  
  boolean validate();

  boolean doAccept();
}
