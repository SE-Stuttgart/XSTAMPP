package xstampp.ui.common.projectsettings;

import java.util.UUID;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

import xstampp.ui.common.ModalShell;

public interface ISettingsPage{
  Composite createControl(CTabFolder control, ModalShell parent, UUID modelId);
  boolean validate();
  boolean doAccept();
}
