package xstampp.astpa.ui.linkingSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.haz.ITableModel;
import xstampp.astpa.model.interfaces.ILinkModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.LinkProposal;

public abstract class LinkSupport<M extends ILinkModel> extends SelectionAdapter
    implements IContentProposalListener {
  private TableViewer displayedList;
  private UUID[] currentContent;
  private UUID currentId;
  private M dataInterface;
  private java.util.List<UUID> available;

  public LinkSupport(M dataInterface) {
    this.dataInterface = dataInterface;
  }

  public void update(UUID id) {
    currentId = id;
    currentContent = null;
    if (!this.displayedList.getTable().isDisposed()) {
      currentContent = fetch().toArray(new UUID[0]);
      this.displayedList.setInput(currentContent);
    }
  }

  final int indexOf(String label) {
    return available.indexOf(label);
  }

  public void setDisplayedList(TableViewer displayedList) {
    this.displayedList = displayedList;
  }

  UUID[] getCurrentContent() {
    return currentContent;
  }

  public M getDataInterface() {
    return dataInterface;
  }

  public UUID getCurrentId() {
    return currentId;
  }

  public String getDescription(int index) {
    if (getCurrentContent().length > index && index >= 0) {
      return getDescription(getCurrentContent()[index]);
    }
    return null;
  }


  @Override
  public void proposalAccepted(IContentProposal proposal) {

    int indexOf = indexOf(proposal.getLabel());
    if (indexOf > 0) {
      UUID uuid = getCurrentContent()[indexOf];
      getDataInterface().getLinkController().addLink(getLinkType(), getCurrentId(),
          uuid);
    }
  }

  List<UUID> fetch() {
    return getDataInterface().getLinkController().getLinksFor(getLinkType(),
        getCurrentId());
  }

  public void unlink(int index) {
    UUID uuid = getCurrentContent()[index];
    getDataInterface().getLinkController().deleteLink(getLinkType(), getCurrentId(),
        uuid);
  }

  abstract java.util.List<UUID> getAvailable();

  public abstract String getText(UUID id);

  public abstract String getDescription(UUID id);

  public abstract String getTitle();

  public abstract ObserverValue getLinkType();

  @Override
  public void widgetSelected(SelectionEvent e) {
    available = getAvailable();

    LinkProposal[] proposals = new LinkProposal[available.size()];
    for (int i = 0; i < proposals.length; i++) {
      LinkProposal proposal = new LinkProposal();
      proposal.setLabel(getText(available.get(i)));
      proposal.setDescription(getDescription(available.get(i)));
      proposals[i] = proposal;
    }

    AutoCompleteField scLinking = new AutoCompleteField(proposals,
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
    scLinking.setProposalListener(this);
    Point point = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
        .toDisplay(new Point(e.x, e.y));
    scLinking.setPopupPosition(point);
    scLinking.openPopup();
  }
}