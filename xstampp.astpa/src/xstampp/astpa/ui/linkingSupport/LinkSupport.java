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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.model.interfaces.ILinkModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.contentassist.AutoCompleteField;
import xstampp.ui.common.contentassist.LinkProposal;

public abstract class LinkSupport<M extends ILinkModel> extends SelectionAdapter
    implements IContentProposalListener {
  private TableViewer displayedList;
  private UUID[] currentContent;
  private UUID currentLinkedId;
  private M dataInterface;
  private java.util.List<UUID> available;
  private List<Listener> changeListeners;
  private ObserverValue type;

  public LinkSupport(M dataInterface, ObserverValue type) {
    this.dataInterface = dataInterface;
    this.type = type;
    this.changeListeners = new ArrayList<>();
  }

  /**
   * Sets the id of the entry which's links are displayed in the {@link TableViewer} set in
   * {@link #setDisplayedList(TableViewer)}. This method updates the array obtained by
   * {@link #getCurrentContent()} as well as the {@link List} returned by {@link #getAvailable()}.
   * 
   * @param id
   *          an id that should serve to create linkings with.
   */
  public void update(UUID id) {
    currentLinkedId = id;
    currentContent = null;
    if (!this.displayedList.getTable().isDisposed()) {
      currentContent = fetch().toArray(new UUID[0]);
      this.displayedList.setInput(currentContent);
    }
  }

  /**
   * 
   * @param label
   *          the literal returned by {@link #getDescription(UUID)} of the abstract classes
   *          implementation.
   * @return the index of the linked item with the given description, or <b>null</b> if the label
   *         didn't match any current linked item
   */
  public final int indexOf(String label) {
    if (label != null) {
      for (int i = 0; i < currentContent.length; i++) {
        if (label.equals(getDescription(i))) {
          return i;
        }
      }
    }
    return -1;
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
    return currentLinkedId;
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
      getDataInterface().getLinkController().addLink(getLinkType(), getCurrentId(), uuid);
      notifyChangeListeners();
    }
  }

  /**
   * fetches the ID's of all items linked to the currently selected one.
   * 
   * @return a {@link List} of {@link UUID}'s
   */
  List<UUID> fetch() {
    return getDataInterface().getLinkController().getLinksFor(getLinkType(), getCurrentId());
  }

  /**
   * Deletes the link at given index of the widget from the data model.
   * 
   * @return whether the link was successfully removed or not.
   */
  public boolean unlink(int index) {
    UUID uuid = getCurrentContent()[index];
    boolean deleteLink = getDataInterface().getLinkController().deleteLink(getLinkType(),
        getCurrentId(), uuid);
    if (deleteLink) {
      notifyChangeListeners();
    }
    return deleteLink;
  }

  /**
   * Adds a {@link Listener} to the list of listeners that get notified whenever the list is
   * Modified meaning a link is added or removed.
   * 
   * @param listener
   *          A listener that should be called whenever the list of links is changed
   */
  public void addChangeListener(Listener listener) {
    this.changeListeners.add(listener);
  }

  private void notifyChangeListeners() {
    for (Listener listener : this.changeListeners) {
      Event event = new Event();
      event.widget = displayedList.getTable();
      listener.handleEvent(event);
    }
  }

  abstract java.util.List<UUID> getAvailable();

  public abstract String getText(UUID id);

  public abstract String getDescription(UUID id);

  public abstract String getTitle();

  public ObserverValue getLinkType() {
    return type;
  }

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