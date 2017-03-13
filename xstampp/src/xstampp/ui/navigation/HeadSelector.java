package xstampp.ui.navigation;

import java.util.UUID;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import messages.Messages;
import xstampp.model.IDataModel;

public class HeadSelector implements IProjectSelection {

  @Override
  public boolean isEmpty() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void addOpenEntry(String id, IMenuManager manager) {
    // TODO Auto-generated method stub

  }

  @Override
  public UUID getProjectId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean expandTree(boolean expand, boolean first) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void changeItem(TreeItem item) {
    // TODO Auto-generated method stub

  }

  @Override
  public TreeItem getItem() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void activate() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setPathHistory(String pathHistory) {
    // TODO Auto-generated method stub

  }

  @Override
  public void cleanUp() {
    // TODO Auto-generated method stub

  }

  @Override
  public void addChild(IProjectSelection child) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setSelectionListener(Listener selectionListener) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getPathHistory() {
    return Messages.PlatformName + " -";
  }

  @Override
  public IDataModel getProjectData() {
    // TODO Auto-generated method stub
    return null;
  }

}
